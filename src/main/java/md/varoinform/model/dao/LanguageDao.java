package md.varoinform.model.dao;

import md.varoinform.model.entities.Language;
import md.varoinform.model.util.ClosableSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/18/14
 * Time: 5:48 PM
 */
public class LanguageDao {
    public static Map<Long, String> getLanguageMap(){
         try (ClosableSession session = new ClosableSession()) {
             @SuppressWarnings("unchecked")
             List<Language> list = session.createCriteria(Language.class).setCacheable(false).list();
             Map<Long, String> map = new HashMap<>();
             for (Language language : list) {
                 map.put(language.getId(), language.getTitle());
             }
             return map;
         }
    }
}
