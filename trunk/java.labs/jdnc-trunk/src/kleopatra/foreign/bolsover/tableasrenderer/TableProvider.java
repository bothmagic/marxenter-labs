/*
 * Created on 06.01.2007
 *
 */
package bolsover.tableasrenderer;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.LabelProvider;

public class TableProvider extends ComponentProvider<JComponent> {

    JXTable xtable;
    MultiCellTableModel model;
    private MultiCellData defaultData;
    private JXPanel panel;
    
    public TableProvider() {
    }
    
    @Override
    protected void configureState(CellContext context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected JComponent createRendererComponent() {
        defaultData = new MultiCellData();
        defaultData.setShowColHeader(true);
        model = new MultiCellTableModel();
        model.setData(defaultData);
        xtable = new JXTable(model);
        xtable.setVisibleRowCount(model.getRowCount());
        xtable.packColumn(0, -1);
        xtable.addHighlighter(HighlighterFactory.createSimpleStriping(
                new Color(0xF0, 0xF0, 0xE0)));
        ComponentProvider<JLabel> controller = new LabelProvider(JLabel.TRAILING);
        xtable.setDefaultRenderer(Object.class, new DefaultTableRenderer(controller));
        panel = new JXPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(xtable.getTableHeader());
        panel.add(xtable);
        xtable.setOpaque(false);
        return panel;
    }

    @Override
    protected void format(CellContext context) {
        MultiCellData data;
        if (context.getValue() instanceof MultiCellData) {
            data = (MultiCellData) context.getValue();
        } else {
            data = defaultData;
        }
        model.setData(data);
        if (context.isSelected()) {
            xtable.selectAll();
        } else {
            xtable.clearSelection();
        }
//        xtable.getTableHeader().setVisible(data.isShowColHeader());
    }

}
