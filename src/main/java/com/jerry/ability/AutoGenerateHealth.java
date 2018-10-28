package com.jerry.ability;
import com.jerry.mapObjects.heroes.*;
import com.sun.tools.javadoc.Start;

public class AutoGenerateHealth implements PassiveAbility {
    int HealPerSec;
    Thread AbilityThread;
    Hero AbilityOwner;
    public AutoGenerateHealth(Hero abilityOwner) {
        HealPerSec = abilityOwner.getAbilityPower() / 2;
        AbilityOwner = abilityOwner;
    }
    public void StartAbility() {
        AbilityThread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000) ; // Every 1 second
                    } catch (InterruptedException e) {

                    }
                    AbilityOwner.restoreHealth(HealPerSec);

                }
            }
        });

        AbilityThread.start();
    }
}
