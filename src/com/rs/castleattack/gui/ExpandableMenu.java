package com.rs.castleattack.gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.castleattack.game.ProjectileType;
import com.rs.castleattack.game.SoldierType;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class ExpandableMenu extends JPanel implements ActionListener
{
	public static final int COLLAPSED_WIDTH = GameMenuButton.WIDTH + 10,
							HEIGHT = GameMenuButton.HEIGHT + 10;
	
	final int EXPAND_SPEED = 20;
	
	private int width;
	
	private GameMenuButton expandButton;
	
	private GameMenuButton[] items;
	
	
	boolean expanding, collapsing;
	Rectangle r = new Rectangle(0, 0, COLLAPSED_WIDTH, HEIGHT);
	
	public ExpandableMenu(GameMenuButton[] items)
	{		
		this.setLayout(null);
		this.setBackground(new Color(0f, 0f, 0f, 0f));
		this.setOpaque(false);

		this.items = items;
		
		if(items[0].getImage() != null)
			width = (SoldierType.values().length + 1) * (GameMenuButton.WIDTH + 10);
		else
			width = (ProjectileType.values().length + 1) * (GameMenuButton.WIDTH + 10);
		
		Command expandCommand = new Command() {
			public void doCommand() {
				expanding = true;
				if(r.width >= width)
					collapsing = true;
			}
		};
		
		if(items[0].getImage() != null)
		{
			BufferedImage img = ResourceLoader.loadImage(ResourceLoader.PLAYER_STILL);
			expandButton = new GameMenuButton(img, "Equip", expandCommand);
		}
		else
		{
			expandButton = new GameMenuButton(Color.BLACK, "Equip", expandCommand);
		}
		

		expandButton.setBounds(5, 5, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
		
		this.add(expandButton);
		
		int x = COLLAPSED_WIDTH+10, y = 5;
		
		for(int i=0; i<items.length; i++)
		{
			GameMenuButton item = items[i];
			item.setBounds(x, y, GameMenuButton.WIDTH, GameMenuButton.HEIGHT);
			this.add(item);
			item.setVisible(false);
			
			x += GameMenuButton.WIDTH + 5;
		}

		Timer timer = new Timer(30, this);
		timer.start();
	}
	
	public GameMenuButton[] getItems()
	{
		return items;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void itemClick(SoldierType type)
	{
		SoldierType.playerType = type;
		collapsing = true;
		expanding = false;
	}
	
	public void itemClick(ProjectileType type)
	{
		ProjectileType.playerType = type;
		collapsing = true;
		expanding = false;
	}
	
	public void expand()
	{
		if(expanding)
		{
			r.width += EXPAND_SPEED;
			expanding = r.width < width;
			
			for(GameMenuButton item : items)
			{
				if(r.width >= item.getX()+item.getWidth())
					item.setVisible(true);
			}
		}
	}
	
	public void collapse()
	{
		if(collapsing)
		{
			r.width -= EXPAND_SPEED;
			collapsing = r.width > COLLAPSED_WIDTH;
			
			for(GameMenuButton item : items)
			{
				if(r.width < item.getX()+item.getWidth())
					item.setVisible(false);
			}
		}
	}
	
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        g.fillRect(r.x, r.y, r.width, r.height);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		expand();
		collapse();
		
		
	}
}
