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
import rubiksolverdef.*;


import rubiksolver.RubikModel2.*;

/**
 * This class represents the move tables for all raw and sym coordinates
 */
public class TwistMoveTable implements Serializable{
    //phase 1
    public int[][] CornOriTwistMove;
    public int[][] CornOriSym;
    public int[][] EdgeOriTwistMove;
    public int[][] UDSliceTwistMove;
    public int[][] FlipUDSliceTwistMove;
    public int[][] FlipUDSliceSym;
    public int[][] FlipUDSliceSymInv;

    //phase 2
    public int[][] P2EdgePermTwistMove;
    public int[][] P2EdgePermSym;
    public int[][] P2UDSliceTwistMove;
    public int[][] P2CornPermTwistMove;
    public int[][] P2CornPermRawTwistMove;
    public int[][] P2CornPermSym;
    public int[][] P2CornPermSymInv;

    /**
     * This function creates the move tables
     *
     * @param model - a RubikModel2
     * @param flip - a FlipUDSlice Table
     * @param perm - a CornerPerm table
     */
    public TwistMoveTable(RubikModel2 model, FlipUDSlice flip, CornerPerm perm){
        //PHASE 1
        System.out.println("Initializing Move Tables:" );


        CornOriTwistMove = new int[2187][Move.values().length];
        CornOriSym = new int[2187][16];
        CubieCube c = new CubieCube(true);
        int max = 0;
        int min = Integer.MAX_VALUE;
        //for each possible value
        for(int i = 0; i < 2187; i++){ //2187 = 3 ^ 7
            //Create a cube with that coordinate
            c.InvCoord(i, 0);
            //apply all 18 moves to that cube
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    int temp = model.CornOriCoord(c.c);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    //store the result in the table
                    CornOriTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
            //also apply all 16 symmetries to the cube
            for(int j = 0; j < 16; j++){
                CubieCube tempC = model.SthenSinv(c, j);
                int temp = model.CornOriCoord(tempC.c);
                //store the result in the table
                CornOriSym[i][j] = temp;
            }
        }
        System.out.println("  CornOriCoord Range: " + min + " - " + max);


        max = 0;
        min = Integer.MAX_VALUE;
        EdgeOriTwistMove = new int[2048][Move.values().length];
        c = new CubieCube(true);
        for(int i = 0; i < 2048; i++){ 
            c.InvCoord(0, i);
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    int temp = model.EdgeOriCoord(c.e);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    EdgeOriTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
        }
        System.out.println("  EdgeOriCoord Range: " + min + " - " + max);


        max = 0;
        min = Integer.MAX_VALUE;
        UDSliceTwistMove = new int[495][Move.values().length];
        c = new CubieCube(true);
        for(int i = 0; i < 495; i++){ 
            c.InvCoord(i);
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    int temp = model.UDSliceCoord(c);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    UDSliceTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
        }
        System.out.println("  UDSliceCoord Range: " + min + " - " + max);
        System.out.print("  Calculating FlipUDSlice Tables: [");


        max = 0;
        min = Integer.MAX_VALUE;
        FlipUDSliceTwistMove = new int[64430][Move.values().length];
        FlipUDSliceSym = new int[64430][16];
        FlipUDSliceSymInv = new int[64430][16];
        c = new CubieCube(true);
        for(int i = 0; i < flip.size; i++){
            int n = flip.FlipUDSliceToRaw[i][0];
            int oldUDSliceCoord = (int)(n / 2048);
            int oldEdgeOriCoord = (int)(n % 2048);
            for(int j = 0; j < 6 * 3; j++){
                int newUDSliceCoord = UDSliceTwistMove[oldUDSliceCoord][j];
                int newEdgeOriCoord = EdgeOriTwistMove[oldEdgeOriCoord][j];
                c = new CubieCube(true);
                c.InvCoord(newUDSliceCoord);
                c.InvCoord(0, newEdgeOriCoord);
                int temp = flip.FlipUDSliceCoord(c);
                if(temp > max) max = temp;
                if(temp < min) min = temp;
                FlipUDSliceTwistMove[i][j] = temp;
            }
            for(int j = 0; j < 16; j++){
                c = new CubieCube(true);
                c.InvCoord(oldUDSliceCoord);
                c.InvCoord(0, oldEdgeOriCoord);
                CubieCube tempC = model.SthenSinv(c, j);
                int newUDSliceCoord = model.UDSliceCoord(tempC);
                int newEdgeOriCoord = model.EdgeOriCoord(tempC.e);
                int temp = (newUDSliceCoord * 2048) + newEdgeOriCoord;
                FlipUDSliceSym[i][j] = temp;
            }
            for(int j = 0; j < 16; j++){
                c = new CubieCube(true);
                c.InvCoord(oldUDSliceCoord);
                c.InvCoord(0, oldEdgeOriCoord);
                CubieCube tempC = model.SinvThenS(c, j);
                int newUDSliceCoord = model.UDSliceCoord(tempC);
                int newEdgeOriCoord = model.EdgeOriCoord(tempC.e);
                int temp = (newUDSliceCoord * 2048) + newEdgeOriCoord;
                FlipUDSliceSymInv[i][j] = temp;
            }
            if(i % 3220 == 0){
                System.out.print("=");
            }
        }
        System.out.println("] Done!");
        System.out.println("  FlipUDSliceCoord Range: " + min + " - " + max);


        //PHASE 2
        P2EdgePermTwistMove = new int[40320][Move.values().length];
        P2EdgePermSym = new int[40320][16];
        c = new CubieCube(true);
        max = 0;
        min = Integer.MAX_VALUE;
        for(int i = 0; i < 40320; i++){
            c.InvEdgeCoord(i);
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    if(j != TurnAxis.U && j != TurnAxis.D){
                        if(k != 1){
                            continue;
                        }
                    }
                    int temp = model.Phase2EdgePermCoord(c);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    P2EdgePermTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
            for(int j = 0; j < 16; j++){
                CubieCube tempC = model.SthenSinv(c, j);
                int temp = model.Phase2EdgePermCoord(tempC);
                P2EdgePermSym[i][j] = temp;
            }
        }
        System.out.println("  Phase2EdgePermCoord Range: " + min + " - " + max);


        P2UDSliceTwistMove = new int[24][Move.values().length];
        c = new CubieCube(true);
        max = 0;
        min = Integer.MAX_VALUE;
        for(int i = 0; i < 24; i++){
            c.InvP2UDSliceCoord(i);
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    if(j != TurnAxis.U && j != TurnAxis.D){
                        if(k != 1){
                            continue;
                        }
                    }
                    int temp = model.UDSliceSortedCoord(c);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    P2UDSliceTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
        }
        System.out.println("  Phase2UDSliceCoord Range: " + min + " - " + max);


        P2CornPermRawTwistMove = new int[40320][Move.values().length];
        c = new CubieCube(true);
        max = 0;
        min = Integer.MAX_VALUE;
        for(int i = 0; i < 40320; i++){
            c.InvCornCoord(i);
            for(TurnAxis j : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    c = model.DoMove(c, j);
                    if(j != TurnAxis.U && j != TurnAxis.D){
                        if(k != 1){
                            continue;
                        }
                    }
                    int temp = model.CornPermCoord(c.c);
                    if(temp > max) max = temp;
                    if(temp < min) min = temp;
                    P2CornPermRawTwistMove[i][(3 * j.ordinal()) + k] = temp;
                }
                c = model.DoMove(c, j);
            }
        }
        System.out.println("  CornPermRawCoord Range: " + min + " - " + max);


        System.out.print("  Calculating CornerPerm Tables: [");
        max = 0;
        min = Integer.MAX_VALUE;
        P2CornPermTwistMove = new int[perm.size][Move.values().length];
        P2CornPermSym = new int[perm.size][16];
        P2CornPermSymInv = new int[perm.size][16];
        c = new CubieCube(true);
        for(int i = 0; i < perm.size; i++){
            int OldCornPermCoord = perm.CornPermToRaw[i][0];
            for(int j = 0; j < 6 * 3; j++){
                int newCornPermCoord = P2CornPermRawTwistMove[OldCornPermCoord][j];
                c = new CubieCube(true);
                c.InvCornCoord(newCornPermCoord);
                int temp = perm.CornPermCoord(c);
                if(temp > max) max = temp;
                if(temp < min) min = temp;
                P2CornPermTwistMove[i][j] = temp;
            }
            for(int j = 0; j < 16; j++){
                c = new CubieCube(true);
                c.InvCornCoord(OldCornPermCoord);
                CubieCube tempC = model.SthenSinv(c, j);
                int newCornPermCoord = model.CornPermCoord(tempC.c);
                P2CornPermSym[i][j] = newCornPermCoord;
            }
            for(int j = 0; j < 16; j++){
                c = new CubieCube(true);
                c.InvCornCoord(OldCornPermCoord);
                CubieCube tempC = model.SinvThenS(c, j);
                int newCornPermCoord = model.CornPermCoord(tempC.c);
                P2CornPermSymInv[i][j] = newCornPermCoord;
            }
            if(i % 138 == 0){
                System.out.print("=");
            }
        }
        System.out.println("] Done!");
        System.out.println("  CornerPermCoord Range: " + min + " - " + max);

        System.out.println("Done Calculating Move Tables!");
    }
}
