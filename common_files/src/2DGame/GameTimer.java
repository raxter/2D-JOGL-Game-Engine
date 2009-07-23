package GameEngine;


import javax.media.opengl.*;

import GameEngine.Game;

class GameTimer extends Thread
{
    //==============================================================================
    
    private boolean running;
    private GLAutoDrawable drawer;
    private Game game;
    
    //==============================================================================
    
    public GameTimer (Game g, GLAutoDrawable d) {
        game = g;
        drawer = d;
    }
    
    //==============================================================================
    
    public void run() {
        
//         GLContext.setCurrent(drawer.getContext());
        
        final long TICKS_PER_SECOND = game.game_frames_per_second;
        final long SKIP_TICKS = 1000000000L / TICKS_PER_SECOND;
        final int MAX_FRAMESKIP = 15;
        long next_game_tick = System.nanoTime();
        int loops;
        //float interpolation;
        
        running = true;
        while (running) {
            
            loops = 0;
//             System.out.println(System.nanoTime()+":"+next_game_tick+":"+SKIP_TICKS);
            while( System.nanoTime() > next_game_tick && loops < MAX_FRAMESKIP) {
                game.logicGame();
                
//                 System.out.println("logicStep");

                next_game_tick += SKIP_TICKS;
                loops++;
            }

//             interpolation = float( GetTickCount() + SKIP_TICKS - next_game_tick )/ float( SKIP_TICKS );
//             display_game( interpolation );
            
            drawer.display();
        }
        
        //cleanup
        System.exit(0);
    }
    
    //==============================================================================
    
    public void endGame() {
        System.out.println("GameTimer.endGame() called");
        running = false;
    }
    
    //==============================================================================
}