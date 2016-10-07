package com.rs.castleattack.game;

public enum ProjectileType
{
	DEFAULT_PROJECTILE,
	BIG_PROJECTILE,
	SMALL_PROJECTILE;
	
	public static ProjectileType playerType = DEFAULT_PROJECTILE,
								 enemyType = DEFAULT_PROJECTILE;
}
