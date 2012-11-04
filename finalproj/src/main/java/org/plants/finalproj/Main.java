package org.plants.finalproj;

/**
 * @author Pranav                                                                                               no friends
 * @author KevinHasMoreFriendsthanPranavHan
 */
import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.awt.color.*;
import java.util.*;

public class Main {


    public static void main(String[] args) {
        
        
        
        final int ROWS = 7;
        final int COLS = 7;

        JFrame pants = new JFrame();
        pants.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pants.setAlwaysOnTop(true);
        pants.setVisible(true); 


        GridMap plants = new GridMap(ROWS, COLS, 2);
        pants.setSize(COLS * 40 + 16, ROWS * 40 + 38);
        pants.setContentPane(plants);
        pants.validate();
        while (true) {
        }


    }
}
