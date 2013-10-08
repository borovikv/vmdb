package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.entities.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;
/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/8/13
 * Time: 3:41 PM
 */
public class ContactTest extends TestEntitiesBase {
    List<Email> emailList = new ArrayList<>();
    List<Phone> phones = new ArrayList<>();
    List<Url> urls = new ArrayList<>();

    @Before
    public void before(){
        for (int i = 0; i < 3; i++) {
            Email e = new Email();
            e.setEmail("email_"+i);
            emailList.add(e);
            session.beginTransaction();
            session.save(e);
            session.getTransaction().commit();
        }

        for (int i = 0; i < 3; i++) {
            Phone p = new Phone();
            p.setPhone("phone_"+i);
            phones.add(p);
            session.beginTransaction();
            session.save(p);
            session.getTransaction().commit();
        }

        for (int i = 0; i < 3; i++) {
            Url u = new Url();
            u.setUrl("url_"+i);
            urls.add(u);
            session.beginTransaction();
            session.save(u);
            session.getTransaction().commit();
        }

        Contact c = new Contact();
        c.setEmails(emailList);
        c.setUrls(urls);
        c.setPhones(phones);

        session.beginTransaction();
        session.save(c);
        session.getTransaction().commit();
    }

    @Test
    public void testEmail()  {
        Contact c = getContact();
        System.out.println(c.getEmails());
        assertArrayEquals(c.getEmails().toArray(), emailList.toArray());
    }

    @Test
    public void testPhone() {
        Contact c = getContact();
        System.out.println(c.getPhones());
        assertArrayEquals(c.getPhones().toArray(), phones.toArray());
    }

    @Test
    public void testUrl(){
        Contact c = getContact();
        System.out.println(c.getUrls());
        assertArrayEquals(c.getUrls().toArray(), urls.toArray());
    }

    private Contact getContact() {
        List<Contact> cs = session.createCriteria(Contact.class).list();
        assertFalse(cs.isEmpty());
        Contact c = cs.get(0);
        session.refresh(c);
        return c;  //To change body of created methods use File | Settings | File Templates.
    }


}
