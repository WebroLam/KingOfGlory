package com.jerry.ability;
import com.jerry.glory.*;
import com.jerry.mapObjects.*;
import org.json.*;
import com.jerry.mapObjects.heroes.*;
public interface Ability extends MapObject {

    boolean cast(Hero target, int AbilityPower);
	void startCoolDown();
	int getMPCost();
	boolean isInCoolDown();
	double getRange();

}
