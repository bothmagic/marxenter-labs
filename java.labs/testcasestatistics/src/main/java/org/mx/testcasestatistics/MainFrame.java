/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mx.testcasestatistics;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author marxma
 */
public class MainFrame extends JFrame {

    private JXTable table;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public MainFrame() throws HeadlessException {
        super("Statistikauswertung");

        setLayout(new BorderLayout());


        table = new JXTable(new DefaultTableModel(new Object[][]{}, new String[]{"Datei", "Testfall", "Prio",
                    "Regressionstest", "Status", "Anzahl Schritte",
                    "Schritte ausgeführt", "Schritte erfolgreich",
                    "Schritte fehlerhaft", "Testdatum", "Tester", "Build",}));
        table.setColumnControlVisible(true);
        table.setColumnSelectionAllowed(true);
        new ExcelAdapter(table);

        add(new JScrollPane(table), BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadData();


    }

    private void loadData() {
        File file = null;

        //System.out.println(file.listFiles().length + " files found");
        List l = new ArrayList();
        PriorityQueue queue = new PriorityQueue();
        queue.add(new File("."));

        while (!queue.isEmpty()) {
            file = (File) queue.poll();
            queue.addAll(Arrays.asList(file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            })));
            for (File xlsFile : file.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.contains(".xls");
                }
            })) {

                //System.out.println("process " + xlsFile.getName());
                try {

                    Workbook w = new HSSFWorkbook(new FileInputStream(xlsFile));
                    System.out.println(w.getNumberOfSheets() + " available");

                    for (int i = 1; i < w.getNumberOfSheets(); i++) {
                        try {
                        Sheet sh = w.getSheetAt(i);
                        l.add(xlsFile.getName());
                        System.out.println("Sheetname: " + sh.getSheetName());
                        Row r1 = sh.getRow(0);
                        //System.out.println("TestCaseName: " + r1.getCell(2).getStringCellValue());
                        l.add(getStringValue(r1.getCell(2)));

                        Row r4 = sh.getRow(3);
                        //System.out.println("Prio: " + r4.getCell(6).getNumericCellValue());
                        l.add((int) getNumericValue(r4.getCell(6)));

                        Row r5 = sh.getRow(4);
                        //System.out.println("Regr: " + r5.getCell(6).getStringCellValue());
                        l.add(getStringValue(r5.getCell(6)));

//                    Row r6 = sh.getRow(5);
//                    System.out.println("Negtst: " + r6.getCell(6).getStringCellValue());
//                    l.add(getStringValue(r6.getCell(6)));

                        Row r3 = sh.getRow(2);
                        //System.out.println("State: " + r3.getCell(6).getStringCellValue());
                        l.add(getStringValue(r3.getCell(6)));


                        Row r15 = sh.getRow(14);
                        //System.out.println("Steps: " + r15.getCell(0).getNumericCellValue());
                        l.add((int) getNumericValue(r15.getCell(0)));


                        l.add((int) (getNumericValue(r15.getCell(3))
                                + getNumericValue(r15.getCell(4))
                                + getNumericValue(r15.getCell(5))));
                        //                    System.out.println("success Steps: " + r15.getCell(3).getNumericCellValue());
                        l.add((int) getNumericValue(r15.getCell(3)));
//                    System.out.println("failed Steps: " + r15.getCell(4).getNumericCellValue());
                        l.add((int) getNumericValue(r15.getCell(4)));
//                    System.out.println("unknown State Steps: " + r15.getCell(5).getNumericCellValue());
                        //l.add(getNumericValue(r15.getCell(5)));
//                    
                        Row r10 = sh.getRow(9);
//                    System.out.println("TEster: " + r10.getCell(2).getStringCellValue());
//                    System.out.println("Datum: " + r10.getCell(6).getDateCellValue());
                        l.add(dateFormat.format(getDateValue(r10.getCell(6))));
                        l.add(getStringValue(r10.getCell(2)));

                        Row r11 = sh.getRow(10);
                        //System.out.println("Build: " + r11.getCell(2).getStringCellValue());
                        //System.out.println("Duration: " + r11.getCell(6).getStringCellValue());
                        l.add(getStringValue(r11.getCell(2)));
                        l.add(getDateValue(r11.getCell(6)));
                        Row r12 = sh.getRow(11);
                        //System.out.println("Umgebung: " + r12.getCell(6).getStringCellValue());
                        l.add(getStringValue(r12.getCell(6)));

                        Row r13 = sh.getRow(12);
                        //System.out.println("Konfig: " + r13.getCell(6).getStringCellValue());
                        l.add(getStringValue(r13.getCell(6)));

                        ((DefaultTableModel) table.getModel()).addRow(l.toArray());
                        
                        } catch (IllegalStateException ex) {
                            
                        }
                        l.clear();
                    }


                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) throw new IllegalStateException();
        return cell.getStringCellValue();
    }

    private double getNumericValue(Cell cell) {
        if (cell == null) throw new IllegalStateException();
        return cell.getNumericCellValue();
    }

    private Object getDateValue(Cell cell) {
        if (cell == null) throw new IllegalStateException();
        try {
            return cell.getDateCellValue();
        } catch (IllegalStateException ex) {
            return null;
        }
    }
}
