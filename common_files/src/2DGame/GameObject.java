package GameEngine;


import java.util.Vector;
import java.awt.*;
import java.awt.geom.*;
import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;

/**
 * This is the base Object for anything in your game. Backgrounds, units, bullets, enemies 
 * all should be instances of GameObject.
 * <p>
 * To specify a GameObject you need a position and a texture. The texture will determine the
 * game object's width and height. The GameObject cannot be scaled. If you want a larger
 * or smaller image you need to edit it manually (using GIMP or some other editor).
 * <p>
 * You may load multiple images in order to do animation. You may also load a large e image
 * and specify a sub-image to render.
 * Each texture will have a specified center relative to the bottom left corner of the
 * texture. When rendering, the center of the image will be drawn at this GameObject's
 * position and the rest of the image drawn around it.
 * <p>
 * 
 * @author Richard Baxter
 *
 *	
 */
public class GameObject {
    
    
    //==============================================================================
    
    protected Point2D.Float position; // in units
    //protected float depth = 0;
    
    private boolean markedForDestruction = false;
    
    //TODO combine texture and center into a nested class
    private Vector<GameTexture> textures = new Vector<GameTexture>();
    private Vector<Point2D.Float> centers = new Vector<Point2D.Float>();
    int activeTexture = -1;
    
    TextureCoords tc;
    
    private boolean useSubImage = false;
    private Point subImage1 = new Point(0, 0);
    private Point subImage2 = new Point(0, 0);
    private Point subImageOffset = new Point(0, 0);
    private Point2D.Float imgDim = new Point2D.Float(0, 0);
    
    //==================================================================================================


    /**
     * Basic Constructor for the GameObject
     *
     * @param x Position along the x coordinate
     * @param y Position along the y coordinate
     */
    public GameObject(float x, float y) {
        position = new Point2D.Float(x, y);
        //setDepth(d);
    }
    
    public void finalize() {
        while (!textures.isEmpty())
            removeTexture(0);
    }
    

    /**
     * Returns the angle between this GameObject and another
     *
     * @param o The GameObject to get the angle from
     * @return Then angle in degrees
     */
    public float getDegreesTo(GameObject o) {
    	
    	return getDegreesTo(o.position);
    }
    

    /**
     * Returns the angle between this GameObject and another
     *
     * @param o The GameObject to get the angle from
     * @return Then angle in radians
     */
    public float getRadiansTo(GameObject o) {
    	
    	return getRadiansTo(o.position);
    }
    


    /**
     * Returns the angle between this GameObject and a point
     *
     * @param o The point to get the angle from
     * @return Then angle in degrees
     */
    public float getDegreesTo(Point2D.Float o) {
    	
    	return (float)Math.toDegrees(getRadiansTo(o));
    }
    

    /**
     * Returns the angle between this GameObject and a point
     *
     * @param o The point to get the angle from
     * @return Then angle in radians
     */
    public float getRadiansTo(Point2D.Float o) {
    	
    	return (float)Math.atan2((o.y - position.y),(o.x - position.x));
    }
    
    //==================================================================================================

    /**
     * Sets the depth of this object, a lower number means it will be behind other objects
     *
     * @param d Depth of this object
     */
    /*public void setDepth(float d) {
        depth = d;
        if (depth < -1) depth = -1;
        if (depth > 1) depth = 1;
    }*/
    
    /**
     * Gets the depth of this object
     *
     * @return Depth of this object
     */
    /*public float getDepth() {
        return depth;
    }*/
    //--------------------------------------------

    /**
     * Sets the position of the object. Objects will be drawn with their center at this position
     *
     * @param p Position of the object
     */
    public void setPosition(Point2D.Float p) {
        position.x = p.x;
        position.y = p.y;
    }
    
    /**
     * Sets the position of the object. Objects will be drawn with their center at this position
     *
     * @param x x position of the object
     * @param y y position of the object
     */
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }
    
    /**
     * Sets the position of the object. Objects will be drawn with their center at this position
     *
     * @param p Position of the object
     */
    public void incrementPosition(Point2D.Float p) {
        position.x += p.x;
        position.y += p.y;
    }
    
    /**
     * Sets the position of the object. Objects will be drawn with their center at this position
     *
     * @param x x position of the object
     * @param y y position of the object
     */
    public void incrementPosition(float x, float y) {
        position.x += x;
        position.y += y;
    }
    
    /**
     * Gets the position of this object
     *
     * @return Depth of this object
     */
    public Point2D.Float getPosition() {
        return new Point2D.Float(position.x, position.y);
    }
    //--------------------------------------------
    
    /**
     * Gets the Axis-Aligned Bounding Box of this object for use in collision detection/debugging
     *
     * @return Axis-Aligned Bounding Box
     */
    public Rectangle2D.Float getAABoundingBox() {
        return new Rectangle2D.Float(position.x - getCurrentCenter().x, position.y - getCurrentCenter().y, imgDim.x, imgDim.y);
    }
    
    /**
     * Gets the Integer Value Axis-Aligned Bounding Box of this object for use in collision detection/debugging
     *
     * @return Integer Valued Axis-Aligned Bounding Box
     */
     public Rectangle getIntAABoundingBox() {
        return new Rectangle((int)(position.x - getCurrentCenter().x), (int)(position.y - getCurrentCenter().y), (int)imgDim.x, (int)imgDim.y);
     }
    
    private Point2D.Float getCurrentCenter() {
        return centers.elementAt(activeTexture);
    }
    
    public GameTexture getCurrentTexture() {
        return textures.elementAt(activeTexture);
    }
    
    //==================================================================================================
    
    /**
     * One can set a flag in this object to mark that it needs to be destroyed at the end of the game loop
     *
     * @param m Boolean value marking whether it should be destroyed (true, means is should be destroyed)
     */
    public void setMarkedForDestruction (boolean m) {
        markedForDestruction = m;
    }
    
    /**
     * Returns whether this unit has been marked for destruction (true, means is should be destroyed)
     *
     * @return Boolean value marking whether it should be destroyed
     */
    public boolean isMarkedForDestruction () {
        return markedForDestruction;
    }
    
    //==================================================================================================
    
    /**
     * Adds a texture to this object's list of textures. The corresponding Center position will be set to the center of the image as default
     * 
     * @param t The GameTexture containing the image
     */
    public void addTexture(GameTexture t) {
        addTexture(t, (t.w+1)/2, (t.h+1)/2);
    }
    
    /**
     * Adds a texture to this object's list of textures with specified center. 
     * The center is relative to the bottom left hand corner of the image. 
     * So (0,0) as center corresponds to the bottom left hand corner of the image.
     * 
     * @param t The GameTexture containing the image
     * @param centerX The center of the image in the x direction relative to the image
     * @param centerY The center of the image in the y direction relative to the image
     */
    public void addTexture(GameTexture t, float centerX, float centerY) {
        textures.add(t);
        centers.add(new Point2D.Float(centerX, centerY));
        
        if (textures.size() == 1) // i.e. textures was empty before calling
            setActiveTexture(0);
        
        
    }
    
    /**
     * Removes the texture at position i. All textures with index greater then i will be shifted down one position
     * 
     * @param index index of the texture to remove
     */
    public void removeTexture(int index) {
        textures.remove(index);
        centers.remove(index);
        
        if (activeTexture == index)
            setActiveTexture(index);
    }
    
    /**
     * Gets the number of textures this object currently holds
     * 
     * @return Number of textures
     */
    public int getNumberOfTextures() {
        return textures.size();
    }
    
    //==================================================================================================
    
    /**
    * Sets which texture will be drawn during the render stage of the game loop
    * 
    * @param i The index of which texture should be set to as active
    */
    public void setActiveTexture(int i) {
        if (textures.size() == 0){
            activeTexture = -1;
            return;
        }
        activeTexture = i%textures.size();
        
        setTextureCoords();
    }
    
    /**
     * Gets the index of the texture that will be drawn during the render stage of the game loop
     * 
     * @return The index of the active texture
     */
    public int getActiveTexture() {
        return activeTexture;
    }
    
    //==================================================================================================
    
    /**
     * Sets the coordinates of the sub-image and its center that will be drawn during the render stage of the game loop
     * 
     * @param x The x coordinate of the subimage relative to the image
     * @param y The y coordinate of the subimage relative to the image
     * @param w The width of the subimage
     * @param h The height of the subimage
     * @param cx The x coordinate of the center of the sub-image relative to the sub-image (not relative to the image)
     * @param cy The y coordinate of the center of the sub-image relative to the sub-image (not relative to the image)
     */
    public void setSubImage(int x, int y, int w, int h, int cx, int cy){
        getCurrentCenter().x = cx;
        getCurrentCenter().y = cy;
        setSubImage(x, y, w, h);
    }
    
    /**
     * Returns the position of the sub-image relative to the whole image, returns 0,0 if no sub-images is being used.
     * 
     * @return position of the sub-image relative to the whole image
     */
    public Point getSubImageOffset() {
        return new Point (subImageOffset.x, subImageOffset.y);
    }
    
    /**
     * Sets the coordinates of the sub-image that will be drawn during the render stage of the game loop. The center of the sub-image remains unchanged
     * 
     * @param x The x coordinate of the subimage relative to the image
     * @param y The y coordinate of the subimage relative to the image
     * @param w The width of the subimage
     * @param h The height of the subimage
     */
    public void setSubImage(int x, int y, int w, int h){
        subImageOffset.x = x;
        subImageOffset.y = y;
        subImage1.x = x*getCurrentTexture().t.getWidth()/getCurrentTexture().w;
        subImage1.y = y*getCurrentTexture().t.getHeight()/getCurrentTexture().h;
        subImage2.x = (x+w)*getCurrentTexture().t.getWidth()/getCurrentTexture().w;
        subImage2.y = (y+h)*getCurrentTexture().t.getHeight()/getCurrentTexture().h;
        useSubImage = true;
        setTextureCoords();
    }
    
    /**
     * Turns off the use of subimages. The whole texture will be drawn during the render stage of the game. This is the default behaviour.
     */
    public void disableSubImage() {
        subImageOffset.x = 0;
        subImageOffset.y = 0;
        useSubImage = false;
        setTextureCoords();
    }
    
    private void setTextureCoords() {
            
        if (useSubImage) {
            tc = getCurrentTexture().t.getSubImageTexCoords(subImage1.x, subImage1.y, subImage2.x, subImage2.y) ;
            
            imgDim.x = (subImage2.x  - subImage1.x) * getCurrentTexture().w / getCurrentTexture().t.getWidth();
            imgDim.y = (subImage2.y  - subImage1.y) * getCurrentTexture().h / getCurrentTexture().t.getHeight();
        }
        else {
            tc = getCurrentTexture().t.getImageTexCoords() ;
            
            imgDim.x = getCurrentTexture().w;
            imgDim.y = getCurrentTexture().h;
        }
            
//         System.out.println("WTF\t"+tc.left()+"\t"+tc.right()+"\t"+tc.top()+"\t"+tc.bottom()+"\t:\t"+imgDim.x+" : "+imgDim.y); 
//         System.out.println("---\t"+getCurrentTexture().t.getWidth() +":"+getCurrentTexture().t.getHeight()+"\t"+getCurrentTexture().w +":"+getCurrentTexture().h);

    }
    //==================================================================================================

    void draw(GL gl, float offsetx, float offsety, float r, float g, float b, float a,  float depth) {

        gl.glColor4f( r, g, b, a );
        if (activeTexture != -1) {
            internalDraw (gl, offsetx, offsety, depth);
        }
    }
    
    void draw(GL gl, float offsetx, float offsety, float depth) {
        
        if (activeTexture != -1) {
            internalDraw (gl, offsetx, offsety, depth);
        }
    }
    
    private void internalDraw (GL gl, float offsetx, float offsety, float depth) {
    	 gl.glPushMatrix();
         gl.glTranslatef(offsetx,offsety, 0);
         gl.glTranslatef(position.x,position.y, 0);
         gl.glTranslatef(-getCurrentCenter().x,-getCurrentCenter().y, 0);
         
         getCurrentTexture().t.enable();
         getCurrentTexture().t.bind();
         
         gl.glBegin(GL.GL_QUADS);
         {
             gl.glTexCoord2f(tc.left(),  tc.bottom());  gl.glVertex3f(0,        0,        depth);
             gl.glTexCoord2f(tc.right(), tc.bottom());  gl.glVertex3f(imgDim.x, 0,        depth);
             gl.glTexCoord2f(tc.right(), tc.top());     gl.glVertex3f(imgDim.x, imgDim.y, depth);
             gl.glTexCoord2f(tc.left(),  tc.top());     gl.glVertex3f(0,        imgDim.y, depth);
         }
         gl.glEnd();
         getCurrentTexture().t.disable();
         
         gl.glPopMatrix();
    }
    
    //==================================================================================================
    
    /**
     * This should be called once per object per frame and should be over-ridden for more specific behaviour
     */
    public void doTimeStep() {
    }
}