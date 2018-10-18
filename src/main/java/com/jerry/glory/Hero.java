package com.jerry.glory;

public class Hero implements com.jerry.mapObjects.MapObject {
	int attackDamage;
	int AbilityPower;
	int maxHealth;
	int currentHealth;
	int maxMP;
	int currentMP;
	int level;
	int currentExp;
	String appearance;
	String name;
	Hero() {
		appearance = "😺";
		level = 1;
		currentExp = 0;
	}
	public void drawOnMap(String [][]Map, com.jerry.mapObjects.Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}
}
