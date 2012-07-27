/*
 * $Id: ImageBrowserDemo.java 915 2006-10-22 02:59:18Z gfx $
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.swingx.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;

/**
 * @author Romain Guy <romain.guy@mac.com>
 */

public class ImageBrowserDemo extends JFrame {
    private JSlider zoomLevel;
    private JTree explorerTree;
    private ImageViewer viewer;

    private static ShadowRenderer factory = new ShadowRenderer(5, 0.70f, Color.BLACK);
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    public ImageBrowserDemo() {
        super("Image Browser Demo");

        JPanel controls = new JPanel(new BorderLayout());
        controls.add(buildZoomControl(), BorderLayout.WEST);
        controls.add(buildQuitControl(), BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              true,
                                              buildExplorer(), buildViewer());
        add(splitPane, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        setupListeners();

        pack();
        setLocationRelativeTo(null);
    }

    private void setupListeners() {
        explorerTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                FileTreeNode node = (FileTreeNode) e.getPath().getLastPathComponent();
                File path = node.getPath();
                viewer.setPath(path);
            }
        });
        zoomLevel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float ratio = zoomLevel.getValue() / 100.0f;
                viewer.setThumbSize(120 + (int) (240 * ratio));
            }
        });
    }

    private JComponent buildViewer() {
        viewer = new ImageViewer(new File("/"));
        JScrollPane scroller = new JScrollPane(viewer);
        scroller.setBorder(null);
        return scroller;
    }

    private JComponent buildExplorer() {
        explorerTree = new JTree(buildRootNode());
        JScrollPane scroller = new JScrollPane(explorerTree);
        scroller.setBorder(null);
        return scroller;
    }

    private static TreeNode buildRootNode() {
        return new FileTreeNode(null, new File("/"));
    }

    private JComponent buildQuitControl() {
        JButton button = new JButton("Quit");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.add(button);
        return panel;
    }

    private JComponent buildZoomControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.add(new JLabel("Smaller"));
        panel.add(zoomLevel = new JSlider(0, 100, 0));
        panel.add(new JLabel("Bigger"));
        return panel;
    }

    private final class ImageViewer extends JList implements Runnable {
        private File path;
        private int thumbSize = 120;
        private DefaultListModel model = new DefaultListModel();

        private ImageViewer(File path) {
            setOpaque(false);
            setPath(path);
            setModel(model);
            setLayoutOrientation(JList.HORIZONTAL_WRAP);
            setCellRenderer(new ImageRenderer());
            setThumbSize(thumbSize);
            setVisibleRowCount(-1);
        }

        public void setPath(File path) {
            this.path = path;
            model.clear();
            findImages();
        }

        private void findImages() {
            Thread finder = new Thread(this);
            finder.start();
        }

        public void run() {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.getName().endsWith(".png") ||
                    file.getName().endsWith(".jpg")) {
                    addImage(file);
                }
            }
        }

        private void addImage(final File file) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.addElement(new ImageContainer(file));
                }
            });
        }

        public int getThumbSize() {
            return thumbSize;
        }

        public void setThumbSize(int thumbSize) {
            this.thumbSize = thumbSize;
            setFixedCellHeight(thumbSize + 4 + 4);
            setFixedCellWidth(thumbSize + 4 + 4);
            revalidate();
            repaint();
        }

        private final class ImageRenderer implements ListCellRenderer {
            public Component getListCellRendererComponent(JList list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                return (Component) value;
            }
        }

        /** @noinspection InnerClassMayBeStatic*/
        private final class ImageContainer extends JComponent {
            private File imagePath;
            private BufferedImage thumb;
            private int lastThumbSize;
            private boolean loading = false;

            private BufferedImage shadow;

            private ImageContainer(File image) {
                this.imagePath = image;
                setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            }

            @Override
            public Dimension getPreferredSize() {
                Insets insets = getInsets();
                return new Dimension(getThumbSize() + insets.left + insets.right,
                                     getThumbSize() + insets.top + insets.bottom);
            }

            @Override
            protected void paintComponent(Graphics g) {
                if ((thumb == null || getThumbSize() != lastThumbSize) &&
                    !loading) {
                    pool.submit(new Runnable() {
                        public void run() {
                            loading = true;
                            BufferedImage image;
                            try {
                                image = GraphicsUtilities.loadCompatibleImage(
                                    imagePath.toURI().toURL());
                                thumb = GraphicsUtilities.createThumbnail(image, getThumbSize());
                                shadow = factory.createShadow(thumb);
                                lastThumbSize = getThumbSize();
                                viewer.repaint();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                loading = false;
                            }
                        }
                    });
                }

                if (thumb != null) {
                    if (!loading) {
                        int x = (getWidth() - thumb.getWidth()) / 2;
                        int y = (getHeight() - thumb.getHeight()) / 2;
                        g.drawImage(shadow, x - 3, y - 1, null);
                        g.drawImage(thumb, x, y, null);
                    } else {
                        int width = thumb.getWidth();
                        int height = thumb.getHeight();
                        if (width > height) {
                            float ratio = (float) width / (float) height;
                            width = getThumbSize();
                            height = (int) (width / ratio);
                        } else {
                            float ratio = (float) height / (float) width;
                            height = getThumbSize();
                            width = (int) (height / ratio);
                        }
                        int x = (getWidth() - width) / 2;
                        int y = (getHeight() - height) / 2;
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        //g.drawImage(shadow, x - 3, y, width, height, null);
                        g.drawImage(thumb, x, y, width, height, null);
                    }
                }
            }
        }
    }

    private static final class FileTreeNode implements TreeNode {
        private File path;
        private List<File> directories;
        private FileTreeNode parent;

        private FileTreeNode(FileTreeNode parent, File path) {
            this.parent = parent;
            this.path = path;
            createChildren();
        }

        @Override
        public String toString() {
            return parent == null ? "/" : path.getName();
        }

        public File getPath() {
            return path;
        }

        private void createChildren() {
            if (path.isFile()) {
                return;
            }

            File[] files = path.listFiles();
            directories = new ArrayList<File>();

            if (files == null) {
                return;
            }

            for (File file : files) {
                if (file.isDirectory() && file.getName().charAt(0) != '.') {
                    directories.add(file);
                }
            }
        }

        public TreeNode getChildAt(int childIndex) {
            return new FileTreeNode(this, directories.get(childIndex));
        }

        public int getChildCount() {
            return directories.size();
        }

        public TreeNode getParent() {
            return parent;
        }

        public int getIndex(TreeNode node) {
            FileTreeNode fileNode = (FileTreeNode) node;
            return directories.indexOf(fileNode.getPath());
        }

        public boolean getAllowsChildren() {
            return true;
        }

        public boolean isLeaf() {
            return path.isFile();
        }

        /** @noinspection RawUseOfParameterizedType*/
        public Enumeration children() {
            return new Enumeration() {
                private Iterator<File> it = directories.listIterator();

                public boolean hasMoreElements() {
                    return it.hasNext();
                }

                public Object nextElement() {
                    return it.next();
                }
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageBrowserDemo().setVisible(true);
            }
        });
    }
}
