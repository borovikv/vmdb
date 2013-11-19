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
            {"phones", "телефон" },
            {"faxes", "факс" },
            {"emails", "email" },
            {"urls", "www" },
            {"null", "н/д"},
            {"true", "да"},
            {"false", "нет"},
            {"title", "название"},
            {"creationdate", "дата создания"},
            {"businessentitytype", "тип собственности"}
            /*
            foreingcapital, region, sector, workplaces, postalcode, urls, officenumber, street, goods, emails,
             housenumber, creationdate, phones, country, contactperson, brands, title, ypurl, checkdate,
             topadministrativeunit, address, faxes, town, businessentitytype, branches

             */
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
