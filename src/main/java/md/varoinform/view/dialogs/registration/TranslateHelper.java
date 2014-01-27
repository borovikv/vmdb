package md.varoinform.view.dialogs.registration;

import md.varoinform.util.ResourceBundleHelper;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 1/24/14
 * Time: 3:32 PM
 */
public enum TranslateHelper {
    instance;

    private static final String[] LANGUAGES = new String[]{"en", "ru", "ro"};
    private static int currentIndex = getDefaultIndex();

    private static int getDefaultIndex() {
        return 1;
    }

    public static String[] getLanguages() {
        return Arrays.copyOf(LANGUAGES, LANGUAGES.length);
    }

    public String getText(String key, String def){
        String language = LANGUAGES[currentIndex];
        String text = getTranslatedText(language, key);
        if (text.isEmpty()){
            text = findText(key, def);
        }
        return text;
    }

    private String getTranslatedText(String language, String key) {
        return ResourceBundleHelper.getString(language, key, "");
    }

    private String findText(String key, String def) {
        for (String language : LANGUAGES) {
            String text = getTranslatedText(language, key);
            if (!text.isEmpty()) return text;
        }
        return def;
    }

    public void setCurrentLanguage(String title){
        for (int i = 0; i < LANGUAGES.length; i++) {
            if (title.equalsIgnoreCase(LANGUAGES[i])){
                currentIndex = i;
            }
        }
    }

    public String getCurrentLanguage(){
        return LANGUAGES[currentIndex];
    }
}
