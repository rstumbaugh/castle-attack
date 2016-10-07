package com.rs.castleattack.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.rs.castleattack.main.Main;


@SuppressWarnings("serial")
public class Switch extends JPanel implements MouseListener
{
	public static final int WIDTH = 150,
							HEIGHT = 50,
							PADDING = 3,
							SWITCH_WIDTH = (WIDTH-PADDING*2) / 2,
							SWITCH_HEIGHT = HEIGHT - PADDING*2;
	
	private final int SPEED = 7;
	
	int onX = PADDING, offX = WIDTH - (SWITCH_WIDTH + PADDING);
	int x = onX, y = PADDING;
	
	private boolean on = true;
	
	boolean animate = false;
	
	
	
	public Switch(boolean on)
	{
		this.setLayout(null);
		this.setBackground(Color.GRAY);
		
		this.on = on;
		
		this.addMouseListener(this);
	}
	
	public void setState(boolean state)
	{
		this.on = state;
	}
	
	public boolean getState()
	{
		return on;
	}
	
	public void flip()
	{
		on = !on;
		animate = true;
	}
	
	public void animate()
	{
		int s = on ? -SPEED : SPEED;
		x += s;
		animate = on ? x > onX : x < offX;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(this.getBackground());
		g.fillRect(0,  0, WIDTH, HEIGHT);
		
		if(!animate)
		{
			x = on ? PADDING : WIDTH - SWITCH_WIDTH - PADDING;
			y = PADDING;
			
			
		}
		else
		{
			animate();
		}
		
		Color c = x+SWITCH_WIDTH/2 < WIDTH / 2 ? Main.GREEN : Color.RED;
		
		g.setColor(c);
		g.fillRect(x, y, SWITCH_WIDTH, SWITCH_HEIGHT);
		
		g.setColor(Color.BLACK);

	}
	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		flip();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
