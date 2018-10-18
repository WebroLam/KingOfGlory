package com.jerry.glory;
import java.util.*;
/**
 * The battle field of the game.
 * The VIEW of the whole program
 * @author Jerry
 */

public class BattleFieldControl {
	int ScreenWidth;
	int ScreenHeight;
	int playingPlayerIndex;
	BattleMap Map;
	Vector<Hero> heroes;
	HashMap<MapObject,Location> mapObjectLocation;


	BattleFieldControl(int width, int height) {
		ScreenWidth = width;
		ScreenHeight = height;
		heroes = new java.util.Vector<Hero>();
		Map = new BattleMap(ScreenHeight,ScreenWidth);
		mapObjectLocation = new HashMap<MapObject, Location>();
		playingPlayerIndex = 0;
		addHero();
		addObjects();
	}

	private void addHero() {
		heroes.insertElementAt(new Hero(),0);
		heroes.insertElementAt(new Hero(),1);

		for (int i = 0; i < heroes.size(); i++) {
			mapObjectLocation.put(heroes.elementAt(i), new Location(i * 2 + 1, i + 1));
		}
	}

	private void addObjects() {
		for(int j =0;j < ScreenHeight;j++) {
			mapObjectLocation.put(new MapBoundaries(),new Location(0,j));
			mapObjectLocation.put(new MapBoundaries(),new Location(ScreenWidth - 1,j));
		}
		for(int i = 0;i < ScreenWidth;i++) {
			mapObjectLocation.put(new MapBoundaries(),new Location(i,0));
			mapObjectLocation.put(new MapBoundaries(),new Location(i,ScreenHeight - 1));
		}
	}



	public void PrintMap() {
		Map.DrawObjects(mapObjectLocation);
		Map.PrintMap();
	}

//		for(int i = 0;i < heroes.size();i++) {
//			printAppearanceOnMap(heroes.elementAt(i).appearance,2,3);
//		}

	public boolean moveHero(final int direction, int steps) {
		Hero playingHero = heroes.elementAt(playingPlayerIndex);
		Location movingLocation = mapObjectLocation.get(playingHero);
		while(steps -- != 0 && movingLocation.Move(direction, mapObjectLocation));
		if(steps != 0)
			return false;
		return true;
	}
	public static void main(String [] args) {
		BattleFieldControl battleFieldControl = new BattleFieldControl(25,15);
		battleFieldControl.PrintMap();
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			String command = in.next();
			if(command.equals("move")) {
				int direction = in.nextInt(), steps = in.nextInt();
				battleFieldControl.moveHero(direction, steps);

			}
			battleFieldControl.PrintMap();
		}
	}

}


/**
 * The location of an object in the map.
 */
class Location {
	int xLoc;
	int yLoc;

	Location(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	void MoveRight() {
		xLoc++;
	}
	void MoveLeft() {
		xLoc--;
	}
	void MoveUp() {
		yLoc--;
	}
	void MoveDown() {
		yLoc++;
	}
	boolean MoveRight(HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(xLoc + 1 == loc.xLoc && yLoc == loc.yLoc)
				return false;
		}
		xLoc++;
		return true;
	}
	boolean MoveLeft(HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(xLoc - 1 == loc.xLoc && yLoc == loc.yLoc) {
				return false;
			}
		}
		xLoc--;
		return true;
	}
	boolean MoveUp(HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(yLoc - 1 == loc.yLoc && xLoc == loc.xLoc) {
				return false;
			}
		}
		yLoc--;
		return true;
	}
	boolean MoveDown(HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(yLoc + 1 == loc.yLoc && xLoc == loc.yLoc) {
				return false;
			}
		}
		yLoc++;
		return true;
	}
	boolean Move(int direction, HashMap<MapObject, Location> locations) {
		switch(direction) {
			case 0:
				if (! MoveUp(locations) )
					return false;
				break;
			case 1:
				if( ! MoveRight(locations))
					return false;
				break;
			case 2:
				if( !MoveDown(locations))
					return false;
				break;
			case 3:
				if( !MoveLeft(locations))
					return false;
				break;
		}
		return true;
	}

}