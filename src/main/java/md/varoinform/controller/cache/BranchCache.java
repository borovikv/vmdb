package md.varoinform.controller.cache;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.NodeTitle;
import md.varoinform.model.util.ClosableSession;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/14/14
 * Time: 9:30 AM
 */
public enum BranchCache {
    instance;

    private Map<Long, List<Long>> branchCache = new ConcurrentHashMap<>();
    private Map<Long, List<Long>> children = new HashMap<>();
    private Map<Long, Map<String, String>> branchTitles = new HashMap<>();

    private BranchCache(){
        createBranchTitleCache();
        new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                Map<Long, List<Long>> cache = new HashMap<>();
                for (Long nodeID : branchTitles.keySet()) {
                    List<Long> eids = NodeDao.getEnterprisesID(nodeID);
                    cache.put(nodeID, eids);
                }
                branchCache.putAll(cache);
                return null;
            }
        }.execute();
    }

    private void createBranchTitleCache() {
        try (ClosableSession session = new ClosableSession()) {
            List<Node> nodes = NodeDao.getAll(session);
            for (Node node : nodes) {
                Map<String, String> titles = new HashMap<>();
                for (NodeTitle title : node.getTitles()) {
                    titles.put(title.getLanguage().getTitle(), title.getTitle());
                }
                branchTitles.put(node.getId(), titles);
            }
        } catch (RuntimeException rex){
            rex.printStackTrace();
        }
    }


    public void update() {
        children.clear();
        branchCache.clear();
        createBranchTitleCache();
    }

    public List<Long> getEnterpriseIdByNode(Long node){
        List<Long> ids = branchCache.get(node);
        if (ids == null){
            ids = NodeDao.getEnterprisesID(node);
            branchCache.put(node, ids);
        }
        return ids;
    }

    public String getTitle(Long id){
        Map<String, String> map = branchTitles.get(id);
        if (map == null) return "unnamed";
        return map.get(LanguageProxy.getCurrentLanguageTitle());
    }

    public List<Long> getChildren(Long id){
        List<Long> children = this.children.get(id);
        if (children != null) return children;

        List<Long> childrenId = NodeDao.getChildrenID(id);
        this.children.put(id, childrenId);
        return childrenId;
    }

    public List<Long> startWith(String text){
        if (text == null || text.isEmpty()) return getChildren(1L);
        List<Long> result = new ArrayList<>();
        for (Long id : branchTitles.keySet()) {
            Map<String, String> map = branchTitles.get(id);
            if (map != null){
                String title = map.get(LanguageProxy.getCurrentLanguageTitle());
                if (title != null && title.toLowerCase().startsWith(text)){
                    result.add(id);
                }
            }
        }
        return result;
    }


}
