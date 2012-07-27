/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt.data;

import junit.framework.Assert;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marxma
 */
public class TestTestCase {
    
    
    
    public TestTestCase() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testCreate() {    
        Session ss = ss = HibernateUtil.getSessionFactory().openSession();
        
        TestCase t = new TestCase();
        
        t.setName("TEst");
        String s = "";
        
        for (int i = 0; i < 100; i++) { s = s + "abc"; }
        
        t.setDescription(s);
        t.setPostcondition(s);
        t.setPrecondition(s);
        t.setResult(s);
        ss.save(t);
        ss.close();
        
        Assert.assertNotNull(t.getId());
        
        ss = HibernateUtil.getSessionFactory().openSession();
        TestCase t2 = (TestCase) ss.get(TestCase.class, t.getId());
        
        assertEquals(t.getId(), t2.getId());
        assertEquals(t.getName(), t2.getName());
        
        ss.delete(t2);
        ss.close();
        
    }
}
