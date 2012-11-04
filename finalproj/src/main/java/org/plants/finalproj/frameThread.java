/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plants.finalproj;

/**
 *
 * @author Kevin
 */
public class frameThread extends Thread {
    int frame;
    int frameControl;
    int count;
    GridMap parent;
    
    public frameThread(GridMap par)
    {
        frame = 1;
        frameControl = -1;
        parent = par;
    }
    
    public void run() {
       while (true) {
        count++;   
        if (count >= 2) {
            parent.repaint();
        }
        if ((frame == 3) || (frame == 1)) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException ex) {
            }
            frameControl = -frameControl;
            }
            frame += frameControl;
            parent.repaint();
            try {
                Thread.sleep(80);
            } catch (InterruptedException ex) {}
      }
    }
        
    }
