/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mx.testcasestatistics;

import java.awt.BorderLayout;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 * @author marxma
 */
public class NewMainFrame extends javax.swing.JFrame {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    JXTable table;

    /**
     * Creates new form NewMainFrame
     */
    public NewMainFrame() {
        initComponents();

        table = new JXTable(new DefaultTableModel(new Object[][]{}, new String[]{"Datei", "Testfall", "Prio",
                    "Reg.tst.", "Neg.tst", "Status", "Anzahl Schritte",
                    "Schritte ausgeführt", "Schritte erfolgreich",
                    "Schritte fehlerhaft", "Testdatum", "Tester", "Build",}));

        table.setColumnControlVisible(true);
        table.setColumnSelectionAllowed(true);
        
        jTable1.setColumnSelectionAllowed(true);
        new ExcelAdapter(table);
        new ExcelAdapter(jTable1);
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(new JScrollPane(table), BorderLayout.CENTER);

        folderTree.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {

                final List<String> filterList = new ArrayList<String>();
                final List<File> files = new ArrayList<File>();

                Stack<File> stack = new Stack<File>();
                table.getSelectionModel().clearSelection();
                for (int row : folderTree.getSelectedRows()) {
                    TreePath path = folderTree.getPathForRow(row);
                    DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) path.getLastPathComponent();
                    stack.add(((FileWrapper) node.getUserObject()).file);

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
                }

                for (File file : files) {
                    filterList.add(file.getName());
                }

                table.setRowFilter(new RowFilter<DefaultTreeTableModel, Integer>() {

                    @Override
                    public boolean include(Entry<? extends DefaultTreeTableModel, ? extends Integer> entry) {
                        
                        if (filterList.contains(entry.getStringValue(0))) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });


        loadData();
        setSize(800, 600);
        //extractData();

    }

    private void extractData() {

        new ExtractDataDialog(this, true).setVisible(true);

    }

    private void loadData() {
        File file = null;

        //System.out.println(file.listFiles().length + " files found");
        List l = new ArrayList();
        PriorityQueue queue = new PriorityQueue();
        queue.add(new File("."));
        
        DefaultTreeTableModel model =
                new DefaultTreeTableModel(
                new DefaultMutableTreeTableNode(new FileWrapper(new File("."), "Alle")), Arrays.asList(new String[]{"Verzeichnisse"}));

        folderTree.setTreeTableModel(model);
        folderTree.setRootVisible(true);

        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) model.getRoot();

        buildFolderStructure(node, new File("."));
        
        
        int tstCount = 0;
        int tstCountOpen = 0;
        int tstCountWork = 0;
        int tstCountError = 0;
        int tstCountSuccess = 0;
        int stepsCount = 0;
        int stepsCountError = 0;
        int stepsCountExec = 0;
        int stepsCountSuccess = 0;
        while (!queue.isEmpty()) {
            
            file = (File) queue.poll();

            queue.addAll(Arrays.asList(file.listFiles(new FileFilter() {

                public boolean accept(File pathname) {

                    return pathname.isDirectory();

                }
            })));
            for (File xlsFile : file.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.toLowerCase().startsWith("testcase") && name.contains(".xls");
                }
            })) {

                //System.out.println("process " + xlsFile.getName());
                try {

                    Workbook w = new HSSFWorkbook(new FileInputStream(xlsFile));

                    System.out.println(w.getNumberOfSheets() + " available");

                    for (int i = 1; i < w.getNumberOfSheets(); i++) {
                        try {
                            tstCount++;
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
                            Row r6 = sh.getRow(5);
                            //System.out.println("Regr: " + r5.getCell(6).getStringCellValue());
                            l.add(getStringValue(r6.getCell(6)));

//                    Row r6 = sh.getRow(5);
//                    System.out.println("Negtst: " + r6.getCell(6).getStringCellValue());
//                    l.add(getStringValue(r6.getCell(6)));

                            Row r3 = sh.getRow(2);
                            final String status = getStringValue(r3.getCell(6));
                            
                            if (status.equals("Offen")) {
                                tstCountOpen++;
                            } else if (status.equals("In Arbeit")) {
                                tstCountWork++;
                            } else if (status.equals("i.O.")) {
                                tstCountSuccess++;
                            } else if (status.equals("n.i.O.")) {
                                tstCountError++;
                            } 
                            //System.out.println("State: " + r3.getCell(6).getStringCellValue());
                            l.add(status);


                            Row r15 = sh.getRow(14);
                            final double stepNumber = getNumericValue(r15.getCell(0));
                            stepsCount += stepNumber;
                            //System.out.println("Steps: " + r15.getCell(0).getNumericCellValue());
                            l.add((int) stepNumber);
                            final int executedSteps = (int) (getNumericValue(r15.getCell(3))
                                                        + getNumericValue(r15.getCell(4))
                                                        + getNumericValue(r15.getCell(5)));
                            stepsCountExec += executedSteps;
                            
                            l.add(executedSteps);
                            final int successSteps = (int) getNumericValue(r15.getCell(3));
                            //                    System.out.println("success Steps: " + r15.getCell(3).getNumericCellValue());
                            stepsCountSuccess += successSteps;
                            l.add(successSteps);
                            final int errorSteps = (int) getNumericValue(r15.getCell(4));
//                    System.out.println("failed Steps: " + r15.getCell(4).getNumericCellValue());
                            stepsCountError += errorSteps;
                            l.add(errorSteps);
//                    System.out.println("unknown State Steps: " + r15.getCell(5).getNumericCellValue());
                            //l.add(getNumericValue(r15.getCell(5)));
//                    
                            Row r10 = sh.getRow(9);
                            final Object dateValue = getDateValue(r10.getCell(6));
//                    System.out.println("TEster: " + r10.getCell(2).getStringCellValue());
//                    System.out.println("Datum: " + r10.getCell(6).getDateCellValue());
                            if (dateValue != null) {
                                l.add(dateFormat.format(dateValue));
                            }
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
                     
                    jTable1.getModel().setValueAt(tstCount, 0, 0);
                    jTable1.getModel().setValueAt(tstCountOpen, 0, 1);
                    jTable1.getModel().setValueAt(tstCountWork, 0, 2);
                    jTable1.getModel().setValueAt(tstCountSuccess, 0, 3);
                    jTable1.getModel().setValueAt(tstCountError, 0, 4);
                    jTable1.getModel().setValueAt(stepsCount, 0, 5);
                    jTable1.getModel().setValueAt(stepsCountExec, 0, 6);
                    jTable1.getModel().setValueAt(stepsCountSuccess, 0, 7);
                    jTable1.getModel().setValueAt(stepsCountError, 0, 8);

                } catch (Exception ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, "could not load " + xlsFile.getName(), ex);
                }
                
               
            }
        }
        folderTree.expandAll();
        
        
        
    }

    public static String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        try {
        return cell.getStringCellValue();
        } catch (IllegalStateException e) {
            return String.valueOf(cell.getNumericCellValue());
        }
    }

    public static double getNumericValue(Cell cell) {
        if (cell == null) {
            throw new IllegalStateException();
        }
        
        return cell.getNumericCellValue();
    }

    public static Object getDateValue(Cell cell) {
        if (cell == null) {
            throw new IllegalStateException();
        }
        try {
            return cell.getDateCellValue();
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        folderTree = new org.jdesktop.swingx.JXTreeTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(200);

        jScrollPane1.setViewportView(folderTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tst Gesamt", "Tst Offen", "Tst In Arbeit", "Tst i.O.", "Tst n.i.O", "Schritte Gesamt", "Schritte ausgef.", "Schritte i.O.", "Schritte n.i.O"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        jMenu1.setText("File");

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        jMenuItem2.setText("MultiChange");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setLabel("CSVExport");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("ExtractDataFromJiraExcel");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        MultiChangeDialog dialog = new MultiChangeDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CSVExportDialog dialog  = new CSVExportDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        ExtractDataFromJiraExcelDialog d = new ExtractDataFromJiraExcelDialog(this, true);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(NewMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(NewMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(NewMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(NewMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NewMainFrame().setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXTreeTable folderTree;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void buildFolderStructure(DefaultMutableTreeTableNode node, File file) {
        for (File childDir : Arrays.asList(file.listFiles(new FileFilter() {

            public boolean accept(File pathname) {

                return pathname.isDirectory();

            }
        }))) {

            DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(new FileWrapper(childDir));
            node.add(childNode);
            buildFolderStructure(childNode, childDir);

        }
    }

    static class FileWrapper {

        public File file;
        public String name;
        public FileWrapper(File file, String name) {
            this.file = file;
            this.name = name;
        }
        
        public FileWrapper(File file) {
            this.file = file;
        }
        @Override
        public String toString() {
            if (name != null) return name;
            return file.getName();
        }
    }
}
