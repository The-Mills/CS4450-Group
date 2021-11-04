/*************************************************************** 
 *  file: GameLoop.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 11/1/2021
 *  
 *  purpose: This is where the game's logic take place
***************************************************************/

package MC_Project_3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
/**
 *
 * @author Kevin/**
n
 */
public class Engine 
{
    
    private static List<Chunks> map = new ArrayList<Chunks>(); //List of lists

    //method: run
    //purpose: sets the player coordinates to 0,0,0 and starts game logic, also starts graphics loop
    public static void run() 
    {
        Player player1 = new Player(0,-120,0);

        //generate world
        long sysTime = System.currentTimeMillis();
       
        Random rand = new Random(sysTime);
       
        int seed1 = rand.nextInt(Integer.MAX_VALUE-1);
        int seed2 = rand.nextInt(Integer.MAX_VALUE-1);
        
        
        regenrateChunks(seed1, seed2, true, true, true);
        
        
        float fps = Config.refresh;
        float avargeFPS = Config.refresh;
        float minFPS = 0;
        
        
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        long lastTime = 0;
        long time = 0;
        float mouseSensitivityY = 0.09f;
        
        float movementSpeed = 1.35f;
        float movementSpeedCorrected = movementSpeed;
        Mouse.setGrabbed(true);
        
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) 
        {

            
            lastTime = time;
            time = Sys.getTime();
            
            movementSpeedCorrected = (movementSpeed*60f)/(float)avargeFPS;
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            player1.yaw(dx * Config.yaw);
            player1.pitch(dy* Config.pitch);
            if (Keyboard.isKeyDown(Config.forward)) player1.walkForward(movementSpeedCorrected);
            if (Keyboard.isKeyDown(Config.backward)) player1.walkBackwards(movementSpeedCorrected);
            if (Keyboard.isKeyDown(Config.left)) player1.strafeLeft(movementSpeedCorrected);
            if (Keyboard.isKeyDown(Config.right)) player1.strafeRight(movementSpeedCorrected);
            if (Keyboard.isKeyDown(Config.up)) player1.moveUp(movementSpeedCorrected);
            if (Keyboard.isKeyDown(Config.down)) player1.moveDown(movementSpeedCorrected);
            
            while(Keyboard.next()){
                //F1 = regen chunk
                if(Keyboard.getEventKey() == Keyboard.KEY_F1) {
                    if(Keyboard.getEventKeyState()){
                        seed1 = rand.nextInt(Integer.MAX_VALUE-1);
                        seed2 = rand.nextInt(Integer.MAX_VALUE-1);
                        regenrateChunks(seed1, seed2, true, true, true);
                    }
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_F2) {
                    if(Keyboard.getEventKeyState()){
                        regenrateChunks(seed1, seed2, true, true, true);
                    }
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_F3) {
                    if(Keyboard.getEventKeyState()){
                        regenrateChunks(seed1, seed2, false, false, true);
                    }
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_F4) {
                    if(Keyboard.getEventKeyState()){
                        regenrateChunks(seed1, seed2, false, true, false);
                    }
                }
                if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
                    if(Keyboard.getEventKeyState()){
                        regenrateChunks(seed1, seed2, true, false, false);
                    }
                }

            }

            
            //fps calculations
            if(Config.showFPS && lastTime < time) {
                fps = (10000/(time-lastTime))/10.0f;
                avargeFPS += fps;
                avargeFPS /= 2;
                fps = (Math.round(avargeFPS * 10.0f) / 10.0f);
                System.out.println(fps);
            }
                    
            
            //set modelview matrix back to idenity
            glLoadIdentity();
            //look thru camera before drawing
            player1.lookThrugh();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            Renderer.renderWolrd(map, player1.getPos());
            
            Display.update();
            Display.sync(Config.refresh);
        }
        Display.destroy();
    }
    
    //method: regenerateChunks
    //purpose: deletes all chunks and replaces them with new ones
    private static void regenrateChunks(int seed1, int seed2, boolean useLarge, boolean useBig, boolean useSmall) {
        
        map.clear();
        
        if(seed2 == seed1) seed2 = (int)Math.floor(seed2/2.0) - 1;

        Noise nSmall = new Noise(seed1, .3, 35, 6*(useSmall ? 1 : 0)); //roughens terrain.
        Noise nBig = new Noise(seed2, .35, 1000, 35*(useBig ? 1 : 0)); // hills, oceans, etc.
        
        Noise sBig = new Noise(seed2/2, .4, 120, 250*(useLarge ? 1 : 0)); // hills, oceans, etc.
        
        
        int worldSizeX = 5;
        int worldSizeY = 5;
        int tots = 0;
        int maxTots = worldSizeX*worldSizeY;
        for(int x = 0; x < worldSizeX; x++) {
            for(int y = 0; y < worldSizeY; y++) {
                Chunks c = new Chunks(x, y, (30*-x),0,(30*-y), sBig, nBig, nSmall);
                map.add(c);
                System.out.println("Initilizing: " + ++tots +" / "+ (maxTots));
            }
        }
        
        
        map.parallelStream().forEach(c -> {c.generateChunk(); System.out.println("Generated Chunk [" + c.getGlobalX() + "," + c.getGlobalY() + "]");});

        tots = 0;
        for(int x = 0; x < map.size(); x++) {
            map.get(x).rebuildMesh();
            System.out.println("Building: " + ++tots +" / "+ (maxTots));
        }

        
        
    }
    
}
