package i18n;

import java.util.ListResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 11/11/13
 * Time: 3:56 PM
 */
@SuppressWarnings("UnusedDeclaration")
public class Strings_ru extends ListResourceBundle {
    private static final Object[][] contents = {
            {"treebranch", "товары и услуги"},
            {"selected", "избранное"},
            {"home", "домой"},
            {"back", "назад"},
            {"forward", "вперед"},
            {"search", "поиск"},
            {"advancedsearch‎", "расширенный поиск"},
            {"orderby", "упорядочить по"},
            {"print", "печать"},
            {"export", "экспорт"},
            {"mail", "отправить"},
            {"settings", "настройки"},
            {"result", "найдено"},
            {"contactperson", "контактное лицо" },
            {"entitytype", "тип" },
            {"creation", "дата создания" },
            {"foreingcapital", "иностранный капитал" },
            {"workplaces", "число рабочих" },
            {"brands", "бренды" },
            {"goods", "товары" },
            {"branches", "отрасли" },
            {"checkdate", "дата проверки" },
            {"postalcode", "почтовый код" },
            {"address", "адресс" },
            {"phones", "телефоны" },
            {"faxes", "факс" },
            {"emails", "email" },
            {"urls", "www" },
            {"null", "н/д"},
            {"true", "да"},
            {"false", "нет"},
            {"title", "название"},
            {"creationdate", "дата создания"},
            {"businessentitytype", "тип собственности"},
            {"registrationCode", "код регистарции"},
            {"licence", "лицензия"},
            {"register_by_internet", "регистр по инт"},
            {"register_by_phone", "регистр по телефону"},
            {"phone", "телефон" },
            {"internet", "интернет" },
            {"i_agree", "я согласен"},
            {"next", "далее"}

    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
