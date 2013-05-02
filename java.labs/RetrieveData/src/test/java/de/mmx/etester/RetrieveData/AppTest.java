package de.mmx.etester.RetrieveData;

import de.mmx.etester.RetrieveData.etrest.CommandFactory;
import de.mmx.etester.RetrieveData.restdata.Project;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        CommandFactory.INSTANCE.setPassword("erdgas");
        CommandFactory.INSTANCE.setUrl("http://win28ecg.hv.vng/EnterpriseTester");
        CommandFactory.INSTANCE.setUser("Administrator");
        final List<Project> projectsCommand = CommandFactory.INSTANCE.getProjectsCommand();
        
        System.out.println("get " +projectsCommand.size() + " projects");
        
        for (Project p : projectsCommand) {
            System.out.println(p.getName());
        }
        
        
    }
}
