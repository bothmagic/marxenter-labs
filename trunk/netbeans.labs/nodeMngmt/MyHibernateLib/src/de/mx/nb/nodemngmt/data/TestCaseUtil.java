/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt.data;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author marxma
 */
public class TestCaseUtil {
    
    
    
    public static List<TestCase> getAllTestCases() {
    
        Session ss = HibernateUtil.getSessionFactory().openSession();
        
        Query q = ss.createQuery("from TestCase");
        List<TestCase> result = q.list();
        ss.close();
        
        return result;
    }
    
    public static TestCase create(TestCase testCase) {
        
        Session ss = HibernateUtil.getSessionFactory().openSession();
        ss.save(testCase);
        ss.close();
        return testCase;
    }
    
}
