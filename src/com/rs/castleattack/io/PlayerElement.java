package com.rs.castleattack.io;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PlayerElement
{	
	private Element main;
	
	private Element castleElement;
	private Element soldierElement;
	private Element projectileElement;
	private Element upgradeElement;
	private Element firePointElement;
	private Element spawnPointElement;
	private Element goldElement;
	private Element airStrikeElement;
	
	private boolean isEnemy;
	
	public PlayerElement(Element n)
	{
		this.main = n;
		
		NodeList castles = main.getElementsByTagName("castle");
		castleElement = (Element) castles.item(castles.getLength()-1);
		soldierElement = (Element) main.getElementsByTagName("soldiers").item(0);
		projectileElement = (Element) main.getElementsByTagName("projectiles").item(0);
		upgradeElement = (Element) main.getElementsByTagName("upgrades").item(0);
		firePointElement = (Element) main.getElementsByTagName("firepoint").item(0);
		spawnPointElement = (Element) main.getElementsByTagName("spawnpoint").item(0);
		goldElement = (Element) main.getElementsByTagName("gold").item(0);
		airStrikeElement = (Element) main.getElementsByTagName("airstrike").item(0);
		
		Element enemyElement = (Element) main.getElementsByTagName("enemy").item(0);
		
		isEnemy = enemyElement.getTextContent().equals("true");
	}
	
	public boolean isEnemy() {
		return isEnemy;
	}

	public Element getCastleElement() {
		return castleElement;
	}

	public Element getSoldierElement() {
		return soldierElement;
	}

	public Element getProjectileElement() {
		return projectileElement;
	}

	public Element getUpgradeElement() {
		return upgradeElement;
	}

	public Element getFirePointElement() {
		return firePointElement;
	}

	public Element getSpawnPointElement() {
		return spawnPointElement;
	}

	public Element getGoldElement() {
		return goldElement;
	}
	
	public Element getAirStrikeElement() {
		return airStrikeElement;
	}
	
	
}
