package com.jerry.mapObjects.heroes;

import com.jerry.glory.GUI;
import com.jerry.mapObjects.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


/**
 * @author jerry
 */
public class automatic extends Hero {
    protected static final Logger logger = LogManager.getLogger();
    int randomTravelCount;
    public automatic(JSONObject obj,GUI game) {

        super(obj,game);
        logger.debug("Automatic generated. name = " + this.name);
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        autoPerform();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    /**
     * Auto perform operation.
     *
     */
    public void autoPerform() {
        logger.debug(name + "Entered autoPerform.");
        game.text.append("Auto perform\n");
        Hero opposite = this;
        for(Hero hero : game.heroes) {
            if(hero.getTeam().equals(this.getTeam())){
                continue;
            }

            if(game.mapObjectLocation.get(hero).distanceTo(game.mapObjectLocation.get(this)) < this.attackDistance ){
                this.attack(hero);
                logger.debug(name + " attacked " + hero.name);
                return;
            }
            if(!hero.getTeam().equals(this.getTeam())) {
                opposite = hero;
            }
        }
        Location thisLoc = game.mapObjectLocation.get(this);
        Location oppLoc = game.mapObjectLocation.get(opposite);
        if(randomTravelCount <= 0) {
            if(!this.moveTo(oppLoc))
                randomTravelCount = 100;
        }
        else {
            game.text.append("Moving randomly\n");
            randomTravelCount--;
            char dirs[] = {
                    'w',
                    'a',
                    's',
                    'd'
            };
            thisLoc.Move(dirs[(int)Math.random()*4],game.mapObjectLocation);
        }

    }
}
