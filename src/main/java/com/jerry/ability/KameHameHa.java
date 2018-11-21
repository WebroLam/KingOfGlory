package com.jerry.ability;

import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.heroes.Hero;

import javax.swing.*;
import java.awt.*;

/**
 * KameHameHa :龟太气功。
 * @author Jerry
 */
public class KameHameHa implements Ability{
    public KameHameHa() {
        coolDownTime = 8;
        MPCost = 10;
        inCoolDown = false;
        range = 1;
    }
    public double getRange() {
        return range;
    }
    double range;
    int coolDownTime;
    int MPCost;
    boolean inCoolDown;
    String appearance = "💦";
    public void drawOnMap(String [][] Map, Location loc) {
        Map[loc.yLoc][loc.xLoc] = appearance;
    }
    public boolean cast(Hero target, int AbilityPower) {
        if(inCoolDown) {
            return false;
        }
        startCoolDown();
        return target.beAttacked(AbilityPower);
    }
    public int getMPCost() {
        return MPCost;
    }
    public boolean isInCoolDown() {
        return inCoolDown;
    }
    public void startCoolDown() {
        inCoolDown = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(coolDownTime * 1000);
                } catch (InterruptedException e) {

                }
            }
        }).start();
        inCoolDown = false;
    }

    // TODO: implement this
    public void draw(Graphics g,Location loc) {

    }
}

