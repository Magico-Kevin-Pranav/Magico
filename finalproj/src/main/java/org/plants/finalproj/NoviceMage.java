/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plants.finalproj;

/**
 *
 * @author Kevin
 */
public class NoviceMage extends Mage {
    
    //is pl player?
    //yes
    public NoviceMage(Location loc, int pl) {
        super(loc, pl);
        movement = 5;
        type = "NoviceMage";
        level = 0;
        randomizeStats();
        resetGrowths();
        

        
        
        hp = maxhp;
        mp = maxmp;
    }
    
    public void randomizeStats() {
        att = getRandomInteger(3, 5);
        spd = getRandomInteger(2, 4);
        def = getRandomInteger(2, 4);
        eva = getRandomInteger(0, 1);
        luck = getRandomInteger(0, 1);
        maxhp = getRandomInteger(25, 35);
        maxmp = getRandomInteger(8, 12);
        hprecov = getRandomInteger(1, 2);
        mprecov = getRandomInteger(1, 2);
        
        //ensures random stats are not below average TOO much
        if ((att + spd + def + 2*eva + 2*luck + (maxhp-25)/5 + (maxmp-8)/2 + hprecov + mprecov) < 17) {
        randomizeStats();
        }
    }
    
    public void resetGrowths() {
        attGrowth = 40;
        spdGrowth = 40;
        defGrowth = 40;
        luckGrowth = 20;
        evaGrowth = 20;
        maxhpGrowth = 60;
        maxmpGrowth = 60;
        hprecovGrowth = 20;
        mprecovGrowth = 20;
    }
}
