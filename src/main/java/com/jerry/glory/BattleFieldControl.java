package com.jerry.glory;
import java.util.*;

import com.jerry.mapObjects.*;
import com.jerry.mapObjects.heroes.Bomber;
import com.jerry.mapObjects.heroes.Healer;
import com.jerry.mapObjects.heroes.Hero;
import com.jerry.mapObjects.heroes.SelfHealer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.io.File;
import com.jerry.jsonHandle;
/**
 * The battle field of the game.
 * The Controller of the whole program
 * @author Jerry
 */
public class BattleFieldControl {
    private static final Logger logger = LogManager.getLogger(BattleFieldControl.class);
	private int ScreenWidth;
	private int ScreenHeight;
	private int playingPlayerIndex;
	private BattleMap Map;
	private Vector<Hero> heroes;
	private HashMap<MapObject, Location> mapObjectLocation;
	private final static String ResourcesFilePath = "src/main/resources/";
	private Location[] SpawnPointTeamRed;
	private Location[] SpawnPointTeamBlue;
    private Thread printMap;
	public BattleFieldControl(int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		heroes = new java.util.Vector<Hero>();
		Map = new BattleMap(ScreenHeight,ScreenWidth);
		mapObjectLocation = new HashMap<MapObject, Location>();
		playingPlayerIndex = 0;
		SpawnPointTeamRed = new Location[10];
		SpawnPointTeamBlue = new Location[10];
		initSpawnLocation();
		addHero();
		addObjects();
		initTreads();
	}
    private void initTreads() {
	    printMap = new Thread(
                new Runnable() {
                    public void run() {
                        while(true) {
                            try {
                                PrintMapWithInformation();

                                Thread.sleep(40); //FPS 25
                            } catch (InterruptedException e) {
                                logger.error("Interrupted PrintMap " + e.getMessage());
                            }
                        }
                    }
                }
        );
	    for(final Hero hero:heroes) {
	        Thread t = new Thread(new Runnable() {
                public void run() {
                    final File commands = new java.io.File(ResourcesFilePath + "commands");
                    Scanner in = null;
                    try {
                        in = new Scanner(commands);
                    } catch (java.io.FileNotFoundException e) {
                        logger.error("Error reading file");
                        System.exit(-1);
                    }
                    performOp(in,hero);
                }
            });
	        t.setPriority(1);
	        t.start();
        }
        printMap.setPriority(10);
        printMap.start();

    }




    public void SpawnHero(@NotNull Hero hero) {
	    Location[] SpawnPoints;
	    if(hero.Team.equals("Red")) {
            SpawnPoints = SpawnPointTeamRed;
        }
        else {
            SpawnPoints = SpawnPointTeamBlue;
        }
        boolean foundSame = false;
        for(int i = 0;i < SpawnPoints.length;i++) {
            foundSame = false;
            for (Location loc : mapObjectLocation.values()) {
                if(loc.equals(SpawnPoints[i])) {
                    foundSame = true;
                    break;
                }
            }
            if(foundSame)
                continue;
          Location foundLoc = new Location(SpawnPoints[i]);
          mapObjectLocation.put(hero,foundLoc);
        }
    }

	/**
	 * TODO: Code needs to be changed based on changing the spawn location
	 */
	private void initSpawnLocation() {
	    int indexForBlue = 0;
        for(int i = 1;i <= 2;i++) {
            for(int j = 1;j<=5;j++) {
                SpawnPointTeamBlue[indexForBlue++] = new Location(i,j);
            }
        }
        int indexForRed = 0;
        for(int i = 22;i <= 23;i++) {
            for(int j = 9;j<=13;j++)
                SpawnPointTeamRed[indexForRed++] = new Location(i,j);
        }


    }

	/**
	 * Add Hero to the Game.
	 */
	private void addHero() {
		final int heroSize = 10;
		try{
		    JSONObject jsonObject = new JSONObject(jsonHandle.readJSONStringFromFile("Heroes.json"));
		    JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
		    int index = 0;
		    for(;index < 3;index++) {
		    	heroes.insertElementAt(new Hero(heroJSON.getJSONObject(index)),0);
		    } // 3 normal Hero
            heroes.insertElementAt(new Healer(heroJSON.getJSONObject(index++)),0);
		    heroes.insertElementAt(new SelfHealer(heroJSON.getJSONObject(index++)) , 0);
            for(;index < 8;index++) {
                heroes.insertElementAt(new Hero(heroJSON.getJSONObject(index)),0);
            }
            heroes.insertElementAt(new Healer(heroJSON.getJSONObject(index++)),0);
            heroes.insertElementAt(new Bomber(heroJSON.getJSONObject(index++)) , 0);

		} catch (JSONException e) {
			System.out.println(e.toString());
		}
        for(Hero hero : heroes) {
            SpawnHero(hero);
        }

	}


	/**
	 * Add bounds to the game, such as trees.
	 */
	private void addObjects() {
		for(int j =0;j < ScreenHeight;j++) {
			mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(0,j));
			mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(ScreenWidth - 1,j));
		}
		for(int i = 0;i < ScreenWidth;i++) {
			mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(i,0));
			mapObjectLocation.put(new MapBoundaries(),new com.jerry.mapObjects.Location(i,ScreenHeight - 1));
		} // Adding objects for the constraint of the map.

        for(int i = 7,j = 11, count = 0;count <= 8;count++) {
            mapObjectLocation.put(new MapBoundaries(), new Location(i++,j--));
        }


		//TODO: Find the object locations.
		final int objLocations[] = {};

	}


	/**
	 * Paint the Whole Map again and print it on the screen.
	 */
	public void PrintMap() {
		Map.DrawObjects(mapObjectLocation);
		Map.PrintMap();
	}

	public String GenerateMapString() {
		Map.DrawObjects(mapObjectLocation);
		return Map.generateMap();
	}
	/**
	 * Move a hero, step by step. Stops if meet an obstacle.
	 * @param direction w for upward, d for right.
	 * @param steps how many steps to move.
	 * @return If the Hero moved expected num of steps.
	 */
	public boolean moveHero(final char direction, int steps) {
		Location movingLocation = mapObjectLocation.get(playingHero); //TODO: ERROR!: shouldn't use playing hero here.
		while(steps -- != 0 && movingLocation.Move(direction, mapObjectLocation));
        return steps == 0;
    }
    /**
     * Move a hero, step by step. Stops if meet an obstacle.
     * @param direction w for upward, d for right.
     * @param steps how many steps to move.
     * @param hero who
     * @return If the Hero moved expected num of steps.
     */
    public boolean moveHero(final char direction, int steps,Hero hero) {
        Location movingLocation = mapObjectLocation.get(hero);
        while(steps -- != 0 && movingLocation.Move(direction, mapObjectLocation));
        return steps == 0;
    }

	public boolean Attacks(Hero attacker, Location target ){
		if(mapObjectLocation.get(attacker).distanceTo(target) > attacker.getAttackDistance()) {
			System.out.println("Attacker is too far away from target.");
			return false;
		}
		else  {
            Location attackerLoc = mapObjectLocation.get(attacker);
            // Painting the bullet on the map.
            TravelingObj bullet = new TravelingObj();
            Location bulletLoc = new Location(attackerLoc);
            bulletLoc.TravelTo(target);
            mapObjectLocation.put(bullet,bulletLoc);
            PrintMap();
            while(!bulletLoc.TravelTo(target)) {
                for (Location loc : mapObjectLocation.values()) {
                    if (loc.equals(bulletLoc) && !loc.equals(target)) {
                        return false; // If there is an object between two location then the target cannot be attacked.
                    }
                }
                PrintMap();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
            mapObjectLocation.remove(bullet);
		    for (final Hero hero: heroes) {
				if(hero == attacker)
					continue;
				Location heroLoc = mapObjectLocation.get(hero);
				if(heroLoc.distanceTo(target) <= attacker.attackSplit) {
					if(hero.beAttacked(attacker.getAttackDamage())) {
						//Do something if the target dies.
						mapObjectLocation.remove(hero);

						new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(hero.getLevel() * 2 * 1000);
								} catch (InterruptedException e) {
									System.out.println();
								}
							    SpawnHero(hero);
								hero.ReSpawn();
								PrintMap();
							}
						}).start();
						System.out.println(hero.appearance + " got attack and died");
						// Determine how much XP to gain.
						attacker.gainXP(500);
					}
				}

			}
		}
		return true;
	}
    public void heal(Healer healer, Location target) {
        // Painting the heart on the map.
        Location healerLoc = mapObjectLocation.get(healer);
        TravelingObj heart= new TravelingObj("❤");
        Location heartLoc = new Location(healerLoc);
        heartLoc.TravelTo(target);
        mapObjectLocation.put(heart,heartLoc);
        PrintMap();
        while(!heartLoc.TravelTo(target)) {
            PrintMap();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        mapObjectLocation.remove(heart);

        for(final Hero hero : heroes) {
            Location heroLoc = mapObjectLocation.get(hero);
            if(heroLoc.distanceTo(target) <= healer.getHealRange()) {
                healer.heals(hero);
            }
        }
    }


    public void PrintMapWithInformation() {
	    System.out.println("**********Blue Team**********");
	    for(final Hero hero : heroes) {
	        Location loc = mapObjectLocation.get(hero);
	        if(!hero.Team.equals("Blue"))
	            continue;
	        hero.PrintInfo();
//	        System.out.println("x y =" + loc.xLoc + " " + loc.yLoc);
        }
        System.out.println("**********Red Team**********");
        for(final Hero hero : heroes) {
            Location loc = mapObjectLocation.get(hero);
            if(!hero.Team.equals("Red"))
                continue;
            hero.PrintInfo();
//            System.out.println("x y =" + loc.xLoc + " " + loc.yLoc);
        }
	    PrintMap();
    }

    public String GenerateMapWithInformation() {
		String result = new String();
		result += "**********Blue Team**********" + "\n";
	    for(final Hero hero : heroes) {
		    if(!hero.Team.equals("Blue"))
			    continue;
		    result += hero.generateInfo();
	    }
	    result += "**********Red Team**********" + "\n";
	    for(final Hero hero : heroes) {
		    if(!hero.Team.equals("Red"))
			    continue;
		    result += hero.generateInfo();
	    }
	    return result;
    }

    public boolean castSpell(Hero caster,Location casterLoc ,Location target) {
        DrawMovingObj(casterLoc,target,caster.getAbility());
        for(final Hero hero:heroes) {
            if(hero.Team.equals(caster.Team)) {
                continue;
            }
            Location heroLoc = mapObjectLocation.get(hero);
            if(heroLoc.distanceTo(target) > caster.getAbility().getRange()) {
                continue;
            }
            if(caster.castSpell(hero)) {
                mapObjectLocation.remove(hero);
                hero.OnDeath(this);
                System.out.println(hero.appearance + " got attack and died");
            }
        }
        return true;
    }
    public void DrawMovingObj(Location start,Location end,MapObject obj) {
	    Location objLoc = new Location(start);
        objLoc.TravelTo(end);
        mapObjectLocation.put(obj,objLoc);
        PrintMap();
        while(!objLoc.TravelTo(end)) {
            PrintMap();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        mapObjectLocation.remove(obj);
    }

    Hero nextHero() {
        do {
            if(playingHero == battleFieldControl.heroes.lastElement()) {
                playingHero = battleFieldControl.heroes.firstElement();

            } else {
                int nextIndex = battleFieldControl.heroes.indexOf(playingHero) + 1;
                playingHero = battleFieldControl.heroes.elementAt(nextIndex);
            }
        } while(!battleFieldControl.mapObjectLocation.containsKey(playingHero));
        return playingHero;
    }

    static void performOp(@NotNull Scanner in) {
	    playingHero = battleFieldControl.nextHero();
		while(in.hasNext()) {
			String command = in.next();
			if(command.equals("move")) {
				String temp = in.next();
				char direction = temp.charAt(0);
				int steps = in.nextInt();
				battleFieldControl.moveHero(direction, steps);
			}
			else if(command.equals("attack")) {
				int xLoc = in.nextInt(), yLoc = in.nextInt();
				// Location of target
				Location target = new Location(xLoc,yLoc);
				battleFieldControl.Attacks(playingHero,target);
			} else if (command.equals("switch")) {
			    playingHero = battleFieldControl.nextHero();
				System.out.println("Now playing hero is " + playingHero.appearance);
			} else if(command.equals("heal")) {
				int xLoc = in.nextInt(), yLoc = in.nextInt();
				Location target = new Location(xLoc,yLoc);
				battleFieldControl.heal(((Healer) playingHero),target);
			} else if(command.equals("cast")) {
				int xLoc = in.nextInt(), yLoc = in.nextInt();
				Location target = new Location(xLoc,yLoc);
				Location nowLoc = battleFieldControl.mapObjectLocation.get(playingHero);
				battleFieldControl.castSpell(playingHero,nowLoc,target);
			}

		}
	}

    void performOp(@NotNull Scanner in,Hero hero) {
        while(in.hasNext()) {
            if(!mapObjectLocation.containsKey(hero)) {
                continue;
            }
            String command = in.next();
            if(command.equals("move")) {
                String temp = in.next();
                char direction = temp.charAt(0);
                int steps = in.nextInt();
                this.moveHero(direction, steps,hero);
            }
            else if(command.equals("attack")) {
                int xLoc = in.nextInt(), yLoc = in.nextInt();
                Location target = new Location(xLoc,yLoc);
                this.Attacks(hero,target);
            } else if (command.equals("switch")) {
                hero = this.nextHero();
                System.out.println("Now playing hero is " + hero.appearance);
            } else if(command.equals("heal")) {
                int xLoc = in.nextInt(), yLoc = in.nextInt();
                Location target = new Location(xLoc,yLoc);
                this.heal(((Healer) hero),target);
            } else if(command.equals("cast")) {
                int xLoc = in.nextInt(), yLoc = in.nextInt();
                Location target = new Location(xLoc,yLoc);
                Location nowLoc = this.mapObjectLocation.get(hero);
                this.castSpell(hero,nowLoc,target);
            }
            //Slower the process.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }


	static BattleFieldControl battleFieldControl = new BattleFieldControl(25,15);
    static Hero playingHero;
	public static void main(String [] args) {
        BattleFieldControl battleFieldControl = new BattleFieldControl(25,15);
		playingHero = battleFieldControl.heroes.elementAt(0);
	}

}


