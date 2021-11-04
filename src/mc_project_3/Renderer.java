/*************************************************************** 
 *  file: GameLoop.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: Class that deals with openGL and game graphics
***************************************************************/
package MC_Project_3;

import java.util.List;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;



public class Renderer 
{
    
    
    //method: render
    //purpose: draws stuff
    public static void renderWolrd(List<Chunks> map, Vector3f playerPos) 
    {
        for(Chunks c : map) {
            
            //if(c.getGlobalX()+30 < playerPos.x+1) 
            
            System.out.println(playerPos.x);
            
            int renderDist = 0;
            if(renderDist == 0) {
                c.render();
            }
            else if((c.getGlobalX()+renderDist)*60 > playerPos.x && (c.getGlobalX()-renderDist-1)*60 < playerPos.x) {
                if((c.getGlobalY()+renderDist)*60 > playerPos.z && (c.getGlobalY()-renderDist-1)*60 < playerPos.z) {
                    c.render();
                }
            }
        }
    }
    
    //method: drawCube
    //purpose: Instructions for drawing a cube at x,y,z coordinate
    private static void drawCube(int x, int y, int z, boolean highLight) 
    {
        
        glTranslatef(x,y,z);
        
        glEnable(GL_CULL_FACE);  
        try
        {
            
            if(highLight) 
            {
                glLineWidth(4f);
                
                glBegin(GL_LINE_LOOP);

                glColor3f(1.0f,1.0f,1.0f);
                
                //Top
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);  
                
                //Bottom
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);

                //Front
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);

                //Back
                glVertex3f( 1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f,-1.0f);

                //Right
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);

                //Left
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                
                glEnd();
            }
            
            glBegin(GL_QUADS);
            //Top
            glColor3f(0.0f,0.0f,1.0f);
            glVertex3f( 2.0f, 2.0f,-6.0f);
            glVertex3f(-2.0f, 2.0f,-6.0f);
            glVertex3f(-2.0f, 2.0f, -2.0f);
            glVertex3f( 2.0f, 2.0f, -2.0f);  
            
            //Bottom
            glColor3f(0.0f,1.0f,0.0f);
            glVertex3f( 2.0f,-2.0f, -2.0f);
            glVertex3f(-2.0f,-2.0f, -2.0f);
            glVertex3f(-2.0f,-2.0f,-6.0f);
            glVertex3f( 2.0f,-2.0f,-6.0f);
            
            //Front
            glColor3f(1.0f,0.0f,0.0f);
            glVertex3f( 2.0f, 2.0f, -2.0f);
            glVertex3f(-2.0f, 2.0f, -2.0f);
            glVertex3f(-2.0f,-2.0f, -2.0f);
            glVertex3f( 2.0f,-2.0f, -2.0f);
            
            //Back
            glColor3f(0.0f,1.0f,1.0f);
            glVertex3f( 2.0f,-2.0f,-6.0f);
            glVertex3f(-2.0f,-2.0f,-6.0f);
            glVertex3f(-2.0f, 2.0f,-6.0f);
            glVertex3f( 2.0f, 2.0f,-6.0f);
            
            //Right
            glColor3f(1.0f,0.0f,1.0f);
            glVertex3f( 2.0f, 2.0f,-6.0f);
            glVertex3f( 2.0f, 2.0f, -2.0f);
            glVertex3f( 2.0f,-2.0f, -2.0f);
            glVertex3f( 2.0f,-2.0f,-6.0f);
            
            //Left
            glColor3f(1.0f,1.0f,0.0f);
            glVertex3f(-2.0f, 2.0f, -2.0f);
            glVertex3f(-2.0f, 2.0f,-6.0f);
            glVertex3f(-2.0f,-2.0f,-6.0f);
            glVertex3f(-2.0f,-2.0f, -2.0f);
            
            
            glEnd();
            
            
        } catch(Exception e) {
            
        }
    }
}
