import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;
import play.modules.cream.ocm.JcrQueryResult;
import play.modules.cream.ocm.JcrVersionMapper;

public class RequirementTest extends UnitTest {

    @Test
    public void createRequirement() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
        
        JcrQueryResult<TestProject> alltp =  TestProject.all();
        assertEquals(2, alltp.count());
        
        TestProject tp1 = alltp.fetch().get(0);
        
        RepoRequirement rpoRepoRequirement = new RepoRequirement();
        rpoRepoRequirement.name = "mein Repo";
        rpoRepoRequirement.description = "description";
        
        tp1.repoRequirements = new ArrayList<RepoRequirement>();
        tp1.repoRequirements.add(rpoRepoRequirement);
        tp1.save();
        
        tp1 = TestProject.get("/testproject/mein_Testprojekt");
        assertEquals(1, tp1.repoRequirements.size());
        
        for (TestProject tp : alltp.fetch()) {
            tp.delete();
        }
        
    }

}
