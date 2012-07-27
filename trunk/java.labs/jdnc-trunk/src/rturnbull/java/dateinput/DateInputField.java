/** DateInputField.java                          
 *
 * Created 15/07/2007 10:32:59 AM
 * 
 * @author Ray Turnbull
 */
package dateinput;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


/**
 *
 */
public class DateInputField extends JFormattedTextField {
	
	private static final String DEFAULT_PATTERN = "dd/MM/yyyy";
	
    private char separator = '/';
	private final String leftAction = "goleft";
    private final String rightAction = "goright";
    private int selectGroup = 0;
    private int dayGroup;
    private int monthGroup;
    private int yearGroup;
    private SimpleDateFormat format;

    private static SimpleDateFormat getFormat(String pattern) {
    	SimpleDateFormat df = new SimpleDateFormat(pattern);
    	df.setLenient(false);
    	return df;
    }
    
    public DateInputField() {
		this(DEFAULT_PATTERN);
	}
	
	public DateInputField(String pattern) {
		super(getFormat(pattern));
		decodePattern(pattern);
		setup();
		// must initialise as can't enter a separator
		setValue(new Date());
	}
	
	private void decodePattern(String pattern) {
		format = getFormat(pattern);
		// check separator
		if (pattern.contains("-")) {
			separator = '-';
		}
		int pos = pattern.indexOf('d');
		dayGroup = findGroup(pattern, pos);
		pos = pattern.indexOf('M');
		monthGroup = findGroup(pattern, pos);
		pos = pattern.indexOf('y');
		yearGroup = findGroup(pattern, pos);
	}
	
	private void setup() {
		setFocusLostBehavior(JFormattedTextField.COMMIT);
        addFocusListener(new java.awt.event.FocusAdapter() { 
        	public void focusGained(java.awt.event.FocusEvent e) {
        		SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						selectGroup = 1;
						setSelect();       
					}        			
        		});
        	}
        });
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int pos = viewToModel(new Point(e.getX(), e.getY()));
                String text = getText();
                selectGroup = findGroup(text, pos);
                // if double click, selection automatic
                if (e.getClickCount() == 1) {
                    setSelect();
                }
            }
        });
        setInputVerifier(new InputVerifier(){
			@Override
			public boolean verify(JComponent input) {
		    	String text = ((JFormattedTextField)input).getText();
		    	try {
					format.parse(text);
				} catch (ParseException e) {
					return false;
				}
				return true;
			}
			@Override
        	public boolean shouldYieldFocus(JComponent input) {
        		boolean noErrors = verify(input);
        		if (!noErrors) {
        			findError();
        		}
        		return noErrors;
        	}
        });
        updateMaps();
	}
	
	private int findGroup(String text, int pos) {
		int l = text.length();
		int first = text.indexOf(separator);
		first = (first == -1) ? l : first;
		int second = text.indexOf(separator, first + 1);
		second = (second == -1) ? l : second;
		if (pos <= first) {
			return 1;
		}
		else if (pos <= second) {
			return 2;
		}
		else {
			return 3;
		}		
	}

    private void setSelect() {
        int from = 0;
        int to = 0;
        String text = getText();
        int first = text.indexOf(separator);
        int second = text.indexOf(separator, first + 1);
        if (first == -1) {
        	to = text.length();
        	selectGroup = 1;
        } else {
        	if (second == -1) {
        		to = text.length();
        		selectGroup = 2;
        	}
        }

        switch (selectGroup) {
        case 0:
        case 1:
            from = 0;
            to = first;
            break;
        case 2:
            from = first + 1;
            to = second;
            break;
        case 3:
            from = second + 1;
            to = text.length();
            break;
        default:
            return;
        }

        setCaretPosition(from);
        moveCaretPosition(to);
    }

    private void updateMaps() {
        //left/right arrow to go backward/foward one select group        
        // update Input Map
        InputMap inputMap = getInputMap();
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        inputMap.put(key, leftAction);
        key = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        inputMap.put(key, rightAction);
        key = KeyStroke.getKeyStroke(separator);
        inputMap.put(key, rightAction);        
        
        // update Action Map
        ActionMap map = getActionMap();
        map.put(leftAction, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                --selectGroup;
                if (selectGroup < 1) {
                    selectGroup = 1;
                }
                setSelect();
            }
        });
        map.put(rightAction, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ++selectGroup;
                if (selectGroup > 3) {
                    selectGroup = 3;
                }
                setSelect();
            }
        });

    }
    
    private void findError() {
    	String text = getText();
    	int pos = -1;
    	try {
			format.parse(text);
		} catch (ParseException e) {
			pos = e.getErrorOffset();
		}
    	if (pos == -1) return;
    	Toolkit.getDefaultToolkit().beep();
    	// if pos less than input length have invalid data 
		if (pos < text.length()) {
			selectGroup = findGroup(text, pos);
			setSelect();
			return;
		}
		// if pos equals input length have illegal day or month
        int first = text.indexOf(separator);
        int second = text.indexOf(separator, first + 1);
        if (first == -1 || second == -1) {
        	setSelect();					// handles missing separators
        	return;
        }
		// try month
        int value = getGroupValue(text, monthGroup);
		if (value == 0 || value > 12) {
			selectGroup = monthGroup;
			setSelect();
			return;
		}
		// try year
		value = getGroupValue(text, yearGroup);
		if (value == 0) {
			selectGroup = yearGroup;
			setSelect();
			return;
		}		
		// must be day
		selectGroup = dayGroup;
		setSelect();
    }
    
    private int getGroupValue(String text, int group) {
    	int from = 0;
    	int to = 0;
    	int first = text.indexOf(separator);
    	int second = text.indexOf(separator, first + 1);
        switch (group) {
        case 1:
            from = 0;
            to = first;
            break;
        case 2:
            from = first + 1;
            to = second;
            break;
        case 3:
            from = second + 1;
            to = text.length();
            break;
        default:
        	return 0;
        }

    	String s = text.substring(from, to);
    	Integer i = 0;
		try {
			i = new Integer(s);
		} catch (NumberFormatException e) {
			return 0;
		}
    	return i;
    }
    
    @Override
    protected void invalidEdit() {
    	findError();
    }
    
}
