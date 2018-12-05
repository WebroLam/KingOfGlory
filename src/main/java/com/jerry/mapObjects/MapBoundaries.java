package com.jerry.mapObjects;
import com.jerry.mapObjects.*;
import com.sun.javaws.util.JfxHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MapBoundaries implements MapObject {
	String appearance;
	Image image;
	public MapBoundaries() {
		appearance = "🌳";
		File imageFile;
		try {
			imageFile = new File(ImageFilePath + "Tree.png");
			image = ImageIO.read(imageFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	public MapBoundaries(String appearance) {
		this.appearance = appearance;
	}
	public void drawOnMap(String [][] Map, Location loc) {
        try {
            Map[loc.yLoc][loc.xLoc] = appearance;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array out of bounds! apperance = " + appearance + "index = " + loc.xLoc + " " + loc.yLoc);
            System.exit(-1);
        }
    }

	//TODO: implement this
	public void draw(Graphics g,Location loc) {
	    g.drawImage(image,loc.xLoc,loc.yLoc,null);
	}
	public void draw(JFrame frame) {


	}
}
