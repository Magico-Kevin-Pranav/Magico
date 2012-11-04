package org.plants.finalproj;

/**
 *
 * @author Kevin The Pro
 */
import java.util.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//NEW RULE: ALWAYS COMMENT FOR THE PURPOSE OF THINGS YOU ADD THAT SEEM SIGNIFICANT
//NEW RULE: LIST ANY MAJOR CHANGES IN CHANGELOG - THIS INCLUDES REMOVING METHODS, ADDING METHODS ETC. GIVE REASONS

public class GridMap extends JPanel implements MouseInputListener {

    public static int cols;
    public static int rows;
    
    int menu_X;
    int menu_Y;
    int arrowTracker;

    // to keep track of where the mouse is
    int mouse_X;
    int mouse_Y;
    
    //phase indicates what "phase" the game is on. i.e selecting a unit, moving a unit, menus, etc.
    // init
    // normal
    // selecting
    // action
    String phase;
    String phaseTracker;
    
    //1st player's turn first
    int turn = 1;
    
    //testing out thread shits
    frameThread t;
    //int frame;
    //int frameControl;
    //variable to keep track of selectedUnit
    
    
    int numPlayers;
    Unit selectedUnit = null;
    // WHAT IS THIS RANGEARRAY VARIABLE? 
    ArrayList<Location> rangeArray = null;
    String rangeImageStr = null;
    
    //ArrayList<Unit> p1Units = new ArrayList<Unit>();
    //ArrayList<Unit> p2Units = new ArrayList<Unit>();
    //ArrayList<Unit> mUnits = new ArrayList<Unit>();
    
    //counter of active units. When at 0, it becomes the next player's turn.
    int activeUnits;
    public static ArrayList<Unit> allUnits = new ArrayList<Unit>();
    public static ArrayList<ArrayList<Location>> allLocations = new ArrayList<ArrayList<Location>>();
    //public static ArrayList<Location> allOccupiedLocations = new ArrayList<Location>();

    
    
    //*************constructor methods**************

    public GridMap() {
        super();
        System.out.println("You used the retarded version and should feel bad");
    }

    public GridMap(int a, int b, int num) {
        super();
        
        phase = "init";
        phaseTracker = "init";
        addMouseListener(this);
        addMouseMotionListener(this);

        //frame = 1;
        //frameControl = -1;
        rows = a;
        cols = b;
        
        arrowTracker = -1;
        

        numPlayers = num;
        
        t = new frameThread(this);
        t.start();
        
        // it works!
        for (int i = 0; i < a; i++) {
            ArrayList<Location> X = new ArrayList<Location>();
            for (int j = 0; j < b; j++) {
                X.add(new Location(j, i));
            }
            allLocations.add(X);
        }

        spawnPlayers(allLocations.get(0).get(0), 1);
        spawnPlayers(allLocations.get(rows - 3).get(cols - 3), 2);
        activeUnits = 9;
        
    }

    
    
    //************game mechanic methods************
    
    
    /*
     * 
     * deprecating because allUnits variable is better
     * 
    public ArrayList<Unit> getAllUnits() {
    ArrayList<Unit> temp = new ArrayList<Unit>();
    temp.addAll(P1Units);
    temp.addAll(P2Units);
    temp.addAll(MUnits);
    return temp;
    }
     */
    
    /*
    public ArrayList<Location> getUnitLocations(ArrayList<Unit> arr) {
        ArrayList<Location> locList = new ArrayList<Location>();
        int i = 0;
        for (i = 0; i < arr.size(); i++) {
            locList.add(arr.get(i).getLoc());
        }
        return locList;
    }*/

    //add 9 units to the array list units with locations in a 3 x 3 area that starts with location loc

    /*public void run() {
        while (true) {
            if (activeUnits == 0) {
                nextTurn();
            }
        }
    }*/
    
    public void nextTurn()
    {
        //rotate turn to next number or 1 if it's the last player's turn
        if (turn != numPlayers) {
            turn++;
        }
        else {
            turn = 1;
        }
        //find all units of the affiliation of the correct turn in allUnits and set to activeUnits
        activeUnits = 0;
        for (Unit a: allUnits) {
            if (a.player == turn) {
                activeUnits++;
                a.isActive = true;
            }
        }
        System.out.println("NEXT TURN " + turn);
    }
    
    private void spawnPlayers(Location loc, int pl) {
        int x, y;
        for (x = loc.x; x <= (loc.x + 2); x++) {
            for (y = loc.y; y <= loc.y + 2; y++) {
              //  Location locus = new Location(x,y);
                Location locus = allLocations.get(y).get(x);
                locus.isOccupied = true;
                Mage mage = new NoviceMage(locus, pl);
                //units.add(mage);
                spawnUnit(mage);
                
                
            }
        }
        
    }
    
    //This what the displayArray() method at the end is for... 
    /* private void spawnArray()
    {
        System.out.println(allUnits); 
    }*/
    
    private void spawnUnit(Unit a)
    {
        allUnits.add(a);
        a.loc.isOccupied = true;
        
    }
    
    public void removeUnit(Unit a)
    {
        allUnits.remove(a);
        a.loc.isOccupied = false;
    }
    
    public void moveUnit(Unit a, Location destination)
    {
        //replace the first instance of the current location with the destination location
        a.loc.isOccupied = false;
        a.loc = destination;
        a.loc.isOccupied = true;
        invalidate();
    }
    
    public void makeInactive(Unit a) 
    {
        a.isActive = false;
        selectedUnit = null;
        activeUnits--;
        phase = "normal";
        invalidate();
        if (activeUnits == 0) {
            nextTurn();
        }
    }

    
        // checks if the attacks are possible 
    // yes, returns true
    // the purpose is to choose the right menu
    public boolean areAttacksPossible(Unit a) {
        boolean canAttack = false;
        for (Location q : a.getAdjacentSquares(a.loc)) {
            if (q.isOccupied) {
                if (q.getUnit().player != turn) {
                    canAttack = true;
                }
            }
        }
        return canAttack;
    }


   
    
    //**************input methods****************
    
    public void mouseMoved(MouseEvent e) {
        //System.out.println(phase);
        mouse_X = e.getX();
        mouse_Y = e.getY();
        t.count = 0;
        
        if ( phase.equals("action") && new Rectangle(menu_X+2,menu_Y+4,110,102).contains(e.getX(),e.getY()))  {
            arrowTracker = 1 + ((e.getY() - menu_Y -4)/34);
            repaint();
        }
        else {arrowTracker = -1;}

    }
    
    public void mouseDragged(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        
        
        //each tile is roughly 40 pixels.  Mouseclick X coordinate / 40 gives an integer equivelant representative of the tile
        //boolean tambien = true;
        Location loc = allLocations.get(e.getY() / 40).get(e.getX() / 40); 
        //if (isOwnPiece(loc, loc.getUnit().player)){
        //Unit thing = loc.getUnit();
        // System.out.println("This is a" +thing);
        //Mage thing2 = new Mage(new Location(1,2),1);
        //System.out.println(thing + " unit");
        //System.out.println(thing2.getPlayer() + " player");
        
        //int thing1 = thing2.getPlayer();
        //Location loc = new Location(e.getX() / 40 + 1, e.getY() / 40 + 1)
        //int index = allOccupiedLocations.indexOf(loc);
        //boolean tf = loc.getUnit().player == turnNumber;
        
        if (SwingUtilities.isMiddleMouseButton(e)) {
            nextTurn();
        }
        
        if (SwingUtilities.isRightMouseButton(e)) {
            System.out.println(loc.getUnit());
            // I actually have no idea what is supposed to happen herez
            if (phase.equals("selecting")) {
                phase = "normal";
                invalidate();
            } else if (phase.equals("attacking")) {
                phase = "selecting";
                invalidate();
            }
            repaint();
        } 
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (phase.equals("attacking") && rangeArray.indexOf(loc) != -1) {
                System.out.println(" ");
                selectedUnit.attack(loc.getUnit());
                if (loc.getUnit() != null && selectedUnit != null) {
                    loc.getUnit().attack(selectedUnit);
                }
                if (selectedUnit != null && loc.getUnit() != null && selectedUnit.canAttackTwice(loc.getUnit()))
                {
                    selectedUnit.attack(loc.getUnit());
                }
                if (loc.getUnit() != null && selectedUnit != null && loc.getUnit().canAttackTwice(selectedUnit)) {
                    loc.getUnit().attack(selectedUnit);
                }

                if (selectedUnit != null) makeInactive(selectedUnit);
            }

            if (phase.equals("action")) {
                if (arrowTracker == 1) {
                    rangeArray = new ArrayList<Location>();
                    for (Location l : selectedUnit.getAdjacentSquares(selectedUnit.loc)) {
                        if ((l.isOccupied) && (l.getUnit().player != turn)) {
                            rangeArray.add(l);
                        }
                    }
                    if (!rangeArray.isEmpty()) {
                        phase = "attacking";
                        rangeImageStr = "RangeTile.gif";
                        invalidate();
                    }
                }
                if (arrowTracker == 2) {
                    //trying to think of better way to do this
                    phase = "magic_choice";
                }
                if (arrowTracker == 3) {
                    makeInactive(selectedUnit);
                }
            }

            if (phase.equals("selecting")) {
                if ((loc.isOccupied == false || loc.getUnit() == selectedUnit) && selectedUnit.rangeOfMovement.indexOf(loc) != -1) {
                    moveUnit(selectedUnit, loc);
                    phase = "action";
                    if (selectedUnit.loc.x >= cols / 2) {
                        menu_X = 10;
                    } else {
                        menu_X = cols * 40 - 124;
                    }
                    menu_Y = rows * 20 - 55;
                    invalidate();
                }
            }


            if (phase.equals("normal")) {
                if (loc.isOccupied && turn == loc.getUnit().getPlayer() && loc.getUnit().isActive) {
                    phase = "selecting";
                    selectedUnit = loc.getUnit();
                    selectedUnit.select();
                    selectedUnit.calculateMovements();
                    rangeArray = selectedUnit.rangeOfMovement;
                    rangeImageStr = "MoveTile.gif";
                    invalidate();
                } else {
                    //pull up menu to end turn, etc.
                }
            }
            repaint();

        }
    }
    
    
    //***********graphics methods************

    //method that reads images from a file so that you can draw it
    public BufferedImage readImage(String imStr) {
        BufferedImage temp = null;

        try {
            temp = ImageIO.read(new File("src\\main\\resources\\Sprites\\" + imStr));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return temp;
    }

    //overrides previous invalidate method so that phase becomes init whenever window is resized,etc.
    //makes paint repaint tiles
    public void invalidate() {
        super.invalidate();
        phaseTracker = phase;
        if (phaseTracker.equals("init")) {
            phaseTracker = "normal";
        }
        phase = "init";
    }

    
    //paint method.  Draws shit.  Called every time the window resizes, minimized, etc.  Draws background
    public void paint(Graphics g) {

        if (phase.equals("init")) {
            /*
            if (allLocations.get(mouse_Y / 40).get(mouse_X / 40).isOccupied) {
                g.drawImage(readImage("stats_menu.gif"), mouse_X / 40, mouse_Y / 40, this);
            }
            */
            update(g);
            phase = phaseTracker;
        }
       
        if (t.count == 0) {
            update(g);
        }
        
        for (Unit a : allUnits) {
            paintTile(g, a, a.type + "\\Stand", a.loc.x, a.loc.y);
        }
        
        if (phase.equals("selecting") || phase.equals("attacking")) {

            for (Location loc : rangeArray) {
                g.drawImage(readImage(rangeImageStr), (loc.x) * 40, (loc.y) * 40, this);
            }
            paintTile(g, selectedUnit, selectedUnit.type + "\\Selected", selectedUnit.loc.x, selectedUnit.loc.y);

        }
        
        if (phase.equals("normal")||phase.equals("attacking")) {
            if (t.count >= 2) {
            
            Location l = allLocations.get(mouse_Y / 40).get(mouse_X / 40);
            if (l.isOccupied) {
                if (l.y<=1) {
                    if (l.x >= 2 ) {
                    g.drawImage(readImage("InfoBox2.gif"), l.x * 40 + 20 - 96, l.y * 40 - 44 + 70, this);
                    paintString(g, l.getUnit().name, l.x * 40 + 68 - 96, l.y * 40 - 40 + 77);
                    paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68 - 96, l.y * 40 - 27 +77);
                    paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68 - 96, l.y * 40 - 15 + 77);
                    }
                    else {
                        g.drawImage(readImage("InfoBox3.gif"), l.x * 40 + 20, l.y * 40 - 44 + 70, this);
                        paintString(g, l.getUnit().name, l.x * 40 + 68, l.y * 40 - 40 + 77);
                        paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68, l.y * 40 - 27 + 77);
                        paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68, l.y * 40 - 15 + 77);
                    }
                }                
                else if (l.y>=rows-2) {
                    if (l.x <= cols-3) {
                        g.drawImage(readImage("InfoBox.gif"), l.x * 40 + 20, l.y * 40 - 44, this);
                        paintString(g, l.getUnit().name, l.x * 40 + 68, l.y * 40 - 40);
                        paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68, l.y * 40 - 27);
                        paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68, l.y * 40 - 15);
                    }
                    else {
                        g.drawImage(readImage("InfoBox4.gif"), l.x * 40 + 20 - 96, l.y * 40 - 44, this);
                        paintString(g, l.getUnit().name, l.x * 40 + 68 - 96, l.y * 40 - 40);
                        paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68 - 96, l.y * 40 - 27);
                        paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68 - 96, l.y * 40 - 15);
                    }
                }
                else if (l.x <= cols-3) {
                    g.drawImage(readImage("InfoBox.gif"), l.x * 40 + 20, l.y * 40 - 44, this);
                    paintString(g, l.getUnit().name, l.x * 40 + 68, l.y * 40 - 40);
                    paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68, l.y * 40 - 27);
                    paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68, l.y * 40 - 15);
                }
                else {
                    g.drawImage(readImage("InfoBox4.gif"), l.x * 40 + 20 - 96, l.y * 40 - 44, this);
                    paintString(g, l.getUnit().name, l.x * 40 + 68 - 96, l.y * 40 - 40);
                    paintString(g, "hp- " + l.getUnit().hp + "#" + l.getUnit().maxhp, l.x * 40 + 68 - 96, l.y * 40 - 27);
                    paintString(g, "mp- " + l.getUnit().mp + "#" + l.getUnit().maxmp, l.x * 40 + 68 - 96, l.y * 40 - 15);
                }
                
            }
            }
            
            
        }
        
        
        
        

        
        
        if (phase.equals("action")) {
            paintTile(g,selectedUnit, selectedUnit.type + "\\Selected", selectedUnit.loc.x, selectedUnit.loc.y);           
            if (areAttacksPossible(selectedUnit)) {
                g.drawImage(readImage("Menu.gif"), menu_X, menu_Y, this);
            } else {
                g.drawImage(readImage("MenuWithoutAttack.gif"), menu_X, menu_Y, this);
            }
            if (arrowTracker != -1) {
                g.drawImage(readImage("Arrow.gif"), menu_X + 8, menu_Y + 16 + (arrowTracker - 1) * 34, this);
            }

        }

    }

    //x and y refer to the MIDDLE of the string (easier for centering purposes)
    public void paintString(Graphics g, String output, int x, int y) {
        int i;
        int j =0;
        for (i=-output.length();i<output.length();i+=2) {
            if (output.charAt(j) == ' ') {
                j++;
                i+=2;
            }
            g.drawImage(readImage("\\Texts\\" + output.charAt(j) + ".gif"),x+(i*3),y,this);
            j++;
        }
    }


    //method to paint a particular tile
    //a gives the player icon, fileDir is where the unit's sprites are, x and y are tile based location values
    public void paintTile(Graphics g, Unit a, String fileDir, int x, int y) {
        
        g.drawImage(readImage("Tile.png"), x * 40, y * 40, this);
        g.drawImage(readImage(fileDir + t.frame + ".gif"), x * 40, y * 40, this);
        g.drawImage(readImage(a.player + ".gif"), x * 40 + 1, y * 40 + 1, this);
    }

    //overrides update method. Draws shit just like paint method. Dont know why I dont just put this in paint
    @Override
    public void update(Graphics g) {
        int i, j;
        for (i = 0; i <= cols-1; i++) {
            for (j = 0; j <= rows-1; j++) {
                g.drawImage(readImage("Tile.png"), i * 40, j * 40, this);
            }
        }

    }
    
    
    
    
    //***********debugging methods************
    
    //displays an array. Make sure the Object has a toString method. 
    //Currently the Unit and Location have toString() methods
    public void displayArray(ArrayList list){
        System.out.println(list.size());
        for (Object a:list)
        {
            System.out.println(a);
        }
    }
    
}
