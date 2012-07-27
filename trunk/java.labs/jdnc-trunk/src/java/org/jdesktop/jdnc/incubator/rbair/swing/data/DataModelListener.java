/*
 * $Id: DataModelListener.java 15 2004-09-04 23:06:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.EventListener;

/**
 * Listener interface which receives events when various events occur within
 * a DataModel object. Three types of events are handled by this interface.
 * <p>
 * First, when <em>values</em> are changed in the DataModel for existing records, a
 * value-changed event is fired. Listeners interested in handling these events
 * must implement the <code>valueChanged(ValueChangeEvent e)</code> method.
 * These events can span a range of rows and also a range of fields (either a
 * specific field or all fields in the DataModel). @see ValueChangeEvent for
 * more details.
 * <p>
 * Second, when the <em>records</em> in a  DataModel are added or removed, a
 * model-changed event is fired. Listeners interested in handling these events
 * must implement the <code>modelChanged(ModelChangeEvent e)</code> method.
 * <p>
 * The third type of event is for when <em>MetaData</em> within the DataModel
 * change. This refers not to changes within the actual MetaData objects
 * themselves (they maintain their own listeners), but rather to the *set* of
 * MetaData contained within the DataModel. For instance, if the DataModel used
 * to contain MetaData for the query &quot;select * from customer&quot; and that
 * MetaData is replaced with MetaData for the query &quot;select * from 
 * employee&quot;.
 *
 * @see DataModel#addDataModelListener
 *
 * @author Amy Fowler
 * @author Richard Bair
 * @version 1.0
 */
public interface DataModelListener extends EventListener {
    /**
     * Invoked when the value of a named data field changes
     * @param e ValueChangeEvent describing the event
     */
    void valueChanged(ValueChangeEvent e);
    /**
     * Invoked when the record set in the DataModel changes,
     * such as when records are added or removed.
     * @param e
     */
    void modelChanged(ModelChangeEvent e);
    /**
     * Invoked when the set of MetaData for the DataModel changes.
     * @param e
     */
    void metaDataChanged(MetaDataChangeEvent e);
}
