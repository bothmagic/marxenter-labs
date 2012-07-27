package org.jdesktop.incubator;

import org.jdesktop.beans.AbstractBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Simple TextField and browse button combination for the input of filenames.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 28-Nov-2006
 * Time: 11:45:48
 */

public class JXFilenameField extends JPanel {
    private FilenameFieldModel model;
    private JFileChooser fileChooser;
    private JTextField filenameField;
    private JButton browseButton;

    private static final long serialVersionUID = -1L;

    //TODO what about UI delegates, I18N, BeanInfo etc..

    static {
        UIManager.getDefaults().put("JXFilenameField.button", "Browse...");
        UIManager.getDefaults().put("JXFilenameField.columns", 15);
    }

    public JXFilenameField() {
        this(new JFileChooser(), null);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public JXFilenameField(String filename) {
        this(new JFileChooser(filename), filename);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public JXFilenameField(JFileChooser fileChooser) {
        this(fileChooser, null);
    }

    public JXFilenameField(JFileChooser fileChooser, String filename) {
        this.fileChooser = fileChooser;
        this.model = new FilenameFieldModel(filename);
        fileChooser.setSelectedFile(filename != null ? new File(filename) : null);
        initComponents();
        initActions();
    }

    void initComponents() {
        JComponent view = this;
        view.setLayout(new GridBagLayout());
        filenameField = new JTextField(model.getSelectedFilename(), UIManager.getInt("JXFilenameField.columns"));
        filenameField.setText(getSelectedFilename());
        filenameField.setCaretPosition(filenameField.getText().length());
        filenameField.setSelectionStart(0);
        filenameField.setSelectionEnd(filenameField.getText().length());
        filenameField.setMinimumSize(filenameField.getPreferredSize());
        browseButton = new JButton();
        view.add(filenameField,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        view.add(browseButton,
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        view.setOpaque(false);
    }

    void initActions() {
        final Action commitAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String filename = filenameField.getText().trim();
                getModel().setSelectedFilename(filename);
            }
        };
        filenameField.setAction(commitAction);
        filenameField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!e.isTemporary()) {
                    commitAction.actionPerformed(new ActionEvent(filenameField, e.getID(), "focusLost"));
                }
            }
        });
        browseButton.setAction(new BrowseAction(model, commitAction));

        model.addPropertyChangeListener(FilenameFieldModel.SELECTED_FILENAME, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String value = (String) event.getNewValue();
                filenameField.setText(value != null ? value : "");
            }
        });
    }

    public FilenameFieldModel getModel() {
        return model;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        filenameField.setEnabled(b);
        browseButton.getAction().setEnabled(b);
    }

    public int getFileSelectionMode() {
        return fileChooser.getFileSelectionMode();
    }

    public void setFileSelectionMode(int mode) {
        fileChooser.setFileSelectionMode(mode);
    }

    public String getSelectedFilename() {
        return getModel().getSelectedFilename();
    }

    public File getSelectedFile() {
        return new File(getSelectedFilename());
    }

    public URL getSelectedURL() throws IllegalArgumentException {
        try {
            return getSelectedFile().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setSelectedFile(File file) {
        getModel().setSelectedFilename(file.getPath());
    }


    class BrowseAction extends AbstractAction {
        private Action subaction;
        private FilenameFieldModel model;

        private static final long serialVersionUID = -1L;

        public BrowseAction(FilenameFieldModel model, Action subaction) {
            super(UIManager.getString("JXFilenameField.button"));
            this.model = model;
            this.subaction = subaction;
        }

        public void actionPerformed(ActionEvent event) {
            subaction.actionPerformed(event);
            fileChooser.setSelectedFile(new File(model.getSelectedFilename()));
            int value = fileChooser.showOpenDialog(JXFilenameField.this);
            if (value == JFileChooser.APPROVE_OPTION) {
                model.setSelectedFilename(fileChooser.getSelectedFile().getPath());
            }
        }
    }


    public static class FilenameFieldModel extends AbstractBean {
        private String selectedFilename;
        public static final String SELECTED_FILENAME = "selectedFilename";

        public FilenameFieldModel() {
        }

        public FilenameFieldModel(String selectedFilename) {
            this.selectedFilename = selectedFilename;
        }

        public String getSelectedFilename() {
            return selectedFilename;
        }

        public void setSelectedFilename(String selectedFilename) {
            firePropertyChange(SELECTED_FILENAME, getSelectedFilename(), this.selectedFilename = selectedFilename);
        }
    }
}
