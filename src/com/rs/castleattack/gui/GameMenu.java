package com.rs.castleattack.gui;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rs.castleattack.game.CastleAttack;
import com.rs.castleattack.game.EnemyPlayer;
import com.rs.castleattack.game.GameState;
import com.rs.castleattack.game.Player;
import com.rs.castleattack.io.PlayerElement;
import com.rs.castleattack.io.XMLReader;
import com.rs.castleattack.io.XMLWriter;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class GameMenu extends JPanel
{
	public static final int WIDTH = Main.WIDTH,
					  		HEIGHT = Main.HEIGHT / 7,
					  		BORDER = 5,
					  		ITEMS = 5;
	
	private GameMenuButton settingsButton,
						   shopButton,
						   saveButton,
						   loadButton,
						   quitButton;
	
	int startX= GameMenuButton.WIDTH*2 + BORDER*2;
	
	private BufferedImage title = ResourceLoader.loadImage(ResourceLoader.TITLE);
	
	private CastleAttack game;
	
	public GameMenu(CastleAttack game)
	{
		this.game = game;
		
		this.setLayout(null);
		
		initItems();
		
		int i = BORDER + GameMenuButton.WIDTH;
		int y = BORDER;
		
		settingsButton.setBounds(startX, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		shopButton.setBounds(settingsButton.getX() + i, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		saveButton.setBounds(shopButton.getX() + i, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		loadButton.setBounds(saveButton.getX() + i, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		quitButton.setBounds(loadButton.getX() + i, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		
		this.add(settingsButton);
		this.add(shopButton);
		this.add(saveButton);
		this.add(loadButton);
		this.add(quitButton);
	}
	
	public void initItems()
	{
		BufferedImage settings = ResourceLoader.loadImage(ResourceLoader.MENU_SETTINGS);
		BufferedImage shop = ResourceLoader.loadImage(ResourceLoader.MENU_SHOP);
		BufferedImage save = ResourceLoader.loadImage(ResourceLoader.MENU_SAVE);
		BufferedImage load = ResourceLoader.loadImage(ResourceLoader.MENU_LOAD);
		BufferedImage quit = ResourceLoader.loadImage(ResourceLoader.MENU_ARROW);
		
		Command settingsCommand = new Command() {
			public void doCommand() {
				settings();
			}
		};
		Command shopCommand = new Command() {
			public void doCommand() {
				shop();
			}
		};
		Command saveCommand = new Command() {
			public void doCommand() {
				save();
			}
		};
		Command loadCommand = new Command() {
			public void doCommand() {
				load();
			}
		};
		Command quitCommand = new Command() {
			public void doCommand() {
				quit();
			}
		};
		
		settingsButton = new GameMenuButton(settings, "Settings", settingsCommand);
		shopButton = new GameMenuButton(shop, "Shop", shopCommand);
		saveButton = new GameMenuButton(save, "Save", saveCommand);
		loadButton = new GameMenuButton(load, "Load", loadCommand);
		quitButton = new GameMenuButton(quit, "Quit", quitCommand);
	}
	
	private void settings()
	{
		PanelManager.switchPanels(game, Main.settings);
		GameState.state = GameState.PAUSED;
	}
	
	private void shop()
	{
		Main.shop.setCurrentItem(0);
		
		PanelManager.switchPanels(game, Main.shop);
		GameState.state = GameState.PAUSED;
	}
	
	private void save()
	{
		GameState.state = GameState.PAUSED; 
		
		Player p = game.getPlayer();
		EnemyPlayer e = game.getEnemy();
		
		String save = XMLWriter.getSaveLocation();
		if(!save.equals(""))
		{
			save += "\\";
			String loc = JOptionPane.showInputDialog(null, "Enter save file name:", "");
			if(loc == null || loc.equals(""))
			{
				saveButton.release();
				return;
			}
			
			save += (loc + XMLReader.EXTENSION);
			
			new XMLWriter(save).saveGame(p, e);
		}
		
		saveButton.release();
		quitButton.release();
	}
	
	private void load()
	{
		GameState.state = GameState.PAUSED; 
		
		String path = "";
		while(!((path = XMLReader.getLoadLocation()).endsWith(XMLReader.EXTENSION)) && !path.equals(""))
		{
			JOptionPane.showMessageDialog(null, "You must choose a "+XMLReader.EXTENSION+" file!", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		
		if(path.equals("")) 
		{
			loadButton.release();
			return;
		}
		
		XMLReader reader = new XMLReader(path);
		PlayerElement pElement = reader.getPlayerElement();
		PlayerElement eElement = reader.getEnemyElement();

		Player player = new Player(pElement);
		EnemyPlayer enemy = new EnemyPlayer(eElement);
		
		player.setEnemy(enemy);
		enemy.setEnemy(player);
		
		Main.updatePlayers(player, enemy);
		
		loadButton.release();
	}
	
	private void quit()
	{
		if(Settings.saveOnQuit())
		{
			save();
		}
		
		PanelManager.switchPanels(game, Main.mainMenu);
		GameState.state = GameState.PAUSED;
	}
	
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(title, BORDER*2, BORDER, startX-BORDER*2, GameMenuButton.HEIGHT, null);
	}
}
