package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.Contact;
import md.varoinform.modeltest.TestHibernateBase;
import md.varoinform.modeltest.searchengine.SearchTest;
import md.varoinform.modeltest.util.EntityCreator;
import org.junit.Before;
import org.junit.Test;

public class TestContact extends TestHibernateBase{
    public TestContact() {
    }

    @Before
    public void createContact(){
        //EntityCreator.createEnterprises();
    }
    @Test
    public void testContact() {
        Contact c = (Contact) SearchTest.session.createCriteria(Contact.class).list().get(0);
        System.out.println(c);
    }
}