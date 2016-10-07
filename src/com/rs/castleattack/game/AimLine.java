package com.rs.castleattack.game;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import com.rs.castleattack.gui.Settings;
import com.rs.castleattack.main.Main;



public class AimLine 
{
	public static final int MAX_LENGTH = 100;
	
	private double length;
	
	public double getDistance(Point p1, Point p2)
	{
		return Math.sqrt(Math.abs((p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y)));
	}
	
	public double getLengthPct()
	{
		return length / MAX_LENGTH;
	}
	
	public void draw(Point start, Point end, Graphics g)
	{
		//draw line
		double slope = (double)(start.y - end.y) / (end.x-start.x);
		double angle = Math.atan(slope);
		
		double dist = getDistance(start, end);

		int startX = start.x;
		int startY =  start.y;
		int endX = end.x;
		int endY = end.y;
		
		if(dist > MAX_LENGTH)
		{
			endX = (int) (start.x  + MAX_LENGTH * Math.cos(Math.PI*2 - angle));
			endY = (int) (start.y + MAX_LENGTH * Math.sin(Math.PI*2 - angle));
		}
		
		length = getDistance(start, new Point(endX, endY));
		
		Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setColor(Color.RED);
        g2d.setStroke(dashed);
        g2d.drawLine(startX, startY, endX, endY);
        g2d.dispose();
        
        //draw path
        if(Settings.showPath())
        {
			g.setColor(Color.RED);
			int x = start.x, y = start.y;
			 
			int xs=0, ys=0;
			switch(ProjectileType.playerType)
			{
				case DEFAULT_PROJECTILE:
					xs = Projectile.DEFAULT_XSPEED;
					ys = Projectile.DEFAULT_YSPEED;
					break;
				case BIG_PROJECTILE:
					xs = Projectile.BIG_XSPEED;
					ys = Projectile.BIG_YSPEED;
					break;
				case SMALL_PROJECTILE:
					xs = Projectile.SMALL_XSPEED;
					ys = Projectile.SMALL_YSPEED;
					break;
			}
			double xvel = Math.cos(angle) * (xs * getLengthPct());
			double yvel = -Math.sin(angle) * (ys * getLengthPct());
			
			while(y+yvel <= Main.GROUND.y)
			{
				x += xvel;
				y += yvel;
				
				g.drawLine(x, y, (int)(x+xvel/2), (int)(y+yvel/2));
				
				yvel += 1;
			}
			
			g.setColor(Color.BLACK);
        }
       
	}
}
