package com.rs.castleattack.game;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import com.rs.castleattack.io.PlayerElement;
import com.rs.castleattack.io.XMLReader;
import com.rs.castleattack.main.Main;



public class Player
{
	public static final Point DEFAULT_PLAYER_FIRE_POINT = new Point(75, 420),
							  DEFAULT_ENEMY_FIRE_POINT = new Point(920, 420);
							  
	public static final int DEFAULT_GOLD = 100;
	
	protected Castle castle;
	protected Player enemy;
	
	protected AirStrike airStrike = new AirStrike();
	
	protected ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	protected ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();

	protected Point firePoint;
	protected Point soldierSpawnPoint;
	
	private int gold = DEFAULT_GOLD;
	
	public Player()
	{
		castle = new Castle();
		
		firePoint = DEFAULT_PLAYER_FIRE_POINT;
		soldierSpawnPoint = new Point(firePoint.x, Main.GROUND.y - Soldier.DEFAULT_HEIGHT);
	}
	
	public Player(PlayerElement p)
	{
		this();
		
		boolean enemy = p.isEnemy();
		
		castle = XMLReader.buildCastle(p.getCastleElement(), enemy);
		soldiers = XMLReader.buildSoldiersList(p.getSoldierElement(), enemy);
		projectiles = XMLReader.buildProjectilesList(p.getProjectileElement(), enemy);
		upgrades = XMLReader.buildUpgradesList(p.getUpgradeElement());
		firePoint = XMLReader.buildPoint(p.getFirePointElement());
		soldierSpawnPoint = XMLReader.buildPoint(p.getSpawnPointElement());
		gold = XMLReader.getTextAsInt(p.getGoldElement());
		airStrike = XMLReader.buildAirStrike(p.getAirStrikeElement());
	}
	
	public void reset()
	{
		soldiers.clear();
		projectiles.clear();
		
		upgrades.clear();
		
		castle.addHealth(Castle.MAX_HEALTH);
		
		
		addGold(DEFAULT_GOLD - gold);
	}
	
	public void addProjectile(Projectile p)
	{
		projectiles.add(p);
	}
	
	public void addUpgrade(Upgrade u)
	{
		upgrades.add(u);
	}
	
	public void removeUpgrade(Upgrade u)
	{
		if(hasUpgrade(u))
			upgrades.remove(u);
	}
	
	public boolean hasUpgrade(Upgrade u)
	{
		return upgrades.contains(u);
	}
	
	public boolean hasArcher()
	{
		for(Soldier s : soldiers)
			if(s.getType() == SoldierType.ARCHER)
				return true;
		
		return false;
	}
	
	public boolean hasAirStrike()
	{
		return upgrades.contains(Upgrade.AIR_STRIKE);
	}
	
	public void setEnemy(Player enemy)
	{
		this.enemy = enemy;
	}
	
	public void setAirStrike(AirStrike as)
	{
		this.airStrike = as;
	}
	
	public boolean addGold(int g)
	{
		if(gold + g >= 0)
		{
			gold += g;
			return true;
		}
		return false;
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public AirStrike getAirStrike()
	{
		return airStrike;
	}
	
	
	public void spawnSoldier()
	{
		Soldier s = new Soldier(soldierSpawnPoint, enemy.getCastle(), SoldierType.playerType);
		int numSpawned = 0;
		for(Soldier soldier : soldiers)
		{
			if(soldier.getType() == s.getType())
				numSpawned++;
		}
		
		ArrayList<SoldierType> types = new ArrayList<SoldierType>(Arrays.asList(SoldierType.values()));
		int index = types.indexOf(s.getType());
		int maxSpawn = SoldierType.MAX_SPAWNS[index];
		
		if(numSpawned < maxSpawn && addGold(-s.getCost()))
		{
			soldiers.add(s);
		}
	}
	
	public void removeSoldier(Soldier s)
	{
		soldiers.remove(s);
	}
	
	public void fireProjectile(double angle, double power)
	{
		Projectile p = new Projectile(firePoint, angle, power, ProjectileType.playerType);
		if(addGold(-p.getCost()))
		{
			projectiles.add(p);
		}
	}
	
	public void removeProjectile(Projectile p)
	{
		projectiles.remove(p);
	}
	
	public ArrayList<Soldier> checkSoldierCollisions(Rectangle box)
	{
		ArrayList<Soldier> hit = new ArrayList<Soldier>();
		for(int i=0; i<soldiers.size(); i++)
		{
			Soldier s = soldiers.get(i);
			if(box.intersects(s.getBoundingBox()))
			{
				hit.add(s);
			}
		}
		return hit;
	}
	
	public boolean checkCastleCollision(Rectangle box)
	{
		return box.intersects(castle.getBoundingBox());
	}
	
	public Point getFirePoint()
	{
		return firePoint;
	}
	
	public Point getSoldierSpawnPoint()
	{
		return soldierSpawnPoint;
	}
	
	public Castle getCastle()
	{
		return castle;
	}
	
	public ArrayList<Projectile> getProjectiles()
	{
		return projectiles;
	}
	
	public ArrayList<Soldier> getSoldiers()
	{
		return soldiers;
	}
	
	public ArrayList<Upgrade> getUpgrades()
	{
		return upgrades;
	}
	
	public void updateSoldiers(double fraction)
	{
		
		for(Soldier s : soldiers)
		{
			s.move(fraction);
			if(s.getType() == SoldierType.ARCHER)
			{
				int centerx = s.x + s.getWidth() / 2;
				int centery = s.y + s.getHeight() / 2;
				
				firePoint = new Point(centerx, centery);
			}
		}
		
		if(!hasArcher())
			firePoint = DEFAULT_PLAYER_FIRE_POINT;
		
	}
	
	public void updateProjectiles(double fraction)
	{
		for(Projectile p : projectiles)
		{
			p.move(fraction);
		}
	}
	
	public void drawCastle(Graphics g)
	{
		castle.draw(g);
	}
	
	public void drawSoldiers(Graphics g)
	{
		for(int i=0; i<soldiers.size(); i++)
		{
			soldiers.get(i).draw(g);
		}
	}
	
	public void drawProjectiles(Graphics g)
	{
		for(int i=0; i<projectiles.size(); i++)
		{
			projectiles.get(i).draw(g);
		}
	}
}
