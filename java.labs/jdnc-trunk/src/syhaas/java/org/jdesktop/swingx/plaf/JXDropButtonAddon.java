/*
 * $Id: JXDropButtonAddon.java 2686 2008-09-08 10:34:48Z osbald $
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

package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXDropButton;
import org.jdesktop.swingx.plaf.basic.BasicDropButtonUI;
import org.jdesktop.swingx.plaf.windows.WindowsDropButtonUI;

/**
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class JXDropButtonAddon extends AbstractComponentAddon
{
    public JXDropButtonAddon() {
        super("JXDropButton");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
	{
        super.addBasicDefaults(addon, defaults);
        defaults.add(JXDropButton.UI_CLASS_ID, BasicDropButtonUI.class.getName());
    }

    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
    {
        super.addMetalDefaults(addon, defaults);
        defaults.add(JXDropButton.UI_CLASS_ID, BasicDropButtonUI.class.getName());
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
	{
        super.addWindowsDefaults(addon, defaults);
        defaults.add(JXDropButton.UI_CLASS_ID, WindowsDropButtonUI.class.getName());
    }

    /*@Override
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
    {
        super.addMacDefaults(addon, defaults);
        defaults.addAll(Arrays.asList(new Object[]
        {
			JXDropButton.UI_CLASS_ID,
			"org.jdesktop.swingx.plaf.basic.BasicDropButtonUI"
        }));
    }*/
}
