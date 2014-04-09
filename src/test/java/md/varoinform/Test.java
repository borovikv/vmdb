package md.varoinform;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.*;
import md.varoinform.model.entities.convert.Branch;
import md.varoinform.model.entities.convert.Good;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/7/14
 * Time: 10:36 AM
 */
public class Test {
    public static void main(String[] args) {
        Class [] classes = new Class[]{Branch.class,  Good.class,};
        Class [] c2 = new Class[] {TreeNode.class, NodeTitleContainer.class, Good2.class};
        System.out.println(count(classes));
        System.out.println(count(c2));

    }

    private static int count(Class[] classes) {
        int c1 = 0;
        for (Class aClass : classes) {
            c1 += getSize(aClass);
        }
        return c1;
    }

    private static<T> int getSize(Class<T> c){
        GenericDaoHibernateImpl<T, Long> dao = new GenericDaoHibernateImpl<>(c);
        return dao.getAll().size();
    }
}
