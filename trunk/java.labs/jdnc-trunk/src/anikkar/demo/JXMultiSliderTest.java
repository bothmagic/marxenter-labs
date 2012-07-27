
import org.jdesktop.swingx.multislider.JXMultiSlider;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

/**
 * @author Arash Nikkar
 */
public class JXMultiSliderTest extends JFrame implements ChangeListener {

	private JXMultiSlider horizontalSlider = new JXMultiSlider(JXMultiSlider.HORIZONTAL);
	private JXMultiSlider verticalSlider = new JXMultiSlider(JXMultiSlider.VERTICAL);

    private JSlider regularHorizontalSlider;
    private JSlider regularVerticalSlider;

	private JLabel horizontalInnerValue;
    private JLabel horizontalOuterValue;
    private JMenu fileMenu;
    private JMenu lnfMenu;
    private JLabel regularHorizontalSliderValue;
    private JLabel regularVerticalSliderValue;
    private JLabel verticalInnerValue;
    private JLabel verticalOuterValue;

	public JXMultiSliderTest() {
		super("JXMultiSlider Demo");
		setupGUI();
		setupMenu();

		regularHorizontalSlider.setMajorTickSpacing(10);
		regularHorizontalSlider.setMinorTickSpacing(5);
		regularHorizontalSlider.setPaintTicks(true);
		regularHorizontalSlider.setPaintTrack(true);
		regularHorizontalSlider.setPaintLabels(true);

		horizontalSlider.setMajorTickSpacing(10);
		horizontalSlider.setMinorTickSpacing(5);
		horizontalSlider.setPaintTicks(true);
		horizontalSlider.setPaintTrack(true);
		horizontalSlider.setPaintLabels(true);

		regularVerticalSlider.setMajorTickSpacing(10);
		regularVerticalSlider.setMinorTickSpacing(5);
		regularVerticalSlider.setPaintTicks(true);
		regularVerticalSlider.setPaintTrack(true);
		regularVerticalSlider.setPaintLabels(true);

		verticalSlider.setMajorTickSpacing(10);
		verticalSlider.setMinorTickSpacing(5);
		verticalSlider.setPaintTicks(true);
		verticalSlider.setPaintTrack(true);
		verticalSlider.setPaintLabels(true);

		regularHorizontalSlider.addChangeListener(this);
		horizontalSlider.addChangeListener(this);
		verticalSlider.addChangeListener(this);
		regularVerticalSlider.addChangeListener(this);

		stateChanged(null);
		
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void stateChanged(ChangeEvent e) {
		regularHorizontalSliderValue.setText("" + regularHorizontalSlider.getValue());

		horizontalInnerValue.setText("" + horizontalSlider.getInnerValue());
		horizontalOuterValue.setText("" + horizontalSlider.getOuterValue());

		regularVerticalSliderValue.setText("" + regularVerticalSlider.getValue());

		verticalInnerValue.setText("" + verticalSlider.getInnerValue());
		verticalOuterValue.setText("" + verticalSlider.getOuterValue());          		
	}

	private void setupGUI() {
		JTabbedPane jTabbedPane1 = new JTabbedPane();
        JPanel jPanel1 = new JPanel();
        regularHorizontalSlider = new JSlider();
        JLabel jLabel1 = new JLabel();
        regularHorizontalSliderValue = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        horizontalInnerValue = new JLabel();
        horizontalOuterValue = new JLabel();
        JPanel jPanel2 = new JPanel();
        regularVerticalSlider = new JSlider();
        regularVerticalSliderValue = new JLabel();
        JLabel jLabel4 = new JLabel();
        verticalOuterValue = new JLabel();
        JLabel jLabel6 = new JLabel();
        verticalInnerValue = new JLabel();
        JLabel jLabel7 = new JLabel();
        JMenuBar jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu();
        lnfMenu = new JMenu();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Value:");

        jLabel2.setText("Inner Value:");

        jLabel3.setText("Outer Value:");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regularHorizontalSliderValue, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                    .addComponent(regularHorizontalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(horizontalInnerValue)
                            .addComponent(horizontalOuterValue)))
                    .addComponent(horizontalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(regularHorizontalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(regularHorizontalSliderValue, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addComponent(horizontalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(horizontalInnerValue)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(horizontalOuterValue))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Horizontal", jPanel1);

        regularVerticalSlider.setOrientation(JSlider.VERTICAL);

        jLabel4.setText("Value:");

        verticalSlider.setOrientation(JSlider.VERTICAL);

        jLabel6.setText("Inner Value:");

        jLabel7.setText("Outer Value:");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(regularVerticalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(regularVerticalSliderValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(verticalSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jLabel6)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(verticalInnerValue)
                    .addComponent(verticalOuterValue))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(regularVerticalSlider, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                    .addComponent(verticalSlider, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(verticalInnerValue))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(verticalOuterValue)))
                    .addComponent(regularVerticalSliderValue, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Vertical", jPanel2);

        fileMenu.setText("File");
        jMenuBar1.add(fileMenu);

        lnfMenu.setText("Look & Feel");
        jMenuBar1.add(lnfMenu);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );

        pack();
	}

	private void setupMenu() {

		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(menuItem);

		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("Windows XP");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JRadioButtonMenuItem)e.getSource()).isSelected()) {
					try {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(JXMultiSliderTest.this);
					JXMultiSliderTest.this.pack();
				}
		}});
		group.add(rbMenuItem);
		lnfMenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Windows Classic");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JRadioButtonMenuItem)e.getSource()).isSelected()) {
					try {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(JXMultiSliderTest.this);
					JXMultiSliderTest.this.pack();
				}
		}});
		group.add(rbMenuItem);
		lnfMenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Metal");
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JRadioButtonMenuItem)e.getSource()).isSelected()) {
					try {
						UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(JXMultiSliderTest.this);
					JXMultiSliderTest.this.pack();
				}
		}});
		group.add(rbMenuItem);
		lnfMenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Motif");
		rbMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JRadioButtonMenuItem)e.getSource()).isSelected()) {
					try {
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(JXMultiSliderTest.this);
					JXMultiSliderTest.this.pack();
				}
		}});
		group.add(rbMenuItem);
		lnfMenu.add(rbMenuItem);		
	}

	public static void showFrame(JFrame frame) {
        if(!frame.isVisible()) {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (dim.width - frame.getWidth()) / 2;
            int y = (dim.height - frame.getHeight()) / 2;
            frame.setLocation(x, y);
            frame.setVisible(true);
        }
        frame.setState(Frame.NORMAL);
        frame.toFront();
    }

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		showFrame(new JXMultiSliderTest());
	}
}
