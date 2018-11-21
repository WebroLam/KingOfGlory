package com.jerry.mapObjects;

import javax.swing.*;
import java.awt.*;

/**
 * A temporary object on the map that can travel to another location.
 * @author Jerry
 */
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
    // TODO: implemnt this
	public void draw(Graphics g,Location loc) {

	}

}
