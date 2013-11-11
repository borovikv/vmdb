package i18n;

import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/11/13
 * Time: 3:56 PM
 */
public class Strings_ru extends ListResourceBundle {
    private static final Object[][] contents = {
            {"treeBranch", "товары и услуги"},
            {"selected", "избранное"},
            {"home", "домой"},
            {"back", "назад"},
            {"forward", "вперед"},
            {"search", "поиск"},
            {"advancedSearch‎", "расширенный поиск"},
            {"orderBy", "упорядочить по"},
            {"print", "печать"},
            {"export", "экспорт"},
            {"mail", "отправить"},
            {"settings", "настройки"},
            {"result", "найдено"},
            {"", ""},
            {"", ""}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
