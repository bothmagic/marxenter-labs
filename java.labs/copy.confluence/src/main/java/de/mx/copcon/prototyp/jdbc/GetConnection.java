/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.copcon.prototyp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author marxma
 */
public class GetConnection {

    public static void main(String[] args) throws Exception {

        Class.forName("oracle.jdbc.OracleDriver");

        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@dbse.vngag.de:1521:egon", 
                "CONFLUENCE_ECG_INTERN", "conflu4intern!");
        // @//machineName:port/SID,   userid,  password
        try {
            Statement stmt = conn.createStatement();
            try {
                ResultSet rset = stmt.executeQuery("select BANNER from SYS.V_$VERSION");
                try {
                    while (rset.next()) {
                        System.out.println(rset.getString(1));   // Print col 1
                    }
                } finally {
                    try {
                        rset.close();
                    } catch (Exception ignore) {
                    }
                }
            } finally {
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {
            }
        }
    }
}

