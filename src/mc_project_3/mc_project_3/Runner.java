/*************************************************************** 
 *  file: Runner.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 2
 *  date last modified: 8/24/2021
 *  
 *  purpose: Init OpenGL window and loads config values (keybinds, window size etc)
***************************************************************/

package MC_Project_3;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Runner 
{
    
    private DisplayMode displayMode;
    
    //method: start
    //purpose: Initilize default values and creates a window
    public void start() 
    {
        
        if(Config.doesCustomProfileExist("custom.xml")) {
            Config.populate("custom.xml");
        }
        else {
            Config.populate("default.xml");
        }

        try {
            createWindow();
            initGL();
            
            TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(System.getProperty("user.dir")+"\\GameData\\Assets\\Textures\\Blocks\\"+Config.blockTexture));
            
            Engine.run();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    //method: createWindow
    //purpose: Code for creating an openGL window
    public void createWindow() throws Exception 
    {
        Display.setFullscreen(false);
        
        DisplayMode d[] = Display.getAvailableDisplayModes();
        displayMode = d[0];
        
        for(int i = 0; i < d.length; i++) {
            if(d[i].getWidth() == Config.width && d[i].getHeight() == Config.height && d[i].getBitsPerPixel() == Config.bitdepth) {
                displayMode = d[i];
                break;
            }
        }
        
        Display.setDisplayMode(displayMode); 
        Display.setTitle(Config.title);
        Display.create();
    }
    
    //method: initGL
    //purpose: Starts OpenGL
    private void initGL() 
    {
        glClearColor(0.5f,0.75f,1.0f,0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float) displayMode.getHeight(), 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glFrontFace(GL_CW);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }
    
    //method: main
    //purpose: Main Method, runs the program
    public static void main(String[] args) 
    {
        
        Runner basic = new Runner(); 
        basic.start();
        
        
        Config.save("custom.xml");
    }
}
