package com.jerry.mapObjects;

public class Bullet implements MapObject {
	public void drawOnMap(String [][] Map, Location loc) {
		Map[loc.yLoc][loc.xLoc] = appearance;
	}
	String appearance;
	public Bullet() {
		appearance = "😱";
	}
}
