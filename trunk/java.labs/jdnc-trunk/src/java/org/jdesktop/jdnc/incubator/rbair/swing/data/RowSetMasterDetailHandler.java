/*
 * Created on Sep 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This handler will cause a detail RowSetDataModel to refresh itself when
 * the master indicates a need to. It contains a list of field names in the
 * master which will be used to get values used as params to the detail's
 * sql statement.
 * @author Richard Bair
 */
public class RowSetMasterDetailHandler implements MasterDetailHandler {
	private List/*<String>*/ fieldNames = new ArrayList/*<String>*/();
	
	public void addFieldName(String fieldName) {
		//I'm using a List instead of a Set in order to preserve the insertion
		//order.
		if (!fieldNames.contains(fieldName)) {
			fieldNames.add(fieldName);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.MasterDetailHandler#handleMasterChanged(org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel, org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel)
	 */
	public void handleMasterChanged(DataModel master, DataModel detail) {
		if (!(detail instanceof RowSetDataModel)) {
			throw new IllegalArgumentException("The detail DataModel must be " +
					"of type RowSetDataModel");
		}

		RowSetDataModel d = (RowSetDataModel)detail;
		
		//If the detail data model currently contains items, save them
		//before changing to a new detail
		if (d.getRecordCount() > 0) {
			try {
				((RowSetDataSource)d.getDataSource()).saveDataModel(d);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//construct a list of params based on the fieldNames
		List params = new ArrayList(fieldNames.size());
		boolean wasNull = false;
		for (int i=0; i<fieldNames.size(); i++) {
			Object val = master.getValue((String)fieldNames.get(i));
			if (val == null) {
			    wasNull = true;
			}
			params.add(val);
		}
		
		//it is possible that one of the params are null. If that is the case,
		//flush the detail and do nothing.
		if (!wasNull) {
			//set the select params
			d.setSelectParams(params);
			
			//have the DataSource refresh this particular row set
			try {
				//don't bother refreshing if the data source is not connected
				if (d.getDataSource().isConnected()) {
					((RowSetDataSource)d.getDataSource()).refreshDataModel(d);
				}
			} catch (Exception e) {
				System.err.println("Failed to refresh a detail RowSetDataModel");
				e.printStackTrace();
			}
		} else {
		    //TODO flush detail DataModel
		}
	}

}
