/*
 * $Id: PDFOperation.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;

/**
 * @author Richard Bair
 */
public class PDFOperation {
	private Object[] operands;
	private PDFOperator operator;
	
	/**
	 * 
	 */
	public PDFOperation(Object[] operands, PDFOperator operator) {
		this.operands = operands == null ? new Object[0] : operands;
		assert operator != null;
		this.operator = operator;
	}
	/**
	 * @return Returns the operands.
	 */
	public Object[] getOperands() {
		return operands;
	}
	/**
	 * @return Returns the operator.
	 */
	public PDFOperator getOperator() {
		return operator;
	}
}
