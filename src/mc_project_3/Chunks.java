/***************************************************************
 *  file: Chunks.java
 *  authors: Kevin Kongwattanachai, Daniel Milligan, Eddie Rivas, Anthony Nguyen
 *  class: CS 4450 - Computer Graphics
 *
 *  assignment: Program 3
 *  date last modified: 8/24/2021
 *
 *  purpose: Stores data and functions of the Chunk Object (which is made up of
 *           blocks)
***************************************************************/

package MC_Project_3;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import java.lang.Math;




public class Chunks {

    public static final int CHUNK_SIZE[] ={16,180,16}; //x z y
    public static final int CUBE_LENGTH = 2;

    public Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;

    private FloatBuffer VertexPositionData;
    private FloatBuffer VertexColorData;
    private FloatBuffer VertexTextureData;


    private int StartX;
    private int StartY;
    private int StartZ;

    private int LocX;
    private int LocY;

    private boolean built;
    private boolean meshRebuilt;

    private Random r;

    private Noise Big;
    private Noise Small;
    private Noise Large;

    //method: Chunks
    //purpose: initilizes chunk
    public Chunks(int globalX, int globalY, int startX, int startY, int startZ, Noise large, Noise big, Noise small) {
        StartX = startX;
        StartY = startY;
        StartZ = startZ;

        LocX = globalX;
        LocY = globalY;

        Big = big;
        Small = small;
        Large =large;

        built = false;

    }

    public void generateChunk() {
        Blocks = new Block[CHUNK_SIZE[0]][CHUNK_SIZE[1]][CHUNK_SIZE[2]];

        simplex2dTerrainGeneration(45,Large, Big, Small);
        //simplex3dTerrainGeneration(45,Small, Big);


        placeDirt(3);
        placeWater(50);
        placeStone();
        placeSand();
        growSand();
        growSand();
        growSand();
        treeGeneration(Large, Big, Small);

        cullHiddenBlocks();


        built = true;
    }

    //method: render
    //purpose: renders the chunk
    public void render() {
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3,GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glTexCoordPointer(2,GL_FLOAT,0,0L);

        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE[0]*CHUNK_SIZE[1]*CHUNK_SIZE[2]*24);
    }

    public void initRender() {
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3,GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    public void loadMesh() {
        VertexPositionData.clear();
        VertexTextureData.clear();
        VertexColorData.clear();

        for(int x = 0; x < CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                    if(Blocks[x][y][z].getVisibility()) {
                        VertexPositionData.put(createCube((float) ((StartX+ x)  * CUBE_LENGTH), (float)((StartY+y)*CUBE_LENGTH),(float) ((StartZ+ z) * CUBE_LENGTH)));
                        VertexTextureData.put(createTexCube((float) 0, (float) 0,Blocks[(int)(x)][(int) (y)][(int) (z)]));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    }
                }
            }
        }

        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    //method: rebuildMesh
    //purpose: creates render data of chunk and sends it to the GPU
    public void rebuildMesh() {
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle= glGenBuffers();

        VertexPositionData = BufferUtils.createFloatBuffer(CHUNK_SIZE[0] * CHUNK_SIZE[1] * CHUNK_SIZE[2] * 6 *12);
        VertexColorData = BufferUtils.createFloatBuffer(CHUNK_SIZE[0] * CHUNK_SIZE[1] * CHUNK_SIZE[2] * 6 *12);
        VertexTextureData = BufferUtils.createFloatBuffer(CHUNK_SIZE[0]* CHUNK_SIZE[1] *CHUNK_SIZE[2] * 6 * 12);

        for(int x = 0; x < CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                    if(Blocks[x][y][z].getVisibility()) {
                        VertexPositionData.put(createCube((float) ((StartX+ x)  * CUBE_LENGTH), (float)((StartY+y)*CUBE_LENGTH),(float) ((StartZ+ z) * CUBE_LENGTH)));
                        VertexTextureData.put(createTexCube((float) 0, (float) 0,Blocks[(int)(x)][(int) (y)][(int) (z)]));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    }
                }
            }
        }

        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();

        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


    }

    //method: createCubeVertexCol
    //purpose: Sets the color of the cube's vertex
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length*4*6];
        for(int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i%CubeColorArray.length];
        }
        return cubeColors;
    }

    //method: createCube
    //purpose: creates the vertexs of the cube
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH/2;

        return new float[] {
            //Top Quad
            x+offset, y+offset, z,
            x-offset, y+offset, z,
            x-offset, y+offset, z - CUBE_LENGTH,
            x+offset, y+offset, z - CUBE_LENGTH,
            //Bottom
            x + offset, y -offset, z -CUBE_LENGTH,
            x -offset, y -offset, z -CUBE_LENGTH,
            x -offset, y -offset, z,
            x + offset, y -offset, z,
            //Front
            x + offset, y + offset, z -CUBE_LENGTH,
            x -offset, y + offset, z -CUBE_LENGTH,
            x -offset, y -offset, z -CUBE_LENGTH,
            x + offset, y -offset, z -CUBE_LENGTH,
            //Back
            x + offset, y -offset, z,
            x -offset, y -offset, z,
            x -offset, y + offset, z,
            x + offset, y + offset, z,
            //Left
            x -offset, y + offset, z -CUBE_LENGTH,
            x -offset, y + offset, z,
            x -offset, y -offset, z,
            x -offset, y -offset, z -CUBE_LENGTH,
            //Right
            x + offset, y + offset, z,
            x + offset, y + offset, z -CUBE_LENGTH,
            x + offset, y -offset, z -CUBE_LENGTH,
            x + offset, y -offset, z
        };
    }

    //method: getCubeColor
    //purpose: returns the color of the cube based on the block type
    //         now has been changed to deal with lighting
    private float[] getCubeColor(Block block) {
        return new float[]{.2f+(.1f*block.getBrightness()),.2f+(.1f*block.getBrightness()),.2f+(.1f*block.getBrightness())};

    }


    //method: createTexCube
    //purpose: plasters a texture to each face of the cube
    public static float[] createTexCube(float x, float y, Block block) {


        float offset = (256f/16)/256f;
        float blockRowU = 10;
        float blockColU = 10;
        float blockRowD = 10;
        float blockColD = 10;
        float blockRowF = 10;
        float blockColF = 10;
        float blockRowB = 10;
        float blockColB = 10;
        float blockRowL = 10;
        float blockColL = 10;
        float blockRowR = 10;
        float blockColR = 10;


            switch (block.GetID()) {
                case 1: blockRowU = 3;
                        blockColU = 9;

                        blockRowD = 1;
                        blockColD = 3;

                        blockRowF = 1;
                        blockColF = 4;

                        blockRowB = 1;
                        blockColB = 4;

                        blockRowL = 1;
                        blockColL = 4;

                        blockRowR = 1;
                        blockColR = 4;
                        break;

                case 2: blockRowU = 2;
                        blockColU = 3;

                        blockRowD = 2;
                        blockColD = 3;

                        blockRowF = 2;
                        blockColF = 3;

                        blockRowB = 2;
                        blockColB = 3;

                        blockRowL = 2;
                        blockColL = 3;

                        blockRowR = 2;
                        blockColR = 3;
                        break;

                case 3: blockRowU = 1;
                        blockColU = 15;

                        blockRowD = 1;
                        blockColD = 15;

                        blockRowF = 1;
                        blockColF = 15;

                        blockRowB = 1;
                        blockColB = 15;

                        blockRowL = 1;
                        blockColL = 15;

                        blockRowR = 1;
                        blockColR = 15;
                        break;

                case 4: blockRowU = 1;
                        blockColU = 3;

                        blockRowD = 1;
                        blockColD = 3;

                        blockRowF = 1;
                        blockColF = 3;

                        blockRowB = 1;
                        blockColB = 3;

                        blockRowL = 1;
                        blockColL = 3;

                        blockRowR = 1;
                        blockColR = 3;
                        break;

                case 5: blockRowU = 1;
                        blockColU = 2;

                        blockRowD = 1;
                        blockColD = 2;

                        blockRowF = 1;
                        blockColF = 2;

                        blockRowB = 1;
                        blockColB = 2;

                        blockRowL = 1;
                        blockColL = 2;

                        blockRowR = 1;
                        blockColR = 2;
                        break;

                case 6: blockRowU = 2;
                        blockColU = 2;

                        blockRowD = 2;
                        blockColD = 2;

                        blockRowF = 2;
                        blockColF = 2;

                        blockRowB = 2;
                        blockColB = 2;

                        blockRowL = 2;
                        blockColL = 2;

                        blockRowR = 2;
                        blockColR = 2;
                        break;
                case 7: blockRowU = 2;
                        blockColU = 6;

                        blockRowD = 2;
                        blockColD = 6;

                        blockRowF = 2;
                        blockColF = 5;

                        blockRowB = 2;
                        blockColB = 5;

                        blockRowL = 2;
                        blockColL = 5;

                        blockRowR = 2;
                        blockColR = 5;
                        break;

                case 8: blockRowU = 4;
                        blockColU = 6;

                        blockRowD = 4;
                        blockColD = 6;

                        blockRowF = 4;
                        blockColF = 6;

                        blockRowB = 4;
                        blockColB = 6;

                        blockRowL = 4;
                        blockColL = 6;

                        blockRowR = 4;
                        blockColR = 6;
                        break;

                default: break;
            }


            return new float[] {

                // Up
                x + offset*(blockColU), y + offset*(blockRowU), //lt
                x + offset*(blockColU-1), y + offset*(blockRowU), //rt
                x + offset*(blockColU-1), y + offset*(blockRowU-1), //rb
                x + offset*(blockColU), y + offset*(blockRowU-1), //lb
                // Down!
                x + offset*(blockColD), y + offset*(blockRowD),
                x + offset*(blockColD-1), y + offset*(blockRowD),
                x + offset*(blockColD-1), y + offset*(blockRowD-1),
                x + offset*(blockColD), y + offset*(blockRowD-1),
                // FRONT QUAD
                x + offset*(blockColF-1), y + offset*(blockRowF-1),
                x + offset*(blockColF), y + offset*(blockRowF-1),
                x + offset*(blockColF), y + offset*(blockRowF),
                x + offset*(blockColF-1), y + offset*(blockRowF),
                // BACK QUAD
                x + offset*(blockColB), y + (offset*blockRowB),
                x + offset*(blockColB-1), y + offset*(blockRowB),
                x + offset*(blockColB-1), y + offset*(blockRowB-1),
                x + offset*(blockColB), y + offset*(blockRowB-1),

                // LEFT QUAD
                x + offset*(blockColL-1), y + offset*(blockRowL-1),
                x + offset*(blockColL), y + offset*(blockRowL-1),
                x + offset*(blockColL), y + offset*(blockRowL),
                x + offset*(blockColL-1), y + offset*(blockRowL),
                // RIGHT QUAD
                x + offset*(blockColR-1), y + offset*(blockRowR-1),
                x + offset*(blockColR), y + offset*(blockRowR-1),
                x + offset*(blockColR), y + offset*(blockRowR),
                x + offset*(blockColR-1), y + offset*(blockRowR)
            };
    }

    //method: randomBlockPlacement
    //purpose: replaces the default block with random blocks
    private void randomBlockPlacement() {
        r = new Random();

        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                    if(Blocks[x][y][z].GetID() == -128) {
                        if(r.nextFloat() > 0.83f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                        }
                        else if(r.nextFloat() > 0.66f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }
                        else if(r.nextFloat() > 0.5f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                        }
                        else if(r.nextFloat() > 0.33f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                        }
                        else if(r.nextFloat() > 0.16f) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                        }
                        else {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                        }
                    }
                }
            }
        }
    }

    //method: placeDirt
    //purpose: places 3 drit blocks under the surface grass blocks
    private void placeDirt(int depth) {
        int orignalDepth = depth;
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = CHUNK_SIZE[1]-1; y > depth+1; y--) {
                    if(Blocks[x][y][z].GetID() == 1) {
                        if(y > 53) depth = 1;
                        else depth = orignalDepth;
                        int i = 0;
                        while(i < depth) {
                            y--;
                            if(Blocks[x][y][z].GetID() == -128) Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                            i++;
                        }
                    }
                }
            }
        }
    }

    //method: placeWater
    //purpose: replaces air with water below a certain level
    private void placeWater(int level) {
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = level; y > 1; y--) {
                    if(Blocks[x][y][z].GetID() == 0) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    }
                }
            }
        }
    }

    //method: placeSand
    //purpose: replaces dirt/grass with sand if it touches water
    //method: placeSand
    //purpose: replaces dirt/grass with sand if it touches water
    private void placeSand() {
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                for(int z = 0; z < CHUNK_SIZE[2]; z++) {

                    //check if block is dirt/grass and touches water
                    if(Blocks[x][y][z].GetID() == 1 || Blocks[x][y][z].GetID() == 4 || Blocks[x][y][z].GetID() == -128) { //if dirt or grass
                        if(z > 0 && Blocks[x][y][z-1].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }
                        else if(z < CHUNK_SIZE[2]-1 && Blocks[x][y][z+1].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }//check back

                        else if(y > 0 && Blocks[x][y-1][z].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }//check bottom

                        else if(y < CHUNK_SIZE[1]-1 && Blocks[x][y+1][z].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }// check top

                        else if(x > 0 && Blocks[x-1][y][z].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }//check left

                        else if(x < CHUNK_SIZE[0]-1 && Blocks[x+1][y][z].GetID() == 3) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                        }//check right
                    }

                }
            }
        }
    }

    //method: growSand()
    //purpose: grows the sand
    private void growSand() {
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                for(int z = 0; z < CHUNK_SIZE[2]; z++) {

                    //check if block is dirt/grass and touches water
                    if(Blocks[x][y][z].GetID() == 1 || Blocks[x][y][z].GetID() == 4 || Blocks[x][y][z].GetID() == -128) { //if dirt or grass
                        if(z > 0 && Blocks[x][y][z-1].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }
                        else if(z < CHUNK_SIZE[2]-1 && Blocks[x][y][z+1].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }//check back

                        else if(y > 0 && Blocks[x][y-1][z].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }//check bottom

                        else if(y < CHUNK_SIZE[1]-1 && Blocks[x][y+1][z].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }// check top

                        else if(x > 0 && Blocks[x-1][y][z].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }//check left

                        else if(x < CHUNK_SIZE[0]-1 && Blocks[x+1][y][z].GetID() == 2) {
                            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                        }//check right
                    }

                }
            }
        }

        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                for(int z = 0; z < CHUNK_SIZE[2]; z++) {

                    if(Blocks[x][y][z].GetID() == -128) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
                    }

                }
            }
        }
    }

    //method: placeStone
    //purpose: Replaces default blocks with stone
    private void placeStone() {
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = CHUNK_SIZE[1]-1; y > 2; y--) {
                    if(Blocks[x][y][z].GetID() == -128) Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
                }
            }
        }
    }

    //method: simplex2dTerrainGeneration
    //purpose: Generates Terrain based on heightmap from simplex noise
    private void simplex2dTerrainGeneration(int depth, Noise large, Noise big, Noise small) {

        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                    if(y < 3) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
                    }
                    else if(y == depth+(int)(big.getNoise((x+StartX), (z+StartZ))+small.getNoise((x+StartX), (z+StartZ))) +(int)(8/(1+Math.exp(large.getNoise((x+StartX), (z+StartZ)) /8 ))) ) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    }
                    else if(y < depth+(int)(big.getNoise((x+StartX), (z+StartZ))+small.getNoise((x+StartX), (z+StartZ))) +(int)( 8/(1+Math.exp(large.getNoise((x+StartX), (z+StartZ)) /8 ))) ) {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                    }
                    else {
                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Air);
                    }
                }
            }
        }

    }

    //method: cullHiddenBlocks
    //purpose: finds blocks that are hidden and set visability to false
    private void cullHiddenBlocks() {
        for(int x = 0; x< CHUNK_SIZE[0]; x++) {
            for(int y = 0; y < CHUNK_SIZE[1]; y++) {
                for(int z = 0; z < CHUNK_SIZE[2]; z++) {
                    if(Blocks[x][y][z].GetID() != 0 && Blocks[x][y][z].GetID() != 3) {
                        Blocks[x][y][z].setVisibility(true);
                        if(z > 0 && Blocks[x][y][z-1].GetID() != 0 && Blocks[x][y][z-1].GetID() != 3) { //check front
                            if(z < CHUNK_SIZE[2]-1 && Blocks[x][y][z+1].GetID() != 0 && Blocks[x][y][z+1].GetID() != 3) { //check back
                                if(y > 0 && Blocks[x][y-1][z].GetID() != 0 && Blocks[x][y-1][z].GetID() != 3) { //check bottom
                                    if(y < CHUNK_SIZE[1]-1 && Blocks[x][y+1][z].GetID() != 0 && Blocks[x][y+1][z].GetID() != 3) { // check top
                                        if(x > 0 && Blocks[x-1][y][z].GetID() != 0 && Blocks[x-1][y][z].GetID() != 3) { //check left
                                            if(x < CHUNK_SIZE[0]-1 && Blocks[x+1][y][z].GetID() != 0 && Blocks[x+1][y][z].GetID() != 3) { //check right
                                                Blocks[x][y][z].setVisibility(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(Blocks[x][y][z].GetID() == 3) {
                        Blocks[x][y][z].setVisibility(true);
                    }
                    else {
                        Blocks[x][y][z].setVisibility(false);
                    }
                }
            }
        }
    }

    public void placeStoneAt(int x, int y, int z) {//places stone at coordinates in front of player
        int origX = x, origY = y, origZ = z;
        if (x < 0 && x > -29.5){// converts the actual position to x array index
            //x = (x + 29) / 2;
            x = (x * -1) / 2;
        }
        else if (x < 2 && x > 0){
            //x = (x + 29) / 2;
            x = x * -1 + 2;
        }
        else if (x < 34 && x > 2){
            //x = (x - 3) / 2;
            x = -1 * x/2 + 16;
        }
        else if (x < 66 && x > 34){
            x = -1 * x/2 + 32;
        }
        else if (x < 98 && x > 66){
            x = -1 * x/2 + 48;
        }
        else if (x < 130 && x > 98){
            x = -1 * x/2 + 65;
        }
        else if (x < 162 && x > 130){
            x = -1 * x/2 + 81;
        }
        else if (x < 194 && x > 162){
            x = -1 * x/2 + 97;
        }
        else if (x < 226 && x > 194){
            x = -1 * x/2 + 113;
        }
        else if (x < 258 && x > 226){
            x = -1 * x/2 + 129;
        }
        else if (x < 290 && x > 258){
            x = -1 * x/2 + 145;
        }

        y = y * (-1) / 2; // converts the actual position to y array index

        if (z < 0 && z > -29.5){// converts the actual position to z array index
            z = (z * -1) / 2;
        }
        else if (z < 2 && z > 0){
            //x = (x + 29) / 2;
            z = z * -1 + 2;
        }
        else if (z < 34 && z > 2){
            z = -1 * z/2 + 16;
        }
        else if (z < 66 && z > 34){
            z = -1 * z/2 + 32;
        }
        else if (z < 98 && z > 66){
            z = -1 * z/2 + 48;
        }
        else if (z < 130 && z > 98){
            z = -1 * z/2 + 65;
        }
        else if (z < 162 && z > 130){
            z = -1 * z/2 + 81;
        }
        else if (z < 194 && z > 162){
            z = -1 * z/2 + 97;
        }
        else if (z < 226 && z > 194){
            z = -1 * z/2 + 113;
        }
        else if (z < 258 && z > 226){
            z = -1 * z/2 + 129;
        }
        else if (z < 290 && z > 258){
            z = -1 * z/2 + 145;
        }


        if (origX > -29.5 && origX < 290 && origZ > -29.5 && origZ < 290){// places blocks only if they are within 10x10 grid
            VertexPositionData.put(createCube((float) origX, (float) origY, (float) origZ));
            VertexTextureData.put(createTexCube((float) 0, (float) 0,Blocks[x][y][z]));
            VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[x][y][z])));
            Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
        }
    }

    private void treeGeneration(Noise large, Noise big, Noise small){
        int gridspace = 5; // Size of grid for tree
        int gridmidpoint = gridspace/2; // Plant in middle of grid tile
        int treepaddingmax = 2; // maximum added distance from next generatable tree
        for(int x = 0; x< CHUNK_SIZE[0]; x+= gridspace) {
            int largenoise = (int)Math.abs(8/(1+Math.exp(large.getNoise((x+StartX), (StartZ)) /8)));
            int bignoise = (int)Math.abs(big.getNoise((x+StartX), (StartZ)));
            int smallnoise = (int)Math.abs(small.getNoise((x+StartX), (StartZ)));
            int xpadding = (7*largenoise + 2*bignoise + 7*smallnoise) % 2;
            x += xpadding;
            for(int z = 0; z < CHUNK_SIZE[2]; z += gridspace) {
                largenoise = (int)(8/(1+Math.exp(large.getNoise((x+StartX), (z+StartZ)) /8)));
                bignoise = (int)Math.abs(big.getNoise((x+StartX), (z+StartZ)));
                smallnoise = (int)Math.abs(small.getNoise((x+StartX), (z+StartZ)));
                if (x + gridspace < CHUNK_SIZE[0] && z + gridspace < CHUNK_SIZE[2]){
                    int treechance = 2*largenoise + 3*bignoise - 11*smallnoise;
                    if (treechance > 35){
                        int grasslevel = 0;
                        boolean placed = false;
                        while (!placed && grasslevel < CHUNK_SIZE[1]){
                            if (Blocks[x + gridmidpoint][grasslevel][z + gridmidpoint].GetID() == 1){
                                int prefab = (9*largenoise + 7*bignoise - 17*smallnoise) % 4;
                                placeTree(prefab, x + gridmidpoint, grasslevel,z + gridmidpoint);
                                placed = true;
                                int zpadding = (7*largenoise + 2*bignoise + 7*smallnoise) % 4;
                                z += zpadding;
                                xpadding = (7*largenoise + 2*bignoise + 7*smallnoise) % 4;
                                x += xpadding;
                            }
                            else
                                grasslevel++;
                        }
                    }
                }
            }
        }
    }
    // Plant tree in middle of every chunk
    private void simpleTreeGeneration(){
        int mid = 14;
        int grasslevel = 0;
        boolean placed = false;

        while (!placed && grasslevel < CHUNK_SIZE[1]){
            if (Blocks[mid][grasslevel][mid].GetID() == 1){
                placeTree(0, mid, grasslevel, mid);
                placed = true;
            }
            else
                grasslevel++;
        }

    }
    private void placeTree(int prefab, int x, int y, int z){

        switch(prefab){
            case 0: // Oak tree
            // Wood Base
            Blocks[x][y+1][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+2][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+3][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+4][z] = new Block(Block.BlockType.BlockType_Wood);

            // Leaves (Height 3)
            Blocks[x+1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            // Leaves (Height 4)
            Blocks[x+1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);


            Blocks[x][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            break;
            case 1: // Alternate Oak tree
            // Wood Base
            Blocks[x][y+1][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+2][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+3][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+4][z] = new Block(Block.BlockType.BlockType_Wood);

            // Leaves (Height 3)
            Blocks[x+1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            // Leaves (Height 4)
            Blocks[x+1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);


            Blocks[x][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            break;
            case 2: // Balloon Oak tree
            // Wood Base
            Blocks[x][y+1][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+2][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+3][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+4][z] = new Block(Block.BlockType.BlockType_Wood);

            // Leaves (Height 3)
            Blocks[x+1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+3][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            // Leaves (Height 4)
            Blocks[x+1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+4][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            // Leaves (Height 5)
            Blocks[x+1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z+2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-2][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z-2] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+2][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+6][z-1] = new Block(Block.BlockType.BlockType_Leaves);
            break;
            default:
            // Wood Base
            Blocks[x][y+1][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+2][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+3][z] = new Block(Block.BlockType.BlockType_Wood);
            Blocks[x][y+4][z] = new Block(Block.BlockType.BlockType_Wood);

            // Leaves
            Blocks[x+1][y+2][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+2][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+2][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+2][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x+1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+3][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+3][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x+1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+4][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+4][z-1] = new Block(Block.BlockType.BlockType_Leaves);


            Blocks[x][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x+1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x-1][y+5][z] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z+1] = new Block(Block.BlockType.BlockType_Leaves);
            Blocks[x][y+5][z-1] = new Block(Block.BlockType.BlockType_Leaves);

            Blocks[x][y+6][z] = new Block(Block.BlockType.BlockType_Leaves);
            break;
        }
    }
    //Method: Get
    //Purpose Returns X Y Z value of chunk
    public int getX() {
        return StartX;
    }
    public int getY() {
        return StartY;
    }
    public int getZ() {
        return StartZ;
    }
    public int getGlobalX() {
        return LocX;
    }
    public int getGlobalY() {
        return LocY;
    }
    public boolean isGenerated() {
        return built;
    }
    public boolean isMeshRebuilt() {
        return meshRebuilt;
    }
    public int[] chunkSize() {
        return CHUNK_SIZE;
    }

    public int getBlocksID(int x, int y, int z){
        return Blocks[x][y][z].GetID();
    }
}
