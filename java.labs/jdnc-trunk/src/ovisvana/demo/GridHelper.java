package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Document;

import com.exalto.ExaltoXmlNode;

public class GridHelper
{
	HashMap rowMapper;
	ArrayList plist;
	int row;
	HashMap nodeMapTbl;
	SimpleTreeModelAdapter tdap;
	XmlTreeModel domTreeModel;

	public GridHelper() {

	}

	public GridHelper(SimpleTreeModelAdapter tdap) {
		this.tdap = tdap;
		domTreeModel = tdap.getModel();
		rowMapper = domTreeModel.getRowMapper();

	}


	public GridHelper(HashMap rowMapper, int row) {
		this.rowMapper = rowMapper;
		this.row = row;	
	}

	public String [][] retrieveSortedParts(int row) {

		domTreeModel = tdap.getModel();
		rowMapper = domTreeModel.getRowMapper();

		ArrayList nlist = (ArrayList) rowMapper.get(new Integer(row));
		String rowcol = (String) nlist.get(0);    

        String [][] parts = null;
		StringTokenizer stok2 = new StringTokenizer(rowcol, "|");
		int num = stok2.countTokens();
		parts = new String[num][3];
		int ct=0;
		while(stok2.hasMoreTokens()) {
			String rwc = stok2.nextToken();			
			StringTokenizer stok3 = new StringTokenizer(rwc, ",");
			parts[ct][0] = stok3.nextToken();
			parts[ct][1] = stok3.nextToken();
			parts[ct++][2] = stok3.nextToken();
			
		}
		
			Arrays.sort(parts, new ColumnComparator());

		return parts;

	}


	public ExaltoXmlNode getNodeForRowColumn(int row, int column) {

		domTreeModel = tdap.getModel();
		rowMapper = domTreeModel.getRowMapper();
	
		ArrayList nlist = (ArrayList) rowMapper.get(new Integer(row));
		String rowcol = (String) nlist.get(0);   

        String [][] parts = null;
		StringTokenizer stok2 = new StringTokenizer(rowcol, "|");
		int num = stok2.countTokens();

		parts = new String[num][3];
		int ct=0;
		while(stok2.hasMoreTokens()) {
			String rwc = stok2.nextToken();			
			StringTokenizer stok3 = new StringTokenizer(rwc, ",");
			parts[ct][0] = stok3.nextToken();
			parts[ct][1] = stok3.nextToken();
			parts[ct++][2] = stok3.nextToken();			
		}
		
			Arrays.sort(parts, new ColumnComparator());
	
		for(int c=0;c<ct;c++) {
		
			int rw = Integer.parseInt(parts[c][0]);
			int col = Integer.parseInt(parts[c][1]);
			int px = Integer.parseInt(parts[c][2]);

			if(row == rw && column == col) {
				return (ExaltoXmlNode) domTreeModel.getParentList().get(px);		
			}

		}
	
		return null;
	
	}


	public static class ColumnComparator implements Comparator {
 
		public int compare(Object o1, Object o2) {
	
		String [] str1 = (String []) o1;
		String [] str2 = (String []) o2;

		int result = 0;

	
	   /* Sort on first element of each array (last name) */
	   if ((result = str1[1].compareTo(str2[1])) == 0)
	   {
			return result;
	   }

		return result;
	}

 }


	
public int getElementColumnForRow(int row) {

	String [][] parts = retrieveSortedParts(row);
	int col = 0;
	for(int r=0;r<1;r++) {
		Arrays.sort(parts, new ColumnComparator());
	    int rw = Integer.parseInt(parts[r][0]);
	    col = Integer.parseInt(parts[r][1]);
	}

	return col;
}

}
