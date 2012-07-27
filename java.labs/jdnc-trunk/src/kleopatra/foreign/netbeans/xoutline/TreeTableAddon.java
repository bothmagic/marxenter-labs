/*
 * Created on 16.06.2008
 *
 */
package netbeans.xoutline;

import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.DefaultsList;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

public class TreeTableAddon extends AbstractComponentAddon {

    public TreeTableAddon() {
        super("JXXTreeTable");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXXTreeTable.uiClassID,
                "netbeans.xoutline.BasicTreeTableUI");
        defaults.add("Outline.indentWidth", 20);
    }

}
