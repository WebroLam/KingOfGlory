package com.jerry.mapObjects.heroes;

import com.jerry.glory.GUI;
import org.json.JSONObject;

public class autoWarrior extends automatic {
    int attackBuff;

    public autoWarrior(JSONObject obj, GUI game) {
        super(obj,game);
    }

    @Override
    public void autoPerform() {
        for(Hero hero : game.heroes ) {
            if(hero.getTeam().equals(this.getTeam())) {
                continue;

            }
            else {
                if(game.mapObjectLocation.get(hero).distanceTo(game.mapObjectLocation.get(this)) < this.attackDistance) {
                    attackDamage *= 1.001;
                    attackDamage += 1;// Rampage!!!
                }
            }
        }
        super.autoPerform();
    }

}
