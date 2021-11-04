/*************************************************************** 
 *  file: GameLoop.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: Holds Player object and it's position and camera. 
***************************************************************/
package MC_Project_3;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Kevin
 */
public class Player 
{
    private Vector3f position = null;
    private Vector3f lastPos = null;
    
    private float yaw = 0.0f; //rotation around Y axis
    private float pitch = 45.0f; //rotation around x axis 
    
    //method: Player
    //purpose: Init Player object
    public Player (float x, float y, float z) 
    {
        position = new Vector3f(x, y, z);
        lastPos= new Vector3f(x,y,z);
    }
    
    //method: yaw
    //purpose: sets the player yaw
    public void yaw(float distance) 
    {
        yaw += distance;
    }
    
    //method: pitch
    //purpose: sets the player pitch
    public void pitch(float distance) 
    {
        pitch -= distance;
    }
    
    //method: walkForward
    //purpose: moves the player forward X distance
    public void walkForward(float distance) 
    {
        float xOffset= distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset= distance * (float)Math.cos(Math.toRadians(yaw));
        position.x-= xOffset;position.z+= zOffset;
    }
    
    //method: walkBackwards
    //purpose: moves the player backwards X distance
    public void walkBackwards(float distance)
    {
        float xOffset= distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset= distance * (float)Math.cos(Math.toRadians(yaw));
        position.x+= xOffset;position.z-= zOffset;
    }
    
    //method: strafeLeft
    //purpose: moves the player left X distance
    public void strafeLeft(float distance)
    {
        float xOffset= distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset= distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x-= xOffset;position.z+= zOffset;
    }
    
    //method: strafeRight
    //purpose: moves the player right X distance
    public void strafeRight(float distance)
    {
        float xOffset= distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset= distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x-= xOffset;position.z+= zOffset;
    }
    
    //method: moveUp
    //purpose: moves the player up X distance
    public void moveUp(float distance)
    {
        position.y-= distance; 
    }
    
    //method: moveDown
    //purpose: moves the player down X distance
    public void moveDown(float distance)
    {
        position.y+= distance;
    }
    
    //method: lookThrugh
    //purpose: sets camera position
    public void lookThrugh() 
    {
        //rotate pitch around X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate vect loc
        glTranslatef(position.x, position.y, position.z);
    }
    
    public Vector3f getPos() {
        return position;
    }
}
