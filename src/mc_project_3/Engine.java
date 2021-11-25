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

import org.lwjgl.input.Mouse;
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
        int teleportDist = 7; // how far player can teleport initially
        int blockPlacementDist = 3; //how far block can be placed from player
        
        float gameTime = 1.7f; //0-7 = morning
                            //8-32 = day
                            //33-40 = dawn
                            //40-72 = night
        
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        long lastTime = 0;
        long time = 0;
        float mouseSensitivityY = 0.09f;
        
        float movementSpeed = 1.35f;
        float movementSpeedCorrected = movementSpeed;
        Mouse.setGrabbed(true);
        
        int rebuildCounter = 0;
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
            if (Mouse.isButtonDown(0)) player1.teleport(teleportDist);// left click for teleporting
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) teleportDist++; //up and down for teleport distance changing
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && teleportDist > 0) teleportDist--;
            if (Mouse.isButtonDown(1)) player1.placeBlock(blockPlacementDist);//right click for placing block
                
                
            
            
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
            if(lastTime < time) {
                fps = (10000/(time-lastTime))/10.0f;
                avargeFPS += fps;
                avargeFPS /= 2;
                fps = (Math.round(avargeFPS * 10.0f) / 10.0f);
                if(Config.showFPS) System.out.println(fps);
            }
                    
            //update world 1 chunk per cycle;
            rebuildCounter++;
            if(rebuildCounter > map.size()-1) {
                rebuildCounter = 0;
                upDateLighting(gameTime);
            }
            else if(rebuildCounter == map.size()-1) {
                //resetLighting();
            }
            map.get(rebuildCounter).loadMesh();
            
            if(gameTime > 72) gameTime = 0;
            gameTime += (0.001f*60f)/(float)avargeFPS;
            
            float skyLight;
            if(gameTime < 33) {
                skyLight = gameTime;
                if (skyLight > 8) gameTime = 8;
            }
            else if(gameTime > 40) {
                skyLight = 0;
            }
            else {
                skyLight = 8 - (gameTime-33);
            }
            glClearColor(0.5f*(skyLight/8),0.75f*(skyLight/8),1.0f*(skyLight/8),0.0f);
                    
            
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
        
        
        int worldSizeX = 10;
        int worldSizeY = 10;
        int tots = 0;
        int maxTots = worldSizeX*worldSizeY;
        for(int x = 0; x < worldSizeX; x++) {
            for(int y = 0; y < worldSizeY; y++) {
                Chunks c = new Chunks(x, y, (16*-x),0,(16*-y), sBig, nBig, nSmall);
                map.add(c);
                System.out.println("Initilizing: " + ++tots +" / "+ (maxTots));
            }
        }
        
        
        map.parallelStream().forEach(c -> {c.generateChunk(); System.out.println("Generated Chunk [" + c.getGlobalX() + "," + c.getGlobalY() + "]");});

        tots = 0;
        for(int x = 0; x < map.size(); x++) {
            map.get(x).rebuildMesh();
            map.get(x).initRender();
            System.out.println("Building: " + ++tots +" / "+ (maxTots));
        }

        
        
    }
    
    //method: resetLighting()
    //purpose: sets all blocks' brightness to 0
    private static void resetLighting() {
        map.parallelStream().forEach(c -> {
            for(int x = 0; x < c.CHUNK_SIZE[0]; x++) {
                for(int z = 0; z < c.CHUNK_SIZE[2]; z++) {
                    for(int y = c.CHUNK_SIZE[1]-1; y > 0; y--) {
                        c.Blocks[x][y][z].setBrightness(0);
                    }
                }
            }
        });
    }
    
    //method: updateLightning
    //purpose: raytrace from the sky to set block brightness
    private static void upDateLighting(float time) {
        //surface lighting from sky, ray cast down
        int skyLight;
        if(time < 33) {
            skyLight = (int)time;
            if (skyLight > 8) skyLight = 8;
        }
        else if(time > 40) {
            skyLight = 0;
        }
        else {
            skyLight = 8 - (int)(time-33);
        }
        final int SKYLIGHT = skyLight;
        
        
        System.out.println(SKYLIGHT + " " + time);
       

        map.parallelStream().forEach(c -> {
            for(int x = 0; x < c.CHUNK_SIZE[0]; x++) {
                for(int z = 0; z < c.CHUNK_SIZE[2]; z++) {
                    for(int y = c.CHUNK_SIZE[1]-1; y > 0; y--) {
                        c.Blocks[x][y][z].setBrightness(SKYLIGHT);
                        if(c.Blocks[x][y][z].GetID() != 0) {
                            break;
                        }
                    }
                }
            }
        });
        /*
        map.parallelStream().forEach(c -> {
            int maxBrightness;
            for(int x = 0; x< c.CHUNK_SIZE[0]; x++) {
                for(int y = 0; y < c.CHUNK_SIZE[1]; y++) {
                    for(int z = 0; z < c.CHUNK_SIZE[2]; z++) {
                        maxBrightness = 0;
                        
                        if(z > 0 && c.Blocks[x][y][z-1].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x][y][z-1].getBrightness();
                        }
                        else if(z < c.CHUNK_SIZE[2]-1 && c.Blocks[x][y][z+1].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x][y][z+1].getBrightness();
                        }//check back

                        else if(y > 0 && c.Blocks[x][y-1][z].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x][y-1][z].getBrightness();
                        }//check bottom

                        else if(y < c.CHUNK_SIZE[1]-1 && c.Blocks[x][y+1][z].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x][y+1][z].getBrightness();
                        }// check top

                        else if(x > 0 && c.Blocks[x-1][y][z].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x-1][y][z].getBrightness();
                        }//check left

                        else if(x < c.CHUNK_SIZE[0]-1 && c.Blocks[x+1][y][z].getBrightness() > maxBrightness) {
                            maxBrightness = c.Blocks[x+1][y][z].getBrightness();
                        }//check right
                        if(c.Blocks[x][y][z].getBrightness() < maxBrightness)c.Blocks[x][y][z].setBrightness(maxBrightness-1);
                    }
                }
            }
        });*/
    }
    
    public static void sendBlockInfo(int x, int y, int z){//gets the chunk where block will be placed and sends to chunk's method
        if (x < 2 && z < 2)
            map.get(0).placeStoneAt(x, y, z);
        else if (x < 2 && z < 34)
            map.get(1).placeStoneAt(x, y, z);
        else if (x < 2 && z < 66)
            map.get(2).placeStoneAt(x, y, z);
        else if (x < 2 && z < 98)
            map.get(3).placeStoneAt(x, y, z);
        else if (x < 2 && z < 130)
            map.get(4).placeStoneAt(x, y, z);
        else if (x < 2 && z < 162)
            map.get(5).placeStoneAt(x, y, z);
        else if (x < 2 && z < 194)
            map.get(6).placeStoneAt(x, y, z);
        else if (x < 2 && z < 226)
            map.get(7).placeStoneAt(x, y, z);
        else if (x < 2 && z < 258)
            map.get(8).placeStoneAt(x, y, z);
        else if (x < 2 && z < 290)
            map.get(9).placeStoneAt(x, y, z);
        
        if (x < 34 && x > 2 && z < 2)
            map.get(10).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 34)
            map.get(11).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 66)
            map.get(12).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 98)
            map.get(13).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 130)
            map.get(14).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 162)
            map.get(15).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 194)
            map.get(16).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 226)
            map.get(17).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 258)
            map.get(18).placeStoneAt(x, y, z);
        else if (x < 34 && x > 2 && z < 290)
            map.get(19).placeStoneAt(x, y, z);
        
        if (x < 66 && x > 34 && z < 2)
            map.get(20).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 34)
            map.get(21).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 66)
            map.get(22).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 98)
            map.get(23).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 130)
            map.get(24).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 162)
            map.get(25).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 194)
            map.get(26).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 226)
            map.get(27).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 258)
            map.get(28).placeStoneAt(x, y, z);
        else if (x < 66 && x > 34 && z < 290)
            map.get(29).placeStoneAt(x, y, z);
        
        if (x < 98 && x > 66 && z < 2)
            map.get(30).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 34)
            map.get(31).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 66)
            map.get(32).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 98)
            map.get(33).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 130)
            map.get(34).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 162)
            map.get(35).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 194)
            map.get(36).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 226)
            map.get(37).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 258)
            map.get(38).placeStoneAt(x, y, z);
        else if (x < 98 && x > 66 && z < 290)
            map.get(39).placeStoneAt(x, y, z);
        
        if (x < 130 && x > 98 && z < 2)
            map.get(40).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 34)
            map.get(41).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 66)
            map.get(42).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 98)
            map.get(43).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 130)
            map.get(44).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 162)
            map.get(45).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 194)
            map.get(46).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 226)
            map.get(47).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 258)
            map.get(48).placeStoneAt(x, y, z);
        else if (x < 130 && x > 98 && z < 290)
            map.get(49).placeStoneAt(x, y, z);
        
        if (x < 162 && x > 130 && z < 2)
            map.get(50).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 34)
            map.get(51).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 66)
            map.get(52).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 98)
            map.get(53).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 130)
            map.get(54).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 162)
            map.get(55).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 194)
            map.get(56).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 226)
            map.get(57).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 258)
            map.get(58).placeStoneAt(x, y, z);
        else if (x < 162 && x > 130 && z < 290)
            map.get(59).placeStoneAt(x, y, z);
        
        if (x < 194 && x > 162 && z < 2)
            map.get(60).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 34)
            map.get(61).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 66)
            map.get(62).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 98)
            map.get(63).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 130)
            map.get(64).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 162)
            map.get(65).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 194)
            map.get(66).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 226)
            map.get(67).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 258)
            map.get(68).placeStoneAt(x, y, z);
        else if (x < 194 && x > 162 && z < 290)
            map.get(69).placeStoneAt(x, y, z);
        
        if (x < 226 && x > 194 && z < 2)
            map.get(70).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 34)
            map.get(71).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 66)
            map.get(72).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 98)
            map.get(73).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 130)
            map.get(74).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 162)
            map.get(75).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 194)
            map.get(76).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 226)
            map.get(77).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 258)
            map.get(78).placeStoneAt(x, y, z);
        else if (x < 226 && x > 194 && z < 290)
            map.get(79).placeStoneAt(x, y, z);
        
        if (x < 258 && x > 226 && z < 2)
            map.get(80).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 34)
            map.get(81).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 66)
            map.get(82).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 98)
            map.get(83).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 130)
            map.get(84).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 162)
            map.get(85).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 194)
            map.get(86).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 226)
            map.get(87).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 258)
            map.get(88).placeStoneAt(x, y, z);
        else if (x < 258 && x > 226 && z < 290)
            map.get(89).placeStoneAt(x, y, z);
        
        if (x < 290 && x > 258 && z < 2)
            map.get(90).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 34)
            map.get(91).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 66)
            map.get(92).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 98)
            map.get(93).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 130)
            map.get(94).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 162)
            map.get(95).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 194)
            map.get(96).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 226)
            map.get(97).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 258)
            map.get(98).placeStoneAt(x, y, z);
        else if (x < 290 && x > 258 && z < 290)
            map.get(99).placeStoneAt(x, y, z);
        
    }
}
