/*
 * $Id: InputContextTest.java 3099 2009-04-17 08:48:35Z kleopatra $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.event;

import java.awt.AWTEvent;
import java.awt.event.FocusEvent;
import java.awt.im.InputContext;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.test.InputEventDispatcherReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * TODO add type doc
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class InputContextTest extends InteractiveTestCase {

    private DispatchingInputContext context;
    private InputEventDispatcher emptyInputEventDispatcher;
    private InputContext coreInputContext;
    private InputEventDispatcherReport report;

    /**
     * Test contract: no dispatch if no delegate
     */
    @Test
    public void testEventNotDispatched() {
        context.setInputEventDispatcher(report);
        JComponent source = new JLabel();
        FocusEvent event = new FocusEvent(source, FocusEvent.FOCUS_GAINED);
        context.dispatchEvent(event);
        assertEquals(0, report.getEventCount());
    }
    
    /**
     * Test contract: dispatch if  delegate
     */
    @Test
    public void testEventDispatched() {
        context.setInputEventDispatcher(report);
        context.getInputContext(coreInputContext);
        JComponent source = new JLabel();
        FocusEvent event = new FocusEvent(source, FocusEvent.FOCUS_GAINED);
        context.dispatchEvent(event);
        assertEquals(1, report.getEventCount());
        assertSame(event, report.getLastEvent());
    }
    
    @Test
    public void testDelegateMethods() {
        context.setInputEventDispatcher(emptyInputEventDispatcher);
        context.getInputContext(coreInputContext);
        assertSame(coreInputContext.getLocale(), context.getLocale());
    }
    
    @Test
    public void testDelegateWithDispatcher() {
        context.setInputEventDispatcher(emptyInputEventDispatcher);
        InputContext result = context.getInputContext(coreInputContext);
        assertSame(context, result);
        assertSame(coreInputContext, context.getDelegate());
    }
    
    @Test
    public void testDelegateInitial() {
        assertNull(context.getDelegate());
    }
    
    @Test
    public void testDelegateSet() {
        InputContext result = context.getInputContext(coreInputContext);
        assertSame("context without dispatcher must return delegate", coreInputContext, result);
    }
    
    @Test
    public void testDispatcherSet() {
        context.setInputEventDispatcher(emptyInputEventDispatcher);
        assertSame(emptyInputEventDispatcher, context.getInputEventDispatcher());
    }

    @Test
    public void testDispatcherInitialNull() {
        assertNull("initial dispatcher must be null", context.getInputEventDispatcher());
    }
    
    private InputEventDispatcher createEmptyDispatcher() {
        InputEventDispatcher d = new InputEventDispatcher() {

            public void dispatchEvent(AWTEvent e) {
                // TODO Auto-generated method stub
                
            }};
        return d;
    }
    
    private InputContext createInputContext() {
        return InputContext.getInstance();
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        context = new DispatchingInputContext();
        emptyInputEventDispatcher = createEmptyDispatcher();
        coreInputContext = createInputContext();
        report = new InputEventDispatcherReport();
    }
    
    
}
