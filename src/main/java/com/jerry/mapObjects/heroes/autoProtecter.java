package com.jerry.mapObjects.heroes;
import com.jerry.glory.GUI;
import com.jerry.mapObjects.Location;
import org.json.JSONObject;

/**
 * Holy Protector!!!
 */
public class autoProtecter extends automatic {
    public autoProtecter(JSONObject obj, GUI game) {
        super(obj,game);
    }

    /**
     * Protect teammate!
     */
    @Override
    public void autoPerform() {
        logger.debug(name + "Enter autoPerform");
        Hero teammate = null;
        for(Hero hero :game.heroes) {
            if(!hero.getTeam().equals(this.getTeam()) && game.mapObjectLocation.get(hero).distanceTo(game.mapObjectLocation.get(this)) < this.attackDistance ){
                this.attack(hero);
                logger.debug(name + " attacked " + hero.name);
                return;
            }
            if(teammate == null || teammate.attackDamage / teammate.getCurrentHealth() > hero.attackDamage / hero.getCurrentHealth()) {
                if(hero.getTeam().equals(this.getTeam())) {
                    teammate =  hero; // Find the lowest health teammate, go protect him quq.
                }
            }

        }
        Location thisLoc = game.mapObjectLocation.get(this);
        Location oppLoc = game.mapObjectLocation.get(teammate);
        if(randomTravelCount <= 0) {
            if(!this.moveTo(oppLoc))
                randomTravelCount = 100;
        }
        else {
            randomMove();
        }


    }

    public int beAttack(int value) {
        currentHealth -= value;
        return currentHealth;
    }
}

