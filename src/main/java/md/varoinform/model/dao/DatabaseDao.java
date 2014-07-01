package md.varoinform.model.dao;

import md.varoinform.model.entities.Database;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/1/14
 * Time: 2:10 PM
 */
public class DatabaseDao extends TransactionDaoHibernateImpl<Database, Long> {
    public DatabaseDao() {
        super(Database.class);
    }

    public boolean checkUID(String uid){
        Database db = read(1L);
        if (db == null) return false;
        String dbUid = db.getUid();
        return dbUid != null && dbUid.equalsIgnoreCase(uid);
    }
}
