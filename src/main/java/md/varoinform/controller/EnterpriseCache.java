package md.varoinform.controller;

import md.varoinform.model.dao.EnterpriseDao;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.util.Profiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/11/14
 * Time: 10:05 AM
 */
public enum EnterpriseCache {
    cache;
    private final Map<Long, Enterprise> map = new LinkedHashMap<>();

    EnterpriseCache() {
        List<Enterprise> enterprises = EnterpriseDao.getEnterprises();
        for (Enterprise enterprise : enterprises) {
            map.put(enterprise.getId(), enterprise);
        }
    }

    public List<Enterprise> get(List<Long> ids){
        List<Enterprise> result = new ArrayList<>();
        for (Long id : ids) {
            Enterprise enterprise = map.get(id);
            if (enterprise != null){
                result.add(enterprise);
            }
        }
        return result;
    }

    public List<Enterprise> getAll(){
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        List<Long> ids = new ArrayList<>();
        for (long i = 0; i < 9000; i++) {
            ids.add(i);
        }
        Profiler p = new Profiler();
        List<Enterprise> enterprises = cache.get(ids);
        p.end();
        System.out.println(enterprises.size());
    }
}
