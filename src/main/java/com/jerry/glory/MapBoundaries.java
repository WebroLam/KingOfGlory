package com.jerry.glory;
import com.jerry.mapObjects.*;
public class MapBoundaries implements MapObject {

	String appearance;
	MapBoundaries() {
		appearance = "🌳";
	}

	public void drawOnMap(String [][] Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}

}
