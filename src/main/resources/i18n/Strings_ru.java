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
            {"contactPerson", "контактное лицо" },
            {"entityType", "тип" },
            {"creation", "дата создания" },
            {"foreingCapital", "иностранный капитал" },
            {"workplaces", "число рабочих" },
            {"brands", "бренды" },
            {"goods", "товары" },
            {"branches", "отрасли" },
            {"checkDate", "дата проверки" },
            {"postalCode", "почтовый код" },
            {"address", "адресс" },
            {"phone", "телефон" },
            {"fax", "факс" },
            {"email", "email" },
            {"urls", "www" },
            {"null", "н/д"},
            {"true", "да"},
            {"false", "нет"}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
