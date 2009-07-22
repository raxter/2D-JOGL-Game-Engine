import java.util.Vector;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;
import java.nio.*;

import game.Game;
import game.GameTexture;
import game.GameFont;
import game.GameObject;

public class Answer {

//     public static Point2D.Float [] points = new Point2D.Float [0];
    
    public static boolean fine_tune_collision(GameObject o1, GameObject o2) {
        
        ByteBuffer bb1 = o1.getCurrentTexture().getByteBuffer();
        ByteBuffer bb2 = o2.getCurrentTexture().getByteBuffer();
        int w1 = o1.getCurrentTexture().getWidth();
        int w2 = o2.getCurrentTexture().getWidth();
        int h1 = o1.getCurrentTexture().getHeight();
        int h2 = o2.getCurrentTexture().getHeight();
        Rectangle r1 = o1.getIntAABoundingBox();
        Rectangle r2 = o2.getIntAABoundingBox();
        Point subimageoffset1 = o1.getSubImageOffset();
        Point subimageoffset2 = o2.getSubImageOffset();
        
        Rectangle inter = r2.intersection(r1);
//         System.out.println("r1: " + r1);
//         System.out.println("r2: " + r2);
//         System.out.println("in: " + inter);
        boolean collided = false;
        
        int r1bx = subimageoffset1.x + inter.x - r1.x;
        int r1by = subimageoffset1.y + inter.y - r1.y;
        int r2bx = subimageoffset2.x + inter.x - r2.x;
        int r2by = subimageoffset2.y + inter.y - r2.y;
        
//         Vector<Point2D.Float> vec = new Vector<Point2D.Float>();
        
//         System.out.println("/------------------------\\");
        outer : 
        for (int i = 0 ; i < inter.height ; i++) {
            for (int j = 0 ; j < inter.width ; j++) {
                
                int b1 = 0;
//                 if (((j - inter.x) < r1.width) && ((i - inter.y) < r1.height))
                b1 = (getAlphaAt(bb1, w1, h1, r1bx+j, r1by+i) == 0 ? 0 : 1);
                
                int b2 = 0;
//                 if (((j - inter.x) < r2.width) && ((i - inter.y) < r2.height))
                b2 = (getAlphaAt(bb2, w2, h2, r2bx+j, r2by+i) == 0 ? 0 : 1);
                
//                 System.out.print(""+ (r2bx+j) +":"+ (r2by+i));
//                 System.out.print(""+ ((b1 == 1) && (b2 == 1) ? "X" : (b1 == b2 ? " " : (b1 == 1? "-" : "'"))) +" ");
                if (b1 == 1 && b2 == 1) {
                    collided = true;
                    break outer;
//                     vec.add(new Point2D.Float(j+inter.x, i+inter.y));
                }
            }
//             System.out.println(">");
        }
//         System.out.println("\\------------------------/");
        
//         points = new Point2D.Float[vec.size()];
//         vec.toArray(points);
        
//         collided = false;
        return collided;
    
    }

    public static byte getAlphaAt(ByteBuffer b, int w, int h, int x, int y) {
//         System.out.println(""+b + ":"+w+":"+h+":"+x+":"+y);
        int index = (4*(x+((h-y)*w)))+3;
        return (index < w*h*4 ? b.get(index) : 0);
    }
}
