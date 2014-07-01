package md.varoinform.controller;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 6/30/14
* Time: 12:28 PM
*/
public enum DefaultLanguages {
    EN("en"), RO("ro"), RU("ru");
    private final String title;

    DefaultLanguages(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static DefaultLanguages getLanguageByTitle(String languageTitle) {
        for (DefaultLanguages language : DefaultLanguages.values()) {
            if (languageTitle.startsWith(language.getTitle())) {
                return language;
            }
        }
        return getDefault();
    }

    public static DefaultLanguages getDefault() {
        return DefaultLanguages.RU;
    }


}
