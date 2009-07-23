

import GameEngine.GameObject;
import java.awt.geom.*;

/**
 * An extension of the GameObject that adds basic physical properties such as mass and velocity. Additional behaviour has also been implemented.
 * 
 * @author Richard Baxter
 *
 */
public class PhysicalObject extends GameObject {

    protected Point2D.Float velocity = new Point2D.Float(0, 0); // in units per frame
    protected float mass; // in ...uhm ... thingies... whatever all the physic-y equations make it ...
    
    //==================================================================================================
    
    /**
     * Basic constructor
     *
     * @param x Position along the x coordinate
     * @param y Position along the y coordinate
     * @param m The mass of this object
     */
    public PhysicalObject(float x, float y, float m){
        super(x,y);
        mass = m;
    }
    
    
    //==================================================================================================
    
    /**
     * Directly sets the velocity of this object
     * 
     * @param v The values in the x and y directions of the new velocity
     */
    public void setVelocity(Point2D.Float v) {
        velocity.x = v.x;
        velocity.y = v.y;
    }
    
    /**
     * Directly sets the velocity of this object
     * 
     * @param vx The values in the x direction the new velocity
     * @param vy The values in the y direction the new velocity
     */
    public void setVelocity(float vx, float vy) {
        velocity.x = vx;
        velocity.y = vy;
    }
    
    /**
     * Gets the object's current velocity
     * @return A point representing the velocities in the x and y directions
     */
    public Point2D.Float getVelocity() {
        return new Point2D.Float(velocity.x, velocity.y);
    }
    //--------------------------------------------
        
    /**
     * Applies a force to this object in the x and y directions. The additional velocity is calculated according to the equation velocity = force/mass.
     * 
     * @param forceX The force to apply in the x direction
     * @param forceY The force to apply in the y direction
     */
    public void applyForce(float forceX, float forceY) {
        velocity.x += forceX/mass;
        velocity.y += forceY/mass;
    }
    
    /**
     * Applies a force in the specified direction.
     * <p>
     * This is equivalent to calling applyForce((float)Math.sin(Math.toRadians(direction))*force, (float)-Math.cos(Math.toRadians(direction))*force);
     * 
     * @param direction The direction in degrees with 0 being South
     * @param force The force to apply in the specified direction
     */
    public void applyForceInDirection(float direction, float force) {
        applyForce((float)Math.sin(Math.toRadians(direction))*force, (float)-Math.cos(Math.toRadians(direction))*force);
    }

    /**
     * Multiplies the current velocity by a specified amount
     * <p>
     * equivalent to: 
	 * <p>
	 * velocity.x *= f; 
	 * <p>
	 * velocity.y *= f;
     * 
     * @param f The multiplying value. Should be between 0 and 1. Other values will give odd results. 
     */
	public void applyFriction(float f) {
		velocity.x *= f;
		velocity.y *= f;
	}
    
	/**
	 * Performs the time step operations for this object.
	 * <p>
	 * In the case of PhysicalObject it updates the position depending on the velocity.
	 * <p>
	 * i.e.
	 * <p>
     *  position.x += velocity.x;
	 * <p>
     *  position.y += velocity.y;
	 * 
	 */
    public void doTimeStep() {
        position.x += velocity.x;
        position.y += velocity.y;
    }
}
