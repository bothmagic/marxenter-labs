package org.jdesktop.swingx;

import org.jdesktop.swingx.JXTextField;


public class JXTextFieldBeanInfo extends JXPromptBeanInfo {
	public JXTextFieldBeanInfo() {
		this(JXTextField.class);
	}
	
	protected JXTextFieldBeanInfo(Class<? extends JXTextField> beanClass) {
		super(beanClass);
		
		setPreferred(true, "outerMargin");
	}
}
