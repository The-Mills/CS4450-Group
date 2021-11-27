/*************************************************************** 
 *  file: Block.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics 
 *  
 *  assignment: Program 3
 *  date last modified: 11/1/2021
 *  
 *  purpose: Store data for the block Object
***************************************************************/
package MC_Project_3;

/**
 *
 * @author kevink1
 */
public class Block {
    private boolean isActive;
    private boolean isVisable;
    private BlockType Type;
    private float x,y,z;
    
    //method: BlockType
    //purpose: enum to map numbers to block name
    public enum BlockType {
        BlockType_Default((byte)-128),
        BlockType_Air((byte)0),
        BlockType_Grass((byte)1),
        BlockType_Sand((byte)2),
        BlockType_Water((byte)3),
        BlockType_Dirt((byte)4),
        BlockType_Stone((byte)5),
        BlockType_Bedrock((byte)6);
        
        
        
        
        private byte BlockID;
        
        BlockType(byte i) {
            BlockID = i;
        }
        public byte GetID() {
            return BlockID;
        }
        public void SetID(int i) {
            BlockID = (byte)i;
        }
        
    }
    
    //method: Block
    //purpose: initilizes block
    public Block(BlockType type) {
        Type = type;
        isVisable = true;
    }
    
    //method: setCoords
    //purpose: sets XYZ Coordinate of block
    public void setCoords(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float getXCoord(){//
        return this.x;
    }
    public float getYCoord(){//
        return this.y;
    }
    public float getZCoord(){//
        return this.z;
    }
    
    //method: isActive
    //purpose: returns if block is active
    public boolean isActive() {
        return isActive;
    }
    
    //method: setActive
    //purpose: return if block is astive
    public void SetActive(boolean active) {
        isActive=active;
    }
    
    //method: GetID
    //purpose: returns the ID of the block
    public int GetID() {
        return Type.GetID();
    }
    
    //method: getVisibility
    //purpose: returns if block is visabile
    public boolean getVisibility() {
        return isVisable;
        
    }
    
    //method: setVisibility
    //purpose: sets if block is visable 
    public void setVisibility(boolean visibility) {
        isVisable = visibility;
    }
    
}
