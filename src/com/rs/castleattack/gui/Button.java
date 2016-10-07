package com.rs.castleattack.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import res.ResourceLoader;

@SuppressWarnings("serial")
public class Button extends JPanel implements MouseListener 
{
	protected int width, height;

	private BufferedImage defaultImg = ResourceLoader.loadImage(ResourceLoader.BUTTON), 
						  clickedImg = ResourceLoader.loadImage(ResourceLoader.BUTTON_CLICKED);
	
	private BufferedImage currentImg;
	
	protected Command clickCommand;
	
	protected boolean isEnabled = true;
	
	public Button(int width, int height, Command command)
	{
		this.width = width;
		this.height = height;
		this.clickCommand = command;
		
		currentImg = defaultImg;
		
		this.addMouseListener(this);
	}
	
	public Button(int width, int height)
	{
		this(width, height, null);
	}
	
	public void doOnClick()
	{
		clickCommand.doCommand();
	}
	
	public void release()
	{
		mouseReleased(null);
	}
	
	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
		currentImg = isEnabled ? defaultImg : clickedImg;
	}

	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void paintComponent(Graphics g)
	{
		g.drawImage(currentImg, 0, 0, width, height, null);
		g.setColor(Color.BLACK);
	}
	
	public BufferedImage getCurrentImage()
	{
		return currentImg;
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{	
		if(isEnabled)
		{
			currentImg = clickedImg;
			
			
			doOnClick();
			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		currentImg = defaultImg;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		currentImg = clickedImg;
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		currentImg = defaultImg;
	}
}
