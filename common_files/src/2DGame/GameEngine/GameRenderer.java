package GameEngine;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;


import com.sun.opengl.util.j2d.*;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;


class GameRenderer implements GLEventListener
{
  class FontStack {
    Stack<FontInfo> stack;
    GameFont gFont;
    TextRenderer tr;
        
    FontStack (GameFont gf) {
      stack = new Stack<FontInfo>();
      tr = new TextRenderer(gf.font);
      gFont = gf;
    }
  }
    
  class FontInfo {
   
    FontInfo (String text, float x, float y, float r, float g, float b, float a, float depth, float scale) {
            
      this.text = text;
      this.x = x;
      this.y = y;
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
      this.depth = depth;
      this.scale = scale;
    }
            
    String text;
    float x;
    float y;
    float r;
    float g;
    float b;
    float a;
    float depth;
    float scale;
        
  }
    
  class FontRenderer {
        
    Vector<FontStack> textRenderers = new Vector<FontStack>();
        
    GameFont addGameFont(Font f) {
      GameFont gf = new GameFont(f, textRenderers.size());
      textRenderers.add(new FontStack(gf));
//             System.out.println(textRenderers.elementAt(textRenderers.size()-1).stack);
      return gf;
    }
        
    void registerFontRender(GameFont gf, String text, float x, float y, float r, float g, float b, float a, float depth, float scale) {
      textRenderers.elementAt(gf.index).stack.add(new FontInfo(text, x, y, r, g, b, a, depth, scale));
    }
        
        
    Rectangle2D.Float getBounds(String s, GameFont gf) {
      return (Rectangle2D.Float)textRenderers.elementAt(gf.index).tr.getBounds(s);
    }
        
        /* need to fix this up, i made the whole stack system so i would reduce the number of calls to 
    * fs.tr.begin3DRendering(); and fs.tr.end3DRendering();, however I've discovered that you can't 
    * change colour more then once inside of those two calls so now they are being called as many times as if I 
    * hadn't done the whole stack system... fail
        */
    void processFontDraws (GL gl) {
      for (FontStack fs : textRenderers) {
        while(!fs.stack.isEmpty()) {
          FontInfo fi = fs.stack.pop();
                    
                    //failfailfailfailfailfailfailfailfailfailfailfailfailfailfail >.<
          fs.tr.begin3DRendering();
          gl.glColor4f(fi.r, fi.g, fi.b, fi.a);
          fs.tr.draw3D(fi.text, fi.x, fi.y, fi.depth, fi.scale);
          fs.tr.end3DRendering();
        }
      }
    }
//         void processFontDraws (GL gl) {
//             int i = 0;
//             textRenderers.elementAt(i).begin3DRendering();
//             while(!fontQ.isEmpty()) {
//                 FontInfo fi = fontQ.poll();
//                 System.out.println(fi.fontsize+"  "+ textRenderers.elementAt(i).getFont().getSize());
//                 //Display
//                 // going to redo anyway
//                 while (textRenderers.elementAt(i).getFont().getSize() != fi.fontsize) {
//                     i++;
//                     if ((i >= textRenderers.size() && !fontQ.isEmpty()) || (i < textRenderers.size() && textRenderers.elementAt(i).getFont().getSize() != fi.fontsize)) {
//                         //error, invalid size
//                         System.out.println("ERROR: FontRenderer, invalid size used ("+fi.fontsize+")");
//                         i--;
//                         break;
//                     }
//                     textRenderers.elementAt(i-1).end3DRendering();
//                     textRenderers.elementAt(i).begin3DRendering();
//                 }
//                 gl.glColor4f(fi.r, fi.g, fi.b, fi.a);
//                 textRenderers.elementAt(i).draw3D(fi.text, fi.x, fi.y, fi.depth, 1.0f);
//             }
//             textRenderers.elementAt(i).end3DRendering();
//             System.out.println("--------");
//         }
  }
    
    //==============================================================================
    
  Game game;
  FontRenderer f;

  Dimension viewPortDimension = new Dimension(1,1);
    //==============================================================================
    
  public GameRenderer (Game g) {
    game = g;
        
    f = new FontRenderer();
    g.fr = f; // game.FontRenderer = f;
  }
    
    //==============================================================================
    
  public void display(GLAutoDrawable drawable) {
    GL gl = drawable.getGL();
    gl.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        
    game.renderGame(gl);
        
    f.processFontDraws (gl);
  }
    
  public void callLogic() {
        
    game.logicGame();
  }
    //==============================================================================
    
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
  }
    
    //==============================================================================
    
  public void init(GLAutoDrawable drawable) {
    System.out.println("GameRenderer.init() called");
    GL gl = drawable.getGL(); 
    gl.glEnable(GL.GL_DEPTH_TEST);
        
//         gl.setSwapInterval(1);// seems to be useful...
        
    game.initGame(gl, f);
  }
    
    //==============================================================================
    
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)  {
    System.out.println("GameRenderer.reshape("+x+", "+y+", "+width+", "+height+") called");
	game.setViewPortDimension(new Dimension(width, height));
    GL gl = drawable.getGL(); 
    GLU glu = new GLU(); 
        
    gl.glViewport( 0, 0, width, height ); 
    gl.glMatrixMode( GL.GL_PROJECTION );
    gl.glLoadIdentity();
    glu.gluOrtho2D( 0.0, width, 0.0, height);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }
    
    //==============================================================================
}