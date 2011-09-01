package GameEngine;

import com.sun.opengl.util.texture.*;
import java.nio.*;

/**
 * A storage place to hold a game texture. Load using ResourceLoader.loadTexture(). Don't load the same texture twice as this will use twice the memory needed.
 * Rather assign the same texture to a different GameObject then load it twice.
 * <p>
 * There is also ability to access the texture's byte data. This will allow you to perform bit-to-bit test using the alpha channel. Please note that byte data is stored such that the start of the array is the bottom left corner of the texture and the end is the top right with lines scanning left to right as normal for image data (so it's a normal representation but upside down). Take this into account when doing the tests.
 *
 * @author Richard Baxter
 *
 */
public class GameTexture {
    Texture t;
    int w, h;
    ByteBuffer bbuffer;
    
    protected GameTexture() {}

    /** Disposes the texture in the proper GL fashion. This should be called automatically upon cleanup of the java program and need not be called directly.
    */
    public void finalize() {
    	t.dispose();
    }
    
    /**
    * Holds the Byte array of this texture in RGBA format. Each byte represents either a red, blue, green or alpha channel. 
    * Each 4 bytes represtents a RGBA pixel colour value. Hence the ByteBuffer will have length width*height*4
    * @return A Read-Only ByteBuffer containing pixel data associated to the texture
    */
    public ByteBuffer getByteBuffer () {
      
        return bbuffer;
    }
    /**
    * Holds the Integer array of this texture in RGBA format. Each integer (32 bits) represents one RGBA pixel colour value. Hence the ByteBuffer will have length width*height
    * @return A Read-Only IntBuffer containing pixel data associated to the texture
    */
    public IntBuffer getIntBuffer () {
      
        return bbuffer.asIntBuffer();
    }
    
    /**
     * Gets the width of this texture
     * 
     * @return the width of this texture
     */
    public int getWidth() {
        return w;
    }
    /**
     * Gets the height of this texture
     * 
     * @return the height of this texture
     */
    public int getHeight() {
        return h;
    }
    
}
