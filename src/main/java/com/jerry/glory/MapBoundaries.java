package com.jerry.glory;

public class MapBoundaries implements com.jerry.mapObjects.MapObject {

	String appearance;
	MapBoundaries() {
		appearance = "🌳";
	}

	public void drawOnMap(String [][] Map, com.jerry.mapObjects.Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}

}
