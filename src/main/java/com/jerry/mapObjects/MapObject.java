package com.jerry.mapObjects;

import javax.swing.*;
import java.awt.*;

/**
 * An object on the map.
 * @author Jerry
 */
public interface MapObject {
	/**
	 * Draw the object on given map
	 * @param Map the map where to draw the object
	 * @param loc the object's location.
	 */
	void drawOnMap(String[][] Map, Location loc);
	static final String ImageFilePath = "src/main/resources/images/";
	void draw(Graphics g,Location loc);

}