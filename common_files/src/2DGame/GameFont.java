package GameEngine;

import java.awt.*;

/**
 * Holds the information required for the GameDrawer to draw text to the screen. Load using ResourceLoader.loadFont(). You only need to load the GameFont once for each font.
 * 
 * @author Richard Baxter
 *
 */
public class GameFont {
    Font font;
    int index;
    
    GameFont(Font f, int i) {
        font = f;
        index = i;
    }
    
}