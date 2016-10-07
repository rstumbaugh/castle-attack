package com.rs.castleattack.io;


import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rs.castleattack.game.AirStrike;
import com.rs.castleattack.game.Castle;
import com.rs.castleattack.game.EnemyCastle;
import com.rs.castleattack.game.EnemyProjectile;
import com.rs.castleattack.game.EnemySoldier;
import com.rs.castleattack.game.Projectile;
import com.rs.castleattack.game.ProjectileType;
import com.rs.castleattack.game.Soldier;
import com.rs.castleattack.game.SoldierType;
import com.rs.castleattack.game.Upgrade;
import com.rs.castleattack.gui.Settings;
import com.rs.castleattack.main.Main;

public class XMLReader 
{
	public static final String EXTENSION = ".sav";
	
	public static String loadedFile = "";
	
	private Document doc;
	
	private Element root;
	private PlayerElement playerElement, enemyElement;
	
	public XMLReader(String path)
	{
		loadedFile = path;
		
		try{
			doc = getDocument(path);
			root = doc.getDocumentElement();
			
			NodeList nodes = root.getElementsByTagName("player");

			playerElement = new PlayerElement((Element) nodes.item(0));
			enemyElement = new PlayerElement((Element) nodes.item(1));
			
			readSettings();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getLoadLocation() 
	{
		String loc = "";
		
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("sav files (*.sav)", "sav");
        fc.setFileFilter(xmlfilter);
        
		int val = fc.showDialog(Main.frame, "Load");
		
		if(val == JFileChooser.APPROVE_OPTION)
		{
			File selected = fc.getSelectedFile();
			loc = selected.getAbsolutePath();
		}
		
		return loc;
	}
	
	public static Castle buildCastle(Node castleNode, boolean isEnemy)
	{
		Element element = (Element) castleNode;
	
		Castle c = null;
		
		if(isEnemy)
			c = new EnemyCastle();
		else
			c = new Castle();
		
		Node healthNode = element.getElementsByTagName("health").item(0);
		int health = getTextAsInt(healthNode);
		
		c.setHealth(health);

		return c;
	}
	
	public static ArrayList<Soldier> buildSoldiersList(Node soldiersNode, boolean isEnemy)
	{
		Element main = (Element) soldiersNode;
		ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
		
		NodeList soldierNodes = main.getElementsByTagName("soldier");
		
		for(int i=0; i<soldierNodes.getLength(); i++)
		{
			Node current = soldierNodes.item(i);
			Soldier s = buildSoldier(current, isEnemy);
			
			soldiers.add(s);
		}
		
		return soldiers;
	}
	
	public static ArrayList<Projectile> buildProjectilesList(Node projectilesNode, boolean isEnemy)
	{
		Element main = (Element) projectilesNode;
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		NodeList projectileNodes = main.getElementsByTagName("projectile");
		
		for(int i=0; i<projectileNodes.getLength(); i++)
		{
			Node current = projectileNodes.item(i);
			Projectile p = buildProjectile(current, isEnemy);
			
			projectiles.add(p);
		}
		
		return projectiles;
	}
	
	public static ArrayList<Upgrade> buildUpgradesList(Node upgradesNode)
	{
		Element main = (Element) upgradesNode;
		ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
		
		NodeList upgradeNodes = main.getElementsByTagName("upgrade");
		
		for(int i=0; i<upgradeNodes.getLength(); i++)
		{
			Node current = upgradeNodes.item(i);
			String upgradeText = current.getTextContent();
			Upgrade u = Upgrade.valueOf(upgradeText);
			
			upgrades.add(u);
		}
		
		return upgrades;
	}
	
	public static Soldier buildSoldier(Node soldierNode, boolean isEnemy)
	{
		Element element = (Element) soldierNode;
		
		Node xNode = element.getElementsByTagName("x").item(0);
		Node yNode = element.getElementsByTagName("y").item(0);
		Node healthNode = element.getElementsByTagName("health").item(0);
		Node targetNode = element.getElementsByTagName("castle").item(0);
		Node typeNode = element.getElementsByTagName("type").item(0);
		
		String typeText = typeNode.getTextContent();
		
		int x = getTextAsInt(xNode);
		int y = getTextAsInt(yNode);
		int health = getTextAsInt(healthNode);
		Castle target = buildCastle(targetNode, isEnemy);
		SoldierType type = SoldierType.valueOf(typeText);
		
		Soldier s = null;
		if(isEnemy)
			s = new EnemySoldier(new Point(x, y), target, type);
		else
			s = new Soldier(new Point(x, y), target, type);
		
		s.setHealth(health);
		
		return s;
	}
	
	public static Projectile buildProjectile(Node projectileNode, boolean isEnemy)
	{
		Element element = (Element) projectileNode;
		
		Node xNode = element.getElementsByTagName("x").item(0);
		Node yNode = element.getElementsByTagName("y").item(0);
		Node vxNode = element.getElementsByTagName("vx").item(0);
		Node vyNode = element.getElementsByTagName("vy").item(0);
		Node typeNode = element.getElementsByTagName("type").item(0);
		
		String typeText = typeNode.getTextContent();
		
		int x = getTextAsInt(xNode);
		int y = getTextAsInt(yNode);
		double vx = getTextAsDouble(vxNode);
		double vy = getTextAsDouble(vyNode);
		ProjectileType type = ProjectileType.valueOf(typeText);
		
		Projectile p = null;
		if(isEnemy)
				p = new EnemyProjectile(new Point(x, y), 0, 0, type);
		else
			p = new Projectile(new Point(x, y), 0, 0, type);
		
		p.setVx(vx);
		p.setVy(vy);
		
		return p;
	}
	
	public static AirStrike buildAirStrike(Node airStrikeNode)
	{
		Element element = (Element) airStrikeNode;
		
		AirStrike as = new AirStrike();
		
		Element pointElement = (Element) element.getElementsByTagName("location").item(0);
		Element activeElement = (Element) element.getElementsByTagName("active").item(0);
		
		boolean active = activeElement.getTextContent().equals("true");
		
		as.setLocation(buildPoint(pointElement));
		as.setActive(active);
		
		return as;
	}
	
	public static Point buildPoint(Node pointNode)
	{
		Element element = (Element) pointNode;
		
		Node xNode = element.getElementsByTagName("x").item(0);
		Node yNode = element.getElementsByTagName("y").item(0);
		
		int x = getTextAsInt(xNode);
		int y = getTextAsInt(yNode);
		
		return new Point(x, y);
	}
	
	public static int getTextAsInt(Node node)
	{
		String text = node.getTextContent();
		
		return Integer.parseInt(text);
	}
	
	public static double getTextAsDouble(Node node)
	{
		String text = node.getTextContent();
		
		return Double.parseDouble(text);
	}
	
	public static boolean getTextAsBoolean(Node node)
	{
		String text = node.getTextContent();
		
		return text.equals("true");
	}
	
	private void readSettings()
	{
		Element element = (Element) root.getElementsByTagName("settings").item(0);
		
		Element path = (Element) element.getElementsByTagName("showpath").item(0);
		Element save = (Element) element.getElementsByTagName("saveonquit").item(0);
		
		Settings.setShowPath(getTextAsBoolean(path));
		Settings.setSaveOnQuit(getTextAsBoolean(save));
		
		
	}
	
	private Document getDocument(String path)
	{
		Document d = null;
		try {
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			d = dBuilder.parse(fXmlFile);
			d.getDocumentElement().normalize();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return d;
	}
	
	public PlayerElement getPlayerElement()
	{
		return playerElement;
	}
	
	public PlayerElement getEnemyElement()
	{
		return enemyElement;
	}
	
	
}
