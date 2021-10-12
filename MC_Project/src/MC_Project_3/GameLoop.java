/*************************************************************** 
 *  file: GameLoop.java
 *  author: Kevin Kongwattanachai
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: This is where the game's logic take place
***************************************************************/

package MC_Project_3;
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
public class GameLoop {
    
    //method: run
    //purpose: sets the player coordinates to 0,0,0 and starts game logic, also starts graphics loop
    public static void run() {
        Player player1 = new Player(0,0,0);
        
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivityY = 0.09f;
        
        float movementSpeed = 0.35f;
        Mouse.setGrabbed(true);
        
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            player1.yaw(dx * Config.yaw);
            player1.pitch(dy* Config.pitch);
            if (Keyboard.isKeyDown(Config.forward)) player1.walkForward(movementSpeed);
            if (Keyboard.isKeyDown(Config.backward)) player1.walkBackwards(movementSpeed);
            if (Keyboard.isKeyDown(Config.left)) player1.strafeLeft(movementSpeed);
            if (Keyboard.isKeyDown(Config.right)) player1.strafeRight(movementSpeed);
            if (Keyboard.isKeyDown(Config.up)) player1.moveUp(movementSpeed);
            if (Keyboard.isKeyDown(Config.down)) player1.moveDown(movementSpeed);
            
            
            
            //set modelview matrix back to idenity
            glLoadIdentity();
            //look thru camera before drawing
            player1.lookThrugh();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            RenderLoop.render();
            
            Display.update();
            Display.sync(Config.refresh);
        }
        Display.destroy();
    }
    
    
    
}
