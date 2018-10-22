package com.jerry.glory;
import com.jerry.mapObjects.*;
public class MapBoundaries implements MapObject {

	String appearance;
	public MapBoundaries() {
		appearance = "🌳";
	}
	public MapBoundaries(String appearance) {
		this.appearance = appearance;
	}

	public void drawOnMap(String [][] Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}

}
