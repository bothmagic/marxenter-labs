/*
 * Created on Oct 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jdesktop.jdnc.incubator.rbair.hibernate;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DefaultDataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.JavaBeanDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;

/**
 * A datamodel for a Hibernate DataSource. This DataModel contains the
 * information necessary to load objects from a HibernateDataSource.
 * @author Richard Bair
 */
public class HibernateDataModel extends JavaBeanDataModel {
	private Query query;
	private List queryParams = new ArrayList();

	/**
	 * @param beanClass
	 * @throws IntrospectionException
	 */
	public HibernateDataModel(Class beanClass) throws IntrospectionException {
		super(beanClass);
	}
	/**
	 * @param beanClass
	 * @param metaData
	 * @throws IntrospectionException
	 */
	public HibernateDataModel(Class beanClass, MetaData[] metaData)
			throws IntrospectionException {
		super(beanClass, metaData);
	}
	
	protected Object getObjectId() {
		return null;
	}
	
	public void setQuery(Query q) {
		this.query = q;
	}
	
	protected Query getQuery() {
		return query;
	}
	
	protected void setQueryParams(List params) {
		queryParams = params == null ? new ArrayList() : params;
	}
	
	protected List getQueryParams() {
		return queryParams;
	}

    /**
     * FIXME: This method is a total hack -- in place for transaction demo
     * purposes
     * @param ds
     */
    public void setDataSource(DataSource ds) {
    	if (dataSource != null) {
    		//TODO remove me from the old DataSource
    	}
    	dataSource = ds;
    	if (dataSource != null) {
    		try {
    			((HibernateDataSource)dataSource).addDataModel(this);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
}
