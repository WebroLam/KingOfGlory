package com.jerry.mapObjects;
import java.util.Vector;


public class Hero implements com.jerry.mapObjects.MapObject {
	double attackDistance;
	int attackDamage;
	double attackDamageIncreaseSpeed;
	int AbilityPower;
	double AbilityPowerIncreaseSped;
	int maxHealth;
	double HealthIncreaseSpeed;
	int currentHealth;
	int maxMP;
	double MPIncreaseSpeed;
	int currentMP;
	int level;
	int currentExp;
	String appearance;
	String name;
	final static int MAXLEVEL = 18;
	final static int []EXPNeedForLevelingUp = {100,200,300,400,600,700,
											1000,1200,1400,1500,1600,1600,
											1600,1600,1600,1600,1600};



	public Hero() {
		appearance = "😺";
		level = 1;
		currentExp = 0;
	}

	/**
	 * Killing another player gains XP
	 * @param XP the amount of xp to gain.
	 */
	public void gainXP(int XP) {
		currentExp += XP;
		if(currentExp > EXPNeedForLevelingUp[currentExp] && level < MAXLEVEL) {
			currentExp = 0;
			levelUp();
		}
	}
	private void levelUp() {
		level++;
		currentHealth   += (maxHealth - currentHealth) * 0.5;
		currentMP       += (maxMP - currentMP) * 0.5;
		attackDamage *= attackDamageIncreaseSpeed;
		AbilityPower *= AbilityPowerIncreaseSped;
		maxHealth *= HealthIncreaseSpeed;
		maxMP *= MPIncreaseSpeed;

	}
	public void drawOnMap(String [][]Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}
}
