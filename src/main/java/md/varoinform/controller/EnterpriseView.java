package md.varoinform.controller;

import md.varoinform.controller.enterprisecomparators.ContactView;
import md.varoinform.model.entities.*;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/31/13
 * Time: 11:08 AM
 */
public class EnterpriseView extends EntityView {

    public static String getView(Enterprise enterprise) {
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise);
        return  getTable(enterpriseProxy);
    }

    public static String getTable(EnterpriseProxy enterpriseProxy) {
        ResourceBundle bundle = getResourceBundle();
        String table  = "<h1>" + enterpriseProxy.getTitle() + "</h1>" +
                "<table>" +
                ContactView.getContactTables(enterpriseProxy.getContactProxies()) +
                "<tr><th>" + bundle.getString("contactPerson") + "</th><td>" + getStringOrNA(enterpriseProxy.getContactPerson()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("entityType") + "</th><td>" + getStringOrNA(enterpriseProxy. getBusinessEntityType()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("creation") + "</th><td>" + getStringOrNA(enterpriseProxy.getCreationDate()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("foreingCapital") + "</th><td>" + getStringOrNA(enterpriseProxy.getForeingCapital()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("workplaces") + "</th><td>" + getStringOrNA(enterpriseProxy.getWorkplaces()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("brands") + "</th><td>" + getStringOrNA(enterpriseProxy.getBrands()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("goods") + "</th><td>" + getStringOrNA(enterpriseProxy.getGoods()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("branches") + "</th><td>" + getStringOrNA(enterpriseProxy.getBranches()) + "</td></tr>" +
                "<tr><th>" + bundle.getString("checkDate") + "</th><td>" + getStringOrNA(enterpriseProxy.getCheckDate()) + "</td></tr>" +
                enterpriseProxy.getAdvertisement() +
                "</table>";
        return "<html><body>" + table + "</body></html>";
    }

    public static String getCellView(Enterprise enterprise) {
        EnterpriseProxy enterpriseProxy = new EnterpriseProxy(enterprise);
        String result = "<html><body><h2>" + enterpriseProxy.getTitle() + "</h2><div class='contact'>" +
                ContactView.getCellViewContact(enterpriseProxy.getContactProxies()) +
        "</div></body></html>";
        return result;
    }




}
