/*
 * $Id: DefaultTreeTableSelectionModel.java 1374 2007-06-04 13:34:55Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.treetable;

import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;

/**
 *
 */
public class DefaultTreeTableSelectionModel extends DefaultTreeSelectionModel
        implements TreeTableSelectionModel {

    /**
     * {@inheritDoc}
     */
    public void addListSelectionListener(ListSelectionListener x) {
        listSelectionModel.addListSelectionListener(x);
    }

    /**
     * {@inheritDoc}
     */
    public void addSelectionInterval(int index0, int index1) {
        listSelectionModel.addSelectionInterval(index0, index1);
    }

    /**
     * {@inheritDoc}
     */
    public int getAnchorSelectionIndex() {
        return listSelectionModel.getAnchorSelectionIndex();
    }

    /**
     * {@inheritDoc}
     */
    public int getLeadSelectionIndex() {
        return listSelectionModel.getLeadSelectionIndex();
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxSelectionIndex() {
        return listSelectionModel.getMaxSelectionIndex();
    }

    /**
     * {@inheritDoc}
     */
    public int getMinSelectionIndex() {
        return listSelectionModel.getMinSelectionIndex();
    }

    /**
     * {@inheritDoc}
     */
    public boolean getValueIsAdjusting() {
        return listSelectionModel.getValueIsAdjusting();
    }

    /**
     * {@inheritDoc}
     */
    public void insertIndexInterval(int index, int length, boolean before) {
        listSelectionModel.insertIndexInterval(index, length, before);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelectedIndex(int index) {
        return listSelectionModel.isSelectedIndex(index);
    }

    /**
     * {@inheritDoc}
     */
    public void removeIndexInterval(int index0, int index1) {
        listSelectionModel.removeIndexInterval(index0, index1);
    }

    /**
     * {@inheritDoc}
     */
    public void removeListSelectionListener(ListSelectionListener x) {
        listSelectionModel.removeListSelectionListener(x);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSelectionInterval(int index0, int index1) {
        listSelectionModel.removeSelectionInterval(index0, index1);
    }

    /**
     * {@inheritDoc}
     */
    public void setAnchorSelectionIndex(int index) {
        listSelectionModel.setAnchorSelectionIndex(index);
    }

    /**
     * {@inheritDoc}
     */
    public void setLeadSelectionIndex(int index) {
        listSelectionModel.setLeadSelectionIndex(index);
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionInterval(int index0, int index1) {
        listSelectionModel.setSelectionInterval(index0, index1);
    }

    /**
     * {@inheritDoc}
     */
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
        listSelectionModel.setValueIsAdjusting(valueIsAdjusting);
    }
}
