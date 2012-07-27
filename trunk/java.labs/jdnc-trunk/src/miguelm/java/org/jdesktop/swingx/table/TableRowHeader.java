/*
 * $Id: TableRowHeader.java 1917 2007-11-16 10:58:10Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Created using IntelliJ IDEA. Date: Nov 27, 2004 Time: 5:31:10 PM
 *
 * @author Miguel Mu\u00f1oz
 */

public class TableRowHeader
				extends JTable {
	public TableRowHeader(BasicRowHeaderModel pMdl, JTable mainTable) {
		super(makeModel(pMdl));
		setPreferredScrollableViewportSize(new Dimension(30, 10));

		setDefaultRenderer(Object.class, new RowHeaderRenderer());
		setRowSorter(mainTable.getRowSorter());
	}

	public void setHeaderWidth(int pWidth) {
		setPreferredScrollableViewportSize(new Dimension(pWidth, 10));
	}

	private static TableModel makeModel(BasicRowHeaderModel pRowMdl) {
		return new ModelWrapper(pRowMdl);
	}

	/**
	 * My table row header is basically a JTable with a single column. This class
	 * wraps the BasicRowHeaderModel with a TableModel for the header's one-column table.
	 */
	private static class ModelWrapper
					extends AbstractTableModel {
		BasicRowHeaderModel mRowMdl;

		ModelWrapper(BasicRowHeaderModel pMdl) {
			mRowMdl = pMdl;
		}

		public int getRowCount() {
			return mRowMdl.getRowCount();
		}

		public int getColumnCount() {
			return 1;
		}

		public Object getValueAt(int row, int column) {
			return mRowMdl.getRowName(row);
		}
	}

	/**
	 * Returns a JScrollPane with a table header if the table model supports one.
	 * If the table model for the specifed table also implements BasicRowHeaderModel,
	 * This instantiates a JScrollPane for the JTable and sets the proper
	 * TableRowHeader. If not, This just returns a JScrollPane with the JTable.
	 *
	 * @param pTbl The JTable
	 * @return A JScrollPane with the header, if supported.
	 */
	public static JScrollPane createScroller(JTable pTbl) {
		TableModel tableModel = pTbl.getModel();
		if (tableModel instanceof BasicRowHeaderModel)
			return createScroller(pTbl, new TableRowHeader((BasicRowHeaderModel) tableModel, pTbl));
		return new JScrollPane(pTbl);
	}

	@SuppressWarnings({"AccessStaticViaInstance"})
	public static JScrollPane createScroller(JTable pTbl, TableRowHeader pHdr) {
		final JScrollPane scr = new JScrollPane(pTbl);
		{
			scr.setRowHeaderView(pHdr);
			// they share a selection model.
			pHdr.setSelectionModel(pTbl.getSelectionModel());
			// put in a blank corner component if there isn't one.
			if (scr.getCorner(scr.UPPER_LEFT_CORNER) == null) {
				JLabel corner = new JLabel("");
				corner.setBorder(new MatteBorder(0, 0, 1, 1, pTbl.getGridColor()));
				scr.setCorner(scr.UPPER_LEFT_CORNER, corner);
			}
			if (scr.getCorner(scr.UPPER_RIGHT_CORNER) == null) {
				JLabel corner = new JLabel("");
				corner.setBorder(new MatteBorder(0, 1, 1, 0, pTbl.getGridColor()));
				scr.setCorner(scr.UPPER_RIGHT_CORNER, corner);
			}
			// The change listener keeps the two viewports in sync. The JScrollPane
			// already makes sure the header will scroll to match the main viewport.
			// This listener makes sure the main viewport will scroll to match the
			// row header.
			// There's always the possibility for an infinite loop with this design
			// pattern. But this listener only scrolls the view if they don't match.
			// So once they match, the process will end.
			ChangeListener
							cl = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JViewport master = (JViewport) e.getSource();
					JViewport slave = ((JScrollPane) master.getParent()).getViewport();
					final Point mstrVp = master.getViewPosition();
					final Point slvVp = slave.getViewPosition();
					int msV = mstrVp.y;
					int slV = slvVp.y;
					if (msV != slV)
						slave.setViewPosition(new Point(slvVp.x, msV));
				}
			};
			scr.getRowHeader().addChangeListener(cl);
		}
		return scr;
	}

	/**
	 * This renderer is less than ideal, but I wanted a renderer that looks
	 * acceptable in all looks and feels, even if it doesn't look good in any of
	 * them. Developers may write their own renderers. I wonder if there's a
	 * constant in the look-and-feel resource maps that I can take advantage of.
	 * I might want to make it look like the table column renderer.
	 */
	static class RowHeaderRenderer
					extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable rowHead,
		                                               Object value,
		                                               boolean isSelected,
		                                               boolean hasFocus,
		                                               int row,
		                                               int column) {
			assert rowHead != null;
			setHorizontalAlignment(RIGHT);
//	    if (rowHead!=null)
//	    {
			JTableHeader header = rowHead.getTableHeader();
			assert header != null;
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
//	    }
			if (isSelected) {
				setBackground((Color) UIManager.get("TableHeader.background"));
			}
			setBorder((Border) UIManager.get("TableHeader.cellBorder"));
			if (hasFocus || rowHead.getSelectionModel().getAnchorSelectionIndex() == row) {
				Border border = UIManager.getBorder("Table.focusCellseelightBorder");
				setBorder(border);
			} else
				setBorder(sEmptyBorder);
//			if (isSelected)
//			{
//				setBackground((Color)UIManager.get("TableHeader.light"));
//				setBorder(null);
//			}
////			if (hasFocus || rowHead.getSelectionModel().getAnchorSelectionIndex()==row)
////			{
////				Border border=UIManager.getBorder("Table.focusCellHighlightBorder");
////				setBorder(border);
////			}
//			else
//				setBorder(sEmptyBorder);

			String title = value == null ? "" : value.toString();
			setText(title);
			setToolTipText(title);
			return this;
		}
	}

	private static Border sEmptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
}
