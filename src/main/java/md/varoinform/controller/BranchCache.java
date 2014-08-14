package md.varoinform.controller;

import md.varoinform.model.dao.NodeDao;
import md.varoinform.model.entities.Node;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/14/14
 * Time: 9:30 AM
 */
public enum BranchCache {
    instance;
    private static final boolean isBranchCached = true;
    private Map<Long, List<Long>> branchCache = new HashMap<>();

    public void update(){
        branchCache.clear();
    }

    public List<Long> getEnterpriseIdByNode(Node node){
        if (isBranchCached){
            return getCachedEnterpriseIdsByNode(node);
        } else {
            return new NodeDao().threadSafeGetEntID(node);
        }
    }

    private List<Long> getCachedEnterpriseIdsByNode(Node node) {
        if (branchCache.isEmpty() && !Holder.await()) {
            List<Long> ids = new NodeDao().getEnterpriseIds(node);

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {

                    try (Holder ignored = new Holder()) {
                        branchCache = createBranchCache();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            worker.execute();


            return ids;
        } else if (branchCache.containsKey(node.getId())){

            return branchCache.get(node.getId());
        } else {

            return new NodeDao().getEnterpriseIds(node);
        }
    }

    private Map<Long, List<Long>> createBranchCache() {
        NodeDao nodeDao = new NodeDao();
        List<Node> nodes = nodeDao.getAll();
        Map<Long, List<Long>> bc = new HashMap<>();
        for (Node node : nodes) {
            List<Long> ids = nodeDao.threadSafeGetEntID(node);
            bc.put(node.getId(), ids);
        }
        return bc;
    }

}
