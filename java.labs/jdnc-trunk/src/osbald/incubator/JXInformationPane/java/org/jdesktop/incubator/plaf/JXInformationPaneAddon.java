package org.jdesktop.incubator.plaf;

import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.DefaultsList;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.IconUIResource;
import java.awt.*;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 10-Jul-2007
 * Time: 12:10:26
 */

public class JXInformationPaneAddon extends AbstractComponentAddon {

    public JXInformationPaneAddon() {
        super(JXInformationPaneAddon.class.getName());
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add("JXInformationPane.contentType", "text/plain");
        defaults.add("JXInformationPane.font", UIManager.get("TextField.font"));
        defaults.add("JXInformationPane.border", UIManager.get("ScrollPane.border"));
        defaults.add("JXInformationPane.background",
                new ColorUIResource(new Color(255, 255, 225)));
        defaults.add("JXInformationPane.foreground",
                new ColorUIResource(SystemColor.infoText));
        defaults.add("JXInformationPane.informationIcon",
                new IconUIResource(new ImageIcon(JXInformationPaneAddon.class.getResource("basic/resources/information.png"))));
        defaults.add("JXInformationPane.warningIcon",
                new IconUIResource(new ImageIcon(JXInformationPaneAddon.class.getResource("basic/resources/error.png"))));
        defaults.add("JXInformationPane.errorIcon",
                new IconUIResource(new ImageIcon(JXInformationPaneAddon.class.getResource("basic/resources/exclamation.png"))));
    }
}
