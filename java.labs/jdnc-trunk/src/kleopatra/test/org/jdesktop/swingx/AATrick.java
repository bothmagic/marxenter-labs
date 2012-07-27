/*
 * Created on 15.09.2008
 *
 */
package org.jdesktop.swingx;

import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.test.AncientSwingTeam;



public class AATrick extends InteractiveTestCase {

    public static void main(String[] args) {
        AATrick test = new AATrick();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveKeepAAInNonAALFs() throws UnsupportedLookAndFeelException {
        // force a lf which sets the aa entry
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        final Object aaKey = getKeyByString("AATextInfoPropertyKey");
        final Object value = UIManager.get(aaKey);
        JComponent comp = Box.createVerticalBox();
        comp.add(new JLabel("many, many, many aaaaaaaaaaaasssssss"));
        JXTable table = new JXTable(new AncientSwingTeam());
        comp.add(new JScrollPane(table));
        table.setColumnControlVisible(true);
        JComponent buttons = Box.createHorizontalBox();
        buttons.add(new JButton("dummy - aaaa"));
        buttons.add(new JRadioButton("another dummy"));
        buttons.add(new JTextField("woaaaaaa ... we do not will not ..."));
        comp.add(buttons);
        JXFrame frame = wrapInFrame(comp, "AA enabled in non-AA LAFs (motif)");
        Action action = new AbstractAction("manual aa in motif") {

            public void actionPerformed(ActionEvent e) {
                if (!UIManager.getLookAndFeel().getClass().getName().contains("Motif")) return;
                UIManager.put(aaKey, value);
                SwingXUtilities.updateAllComponentTreeUIs();
                setEnabled(false);
            }
            
        };
        addAction(frame, action);
        show(frame);
    }
    
    private Object getKeyByString(String aaString)  {
      UIDefaults uiDefaults = UIManager.getDefaults();
      Enumeration keys = uiDefaults.keys();
      while(keys.hasMoreElements()) {
          Object key = keys.nextElement();
          if (aaString.equals(key.toString())) return key;
      }
      return null;
  }

}
