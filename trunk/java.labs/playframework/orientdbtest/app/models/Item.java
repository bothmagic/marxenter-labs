package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.Version;
import play.data.validation.Required;
import play.modules.orientdb.Model;

public class Item extends Model {
    
    @SuppressWarnings("unused")
    @Id
    private Object id;

    @SuppressWarnings("unused")
    @Version
    private Object version;

    @Required
    public String name;

    public String description;
    
    public List<Item> children = new ArrayList<Item>();
    
}