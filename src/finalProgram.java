/**
 *
 * @author ed144
 */
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class finalProgram {
    
    private FPCameraController fp = new FPCameraController(0f,0f,0f);
    private DisplayMode displayMode;
    public void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        
        for (int i = 0; i < d.length; i++){
            if (d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32){
                displayMode = d[i];
                break;
            }
        }
        
        Display.setDisplayMode(displayMode);
        Display.setTitle("Final Program");
        Display.create();
        
    }
    private void render(){
        
        //while(!Display.isCloseRequested()){
            while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();

                glColor3f(0.0f, 1.0f, 0.0f);
                glPointSize(1);

                
                    glBegin(GL_POINTS);
                        
                    glEnd();


                Display.update();
                Display.sync(60);
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
        //}
        }
        Display.destroy();
    }
    

    private void initGL(){
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        //glOrtho(0,640,0,480,1,-1);
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float) displayMode.getHeight(), 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    public void start(){
        try{
            createWindow();
            initGL();
            fp.gameLoop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        finalProgram test = new finalProgram();
        test.start();

    }   
}