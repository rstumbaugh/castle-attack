package com.rs.castleattack.game;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import com.rs.castleattack.gui.PanelManager;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

public class Castle
{
	public final static int WIDTH = 150;
	
	public static final int MAX_HEALTH = 500;
	protected int health = MAX_HEALTH;
	
	protected HealthBar healthBar;
	
	protected Point location;
	
	BufferedImage img = ResourceLoader.loadImage(ResourceLoader.CASTLE);
	
	public Castle()
	{
		location = new Point(0, Main.GROUND.y - WIDTH);
		healthBar = new HealthBar(25, Main.GROUND.y-WIDTH-25, 100, 15);
	}
	
	public void addHealth(int h)
	{
		health = (health + h) > MAX_HEALTH ? MAX_HEALTH : health + h;

		if(health <= 0)
		{
			String msg = this instanceof EnemyCastle ? "Congratulations, you win!" : "Sorry, you lose!";
			JOptionPane.showMessageDialog(null, msg, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
			
			PanelManager.switchPanels(Main.game, Main.mainMenu);
			GameState.state = GameState.GAME_OVER;
		}
	}
	
	public void setHealth(int health)
	{
		this.health = health;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(location.x, location.y, WIDTH, WIDTH);
	}
	
	
	
	public void draw(Graphics g)
	{
		double pct = (double)health / MAX_HEALTH;
		healthBar.draw(pct, g);
		g.drawImage(img, location.x, location.y, WIDTH, WIDTH, null);
	}
}
