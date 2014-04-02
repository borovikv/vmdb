package md.varoinform.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EXPORTED_DB.DB_topadministrativeunit")
public class TopAdministrativeUnit extends TitleContainer<TopAdministrativeUnitTitle> {
}
