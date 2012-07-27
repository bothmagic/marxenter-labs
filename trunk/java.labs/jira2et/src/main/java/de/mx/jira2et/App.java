package de.mx.jira2et;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreePath;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Stack<File> stack = new Stack<File>();
        final List<File> files = new ArrayList<File>();

        stack.add(new File("."));

        while (!stack.isEmpty()) {
            File pop = stack.pop();
            stack.addAll(Arrays.asList(pop.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            })));

            files.addAll(Arrays.asList(pop.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.contains(".xls");
                }
            })));
        }

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(
              new FileWriter("importet.csv"), ',');
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        writer.writeNext(new String[]{"ExtId", "Name", "Beschreibung", "Voraussetzungen", "Type", "State", "Stepnum", "Stepbeschr", "Stepresult"});

        
        for (File file : files) {

            try {
                Workbook w = new HSSFWorkbook(new FileInputStream(file));
                System.out.println("run file " + file.getName());
                Sheet sh = w.getSheetAt(0);
                
                for (Iterator<Row> it = sh.rowIterator(); it.hasNext();) {
                    String[] line = new String[9];
                    Row row = it.next();
                    
                    if ("manueller Test".equals(row.getCell(0).getStringCellValue())) {
                        line[0] = row.getCell(1).getStringCellValue();
                        line[1] = row.getCell(4).getStringCellValue().replaceAll("Test: [KapMan2011] ", "")
                                .trim().replace("Test: ", "").trim().replace("[KapMan2011] - ", "")
                                .replace("[KapMan2011] ", "").replace("[KapMan 2011] ", "").replace("[Kapman 2011] ", "").replace("[Kapman2011] ", "")
                                .replace("Test: ", "");
                        line[2] = row.getCell(5).getStringCellValue().trim();
                        if (line[2].isEmpty()) line[2] = null;
                        line[3] = row.getCell(6).getStringCellValue().trim();
                        line[4] = "Functional";
                        line[5] = "Approved";
                        line[6] = "1";
                        line[7] = row.getCell(7).getStringCellValue().trim();
                        line[8] = row.getCell(8).getStringCellValue().trim();
                        writer.writeNext(line);
                    }
                    
                }
                
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {//nop
                
            }

            
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
