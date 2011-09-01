package GameEngine;

import java.util.Stack;
import javax.media.opengl.*;

import com.sun.opengl.util.texture.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
import java.nio.*;

/**
* 
* This class holds the basic components for running a game. Classes should extend this class and over-ride the 
* initStep(), logicStep() and renderStep() functions. This class should be used in conjunction with the GameCanvas 
* class and Java's swing API.
* <p><p>
* The first variable to be set is the game-frames-per-second of the game. Please note this is NOT how many times per second your game will be rendered 
* to screen (that is just frames-per-second). This is how many times per second your game logic will update. A higher GFPS means you game will
* require more processing power but allows the renderer to display frames at a higher rate, thus making the game look smooth. A low GFPS (say 30 GFPS)
* means the game only updates every 1/30th of a second and thus the renderer can only update the screen each 1/30th per second, resulting in a not-as-smooth looking
* game.
* <p><p>
* This class (used with the GameCanvas class) will call the logicStep() function every couple of milliseconds (depending on the frame-rate). 
* This will form the main execution of your game's logic.
* In order to keep the frame rate stable the renderStep() function is not guaranteed to run once every frame. For instance, it may run only once every
* two or three frames depending on how fast your computer is, and how high your game-frames-per-seconds is set. For this reason do not put anything 
* related to game logic in this method. This methods should be purely for calling the draw() functions. Calculations should all go in the logicStep() method.
* <p><p>
* The Game class also has the initStep() method (which must also be over-rided) which is the initialisation step.
* This function is used to load all the textures (stored as GameTextures) that your game will be requiring. You cannot load textures during the logicStep or the renderStep.
* It is suggested that creating of GameObjects goes in this function instead of the constructor as the constructor will not have access to GameTextures.
* <p><p>
* Notes:
* <p><p>
* Use endGame() to exit your game cleanly. Do not System.exit(0);.
* <p><p>
* Make sure to link you Game to the JFrame class using the linkToFrame(JFrame f) method. This will cause a clean exit of the Game when you close the JFrame.
* <p>
* E.g:<p>
* <code> JFrame frame = new JFrame("My Fantastic Game");</code><p>
* <code> FantasticGame fanGame = new FantasticGame(100);</code><p>
* <code> fanGame.linkToFrame(frame);</code><p>
* 
* @see GameCanvas
* @see GameObject
* @see GameTexture
* @author Richard Baxter
*
*/
public abstract class Game {
    
    
    //==============================================================================
    int game_frames_per_second;
    private boolean initialised;
    private GameTimer timer;
    
    GameRenderer.FontRenderer fr;
    GameInputInterface theGamesInputInterface = new GameInputInterface();
    
    boolean [] keyDownVec = new boolean [512];
    boolean [] mouseDownVec = new boolean [4];
    boolean mouseInWindow = false;
    boolean mouseDragged = false;
    boolean mouseMoved = false;
    Point mousePos = new Point(0,0);

    Dimension viewPortDimension = new Dimension(1,1);
    
    boolean [] keyTypedVec = new boolean [512];
    Stack<Integer> keyTypedStack = new Stack<Integer>();
    boolean [] mouseClickedVec = new boolean [4];
    Stack<Integer> mouseClickedStack = new Stack<Integer>();
    
    
    int scrollAmount = 0;
    int scrollType = 0;
    int unitsToScroll = 0;
    int wheelRotation = 0;
    //==============================================================================
    
    /**
    * Basic game constructor
    * 
    * @param GFPS The desired game-frames-per-second. Note this is not how many frames per second the screen will be rendered to but 
    * rather how many times per second your game logic will be updated
    */
    public Game (int GFPS) {
        System.out.println("abstract class Game constructor");
        game_frames_per_second = GFPS;
        initialised = false;
    }
    
    //==============================================================================
    
    void startGame(GLAutoDrawable glc) {
        timer = new GameTimer(this, glc);
    }
    
    /**
    * Call this function when you wish to exit the game. It is recommended that you call this and not System.exit(0) to end you game.
    * Use the linkToFrame(JFrame f) to ensure this method is called when the frame is closed
    */
    public void endGame() {
        timer.endGame();
    }
    
    //==============================================================================
    
    /**
    * This class is purely used to call the load() method for loading GameTextures and loadFont() for initilising fonts that will be used in the game.
            
    * Please note that some very large images might not load depending on your graphics hardware. For this reason it is suggested that you use an image editor to
    * breakup your large image into smaller ones and load those individually.
    */
    public class ResourceLoader {
        GameRenderer.FontRenderer fontR;
        private ResourceLoader (GameRenderer.FontRenderer f) {
            fontR = f;
        }
    
        /**
        * Initializes a font for rendering during the renderStep. In order to use a font during rendering you need to load up the font with this method 
        * and then use it with th draw method in GameDrawer
        */
        public GameFont loadFont(Font f) {
//             System.out.println(fontR == null);
            return fontR.addGameFont(f);
        }
        
        //==============================================================================
    
        // taken from http://forum.java.sun.com/thread.jspa?threadID=5197213&tstart=45
        /**
        * Texture loader utilizes JOGL's provided utilities to produce a texture.
        * This is the same is the load() function
        *
        * @param fileName relative filename from execution point
        * @return a GameTexture ready for use or null is loading failed
        */
        public GameTexture loadTexture(String fileName) {
            return load(fileName);
        }
        // taken from http://forum.java.sun.com/thread.jspa?threadID=5197213&tstart=45
        /**
        * Texture loader utilizes JOGL's provided utilities to produce a texture.
        *
        * @param fileName relative filename from execution point
        * @return a GameTexture ready for use or null is loading failed
        */
        public GameTexture load(String fileName) {
            GameTexture gt = null;
            try {
                gt = new GameTexture();
                TextureData textD = TextureIO.newTextureData(new File(fileName), true, null);
                gt.w = textD.getWidth();
                gt.h = textD.getHeight();
                gt.t = TextureIO.newTexture(textD);
//                 System.out.println("loaded "+fileName +" ("+gt.w+":"+gt.h+")");
                gt.bbuffer = ((ByteBuffer)textD.getBuffer()).asReadOnlyBuffer();
                
                gt.t.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                gt.t.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
    
            }
            catch(Exception e) {
                System.err.println(e.getMessage());
                System.err.println("Error loading texture " + fileName);
            }
            
            return gt;
        }
        
    }
    
    //==============================================================================
    
    final void initGame(GL gl, GameRenderer.FontRenderer fontR) {
        //System.out.println("Game.initGame() called");
        gl.glEnable(GL.GL_TEXTURE_2D);
        // gl.glEnable(GL.GL_TEXTURE_RECTANGLE_ARB);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        
        gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
    
        gl.glEnable(GL.GL_BLEND);
        
        initStep(new ResourceLoader(fontR));
        initialised = true;
        timer.start();
    }
    /**
    * This step is to load textures and it is recommended for initialising GameObjects. 
    * This will be called once before the game starts and never called again
    * 
    * @param loader A TextureLoader for loading GameTextures
    */
    public abstract void initStep(ResourceLoader loader);
    
    //==============================================================================
    
    /**
    * This class is your interface to the OpenGL rendering capabilities.
    * <p>
    * You may specify offsets which will offset anything you draw by that amount. This will effectively shift your entire game world by 
    * a certain amount. Useful for scrolling around your screen.
    * <p>
    * The draw method draws the specified shape using a list of points given.
    * The shape can be one of LINES, TRIANGLES, QUADS, POINTS
    * Multiple shapes of the same type can be drawn using one call of the draw method.
    * If insufficient points are provided the shape will not be drawn.
    * <p>
    * For example if you specify QUADS and provide 4 points. 1 QUAD will be drawn. (QUADS require 4 points to be specified)
    * <p>
    * If you specify QUADS and provide 12 points. 3 QUADS will be drawn. The first QUAD will use points 0-3, the second QUAD will use points 4-7, the third QUAD will use points 8-11
    * <p>
    * If you specify QUADS to be drawn and only give an array of 3 points the QUAD will not be drawn.
    * <p>
    * If you specify QUADS and provide 7 points the first quad will be drawn but since there are only 3 points left the second QUAD will not.
    * <p>
    * 
    * You may also use the draw method to draw a polygon or a line strip (a string of lines). 
    * Only one polygon or line strip will be drawn per call of the draw method
    * 
    * <p>
    * For those who know OpenGL these 'shapes' correspond to the types of things that can be rendered in OpenGL (i.e. QUADS corresponds to GL_QUADS).
    * I can add support for drawing of GL_LINE_LOOP, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN and GL_QUAD_STRIP if there is a demand for it.
    *
    * <p>
    * <p>
    * The drawing of fonts actually happens after the renderStep method has finished. They are buffered into an array of Stack objects and drawn all 
    * at once afterwards. This is partly is because of how JOGL's inbuilt Font rendering is set up, and partly because of the design of the API. 
    * <p>
    * All the strings of text are grouped with their respective fonts and then rendered font by font in the order the GameFont object were 
    * created in the initStep method. This is only an issue if your text is goin to be transparent in anyway, otherwise you can use it as normal.
    */
    public class GameDrawer {
        
        private float offx, offy;
        private float [] lastSetColour = {0.0f, 0.0f, 0.0f, 0.0f};
    
        GL gl;
        
        private GameDrawer (GL g) { gl = g;}
    
        /**
            * Sets the values to which this renderer will offset anything it draws.
            * @param x The x-offset
            * @param y The y-offset
            */
        public void setWorldOffset(float x, float y){
        offx = x; offy = y;
        }
        
        /**
            * Sets the colour that the renderer will draw when drawing shapes or objects (unless specified otherwise in the function). 
            * All values should be between 0.0 and 1.0.
            * @param c A float [4] specifying the red, green, blue, alpha channels respectively.
            */
        public void setColour(float [] c) {
        
            for (int i = 0 ; i < 4 ; i++)
                lastSetColour[i] = c[i];
            gl.glColor4f(c[0], c[1], c[2], c[3]);
        }
    
        /**
        * Sets the colour that the renderer will draw when drawing shapes or objects (unless specified otherwise in the function)
        * All values should be between 0.0 and 1.0.
        * @param r Value of the red channel (between 0.0 and 1.0)
        * @param g Value of the green channel (between 0.0 and 1.0)
        * @param b Value of the blue channel (between 0.0 and 1.0)
        * @param a Value of the alpha channel (between 0.0 and 1.0)
        */
        public void setColour(float r, float g, float b, float a) {
            lastSetColour[0] = r;
            lastSetColour[1] = g;
            lastSetColour[2] = b;
            lastSetColour[3] = a;
            gl.glColor4f(r, g, b, a);
        }
        
        /**
        * Draws a GameObject at a specified depth with whatever colour is currently set. The game object will be drawn at it's internally specified position, center and with it's
        * current active texture.
        * @param o The GameObject to be drawn
        * @param depth The depth at which to draw the GameObject, a lower number means it will be behind other objects
        */
        public void draw (GameObject o, float depth) {
                o.draw(gl, offx, offy, depth);
        }
    
        /**
        * Draws a GameObject at a specified depth with specified colour. The game object will be drawn at it's internally specified position, center and with it's
        * current active texture.
        * <p>
        * This is equivalent to calling setColour (float [] c); draw (GameObject o, float depth) ;
        * @param o The GameObject to be drawn
        * @param c A float [4] specifying the red, green, blue, alpha channels respectively.
        * @param depth The depth at which to draw the GameObject, a lower number means it will be behind other objects
        */
        public void draw (GameObject o, float [] c, float depth) {
                o.draw(gl, offx, offy, c[0], c[1], c[2], c[3], depth);
        }
        
        /**
        * Draws a GameObject at a specified depth with specified colour. The game object will be drawn at it's internally specified position, center and with it's
        * current active texture.
        * <p>
        * This is equivalent to calling setColour (float [] c); draw (GameObject o, float depth);
        * 
        * @param o The GameObject to be drawn
        * @param r Value of the red channel (between 0.0 and 1.0)
        * @param g Value of the green channel (between 0.0 and 1.0)
        * @param b Value of the blue channel (between 0.0 and 1.0)
        * @param a Value of the alpha channel (between 0.0 and 1.0)
        * @param depth The depth at which to draw the GameObject, a lower number means it will be behind other objects
        */
        public void draw (GameObject o, float r, float g, float b, float a, float depth) {
                o.draw(gl, offx, offy, r, g, b, a, depth);
        }
        
        /**
        * Specifies to the draw function that it must draw LINES.
        * LINES are drawn in groups of 2
        */
        public static final int LINES = GL.GL_LINES;
        /**
        * Specifies to the draw function that it must draw TRIANGLES.
        * TRIANGLES are drawn in groups of 3.
        */
        public static final int TRIANGLES = GL.GL_TRIANGLES;
        /**
        * Specifies to the draw function that it must draw QUADS (quadrilaterals).
        * QUADS are drawn in groups of 4.
        */
        public static final int QUADS = GL.GL_QUADS;
        /**
        * Specifies to the draw function that it must draw POINTS.
        * POINTS are drawn in groups of 1.
        */
        public static final int POINTS = GL.GL_POINTS;
        /**
        * Specifies to the draw function that it must draw a POLYGON.
        * Only a single POLYGON will be drawn per call to the draw method.
        */
        public static final int POLYGON = GL.GL_POLYGON;
        /**
        * Specifies to the draw function that it must draw a LINE_STRIP.
        * Only a single LINE_STRIP will be drawn per call to the draw method.
        */
        public static final int LINE_STRIP = GL.GL_LINE_STRIP;
    
        /**
        * Draws a specified shape (possibly zero or multiple times) with a given array of points at a specified depth with the currently set colour.
        * <p>
        * See class description for more details
        * 
        * @param type The type of shape to draw. One of LINES, TRIANGLES, QUADS, POINTS, POLYGON or LINE_STRIP
        * @param points The list of points (or vertexes)
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(int type, Point2D.Float [] points, float depth) {
                gl.glBegin(type); {
                        for (int i = 0 ; i < points.length; i++) {
                                gl.glVertex3f(points[i].x+offx, points[i].y+offy, depth);
                        }
                }
                gl.glEnd();
        }
        
        /**
        * Draws a specified shape (possibly zero or multiple times) with a given array of points at a specified depth with the specified colors.
        * The array of floats should be the same length (or longer) then the length of the array of points.
        * Each point will be assigned the colour specified at the corresponding index of the colour array.
        * This allows one to specify different colours at different vertexes. Colours will fade into one another and will for a gradient.
        * <p>
        * E.g: points[50] will get the colour values set at c[50]
        * <p>
        * See class description for more details
        * 
        * @param type The type of shape to draw. One of LINES, TRIANGLES, QUADS, POINTS, POLYGON or LINE_STRIP
        * @param points The list of points (or vertexes)
        * @param c The list of corresponding colours. Should be an array of float [4]
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(int type, Point2D.Float [] points, float [][] c, float depth) {
            gl.glBegin(type); {
                for (int i = 0 ; i < points.length; i++) {
                    gl.glColor4f(c[i][0], c[i][1], c[i][2], c[i][3]);
                    gl.glVertex3f(points[i].x+offx, points[i].y+offy, depth);
                }
            }
            gl.glEnd();
            setColour(c[points.length-1]);
        }
        
        /**
        * Draws a specified shape (possibly zero or multiple times) with a given array of points at a specified depth with the specified colour.
        * <p>
        * This is equivalent to calling setColour (float r, float g, float b, float a); draw(int type, Point2D.Float [] points, float depth);
        * <p>
        * See class description for more details
        * 
        * @param type The type of shape to draw. One of LINES, TRIANGLES, QUADS, POINTS, POLYGON or LINE_STRIP
        * @param points The list of points (or vertexes)
        * @param r Value of the red channel (between 0.0 and 1.0)
        * @param g Value of the green channel (between 0.0 and 1.0)
        * @param b Value of the blue channel (between 0.0 and 1.0)
        * @param a Value of the alpha channel (between 0.0 and 1.0)
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(int type, Point2D.Float [] points, float r, float g, float b, float a, float depth) {
            setColour(r, g, b, a);
            draw(type, points, depth);
        }
        
        /**
        * Draws a specified shape (possibly zero or multiple times) with a given array of points at a specified depth with the specified colour.
        * <p>
        * This is equivalent to calling setColour (float r, float g, float b, float a); draw(int type, Point2D.Float [] points, float depth);
        * <p>
        * See class description for more details
        * 
        * @param type The type of shape to draw. One of LINES, TRIANGLES, QUADS, POINTS, POLYGON or LINE_STRIP
        * @param points The list of points (or vertexes)
        * @param c A float [4] specifying the red, green, blue, alpha channels respectively.
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(int type, Point2D.Float [] points, float [] c, float depth) {
            setColour(c[0], c[1], c[2], c[3]);
            draw(type, points, depth);
        }
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        /**
        * Draws a String in the specified font (described by a GameFont object) at a specified position and depth.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float depth) {
            draw(gf, text, point, depth, 1.0f);
        }
        /**
                * Draws a String in the specified font (described by a GameFont object) at a specified position and depth, scaled to a percentage of it's origional size. 
        * Note that the scaling is intended for minor adjustments, scalling too big will cause blocky text 
        * and scaling too small with create aliasing artifacts. Rather create another font with smaller or higher size.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        * @param scale What percentage scaling this text should be rendered at (1.0 means the same size, 2.0 means double size etc)
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float depth, float scale) {
            draw(gf, text, point, lastSetColour[0], lastSetColour[1], lastSetColour[2], lastSetColour[3], depth, scale);
        }
        
        /**
        * Draws a String in the specified font (described by a GameFont object) at a specified position, colour and depth.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param c A float [4] specifying the red, green, blue, alpha channels respectively.
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float [] c, float depth) {
            draw(gf, text, point, c[0], c[1], c[2], c[3], depth, 1.0f);
        }
        
        /**
        * Draws a String in the specified font (described by a GameFont object) at a certain position, colour and depth, scaled to a percentage of it's origional size. 
        * Note that the scaling is intended for minor adjustments, scalling too big will cause blocky text 
        * and scaling too small with create aliasing artifacts. Rather create another font with smaller or higher size.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param c A float [4] specifying the red, green, blue, alpha channels respectively.
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        * @param scale What percentage scaling this text should be rendered at (1.0 means the same size, 2.0 means double size etc)
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float [] c, float depth, float scale) {
            draw(gf, text, point, c[0], c[1], c[2], c[3], depth, scale);
        }
        
        /**
        * Draws a String in the specified font (described by a GameFont object) at a certain position, colour and depth, scaled to a percentage of it's origional size. 
        * Note that the scaling is intended for minor adjustments, scalling too big will cause blocky text 
        * and scaling too small with create aliasing artifacts. Rather create another font with smaller or higher size.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param r Value of the red channel (between 0.0 and 1.0)
        * @param g Value of the green channel (between 0.0 and 1.0)
        * @param b Value of the blue channel (between 0.0 and 1.0)
        * @param a Value of the alpha channel (between 0.0 and 1.0)
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float r, float g, float b, float a, float depth) {
            draw(gf, text, point, r, g, b, a, depth, 1.0f);
        }
        
        /**
        * Draws a String in the specified font (described by a GameFont object) at a certain position, colour and depth, scaled to a percentage of it's origional size. 
        * Note that the scaling is intended for minor adjustments, scalling too big will cause blocky text 
        * and scaling too small with create aliasing artifacts. Rather create another font with smaller or higher size.
        *
        * @param gf The GameFont that hold the desired font to render with
        * @param text The text to be rendered
        * @param point The position of the lower left corner of the text to be drawn
        * @param r Value of the red channel (between 0.0 and 1.0)
        * @param g Value of the green channel (between 0.0 and 1.0)
        * @param b Value of the blue channel (between 0.0 and 1.0)
        * @param a Value of the alpha channel (between 0.0 and 1.0)
        * @param depth The depth at which to draw the shape(s), a lower number means it will be behind other objects
        * @param scale What percentage scaling this text should be rendered at (1.0 means the same size, 2.0 means double size etc)
        */
        public void draw(GameFont gf, String text, Point2D.Float point, float r, float g, float b, float a, float depth, float scale) {
            fr.registerFontRender(gf, text, point.x+offx, point.y+offy, r, g, b, a, depth, scale);
        }
        
    }
    
    final void renderGame(GL g) {
        //System.out.println("Game.renderGame() called");
        if (initialised)
            renderStep(new GameDrawer(g));
    }
    /**
    * This step is to render the game world. No game logic should be in this step, all heavy calculations should be moved to the logicStep function.
    * This method should be over-ridden
    * 
    * @param drawer The interface to OpenGL rendering capabilities. Use this to draw your game objects
    */
    public abstract void renderStep(GameDrawer drawer);
    
    //==============================================================================
    /**
    * This class is the interface to Java's Key and mouse input listeners. The standard java way of using Listeners does not apply fully when in a game as
    * it runs on a separate thread. One needs to ensure that the input events do not fire during the logicStep phase and hence the game class acts as a 
    * buffer between the Listener events and the game loop. 
    * <p>
    * The game class stores the current state of the keyboard and mouse inputs (is button x held down,
    * was key z typed this frame etc). During the logic step is blocks all changes to this information and makes it read only, thus allowing the logic loop 
    * safe access to this information without fear of it being changed halfway through. After the logic step is completed, any KeyEvents or MouseEvents 
    * fired during the logicStep phase are logged and untill the logicStep is called again, any Events are logged as well. 
    * <p>
    * This class ensures that the 
    * key/mouse inputs are only read from during the logicStep phase and not written (this makes use of Java syncornized methods for those interested to know).
    */
    public class GameInputInterface {
            
        private GameInputInterface() {}
        /**
        * Returns whether this key is currently held down or not (true means the key is pressed down, false mean the key is currently not held down).
        *
        * Please note that if your GFPS is high, a key will usually register as being down over multiple frames.
        * In some cases this is desired (e.g. holding down a direction button) but in other cases it is not (e.g. firing of a weapon). 
        * Additional logic should be built into your game to prevent unwanted behaviour. Or use keyTyped() method where applicable
        *
        * @param keyCode The key code as specified in the KeyEvent class
        * @return Boolean value whether the key is currently held down.
        */
        public boolean keyDown (int keyCode) {
            return keyDownVec[keyCode];
        }
        
        /**
        * Returns whether a mouse button is currently held down or not (true means the key is pressed down, false mean the key is currently not held down).
        * Please note that if your GFPS is high, a mouse click will usually register as being down over multiple frames.
        * In some cases this is desired (e.g. holding down a direction button) but in other cases it is not (e.g. firing of a weapon). 
        * Additional logic should be built into your game to prevent unwanted behaviour. Or use mouseClicked() method where applicable
        *
        * For the mouseButton parameter, it is as specified in MouseEvent (BUTTON1, BUTTON2 BUTTON3)
        * 
        * @param mouseButton The mouse button as specified in the MouseEvent class
        * @return Boolean value whether the mouse button is currently held down.
        */
        public boolean mouseButtonDown (int mouseButton) {
            return mouseDownVec[mouseButton];
        }
        
        /**
        * Returns whether this key was typed (that it was pressed down and has just been released).
        * This will return true as the key is released. This will only call once and will not go over multiple frames like the keyDown() method.
        * If you want the key to register as being held down over multiple frames use the keyDown() method.
        *
        * @param keyCode The key code as specified in the KeyEvent class
        * @return Boolean value whether the key was typed.
        */
        public boolean keyTyped (int keyCode) {
            return keyTypedVec[keyCode];
        }
        
        /**
        * Returns whether this mouse button was clicked (that it was pressed down and has just been released).
        * This will return true as the mouse button is released. This will only call once and will not go over multiple frames like the mouseuttonDown() method.
        * If you want the mouse button to register as being held down over multiple frames use the mouseButtonDown() method.
        *
        * For the mouseButton parameter, it is as specified in MouseEvent (BUTTON1, BUTTON2 BUTTON3)
        * 
        * @param mouseButton The key code as specified in the MouseEvent class
        * @return Boolean value whether the mouse button was clicked.
        */
        public boolean mouseButtonClicked (int mouseButton) {
            return mouseClickedVec[mouseButton];
        }
        
        /**
        * Returns the x coordinate of the mouse relative to the GameCanvas that it is attached to
        * @return The x coordinate of the mouse
        */
        public int mouseXScreenPosition () {
            return mousePos.x;
        }
        
        /**
        * Returns the y coordinate of the mouse relative to the GameCanvas that it is attached to
        * @return The y coordinate of the mouse
        */
        public int mouseYScreenPosition () {
            return mousePos.y;
        }
        
        /**
        * Returns how much the mouse wheel scrolled (relates to the MouseWheelEvent method getScrollAmount())
        */
        public int mouseWheelScrollAmount() {
            return scrollAmount;
        }
        
        /**
        * Returns what type of scroll occured (relates to the MouseWheelEvent method getScrollType()).
        */
        public int mouseWheelScrollType() {
            return scrollType;
        }
        
        /**
        * Returns the number of units to scroll(relates to the MouseWheelEvent method getUnitsToScroll()).
        */
        public int mouseWheelUnitsToScroll() {
            return unitsToScroll;
        }
        
        /**
        * Returns the mouse whell rotation, 1 or -1 (relates to the MouseWheelEvent method getUnitsToScroll()).
        */
        public int mouseWheelRotation() {
            return wheelRotation;
        }
        
        /**
        * Returns whether the mouse is currently being dragged.
        *
        * This method will return true no matter what button is beng pressed. If you wish to find out if BUTTON1 is being dragged, 
        * use this in conjunction with the mouseButtonDown() function
        */
        public boolean mouseDragged() {
            return mouseDragged;
        }
        
        /**
        * Returns whether the mouse is currently being moved.
        *
        * Only returns true if mouse is being moved without a button being held down. So will return false if mouse is being dragged.
        */
        public boolean mouseMoved() {
            return mouseMoved;
        }
        
    }
    final synchronized void logicGame() {
        //System.out.println("Game.logicGame() called");
        if (initialised) {
            logicStep(theGamesInputInterface);
            
            
            scrollAmount = 0;
            scrollType = 0;
            unitsToScroll = 0;
            wheelRotation = 0;
            
            mouseDragged = false;
            mouseMoved = false;
            
            // undoing all the keyTypes and mouseClicks that occured
            while (!keyTypedStack.isEmpty()) {
                int kc = keyTypedStack.pop();
                keyTypedVec[kc] = false;
            }
            while (!mouseClickedStack.isEmpty()) {
                int mb = mouseClickedStack.pop();
                mouseClickedVec[mb] = false;
            }
        }
    }
    /**
    * This is the backbone of the Game. All game mechanics, logic and other systems must be called from this method. 
    * The Game class will automatically call this function every 1/GFPS seconds (on average) to maintain a steady game pace.
    * All heavy calculations should be in this function and not in renderStep (as if I haven't told you enough :p ).
    *
    */
    public abstract void logicStep(GameInputInterface gii);
    
    //==============================================================================
    
    /**
    * Returns the bounding box of the specified string with the given font (described by a GameFont object).
    * The Rectangle2D's position is at the origin.
    * @param str The string
    * @param gf The GameFont that holds the specified font
    */
    public Rectangle2D.Float getBounds(String str, GameFont gf) {
        return fr.getBounds(str, gf);
    } 
    
    synchronized void updateMousePos(int x, int y) {
        mousePos.x = x;
        mousePos.y = this.viewPortDimension.height-y;
    }
    

    synchronized void setViewPortDimension(Dimension dimension) {
    	this.viewPortDimension = new Dimension(dimension);
    }

  /**
  * Returns the dimentions of the viewport the game is in.
  * The Rectangle2D's position is at the origin.
  * @return Dimention of the viewport
  */
	public Dimension getViewportDimension() {
		
		return new Dimension(viewPortDimension);
	}
	
    // we need to push the keyTyped and mouseClicked events to a stack so that they can be undone at the end of the logic step
    // it is a hell of a lot more eficient then clearing all 512 boolean values in my opinion
    synchronized void registerKeyTyped(int keyCode) {
        keyTypedVec[keyCode] = true;
        keyTypedStack.push(keyCode);
    }
    synchronized void registerMouseClicked(int mouseButton) {
        mouseClickedVec[mouseButton] = true;
        mouseClickedStack.push(mouseButton);
    }
    
    synchronized void registerKeyDown(int keyCode, boolean pressed /*false means released*/) {
        keyDownVec[keyCode] = pressed;
    }
    
    synchronized void registerMouseDown(int mouseButton, boolean pressed /*false means released*/) {
        mouseDownVec[mouseButton] = pressed;
    }
    
    synchronized void registerMouseDragged() {
        mouseDragged = true;
    }
    
    synchronized void registerMouseMoved() {
        mouseMoved = true;
    }
    
    synchronized void registerScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }
    synchronized void registerScrollType(int scrollType) {
        this.scrollType = scrollType;
    }
    synchronized void registerUnitsToScroll(int unitsToScroll) {
        this.unitsToScroll = unitsToScroll;
    }
    synchronized void registerWheelRotation(int wheelRotation) {
        this.wheelRotation = wheelRotation;
    }
    
    
    
    /**
    * This function links this game to a JFrames windowClosing event. 
    * Which means that this Game's endGame method will be called before the window is closed.
    * If you do not link this Game to your JFrame the game will continue to run even after closing the window.
    * 
    * @param f The JFrame to link this Game to
    */
    public void linkToFrame(JFrame f) {
        
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)  {
                endGame();
            }
        });
    }
    //==============================================================================
}
