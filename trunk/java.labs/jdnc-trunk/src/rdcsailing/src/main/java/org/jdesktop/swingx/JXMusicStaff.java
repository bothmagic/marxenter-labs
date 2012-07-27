/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx;

import javax.swing.JTextPane;
import org.jdesktop.swingx.music.MusicalStaffEditorKit;
import org.jdesktop.swingx.music.MusicalStaffUI;

/**
 * Renders the contents as a musical staff
 * @author Ryan Cuprak
 */
public class JXMusicStaff extends JTextPane {

    public JXMusicStaff() {
        this.setEditorKit(new MusicalStaffEditorKit());
        setText("Hello World!");
    }

    /**
     * Make sure we make the right UI
     */
    @Override
    public void updateUI() {
        setUI(new MusicalStaffUI());
    }
}
