package com.rs.castleattack.game;

import java.awt.Point;

import com.rs.castleattack.main.Main;


public class EnemyCastle extends Castle 
{
	
	public EnemyCastle() 
	{
		location = new Point(Main.WIDTH-WIDTH-5, Main.GROUND.y - WIDTH);
		healthBar = new HealthBar(Main.WIDTH-125-5, Main.GROUND.y-WIDTH-25, 100, 15);
	}
}
