package com.rs.castleattack.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.rs.castleattack.ked.KED;

import res.ResourceLoader;

public class Soldier
{
	public static final int DEFAULT_WIDTH = 32,
					        DEFAULT_HEIGHT = 47,
					        BIG_WIDTH = (int) (DEFAULT_WIDTH * 1.5),
					        BIG_HEIGHT = (int) (DEFAULT_HEIGHT * 1.5);
	
	public static final int DEFAULT_COST = 2,
							FAST_COST = DEFAULT_COST / 2,
							BIG_COST = DEFAULT_COST * 2,
							ARCHER_COST = DEFAULT_COST * 3;
	
	public static final int DEFAULT_HEALTH = 10,
							BIG_HEALTH = (int) (DEFAULT_HEALTH * 2.5),
							FAST_HEALTH = (int) (DEFAULT_HEALTH * .5);
	
	public static final int DEFAULT_DAMAGE = 5,
							BIG_DAMAGE = DEFAULT_DAMAGE * 2;
	
	public static final double DEFAULT_SPEED = 2,
							   FAST_SPEED = DEFAULT_SPEED * 2.5,
							   BIG_SPEED = DEFAULT_SPEED * .5;
	
	public static final double VELOCITY_DIVISOR = 20;
	
	protected Castle target;
	
	protected ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private int currentImg = 0;
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	protected int cost;
	
	protected int maxHealth;
	protected int health;
	private HealthBar healthBar;
	
	protected double maxSpeed;
	protected double speed;
	private int damage;
	
	private SoldierType type;
	
	int defaultCycle = 100;
	private int imageCycle;
	long lastCycle;
	
	boolean animate = true;

	public Soldier(Point p, Castle target, SoldierType type)
	{
		this.x = p.x;
		this.y = p.y;
		this.target = target;
		
		String step = "", still = "";
		
		switch(type)
		{
			case DEFAULT_SOLDIER:
				this.maxHealth = DEFAULT_HEALTH;
				this.damage = DEFAULT_DAMAGE;
				this.maxSpeed = DEFAULT_SPEED;
				this.width = DEFAULT_WIDTH;
				this.height = DEFAULT_HEIGHT;
				this.cost = DEFAULT_COST;
				still = ResourceLoader.PLAYER_STILL;
				step = ResourceLoader.PLAYER_STEP;
				break;
			case FAST_SOLDIER:
				this.maxHealth = FAST_HEALTH;
				this.damage = DEFAULT_DAMAGE;
				this.maxSpeed = FAST_SPEED;
				this.width = DEFAULT_WIDTH;
				this.height = DEFAULT_HEIGHT;
				this.cost = FAST_COST;
				still = ResourceLoader.FAST_PLAYER_STILL;
				step = ResourceLoader.FAST_PLAYER_STEP;
				break;
			case BIG_SOLDIER:
				this.maxHealth = BIG_HEALTH;
				this.damage = BIG_DAMAGE;
				this.maxSpeed = BIG_SPEED;
				this.width = BIG_WIDTH;
				this.height = BIG_HEIGHT;
				this.cost = BIG_COST;
				still = ResourceLoader.BIG_PLAYER_STILL;
				step = ResourceLoader.BIG_PLAYER_STEP;
				break;
			case ARCHER:
				this.maxHealth = DEFAULT_HEALTH;
				this.damage = 0;
				this.maxSpeed = DEFAULT_SPEED;
				this.width = DEFAULT_WIDTH;
				this.height = DEFAULT_HEIGHT;
				this.cost = ARCHER_COST;
				still = ResourceLoader.ARCHER_STILL;
				step = ResourceLoader.ARCHER_STEP;
				break;
		}
		this.type = type;
		
		this.health = maxHealth;
		this.speed = maxSpeed;
		
		images.add(ResourceLoader.loadImage(still));
		images.add(ResourceLoader.loadImage(step));

		healthBar = new HealthBar(width, 5);

		imageCycle = (int) (DEFAULT_SPEED / speed * defaultCycle);
	}
	
	public double getSpeed()
	{
		return speed;
	}

	public Rectangle getBoundingBox()
	{
		return new Rectangle(x, y, width, height);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	public SoldierType getType()
	{
		return type;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public Castle getTarget()
	{
		return target;
	}
	
	public void setLocation(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public void setHealth(int h)
	{
		this.health = h;
	}

	public void addHealth(int h)
	{
		health += h;
	}

	public void collide(double velocity)
	{
		if(type != SoldierType.ARCHER)
		{
			speed = -(maxSpeed * velocity/VELOCITY_DIVISOR);
		}
	}

	public void move(double delta)
	{
		double toMove = speed * delta;
		if(type == SoldierType.ARCHER)
		{
			if(KED.aPressed)
			{
				x -= toMove-.5;
				animate = true;
			}
			else if(KED.dPressed)
			{
				x += toMove+.5;
				animate = true;
			}
			else
			{
				animate = false;
			}
		}
		else
		{
			x += (toMove + .5);

			if(speed / maxSpeed < 1)
			{
				speed += maxSpeed/10;
			}
			if(speed / maxSpeed > 1)
			{
				speed = maxSpeed;
			}
		}
		if(animate && System.currentTimeMillis() - lastCycle >= imageCycle)
		{
			currentImg = currentImg == 0 ? 1 : 0;
			lastCycle = System.currentTimeMillis();
		}
	}
	
	public void draw(Graphics g)
	{
		BufferedImage img = images.get(currentImg);
		g.drawImage(img, x, y, width, height, null);
		
		double pct = (double)health / maxHealth;
		healthBar.draw(x, y-10, pct, g);
	}
}
