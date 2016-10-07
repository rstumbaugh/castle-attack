package com.rs.castleattack.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.rs.castleattack.main.Main;


@SuppressWarnings("serial")
public class TextButton extends Button 
{
	public static final int WIDTH = Main.WIDTH / 3,
							HEIGHT = Main.HEIGHT / 7;
	
	private String message;
	
	
	public TextButton(String message, Command command)
	{
		super(WIDTH, HEIGHT, command);
		
		this.message = message;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		Font font =  Main.DEFAULT_FONT.deriveFont(30f);
		g2d.setFont(font);
		
		FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(message, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();

		g2d.drawString(message, x, y);
	}
}
