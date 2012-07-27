package org.jdesktop.swing.decorator;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.jdnc.JNTable;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.swingx.util.WindowUtils;


/**
 * @author Gilles Philippart
 */
public class QuickSearcherDemo {
	private JPanel searchPanel;
	private JTextField searchField;
	
	public static void main(String[] args) {
		new QuickSearcherDemo().run();
	}
	
	private void run() {
		initGUI();
	}
	
	private void initGUI() {
		this.searchPanel = createSearchPanel();
		
		JPanel centerPanel = new JPanel();
		JPanel p = centerPanel;
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JPanel tableAndSearch = new JPanel();
		tableAndSearch.setLayout(new BorderLayout());
		tableAndSearch.add(createJXTablePanel(), BorderLayout.CENTER);
		tableAndSearch.add(this.searchPanel, BorderLayout.SOUTH);
		p.add(tableAndSearch);
		p.add(Box.createHorizontalStrut(10));
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout(10,10));
		f.getContentPane().add(new JLabel("Please give focus to one of the components hereunder. Then start typing some keys to filter entries."),
			BorderLayout.NORTH);
		f.getContentPane().add(p, BorderLayout.CENTER);
		f.pack();
		f.setLocation(WindowUtils.getPointForCentering(f));
		f.setVisible(true);
	}
	
	private JPanel createSearchPanel() {
		JPanel sPanel = new JPanel();
		FlowLayout layout = (FlowLayout)sPanel.getLayout();
		layout.setHgap(5);
		layout.setAlignment(FlowLayout.LEFT);
		
		sPanel.add(new JLabel("Find:"));
		this.searchField = new JTextField();
		this.searchField.setColumns(20);
		
		sPanel.add(this.searchField);
		return sPanel;
	}
	
	
	private Component createJXTablePanel() {
		final JNTable table = new JNTable();
		table.setHighlighters(new HighlighterPipeline(new Highlighter[] { new AlternateRowHighlighter() }));
		table.setModel(createSampleTableModel());
		// adding the quicksearcher
		QuickSearcher quickSearcher = new PatternQuickSearcher("Search ", table, this.searchPanel, this.searchField) {
			
			protected void filter(FilterPipeline filterPipeline) {
				table.getTable().setFilters(filterPipeline);
			}
		};
		table.getTable().addAncestorListener(quickSearcher);
		table.getTable().addKeyListener(quickSearcher);
		return table;
	}
	
	private TableModel createSampleTableModel() {
		Vector data = loadTestData();
    Vector cols = new Vector();
    cols.add("Last Name");
    cols.add("First Name");
    // new Object[][] { { "Orson", "Scott Card" }, { "John", "Wayne" },
		// { "Johnny", "Wilkinson" }, { "Steve", "Vai" }, { "Eric", "Clapton" }, { "Brad", "Pitt" } }
		DefaultTableModel tableModel = new DefaultTableModel(data, cols ) {
				
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			return tableModel;
	}
	
	private static abstract class PatternQuickSearcher extends AbstractQuickSearcher {
		
		private PatternQuickSearcher(String textPrefix, Component owner, JPanel aSearchPanel, JTextField aTextField) {
			super(textPrefix, owner, aSearchPanel, aTextField);
		}
		
		public void search(String s) {
			String s2 = StringUtils.replace(s, "*", ".*");
			String last = StringUtils.right(s2, 1);
			if (!"*".equals(last) && s.length() != 0) {
				s2 += ".*";
			}
			FilterPipeline filterPipeline = new FilterPipeline(new Filter[] { new PatternFilter(s2, Pattern.CASE_INSENSITIVE, 0) });
			filter(filterPipeline);
		}
		
		abstract protected void filter(FilterPipeline filterPipeline);
	}
	
	private Vector loadTestData() {
    Vector data = null;
		try {
			List<String> firsts = readLines("resources/test-data-first.csv");
			List<String> lasts = readLines("resources/test-data-last.csv");
      int total = firsts.size() * lasts.size();
      data = new Vector(total);
      Vector item = null;
      for ( String first : firsts ) {
        for ( String last : lasts ) {
          item = new Vector();
          item.add(last);
          item.add(first);
          data.add(item);
        }
      }
    } catch (Exception ex) {
			ex.printStackTrace();
		}
    return data;
	}

  private List<String> readLines(String resourceName) throws IOException {
      InputStream is = openStream(resourceName);
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
			ArrayList lines = new ArrayList();
			String line = null;
			while ( (line = lnr.readLine()) != null ) {
        lines.add(line);
			}
      is.close();
      return lines;
  }
  
	private InputStream openStream(String resourceName) {
		InputStream stream = null;
		
		try {
			stream = getClass().getResourceAsStream(resourceName);
		} catch (Exception e) {
			System.out.println("can't open resource " + resourceName);
			return null;
		}
		if ( stream == null ) {
			System.err.println("resource does not exist: " + resourceName);
			return null;
		}
		return stream;
	}
}
