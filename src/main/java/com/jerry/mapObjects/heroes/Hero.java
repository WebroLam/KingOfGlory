package com.jerry.mapObjects.heroes;

import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.MapObject;
import com.jerry.ability.*;
import org.json.*;


public class Hero implements MapObject,CharacterInterface{
	protected double attackDistance;
	protected int attackDamage;
	protected double attackDamageIncreaseSpeed;
	protected int AbilityPower;
	protected double AbilityPowerIncreaseSped;
	protected int maxHealth;
	protected double HealthIncreaseSpeed;
	protected int currentHealth;
	protected int maxMP;
	protected double MPIncreaseSpeed;
	protected int currentMP;
	protected int level;
	protected int currentExp;
	public double attackSplit;
	public String appearance;
	public String name;
	private final static int MAXLEVEL = 18;
	private final static int []EXPNeedForLevelingUp = {100,200,300,400,600,700,
											1000,1200,1400,1500,1600,1600,
											1600,1600,1600,1600,1600};
    Ability ability;
	public String Team;
	public Hero() {
		appearance = "😺";
		level = 1;
		currentExp = 0;
	}

    public Ability getAbility() {
        return ability;
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
		ability = new KameHameHa();
	}
    public int getAbilityPower() {
        return AbilityPower;
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
    /**
     * Level Up
     */
	protected void levelUp() {
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


	public int getCurrentHealth() {
	    return currentHealth;
    }
	// Define ReSpawn time as level * 2.
	public void ReSpawn() {
		currentHealth = maxHealth;
		currentMP = maxMP;
	}
	public void Die() {

	}

    /**
     * Print basic info of this hero in one line.
     */
	public void PrintInfo() {
	    System.out.print("Hero: " + appearance);
	    System.out.print(" HP: " + currentHealth + "/" + maxHealth + " MP: " + currentMP + "/" + maxMP);
	    System.out.println(" Level: " + level + " Exp: " + currentExp);
    }


    /**
     * Make this hero be attacked by certain damage
     * @param damage the amount of damage to take.
     * @return if that damage kills this hero.
     */
    public boolean beAttacked(int damage) {
        currentHealth -= damage;
        if(currentHealth <= 0) {
            currentHealth = 0;
            return true;
        }
        return false;
    }
    /**
     * Restore specific amount health to hero.
     * @param amount The amount of health to be restored.
     */
    public void restoreHealth(int amount) {
	    if(currentHealth == 0)
	        return;
	    currentHealth += amount;
	    if(currentHealth > maxHealth) {
	        currentHealth = maxHealth;
        }
    }


    /**
     * Use this hero's ability on other hero.
     * @param target the target hero
     * @return if that move kills the target
     */
    public boolean castSpell(Hero target) {
        if(currentMP < ability.getMPCost()) {
            return false;
        }

        currentMP -= ability.getMPCost();

        boolean killedTarget = ability.cast(target,AbilityPower);
        if(killedTarget) {
            gainXP(200); // Killing a Hero gains 200 xp.
        }
        return killedTarget;
    }

}

interface CharacterInterface {
	void ReSpawn();
	void Die();
}
