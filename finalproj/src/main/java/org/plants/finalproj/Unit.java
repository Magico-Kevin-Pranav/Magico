package org.plants.finalproj;

/**
 *
 * @author Kevin
 */
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.math.*;
import java.util.Random;

public abstract class Unit {

    
    String name;
    Location loc;
    //refers to the unit's "type" e.g. novicemage, firemage, attmonster, etc.
    String type;
    //can the player move the unit
    boolean isActive;
    //is the unit currently selected
    boolean isSelected;
    //the affiliation of the unit.  0 for monsters
   
    ArrayList<Location> rangeOfMovement = new ArrayList<Location>();
    
    int level;
    
    int player;
    int movement;
    int att;
    int spd;
    int def;
    int eva;
    int luck;
    int maxhp;
    int maxmp;
    int hprecov;
    int mprecov;
    //hp and mp are "current hp".  maxhp and maxmp are the actual stats that increase
    int hp;
    int mp;

    public Unit() {
    }

    public Unit(Location l, int pl) {
        loc = l;
        player = pl;
        name = "plants";
        
    }

    public int getPlayer(){
    
       return player;
    }
    public Location getLoc() {
        return loc;
    }

    public void select() {
        isSelected = !(isSelected);
    }
    
    // does same as above, but its easier to read the code later ;)
    public void unselect() {
        isSelected = !(isSelected);
    }
    
    public void attack(Unit a) {
        System.out.println("Dmg: " + getDmg(a) + "\nCrit %: " + getCrit(a) + "\nHit %: " + getAcc(a));
        
        
        int dmg = getDmg(a);
        if (getRandomInteger(1,100)<= getAcc(a)) {
            if (getRandomInteger(1,100)<=getCrit(a)) {
                System.out.println("CRIT HIT!");
                dmg *= 3;
            } 
 
            System.out.println(dmg + " dealt to " + a.player);
            a.damage(dmg);
            if (a.hp <= 0) {
                a.die(this);
            }
        }
        else System.out.println("MISS");
    }
    
    //kills unit. attacker is used in overridden version
    public void die(Unit attacker) {
            GridMap.allUnits.remove(this);
            this.loc.isOccupied = false;
    }
    
    public void damage(int dam) {
        hp -= dam;    
    }
    
    public int getDmg(Unit a) {
        int dmg = att-a.def;
        if (dmg >= 0)
        return dmg;
        else return 0;
    }
    
    public int getCrit(Unit a) {
        int crit = (luck - a.luck) * 5 - a.eva;
        if (crit <= 0) crit=0;
        if (crit >= 100) crit = 100;
        return crit;
    }
    
    public int getAcc(Unit a) {
        int levelBoost = 5 * (level - a.level);
        if (levelBoost < 0) levelBoost = 0;
        int acc = 100 + levelBoost + 8 * (luck - a.eva);
        //shiz here
        if (acc >= 100) acc = 100;
        if (acc <= 0) acc = 0;
        return acc;
    }

    public boolean canAttackTwice(Unit a) {
        return (spd - a.spd) >= 5;
    }
    
    public int getRandomInteger(int a, int b) {
        return ( a + (int) (Math.random() * (b-a+.9999)));
    }
    
    public void calculateMovements() {

        rangeOfMovement = new ArrayList<Location>();
        rangeOfMovement.add(this.loc);
        addMovements(this.loc,movement);
        
    }

    private void addMovements(Location qq, int radius) {
        
        //location not already in rangeOfMovement 
        if (rangeOfMovement.indexOf(qq) == -1) {
            rangeOfMovement.add(qq);

        }
        
        //stop if there is no more movement left
        if (radius == 0) {
            return;
        } else {

            //go through each adjacent square
            for (Location a : getAdjacentSquares(qq)) {
                //adjacent location not occupied or occupied by allied unit. Short-circuits so that getUnit() will never be null when called
                //also the tile has to actually be in the grid
                if ((!(a.isOccupied) || (a.getUnit().player == this.player)) && a.exists()) {
                    addMovements(a, radius - 1);
                }
            }
        }

    }

    public ArrayList<Location> getAdjacentSquares(Location b) {
        ArrayList<Location> adjacentSquares = new ArrayList<Location>();
        
        
        try {
        adjacentSquares.add(GridMap.allLocations.get(b.y+1).get(b.x));
        } catch(Exception e) {            
        }
        try {
        adjacentSquares.add(GridMap.allLocations.get(b.y-1).get(b.x));
        }
        catch (Exception e) {}
        try {
        adjacentSquares.add(GridMap.allLocations.get(b.y).get(b.x+1));
        } catch(Exception e) {}
        try {
        adjacentSquares.add(GridMap.allLocations.get(b.y).get(b.x-1));
        } catch(Exception e) {}
        return adjacentSquares;
    }

    /*private boolean isTheLocationGood(Location a) {
        if (GridMap.allOccupiedLocations.indexOf(a) != -1) {
            if (this.player == a.getUnit().player) {
                return true;
            } else {
                return false;
            }
        } 
        else {
            return true;
        }
    }

    public void getRidOfRedundancies(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            Object a = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                boolean theyAreTheSame = a.equals(list.get(j));
                if (theyAreTheSame) {
                    list.remove(j);
                }
            }
        }
    }
    */
    public String toString(){
        String info = type + "\nPlayer: " + player + "\nLevel: " + level + 
                "\nMaxHP: " + maxhp + "\nMaxMP: " + maxmp + "\nAtt: " + att + 
                "\nDef: " + def + "\nSpd: " + spd + "\nEva: " + eva + 
                "\nLuk: " + luck + "\nHP: " + hp + "\nMP: " + mp;
        
        return info;
    }
}
