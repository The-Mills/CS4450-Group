/*************************************************************** 
 *  file: Push.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 8/24/2021
 *  
 *  purpose: Collection of methods that uses Java's Swing to send popups
 *           or notifications to the user.
***************************************************************/


/*

Ver 1.1

-- methods list --

out 
outInfo
outWarn
outQuestion
outError

input
inputQuestion
inputError

intInput
intInputQuestion
intInputError

doubleInput
doubleInputQuestion
doubleInputError
*/




package MC_Project_3;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

public class Push 
{
    
    //method: out
    //purpose: shows popup with a message and pauses the program
    public static void out(String message) 
    { 
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);  
    }
    public static void out(int message) 
    {
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    public static void out(double message) 
    {  
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    public static void out(float message) 
    { 
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    public static void out(boolean message) 
    {   
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    public static void out(String[] message) 
    {
        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    public static void out(String[][] message) 
    {

        StringBuilder fromatedMessage = new StringBuilder();
        int sizeOfBiggest = 0;
        
        //find biggest word
        for(int a = 0; a < message.length; a++) 
            for(int b = 0; b < message[a].length; b++) 
                if(message[a][b].length() > sizeOfBiggest) sizeOfBiggest = message[a][b].length();
        
        //adds padding so all words line up
        for(int a = 0; a < message.length; a++) 
        {
                    
            for(int b = 0; b < message[a].length; b++) 
            {     
                fromatedMessage.append(message[a][b]);

                //add padding
                for(int p = sizeOfBiggest-message[a][b].length()+1; p > 0; p--)
                    fromatedMessage.append(" ");;
            }
            
            fromatedMessage.append("\n");
        }
        
        JTextArea label = new JTextArea(fromatedMessage.toString());
        label.setOpaque( false );
        label.setEditable( false );
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        JOptionPane.showOptionDialog(null, label, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        
    }
    public static void out(int[][] message) 
    {
        
        String[][] messageStr = new String[message.length][];
        
        //convert int 2d array to string 2d array
        for(int a = 0; a < message.length; a++) 
        {
            messageStr[a] = new String[message[a].length];
            
            for(int b = 0; b < message[a].length; b++)
                messageStr[a][b] = String.valueOf(message[a][b]);
        }
        
        Push.out(messageStr);
    }
    public static void out(double[][] message) 
    {   
        String[][] messageStr = new String[message.length][];
        
        //convert int 2d array to string 2d array
        for(int a = 0; a < message.length; a++) {
            messageStr[a] = new String[message[a].length];
            
            for(int b = 0; b < message[a].length; b++) {
                messageStr[a][b] = String.valueOf(message[a][b]);
            }
        }
        
        Push.out(messageStr);
    }
    public static void out(float[][] message) 
    {
        
        String[][] messageStr = new String[message.length][];
        
        //convert int 2d array to string 2d array
        for(int a = 0; a < message.length; a++) {
            messageStr[a] = new String[message[a].length];
            
            for(int b = 0; b < message[a].length; b++) {
                messageStr[a][b] = String.valueOf(message[a][b]);
            }
        }
        
        Push.out(messageStr);
    }
    public static void out(boolean[][] message) 
    {
        
        String[][] messageStr = new String[message.length][];
        
        //convert int 2d array to string 2d array
        for(int a = 0; a < message.length; a++) {
            messageStr[a] = new String[message[a].length];
            
            for(int b = 0; b < message[a].length; b++) {
                messageStr[a][b] = String.valueOf(message[a][b]);
            }
        }
        
        Push.out(messageStr);
    }
    
    //method: error
    //purpose: shows error popup message and pauses the program
    public static void outError(String message) 
    {

        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);

    }
    
    //method: info
    //purpose: shows info popup message and pauses the program
    public static void outInfo(String message) 
    {

        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }
    
    //method: outWarn
    //purpose: shows warning popup message and pauses the program
    public static void outWarn(String message) 
    {

        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

    }
    
    //method: question
    //purpose: shows warning popup message and pauses the program
    public static void outQuestion(String message) 
    {

        JOptionPane.showOptionDialog(null, message, getProjectName(), JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

    }
    
    
    //method: input
    //purpose: display message gets a string input from user 
    public static String input(String message) 
    {
        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.PLAIN_MESSAGE, null, null, ""));
    }
    public static String input(String message, String preText) 
    {
        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.PLAIN_MESSAGE, null, null, preText));
    }
    
    //method: intInput
    //purpose: display message gets a int input from user. Gives error and asks
    //         again if input is not an int
    public static int intInput(String message) 
    {
        
        String userInput = input(message);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static int intInput(String message, String preText) 
    {
        
        String userInput = input(message, preText);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    //method: dubInput
    //purpose: display message gets a double input from user. Gives error and
    //         asks again if input is not an double
    public static double doubleInput(String message) 
    {
        
        String userInput = input(message);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static double doubleInput(String message, String preText) 
    {
        
        String userInput = input(message, preText);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Decimal");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    
    //method: inputQuestion
    //purpose: display message with question icon, gets string input from user
    public static String inputQuestion(String message) 
    {
        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.QUESTION_MESSAGE, null, null, ""));
    }
    public static String inputQuestion(String message, String preText) 
    {
        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.QUESTION_MESSAGE, null, null, preText));
    }
    
    //method: intInputQuestion
    //purpose: display message gets a int input from user. Gives error and asks
    //         again if input is not an int. Has Question Icon.
    public static int intInputQuestion(String message) 
    {
        
        String userInput = inputQuestion(message);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static int intInputQuestion(String message, String preText) 
    {
        
        String userInput = inputQuestion(message, preText);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    //method: dubInputQuestion
    //purpose: display message gets a double input from user. Gives error and
    //         asks again if input is not an double. Has Question Icon.
    public static double doubleInputQuestion(String message) 
    {
        
        String userInput = inputQuestion(message);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static double doubleInputQuestion(String message, String preText) 
    {
        
        String userInput = inputQuestion(message, preText);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Decimal");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    
    
    
    //method: inputError
    //purpose: display message with question icon, gets string input from user
    public static String inputError(String message) 
    {
        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.ERROR_MESSAGE, null, null, ""));
    }
    public static String inputError(String message, String preText) 
    {

        return ""+(JOptionPane.showInputDialog(null, message, getProjectName(), JOptionPane.ERROR_MESSAGE, null, null, preText));
    }
    
    //method: intInputError
    //purpose: display message gets a int input from user. Gives error and asks
    //         again if input is not an int. Has Error Icon.
    public static int intInputError(String message) 
    {
        
        String userInput = inputError(message);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static int intInputError(String message, String preText) 
    {
        
        String userInput = inputError(message, preText);
        int userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Integer.parseInt(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    //method: dubInputError
    //purpose: display message gets a double input from user. Gives error and
    //         asks again if input is not an double. Has Error Icon.
    public static double doubleInputError(String message) 
    {
        
        String userInput = inputError(message);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Integer");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    public static double doubleInputError(String message, String preText) 
    {
        
        String userInput = inputError(message, preText);
        double userInt = 0;
        
        if(userInput != null) {
            try{
                userInt = Double.parseDouble(userInput);
            }
            catch (NumberFormatException nfe) {
                outError("Error: Input not an Decimal");
                intInput(message);
            }
        }
        
        return userInt;
        
    }
    
    
    //method: getName
    //purpose: Returns the name of the project
    private static String getProjectName() 
    {
        String path = System.getProperty("user.dir");
        
        String projectName = path.substring(path.lastIndexOf("\\")+1);
        projectName.trim();
        
        return projectName;
    }
}
