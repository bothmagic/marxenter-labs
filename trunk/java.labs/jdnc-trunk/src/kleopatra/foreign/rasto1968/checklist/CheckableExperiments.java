/*
 * $Id: CheckableExperiments.java 3312 2010-11-03 10:55:40Z kleopatra $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package rasto1968.checklist;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

public class CheckableExperiments extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(CheckableExperiments.class.getName());
    
    public static void main(String[] args) {
        CheckableExperiments test = new CheckableExperiments();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveCheckList() {
        StringValue sv = new StringValue() {
            public String getString(Object value) {
                if (value instanceof Number) {
                    return "number: " + value;
                }
                return StringValues.TO_STRING.getString(value);
            }
        };
        DefaultListModel model = createListModel(0, 10);
        final JXList list = CheckListFactory.create(model, new LabelProvider(sv));
        
        list.addHighlighter(HighlighterFactory.createSimpleStriping());
        list.setFixedCellHeight(new JLabel(" ").getPreferredSize().height + 5);

        JXFrame frame = wrapWithScrollingInFrame(list, "checklist");

        Action sort = new AbstractActionExt("sort") {
            public void actionPerformed(ActionEvent e) {
                list.toggleSortOrder();
            }
        };
        addAction(frame, sort);
        
        final CheckListModel controller = (CheckListModel) list.getModel();
        
        Action toggleNodes = new AbstractActionExt("toggle exposeNodes: " + controller.getUnwrapUserObject()) {
            public void actionPerformed(ActionEvent e) {
                controller.setUnwrapUserObject(!controller.getUnwrapUserObject());
                setName("toggle exposeNodes: " + controller.getUnwrapUserObject());
            }
        };
        addAction(frame, toggleNodes);
        addComponentOrientationToggle(frame);
        // just a quick check: feel difference between 
        // faked checkbox behaviour in list vs. standalone
        Action check = new AbstractActionExt("press?") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("fired on release!");
                
            }
        };
        addStatusComponent(frame, new JCheckBox(check));
        show(frame);
    }

    private DefaultListModel createListModel(int start, int lenght) {
        DefaultListModel model = new DefaultListModel();
        for (int i = start; i < lenght; i++) {
            model.addElement(i);
        }
        return model;
    }

}
