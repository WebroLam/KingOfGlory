package com.jerry.mapObjects;
import java.util.HashMap;
/**
 * The location of an object in the map.
 */
public class Location {
	public int xLoc;
	public int yLoc;

	public Location(int x, int y) {
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
	boolean MoveRight(java.util.HashMap<com.jerry.mapObjects.MapObject, Location> locations) {
		for(com.jerry.mapObjects.Location loc : locations.values()) {
			if(xLoc + 1 == loc.xLoc && yLoc == loc.yLoc)
				return false;
		}
		xLoc++;
		return true;
	}
	boolean MoveLeft(java.util.HashMap<com.jerry.mapObjects.MapObject, Location> locations) {
		for(com.jerry.mapObjects.Location loc : locations.values()) {
			if(xLoc - 1 == loc.xLoc && yLoc == loc.yLoc) {
				return false;
			}
		}
		xLoc--;
		return true;
	}
	boolean MoveUp(java.util.HashMap<com.jerry.mapObjects.MapObject, Location> locations) {
		for(com.jerry.mapObjects.Location loc : locations.values()) {
			if(yLoc - 1 == loc.yLoc && xLoc == loc.xLoc) {
				return false;
			}
		}
		yLoc--;
		return true;
	}
	boolean MoveDown(java.util.HashMap<com.jerry.mapObjects.MapObject, Location> locations) {
		for(com.jerry.mapObjects.Location loc : locations.values()) {
			if(yLoc + 1 == loc.yLoc && xLoc == loc.xLoc) {
				return false;
			}
		}
		yLoc++;
		return true;
	}
	public boolean Move(int direction, java.util.HashMap<com.jerry.mapObjects.MapObject, Location> locations) {
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
