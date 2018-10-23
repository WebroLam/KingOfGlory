package com.jerry.ability;
import com.jerry.glory.*;
import com.jerry.mapObjects.*;
import org.json.*;
public interface Ability {
	int coolDown = 0;
	double distance = 0;
	String appearance = "❌";
	public int attack(Location loc);

}
