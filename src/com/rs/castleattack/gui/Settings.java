package com.rs.castleattack.gui;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.castleattack.game.GameState;
import com.rs.castleattack.main.Main;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class Settings extends JPanel implements ActionListener
{
	private static SettingsItem showPathItem = new SettingsItem("Show Projectile Path", "Shows path of projectile to be fired");
	private static SettingsItem saveOnQuitItem = new SettingsItem("Save On Exit", "Saves game on exit");
	
	private TextButton backButton;
	
	private BufferedImage bg = ResourceLoader.loadImage(ResourceLoader.BACKGROUND);
	
	int startX = (Main.WIDTH - SettingsItem.WIDTH) / 2;
	int startY = 25;
	
	public Settings()
	{
		this.setLayout(null);
		
		showPathItem.setBounds(startX, startY, SettingsItem.WIDTH, SettingsItem.HEIGHT);
		saveOnQuitItem.setBounds(startX, showPathItem.getY()+SettingsItem.HEIGHT, SettingsItem.WIDTH, SettingsItem.HEIGHT);
		
		this.add(showPathItem);
		this.add(saveOnQuitItem);
		
		initButtons();
		
		Timer t = new Timer(30, this);
		t.start();
	}
	
	public static boolean showPath()
	{
		return showPathItem.isEnabled();
	}
	
	public static boolean saveOnQuit()
	{
		return saveOnQuitItem.isEnabled();
	}
	
	public static void setShowPath(boolean flag)
	{
		showPathItem.setEnabled(flag);
	}
	
	public static void setSaveOnQuit(boolean flag)
	{
		saveOnQuitItem.setEnabled(flag);
	}
	
	public void initButtons()
	{
		Command backCommand = new Command() {
			public void doCommand() {
				close();
				
			}
		};
		
		backButton = new TextButton("EXIT", backCommand);
		
		backButton.setBounds(Main.WIDTH/2-(TextButton.WIDTH/2), Main.HEIGHT-TextButton.HEIGHT-50, TextButton.WIDTH, TextButton.HEIGHT);
		
		this.add(backButton);
	}
	
	public void close()
	{
		PanelManager.switchPanels(this, Main.game);
		GameState.state = GameState.PLAYING;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw bg
		g.drawImage(bg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		this.repaint();
	}
}
