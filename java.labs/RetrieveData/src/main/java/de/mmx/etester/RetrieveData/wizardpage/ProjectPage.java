/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.wizardpage;

import de.mmx.etester.RetrieveData.etrest.LoadManager;
import de.mmx.etester.RetrieveData.restdata.ETPackage;
import de.mmx.etester.RetrieveData.restdata.Project;
import de.mmx.etester.RetrieveData.restdata.Requirement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import org.ciscavate.cjwizard.WizardPage;
import org.ciscavate.cjwizard.WizardSettings;

/**
 *
 * @author marxma
 */
public class ProjectPage extends WizardPage {

    /**
     * Creates new form RequirementsPage
     */
    public ProjectPage() {
        super("Anforderungen auswählen", "Anforderungen auswählen");
        initComponents();
        ttEtReq.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
    }

    @Override
    public void updateSettings(WizardSettings settings) {
        super.updateSettings(settings);
        
        ArrayList<Requirement> reqs = new ArrayList<Requirement>();
        
        int[] rows = ttEtReq.getSelectedRows();
        for (int row : rows) {
            TreePath path = ttEtReq.getPathForRow(row);
            Object lastElem = path.getLastPathComponent();
            if (lastElem instanceof Project) {
                for (ETPackage pkg: ((Project)lastElem).getReqPackage()) {
                    collectReq(pkg, reqs);
                }
            } else if (lastElem instanceof ETPackage) {
                collectReq(((ETPackage)lastElem), reqs);
            } else if (lastElem instanceof Requirement) {
                collectReq(((Requirement)lastElem), reqs);
            }
        }
        
        settings.put("requirements", reqs);
    }
    
    @Override
    public void rendering(List<WizardPage> path, WizardSettings settings) {
        super.rendering(path, settings);
        setNextEnabled(false);
        ttEtReq.setTreeTableModel(new ProjectTreeTableModel());        
        ttEtReq.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                setNextEnabled(ttEtReq.getSelectedRowCount() > 0);
            }
        });
    }
    
    private void collectReq(ETPackage eTPackage, List<Requirement> collectedList) {
        LoadManager.getInstance().load(eTPackage, false);
        for (ETPackage pgk: eTPackage.getChildren()) {
            collectReq(pgk, collectedList);
        }
        for (Requirement r : eTPackage.getRequirements()) {
            collectReq(r, collectedList);
        }
    }

    private void collectReq(Requirement req, List<Requirement> collectedList) {
        collectedList.add(req);
        LoadManager.getInstance().load(req, false);
        for (Requirement r : req.getChildren()) {
            collectReq(r, collectedList);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        ttEtReq = new org.jdesktop.swingx.JXTreeTable();
        jLabel3 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(ttEtReq);

        jLabel3.setText("Enterprise Tester Requirements");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 293, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTreeTable ttEtReq;
    // End of variables declaration//GEN-END:variables
}