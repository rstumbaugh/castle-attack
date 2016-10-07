package com.rs.castleattack.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Drawer 
{
	public static void drawTextWithUnderlay(int x, int y, String text, Color textColor, Color underlay, Font font, Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setFont(font);
		
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(text, g2d);
		
		g2d.setColor(underlay);
        g2d.fillRect(x-5, (int) (y-r.getHeight()+10), (int)r.getWidth()+5, (int)r.getHeight());
        
        g2d.setColor(textColor);
        
        g2d.drawString(text, x, y);
	}
}
