package md.varoinform.view;

import md.varoinform.model.dao.GenericDaoHibernateImpl;
import md.varoinform.model.entities.*;
import md.varoinform.util.RenderTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/31/13
 * Time: 11:08 AM
 */
public class EnterpriseView {

    public static String getView(Enterprise enterprise, RenderTemplate render) {
        String result =  "<b><a href='http://google.com'>" + enterprise.title(getCurrentLanguage()) + "</a></b> <br />" +
                "" + getBranchName(enterprise);

        return render.renderTemplate(result);
    }

    private static String getBranchName(Enterprise enterprise) {
        StringBuilder branchNames = new StringBuilder();
        List<Branch> branches  = enterprise.branches();
        for (Branch branch : branches) {
            branchNames.append(branch.title(getCurrentLanguage()));
            branchNames.append("; ");
        }
        return branchNames.toString();
    }

    private static Language getCurrentLanguage() {
        Language language = new GenericDaoHibernateImpl<Language, Long>(Language.class).read(1L);
        return language;  //To change body of created methods use File | Settings | File Templates.
    }
}
