package md.varoinform.controller;

import md.varoinform.controller.entityproxy.EnterpriseProxy;
import md.varoinform.model.dao.DAOTag;
import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.Tag;

import javax.swing.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum Cache {
    instance;
    private final Map<Long, Enterprise> enterpriseCache = new LinkedHashMap<>();
    private final Map<Long, List<Long>> branchCache = new HashMap<>();
    private final Map<Long, List<EnterpriseProxy>> proxyCache = new HashMap<>();
    private final Set<Tag> tags = new TreeSet<>();
    private final DAOTag daoTag = new DAOTag();

    Cache() {
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
        for (Enterprise enterprise : enterprises) {
            Long id = enterprise.getId();
            enterpriseCache.put(id, enterprise);
            List<Language> languages = LanguageProxy.instance.getLanguages();
            List<EnterpriseProxy> proxies = new ArrayList<>(languages.size());
            for (Language language : languages) {
                proxies.add(new EnterpriseProxy(enterprise, language));
            }
            proxyCache.put(id, proxies);
        }
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.getEnterpriseIds(node);
            branchCache.put(node.getId(), ids);
        }

        tags.addAll(daoTag.getAll());
    }

    public List<Enterprise> getEnterprises(List<Long> ids){
        List<Enterprise> result = new ArrayList<>();
        for (Long id : ids) {
            Enterprise enterprise = enterpriseCache.get(id);
            if (enterprise != null){
                result.add(enterprise);
            }
        }
        return result;
    }

    public List<Enterprise> getAllEnterprises(){
        return new ArrayList<>(enterpriseCache.values());
    }

    public List<Enterprise> getEnterpriseByNode(Node node){
        List<Long> ids = branchCache.get(node.getId());
        if (ids == null) return new ArrayList<>();
        return getEnterprises(ids);
    }

    @SuppressWarnings("UnusedDeclaration")
    public List<EnterpriseProxy> getProxys(List<Enterprise> enterprises, Language language){
        List<EnterpriseProxy> result = new ArrayList<>();
        int index = LanguageProxy.instance.getLanguages().indexOf(language);
        for (Enterprise enterprise : enterprises) {
            List<EnterpriseProxy> proxies = proxyCache.get(enterprise.getId());
            if (proxies != null){
                result.add(proxies.get(index));
            }
        }
        return result;
    }

    public EnterpriseProxy getProxy(Enterprise enterprise, Language language){
        int index = LanguageProxy.instance.getLanguages().indexOf(language);
        List<EnterpriseProxy> proxies = proxyCache.get(enterprise.getId());
        if (proxies != null){
            return proxies.get(index);
        }
        return null;
    }

    public EnterpriseProxy getProxy(Enterprise enterprise){
        return getProxy(enterprise, LanguageProxy.instance.getCurrentLanguage());
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public void saveTag(final Tag tag){
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                daoTag.save(tag);
                return null;
            }
        }.execute();
    }

    public void createTag(String title, List<Enterprise> enterprises) {
        Tag tag = daoTag.createTag(title, enterprises);
        if (tag == null) return;
        tags.add(tag);
        saveTag(tag);
    }

    public void delete(final Tag tag) {
        tags.remove(tag);
        new SwingWorker<Void, Void>(){

            @Override
            protected Void doInBackground() throws Exception {
                daoTag.delete(tag);
                return null;
            }
        }.execute();
    }
}
