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
    private float pitch1 = -45.0f; //used for calculations
    
    
   
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
        if (yaw > 360)
            yaw = yaw - 360;
        else if (yaw < 0)
            yaw = yaw + 360;
    }
    
    //method: pitch
    //purpose: sets the player pitch
    public void pitch(float distance) 
    {
        pitch -= distance;
        pitch1 += distance;
        
        if (pitch1 > 360)
            pitch1 = pitch1 - 360;
        else if (pitch1 < 0)
            pitch1 = pitch1 + 360;
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
        //if (position.y != )
            position.y-= distance; 
    }
    
    //method: moveDown
    //purpose: moves the player down X distance
    public void moveDown(float distance)
    {
        //if (position.y != )
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
    
    public void teleport(int dist){//allows user to teleport in direction of camera
        float yDiff, hyp, pitchNum = pitch1;
        
        yDiff = dist * (float) Math.sin(Math.toRadians(pitchNum));
        hyp = dist * (float) Math.cos(Math.toRadians(pitchNum));
        
        walkForward(hyp);// sending horizontal distance to walk forward method
        if (yDiff < 0)//sending vertical distance to move up/down method
            moveDown(-yDiff);
        else if (yDiff > 0)
            moveUp(yDiff);
        
    }
    
    public void placeBlock(int dist){//places block in front of player
        float blockPosX, blockPosY, blockPosZ, yDiff, hyp, yawNum = yaw, pitchNum = pitch1;
        
        yDiff = dist * (float) Math.sin(Math.toRadians(pitchNum));
        hyp = dist * (float) Math.cos(Math.toRadians(pitchNum));
        
        float xOffset= hyp * (float)Math.sin(Math.toRadians(yawNum));
        float zOffset= hyp * (float)Math.cos(Math.toRadians(yawNum));
        
        blockPosX = position.x - xOffset;// calculates X for block
        blockPosZ = position.z + zOffset;// calculates z for block
        if (yDiff < 0)// calculates y for block
            blockPosY = position.y - yDiff; 
        else if (yDiff > 0)
            blockPosY = position.y + yDiff; 
        else
            blockPosY = position.y;
        
        //rounding to nearest whole number
        if (blockPosX % 2 > 1)
            blockPosX = (float) Math.floor(blockPosX);
        else 
            blockPosX = (float) Math.ceil(blockPosX);
        if (blockPosY % 2 > 1)
            blockPosY = (float) Math.floor(blockPosY);
        else 
            blockPosY = (float) Math.ceil(blockPosY);
        if (blockPosZ % 2 > 1)
            blockPosZ = (float) Math.floor(blockPosZ);
        else 
            blockPosZ = (float) Math.ceil(blockPosZ);
        
        //sends block position
        Engine.sendBlockInfo((int) blockPosX, (int) blockPosY, (int) blockPosZ);

    }
    
    public float getPosX(){
        return position.x;
    }
    public float getPosY(){
        return position.y;
    }
    public float getPosZ(){
        return position.z;
    }
    
    public Vector3f getPos() {
        return position;
    }
}
