package com.rs.castleattack.ked;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class KED implements KeyEventDispatcher
{	
	public static boolean pPressed, spacePressed, aPressed, dPressed, shiftPressed, commaPressed;
	
	public static boolean keyPressed()
	{
		return pPressed || spacePressed || aPressed || dPressed || shiftPressed || commaPressed;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent arg0) 
	{
		int kc = arg0.getKeyCode();
		
		if(arg0.getID() == KeyEvent.KEY_PRESSED)
		{
			if(kc == KeyEvent.VK_P)
			{
				pPressed = true;
			}
			else if(kc == KeyEvent.VK_SPACE)
			{
				spacePressed = true;
			}
			else if(kc == KeyEvent.VK_A)
			{
				aPressed = true;
			}
			else if(kc == KeyEvent.VK_D)
			{
				dPressed = true;
			}
			else if(kc == KeyEvent.VK_SHIFT)
			{
				shiftPressed = true;
			}
			else if(kc == KeyEvent.VK_COMMA)
			{
				commaPressed = true;
			}
		}
		if(arg0.getID() == KeyEvent.KEY_RELEASED)
		{
			if(kc == KeyEvent.VK_P)
			{
				pPressed = false;
			}
			else if(kc == KeyEvent.VK_SPACE)
			{
				spacePressed = false;
			}
			else if(kc == KeyEvent.VK_A)
			{
				aPressed = false;
			}
			else if(kc == KeyEvent.VK_D)
			{
				dPressed = false;
			}
			else if(kc == KeyEvent.VK_SHIFT)
			{
				shiftPressed = false;
			}
			else if(kc == KeyEvent.VK_COMMA) 
			{
				commaPressed = false;
			}
		}
		
		
		return false;
	}

}
