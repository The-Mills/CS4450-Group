/*************************************************************** 
 *  file: Config.java
 *  author: Kevin Kongwattanachai
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: Stores user changeable data 
***************************************************************/

package MC_Project_3;

import org.lwjgl.input.Keyboard;

/**
 *
 * @author Kevin
 */
public class Config {
    
    //opengl - window
    public static String title;
    public static int width;
    public static int height;
    public static int bitdepth;
    
    //engine
    public static int refresh;
    
    //controlls - movment
    public static int forward;
    public static int backward;
    public static int left;
    public static int right;
    public static int up;
    public static int down;
    
    //controlls - sensitivity
    public static float pitch;
    public static float yaw;
    
    public static void populate(String xmlName) {
        
        String xmlLoc = System.getProperty("user.dir")+"\\"+xmlName;
        
        //opengl - window
        title = FileIO.parseStr(xmlLoc, "config/opengl/window/title");
        width = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/width");
        height = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/height");
        bitdepth = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/bitdepth");
        
        //engine
        refresh = (int)FileIO.parseNum(xmlLoc, "config/engine/refresh");
        
        //controlls - movment
        forward = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/forward"));
        backward = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/backward"));
        left = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/left"));
        right = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/right"));
        up = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/up"));
        down = Keyboard.getKeyIndex(FileIO.parseStr(xmlLoc, "config/controlls/movement/down"));
        
        //controlls - sensitivity
        pitch = (float)FileIO.parseNum(xmlLoc, "config/controlls/sensitivity/pitch");
        yaw = (float)FileIO.parseNum(xmlLoc, "config/controlls/sensitivity/yaw");
    }
    
    
}
