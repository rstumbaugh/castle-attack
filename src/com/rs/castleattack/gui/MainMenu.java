package com.rs.castleattack.gui;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.castleattack.game.GameState;
import com.rs.castleattack.io.XMLReader;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener
{
	public static final int BORDER = TextButton.HEIGHT + 10;
	
	private TextButton newGameButton,
					   continueButton,
					   instructionsButton,
					   okButton,
					   exitButton;
	
	private Command newGameCommand,
					continueCommand,
					instructionsCommand,
					okCommand,
					exitCommand;
	
	
	
	boolean showInstructions = false;
	String[] instructions = {
								"Welcome to Castle Attack! Click anywhere",
								"on the screen to fire a projectile at the",
								"enemy's castle or soldiers. Killing enemy",
								"soldiers awards you gold. Pressing SPACE",
								"spawns a soldier, although it does cost",
								"some gold. You can also use gold to ",
								"purchase upgrades in the shop. Pressing ",
								"P pauses or unpauses the game. GOOD LUCK!"
							};
	
	
	BufferedImage bg = ResourceLoader.loadImage(ResourceLoader.BACKGROUND);
	BufferedImage title = ResourceLoader.loadImage(ResourceLoader.TITLE);
	
	
	Timer timer;
	
	public MainMenu()
	{
		this.setLayout(null);

		initCommands();
		
		newGameButton = new TextButton("NEW GAME", newGameCommand);
		continueButton = new TextButton("CONTINUE GAME", continueCommand);
		instructionsButton = new TextButton("INSTRUCTIONS", instructionsCommand);
		okButton = new TextButton("OK", okCommand);
		exitButton = new TextButton("EXIT", exitCommand);
		
		newGameButton.setBounds(Main.WIDTH/2-TextButton.WIDTH/2, Main.HEIGHT/4, TextButton.WIDTH, TextButton.HEIGHT);
		continueButton.setBounds(newGameButton.getX(), newGameButton.getY()+BORDER, TextButton.WIDTH, TextButton.HEIGHT);
		instructionsButton.setBounds(continueButton.getX(), continueButton.getY()+BORDER, TextButton.WIDTH, TextButton.HEIGHT);
		exitButton.setBounds(instructionsButton.getX(), instructionsButton.getY()+BORDER, TextButton.WIDTH, TextButton.HEIGHT);
		okButton.setBounds(newGameButton.getX(), Main.HEIGHT-TextButton.HEIGHT-BORDER, TextButton.WIDTH, TextButton.HEIGHT);
		
		this.add(newGameButton);
		this.add(continueButton);
		this.add(instructionsButton);
		this.add(exitButton);
		this.add(okButton);
		
		okButton.setVisible(false);
	
		timer = new Timer(30, this);
		timer.start();
	}
	
	public void initCommands()
	{
		newGameCommand = new Command() {
			public void doCommand() {
				GameState.state = GameState.GAME_OVER;
				startGame();
			}
		};
		continueCommand = new Command() {
			public void doCommand() {
				startGame();
			}
		};
		instructionsCommand = new Command() {
			public void doCommand() {
				showInstructions();
			}
		};
		exitCommand = new Command() {
			public void doCommand() {
				System.exit(0);
			}
		};
		okCommand = new Command() {
			public void doCommand() {
				hideInstructions();
			}
		};
	}
	
	public void startGame()
	{
		PanelManager.switchPanels(this, Main.game);

		if(GameState.state == GameState.GAME_OVER)
		{
			Main.game.newGame();
			XMLReader.loadedFile = "";
		}

		GameState.state = GameState.PLAYING;
	}
	
	public void showInstructions()
	{
		newGameButton.setVisible(false);
		continueButton.setVisible(false);
		instructionsButton.setVisible(false);
		exitButton.setVisible(false);
		
		okButton.setVisible(true);
		showInstructions = true;
	}
	
	public void hideInstructions()
	{
		newGameButton.setVisible(true);
		continueButton.setVisible(true);
		instructionsButton.setVisible(true);
		exitButton.setVisible(true);
		
		okButton.setVisible(false);
		showInstructions = false;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		//paint bg
		g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
		
		//paint title
		g.drawImage(title, Main.WIDTH/2-300, 25, 600, 100, null);
		
		continueButton.setEnabled(GameState.state == GameState.PAUSED);
		
		//draw instructions
		if(showInstructions)
		{
			Graphics2D g2d = (Graphics2D)g;		
			
			Font font =  Main.DEFAULT_FONT.deriveFont(20f);
			g2d.setFont(font);
			
			FontMetrics fm = g2d.getFontMetrics();
	        Rectangle2D r = fm.getStringBounds(instructions[0], g2d);
	        int x = (this.getWidth() - (int) r.getWidth()) / 2;
	        int y = 175;
	        
	        
	        //draw instructions bg
	        float alpha = 0.5f;
	        Color c = new Color(0.5f, 0.5f, 0.5f, alpha);

	        g2d.setColor(c);
	        g2d.fillRect(x-15, y-25, (int)(r.getWidth()+75), (int)(r.getHeight()*instructions.length+10));
	        g2d.setColor(Color.BLACK);
	        
	        for(int i=0; i<instructions.length; i++)
	        {
	        	g.drawString(instructions[i], x, (int) (y+r.getHeight()*i));
	        }
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		this.repaint();
	}

}
