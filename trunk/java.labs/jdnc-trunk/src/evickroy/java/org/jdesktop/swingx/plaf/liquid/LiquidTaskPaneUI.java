package org.jdesktop.swingx.plaf.liquid;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;

import com.birosoft.liquid.skin.Skin;

public class LiquidTaskPaneUI extends BasicTaskPaneUI {

  private static Skin skin;
  private static Skin skinButton;
  private static Skin arrowSkins[] = new Skin[2];

  private static int CONTROL_HEIGHT = 22;
  
  private static final String[] arrowfiles = {"spinneruparrows.png","spinnerdownarrows.png"};
  
  static {
    TITLE_HEIGHT = 27;
  }
  
  public static ComponentUI createUI(JComponent c) {
    return new LiquidTaskPaneUI();
  }

  protected Border createPaneBorder() {
    return new LiquidPaneBorder();
  }
  
  protected Border createContentPaneBorder() {
    return new CompoundBorder(new LiquidContentPaneBorder(new Color(145, 155, 156)), BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }
  

  /**
   * The border around the content pane
   */
  protected class LiquidContentPaneBorder implements Border {
    Color color;
    
    public LiquidContentPaneBorder(Color color) {
      this.color = color;
    }
    
    public Insets getBorderInsets(Component c) {
      return new Insets(0, 3, 6, 3);
    }

    public boolean isBorderOpaque() {
      return true;
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Insets insets =  new Insets(0, 0, 0, 0);
        Insets contentInsets = getBorderInsets(c);

        x = insets.left;
        y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

        Color oldColor = g.getColor();
        Color inBorderClr = new Color(198, 214, 252); //blue inner border
        Color grayBorderClr = new Color(145, 155, 156); //darker gray border
        Color darkShadowClr = new Color(208, 206, 191); //darker shadow
        Color lightShadowClr = new Color(227, 224, 208); //lighter shadow

// RIGHT    
        g.setColor(inBorderClr);
        g.drawLine((w-contentInsets.right), y + contentInsets.top, (w-contentInsets.right), (y + h) - contentInsets.bottom+1);
        g.drawLine((w-contentInsets.right)+1, y + contentInsets.top, (w-contentInsets.right)+1, (y + h) - contentInsets.bottom+1);
        g.setColor(grayBorderClr);
        g.drawLine((w-contentInsets.right)+2, y + contentInsets.top, (w-contentInsets.right)+2, (y + h) - contentInsets.bottom+1);

// LEFT
        g.setColor(grayBorderClr);
        g.drawLine(x, y + contentInsets.top, x, (y + h) - contentInsets.bottom+2);
        g.setColor(inBorderClr);
        g.drawLine(x+1, y + contentInsets.top, x+1, (y + h) - contentInsets.bottom+2);
        g.drawLine(x+2, y + contentInsets.top, x+2, (y + h) - contentInsets.bottom+2);

// BOTTOM            
        g.setColor(inBorderClr);
        g.drawLine(x + contentInsets.left, height - contentInsets.bottom, w - contentInsets.right - 1, height - contentInsets.bottom);
        g.drawLine(x + contentInsets.left, height - contentInsets.bottom+1, w - contentInsets.right - 1, height - contentInsets.bottom+1);

        g.setColor(grayBorderClr);
        g.drawLine(x+1, height - contentInsets.bottom+2, w - contentInsets.right+2, height - contentInsets.bottom+2);

        g.setColor(darkShadowClr);
        g.drawLine(x+1, height - contentInsets.bottom+3, w - contentInsets.right+2, height - contentInsets.bottom+3);
        g.drawLine(x+1, height - contentInsets.bottom+4, w - contentInsets.right+2, height - contentInsets.bottom+4);

        g.setColor(lightShadowClr);
        g.drawLine(x+1, height - contentInsets.bottom+5, w - contentInsets.right+2, height - contentInsets.bottom+5);

        g.setColor(oldColor);
    }
  }

  
  protected class LiquidPaneBorder extends PaneBorder {
    
    public LiquidPaneBorder() {
      borderColor = new Color(145, 155, 156);
    }
    
    protected void paintTitleBackground(JXTaskPane group, Graphics g) {
//        int index = 0;
        int index = !group.isExpanded() ? 0 : 2;

//        if (group.isSpecial()) {
//            index = 1;
//        } else if (mouseOver) {
//            index = 2;
//        }

//        Graphics2D g2 = (Graphics2D)g;
//        GradientPaint gp = new GradientPaint(0, TITLE_HEIGHT, new Color(102, 186, 255), 0, 0, new Color(60, 144, 233));
//        g2.setPaint(gp);
//        g2.fillRect(0, 0, group.getWidth() - 1, TITLE_HEIGHT - 1);
//        g2.setPaint(new Color(153, 153, 153));
//        g2.drawRect(0, 0, group.getWidth() - 1, TITLE_HEIGHT - 1);
        
        getSkin().draw(g, index, 0, 0, group.getWidth(), TITLE_HEIGHT);

        Paint oldPaint = ((Graphics2D)g).getPaint();
    }

    protected void paintExpandedControls(JXTaskPane group, Graphics g, int x,
      int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
        
        int index = group.isExpanded() ? 2 : 0;
        int type = group.isExpanded() ? SwingConstants.NORTH : SwingConstants.SOUTH;
        
        getSkinButton().draw(g, index, x-5, 2, CONTROL_HEIGHT, CONTROL_HEIGHT);//width, height);
        
        Color oldColor = g.getColor();
        g.setColor(new Color(128,144,162));  //128,144,162

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintChevronControls(group, g, x-6+index, index, CONTROL_HEIGHT, CONTROL_HEIGHT);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      
        g.setColor(oldColor);
//        getArrowSkin(type).draw(g, 3, x+2+index, 10, 7, 6);
    }

    protected boolean isMouseOverBorder() {
      return true;
    }

    protected void paintFocus(Graphics g, Color paintColor, int x, int y, int width, int height) {
        g.setColor(paintColor);
        BasicGraphicsUtils.drawDashedRect(
          g,
          x-1,
          y-1,
          width+2,
          height+2);
    }

    protected void configureLabel(JXTaskPane group) {
      label.applyComponentOrientation(group.getComponentOrientation());
      label.setFont(group.getFont());
      label.setText(group.getTitle());
      label.setIcon(group.getIcon() == null ? new EmptyIcon() : group.getIcon());      
    }
  }

    public Skin getSkin() {
        if (skin == null) {
            skin = new Skin("tableheader.png", 8, 4, 13, 4, 10);
        }

        return skin;
    }
  
    public Skin getSkinButton() {
        if (skinButton == null) {
            skinButton = new Skin("button.png", 5, 10, 10, 12, 12);
//            skinButton.colourImage();
        }

        return skinButton;
    }

    protected Skin getArrowSkin(int type)
    {
        switch (type)
        {
            case SwingConstants.NORTH:
                arrowSkins[0]=new Skin(arrowfiles[0], 4, 2);
                return arrowSkins[0];
            case SwingConstants.SOUTH:
                arrowSkins[1]=new Skin(arrowfiles[1], 4, 2);
                return arrowSkins[1];
            default:
                throw new IllegalStateException("type must be either SwingConstants.SOUTH or SwingConstants.NORTH for XPSpinnerButton");
        }
    }
    
}
