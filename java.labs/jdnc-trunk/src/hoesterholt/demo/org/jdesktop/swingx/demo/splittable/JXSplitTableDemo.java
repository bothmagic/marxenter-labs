/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/

package org.jdesktop.swingx.demo.splittable;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXSplitTable;
import org.jdesktop.swingx.JXTwoLevelSplitTable;
import org.jdesktop.swingx.splittable.AbstractSplitTableModel;
import org.jdesktop.swingx.splittable.AbstractTwoLevelSplitTableModel;
import org.jdesktop.swingx.splittable.Jzc3Table;

import net.miginfocom.swing.MigLayout;

class SplitTableModel extends AbstractTableModel {
	
	private AbstractTableModel _model;
	private int _splitcol;
	private boolean _left;
	
	public SplitTableModel(AbstractTableModel m,int splitcol,boolean left) {
		_model=m;
		_splitcol=splitcol;
		_left=left;
	}

	public int getColumnCount() {
		if (_left) {
			return _splitcol;
		} else {
			return _model.getColumnCount()-_splitcol;
		}
	}

	public int getRowCount() {
		return _model.getRowCount();
	}

	public Object getValueAt(int row, int col) {
		if (_left) {
			return _model.getValueAt(row,col);
		} else {
			return _model.getValueAt(row,col+_splitcol);
		}
	}
	
	public String getColumnName(int col) {
		if (_left) {
			return _model.getColumnName(col);
		} else {
			return _model.getColumnName(col+_splitcol);
		}
	}
}

class JSplitTableInternal extends JScrollPane {

	public JSplitTableInternal(Jzc3Table right,JComponent left) {
		super(right,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.setRowHeaderView(left);
	}
}
	
class JSplitTable extends JPanel {
	
	SplitTableModel _left;
	SplitTableModel _right;
	
	Jzc3Table _tleft;
	Jzc3Table _tright;
	
	public JSplitTable(AbstractTableModel m,int splitAtColumn) {
		_left=new SplitTableModel(m,splitAtColumn,true);
		_right=new SplitTableModel(m,splitAtColumn,false);
		_tleft=new Jzc3Table(_left);
		_tright=new Jzc3Table(_right);
		_tright.setHorizontalScrollEnabled(true);
		JPanel p=new JPanel();
		p.setLayout(new MigLayout("insets 0,fill"));
		p.add(_tleft,"growx,growy");
		p.add(new JSeparator(SwingConstants.VERTICAL));
		super.setLayout(new MigLayout("insets 0,fill"));
		super.add(new JSplitTableInternal(_tright,p));
	}
}

public class JXSplitTableDemo {

	//static Logger logger=zc3Logger.getLogger(SplitTable.class);
	
	public static void main(String [] args) {
		
		//final zc3Log4JAppender _appender=new zc3Log4JAppender();
		//BasicConfigurator.configure(_appender);
		
	    try {
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	      } catch(Exception e) {
	    	  e.printStackTrace();
	        //logger.error("Error setting native LAF: " + e);
	    }	
	      
		
		SwingUtilities.invokeLater(new Runnable() {
			private JFrame _frame;
			
			public void run() {
				_frame=new JFrame();
				_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				try {
					AbstractTableModel model=new AbstractTableModel() {

						public int getColumnCount() {
							return 100;
						}

						public int getRowCount() {
							return 100;
						}

						public Object getValueAt(int rowIndex, int columnIndex) {
							return String.format("%d/%d",rowIndex,columnIndex);
						}
						
						public String getColumnName(int col) {
							return String.format("column %d",col);
						}
					};
					
					JSplitTable table=new JSplitTable(model,2);
					JPanel p=new JPanel();
					MigLayout l=new MigLayout("insets 0,fill","[grow]","[grow][][grow]");
					p.setLayout(l);
					p.add(table,"growx,growy,wrap");
					
					p.add(new JSeparator(SwingConstants.HORIZONTAL),"growx,wrap");
					
					class MyModel extends AbstractTwoLevelSplitTableModel {
						
						private Vector<Boolean> 							_expanded;
						private Vector<String>  							_nodes;
						private Hashtable<Integer,Vector<Vector<String>>> 	_rowsPerNode;
						private int _columns;
						
						public MyModel(int nodes,int rowspernode,int cols) {
							int i;
							_columns=cols;
							_expanded=new Vector<Boolean>();
							_nodes=new Vector<String>();
							_rowsPerNode=new Hashtable<Integer,Vector<Vector<String>>>();
							for(i=0;i<nodes;i++) {
								_expanded.add(false);
								_nodes.add(String.format("node %d",i));
								int j;
								Vector<Vector<String>> v=new Vector<Vector<String>>();
								for(j=0;j<(i+1)*2;j++) {
									Vector<String> r=new Vector<String>();
									for(int k=0;k<cols;k++) {
										r.add(String.format("%d/%d", j,k));
									}
									v.add(r);
								}
								_rowsPerNode.put(i, v);
							}
							_expanded.set(5,true);
						}

						public int getNodeColumnCount() {
							return 2;
						}

						public int getNodeColumnCount(int nodeIndex) {
							return _columns;
						}

						public boolean getNodeExpanded(int nodeIndex) {
							return _expanded.get(nodeIndex);
						}

						public int getNodeRowCount() {
							return _nodes.size();
						}

						public int getNodeRowCount(int nodeIndex) {
							return _rowsPerNode.get(nodeIndex).size();
						}

						public Object getNodeValue(int nodeIndex, int nodeColumn) {
							if (nodeColumn==1) {
								return (Integer) nodeIndex;
							} else {
								return _nodes.get(nodeIndex);
							}
						}

						public Object getValueAt(int nodeIndex, int nodeRow, int nodeColumn) {
							return _rowsPerNode.get(nodeIndex).get(nodeRow).get(nodeColumn);
						}

						public boolean setNodeExpanded(int nodeIndex,boolean true_is_yes) {
							_expanded.set(nodeIndex,true_is_yes);
							return _expanded.get(nodeIndex);
						}

						public int getSplitColumn() {
							return 1;
						}
						
						public String getColumnName(int c) {
							return String.format("col %d",c);
						}
						
						public String getMaxString(int col) {
							if (col==0) {
								return "ABCDEfghijklmnopQrestuvwxyz ABCDEfghijklmnopQrestuvwxyz"; 
							} else {
								return null;
							}
						}
						
					}
					
					final MyModel m=new MyModel(10,10,100);
					
					final JXTwoLevelSplitTable t=new JXTwoLevelSplitTable("test",m,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					p.add(t,"growx,growy,wrap");
					m.addColumnWidthListener(new AbstractSplitTableModel.ColumnWidthListener() {
						public void prefferedWidthForColumn(int col, int width) {
							//logger.info("col: "+col+", width: "+width);
							t.getColumnExt(col, true).setPreferredWidth(width);
							//t.getColumnExt(col, true).setWidth(width);
						}
					});
					//m.fireTableStructureChanged();
					//t.getColumnExt(0, false).setPrototypeValue("Abcdefghijklmnopqrstuvwxyz Abcdefghijklmnopqrstuvwxyz");
					//Object q=t.getColumnExt(0, false).getPrototypeValue();
					t.getColumnExt(0, true).setPreferredWidth(300);
					//t.doLayout();
					//t.packColumn(0,10, false);
					//JXTable tt;
					//tt.packColumn(column, margin)
					//t.setNodeIconColor(Color.RED);
					t.addSelectionListener(new JXSplitTable.SelectionListener() {
						public void choosen(int row, int col, boolean left) {
							//logger.info(String.format("Choosen : row=%d, col=%d, left=%d",row,col,(left)?1:0));
							System.out.println(String.format("Choosen : row=%d, col=%d, left=%d",row,col,(left)?1:0));
							m.fireTableStructureChanged();
							//t.doLayout();
							//t.getColumnExt(0, true).setPreferredWidth(400);
						}

						public void selected(int row, int col, boolean left) {
							//logger.info(String.format("Selected : row=%d, col=%d, left=%d",row,col,(left)?1:0));
							System.out.println(String.format("Selected : row=%d, col=%d, left=%d",row,col,(left)?1:0));
						}

						public void unSelected(boolean left) {
							//logger.info(String.format("UnSelected : left=%d",(left)?1:0));
							System.out.println(String.format("UnSelected : left=%d",(left)?1:0));
						}
						
					});
					
					{
						/*Jzc3ButtonArea a=new Jzc3ButtonArea();
						a.addButton("Ok", "ok", new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								_frame.setVisible(false);
								_frame.dispose();
							}
						});
						p.add(a,"growx");*/
						JButton a=new JButton("Ok");
						a.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								_frame.setVisible(false);
								_frame.dispose();
							}
						});
						p.add(a,"right,wrap");
					}
					
					_frame.add(p);
					_frame.pack();
					_frame.setSize(800,400);
					_frame.setVisible(true);
				} catch (Exception E) {
					E.printStackTrace();
					//logger.fatal(E);
				}
			}
		});
		
	}

}
