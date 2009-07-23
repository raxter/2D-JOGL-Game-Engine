package GameEngine;

import java.awt.event.*;


class GameInterface implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener 
{
    protected Game game;
    
    public GameInterface(Game g) {
        game = g;
    }
    
    //------------------------------------------
    
    public void keyPressed(KeyEvent e) {
        game.registerKeyDown(e.getKeyCode(), true);
    }
    public void keyReleased(KeyEvent e) {
        game.registerKeyDown(e.getKeyCode(), false);
    }
    public void keyTyped(KeyEvent e) {
        game.registerKeyTyped(e.getKeyCode());
    }
    
    //------------------------------------------
    
    public void mouseEntered(MouseEvent e) {
        game.mouseInWindow = false;
    }
    public void mouseExited(MouseEvent e) {
        game.mouseInWindow = true;
    }
    public void mousePressed(MouseEvent e) {
        game.updateMousePos(e.getX(), e.getY());
        
        game.registerMouseDown(e.getButton(), true);
    }
    public void mouseReleased(MouseEvent e) {
        game.updateMousePos(e.getX(), e.getY());
        
        game.registerMouseDown(e.getButton(), false);
    }
    public void mouseClicked(MouseEvent e) {
        game.registerMouseClicked(e.getButton());
    }
    
    //------------------------------------------
    
    public void mouseDragged(MouseEvent e) {
        game.updateMousePos(e.getX(), e.getY());
        game.registerMouseDragged();
    }
    public void mouseMoved(MouseEvent e) {
        game.updateMousePos(e.getX(), e.getY());
        game.registerMouseMoved();
    }
    
    //------------------------------------------
    
    public void mouseWheelMoved(MouseWheelEvent e) {
        game.registerScrollAmount(e.getScrollAmount());
        game.registerScrollType(e.getScrollType());
        game.registerUnitsToScroll(e.getUnitsToScroll());
        game.registerWheelRotation(e.getWheelRotation());
    }
	
    //------------------------------------------
    
}