/*
 * Copyright (c) 2005-2010 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package test.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.*;

import org.pushingpixels.flamingo.api.bcb.*;
import org.pushingpixels.flamingo.api.bcb.core.BreadcrumbFileSelector;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.StringValuePair;

public class FileExplorerStates extends JFrame {
	private ExplorerFileViewPanel<File> filePanel;

	private BreadcrumbFileSelector bar;

	public FileExplorerStates() {
		super("File explorer");

		this.bar = new BreadcrumbFileSelector();

		this.setLayout(new BorderLayout());
		this.add(bar, BorderLayout.NORTH);

		this.filePanel = new ExplorerFileViewPanel<File>(bar, CommandButtonDisplayState.BIG,
				null);
		JScrollPane fileListScrollPane = new JScrollPane(this.filePanel);

		this.bar.getModel().addPathListener(new BreadcrumbPathListener() {
			@Override
			public void breadcrumbPathEvent(BreadcrumbPathEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final List<BreadcrumbItem<File>> newPath = bar
								.getModel().getItems();
						if (newPath.size() > 0) {
							SwingWorker<List<StringValuePair<File>>, Void> worker = new SwingWorker<List<StringValuePair<File>>, Void>() {
								@Override
								protected List<StringValuePair<File>> doInBackground()
										throws Exception {
									return bar.getCallback().getLeafs(newPath);
								}

								@Override
								protected void done() {
									try {
										filePanel.setFolder(get());
										// fileList
										// .setIconDimension(currIconSize);
									} catch (Exception exc) {
									}
								}
							};
							worker.execute();
						}
						return;
					}
				});
			}
		});

		final JComboBox states = new JComboBox(new DefaultComboBoxModel(
				new Object[] { CommandButtonDisplayState.BIG, CommandButtonDisplayState.TILE,
						CommandButtonDisplayState.MEDIUM, CommandButtonDisplayState.SMALL }));
		states.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CommandButtonDisplayState selected = (CommandButtonDisplayState) states.getSelectedItem();
				filePanel.cancelMainWorker();
				filePanel.setIconState(selected);
			}
		});

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		controls.add(states);
		this.add(controls, BorderLayout.SOUTH);
		this.add(fileListScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Main method for testing.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String... args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				FileExplorerStates test = new FileExplorerStates();
				test.setSize(500, 400);
				test.setLocationRelativeTo(null);
				test.setVisible(true);
				test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
