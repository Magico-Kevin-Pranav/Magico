package org.plants.finalproj;

/**
 *
 * @author Kevin
 */
public class Location {
    //x and y coordinates are based on tiles, not pixel points or shit like that

    int x;
    int y;
    
    //variable may be useless
    boolean isOccupied;

    public Location() {
    }

    public Location(int a, int b) {
        x = a;
        y = b;
        isOccupied = false;
    }

    public Unit getUnit() {
        int i;
        for (Unit a : GridMap.allUnits) {
            if (a.loc.equals(this)) {
                return a;
            }
        }

        return null;

    }
    
    public boolean exists() {
        if ( x < 0 || x > GridMap.cols-1 || y < 0 || y > GridMap.rows-1)
        {
            return false;
        }
        return true;
    }

    public boolean equals(Object l) {
        Location loc = (Location) l;
        if ((loc.x == this.x) && (loc.y == this.y)) {
            return true;
        }
        return false;
    }

    

    public String toString() {
        return x + " " + y;
    }
}
