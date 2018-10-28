package com.jerry.mapObjects;
import java.util.Vector;
import org.json.*;


public class Hero implements MapObject,CharacterInterface{
	private double attackDistance;
	private int attackDamage;
	private double attackDamageIncreaseSpeed;
	private int AbilityPower;
	private double AbilityPowerIncreaseSped;
	private int maxHealth;
	private double HealthIncreaseSpeed;
	private int currentHealth;
	private int maxMP;
	private double MPIncreaseSpeed;
	private int currentMP;
	private int level;
	private int currentExp;
	public double attackSplit;
	public String appearance;
	public String name;
	private final static int MAXLEVEL = 18;
	private final static int []EXPNeedForLevelingUp = {100,200,300,400,600,700,
											1000,1200,1400,1500,1600,1600,
											1600,1600,1600,1600,1600};
	public String Team;
	public Hero() {
		appearance = "😺";
		level = 1;
		currentExp = 0;
	}
	public double getAttackDistance() {
		return attackDistance;
	}
	public int getAttackDamage() {
		return attackDamage;
	}
	public int getLevel() {
		return level;
	}

	public Hero(JSONObject jsonObj) {
		try{
		appearance = jsonObj.getString("appearance");
		level = 1;
		currentExp = 0;
		attackDistance = jsonObj.getDouble("attackDistance");
		attackDamage = jsonObj.getInt("attackDamage");
		attackDamageIncreaseSpeed = jsonObj.getDouble("attackDamageIncreaseSpeed");
		AbilityPower = jsonObj.getInt("AbilityPower");
		AbilityPowerIncreaseSped = jsonObj.getDouble("AbilityPowerIncreaseSpeed");
		maxHealth = jsonObj.getInt("maxHealth");
		HealthIncreaseSpeed = jsonObj.getDouble("HealthIncreaseSpeed");
		currentHealth = maxHealth;
		maxMP = jsonObj.getInt("MP");
		currentMP = maxMP;
		MPIncreaseSpeed = jsonObj.getDouble("MPIncreaseSpeed");
		Team = jsonObj.getString("Team");
		attackSplit = jsonObj.getDouble("attackSplit");}

		catch(JSONException e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Killing another player gains XP
	 * @param XP the amount of xp to gain.
	 */
	public void gainXP(int XP) {
		currentExp += XP;
		if(currentExp >= EXPNeedForLevelingUp[level - 1] && level < MAXLEVEL) {
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




	public boolean beAttacked(int damage) {
		currentHealth -= damage;
		if(currentHealth <= 0) {
			currentHealth = 0;
			return true;
		}
		return false;
	}
	// Define ReSpawn time as level * 2.
	public void ReSpawn() {
		currentHealth = maxHealth;
	}

	public void Die() {

	}
}

interface CharacterInterface {
	void ReSpawn();
	void Die();
}
