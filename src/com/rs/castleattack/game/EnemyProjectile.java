package com.rs.castleattack.game;

import java.awt.Point;

import com.rs.castleattack.main.Main;


public class EnemyProjectile extends Projectile 
{
	public EnemyProjectile(Point p, double angle, double power, ProjectileType type)
	{
		super(p, angle, power, type);
		vx = -vx;
	}
	
	public static Point getMaxReach(int x, int y, int vx_max, int vy_max, double angle, double power)
	{
		double xvel = -Math.cos(angle) * (vx_max * power);
		double yvel = -Math.sin(angle) * (vy_max * power);
		
		while(y <= Main.HEIGHT)
		{
			x += xvel;
			y += yvel;
			
			yvel += 1;
		}
		
		return new Point(x, y);
	}
}
