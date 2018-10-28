package com.jerry.mapObjects.heroes;

import com.jerry.ability.Bomb;
import com.jerry.ability.KameHameHa;
import org.json.JSONObject;

public class Bomber extends Hero {
    public Bomber(JSONObject jsonObject) {
        super(jsonObject);
        ability = new Bomb();
    }

    @Override
    protected void levelUp() {
        super.levelUp();
    }


}
