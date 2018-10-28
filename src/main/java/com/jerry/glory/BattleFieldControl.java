package com.jerry.glory;
import java.util.*;

import com.jerry.ability.Ability;
import com.jerry.mapObjects.*;
import com.jerry.mapObjects.heroes.Healer;
import com.jerry.mapObjects.heroes.Hero;
import org.json.*;
import java.io.FileReader;
import java.io.File;


/**
 * The battle field of the game.
 * The Controller of the whole program
 * @author Jerry
 */
public class BattleFieldControl {
	private int ScreenWidth;
	private int ScreenHeight;
	private int playingPlayerIndex;
	private BattleMap Map;
	private Vector<Hero> heroes;
	private HashMap<MapObject, Location> mapObjectLocation;
	private final static String ResourcesFilePath = "src/main/resources/";
	private Location[] SpawnPointTeamRed;
	private Location[] SpawnPointTeamBlue;
	public BattleFieldControl(int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		heroes = new java.util.Vector<Hero>();
		Map = new BattleMap(ScreenHeight,ScreenWidth);
		mapObjectLocation = new HashMap<MapObject, Location>();
		playingPlayerIndex = 0;
		SpawnPointTeamRed = new Location[10];
		SpawnPointTeamBlue = new Location[10];
		addHero();
		addObjects();

	}

	/**
	 * To take a json file into a String
	 * @param fileName the name of the file. The file shall be in resources folder.
	 * @return Casted String of the file.
	 */
	private static String readJSONStringFromFile(final String fileName) {
		FileReader fr;
		File file = new File(ResourcesFilePath + fileName);
		try {
			fr = new FileReader(file);
		} catch(java.io.FileNotFoundException e) {
			System.out.println("Fuck, " + e.getMessage());
			return "";
		}
		char [] temp = new char[9000000];
		try {
			fr.read(temp);
		} catch(java.io.IOException e) {
			System.out.println("Fuck, " + e.getMessage());
			return "";
		}
		String jsonString = new String(temp);
		return jsonString;
	}

	/**
	 * Add Hero to the Game.
	 */
	private void addHero() {
		final int heroSize = 10;
//		heroes.insertElementAt(new Hero(),0);
//		heroes.insertElementAt(new Hero(),1);
//		heroes.insertElementAt(new Hero(), 2);
//		heroes.insertElementAt(new Hero(), 3);
//		for(int i = 0;i < heroSize;i++) {
//			heroes.insertElementAt(new Hero(), i);
//		}

		try{
		    JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
		    JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
		    for(int i = 0;i < heroJSON.length() - 1;i++) {
		    	heroes.insertElementAt(new Hero(heroJSON.getJSONObject(i)),0);
		    }
		    heroes.insertElementAt(new Healer(heroJSON.getJSONObject(heroJSON.length() - 1)),0);


		} catch (JSONException e) {
			System.out.println(e.toString());
		}


		for (int i = 0; i <= heroes.size() / 2; i++) {
			// #Locations: 1,1 3,1
			mapObjectLocation.put(heroes.elementAt(i), new Location(i * 2 + 1, i + 1));

		}

		for(int i = heroes.size() / 2 + 1;i < heroes.size();i++) {
			mapObjectLocation.put(heroes.elementAt(i), new Location(i * 2 + 2,i * 2));
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

		// TODO: Add spawn area


//		for(int j =0;j < ScreenHeight;j++) {
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(j)),new Location(0 + 1,j));
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(j)),new Location(ScreenWidth - 2,j));
//		}
//		for(int i = 0;i < ScreenWidth;i++) {
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(i)),new Location(i,1));
//			mapObjectLocation.put(new MapBoundaries(Integer.toString(i)),new Location(i,ScreenHeight - 2));
//		}

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


//		for(int i = 0;i < heroes.size();i++) {
//			printAppearanceOnMap(heroes.elementAt(i).appearance,2,3);
//		}

	/**
	 * Move a hero, step by step. Stops if meet an obstacle.
	 * @param direction 1 for upward, 2 for right.
	 * @param steps how many steps to move.
	 * @return If the Hero moved expected num of steps.
	 */
	public boolean moveHero(final char direction, int steps) {
		Location movingLocation = mapObjectLocation.get(playingHero);
		while(steps -- != 0 && movingLocation.Move(direction, mapObjectLocation));
		if(steps != 0)
			return false;
		return true;
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
								// Define respawn time = level * 2
								try {
									Thread.sleep(hero.getLevel() * 2 * 1000);
								} catch (InterruptedException e) {
									System.out.println("");
								}
								//TODO: Determine where to spawn this hero
								mapObjectLocation.put(hero,new Location(1, 1));
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
	        if(!hero.Team.equals("Blue"))
	            continue;
	        hero.PrintInfo();
        }
        System.out.println("**********Red Team**********");
        for(final Hero hero : heroes) {
            if(!hero.Team.equals("Red"))
                continue;
            hero.PrintInfo();
        }

	    PrintMap();
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
            caster.castSpell(hero);
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
    static Hero playingHero;
	public static void main(String [] args) {
		BattleFieldControl battleFieldControl = new BattleFieldControl(25,15);
		battleFieldControl.PrintMap();
		Scanner in = new Scanner(System.in);
		playingHero = battleFieldControl.heroes.elementAt(0);
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
			    do {
                    if(playingHero == battleFieldControl.heroes.lastElement()) {
                        playingHero = battleFieldControl.heroes.firstElement();

                    } else {
                        int nextIndex = battleFieldControl.heroes.indexOf(playingHero) + 1;
                        playingHero = battleFieldControl.heroes.elementAt(nextIndex);
                    }
                } while(!battleFieldControl.mapObjectLocation.containsKey(playingHero));
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
			battleFieldControl.PrintMapWithInformation();
		}
	}

}


