/*
 * Created on 12.03.2008
 *
 */
package osbald.button;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.InteractiveTestCase;

public class ButtonExperiments extends InteractiveTestCase{

    public static void main(String[] args) {
//      setSystemLF(true);
        ButtonExperiments test = new ButtonExperiments();
      try {
          test.runInteractiveTests();
//        test.runInteractiveTests(".*Tree.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }

    /**
     * Requirement:  multiple selection of checkboxes with minimal count.
     * 
     * Here: use a custom buttonGroup.
     */
    public void interactiveMultipleCheckBoxSelection() {
        JComponent comp = new JPanel();
        JCheckBox[] boxes = new JCheckBox[]{new JCheckBox("first"), new JCheckBox("second")};
        MyButtonGroup group = new MyButtonGroup();
        for (JCheckBox checkBox : boxes) {
            group.add(checkBox);
            comp.add(checkBox);
        }
        showInFrame(comp, "custom group");
    }
    
    /**
     * Custom ButtonGroup which forces a minimal number of selections.
     * Have to override all super methods which rely on the single selection.
     */
    public static class MyButtonGroup extends ButtonGroup {
        
        private int minSelection = 1;
        private List<ButtonModel> models;

        @Override
        public void add(AbstractButton b) {
            buttons.add(b);
            if (!b.getModel().isSelected() && forceSelection()) {
                b.getModel().setSelected(true);
            }
            if (b.getModel().isSelected()) {
                getSelectedModels().add(b.getModel());
            }
            b.getModel().setGroup(this);
        }
        
        private boolean forceSelection() {
            return getSelectedModels().size() < minSelection;
        }

        @Override
        public boolean isSelected(ButtonModel m) {
            return getSelectedModels().contains(m);
        }

        
        @Override
        public ButtonModel getSelection() {
            return getSelectedModels().size() > 0 ? getSelectedModels().get(0) : null;
        }

        @Override
        public void remove(AbstractButton b) {
            // TODO: implement ...
        }
        
        @Override
        public void setSelected(ButtonModel m, boolean b) {
            if (m.isSelected() == b) return;
            if (!b) {
                if (canDeselect(m)) {
                    getSelectedModels().remove(m);
                    m.setSelected(b);
                }
            } else {
                if (getSelectedModels().contains(m)) return;
                getSelectedModels().add(m);
                m.setSelected(b);
            }
        }
        
        public boolean canDeselect(ButtonModel m) {
            return getSelectedModels().contains(m) && getSelectedModels().size() >= minSelection + 1;
        }
        

        @Override
        public void clearSelection() {
            if ((minSelection > 0) && (getButtonCount() > 0))
                throw new IllegalStateException("clearSelection not allowed, mininal selection " + minSelection);
            super.clearSelection();
        }
        
        private List<ButtonModel> getSelectedModels() {
            if (models == null) {
                models = new ArrayList<ButtonModel>();
            }
            return models;
        }
        
        
    }

}
