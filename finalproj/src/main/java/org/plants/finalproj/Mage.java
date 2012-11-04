/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plants.finalproj;
import java.util.*;

/**
 *
 * @author Pranav Shenoy
 */
public abstract class Mage extends Unit {

    int currentExp;
    int maxhpGrowth;
    int maxmpGrowth;
    int attGrowth;
    int spdGrowth;
    int defGrowth;
    int luckGrowth;
    int evaGrowth;
    int hprecovGrowth;
    int mprecovGrowth;
    Integer[] statGrowths ;

    public Mage() {
        //default constructor shits
        // should never use this
    }

    public Mage(Location l, int pl) {
        super(l, pl);
        currentExp = 0;
        isActive = true;
    }
    
    //overrides checkDeath in Unit to account for exp and leveling
    public void die(Unit attacker) {
        Mage winner = (Mage) attacker;
        if (this.type.equals("NoviceMage")) {
            winner.addExp(100);
        }
        //implementation for exp gained from killing a monster
        super.die(winner);
    }
    
    public void addExp(int gain) {
        currentExp += gain;
        if (currentExp >= 100) {
            levelUp();
            currentExp -= 100;
        }
    }
    
    public void levelUp() {
        
        level++;
            //System.out.println(statGrowths[i]);
        if (getRandomInteger(1, 100) <= attGrowth) {
            att++;
        }    
        if (getRandomInteger(1, 100) <= spdGrowth) {
            spd++;
        }
        if (getRandomInteger(1, 100) <= defGrowth) {
            def++;
        }
        if (getRandomInteger(1, 100) <= luckGrowth) {
            luck++;
        }
        if (getRandomInteger(1, 100) <= evaGrowth) {
            eva++;
        }
        if (getRandomInteger(1, 100) <= maxmpGrowth) {
            maxmp++;
        }
        if (getRandomInteger(1, 100) <= hprecovGrowth) {
            hprecov++;
        }
        if (getRandomInteger(1, 100) <= mprecovGrowth) {
            mprecov++;
        }
        //gain more for maxhp because hp is inflated in this game
        if (getRandomInteger(1, 100) <= maxhpGrowth) {
            maxhp++;
        }
        if (getRandomInteger(1, 100) <= maxhpGrowth) {
            maxhp++;
        }
        if (getRandomInteger(1, 100) <= maxhpGrowth) {
            maxhp++;
        }
        resetGrowths();
        
        //implementation for class changes go here
        
    }
    
    
    
    public abstract void resetGrowths();
        
    
}
