/*************************************************************** 
 *  file: FileIO.java
 *  author: Kevin Kongwattanachai
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 10/11/2021
 *  
 *  purpose: Methods used to read and write files
***************************************************************/

package MC_Project_3;

/**
 *
 * @author Kevin
 */

import org.w3c.dom.NodeList;
import org.xml.sax.*;
import javax.xml.xpath.*;


public class FileIO {
    
    //method: parseStr
    //purpose: Reads XML and outputs the target to a string
    public static String parseStr(String fileLoc, String path) {
        
        String data = null;
        
        try {
            InputSource src = new InputSource(fileLoc);
            XPath xpath = XPathFactory.newInstance().newXPath();
            
            data = xpath.evaluate(path, src);
            
        } catch(Exception e) {
            Push.outError("Unable to find: " + path);
        }
        
        return data;
        
    }
    
    //method: parseNum
    //purpose: Reads XML and outputs the target to a double
    public static double parseNum(String fileLoc, String path) {
        String data = parseStr(fileLoc, path);
        
        double num = toNum(data);
        
        return num;
    }
    
    //method: toNum
    //purpose: turns string into number
    private static double toNum(String rawNum) {
        double num = 0.0;
        
        try {
            if(rawNum.length() > 0) num = Double.parseDouble(rawNum);
        }
        catch(NumberFormatException e) {
            Push.outError("Not a number");
        }
        
        return num;
    }
    
    //method: isNum
    //purpose: Checks if a given char is a number
    private static boolean isNum(char c) {
        
        switch(c) {
            case '1': return true;
            case '2': return true;
            case '3': return true;
            case '4': return true;
            case '5': return true;
            case '6': return true;
            case '7': return true;
            case '8': return true;
            case '9': return true;
            case '0': return true;
            case '.': return true;
            case '-': return true;
            default: break;
        }
        
        return false;
    }

}
