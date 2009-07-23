

import game.GameCanvas;
import javax.swing.*;

public class Runner {
    public static void main (String [] args) {
        
        JFrame frame = new JFrame("Asteroid Game");
        AsteroidGame ag = new AsteroidGame(100);
        ag.linkToFrame(frame);
        
        GameCanvas glc = new GameCanvas(ag);
        frame.add(glc);
        
        frame.setResizable(false);
        frame.setSize(1024, 768);
        frame.setVisible(true);
        
        // need this so that you don't have to click on the window to gain focus ;)
        glc.requestFocusInWindow();
    }
    
    
}