package com.rs.castleattack.game;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.rs.castleattack.gui.GameMenu;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;


public class AirStrike 
{
	public static final int WIDTH = 100, HEIGHT = 50;
	
	final int SPEED = 3;
	
	private int x = 0, y = GameMenu.HEIGHT;
	
	private boolean active = false;
	
	BufferedImage img = ResourceLoader.loadImage(ResourceLoader.PLANE);
	
	public void activate()
	{
		active = true;
		
		x = -WIDTH*2;
	}
	
	public void deactivate()
	{
		active = false;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public void setLocation(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point getLocation()
	{
		return new Point(x, y);
	}
	
	public BufferedImage getImg()
	{
		return img;
	}
	
	public void update(Player p, EnemyPlayer e)
	{
		if(active && TimeManager.canDoAirStrike())
		{
			Projectile pBomb = new Projectile(new Point(x+WIDTH/2, y+HEIGHT/2), 0, 0, ProjectileType.playerType);
			Projectile eBomb = new EnemyProjectile(new Point(x+WIDTH/2, y+HEIGHT/2), 0, 0, ProjectileType.playerType);

			pBomb.setVx(0);
			pBomb.setVy(0);
			eBomb.setVx(0);
			eBomb.setVy(0);
			
			p.addProjectile(pBomb);
			e.addProjectile(eBomb);
			
			if(x >= Main.WIDTH)
			{
				deactivate();
				p.removeUpgrade(Upgrade.AIR_STRIKE);
			}
			
			x += SPEED;
			
			TimeManager.setLastAirStrike(System.currentTimeMillis());
		}
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(img, x, y, WIDTH, HEIGHT, null);
	}
}
