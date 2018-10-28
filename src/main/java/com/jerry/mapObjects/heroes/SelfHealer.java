package com.jerry.mapObjects.heroes;

import com.jerry.ability.AutoGenerateHealth;
import com.jerry.ability.PassiveAbility;
import org.json.*;
public class SelfHealer extends Hero {
    PassiveAbility passiveAbility;
    public SelfHealer(JSONObject jsonObject) {
        super(jsonObject);
        passiveAbility = new AutoGenerateHealth(this);
        passiveAbility.StartAbility();
    }

}
