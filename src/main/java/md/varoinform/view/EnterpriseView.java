package md.varoinform.view;

import md.varoinform.controller.LanguageProxy;
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
        Language currentLanguage = LanguageProxy.getInstance().getCurrentLanguage();

        String result =  "<b><a href='http://google.com'>" + enterprise.title(currentLanguage) + "</a></b> <br />" +
                "" + getBranchName(enterprise) + "<br />";


        return render.renderTemplate(result);
    }

    private static String getBranchName(Enterprise enterprise) {
        StringBuilder branchNames = new StringBuilder();
        List<Branch> branches  = enterprise.branches();
        for (Branch branch : branches) {
            branchNames.append(branch.title(LanguageProxy.getInstance().getCurrentLanguage()));
            branchNames.append("; ");
        }
        return branchNames.toString();
    }


}
