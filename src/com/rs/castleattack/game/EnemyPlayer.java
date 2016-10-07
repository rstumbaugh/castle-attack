package com.rs.castleattack.game;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.rs.castleattack.io.PlayerElement;
import com.rs.castleattack.main.Main;


public class EnemyPlayer extends Player 
{
	public static final double SPAWN_CHANCE = 5.0 / 100,
							   FIRE_CHANCE = 5.0 / 100;
	
	public static final double ANGLE = Math.PI / 4;
	
	final int MAX_TIME_WITHOUT_SPAWN = 5000;
	long lastSpawn;
	
	Random random = new Random();
	
	public EnemyPlayer()
	{
		castle = new EnemyCastle();
		
		firePoint = Player.DEFAULT_ENEMY_FIRE_POINT;
		soldierSpawnPoint = new Point(firePoint.x, Main.GROUND.y - Soldier.DEFAULT_HEIGHT);
	}
	
	public EnemyPlayer(PlayerElement e) 
	{
		super(e);
		
	}

	public void fireProjectile(double angle, double power)
	{
		Projectile p = new EnemyProjectile(firePoint, angle, power, ProjectileType.enemyType);
		projectiles.add(p);
	}
	
	public void spawnSoldier()
	{
		EnemySoldier s = new EnemySoldier(soldierSpawnPoint, enemy.getCastle(), SoldierType.enemyType);
		int numSpawned = 0;
		for(Soldier soldier : soldiers)
		{
			if(soldier.getType() == s.getType())
				numSpawned++;
		}
		
		ArrayList<SoldierType> types = new ArrayList<SoldierType>(Arrays.asList(SoldierType.values()));
		int index = types.indexOf(s.getType());
		int maxSpawn = SoldierType.MAX_SPAWNS[index];
		
		if(numSpawned < maxSpawn)
		{
			soldiers.add(s);
		}
	}
	
	public void updateSoldiers(double fraction)
	{
		super.updateSoldiers(fraction);
		
		if(!hasArcher())
			firePoint = DEFAULT_ENEMY_FIRE_POINT;
	}
	
	public void logic()
	{
		double d = random.nextDouble();

		if(d <= SPAWN_CHANCE || (System.currentTimeMillis() - lastSpawn) > MAX_TIME_WITHOUT_SPAWN)
		{
			int type = (int)(Math.random() * ((SoldierType.values().length-1)));

			SoldierType.enemyType = SoldierType.values()[type];
	
			spawnSoldier();
			
			lastSpawn = System.currentTimeMillis();
		}
		ArrayList<Soldier> enemySoldiers = enemy.getSoldiers();
		
		Point max = EnemyProjectile.getMaxReach(firePoint.x + Castle.WIDTH/2, firePoint.y, Projectile.DEFAULT_XSPEED, 
				Projectile.DEFAULT_YSPEED, ANGLE, 1.0);
		
		int maxReachDist = firePoint.x - max.x;

		if(d <= FIRE_CHANCE)
		{
			for(Soldier s : enemySoldiers)
			{
				Rectangle b = s.getBoundingBox();
				if(b.x >= max.x)
				{
					int soldierDist = firePoint.x - b.x + Castle.WIDTH/2;
					if(soldierDist <= 100)
					{
						fireProjectile(-Math.PI/6, 1.0);
						return;
					}
					double power = (double)soldierDist / maxReachDist;
					fireProjectile(ANGLE, power);
					return;
				}
			}
		}
		
		
	}
}
