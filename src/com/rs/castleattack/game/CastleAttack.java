package com.rs.castleattack.game;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.castleattack.gui.Command;
import com.rs.castleattack.gui.ExpandableMenu;
import com.rs.castleattack.gui.GameMenu;
import com.rs.castleattack.gui.GameMenuButton;
import com.rs.castleattack.io.XMLReader;
import com.rs.castleattack.ked.KED;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class CastleAttack extends JPanel implements ActionListener, MouseListener
{
	public static final int FPS = 30;
	public static final int OPTIMAL_TIME = 1000000000 / FPS;
	
	private GameMenu gameMenu = new GameMenu(this);
	
	private Player player = new Player();
	private EnemyPlayer enemy = new EnemyPlayer();
	
	private AimLine aimLine = new AimLine();
	
	private ExpandableMenu soldierMenu, projectileMenu;
	
	private Image bg = ResourceLoader.loadImage(ResourceLoader.BACKGROUND);
	Timer timer;
	
	long last;

	public CastleAttack()
	{
		this.setLayout(null);
		
		initMenus();
		
		player.setEnemy(enemy);
		enemy.setEnemy(player);

		gameMenu.setBounds(0, 0, GameMenu.WIDTH, GameMenu.HEIGHT);
		soldierMenu.setBounds(5, gameMenu.getHeight() + 5, soldierMenu.getWidth(), ExpandableMenu.HEIGHT);
		projectileMenu.setBounds(5, soldierMenu.getY() + ExpandableMenu.HEIGHT+5, projectileMenu.getWidth(), ExpandableMenu.HEIGHT);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KED());
		
		this.addMouseListener(this);		
		this.add(gameMenu);
		this.add(soldierMenu);
		this.add(projectileMenu);
		
		timer = new Timer(30, this);
		timer.start();
		
	}
	
	public void newGame()
	{
		player.reset();
		enemy.reset();
		
		player.getAirStrike().deactivate();
		
		SoldierType.playerType = SoldierType.DEFAULT_SOLDIER;
		SoldierType.enemyType = SoldierType.DEFAULT_SOLDIER;
		
		GameState.state = GameState.PLAYING;
	}
	
	public void initMenus()
	{
		Command defaultCommand = new Command() {
			public void doCommand() {
				soldierMenu.itemClick(SoldierType.DEFAULT_SOLDIER);
			}
		};
		Command fastCommand = new Command() {
			public void doCommand() {
				soldierMenu.itemClick(SoldierType.FAST_SOLDIER);
			}
		};
		Command bigCommand = new Command() {
			public void doCommand() {
				soldierMenu.itemClick(SoldierType.BIG_SOLDIER);
			}
		};
		Command archerCommand = new Command() {
			public void doCommand() {
				soldierMenu.itemClick(SoldierType.ARCHER);
			}
		};
		
		Command defaultpCommand = new Command() {
			public void doCommand() {
				projectileMenu.itemClick(ProjectileType.DEFAULT_PROJECTILE);
			}
		};
		Command bigpCommand = new Command() {
			public void doCommand() {
				projectileMenu.itemClick(ProjectileType.BIG_PROJECTILE);
			}
		};
		Command redCommand = new Command() {
			public void doCommand() {
				projectileMenu.itemClick(ProjectileType.SMALL_PROJECTILE);
			}
		};
		
		BufferedImage defaultImg = ResourceLoader.loadImage(ResourceLoader.PLAYER_STILL);
		BufferedImage fastImg = ResourceLoader.loadImage(ResourceLoader.FAST_PLAYER_STILL);
		BufferedImage bigImg = ResourceLoader.loadImage(ResourceLoader.BIG_PLAYER_STILL);
		BufferedImage archerImg = ResourceLoader.loadImage(ResourceLoader.ARCHER_STILL);
		
		
		GameMenuButton defaultItem = new GameMenuButton(defaultImg, "Default", defaultCommand);
		GameMenuButton fastItem = new GameMenuButton(fastImg, "Fast", fastCommand);
		GameMenuButton bigItem = new GameMenuButton(bigImg, "Big", bigCommand);
		GameMenuButton archerItem = new GameMenuButton(archerImg, "Archer", archerCommand);
		
		GameMenuButton defaultpItem = new GameMenuButton(Projectile.DEFAULT_COLOR, "Default", defaultpCommand);
		GameMenuButton bigpItem = new GameMenuButton(Projectile.BIG_COLOR, "Big", bigpCommand);
		GameMenuButton redItem = new GameMenuButton(Projectile.SMALL_COLOR, "Red", redCommand);
		
		GameMenuButton[] arr0 = {defaultItem, fastItem, bigItem, archerItem};
		GameMenuButton[] arr1 = {defaultpItem, bigpItem, redItem};
		
		soldierMenu = new ExpandableMenu(arr0);
		projectileMenu = new ExpandableMenu(arr1);
	}

	public void setBg(Image i)
	{
		bg = i;
	}
	
	public void setPlayer(Player p)
	{
		player = p;
	}
	
	public void setEnemy(EnemyPlayer e)
	{
		this.enemy = e;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public EnemyPlayer getEnemy()
	{
		return enemy;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//draw all elements
		draw(g);
		
		//draw pause screen
		if(!GameState.isPlaying())
		{
			Graphics2D g2d = (Graphics2D)g;
			
			g2d.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
			g2d.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			g2d.setColor(Color.BLACK);
			
			Font bigFont = Main.DEFAULT_FONT.deriveFont(50f);
			String msg = "PAUSED";
			
			g2d.setFont(bigFont);
			
			FontMetrics fm = g2d.getFontMetrics();
	        Rectangle2D r = fm.getStringBounds(msg, g2d);
	        int x = (int) (Main.WIDTH*.87 / 2 - r.getHeight()/2);
	        int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
	        
	        
	        g2d.drawString(msg, x, y);
		}
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		//paint bg
		g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);

		//draw castles
		player.drawCastle(g);
		enemy.drawCastle(g);
		
		//draw gold
		String msg1 = "Gold: ";
		String msg2 = player.getGold()+"g";

		Font font = Main.DEFAULT_FONT.deriveFont(20f);
		g2d.setFont(font);
		
		FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(msg1, g2d);
        
        int x = (Main.WIDTH - (int) r.getWidth()) / 2;
        int y = GameMenuButton.HEIGHT + GameMenu.BORDER*6;
        
        g2d.drawString(msg1, x, y);
        g2d.setColor(Main.GOLD_COLOR);
        g2d.drawString(msg2, (int) (x+r.getWidth()), y);
        
        g2d.setColor(Color.BLACK);
     
        //draw airstrike icon
        if(player.hasAirStrike())
        {
        	x = x - 45;
        	y = GameMenu.HEIGHT;
        	g2d.drawImage(player.getAirStrike().getImg(), x, y, 40, 20, null);
        }
        
        //draw load loc
        String msg = "";
        if(!XMLReader.loadedFile.equals(""))
        {
        	msg = XMLReader.loadedFile.substring(XMLReader.loadedFile.lastIndexOf('\\')+1);
        }
        fm = g2d.getFontMetrics();
        r = fm.getStringBounds(msg, g2d);
        
        x = (int) (Main.WIDTH - r.getWidth()*1.5);
        
        g2d.drawString(msg, x, y);
        
        
        
		//draw soldiers
		player.drawSoldiers(g);
		enemy.drawSoldiers(g);
		
		//draw projectiles
		player.drawProjectiles(g);
		enemy.drawProjectiles(g);
		
		//draw air strike
		if(player.getAirStrike().isActive())
		{
			player.getAirStrike().draw(g);
		}
		
		//draw aim line
		Point a = MouseInfo.getPointerInfo().getLocation();
		Point mouse = new Point(a.x-this.getLocationOnScreen().x, a.y-this.getLocationOnScreen().y);
		Point p = player.getFirePoint();
		aimLine.draw(p, mouse, g);
	}
	
	public void checkCollision(double delta)
	{
		ArrayList<Soldier> sp = player.getSoldiers();
		ArrayList<Soldier> se = enemy.getSoldiers();
		ArrayList<Projectile> pp = player.getProjectiles();
		ArrayList<Projectile> pe = enemy.getProjectiles();

		for(int i=0; i<sp.size(); i++)
		{
			Soldier s = sp.get(i);
			Rectangle b = s.getBoundingBox();
			
			if(b.y + b.height != Main.GROUND.y)
			{
				int y = Main.GROUND.y - b.height;
				s.setLocation(new Point(b.x, y));
			}
			if(enemy.checkCastleCollision(b))
			{
				enemy.getCastle().addHealth((int) (-s.getDamage() * delta));
				s.collide(Soldier.VELOCITY_DIVISOR);
			}
			for(Soldier sd : enemy.checkSoldierCollisions(b))
			{
				s.collide(Soldier.VELOCITY_DIVISOR);
				sd.collide(Soldier.VELOCITY_DIVISOR);

				if(sd.getHealth() != 0 && s.getHealth() != 0)
				{
					sd.addHealth((int) (-s.getDamage() * delta));
					s.addHealth((int) (-sd.getDamage() * delta));
				}

				if(s.getHealth() <= 0)
				{
					player.removeSoldier(s);
				}
				if(sd.getHealth() <= 0)
				{
					enemy.removeSoldier(sd);
					player.addGold(s.getCost()/2);
				}
			}
		}
		for(int i=0; i<se.size(); i++)
		{
			Soldier s = se.get(i);
			Rectangle b = s.getBoundingBox();
			
			if(b.y + b.height != Main.GROUND.y)
			{
				int y = Main.GROUND.y - b.height;
				s.setLocation(new Point(b.x, y));
			}
			if(player.checkCastleCollision(b))
			{
				player.getCastle().addHealth((int) (-s.getDamage() * delta));
				s.collide(Soldier.VELOCITY_DIVISOR);
				
			}
		}
		
		for(int i=0; i<pp.size(); i++)
		{
			Projectile pr = pp.get(i);
			Rectangle b = pr.getBoundingBox();
			
			if(b.x > Main.WIDTH || b.x < 0 || b.y > Main.HEIGHT)
			{
				player.removeProjectile(pr);
			}
			
			if(!player.getAirStrike().isActive() && enemy.checkCastleCollision(b))
			{
				enemy.getCastle().addHealth((int) (-pr.getDamage() * delta));
				player.removeProjectile(pr);
			}
			for(Soldier s : enemy.checkSoldierCollisions(b))
			{
				s.collide(pr.getYVelocity());
				s.addHealth(-pr.getDamage());
				if(s.getHealth() <= 0)
				{
					enemy.removeSoldier(s);
					player.addGold(s.getCost()/2);

				}
				player.removeProjectile(pr);
			}
		}
		for(int i=0; i<pe.size(); i++)
		{
			Projectile pr = pe.get(i);
			Rectangle b = pr.getBoundingBox();
			
			if(b.x > Main.WIDTH || b.x < 0 || b.y > Main.HEIGHT)
			{
				enemy.removeProjectile(pr);
			}
			
			if(!player.getAirStrike().isActive() && player.checkCastleCollision(b))
			{
				player.getCastle().addHealth(-pr.getDamage());
				enemy.removeProjectile(pr);
			}
			for(Soldier s : player.checkSoldierCollisions(b))
			{
				s.collide(pr.getYVelocity());
				s.addHealth(-pr.getDamage());
				if(s.getHealth() <= 0)
				{
					player.removeSoldier(s);
				}
				enemy.removeProjectile(pr);
			}
		}
	}
	
	public void checkKeyPress()
	{
		if(KED.keyPressed() && TimeManager.canPress())
		{
			if(GameState.isPlaying())
			{
				if(KED.spacePressed)
				{
					player.spawnSoldier();
				}
				if(KED.shiftPressed && player.hasAirStrike() && !player.getAirStrike().isActive())
				{
					player.getAirStrike().activate();
				}
				if(KED.commaPressed)
				{
					player.addGold(50);
				}
			}
			if(KED.pPressed)
			{
				GameState gs = GameState.isPlaying() ? GameState.PAUSED : GameState.PLAYING;
				GameState.state = gs;
			}
			
			TimeManager.setLastKeyPress(System.currentTimeMillis());
		}
		

		
	}
	
	public void updateMenus() 
	{
		GameMenuButton[] solierItems = soldierMenu.getItems();
		solierItems[1].setEnabled(player.hasUpgrade(Upgrade.FAST_SOLDIER));
		solierItems[2].setEnabled(player.hasUpgrade(Upgrade.BIG_SOLDIER));
		solierItems[3].setEnabled(player.hasUpgrade(Upgrade.ARCHER));
		
		String defaultText = SoldierType.playerType == SoldierType.DEFAULT_SOLDIER ? "Equipped" : Soldier.DEFAULT_COST+"g";
		String fastText = SoldierType.playerType == SoldierType.FAST_SOLDIER ? "Equipped" : Soldier.FAST_COST+"g";
		String bigText = SoldierType.playerType == SoldierType.BIG_SOLDIER ? "Equipped" : Soldier.BIG_COST+"g";
		String archerText = SoldierType.playerType == SoldierType.ARCHER ? "Equipped" : Soldier.ARCHER_COST+"g";
		
		solierItems[0].setMessage(defaultText);
		solierItems[1].setMessage(fastText);
		solierItems[2].setMessage(bigText);
		solierItems[3].setMessage(archerText);
		
		GameMenuButton[] projectileItems = projectileMenu.getItems();
		projectileItems[1].setEnabled(player.hasUpgrade(Upgrade.BIG_PROJECTILE));
		projectileItems[2].setEnabled(player.hasUpgrade(Upgrade.SMALL_PROJECTILE));
		
		String defaultpText = ProjectileType.playerType == ProjectileType.DEFAULT_PROJECTILE ? "Equipped" : Projectile.DEFAULT_COST+"g";
		String bigpText = ProjectileType.playerType == ProjectileType.BIG_PROJECTILE ? "Equipped" : Projectile.BIG_COST+"g";
		String redText = ProjectileType.playerType == ProjectileType.SMALL_PROJECTILE ? "Equipped" : Projectile.SMALL_COST+"g";
		
		projectileItems[0].setMessage(defaultpText);
		projectileItems[1].setMessage(bigpText);
		projectileItems[2].setMessage(redText);
	}
	
	public void update()
	{
		while (true) {
			long time = (Math.abs(last - (last = System.nanoTime())));
			
			if(GameState.isPlaying())
			{
				double frac = time / (double)OPTIMAL_TIME;

				player.updateSoldiers(frac);
				player.updateProjectiles(frac);
				enemy.updateSoldiers(frac);
				enemy.updateProjectiles(frac);
				
				player.getAirStrike().update(player, enemy);
				
				if(TimeManager.canDoLogic())
				{
					enemy.logic();
					TimeManager.setLastEnemyLogic(System.currentTimeMillis());
				}
				
				checkCollision(frac);
			}
			checkKeyPress();
			updateMenus();
			
			if (time < OPTIMAL_TIME)
			{
				try {
					Thread.sleep((OPTIMAL_TIME - time) / 1000000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		Object source = arg0.getSource();
		if(source == timer)
		{
			this.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if(GameState.isPlaying())
		{
			if(TimeManager.canClick())
			{
				Point p0 = player.getFirePoint();
				Point p1 = arg0.getPoint();
				
				double slope = (double)(p0.y - p1.y) / (p1.x-p0.x);
				double angle = Math.atan(slope);
				
				player.fireProjectile(angle, aimLine.getLengthPct());
				TimeManager.setLastMouseClick(System.currentTimeMillis());
			}
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
