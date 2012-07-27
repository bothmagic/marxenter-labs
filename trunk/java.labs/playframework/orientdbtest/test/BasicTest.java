
import java.util.List;
import org.junit.*;
import play.test.*;
import models.*;
import play.modules.orientdb.Model;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void createAndRetrieveUser() {
       Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");

        //ODatabaseObjectTx db = ODB.openObjectDB();
        Item item = new Item();
        item.description = "Description";
        item.name = "Item578";
        // item.save();
        //db.save(item);

        //assertEquals(3, Item.count());
       /* for (Model it : Item.all()) {
            assertNotNull(it.getIdentity());
            assertEquals("Item578", ((Item) it).name);
        }*/

        List<Model> resList = Item.find("select * from Item where name = ?", "root");
        assertEquals(1, resList.size());
        Item.deleteAll();
    }
}
