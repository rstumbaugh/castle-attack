package com.rs.castleattack.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.castleattack.game.CastleAttack;
import com.rs.castleattack.game.GameState;
import com.rs.castleattack.game.Player;
import com.rs.castleattack.game.Projectile;
import com.rs.castleattack.game.ProjectileType;
import com.rs.castleattack.game.Soldier;
import com.rs.castleattack.game.SoldierType;
import com.rs.castleattack.game.Upgrade;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class Shop extends JPanel implements ActionListener
{
	public static final int FAST_SOLDIER_PRICE = 35,
							BIG_SOLDIER_PRICE = 35,
							ARCHER_PRICE = 35,
							BIG_PROJECTILE_PRICE = 15,
							SMALL_PROJECTILE_PRICE = 15,
							AIR_STRIKE_PRICE = 30;
	
	private final int TILE_SIZE = 32;
	
	private CastleAttack game;
	private Player player;

	private ShopItem currentItem;
	private ArrowButton leftArrow, rightArrow;
	
	private ArrayList<ShopItem> items = new ArrayList<ShopItem>();
	private int currentItemIndex = 0;
	
	
	
	private TextButton backButton;
	
	private BufferedImage bg = ResourceLoader.loadImage(ResourceLoader.SHOP_BG);
	
	Rectangle itemBounds = new Rectangle(Main.WIDTH/2-(ShopItem.WIDTH/2), 50, ShopItem.WIDTH, ShopItem.HEIGHT);
	
	Timer timer;
	
	public Shop(CastleAttack g)
	{
		this.game = g;
		this.player = game.getPlayer();
		
		this.setLayout(null);
		
		
		initItems();
		initButtons();
		
		
		currentItem = items.get(currentItemIndex);
		
		currentItem.setBounds(itemBounds);

		for(ShopItem item : items)
			this.add(item);
		
		
		timer = new Timer(30, this);
		timer.start();
	}
	
	public void initItems()
	{
		String fastName = "FAST SOLDIER";
		String fastDescription = "x2.5 SPEED, x.5 HEALTH";
		BufferedImage fastImg = ResourceLoader.loadImage(ResourceLoader.FAST_PLAYER_STILL);
		Command fastSoldierCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-FAST_SOLDIER_PRICE))
				{
					SoldierType.playerType = SoldierType.FAST_SOLDIER;
					player.addUpgrade(Upgrade.FAST_SOLDIER);
				}
			}
		};
		
		String bigName = "BIG SOLDIER";
		String bigDescription = "x2.5 HEALTH, x2 DAMAGE, x.5 SPEED";
		BufferedImage bigImg = ResourceLoader.loadImage(ResourceLoader.BIG_PLAYER_STILL);
		Command bigSoldierCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-BIG_SOLDIER_PRICE))
				{
					SoldierType.playerType = SoldierType.BIG_SOLDIER;
					player.addUpgrade(Upgrade.BIG_SOLDIER);
				}
			}
		};
		
		String archerName = "ARCHER";
		String archerDescription = "A AND D TO MOVE";
		BufferedImage archerImg = ResourceLoader.loadImage(ResourceLoader.ARCHER_STILL);
		Command archerCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-ARCHER_PRICE))
				{
					SoldierType.playerType = SoldierType.ARCHER;
					player.addUpgrade(Upgrade.ARCHER);
				}
			}
		};
		
		String bigpName = "BIG PROJECTILE";
		String bigpDescription = "x2 SIZE, x2 DAMAGE, x.75 SPEED";
		Color bigpColor = Projectile.BIG_COLOR;
		Command bigpCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-BIG_PROJECTILE_PRICE))
				{
					ProjectileType.playerType = ProjectileType.BIG_PROJECTILE;
					player.addUpgrade(Upgrade.BIG_PROJECTILE);
				}
			}
		};
		
		String smallName = "SMALL PROJECTILE";
		String smallDescription = "x.75 SIZE, x.75 DAMAGE, x2 SPEED";
		Color smallColor = Projectile.SMALL_COLOR;
		Command smallCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-SMALL_PROJECTILE_PRICE))
				{
					ProjectileType.playerType = ProjectileType.SMALL_PROJECTILE;
					player.addUpgrade(Upgrade.SMALL_PROJECTILE);
				}
			}
		};
		
		String asName = "AIR STRIKE";
		String asDescription = "1 USE, PRESS SHIFT TO ACTIVATE";
		BufferedImage asImg = ResourceLoader.loadImage(ResourceLoader.PLANE);
		Command asCommand = new Command() {
			public void doCommand() {
				if(player.addGold(-AIR_STRIKE_PRICE))
				{
					player.addUpgrade(Upgrade.AIR_STRIKE);
				}
			}
		};
		
		ShopItem fastSoldierItem = new ShopItem(fastImg, fastName, fastDescription, 
				FAST_SOLDIER_PRICE, Soldier.FAST_COST, Upgrade.FAST_SOLDIER, fastSoldierCommand);
		ShopItem bigSoldierItem = new ShopItem(bigImg, bigName, bigDescription, 
				BIG_SOLDIER_PRICE, Soldier.BIG_COST, Upgrade.BIG_SOLDIER, bigSoldierCommand);
		ShopItem archerItem = new ShopItem(archerImg, archerName, archerDescription,
				ARCHER_PRICE, Soldier.ARCHER_COST, Upgrade.ARCHER, archerCommand);
		ShopItem bigProjectileItem = new ShopItem(bigpColor, bigpName, bigpDescription,
				BIG_PROJECTILE_PRICE, Projectile.BIG_COST, Upgrade.BIG_PROJECTILE, bigpCommand);
		ShopItem smallProjectileItem = new ShopItem(smallColor, smallName, smallDescription,
				SMALL_PROJECTILE_PRICE, Projectile.SMALL_COST, Upgrade.SMALL_PROJECTILE, smallCommand);
		ShopItem airStrikeItem = new ShopItem(asImg, asName, asDescription, AIR_STRIKE_PRICE,
				0, Upgrade.AIR_STRIKE, asCommand);
		
		items.add(fastSoldierItem);
		items.add(bigSoldierItem);
		items.add(archerItem);
		items.add(airStrikeItem);
		items.add(smallProjectileItem);
		items.add(bigProjectileItem);
		
		
		
		fastSoldierItem.setBounds(itemBounds);
		bigSoldierItem.setBounds(itemBounds);
		archerItem.setBounds(itemBounds);
		bigProjectileItem.setBounds(itemBounds);
		smallProjectileItem.setBounds(itemBounds);
		airStrikeItem.setBounds(itemBounds);
	}
	
	public void initButtons()
	{
		Command leftCommand = new Command() {
			public void doCommand() {
				if(currentItemIndex  > 0)
					currentItemIndex --;
			}
		};
		Command rightCommand = new Command() {
			public void doCommand() {
				if(currentItemIndex < items.size()-1)
					currentItemIndex ++;
				
			}
		};
		Command backCommand = new Command() {
			public void doCommand() {
				close();
				
			}
		};
		
		leftArrow = new ArrowButton("LEFT", leftCommand);
		rightArrow = new ArrowButton("RIGHT", rightCommand);
		backButton = new TextButton("EXIT SHOP", backCommand);

		leftArrow.setBounds(itemBounds.x-ArrowButton.WIDTH-10, Main.HEIGHT/2-ArrowButton.HEIGHT/2, ArrowButton.WIDTH, ArrowButton.HEIGHT);
		rightArrow.setBounds(itemBounds.x+ShopItem.WIDTH+10, leftArrow.getBounds().y, ArrowButton.WIDTH, ArrowButton.HEIGHT);
		backButton.setBounds(Main.WIDTH/2-(TextButton.WIDTH/2), Main.HEIGHT-TextButton.HEIGHT-50, TextButton.WIDTH, TextButton.HEIGHT);

		this.add(leftArrow);
		this.add(rightArrow);
		this.add(backButton);
	}
	
	public void close()
	{
		PanelManager.switchPanels(this, game);
		GameState.state = GameState.PLAYING;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw bg
		int x=0, y=0;
		while(y + TILE_SIZE < Main.HEIGHT)
		{
			g.drawImage(bg, x, y, TILE_SIZE, TILE_SIZE, null); 
			x += TILE_SIZE;
			if(x >= Main.WIDTH)
			{
				x = 0;
				y += TILE_SIZE;
			}
		}
		
		//draw gold
		Graphics2D g2d = (Graphics2D)g;
		String msg1 = "Gold: ";
		String msg2 = player.getGold()+"g";

		Font font = Main.DEFAULT_FONT.deriveFont(25f);
		g2d.setFont(font);
		
		FontMetrics fm = g2d.getFontMetrics();
		Rectangle2D totalRect = fm.getStringBounds(msg1+msg2, g2d);
        Rectangle2D r = fm.getStringBounds(msg1, g2d);

        x = (Main.WIDTH - (int) totalRect.getWidth()) / 2;
        y = 30;
        
        float alpha = 0.5f;
        Color bgColor = new Color(1f, 1f, 1f, alpha);
        g2d.setColor(bgColor);
        g2d.fillRect(x-5, (int) (y-r.getHeight()+10), 
        		(int)totalRect.getWidth()+10, (int)totalRect.getHeight());
        
        g2d.setColor(Color.BLACK);
        
        g2d.drawString(msg1, x, y);
        g2d.setColor(Main.GOLD_COLOR);
        g2d.drawString(msg2, (int) (x+r.getWidth()), y);
        
        g2d.setColor(Color.BLACK);
        
        
		
	}
	
	public void setPlayer(Player p)
	{
		this.player = p;
	}
	
	public void setCurrentItem(int index)
	{
		this.currentItemIndex = index;
	}
	
	public void updateArrows()
	{
		boolean left = currentItemIndex != 0;
		boolean right = currentItemIndex != items.size() - 1;
		
		leftArrow.setEnabled(left);
		rightArrow.setEnabled(right);
	}
	
	public void updateItems()
	{
		currentItem = items.get(currentItemIndex);
		currentItem.setVisible(true);
		for(int i=0; i<items.size(); i++)
		{
			ShopItem item = items.get(i);
			if(item != currentItem)
				item.setVisible(false);
		}
		Upgrade currentUpgrade = currentItem.getUpgrade();
		currentItem.setPurchased(player.hasUpgrade(currentUpgrade));
		
		if(!currentItem.isPurchased())
		{
			boolean canAfford = currentItem.getPrice() <= player.getGold();
			currentItem.setEnabled(canAfford);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		this.repaint();
		updateArrows();
		updateItems();
	}
}

@SuppressWarnings("serial")
class ArrowButton extends Button
{
	public final static int WIDTH = 50, HEIGHT = 100;
	
	private String direction = "";
	
	private BufferedImage img = ResourceLoader.loadImage(ResourceLoader.SHOP_ARROW);
	
	public ArrowButton(String direction, Command command)
	{
		super(WIDTH, HEIGHT, command);
		this.direction = direction;
		
		if(this.direction.equals("LEFT"))
		{
			img = ResourceLoader.flipImage(img);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawImage(img, WIDTH / 4, HEIGHT / 4, WIDTH/2, HEIGHT/2, null);
	}
}
