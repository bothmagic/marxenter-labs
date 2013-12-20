/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mmx.etester.RetrieveData.wizardpage;

import de.mmx.etester.RetrieveData.restdata.Requirement;
import de.mmx.etester.RetrieveData.wizardpage.FinishPage;
import de.mmx.etester.RetrieveData.wizardpage.ProjectPage;
import de.mmx.etester.RetrieveData.wizardpage.RequirementsPage;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ciscavate.cjwizard.PageFactory;
import org.ciscavate.cjwizard.WizardContainer;
import org.ciscavate.cjwizard.WizardListener;
import org.ciscavate.cjwizard.WizardSettings;
import org.ciscavate.cjwizard.pagetemplates.TitledPageTemplate;

/**
 * Container for wizardpages to Select requirements per TemplatePart.
 * 
 * @author marxma
 */
public class WizardPage extends javax.swing.JPanel implements WizardListener {

    
    private List<String> templates;
    
    private Map<String, RequiementSettings> settings;
    
    private int templateCount;
    private int wizardCount;
    private final FinishListener finishListener;
   
    
    /**
     * Creates new form WizardPage
     */
    public WizardPage(FinishListener finishListener) {
        initComponents();
        this.finishListener = finishListener;
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
        liTemplate = new javax.swing.JList();
        pWizard = new javax.swing.JPanel();

        liTemplate.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(liTemplate);

        javax.swing.GroupLayout pWizardLayout = new javax.swing.GroupLayout(pWizard);
        pWizard.setLayout(pWizardLayout);
        pWizardLayout.setHorizontalGroup(
            pWizardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );
        pWizardLayout.setVerticalGroup(
            pWizardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pWizard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(pWizard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList liTemplate;
    private javax.swing.JPanel pWizard;
    // End of variables declaration//GEN-END:variables

    public void setTemplateList(List<String> templateList) {
        liTemplate.setListData(templateList.toArray());
        this.templates = templateList;
        WizardContainer wc = new WizardContainer(
                new WizardFactory(templateList.size()),
                new TitledPageTemplate());

        wc.addWizardListener(this);
        pWizard.setLayout(new BorderLayout());
        pWizard.add(wc, BorderLayout.CENTER);
        
        liTemplate.setSelectedIndex(0);
        settings = new HashMap<String, RequiementSettings>();
    }

    public void onPageChanged(org.ciscavate.cjwizard.WizardPage newPage, List<org.ciscavate.cjwizard.WizardPage> path) {
        liTemplate.setSelectedIndex((path.size()-1) / wizardCount);
    }

    public void onFinished(List<org.ciscavate.cjwizard.WizardPage> path, WizardSettings settings) {
        this.finishListener.finish(
                new File(((FinishPage)path.get(path.size()-1)).getOutput()), this.settings);
    }

    public void onCanceled(List<org.ciscavate.cjwizard.WizardPage> path, WizardSettings settings) {
    }

    private class WizardFactory implements PageFactory {
        
       
        
        // To keep things simple, we'll just create an array of wizard pages:
        private final List<org.ciscavate.cjwizard.WizardPage> allPages;

        public WizardFactory(int templateCount) {
            WizardPage.this.templateCount = templateCount;
            List<org.ciscavate.cjwizard.WizardPage> pages =
                    new ArrayList<org.ciscavate.cjwizard.WizardPage>(
                    Arrays.asList(new org.ciscavate.cjwizard.WizardPage[]{
                        new ProjectPage(),
                        new RequirementsPage()
                    }));
            allPages = new ArrayList<org.ciscavate.cjwizard.WizardPage>();
            wizardCount = pages.size();
            for (int i = 0; i < templateCount; i++) {
                allPages.addAll(pages);
            }
            
            allPages.add(new FinishPage());

        }

        /* (non-Javadoc)
         * @see org.ciscavate.cjwizard.PageFactory#createPage(java.util.List, org.ciscavate.cjwizard.WizardSettings)
         */
        @Override
        public org.ciscavate.cjwizard.WizardPage createPage(List<org.ciscavate.cjwizard.WizardPage> path,
                WizardSettings settings) {

            if (path.size() > 1 && (path.size() % wizardCount == 0)) {
                RequiementSettings reqSett = new RequiementSettings();

                reqSett.setRequirements((List<Requirement>)settings.get("requirements"));
                reqSett.setProperties((List<String>)settings.get("properties"));
                WizardPage.this.settings.put((String)templates.get((path.size()-1) / wizardCount), reqSett);

            }
        
            
            org.ciscavate.cjwizard.WizardPage page = allPages.get(path.size());
            
            return page;
        }
    }
    
    public static class RequiementSettings {
        
        private List<Requirement> requirements;
        
        private List<String> properties;

        public List<Requirement> getRequirements() {
            return requirements;
        }

        public void setRequirements(List<Requirement> requirements) {
            this.requirements = requirements;
        }

        public List<String> getProperties() {
            return properties;
        }

        public void setProperties(List<String> properties) {
            this.properties = properties;
        }
        
    }
    
    public static interface FinishListener {
        
        public void finish(File output, Map<String, RequiementSettings> settings);
        
    }
}