package org.jdesktop.swingx;

import java.awt.Shape;

/**
 * Draws a simple shape
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.2 $</p>
 */
public interface JXShapeGenerator
{
  /**
   * Get a shape
   * @param return the shape in the parent coordianates if true
   * @return the generated shape
   */ 
  public Shape getShape( boolean useParentSpace );
}
