package utility.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBar extends BasicScrollBarUI {
	Color c1 = new Color(115,129,139);
  private final Dimension d = new Dimension();

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return new JButton() {
      @Override
      public Dimension getPreferredSize() {
        return d;
      }
    };
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return new JButton() {
      @Override
      public Dimension getPreferredSize() {
        return d;
      }
    };
  }

  @Override
  protected void paintTrack(Graphics g, JComponent c, Rectangle r) {	  
  }

  @Override
  protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    JScrollBar sb = (JScrollBar) c;
    if (!sb.isEnabled() || r.width > r.height) {
      return;
    } else if (isDragging || isThumbRollover()) {
      g2.setPaint(c1);
      g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
    } else {
      g2.setPaint(c1);
      g2.fillRoundRect((r.x+r.width)/2, r.y, 3, r.height, 3, 1);
    }
    g2.dispose();
  }

  @Override
  protected void setThumbBounds(int x, int y, int width, int height) {
    super.setThumbBounds(x, y, width, height);
    scrollbar.repaint();
  }
}