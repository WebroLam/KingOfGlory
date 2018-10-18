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

	int xLoc,yLoc;
	String appearance;

	Hero(int x, int y) {
		appearance = "😺";
		xLoc = x;
		yLoc = y;
	}
	public void drawOnMap(String [][]Map) {
		Map[yLoc][xLoc] = appearance;
	}
}
