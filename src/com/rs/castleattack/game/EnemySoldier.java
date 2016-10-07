package com.rs.castleattack.game;

import java.awt.Point;
import java.awt.image.BufferedImage;

import res.ResourceLoader;

public class EnemySoldier extends Soldier 
{
	public EnemySoldier(Point p, Castle target, SoldierType type) 
	{
		super(p, target, type);
		
		speed = -speed;
		maxSpeed = -maxSpeed;
		
		BufferedImage step = null, still = null;
		if(type == SoldierType.DEFAULT_SOLDIER)
		{
			step = ResourceLoader.loadImage(ResourceLoader.ENEMY_STEP);
			still = ResourceLoader.loadImage(ResourceLoader.ENEMY_STILL);
		}
		else
		{
			step = images.get(0);
			still = images.get(1);
		}
		

		BufferedImage flippedStep = ResourceLoader.flipImage(step);
		BufferedImage flippedStill = ResourceLoader.flipImage(still);
		
		images.clear();
		
		images.add(flippedStep);
		images.add(flippedStill);
	}
}
