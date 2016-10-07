package com.rs.castleattack.io;


import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rs.castleattack.game.AirStrike;
import com.rs.castleattack.game.Castle;
import com.rs.castleattack.game.EnemyPlayer;
import com.rs.castleattack.game.Player;
import com.rs.castleattack.game.Projectile;
import com.rs.castleattack.game.Soldier;
import com.rs.castleattack.game.Upgrade;
import com.rs.castleattack.gui.Settings;
import com.rs.castleattack.main.Main;

public class XMLWriter 
{
	private String dest;
	
	private Document doc;
	private Element root;
	
	public XMLWriter(String dest)
	{
		this.dest = dest;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			root = doc.createElement("players");
			doc.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}	
	}
	
	public static String getSaveLocation()
	{
		String loc = "";
		
		JFileChooser newDirChooser = new JFileChooser();
		newDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int val = newDirChooser.showDialog(Main.frame, "Save Here");

		if(val == JFileChooser.APPROVE_OPTION)
		{
			File file = newDirChooser.getSelectedFile();
			loc = file.getAbsolutePath();
		}
		
		return loc;
	}
	
	public void saveGame(Player p, EnemyPlayer e)
	{
		writePlayers(p, e);
		writeSettings();
		
		writeDoc();
	}
	
	public void writePlayer(Player p)
	{
		ArrayList<Soldier> soldiers = p.getSoldiers();
		ArrayList<Projectile> projectiles = p.getProjectiles();
		ArrayList<Upgrade> upgrades = p.getUpgrades();
		
		Point firePoint = p.getFirePoint();
		Point spawnPoint = p.getSoldierSpawnPoint();
		
		Castle castle = p.getCastle();
		int gold = p.getGold();
		
		AirStrike as = p.getAirStrike();
		
		Element main = doc.createElement("player");
		
		boolean isEnemy = p instanceof EnemyPlayer;
		
		Element enemyElement = createTextElement("enemy", isEnemy+"");
		
		Element soldiersElement = createSoldiersElement(soldiers);
		Element projectilesElement = createProjectilesElement(projectiles);
		Element upgradesElement = createUpgradesElement(upgrades);
		
		Element firePointElement = createPointElement(firePoint, "firepoint");
		Element spawnPointElement = createPointElement(spawnPoint, "spawnpoint");
		
		Element castleElement = createCastleElement(castle);
		Element goldElement = createTextElement("gold", gold+"");
		
		Element airStrikeElement = createAirStrikeElement(as);
		
		main.appendChild(enemyElement);
		main.appendChild(soldiersElement);
		main.appendChild(projectilesElement);
		main.appendChild(upgradesElement);
		main.appendChild(firePointElement);
		main.appendChild(spawnPointElement);
		main.appendChild(castleElement);
		main.appendChild(goldElement);
		main.appendChild(airStrikeElement);
		
		root.appendChild(main);
	}
	
	public void writePlayers(Player player, EnemyPlayer enemy)
	{
		writePlayer(player);
		writePlayer(enemy);
	}
	
	public void writeSettings()
	{
		Element main = doc.createElement("settings");
		
		Element pathElement = createTextElement("showpath", Settings.showPath()+"");
		Element saveElement = createTextElement("saveonquit", Settings.saveOnQuit()+"");
		
		main.appendChild(pathElement);
		main.appendChild(saveElement);
		
		root.appendChild(main);
	}
	
	public Element createSoldiersElement(ArrayList<Soldier> soldiers)
	{
		Element soldiersElement = doc.createElement("soldiers");
		
		for(Soldier s : soldiers)
		{
			Rectangle r = s.getBoundingBox();
			
			Element soldier = doc.createElement("soldier");
			Element x = createTextElement("x", r.x+"");
			Element y = createTextElement("y", r.y+"");
			Element health = createTextElement("health", s.getHealth()+"");
			Element type = createTextElement("type", s.getType()+"");
			Element c = createCastleElement(s.getTarget());
			
			soldier.appendChild(x);
			soldier.appendChild(y);
			soldier.appendChild(health);
			soldier.appendChild(type);
			soldier.appendChild(c);
			
			soldiersElement.appendChild(soldier);
		}
		
		return soldiersElement;
	}
	
	public Element createProjectilesElement(ArrayList<Projectile> projectiles)
	{
		Element projectilesElement = doc.createElement("projectiles");
		
		for(Projectile pr : projectiles)
		{
			Rectangle r = pr.getBoundingBox();
			
			Element projectile = doc.createElement("projectile");
			Element x = createTextElement("x", r.x+"");
			Element y = createTextElement("y", r.y+"");
			Element vx = createTextElement("vx", pr.getXVelocity()+"");
			Element vy = createTextElement("vy", pr.getYVelocity()+"");
			Element type = createTextElement("type", pr.getType()+"");
			
			projectile.appendChild(x);
			projectile.appendChild(y);
			projectile.appendChild(vx);
			projectile.appendChild(vy);
			projectile.appendChild(type);
			
			projectilesElement.appendChild(projectile);
		}
		
		return projectilesElement;
	}
	
	public Element createUpgradesElement(ArrayList<Upgrade> upgrades)
	{
		Element upgradesElement = doc.createElement("upgrades");
		
		for(Upgrade u : upgrades)
		{
			Element upgrade = createTextElement("upgrade", u+"");
			
			upgradesElement.appendChild(upgrade);
		}
		
		return upgradesElement;
	}
	
	public Element createCastleElement(Castle c)
	{
		Element castle = doc.createElement("castle");
		castle.appendChild(createTextElement("health", c.getHealth()+""));
		
		return castle;
	}
	
	public Element createAirStrikeElement(AirStrike a)
	{
		Element as = doc.createElement("airstrike");
		as.appendChild(createPointElement(a.getLocation(), "location"));
		as.appendChild(createTextElement("active", a.isActive()+""));
		
		return as;
	}
	
	public Element createPointElement(Point p, String name)
	{
		Element element = doc.createElement(name);
		element.appendChild(createTextElement("x", p.x+""));
		element.appendChild(createTextElement("y", p.y+""));
		
		return element;
	}
	
	public Element createTextElement(String name, String text)
	{
		Element element = doc.createElement(name);
		element.appendChild(doc.createTextNode(text));
		
		return element;
	}
	
	public void writeDoc()
	{
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            DOMSource source = new DOMSource(doc);
            
            OutputStream out = new FileOutputStream(dest);
            StreamResult result = new StreamResult(out);
            
            transformer.transform(source, result);
            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("XML written successfully..");
			
			JOptionPane.showMessageDialog(null, "Game saved to "+dest.substring(dest.lastIndexOf('\\')+1)+
					" successfully.");
		}
	}
	
	
}
