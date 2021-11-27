/*************************************************************** 
 *  file: Config.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: Stores user changeable data 
***************************************************************/

package MC_Project_3;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Kevin
 */
public class Config 
{
    
    //opengl - window
    public static String title;
    public static int width;
    public static int height;
    public static int bitdepth;
    
    //engine
    public static String blockTexture;
    public static int refresh;
    public static boolean showFPS;
    
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
    
    //method:formatFileName
    //returns a file name in the correct format for mac and windows pc
    private static String formatFileName(String xmlName) {
        String xmlLoc = System.getProperty("user.dir");

        if(System.getProperty("os.name").contains("Windows")) {
            xmlLoc += "\\GameData\\Config\\"+xmlName;

        } else if(System.getProperty("os.name").contains("Mac")) {
            xmlLoc += "/GameData/Config/"+xmlName;
        } else {
            Push.outWarn("Unsupported OS Detected");
            xmlLoc += "\\GameData\\Config\\"+xmlName;
        }
        
        return xmlLoc;
    }
    
    // method: populate(String xmlName)
    // purpose: Uses FileIO to read the xml file
    // and store its information in variables.
    public static void populate(String xmlName) 
    {
        
        String xmlLoc = formatFileName(xmlName);
        
        
        
        
        
        //opengl - window
        title = FileIO.parseStr(xmlLoc, "config/opengl/window/title");
        width = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/width");
        height = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/height");
        bitdepth = (int)FileIO.parseNum(xmlLoc, "config/opengl/window/bitdepth");
        
        //engine
        blockTexture = FileIO.parseStr(xmlLoc, "config/engine/blockTexture");
        refresh = (int)FileIO.parseNum(xmlLoc, "config/engine/refresh");
        showFPS = Boolean.parseBoolean(FileIO.parseStr(xmlLoc, "config/engine/showFPS"));
        
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
    
    //method: save
    //purpose: uses FileIO to save all the vars to a new xml file
    public static void save(String xmlName) {
        
        if(xmlName != "default.xml") {
            String data = "" +
                    "<?xml version=\"1.0\"?>\n" +
                    "<config>\n" +
                    "<opengl>\n" +
                    "	<window>\n" +
                    "		<title>"+title+"</title>\n" +
                    "		<width>"+width+"</width>\n" +
                    "		<height>"+height+"</height>\n" +
                    "		<bitdepth>"+bitdepth+"</bitdepth>\n" +
                    "	</window>\n" +
                    "</opengl>\n" +
                    "<engine>\n" +
                    "   <blockTexture>"+blockTexture+"</blockTexture>\n" +
                    "   <refresh>"+refresh+"</refresh>\n" +
                    "   <showFPS>"+showFPS+"</showFPS>\n" +
                    "</engine>\n" +
                    "<controlls>\n" +
                    "	<movement>\n" +
                    "		<forward>"+Keyboard.getKeyName(forward)+"</forward>\n" +
                    "		<backward>"+Keyboard.getKeyName(backward)+"</backward>\n" +
                    "		<left>"+Keyboard.getKeyName(left)+"</left>\n" +
                    "		<right>"+Keyboard.getKeyName(right)+"</right>\n" +
                    "		<up>"+Keyboard.getKeyName(up)+"</up>\n" +
                    "		<down>"+Keyboard.getKeyName(down)+"</down>\n" +
                    "	</movement>\n" +
                    "	<sensitivity>\n" +
                    "		<pitch>"+pitch+"</pitch>\n" +
                    "		<yaw>"+yaw+"</yaw>\n" +
                    "	</sensitivity>\n" +
                    "</controlls>\n" +
                    "</config>";

            String xmlLoc = formatFileName(xmlName);
            
            FileIO.writeStr(xmlLoc, data);
        }
        else {
            Push.outError("Can not overwrite default.xml");
        }
    }
    
    //method: doesCustomProfileExist
    //purpose: determines if a sepcifiles profile exist
    public static boolean doesCustomProfileExist(String xmlName) {
        String xmlLoc = formatFileName(xmlName);
        
        return FileIO.doesFileExist(xmlLoc);
    }
}
