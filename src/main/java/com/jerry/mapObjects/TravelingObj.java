package com.jerry.mapObjects;

public class TravelingObj implements MapObject {
	public void drawOnMap(String [][] Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}
	String appearance;
	public TravelingObj() {
		appearance = "😱";
	}
	public TravelingObj(String appearance) {
	    this.appearance = appearance;
    }

}
