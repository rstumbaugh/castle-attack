package com.rs.castleattack.game;

public enum SoldierType 
{
	DEFAULT_SOLDIER,
	FAST_SOLDIER,
	BIG_SOLDIER,
	ARCHER;
	
	public static final int[] MAX_SPAWNS = {10, 15, 3, 1};
	
	public static SoldierType playerType = DEFAULT_SOLDIER,
							  enemyType = DEFAULT_SOLDIER;
}
