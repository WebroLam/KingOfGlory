package com.jerry.glory;

public class Hero implements MapObject{
	int attackDamage;
	int AbilityPower;
	int Speed;
	int maxHealth;
	int currentHealth;
	int maxMP;
	int currentMP;
	int level;
	int currentExp;
	boolean isAccessible = false;
	String appearance;

	Hero() {
		appearance = "😺";
	}
	public void drawOnMap(String [][]Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}
}
