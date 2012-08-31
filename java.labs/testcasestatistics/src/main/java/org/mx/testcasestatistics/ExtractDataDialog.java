/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mx.testcasestatistics;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author marxma
 */
public class ExtractDataDialog extends javax.swing.JDialog {

    /**
     * Creates new form ExtractDataDialog
     */
    public ExtractDataDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtFile = new javax.swing.JTextField();
        txtTemplate = new javax.swing.JTextField();
        txtOutput = new javax.swing.JTextField();
        txtPaket = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtFile.setText("OGE_12_Kapazitätsprodukte_erstellen+Auktionskalender\\Testcase_Produktgenerierung.xls");
        txtFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFileActionPerformed(evt);
            }
        });

        txtTemplate.setText("D:\\development_marx\\java.labs\\testcasestatistics\\data\\Testsets-work\\Testcase-Vorlage.xls");
        txtTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTemplateActionPerformed(evt);
            }
        });

        txtOutput.setText("OGE_12_Kapazitätsprodukte_erstellen+Auktionskalender\\");

            txtPaket.setText("OGE 12 Kapazitätsprodukte erstellen/Produktgenerierung");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton1))
                        .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txtTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(txtOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addComponent(txtPaket))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtPaket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        Workbook templateW = null;
        Workbook fileW = null;
        Workbook newBook = null;
        File fileFile = new File(txtFile.getText());
        try {
            
            fileW = new HSSFWorkbook(new FileInputStream(txtFile.getText()));
        } catch (IOException ex) {
            Logger.getLogger(ExtractDataDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Map<Integer, Integer[]> copyMap = new HashMap<Integer, Integer[]>();
        copyMap.put(0, new Integer[] {2});
        copyMap.put(1, new Integer[] {2, 6});
        copyMap.put(2, new Integer[] {2});
        copyMap.put(3, new Integer[] {2, 6});
        copyMap.put(4, new Integer[] {2, 6});
        copyMap.put(5, new Integer[] {2, 6});
        copyMap.put(7, new Integer[] {2});
        copyMap.put(9, new Integer[] {2, 6});
        copyMap.put(10, new Integer[] {2, 6});
        copyMap.put(11, new Integer[] {2, 6});
        copyMap.put(12, new Integer[] {2, 6});
        
        
        for (int i = 1; i < fileW.getNumberOfSheets(); i++) {
            try {
                templateW = new HSSFWorkbook(new FileInputStream(txtTemplate.getText()));
            } catch (IOException ex) {
                Logger.getLogger(ExtractDataDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            Sheet zus = templateW.getSheetAt(0);
            Sheet testcase  = templateW.getSheetAt(1);
            
            try {
                Sheet sh = fileW.getSheetAt(i);
                zus.getRow(2).getCell(1).setCellValue(sh.getSheetName());
                zus.getRow(1).getCell(1).setCellValue(txtPaket.getText());
                
                
                for (Map.Entry<Integer, Integer[]> entry: copyMap.entrySet()) {
                    
                    for (int cell : entry.getValue()) {
                        Cell srcCell = sh.getRow(entry.getKey()).getCell(cell);
                        Cell dstCell = testcase.getRow(entry.getKey()).getCell(cell);
                        
                        switch (srcCell.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN: dstCell.setCellValue(srcCell.getBooleanCellValue());break;
                            case Cell.CELL_TYPE_NUMERIC: dstCell.setCellValue(srcCell.getNumericCellValue());break;
                            case Cell.CELL_TYPE_STRING: dstCell.setCellValue(srcCell.getStringCellValue());break;

                        }
                    }
                
                }
                
                for (int k = 16; k <= 43; k++) {
                    Cell srcCell = sh.getRow(k).getCell(0);
                    Cell dstCell = testcase.getRow(k).getCell(0);
                    
                    if (srcCell.getNumericCellValue() < 1) break;
                    
                    dstCell.setCellValue(srcCell.getNumericCellValue());
                    
                    
                    srcCell = sh.getRow(k).getCell(1);
                    dstCell = testcase.getRow(k).getCell(1);
                    dstCell.setCellValue(srcCell.getStringCellValue());
                    srcCell = sh.getRow(k).getCell(2);
                    dstCell = testcase.getRow(k).getCell(2);
                    dstCell.setCellValue(srcCell.getStringCellValue());
                    srcCell = sh.getRow(k).getCell(2);
                    dstCell = testcase.getRow(k).getCell(2);
                    dstCell.setCellValue(srcCell.getStringCellValue());
                    srcCell = sh.getRow(k).getCell(6);
                    dstCell = testcase.getRow(k).getCell(6);
                    if (srcCell.getCellType() == Cell.CELL_TYPE_STRING)
                        dstCell.setCellValue(srcCell.getStringCellValue());
                    
                }
                templateW.write(new FileOutputStream(txtOutput.getText() + "/" +"Testcase_"+sh.getSheetName()+".xls"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    
    private static Object getCellValue(Cell cell) {
        if (cell == null) throw new IllegalArgumentException("argument cell is null.");
        
        
        return "";
    }

    private void txtFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFileActionPerformed

    private void txtTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTemplateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTemplateActionPerformed

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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExtractDataDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExtractDataDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExtractDataDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExtractDataDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ExtractDataDialog dialog = new ExtractDataDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextField txtOutput;
    private javax.swing.JTextField txtPaket;
    private javax.swing.JTextField txtTemplate;
    // End of variables declaration//GEN-END:variables
}