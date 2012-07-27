/*
 * Created on 01.04.2009
 *
 */
package org.jdesktop.swingx.plaf.basic;


import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;


public class BasicWMonthViewUI extends BasicMonthViewUI {

    @SuppressWarnings({"UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new BasicWMonthViewUI();
    }

    @Override
    protected MyRenderingHandler createRenderingHandler() {
        Object handler = monthView.getClientProperty("renderingHandler");
        if (handler instanceof MyRenderingHandler) {
            return (MyRenderingHandler) handler;
        }
        monthView.putClientProperty("renderingHandler", new MyRenderingHandler());
        return (MyRenderingHandler) monthView.getClientProperty("renderingHandler");
    }
    
    @Override
    public MyRenderingHandler getRenderingHandler() {
        return (MyRenderingHandler) super.getRenderingHandler();
    }
    
    
}
