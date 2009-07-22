import java.util.Vector;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;

import game.Game;
import game.GameTexture;
import game.GameFont;
import game.GameObject;


//==================================================================================================
//==================================================================================================

public class AsteroidGame extends Game
{
	// Offset of the screen
    private float offsetx = 0, offsety = 0;
    
    private boolean alive = true;
    
    // A Collection of GameObjects in the world that will be used with the collision detection system
    private Vector<GameObject> objects = new Vector<GameObject>();
    
    // The cooldown of the gun (set this to 0 for a cool effect :> )
    private int cooldown = 20;
    private int cooldownTimer = 0;
    
    // Important GameObjects
    private PlayerObject p; // the player
    private GameObject background;
    
    //Textures that will be used
    private GameTexture bulletTexture;
    private GameTexture astTex;
    private GameTexture halfAstTex;
    
    //GameFonts that will be used
    private GameFont arial, serif;
    
    // The positin of the mouse
    private Point mousePos = new Point (0,0);
    
    // a counter for how far the mousewheel has been moved (just an example)
    private int mouseWheelTick = 0;
    
    // Information for the random line at the bottom of the screen
    Point2D.Float [] ps = {new Point2D.Float(0,0), new Point2D.Float(100,100)};
    float [][] c = {{1.0f,1.0f,1.0f,1.0f},{1.0f,0.0f,0.0f,1.0f}};
    
    //==================================================================================================
    
    public AsteroidGame (int GFPS) {
        super(GFPS);
    }

    //==================================================================================================
    
    public void initStep(ResourceLoader loader) {

        //Loading up some fonts
        arial = loader.loadFont(  new Font("Arial", Font.ITALIC, 48) );
        serif = loader.loadFont(  new Font("Serif", Font.PLAIN, 48) );
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        //loading up some textures
        bulletTexture = loader.loadTexture("Textures/bullet.png");
        astTex = loader.loadTexture("Textures/asteroid.png");
        halfAstTex = loader.loadTexture("Textures/asteroidsmall.png");

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        background = new GameObject(0,0);
        GameTexture backgroundTexture = loader.load("Textures/background1024.jpg");
        background.addTexture(backgroundTexture, 0, 0);
        
        // Can also do...
        //background.addTexture(loader.load("Textures/background1024.jpg"), 0, 0);
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        // Creating the player's ship
        p = new PlayerObject(
                             (float)(background.getAABoundingBox().width)/2f,
                             (float)(background.getAABoundingBox().height)/2f,  100f);
        
        for (int i = 0 ; i < 72 ; i++) {
            p.addTexture(loader.load("Textures/spaceship_sm"+i+".gif"), 16, 16);
        }
        
        p.setSubImage(0,0,32, 32);
        objects.add(p);

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        // Adding 10 asteroids in random locations
        for (int i = 0 ; i < 10 ; i++) {
            AsteroidObject ast = new AsteroidObject ((float)(Math.random()*background.getAABoundingBox().width), (float)(Math.random()*background.getAABoundingBox().height), 100.0f, astTex);
            ast.applyForce((float)(Math.random())*100f - 50f,(float)(Math.random())*100f - 50f);
            
            objects.add(ast);
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    }

    //==================================================================================================
    
    // this method is used to fire a bullet 
    public void fireBullet() {
        
        cooldownTimer = cooldown;
        
        float dir = p.getDirection();
        BulletObject bullet =
                new BulletObject(
                         p.getPosition().x + (float)Math.sin(Math.toRadians(dir))*32, p.getPosition().y - (float)Math.cos(Math.toRadians(dir))*32, 1f, 300, bulletTexture);
        
        bullet.setVelocity(p.getVelocity());
        bullet.applyForceInDirection(dir, 3f);
        
        objects.add(bullet);
    }
    
    public static boolean isPointInBox(final Point2D.Float p, final Rectangle2D.Float d) {
        return p.x - d.x <= d.width  && p.x - d.x >= 0 &&
               p.y - d.y <= d.height && p.y - d.y >= 0;
    }
    
    // This is a pretty bad implementation and faster ones exist, it is suggested you find a better one. At least try make use of the Rectangle2D's createIntersection method.
    public static boolean boxIntersectBox (final Rectangle2D.Float d, final Rectangle2D.Float d2) {
        return  isPointInBox(new Point2D.Float (d.x, d.y), d2) ||
                isPointInBox(new Point2D.Float (d.x, d.y+d.height), d2) ||
                isPointInBox(new Point2D.Float (d.x+d.width, d.y), d2) ||
                isPointInBox(new Point2D.Float (d.x+d.width, d.y+d.height), d2) ||
                isPointInBox(new Point2D.Float (d2.x, d2.y), d) ||
                isPointInBox(new Point2D.Float (d2.x, d2.y+d2.height), d) ||
                isPointInBox(new Point2D.Float (d2.x+d2.width, d2.y), d) ||
                isPointInBox(new Point2D.Float (d2.x+d2.width, d2.y+d2.height), d);
    }
    
    private void handleShipControls(GameInputInterface gii) {
        
        //----------------------------------
        
        if(gii.keyDown(KeyEvent.VK_UP)) {
            p.applyThust(3f);
        }
        if(gii.keyDown(KeyEvent.VK_DOWN)) {
        }
        if(gii.keyDown(KeyEvent.VK_LEFT)) {
            p.incrementDirection(3f);
        }
        if(gii.keyDown(KeyEvent.VK_RIGHT)) {
            p.incrementDirection(-3f);
        }
        
        if (cooldownTimer <= 0) {
            if(gii.keyDown(KeyEvent.VK_SPACE)) {
                fireBullet();
            }
        }
        cooldownTimer --;
        
    }

    //==================================================================================================
    
    public void logicStep(GameInputInterface gii) {

        if(gii.keyDown(KeyEvent.VK_W)) {
            offsety -= 1.0;
        }
        if(gii.keyDown(KeyEvent.VK_S)) {
            offsety += 1.0;
        }
        if(gii.keyDown(KeyEvent.VK_A)) {
            offsetx += 1.0;
        }
        if(gii.keyDown(KeyEvent.VK_D)) {
            offsetx -= 1.0;
        }
        if(gii.keyDown(KeyEvent.VK_ESCAPE)) {
            endGame();
        }
        
        // some examples of the mouse interface
        mouseWheelTick+= gii.mouseWheelRotation();
        mousePos.x = gii.mouseXScreenPosition();
        mousePos.y = gii.mouseYScreenPosition();
        
        //----------------------------------
        
        if (alive) {
            handleShipControls(gii);
            p.applyFriction(0.999f);
        }
        
        // NOTE: you must call doTimeStep for ALL game objects once per frame!
        // updateing step for each object
        for (int i = 0 ; i < objects.size() ; i++) {
            objects.elementAt(i).doTimeStep();
        }
        
        //looping through all objects in the game
        for (int i = 0 ; i < objects.size() ; i++) {
            
            // wrapping objects around the map that go out of range
            Point2D.Float pos = objects.elementAt(i).getPosition();
            Rectangle2D.Float bb = background.getAABoundingBox();
            
            if (!isPointInBox(pos, bb)) {
                if (pos.x < 0)          pos.x += bb.width;
                if (pos.y < 0)          pos.y += bb.height;
                if (pos.x >= bb.width)  pos.x -= bb.width;
                if (pos.y >= bb.height) pos.y -= bb.height;
                objects.elementAt(i).setPosition(pos);
            }
        }
        
        //checking each unit against each other unit for collisions
        for (int i = 0 ; i < objects.size() ; i++) {
            for (int j = i+1 ; j < objects.size() ; j++) {
            	if (objects.elementAt(i) instanceof PlayerObject && objects.elementAt(j) instanceof BulletObject)
            		continue;
            	if (objects.elementAt(i) instanceof BulletObject && objects.elementAt(j) instanceof BulletObject)
            		continue;
            	if (objects.elementAt(i) instanceof AsteroidObject && objects.elementAt(j) instanceof AsteroidObject)
            		continue;
                if (boxIntersectBox(objects.elementAt(i).getAABoundingBox(), objects.elementAt(j).getAABoundingBox())) {
                        System.out.println("Removing objects "+i +":"+j);
                        objects.elementAt(i).setMarkedForDestruction(true);
                        objects.elementAt(j).setMarkedForDestruction(true);
                }
            }
        }
        
        // destroying units that need to be destroyed
        for (int i = 0 ; i < objects.size() ; i++) {
            if (objects.elementAt(i).isMarkedForDestruction()) {
                if (objects.elementAt(i) instanceof AsteroidObject) {
                    int scale = ((AsteroidObject)objects.elementAt(i)).getPictureScale();
                    float scalef = (float)scale;
                    
                    // creating 2 smaller asteroids
                    if (scale != 2) {
                        for (int j = 0 ; j < 2 ; j++) {
                            AsteroidObject ast = new AsteroidObject (objects.elementAt(i).getPosition().x, objects.elementAt(i).getPosition().y, 100.0f/scalef, halfAstTex);
                            ast.setPictureScale(scale*2);
                            
                            ast.applyForce((float)(Math.random())*100f - 50f,(float)(Math.random())*100f - 50f);
                            objects.add(ast);
                        }
                    }
                }
                else if (objects.elementAt(i) == p) {
                    alive = false;
                    p = null;
                }
                // removing object from list of GameObjects
                objects.remove(i);
                i--;
            }
        }
    }

    //==================================================================================================
    public void renderStep(GameDrawer drawer) {
    	//For every object that you want to be rendered, you must call the draw function with it as a parameter
    	
    	// NOTE: Always draw transparent objects last!
    	drawer.setOffsets(offsetx, offsety);
        drawer.setColour(1.0f, 1.0f, 1.0f, 1.0f);
        drawer.draw(background, -1);
        
        for (GameObject o: objects) {
        	drawer.draw(o, 1.0f, 1.0f, 1.0f, 1.0f, 0);
        }
        

        // this is just a random line drawn in the corner of the screen
        drawer.draw(GameDrawer.LINES, ps, c, -0.5f);
        
        drawer.setColour(1.0f,1.0f,1.0f,1.0f);
        
        drawer.setOffsets(0, 0);
        // this is just a random line drawn in the corner of the screen (but not offsetted)
        drawer.draw(GameDrawer.LINES, ps, c, -0.5f);
        
        // Some debug type info to demonstrate the font drawing
        drawer.draw(arial, ""+mouseWheelTick, new Point2D.Float(20,68), 1.0f, 0.5f, 0.0f, 0.7f, 0.1f);
        drawer.draw(serif, ""+mousePos.x +":"+mousePos.y, new Point2D.Float(20,20), 1.0f, 0.5f, 0.0f, 0.7f, 0.1f);
    }
}








