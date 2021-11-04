/*************************************************************** 
 *  file: FileIO.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 11/1/2021
 *  
 *  purpose: Methods used to read and write files
***************************************************************/

package MC_Project_3;

/**
 *
 * @author Kevin
 */


import org.xml.sax.*;
import javax.xml.xpath.*;
 
import java.io.PrintWriter;  

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.w3c.dom.NodeList;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



public class FileIO 
{
    //method: doesFileExist
    //purpose: returns true if file exist, false if not
    public static boolean doesFileExist(String loc) {
        File tempFile = new File(loc);
        boolean exists = tempFile.exists();
        
        return exists;
    }
    
    //method: parseStr
    //purpose: Reads XML and outputs the target to a string
    public static String parseStr(String fileLoc, String path) 
    {
        
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
    public static double parseNum(String fileLoc, String path) 
    {
        String data = parseStr(fileLoc, path);
        
        double num = toNum(data);
        
        return num;
    }
    
    //method: toNum
    //purpose: turns string into number
    private static double toNum(String rawNum) 
    {
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
    private static boolean isNum(char c) 
    {
        
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
    
    //method: writeStr
    // purpose: writes to a file
    public static void writeStr(String fileLoc, String content) {
        try {
            PrintWriter writer1 = new PrintWriter(new File(fileLoc));
            
            writer1.write(content);                                                   
                         writer1.flush();  
            writer1.close();  
            
        }
        catch (java.io.FileNotFoundException e) {
        
        }
    }

}
