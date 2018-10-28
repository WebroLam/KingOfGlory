package com.jerry.mapObjects;

public interface MapObject {
	/**
	 * Draw the object on given map
	 * @param Map the map where to draw the object
	 * @param loc the object's location.
	 */
	void drawOnMap(String[][] Map, Location loc);

}