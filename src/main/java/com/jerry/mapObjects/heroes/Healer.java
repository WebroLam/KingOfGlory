package com.jerry.mapObjects.heroes;
import org.json.*;
public class Healer extends Hero {

    protected int healPerTime;
    protected double healRange;
    public Healer(JSONObject jsonObject) {
        super(jsonObject);
        healRange = attackSplit;
        healPerTime = attackDamage;
    }
    public double getHealRange() {
        return healRange;
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        healPerTime *= AbilityPowerIncreaseSped;
    }

    public void heals(Hero hero) {
        if(hero.currentHealth + healPerTime < hero.maxHealth) {
            hero.currentHealth += healPerTime;
        }
        else {
            hero.currentHealth = hero.maxHealth;
        }

        if (hero != this)
            gainXP(50); // Healing other hero gains XP.
    }


}
