package com.rs.castleattack.game;

public enum GameState 
{
	PLAYING,
	PAUSED,
	GAME_OVER;
	
	public static GameState state = GAME_OVER;
	
	public static boolean isPlaying()
	{
		return state == PLAYING;
	}
}
