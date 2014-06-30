package md.varoinform.view.dialogs.registration;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/30/14
 * Time: 1:29 PM
 */
public enum RegistrationType {
    INTERNET("by_internet"), PHONE("by_phone");

    private final String title;

    RegistrationType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
