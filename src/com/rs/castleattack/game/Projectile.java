package com.rs.castleattack.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.rs.castleattack.main.Main;


public class Projectile 
{	
	public static final int DEFAULT_SIZE = 15,
							BIG_SIZE = DEFAULT_SIZE * 2,
							SMALL_SIZE = (int) (DEFAULT_SIZE * .75);
	
	public static final int DEFAULT_XSPEED = 20,
							BIG_XSPEED = 15,
							SMALL_XSPEED = DEFAULT_XSPEED * 2,
							DEFAULT_YSPEED = 30,
							BIG_YSPEED = 22,
							SMALL_YSPEED = DEFAULT_YSPEED * 2;
	
	public static final int DEFAULT_DAMAGE = 5,
							BIG_DAMAGE = 10,
							SMALL_DAMAGE = (int) (DEFAULT_DAMAGE * .75);
	
	public static final int DEFAULT_COST = 0,
							BIG_COST = 2,
							SMALL_COST = 2;
	
	public static final Color DEFAULT_COLOR = Color.BLACK,
							  BIG_COLOR = Color.BLUE,
							  SMALL_COLOR = Color.RED;
	
	private ProjectileType type;
	
	private Color color;
	
	private int x, y, size;
	
	protected double vx;
	protected double vy;
	
	protected int vxMax, vyMax;
	
	protected double acceleration = 1;
	
	private int damage;
	private int cost;

	public Projectile(Point p, double angle, double power, ProjectileType type)
	{
		this.x = p.x;
		this.y = p.y;
		
		switch(type)
		{
			case DEFAULT_PROJECTILE:
				vxMax = DEFAULT_XSPEED;
				vyMax = DEFAULT_YSPEED;
				damage = DEFAULT_DAMAGE;
				size = DEFAULT_SIZE;
				color = DEFAULT_COLOR;
				cost = DEFAULT_COST;
				break;
			case BIG_PROJECTILE:
				vxMax = BIG_XSPEED;
				vyMax = BIG_YSPEED;
				damage = BIG_DAMAGE;
				size = BIG_SIZE;
				color = BIG_COLOR;
				cost = BIG_COST;
				break;
			case SMALL_PROJECTILE:
				vxMax = SMALL_XSPEED;
				vyMax = SMALL_YSPEED;
				damage = SMALL_DAMAGE;
				size = SMALL_SIZE;
				color = SMALL_COLOR;
				cost = SMALL_COST;
				break;
		}
		
		this.type = type;
		
		vx = Math.cos(angle) * (vxMax * power);
		vy = -Math.sin(angle) * (vyMax * power);
	}
	
	public static Point getMaxReach(int x, int y, int vx_max, int vy_max)
	{
		return getMaxReach(x, y, vx_max, vy_max, Math.PI / 4.0, 1.0);
	}
	
	public static Point getMaxReach(int x, int y, int vx_max, int vy_max, double angle, double power)
	{
		double xvel = Math.cos(angle) * (vx_max * power);
		double yvel = -Math.sin(angle) * (vy_max * power);
		
		while(y <= Main.GROUND.y)
		{
			x += xvel;
			y += yvel;
			
			yvel += 1;
		}
		
		return new Point(x, y);
	}
	
	public void setVx(double vx)
	{
		this.vx = vx;
	}
	
	public void setVy(double vy)
	{
		this.vy = vy;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public double getXVelocity()
	{
		return vx;
	}
	
	public double getYVelocity()
	{
		return vy;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public ProjectileType getType()
	{
		return type;
	}

	public void move(double fraction) 
	{
		x += vx * fraction;
		y += vy * fraction;

		vy += acceleration * fraction;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle((int)x, (int)y, size, size);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int)x, (int)y, size, size);
		g.setColor(Color.BLACK);
	}
}
