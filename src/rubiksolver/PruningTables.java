/**
 * This program will solve a Rubik's Cube Puzzle using the
 * two phase algorithm described by Herbert Kociemba at
 * http://kociemba.org/cube.htm
 *
 * This is for private use only, not for release
 *
 * @author Russell Feldhausen
 * @version 1.0 2008.12.04
 */

package rubiksolver;

import java.io.Serializable;
import rubiksolverdef.CubieCube;
import rubiksolverdef.FlipUDSlice;
import rubiksolver.RubikModel2.*;
import rubiksolverdef.CornerPerm;

/**
 * This class contains the Pruning tables needed for the Two-Phase algorithm
 *
 * See http://kociemba.org/math/pruning.htm for more info
 */
public class PruningTables implements Serializable{
    
    public byte Phase1PruningTable[][];
    public byte Phase2PruningTable[][];


    /**
     * This is the constructor that takes the structures as input, and constructs
     * the Pruning tables using those structures
     *
     * @param model - the RubikModel
     * @param flip - a FlipUDSlice instance
     * @param table - a TwistMoveTable instance
     * @param perm - a CornerPerm instance
     */
    public PruningTables(RubikModel2 model, FlipUDSlice flip, TwistMoveTable table, CornerPerm perm){
        Phase1PruningTable = new byte[flip.size][2187];
        System.out.println("Initializing Phase1PruningTable:" );

        //First, set all entries to a large number (allows for simple checking)
        for(int i = 0; i < flip.size; i++){
            for(int j = 0; j < 2187; j++){
                Phase1PruningTable[i][j] = 42;
            }
        }
        int count = 0;
        CubieCube c = new CubieCube(true);
        Phase1PruningTable[0][0] = 0;

        //For each depth in the pruning table
        for(int i = 0; i < 13; i++){
            System.out.print(("  Depth " + i + ": ["));
            //For each possible FlipUDSlice SymCoordinate
            for(int j = 0; j < flip.size; j++){
                //For each Corner Orientation Coordinate
                for(int k = 0; k < 2187; k++){
                    if(Phase1PruningTable[j][k] == i){
                        //apply all 18 moves to the cube using move tables
                        for(TurnAxis l : TurnAxis.values()){
                            for(int m = 0; m < 3; m++){
                                int UDSliceCoordRaw = (table.FlipUDSliceTwistMove[j][3 * l.ordinal() + m]);
                                int UDSliceCoord = (int)(UDSliceCoordRaw / 16);
                                int CornOriCoordRaw = table.CornOriTwistMove[k][3 * l.ordinal() + m];
                                int CornOriCoord = table.CornOriSym[CornOriCoordRaw][UDSliceCoordRaw % 16];

                                //check to see if resulting entry is not set
                                if(Phase1PruningTable[UDSliceCoord][CornOriCoord] == 42){
                                    //if not, set it
                                     Phase1PruningTable[UDSliceCoord][CornOriCoord] = (byte)(i + 1);
                                     count++;
                                }
                                int UDSliceRaw = flip.FlipUDSliceToRaw[UDSliceCoord][0];
                                //check all possible symmetries. If the raw coordinate is the same, then also set
                                for(int x = 1; x < 16; x++){
                                    if(table.FlipUDSliceSym[UDSliceCoord][x] == UDSliceRaw){
                                        int newCornOriCoord = table.CornOriSym[CornOriCoord][x];
                                        if(Phase1PruningTable[UDSliceCoord][newCornOriCoord] == 42){
                                            Phase1PruningTable[UDSliceCoord][newCornOriCoord] = (byte)(i + 1);
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(j % 3220 == 0){
                    System.out.print("=");
                }
            }
            System.out.println("] Done!");
        }
        System.out.println("  Size of Phase1PruningTable: " + (count + 1));
        System.out.println("  Distribution of Values:");
        int[] dist = new int[13];

        //Print the distribution of the pruning table
        //See http://kociemba.org/math/distribution.htm
        for(int i = 0; i < flip.size; i++){
            for(int j = 0; j < 2187; j++){
                dist[Phase1PruningTable[i][j]]++;
            }
        }
        for(int i = 0; i < 13; i++){
            System.out.println("  " + i + ": " + dist[i]);
        }
        System.out.println("Done Initializing Phase1PruningTable!");

        //Process is similar for Phase2
        System.out.println("Initializing Phase2PruningTable:" );
        Phase2PruningTable = new byte[perm.size][40320];
        for(int i = 0; i < perm.size; i++){
            for(int j = 0; j < 40320; j++){
                Phase2PruningTable[i][j] = 42;
            }
        }
        count = 0;
        c = new CubieCube(true);
        Phase2PruningTable[0][0] = 0;
        //For each depth in pruning table
        for(int i = 0; i < 19; i++){
            System.out.print(("  Depth " + i + ": ["));
            //For each Corner Permutation Sym Coordinate
            for(int j = 0; j < perm.size; j++){
                //Foe each Edge Permutation Coordinate
                for(int k = 0; k < 40320; k++){
                    if(Phase2PruningTable[j][k] == i){
                        for(TurnAxis l : TurnAxis.values()){
                            for(int m = 0; m < 3; m++){
                                if(l != TurnAxis.U && l != TurnAxis.D){
                                    if(m != 1){
                                        continue;
                                    }
                                }
                                int CornPermRaw = (table.P2CornPermTwistMove[j][3 * l.ordinal() + m]);
                                int CornPerm = (int)(CornPermRaw / 16);
                                int EdgePermRaw = table.P2EdgePermTwistMove[k][3 * l.ordinal() + m];
                                int EdgePermCoord = table.P2EdgePermSym[EdgePermRaw][CornPermRaw % 16];
                                if(Phase2PruningTable[CornPerm][EdgePermCoord] == 42){
                                     Phase2PruningTable[CornPerm][EdgePermCoord] = (byte)(i + 1);
                                     count++;
                                }
                                int CornPermAsRaw = perm.CornPermToRaw[CornPerm][0];
                                if(table.P2CornPermSym[CornPerm][0] != CornPermAsRaw){
                                    System.out.println("Something is wrong in prune!");
                                }
                                for(int x = 1; x < 16; x++){
                                    if(table.P2CornPermSym[CornPerm][x] == CornPermAsRaw){
                                        int newEdgePermCoord = table.P2EdgePermSym[EdgePermCoord][x];
                                        if(Phase2PruningTable[CornPerm][newEdgePermCoord] == 42){
                                            Phase2PruningTable[CornPerm][newEdgePermCoord] = (byte)(i + 1);
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(j % 138 == 0){
                    System.out.print("=");
                }
            }
            System.out.println("] Done!");
        }
        System.out.println("  Size of Phase2PruningTable: " + (count + 1));
        System.out.println("  Distribution of Values:");
        dist = new int[19];
        for(int i = 0; i < perm.size; i++){
            for(int j = 0; j < 40320; j++){
                dist[Phase2PruningTable[i][j]]++;
            }
        }
        for(int i = 0; i < 19; i++){
            System.out.println("  " + i + ": " + dist[i]);
        }
        System.out.println("Done Initializing Phase2PruningTable!");
    }

}
