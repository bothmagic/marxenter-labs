/*
 * $Id: JXScrollMapTest.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 20102006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.test.EDTRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Karl Schaefer
 */
@RunWith(EDTRunner.class)
public class JXScrollMapTest {
    private JXScrollMap scrollMap;
    
    @Before
    public void setUp() {
        scrollMap = new JXScrollMap();
    }
    
    @Test
    public void testDefaults() {
        assertThat(scrollMap.getIcon(), is(notNullValue()));
        assertThat(scrollMap.getMaximumPopupSize(), is(notNullValue()));
        assertThat(scrollMap.getText(), is(nullValue()));
        assertThat(scrollMap.getViewport(), is(nullValue()));
        assertThat(scrollMap.isLightWeightPopupEnabled(), is(true));
        assertThat(scrollMap.isSynchronizedScrolling(), is(false));
    }
    
    @Test
    public void testViewportSynchronization() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setCorner(ScrollPaneConstants.LOWER_TRAILING_CORNER, scrollMap);
        //fake out display hierarchy
        scrollPane.addNotify();
        assertThat(scrollMap.getViewport(), is(sameInstance(scrollPane.getViewport())));
        
        scrollPane.setViewport(new JViewport());
        assertThat(scrollMap.getViewport(), is(sameInstance(scrollPane.getViewport())));
    }
    
    @Test
    public void testScrollPaneListeners() {
        JScrollPane scrollPane = new JScrollPane();
        int pclCount = scrollPane.getPropertyChangeListeners().length;
        
        scrollPane.setCorner(ScrollPaneConstants.LOWER_TRAILING_CORNER, scrollMap);
        
        //fake out display hierarchy
        scrollPane.addNotify();
        
        assertThat(scrollPane.getPropertyChangeListeners().length, is(not(pclCount)));
        
        //fake out display hierarchy
        scrollPane.removeNotify();
        
        assertThat(scrollPane.getPropertyChangeListeners().length, is(pclCount));
    }
}
