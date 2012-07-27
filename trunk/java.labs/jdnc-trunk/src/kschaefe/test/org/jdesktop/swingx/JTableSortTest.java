/*======================================================================
                   WESTINGHOUSE ELECTRIC CORPORTATION
                       NUCLEAR SERVICES DIVISION
                  Westinghouse Proprietary Class II

     This document contains information proprietary to Westinghouse
     Electric Corporation; it is submitted in confidence and is to
     be used solely for the purpose for which it is furnished and
     returned upon request.  This document and such information is
     not to be reproduced, transmitted, disclosed or used otherwise
     in whole or in part without the authorization of Westinghouse
     Electric Corporation ESBU, NSD.
======================================================================*/
package org.jdesktop.swingx;

import java.util.Random;
import java.util.Vector;

import javax.swing.JTable;

import org.jdesktop.swingx.JXTable;

/**
 *
 */
public class JTableSortTest {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        Vector<String> names = new Vector<String>(2);
        names.add("Row");
        names.add("Random");
        
        JTable table = new JTable(getIntData(), names);
        table.setAutoCreateRowSorter(true);
        System.err.println("Sorting JTable (ints):");
        
        testJTable(table, 4);
        
        JXTable xTable = new JXTable(table.getModel());
        System.err.println("\nSorting JXTable (ints):");
        
        testJXTable(xTable, 4);
        
        table = new JTable(getStringData(), names);
        table.setAutoCreateRowSorter(true);
        System.err.println("\nSorting JTable (strings):");
        
        testJTable(table, 4);
        
        xTable = new JXTable(table.getModel());
        System.err.println("\nSorting JXTable (strings):");
        
        testJXTable(xTable, 4);
    }

    private static void testJTable(JTable table, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long time = System.currentTimeMillis();
            table.getRowSorter().toggleSortOrder(0);
            System.err.println(System.currentTimeMillis() - time + " ms");
        }
    }
    
    private static void testJXTable(JXTable table, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long time = System.currentTimeMillis();
            table.toggleSortOrder(0);
            System.err.println(System.currentTimeMillis() - time + " ms");
        }
    }
    
    private static Vector<Vector<Integer>> getIntData() {
        Random r = new Random();
        Vector<Vector<Integer>> data = new Vector<Vector<Integer>>(160000);
        
        for (int i = 0; i < 160000; i++) {
            Vector<Integer> row = new Vector<Integer>(2);
            
            row.add(i);
            row.add(r.nextInt());
            
            data.add(row);
        }
        
        return data;
    }

    private static Vector<Vector<String>> getStringData() {
        Random r = new Random();
        Vector<Vector<String>> data = new Vector<Vector<String>>(160000);
        
        for (int i = 0; i < 160000; i++) {
            Vector<String> row = new Vector<String>(2);
            
            row.add(i + "");
            row.add(r.nextInt() + "");
            
            data.add(row);
        }
        
        return data;
    }
}
