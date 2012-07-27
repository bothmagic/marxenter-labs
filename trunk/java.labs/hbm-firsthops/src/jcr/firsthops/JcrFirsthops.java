/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jcr.firsthops;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.hibernate.Session;

/**
 *
 * @author marxma
 */
public class JcrFirsthops {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hbm-firsthopsPU");
        
EntityManager em = emf.createEntityManager(); // Retrieve an application managed entity manager


EntityTransaction transaction = em.getTransaction();
transaction.begin();

em.createNativeQuery( "SET @a = 1;" ).executeUpdate();

transaction.commit();

em.close();
emf.close(); //close at application end
        
        
    }
}
