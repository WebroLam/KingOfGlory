package com.jerry.mapObjects;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.math.*;
/**
 * The location of an object in the map.
 * @author Jerry
 */
public class Location {
	public int xLoc;
	public int yLoc;
	boolean wenup = false;

	public Location(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	public Location(@NotNull Location rhs) {
		xLoc = rhs.xLoc;
		yLoc = rhs.yLoc;
	}
	private void MoveRight() {
		xLoc++;
	}
	private void MoveLeft() {
		xLoc--;
	}
	private void MoveUp() {
		yLoc--;
	}
	private void MoveDown() {
		yLoc++;
	}
	private boolean MoveRight(@NotNull HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(xLoc + 1 == loc.xLoc && yLoc == loc.yLoc)
				return false;
		}
		xLoc++;
		return true;
	}
	private boolean MoveLeft(HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(xLoc - 1 == loc.xLoc && yLoc == loc.yLoc) {
				return false;
			}
		}
		xLoc--;
		return true;
	}
	private boolean MoveUp(@NotNull HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(yLoc - 1 == loc.yLoc && xLoc == loc.xLoc) {
				return false;
			}
		}
		yLoc--;
		return true;
	}
	private boolean MoveDown(@NotNull HashMap<MapObject, Location> locations) {
		for(Location loc : locations.values()) {
			if(yLoc + 1 == loc.yLoc && xLoc == loc.xLoc) {
				return false;
			}
		}
		yLoc++;
		return true;
	}

	/**
	 * Move a certain character
	 * @param direction The moving direction, 1 for upward, 2 for left
	 * @param locations The whole objects of the game.
	 * @return Indicates whether moving is successful.
	 */
	public boolean Move(char direction, HashMap<MapObject, Location> locations) {
		switch(direction) {
			case 'w':
				if (! MoveUp(locations) )
					return false;
				break;
			case 'd':
				if( ! MoveRight(locations))
					return false;
				break;
			case 's':
				if( !MoveDown(locations))
					return false;
				break;
			case 'a':
				if( !MoveLeft(locations))
					return false;
				break;
		}
		return true;
	}

	/**
	 * Calculate the distance to another point.
	 * @param rhs The other point
	 * @return The distance between two points.
	 */
	public double distanceTo(Location rhs) {
		return Math.sqrt((xLoc - rhs.xLoc) * (xLoc - rhs.xLoc) + (yLoc - rhs.yLoc) * (yLoc - rhs.yLoc));
	}

    /**
     * Tell if two locations are equal
     * @param rhs the location to compare to
     * @return if two locations are equal.
     */
	public boolean equals(Location rhs) {
		return xLoc == rhs.xLoc && yLoc == rhs.yLoc;
	}
    /**
     * Make current location travel to another, move one step each direction each time.
     * @param target to location to travel to.
     * @return if current location has reached target location.
     */
	public boolean TravelTo(Location target) {
		if(target.yLoc == yLoc && target.xLoc == xLoc) {
			return true;
		}
		if(wenup) {
			if(target.xLoc > xLoc) {
				xLoc++;
			}
			else if(target.xLoc < xLoc) {
				xLoc--;
			}
			wenup = false;
		}
		else {
			if(target.yLoc > yLoc) {
				yLoc++;
			}
		else if(target.yLoc < yLoc) {
				yLoc--;
			}
			wenup = true;
		}

		return false;
	}

}
