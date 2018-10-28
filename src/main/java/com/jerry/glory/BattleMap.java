package com.jerry.glory;
import java.util.HashMap;
import com.jerry.mapObjects.*;
/**
 * The Map of the game.
 * <br/>
 * @author Jerry
 *
 */
public class BattleMap {
	protected String Map[][];
	final static String BasicEmoji = "🕸";
	BattleMap(int height, int width) {
		Map = new String[height][width];
		for(int i = 0;i < height;i++)
			for(int j = 0;j < width;j++)
				Map[i][j] = BasicEmoji;
	}

	/**
	 * Paint the whole map as BasicEmoji.
	 *
	 */
	private void initMap() {
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[i].length; j++)
				Map[i][j] = BasicEmoji;
		}
	}
	/**
	 * Draw all the map objects on the map.
	 * @param objects HashMap of all the MapObject.
	 */
	void DrawObjects(HashMap<MapObject, Location> objects) {
		initMap();
		for(MapObject key : objects.keySet()) {
			key.drawOnMap(Map,objects.get(key));
		}
	}

	/**
	 * Print the Map in console.
	 */
	void PrintMap() {
		for (int i = 0; i < Map.length; i++) {
			for (int j = 0; j < Map[i].length; j++)
				System.out.print(Map[i][j]);
			System.out.println();
		}
	}

}

