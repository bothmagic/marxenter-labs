/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.firstorientdb;

import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author marxma
 */
public class OrientDbTest {
    
    public OrientDbTest() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void crud() {
        
        //ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire("local:C:/temp/databases/petshop/petshop", "admin", "admin");
        
        ODatabaseDocumentTx db = new ODatabaseDocumentTx("remote:localhost:2424/testplayer-dev");
        db.open("admin", "admin");
        
        ODocument doc = new ODocument(db, "Person");
        doc.field("name", "Luke");
        doc.field("surname", "Skywalker");
        doc.field("city", 
                new ODocument(db, "City").field("name", "Rome").field("country", "Italy"));
        
        doc.save();
        
        ODocument doc1 = db.load(doc.getIdentity());
        
        assertEquals(doc.field("name"), doc1.field("name"));
        doc1.field("name", "Lukas");
        
        assertTrue(doc.field("name").equals(doc1.field("name")));
        
        //doc1.detach();
        
        doc1.field("name", "Laura");
        
        assertTrue(doc.field("name").equals(doc1.field("name")));
        
        
        ODocument refDoc = new ODocument(db, "Person");
        refDoc.field("name", "hallo");
        refDoc.save();
        
        ODocument refDoc1 = new ODocument(db, "Person");
        refDoc1.field("name", "hallo2");
        refDoc1.save();
        
        List<ORecord> l = new ArrayList<ORecord>();
        l.add(refDoc);
        l.add(refDoc1);
        
        doc.field("refdoc", l);
        
        doc.save();
        
        //System.out.println(refDoc.isEmbedded());
        
        System.out.println(((List<ODocument>)doc.field("refdoc")).get(0).field("name"));
        
        OCommandRequest cmd = db.command(new OSQLSynchQuery("select from Person"));
        
        System.out.println(cmd.execute());
        
        cmd = db.command(new OCommandSQL("find references 5:3"));
        
        System.out.println(cmd.execute());
        
        db.close();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
}
