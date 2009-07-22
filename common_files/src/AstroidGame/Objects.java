import game.GameTexture;
import game.PhysicalObject;

class PlayerObject extends PhysicalObject {
    
    
    private float direction = 0;
    
    //==================================================================================================
    public PlayerObject (float x, float y, float m) {
        super (x, y, m);
    }
    
    //==================================================================================================
    
    public void incrementDirection(float inc) {
        setDirection(direction + inc);
    }
    
    public void setDirection(float dir) {
        while (dir < 0.0) dir += 360.0;
        while (dir >= 360.0) dir -= 360.0;
        direction = dir;
        
        setActiveTexture((int)((float)((direction)+0.5)/360.0*72.0));
    }
    
    public float getDirection() {
        return direction;
    }
    
    //==================================================================================================
    
    public void applyThust(float force) {
        applyForceInDirection(direction, force);
    }
}

class AsteroidObject extends PhysicalObject {
    private int picNum = 0;
    private int picRow = 0;
    private int timeBreak = 0;
    private int timeSpeed = 5;

    private int pictureScale = 1;
    
    public AsteroidObject (float x, float y, float m, GameTexture gt) {
        super (x, y, m);
        
        addTexture(gt, 0, 0);
        picNum = (int)(Math.random()*100f)%8;
        picRow = (int)(Math.random()*100f)%8;
        picRow = (int)(Math.random()*100f)%5+3;
    }

    public int getPictureScale() {
        return pictureScale;
    }

    public void setPictureScale(int scale) {
        pictureScale = scale;
        incPicture();
    }
    
	private void incPicture() {
        
        timeBreak ++;
        timeBreak%=timeSpeed;
        
        if (timeBreak == 0)
            picNum ++;
        
        if (picNum == 8) {
            picNum = 0;
            picRow++;
        }
        
        picRow%=8;
        setSubImage((64*picNum)/pictureScale,(64*(7-picRow)+8)/pictureScale,64/pictureScale, 64/pictureScale, 32/pictureScale ,32/pictureScale);
	}
	
    public void doTimeStep() {
        incPicture();
        super.doTimeStep();
    }
}

class BulletObject extends PhysicalObject {
    private int destroyTimer = 0;
    
    public BulletObject (float x, float y, float m, int time, GameTexture bt) {
        super (x, y, m);
        setDestroyTimer(time);
        addTexture(bt);
    }
    
    public void setDestroyTimer(int time) {
        destroyTimer = time;
    }
    
    public void doTimeStep() {
        destroyTimer--;
        if (destroyTimer == 0)
            setMarkedForDestruction(true);
        
        super.doTimeStep();
    }
}