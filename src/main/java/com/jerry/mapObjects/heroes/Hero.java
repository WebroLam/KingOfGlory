package com.jerry.mapObjects.heroes;

import com.jerry.glory.BattleFieldControl;
import com.jerry.glory.GUI;
import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.MapObject;
import com.jerry.ability.*;
import org.jetbrains.annotations.NotNull;
import org.json.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.*;
public class Hero implements MapObject,CharacterInterface{
	private static Logger logger = LogManager.getLogger();
    private final static String ImageFilePath = "src/main/resources/images/";
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
	public BufferedImage image;
	public JLabel label;
	public String name;
	private final static int MAXLEVEL = 18;
	private final static int []EXPNeedForLevelingUp = {100,200,300,400,600,700,
											1000,1200,1400,1500,1600,1600,
											1600,1600,1600,1600,1600};
    Ability ability;
    GUI game = null;
	public String Team;
	public Hero() {
	    maxHealth = 500;
	    currentHealth = maxHealth;
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
	public String getTeam() {
	    return Team;
    }

    public Hero(JSONObject obj,@NotNull GUI game) {
	    this(obj);
	    this.game = game;
    }

	public Hero(@NotNull JSONObject jsonObj) {
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
		    attackSplit = jsonObj.getDouble("attackSplit");
			String imageName = jsonObj.getString("image");
			try {
				File imageFile = new File(ImageFilePath + imageName);
				image = ImageIO.read(imageFile);
			} catch (IOException e) {
				System.out.println("Failed to read image");
				System.exit(0);
			}
		}
		catch(JSONException e) {
			System.out.println(e.getMessage());
		}
		label =new JLabel(new ImageIcon(image));
		ability = new KameHameHa();
	}

	public void attack(Hero defender) {
		if(!game.mapObjectLocation.containsKey(defender)) {
			return;
			// Oppsite Hero not in the game.
		}
		if(defender.getTeam().equals(this.getTeam())) {
			return;
			// Both hero are on the same team
		}


	}

	/**
	 * Draw this Hero's image on the map.
	 * @param g the graphics
	 * @param loc this hero's location
	 */
	public void draw(Graphics g, Location loc) {
	    if(image == null) {
	    	logger.error("no image found");
        }

        try {
	        g.drawImage(image,loc.xLoc,loc.yLoc,null);
        }
	    catch (Exception e) {
            e.printStackTrace();
        }
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
        try{
            Map[loc.yLoc][loc.xLoc] = appearance;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array out of bounds! apperance = " + appearance + "index = "+loc.xLoc +" " + loc.yLoc);
            System.exit(-1);
        }
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

    public String generateInfo() {
		String result = new String();
		result += "Hero: " + appearance;
		result += " HP: " + currentHealth + "/" + maxHealth + " MP: " + currentMP + "/" + maxMP;
		result += " Level: " + level + " Exp: " + currentExp + "\n";
		return result;
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

    public void OnDeath(final BattleFieldControl battleFieldControl) {
        final Hero deadHero = this;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(deadHero.getLevel() * 2 * 1000);
                } catch (InterruptedException e) {
                }
                battleFieldControl.SpawnHero(deadHero);
                deadHero.ReSpawn();
            }
        }).start();
    }

    public boolean moveTo(final Location loc) {
//        while (!game.mapObjectLocation.get(this).equals(loc)) {
//            game.mapObjectLocation.get(this).TravelTo(loc);
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(100);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
        final Location thisLoc = game.mapObjectLocation.get(this);
        new Thread(new Runnable() {
            public void run() {
                synchronized (game.mapObjectLocation) {
                if(thisLoc == null) {
                    game.text.append("Trigger NULL\n");
                    return;
                }

                while(true) {
                    Location oldLoc = new Location(thisLoc);
                    boolean result = thisLoc.TravelTo(loc);
                    for(Location location : game.mapObjectLocation.values()) {
                        if(thisLoc != location && thisLoc.xLoc == location.xLoc && thisLoc.yLoc == location.yLoc) {
                            thisLoc.xLoc = oldLoc.xLoc;
                            thisLoc.yLoc = oldLoc.yLoc;
                            return;
                        }
                    }
                    game.text.append("Moved onece\n");
                    game.text.append("Current Loc: " + thisLoc.xLoc + " " + thisLoc.yLoc + "\n");
                    if (result == true)
                        return;
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        }).start();
        game.text.append("Returning false\n");
        return thisLoc.xLoc == loc.xLoc && thisLoc.yLoc == loc.yLoc;
    }
}

interface CharacterInterface {

	void ReSpawn();
	void Die();
}
