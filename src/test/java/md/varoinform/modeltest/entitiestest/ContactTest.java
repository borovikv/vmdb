package md.varoinform.modeltest.entitiestest;

import md.varoinform.model.dao.TransactionDaoHibernateImpl;
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
    private List<Email> emails = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();
    private List<Url> urls = new ArrayList<>();
    private TransactionDaoHibernateImpl<Contact, Long> contactDao;
    private TransactionDaoHibernateImpl<Url, Long> urlDao;
    private TransactionDaoHibernateImpl<Phone, Long> phoneDao;
    private TransactionDaoHibernateImpl<Email, Long> emailDao;

    public ContactTest() {
        contactDao = new TransactionDaoHibernateImpl<Contact, Long>(Contact.class);
        urlDao = new TransactionDaoHibernateImpl<Url, Long>(Url.class);
        phoneDao = new TransactionDaoHibernateImpl<Phone, Long>(Phone.class);
        emailDao = new TransactionDaoHibernateImpl<Email, Long>(Email.class);
    }

    @Before
    public void before(){
        emails = createEmails();
        phones = createPhones();
        urls = createUrls();
        createContact(emails, urls, phones);
    }

    private void createContact(List<Email> emails, List<Url> urls, List<Phone> phones) {
        Contact c = new Contact();
        c.setEmails(emails);
        c.setUrls(urls);
        c.setPhones(phones);
        contactDao.create(c);
    }

    private List<Url> createUrls() {
        List<Url> urls = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Url u = new Url();
            u.setUrl("url_"+i);
            urls.add(u);
            urlDao.create(u);
        }
        return urls;
    }

    private List<Phone> createPhones() {
        List<Phone> phones = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Phone p = new Phone();
            p.setPhone("phone_"+i);
            phones.add(p);
            phoneDao.create(p);
        }
        return phones;
    }

    private List<Email> createEmails() {
        List<Email> emails = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Email e = new Email();
            e.setEmail("email_"+i);
            emails.add(e);
            emailDao.create(e);
        }
        return emails;
    }

    @Test
    public void testEmail()  {
        Contact c = contactDao.read(1L);
        System.out.println(c.getEmails());
        assertArrayEquals(c.getEmails().toArray(), emails.toArray());
    }

    @Test
    public void testPhone() {
        Contact c = contactDao.read(1L);
        System.out.println(c.getPhones());
        assertArrayEquals(c.getPhones().toArray(), phones.toArray());
    }

    @Test
    public void testUrl(){
        Contact c = contactDao.read(1L);;
        System.out.println(c.getUrls());
        assertArrayEquals(c.getUrls().toArray(), urls.toArray());
    }




}
