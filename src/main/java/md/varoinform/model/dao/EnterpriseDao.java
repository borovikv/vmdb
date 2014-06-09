package md.varoinform.model.dao;

import md.varoinform.controller.sorter.Sorter;
import md.varoinform.model.entities.Enterprise;
import md.varoinform.model.util.SessionManager;
import java.util.List;

public class EnterpriseDao {
   public static List<Enterprise> getEnterprises(){
        //noinspection unchecked
       List<Enterprise> enterprises = SessionManager.getSession().createCriteria(Enterprise.class).list();
       Sorter.sortEnterprises(enterprises);
       return enterprises;
    }
}