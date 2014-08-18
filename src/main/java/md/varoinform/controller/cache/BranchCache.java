package md.varoinform.controller.cache;

import md.varoinform.controller.LanguageProxy;
import md.varoinform.controller.comparators.EnterpriseIDComparator;
import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Node;
import md.varoinform.model.entities.NodeTitle;
import md.varoinform.model.util.ClosableSession;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/14/14
 * Time: 9:30 AM
 */
public enum BranchCache {
    instance;

    private Map<Long, List<Long>> branchCache = new HashMap<>();
    private Map<Long, List<Long>> children = new HashMap<>();
    private Map<Long, Map<String, String>> branchTitles = new HashMap<>();
    private Set<Long> sorted = new HashSet<>();


    private BranchCache(){
        createBranchCache();
    }

    public void update() {
        sorted.clear();
    }

    public List<Long> getEnterpriseIdByNode(Long node){
        List<Long> ids = branchCache.get(node);
        if (!sorted.contains(node)) {
            Collections.sort(ids, new EnterpriseIDComparator());
        }
        return ids;
    }

    public String getTitle(Long id){
        Map<String, String> map = branchTitles.get(id);
        if (map == null) return "unnamed";
        return map.get(LanguageProxy.getCurrentLanguageTitle());
    }

    private void createBranchCache() {
        NodeDao nodeDao = new NodeDao();
        try (ClosableSession session = new ClosableSession()) {
            List<Node> nodes = nodeDao.getAll(session);
            for (Node node : nodes) {
                Long nodeId = node.getId();

                List<Long> entID = nodeDao.getEnterprisesID(node);
                branchCache.put(nodeId, entID);

                List<Long> childrenId = nodeDao.getChildrenID(nodeId);
                children.put(nodeId, childrenId);

                Map<String, String> titles = new HashMap<>();
                for (NodeTitle title : node.getTitles()) {
                    titles.put(title.getLanguage().getTitle(), title.getTitle());
                }
                branchTitles.put(nodeId, titles);
            }
        } catch (RuntimeException rex){
            rex.printStackTrace();
        }
    }


    public List<Long> getChildren(Long id){
        return children.get(id);
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
