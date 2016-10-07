package com.rs.castleattack.game;

public class TimeManager 
{
	public static long keyPressBuffer = 500,
					   mouseClickBuffer = 500,
					   enemyLogicBuffer = 200,
					   airStrikeBuffer = 300;
	
	private static long lastKeyPress, lastMouseClick, lastGameLogic, lastEnemyLogic, lastAirStrike;
	
	public static void setLastKeyPress(long time)
	{
		lastKeyPress = time;
	}
	
	public static void setLastMouseClick(long time)
	{
		lastMouseClick = time;
	}
	
	public static void setLastEnemyLogic(long time)
	{
		lastEnemyLogic = time;
	}
	
	public static void setLastGameLogic(long time)
	{
		lastGameLogic = time;
	}
	
	public static void setLastAirStrike(long time)
	{
		lastAirStrike = time;
	}
	
	public static long getLastGameLogic()
	{
		return lastGameLogic;
	}
	
	public static boolean canPress()
	{
		return (System.currentTimeMillis() - lastKeyPress) >= keyPressBuffer;
	}
	
	public static boolean canClick()
	{
		return (System.currentTimeMillis() - lastMouseClick) >= mouseClickBuffer;
	}
	
	public static boolean canDoLogic()
	{
		return (System.currentTimeMillis() - lastEnemyLogic) >= enemyLogicBuffer;
	}
	
	public static boolean canDoAirStrike()
	{
		return (System.currentTimeMillis() - lastAirStrike) >- airStrikeBuffer;
	}
}
