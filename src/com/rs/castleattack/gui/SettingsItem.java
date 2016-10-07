package com.rs.castleattack.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.rs.castleattack.main.Main;


@SuppressWarnings("serial")
public class SettingsItem extends JPanel
{
	public static final int WIDTH = (int) (Main.WIDTH * .9),
							HEIGHT = Main.HEIGHT / 6;
	
	private String name, description;
	
	private Switch itemSwitch = new Switch(false);
	
	
	
	public SettingsItem(String name, String description)
	{
		this.name = name;
		this.description = description;
		
		Color c = new Color(0.75f, 0.75f, 0.75f, 0.5f);
		this.setBackground(c);
		this.setLayout(null);
		
		itemSwitch.setBounds(WIDTH - (Switch.WIDTH+10), (HEIGHT - Switch.HEIGHT)/2, Switch.WIDTH, Switch.HEIGHT);
		
		this.add(itemSwitch);
	}
	
	public SettingsItem(String name, String description, boolean enabled)
	{
		this(name, description);
		
		itemSwitch.setState(enabled);
	}
	
	public boolean isEnabled()
	{
		return itemSwitch.getState();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		Font bigFont = Main.DEFAULT_FONT.deriveFont(30f);
		Font smallFont = Main.DEFAULT_FONT.deriveFont(20f);
		
		g2d.setColor(Color.BLACK);
		
		//draw name
		g2d.setFont(bigFont);
		FontMetrics fm = g2d.getFontMetrics();
		int x = 5;
        int y = fm.getAscent();

        g2d.drawString(name, x, y);
        
        //draw description
        g2d.setFont(smallFont);
        y += fm.getAscent();
        
        g2d.drawString(description, x, y);
        
	}
}
