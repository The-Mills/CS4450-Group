/*************************************************************** 
 *  file: Noise.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 8/24/2021
 *  
 *  purpose: Simplex noise object
***************************************************************/

package MC_Project_3;

import java.util.Random;


public class Noise {
    private int noiseSeed;
    
    private int numberOfOctaves;
    
    private SimplexNoise_octave[] octaves;
    
    private double[] frequencys;
    
    private double[] amplitudes;
    
    private double fAmp;
    
    double persistence;
    
    //method: Noise
    //purpose: initilize Noise generator
    public Noise(int seed, double noisePersistence, int largestFeature, double amp) {
        noiseSeed = seed;
        
        persistence = noisePersistence;
        
        numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));
        
        octaves=new SimplexNoise_octave[numberOfOctaves];
        
        frequencys=new double[numberOfOctaves];
        
        amplitudes=new double[numberOfOctaves];
        
        fAmp = amp;
        
        
         for(int i=0;i<numberOfOctaves;i++){
             octaves[i]=new SimplexNoise_octave(seed);
             frequencys[i] = Math.pow(2,i);
             amplitudes[i] = Math.pow(persistence,octaves.length-i);
         }
    }
    
    //method: getNoise
    //purpose: gets the noise at a certain 2d coordinate
    public double getNoise(int x, int y){
        double result=0;
        for(int i=1;i<octaves.length;i++)
            {                  
                result=result+octaves[i].noise(x/frequencys[i], y/frequencys[i])* amplitudes[i];
            }        
        return result*fAmp;    
    }    
    
    
}
