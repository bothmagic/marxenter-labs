package de.mx.firstorientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTxPooled;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
//  OServer os = OServerMain.create();
//  os.startup(
//          
//   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//   + "<orient-server>"
//   + "<network>"
//   + "<protocols>"
//   + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
//   + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
//   + "</protocols>"
//   + "<listeners>"
//   + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
//   + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
//   + "</listeners>"
//   + "</network>"
//   + "<users>"
//   + "<user name=\"root\" password=\"test\" resources=\"*\"/>"
//   + "</users>"
//   + "<properties>"
//   + "<entry name=\"orientdb.www.path\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/\"/>"
//   + "<entry name=\"orientdb.config.file\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml\"/>"
//   + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
//   + "<entry name=\"log.console.level\" value=\"info\"/>" + "<entry name=\"log.file.level\" value=\"fine\"/>"
//   + "</properties>" + "</orient-server>");
//   
//    os.shutdown();
//        
        
        ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire("local:C:/temp/databases/petshop/petshop", "admin", "admin");
        
        db.open("admin", "admin");
        
        ODocument doc = new ODocument(db, "Person");
        doc.field("name", "Luke");
        doc.field("surname", "Skywalker");
        doc.field("city", 
                new ODocument(db, "City").field("name", "Rome").field("country", "Italy"));
        
        
        for (ODocument d : db.browseClass("Person")) {
            
            System.out.println(d.field("name"));
            System.out.println(d.getIdentity().toString());
        }
        
        doc.save();
        
        db.close();
    }
}
