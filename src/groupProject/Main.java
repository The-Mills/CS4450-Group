package groupProject;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

public class Main
{     
    public void start()
    {
        try
        {
            createWindow();
            initGL();
            render();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception
    {
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Group Project");
        Display.create();
    }
    

    private void initGL()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glOrtho(0, 640, 0, 480, 1, -1);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    private void render()
    {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            try
            {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                
                glColor3f(1.0f, 1.0f, 0.0f);
                glPointSize(1);
                
                glBegin(GL_POINTS);
                glEnd();
                
                Display.update();
                Display.sync(60);
            }
            catch(Exception e){}
        }
        Display.destroy();
    }
    
    public static void main(String[] args)
    {   
        Main window = new Main();
        window.start();
    }
}