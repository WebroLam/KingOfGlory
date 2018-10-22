package com.jerry.glory;
import java.util.*;
import com.jerry.mapObjects.*;
import org.json.*;
import java.io.FileReader;
import java.io.File;
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
	HashMap<MapObject, Location> mapObjectLocation;
	final static String ConfigurationFilePath = "src/main/resources/";

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

	private static String readJSONStringFromFile(String fileName) {
		FileReader fr;
		File file = new File(ConfigurationFilePath + fileName);
		try {
			fr = new FileReader(file);
		} catch(java.io.FileNotFoundException e) {
			System.out.println("Fuck, " + e.getMessage());
			return "";
		}
		char [] temp = new char[20000];
		try {
			fr.read(temp);
		} catch(java.io.IOException e) {
			System.out.println("Fuck, " + e.getMessage());
			return "";
		}
		String jsonString = new String(temp);
		return jsonString;
	}
	private void addHero() {
		final int heroSize = 10;
//		heroes.insertElementAt(new Hero(),0);
//		heroes.insertElementAt(new Hero(),1);
//		heroes.insertElementAt(new Hero(), 2);
//		heroes.insertElementAt(new Hero(), 3);
//		for(int i = 0;i < heroSize;i++) {
//			heroes.insertElementAt(new Hero(), i);
//		}

		JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
		
		for (int i = 0; i < heroes.size(); i++) {
			// #Locations: 1,1 3,1
			mapObjectLocation.put(heroes.elementAt(i), new Location(i * 2 + 1, i + 1));
		}
	}

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





		final int objLocations[] = {};



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


