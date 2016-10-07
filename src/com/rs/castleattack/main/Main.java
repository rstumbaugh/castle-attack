package com.rs.castleattack.main;



import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.rs.castleattack.game.CastleAttack;
import com.rs.castleattack.game.EnemyPlayer;
import com.rs.castleattack.game.GameState;
import com.rs.castleattack.game.Player;
import com.rs.castleattack.gui.MainMenu;
import com.rs.castleattack.gui.Settings;
import com.rs.castleattack.gui.Shop;
import com.rs.castleattack.io.XMLReader;
import com.rs.castleattack.io.XMLWriter;

import res.ResourceLoader;


public class Main
{
	public static final int WIDTH = 1000,
							HEIGHT = 600;
	
	public static final Rectangle GROUND = new Rectangle(0, (int)(HEIGHT * .89), WIDTH, (int)(HEIGHT * .11));
	
	public static final Color GOLD_COLOR = new Color(235, 220, 0),
							  DARKER_GOLD_COLOR = new Color(255, 215, 0),
							  GREEN = new Color(0, 204, 0);
	
	public static Font DEFAULT_FONT = ResourceLoader.loadFont(ResourceLoader.MINECRAFTIA);
	
	public static JFrame frame = new JFrame("Castle Attack!");
	
	public static MainMenu mainMenu = new MainMenu();
	public static CastleAttack game = new CastleAttack();
	public static Shop shop = new Shop(game);
	public static Settings settings = new Settings();
	
	public static void updatePlayers(Player p, EnemyPlayer e)
	{
		game.setPlayer(p);
		game.setEnemy(e);
		
		shop.setPlayer(p);
	}
	
	public static void main(String[] args) 
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
    	frame.setResizable(false);
    	
    	WindowAdapter wa = new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				if(Settings.saveOnQuit() && GameState.state != GameState.GAME_OVER)
				{
					GameState.state = GameState.PAUSED; 
					
					Player p = game.getPlayer();
					EnemyPlayer ep = game.getEnemy();
					

					String save = "";

					if(XMLReader.loadedFile.equals(""))
					{
						save = XMLWriter.getSaveLocation();
						if(!save.equals(""))
						{
							save += "\\";
							String loc = JOptionPane.showInputDialog(null, "Enter save file name:", "");
							if(loc == null || loc.equals(""))
							{
								return;
							}
							
							save += (loc + XMLReader.EXTENSION);
						}
					}
					else
					{
						save = XMLReader.loadedFile;
					}
					
					if(!save.equals(""))
					{
						new XMLWriter(save).writePlayers(p, ep);
					}
					
					
				}
				
			}
		};
		frame.addWindowListener(wa);
    	
    	frame.setLayout(null);
    	
    	Rectangle bounds = new Rectangle(0, 0, WIDTH, (int)(HEIGHT*.95));
    	mainMenu.setBounds(bounds);
    	game.setBounds(bounds);
    	shop.setBounds(bounds);
    	settings.setBounds(bounds);
    	
		Container c = frame.getContentPane();
		c.add(mainMenu);
		c.add(game);
		c.add(shop);
		c.add(settings);
		
    	game.setVisible(false);
    	shop.setVisible(false);
    	settings.setVisible(false);
    	
    	frame.setVisible(true);   
    	game.update();
	}
}
