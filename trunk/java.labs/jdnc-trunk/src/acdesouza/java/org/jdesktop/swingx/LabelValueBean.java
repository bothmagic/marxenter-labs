package org.jdesktop.swingx;

import java.io.Serializable;

/**
 * Strongly inspired in Struts' LabelValueBean class.
 * 
 * @author AC de Souza
 *
 */
public class LabelValueBean<T> implements Serializable, Comparable{
	private static final long serialVersionUID = 1L;

	private final String label;
	private final T value;
	
	public LabelValueBean(final String label, final T value){
		this.label = label;
		this.value = value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLabel();
	}
	
	/**
	 * The order is by the label.
	 *   
	 * @param o
	 * @return
	 * <ul>
	 * <li>-1 this is less then</li>
	 * <li>0 this is equals</li>
	 * <li>+1 this is greater then</li>
	 * </ul>
	 * @see Comparable#compareTo(T)
	 */	
	@SuppressWarnings("unchecked")
	public int compareTo(Object o) {
		LabelValueBean other = (LabelValueBean)o; 
		int compareTo = this.label.compareTo(other.label);
		return compareTo;
	}
	
	/**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	    final int PRIME = 31;
	    int result = 1;
	    result = PRIME * result + ((label == null) ? 0 : label.hashCode());
	    result = PRIME * result + ((value == null) ? 0 : value.hashCode());
	    return result;
    }

	/**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    //Sugestion from a co-worker, Ana Paula Valente Pais.
	    if( obj == null )
	    	return false;
	    if ( !(obj instanceof LabelValueBean) )
		    return false;

		final LabelValueBean<T> other = this.getClass().cast(obj) ;
	    if (label == null) {
		    if (other.label != null)
			    return false;
	    } else if (!label.equals(other.label))
		    return false;
	    if (value == null) {
		    if (other.value != null)
			    return false;
	    } else if (!value.equals(other.value))
		    return false;
	    return true;
    }

	public String getLabel() {
		return label;
	}
	public T getValue() {
		return value;
	}
}	
