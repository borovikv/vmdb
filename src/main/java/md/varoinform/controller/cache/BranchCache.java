package md.varoinform.controller.cache;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.product.Node;
import md.varoinform.model.entities.product.NodeTitle;
import md.varoinform.model.utils.DefaultClosableSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/14/14
 * Time: 9:30 AM
 */
public enum BranchCache {
    instance;

    private Map<Integer, List<Integer>> branchCache = new ConcurrentHashMap<>();
    private Map<Integer, Set<Integer>> children = new HashMap<>();
    private Map<Integer, Map<String, String>> branchTitles = new HashMap<>();

    BranchCache(){
        createBranchTitleCache();
        children.putAll(NodeDao.getArcs());
        branchCache.putAll(NodeDao.getNodeEnterpriseMap());

    }

    private void createBranchTitleCache() {
        try (DefaultClosableSession session = new DefaultClosableSession()) {
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

    public List<Integer> getEnterpriseIdByNode(Integer node){
        if  (node == 1L) {
            return Cache.instance.getAllEnterpriseIds();
        }
        return branchCache.get(node);
    }

    public String getTitle(Integer id){
        Map<String, String> map = branchTitles.get(id);
        if (map == null) return "unnamed";
        return map.get(LanguageProxy.getCurrentLanguageTitle());
    }

    public List<Integer> getChildren(Integer id){
        Set<Integer> children = this.children.get(id);
        if (children != null) {
            return new ArrayList<>(children);
        }
        return new ArrayList<>();
    }

    public List<Integer> startWith(String text){
        if (text == null || text.isEmpty()) return getChildren(1);
        List<Integer> result = new ArrayList<>();
        for (Integer id : branchTitles.keySet()) {
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
