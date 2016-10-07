package com.rs.castleattack.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.rs.castleattack.game.Upgrade;
import com.rs.castleattack.main.Main;


@SuppressWarnings("serial")
public class ShopItem extends Button 
{
	public static final int WIDTH = Main.WIDTH / 2,
							HEIGHT = Main.WIDTH / 3;
	
	private final int ICON_WIDTH = WIDTH / 5,
					  ICON_HEIGHT = HEIGHT / 3;
	
	private BufferedImage itemImg;
	private String name, description;
	private int price, spawnCost;
	private Upgrade upgrade;
	private boolean purchased;
	private Color color;
	
	
	
	public ShopItem(BufferedImage img, String name, String description, int price, int spawnCost, Upgrade upgrade, Command command) 
	{
		super(WIDTH, HEIGHT, command);
		
		this.itemImg = img;
		this.name = name;
		this.description = description;
		this.price = price;
		this.spawnCost = spawnCost;
		this.upgrade = upgrade;
	}
	
	public ShopItem(Color c, String name, String description, int price, int spawnCost, Upgrade upgrade, Command command)
	{
		this((BufferedImage)null, name, description, price, spawnCost, upgrade, command);
		this.color = c;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public Upgrade getUpgrade()
	{
		return upgrade;
	}
	
	public void setPurchased(boolean purchased)
	{
		this.purchased = purchased;
		this.setEnabled(!purchased);
	}
	
	public boolean isPurchased()
	{
		return purchased;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Font bigFont = Main.DEFAULT_FONT.deriveFont(30f);
		Font smallFont = Main.DEFAULT_FONT.deriveFont(20f);
		
		int y = HEIGHT / 10;
		
		//draw icon
		if(itemImg == null)
		{
			g2d.setColor(color);
			g2d.fillOval(WIDTH/2 - ICON_WIDTH/2, y, ICON_WIDTH, ICON_HEIGHT);
			g2d.setColor(Color.BLACK);
		}
		else
		{
			g2d.drawImage(itemImg, WIDTH/2 - ICON_WIDTH/2, y, ICON_WIDTH, ICON_HEIGHT, null);
		}
		
		g2d.setFont(bigFont);
		
		//draw name
		FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(name, g2d);
        int x = (this.getWidth() - (int) r.getWidth()) / 2;
        y += (HEIGHT/10 + ICON_HEIGHT);
        
		g2d.drawString(name, x, y);
		
		y += (r.getHeight());
		
		//draw description
		g2d.setFont(smallFont);
		fm = g2d.getFontMetrics();
        r = fm.getStringBounds(description, g2d);
        x = (this.getWidth() - (int) r.getWidth()) / 2;
        
        g2d.drawString(description, x, y);
        
        y += (HEIGHT/10);
        
        //draw spawn cost
        String msg1 = "SPAWN COST: ";
        String msg2 = spawnCost+"g";
        
        Rectangle2D totalRect = fm.getStringBounds(msg1+msg2, g2d);
        r = fm.getStringBounds(msg1, g2d);
        
        x = (WIDTH- (int) totalRect.getWidth()) / 2;

        g2d.drawString(msg1, x, y);
        x += r.getWidth();
        
        Color bg = new Color(0.5f, 0.5f, 0.5f, 0.5f);
        
        Drawer.drawTextWithUnderlay(x, y, msg2, Main.DARKER_GOLD_COLOR, bg, smallFont, g);

        y += (r.getHeight() + 10);
        
        //draw price
        String pr = price+"g";

        fm = g2d.getFontMetrics();
        r = fm.getStringBounds(pr, g2d);
        x = (this.getWidth() - (int) r.getWidth()) / 2;
        
        Drawer.drawTextWithUnderlay(x, y, pr, Main.DARKER_GOLD_COLOR, bg, bigFont, g);
        
        
        //draw purchased text
        if(purchased)
        {
        	String msg = "PURCHASED";
        	Graphics2D g2 = (Graphics2D)g.create();
            
    		AffineTransform at = new AffineTransform();
    		at.rotate(-Math.PI / 6.0);
    		g2.setFont(bigFont.deriveFont(50f).deriveFont(at));
    		fm = g2.getFontMetrics();
            r = fm.getStringBounds(msg, g2);
            x = (int) ((this.getWidth() - r.getWidth()) / 2 + fm.getAscent()/1.5);
            y = (int) ((this.getHeight() - r.getHeight()) / 2 + fm.getAscent()*2.5);
            
            g2.setColor(Color.RED);
            
    	    g2.drawString(msg, x, y);
    	    g2.dispose();
        }
        
		
	}
}
