

import java.util.Vector;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;

import GameEngine.Game;
import GameEngine.GameTexture;
import GameEngine.GameFont;
import GameEngine.GameObject;


//==================================================================================================
//==================================================================================================

public class SurvivalGame extends Game
{
	// Offset of the screen
    private Point2D.Float offset = new Point2D.Float(0,0);
    
    private boolean alive = true;
    
    // A Collection of GameObjects in the world that will be used with the collision detection system
    private Vector<GameObject> objects = new Vector<GameObject>();
    
    // Grid GameObjects
    private GameObject [] [] gridTile;
    
    // The cooldown of the gun (set this to 0 for a cool effect :> )
    private int cooldown = 10;
    private int cooldownTimer = 0;
    
    // Important GameObjects
    private PlayerObject player; // the player
    
    //Textures that will be used
    private GameTexture bulletTexture;
    
    //GameFonts that will be used
    private GameFont arial, serif;
    
    // The positin of the mouse
    private Point2D.Float mousePos = new Point2D.Float (0,0);
    
    // a counter for how far the mousewheel has been moved (just an example)
    private int mouseWheelTick = 0;
    
    // Information for the random line at the bottom of the screen
    Point2D.Float [] linePositions = {new Point2D.Float(0,0), new Point2D.Float(100,100)};
    float [][] lineColours = {{1.0f,1.0f,1.0f,1.0f},{1.0f,0.0f,0.0f,1.0f}};
    
    //==================================================================================================
    
    public SurvivalGame (int GFPS) {
        super(GFPS);
    }

    //==================================================================================================
    
    public void initStep(ResourceLoader loader) {

        //Loading up some fonts
        arial = loader.loadFont(  new Font("Arial", Font.ITALIC, 48) );
        serif = loader.loadFont(  new Font("Serif", Font.PLAIN, 48) );
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        

        //Loading up our textures
        GameTexture softRockTexture = loader.loadTexture("Textures/soft_rock.png");
        GameTexture rockTexture = loader.loadTexture("Textures/rock.png");
        GameTexture grassTexture = loader.load("Textures/grass_tile.jpg");
        bulletTexture = loader.loadTexture("Textures/bullet.png");
        
        
        int gridSize = 12;
        
        
        // creating some random rocks to shoot
        for (int i = 0 ; i < 8 ; i++ ) {
          
          float x = (float) ((Math.random()*(gridSize-4)+2)*grassTexture.getWidth());
          float y = (float) ((Math.random()*(gridSize-4)+2)*grassTexture.getHeight());
          
    		  GameObject go = new GameObject(x, y);
    		  go.addTexture(softRockTexture, 0, 0);
    		  objects.add(go);
        }
        
        
        // creating the floor objects
        gridTile = new GameObject[gridSize][gridSize];
        for (int i = 0 ; i < gridSize ; i++ ) {
        	for (int j = 0 ; j < gridSize ; j++ ) {
        		gridTile[i][j] = new GameObject(grassTexture.getWidth()*i,grassTexture.getHeight()*j);
        		gridTile[i][j].addTexture(grassTexture, 0, 0);
            }
        }
        
        
        // Creating wall objects
        for (int i = 0 ; i < grassTexture.getWidth()*gridSize ; i += rockTexture.getWidth()) {
    		WallObject go = new WallObject(i, 0);
    		go.addTexture(rockTexture, 0, 0);
    		objects.add(go);
    		
    		go = new WallObject(i, grassTexture.getHeight()*(gridSize-1));
    		go.addTexture(rockTexture, 0, 0);
    		objects.add(go);
        }
        for (int i = grassTexture.getHeight() ; i < grassTexture.getHeight()*(gridSize-1) ; i += rockTexture.getHeight()) {
    		WallObject go = new WallObject(0, i);
    		go.addTexture(rockTexture, 0, 0);
    		objects.add(go);
    		
    		go = new WallObject(rockTexture.getWidth()*(gridSize-1), i);
    		go.addTexture(rockTexture, 0, 0);
    		objects.add(go);
        }
        
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        
        // Creating the player's ship
        player = new PlayerObject(
                             (float)(grassTexture.getWidth()*gridSize)/2f,
                             (float)(grassTexture.getHeight()*gridSize)/2f);
        
        for (int i = 0 ; i < 72 ; i++) {
        	player.addTexture(loader.load("Textures/ship/spaceship_sm"+i+".gif"), 16, 16);
        }
        
        objects.add(player);

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    }

    //==================================================================================================
    
    // this method is used to fire a bullet 
    public void fireBullet() {
        
        cooldownTimer = cooldown;
        
        float dir = 90+player.getDegreesTo(mousePos);
        BulletObject bullet =
                new BulletObject(
                         player.getPosition().x + (float)Math.sin(Math.toRadians(dir))*32, player.getPosition().y - (float)Math.cos(Math.toRadians(dir))*32, 1f, 300, bulletTexture);
        
        //bullet.setVelocity(player.getVelocity());
        bullet.applyForceInDirection(dir, 6f);
        
        objects.add(bullet);
    }
    
    public static boolean isPointInBox(final Point2D.Float point, final Rectangle2D.Float box) {
        return box.contains(point.x, point.y);
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
    
    private void handleControls(GameInputInterface gii) {
        
        //----------------------------------
    	
    	// This isn't so great, there are better and neater ways to do this, you are encouraged to implement a better one
        boolean move = false;
        float directionToMove = 0;
        
        if(gii.keyDown(KeyEvent.VK_UP)) {
        	move = true;
            if(gii.keyDown(KeyEvent.VK_LEFT) && !gii.keyDown(KeyEvent.VK_RIGHT))
            	directionToMove = 225;
            else if(gii.keyDown(KeyEvent.VK_RIGHT) && !gii.keyDown(KeyEvent.VK_LEFT))
            	directionToMove = 135;
            else
            	directionToMove = 180;
        }
        else if(gii.keyDown(KeyEvent.VK_DOWN)) {
        	move = true;
            if(gii.keyDown(KeyEvent.VK_LEFT) && !gii.keyDown(KeyEvent.VK_RIGHT))
            	directionToMove = -45;
            else if(gii.keyDown(KeyEvent.VK_RIGHT) && !gii.keyDown(KeyEvent.VK_LEFT))
            	directionToMove = 45;
            else
            	directionToMove = 0;
        }
        else if(gii.keyDown(KeyEvent.VK_LEFT) && !gii.keyDown(KeyEvent.VK_RIGHT)) {
        	move = true;
        	directionToMove = 270;
        }
        else if(gii.keyDown(KeyEvent.VK_RIGHT) && !gii.keyDown(KeyEvent.VK_LEFT)) {
        	move = true;
        	directionToMove = 90;
        }
        if (move)
        	player.moveInDirection(directionToMove);
        
        if (cooldownTimer <= 0) {
            if(gii.keyDown(KeyEvent.VK_SPACE) || gii.mouseButtonDown(MouseEvent.BUTTON1)) {
                fireBullet();
            }
        }
        cooldownTimer --;
        
    }

    //==================================================================================================
    
    public void logicStep(GameInputInterface gii) {

        /*if(gii.keyDown(KeyEvent.VK_W)) {
            offset.y -= 3.0;
        }
        if(gii.keyDown(KeyEvent.VK_S)) {
            offset.y += 3.0;
        }
        if(gii.keyDown(KeyEvent.VK_A)) {
            offset.x += 3.0;
        }
        if(gii.keyDown(KeyEvent.VK_D)) {
            offset.x -= 3.0;
        }
        if(gii.keyDown(KeyEvent.VK_ESCAPE)) {
            endGame();
        }*/
        
        // some examples of the mouse interface
        mouseWheelTick+= gii.mouseWheelRotation();
        mousePos.x = (float)gii.mouseXScreenPosition() - offset.x;
        mousePos.y = (float)gii.mouseYScreenPosition() - offset.y;
        
        //----------------------------------
        
        if (alive) {
            handleControls(gii);
            player.setDirection(90+player.getDegreesTo(mousePos));
        }
        
        // NOTE: you must call doTimeStep for ALL game objects once per frame!
        // updateing step for each object
        for (int i = 0 ; i < objects.size() ; i++) {
            objects.elementAt(i).doTimeStep();
        }
        
        // setting the camera offset
        offset.x = -player.getPosition().x + (this.getViewportDimension().width/2);
        offset.y = -player.getPosition().y + (this.getViewportDimension().height/2);
        
        
        //checking each unit against each other unit for collisions
        for (int i = 0 ; i < objects.size() ; i++) {
            for (int j = i+1 ; j < objects.size() ; j++) {
            	GameObject o1 = objects.elementAt(i);
            	GameObject o2 = objects.elementAt(j);
                if (boxIntersectBox(o1.getAABoundingBox(), o2.getAABoundingBox())) {
                	if (o1 instanceof WallObject && o2 instanceof WallObject) {
                		//do nothing
                	}
                	else if ((o1 instanceof BulletObject && o2 instanceof WallObject) || o1 instanceof WallObject && o2 instanceof BulletObject) {
                		// just destroy the bullet, not the wall
                		if (o1 instanceof BulletObject)
                    		o1.setMarkedForDestruction(true);
                		else
                    		o2.setMarkedForDestruction(true);
                			
                	}
                	else if (o1 instanceof PlayerObject || o2 instanceof PlayerObject){
                		player.revertPosition ();
                	}
                	else {
                		System.out.println("Removing objects "+i +":"+j);
                		o1.setMarkedForDestruction(true);
                		o2.setMarkedForDestruction(true);
                		
                		// Note: you can also implement something like o1.reduceHealth(5); if you don't want the object to be immediatly destroyed
                	}
                }
            }
        }
        
        // destroying units that need to be destroyed
        for (int i = 0 ; i < objects.size() ; i++) {
            if (objects.elementAt(i).isMarkedForDestruction()) {
            	if (objects.elementAt(i) == player) {
                    alive = false;
                    player = null;
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
    	
    	// Offsetting the world so that all objects are drawn 
    	drawer.setWorldOffset(offset.x, offset.y);
        drawer.setColour(1.0f, 1.0f, 1.0f, 1.0f);

        // drawing the ground tiles
        for (int i = 0 ; i < gridTile.length ; i++ ) {
        	for (int j = 0 ; j < gridTile[i].length ; j++ ) {
        		drawer.draw(gridTile[i][j], -1);
        	}
        }
        
        // drawing all the objects in the game
        for (GameObject o: objects) {
        	drawer.draw(o, 1.0f, 1.0f, 1.0f, 1.0f, 0);
        }
        

        // this is just a random line drawn in the corner of the screen
        drawer.draw(GameDrawer.LINES, linePositions, lineColours, 0.5f);
        
        if (player != null) {
        	Point2D.Float [] playerLines = {mousePos, player.getPosition()};
        	drawer.draw(GameDrawer.LINES, playerLines, lineColours, 0.5f);
        }
        
        drawer.setColour(1.0f,1.0f,1.0f,1.0f);
        
        // Changing the offset to 0 so that drawn objects won't move with the camera
        drawer.setWorldOffset(0, 0);
        
        // this is just a random line drawn in the corner of the screen (but not offsetted this time ;) )
        drawer.draw(GameDrawer.LINES, linePositions, lineColours, 0.5f);
        
        
        // Some debug type info to demonstrate the font drawing
        if (player != null) {
        	drawer.draw(arial, ""+player.getDirection(), new Point2D.Float(20,120), 1.0f, 0.5f, 0.0f, 0.7f, 0.1f);
        }
        drawer.draw(arial, ""+mouseWheelTick, new Point2D.Float(20,68), 1.0f, 0.5f, 0.0f, 0.7f, 0.1f);
        drawer.draw(serif, ""+mousePos.x +":"+mousePos.y, new Point2D.Float(20,20), 1.0f, 0.5f, 0.0f, 0.7f, 0.1f);
    }
}








