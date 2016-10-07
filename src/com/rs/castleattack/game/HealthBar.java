package com.rs.castleattack.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

@SuppressWarnings("serial")
public class HealthBar extends Rectangle implements Serializable
{
	
	public HealthBar(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public HealthBar(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
	
	public Color getColor(double pct)
	{
	    double H = pct * 0.35;
	    double S = 0.9;
	    double B = 0.9;
	
	    return Color.getHSBColor((float)H, (float)S, (float)B);
	}
	
	public void draw(double pct, Graphics g)
	{
		g.setColor(getColor(pct));
		g.fillRect(x, y, (int) (width*pct), height);
		g.setColor(Color.BLACK);
	}
	
	public void draw(int x, int y, double pct, Graphics g)
	{
		g.setColor(getColor(pct));
		g.fillRect(x, y, (int) (width*pct), height);
		g.setColor(Color.BLACK);
	}
}
