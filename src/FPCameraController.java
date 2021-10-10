/**
 *
 * @author ed144
 */
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController {
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    
    public FPCameraController(float x, float y, float z){
        position = new Vector3f(x,y,z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }
    
    public void yaw(float amount){
        yaw += amount;
    }
    public void pitch (float amount){
        pitch -= amount;
    }
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;position.z+= zOffset;
    }
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    public void moveUp(float distance){
        position.y -= distance;
    }
    public void moveDown(float distance){
        position.y += distance;
    }
    public void lookThrough(){
        //roatatethe pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatatethe yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }
    public void gameLoop(){
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;//hide the mouse
        Mouse.setGrabbed(true);
        
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            time = Sys.getTime();
            lastTime = time;
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
                camera.walkForward(movementSpeed);
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
                camera.walkBackwards(movementSpeed);
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left
                camera.strafeLeft(movementSpeed);
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right  
                camera.strafeRight(movementSpeed);
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up     
                camera.moveUp(movementSpeed);
            if (Keyboard.isKeyDown(Keyboard.KEY_E))
                camera.moveDown(movementSpeed);
            
            //set the modelviewmatrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            render();
            //draw the buffer to the screen 
            Display.update();
            Display.sync(60);
        
        }
    }
    
    
    private void render(){
        try{
            glBegin(GL_QUADS);
            glColor3f(1.0f,0.0f,1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f); 
            glEnd();
        }catch(Exception e){
        }
    }       

}
