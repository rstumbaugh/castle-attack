package com.rs.castleattack.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.rs.castleattack.main.Main;



@SuppressWarnings("serial")
public class GameMenuButton extends Button
{
	public static int WIDTH = 137,
					  HEIGHT = GameMenu.HEIGHT - GameMenu.BORDER*2;
	
	public static int ICON_SIZE = HEIGHT / 2;
	

	private BufferedImage icon;
	private String message;
	
	private Color color;
	
	public GameMenuButton(BufferedImage icon, String message, Command command)
	{
		super(WIDTH, HEIGHT, command);
		this.icon = icon;
		this.message = message;
	}
	
	public GameMenuButton(Color color, String message, Command command)
	{
		this((BufferedImage)null, message, command);
		this.color = color;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getText()
	{
		return message;
	}
	
	public BufferedImage getImage()
	{
		return icon;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D)g;

		Font font =  Main.DEFAULT_FONT.deriveFont(25f);
		g2d.setFont(font);

		FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(message, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2 + 2;
        int y = HEIGHT/3 + ICON_SIZE + 5;
        
        if(icon == null)
        {
        	g2d.setColor(color);
        	g2d.fillOval(WIDTH/2 - (ICON_SIZE/2), HEIGHT/3 - (ICON_SIZE/2), ICON_SIZE, ICON_SIZE);
        	g2d.setColor(Color.BLACK);
        }
        else
        {
        	g2d.drawImage(icon, WIDTH/2 - (ICON_SIZE/2), HEIGHT/3 - (ICON_SIZE/2), ICON_SIZE, ICON_SIZE, null);
        }
        
        g2d.drawString(message, x, y);
	}
}
