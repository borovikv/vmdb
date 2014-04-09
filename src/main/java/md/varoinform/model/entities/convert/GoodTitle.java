package md.varoinform.model.entities.convert;

import md.varoinform.model.entities.Language;
import md.varoinform.model.entities.Title;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 10/7/13
 * Time: 4:16 PM
 */
@Entity
@Table(name = "EXPORTED_DB.DB_goodtitle")
public class GoodTitle extends Title<Good> {
}

