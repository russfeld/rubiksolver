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

/**
 * This class contains the algorithm to solve the given Rubik's Cube
 */
import java.util.LinkedList;
import rubiksolverdef.*;
import rubiksolver.RubikModel2.*;
import rubiksolver.Duple.*;
//import java.io.*;

public class RubikSolver2 {

    /**
     * This algorithm solves a given Rubik's Cube using my implementation
     * of Kociemba's Two-Phase algorithm
     *
     * @param cc - the cube to solve
     * @param table - the move tables to use
     * @param flip - the FlipUDSlice table to use
     * @param prune - the pruning tables to use
     * @param model - the model to use
     * @param perm - the CornerPerm table to use
     * @param parent - the parent window (for callbacks)
     */
   public static LinkedList<Integer> solve(CubieCube cc, TwistMoveTable table, FlipUDSlice flip, PruningTables prune, RubikModel2 model, CornerPerm perm, RubikDisplay2 parent){
       //PHASE 1

        java.util.LinkedList<Duple> queue = new java.util.LinkedList<Duple>();
        java.util.LinkedList<Duple> visited = new java.util.LinkedList<Duple>();
        queue.add(new Duple(model.CornOriCoord(cc.c), flip.FlipUDSliceCoord(cc) , model.EdgeOriCoord(cc.e), model.UDSliceCoord(cc), ""));
        visited.add(new Duple(model.CornOriCoord(cc.c), flip.FlipUDSliceCoord(cc) , model.EdgeOriCoord(cc.e), model.UDSliceCoord(cc), ""));
        boolean cont;
        boolean solved1 = false;
        Duple temp = null;
        java.util.LinkedList<Integer> moveList = new java.util.LinkedList<Integer>();
        while(!queue.isEmpty()){
            cont = false;
            temp = queue.removeFirst();
            if(temp.CornOriCoord == 0 && (int)(temp.FlipUDSliceCoord / 16) == 0){
                parent.updateText("Solved Phase 1! " + temp.backtrace);
                solved1 = true;
                break;
            }
            int oldFlip = temp.FlipUDSliceCoord;
            int oldCorn = temp.CornOriCoord;
            int oldEdge = temp.EdgeOriCoord;
            int oldUD = temp.UDSliceCoord;
            int oldstep = prune.Phase1PruningTable[(int)(oldFlip / 16)][table.CornOriSym[oldCorn][oldFlip % 16]];
            int oldSym = oldFlip % 16;
            System.out.println("Checking coordinates " + temp.CornOriCoord + " " + (int)(temp.FlipUDSliceCoord / 16) + " " + temp.FlipUDSliceCoord % 16 + " " + temp.backtrace + " " + oldstep);
            //apply all possible moves to cube
            for(TurnAxis l : TurnAxis.values()){
                for(int k = 0; k < 3; k++){
                    int newMove = model.SymMove[oldSym][3 * l.ordinal() + k];
                    int newFlipRaw = table.FlipUDSliceTwistMove[(int)(oldFlip / 16)][newMove];
                    int UDSliceCoord = (int)(newFlipRaw / 16);
                    int newEdgeRaw = table.EdgeOriTwistMove[oldEdge][3 * l.ordinal() + k];
                    int newUDRaw = table.UDSliceTwistMove[oldUD][3 * l.ordinal() + k];
                    int newUDSlice = (newUDRaw * 2048) + newEdgeRaw;
                    int newSym = model.SymComb[oldSym][newFlipRaw % 16];
                    //adjust symmetry index if needed!
                    if(table.FlipUDSliceSymInv[UDSliceCoord][newSym] != newUDSlice){
                        int j = 0;
                        for(j = 0; j < 16; j++){
                            if(table.FlipUDSliceSymInv[UDSliceCoord][j] == newUDSlice){
                                newSym = j;
                                break;
                            }
                        }
                        if(j == 16){
                            System.out.println("Error fixing Symmetry Index!");
                        }
                    }
                    int newCornRaw = table.CornOriTwistMove[oldCorn][3 * l.ordinal() + k];
                    int newCornSym = table.CornOriSym[newCornRaw][newSym];
                    int newstep = prune.Phase1PruningTable[(int)(newFlipRaw / 16)][newCornSym];
                    //if found a cube with a lower pruning depth, go with it
                    if(newstep < oldstep){
                        String tempMove = Move.values()[3 * l.ordinal() + k].name();
                        Duple tempD = new Duple(newCornRaw, (UDSliceCoord * 16) + newSym , newEdgeRaw, newUDRaw, temp.backtrace+" "+tempMove);
                        if(!visited.contains(tempD)){
                            moveList.add(3 * l.ordinal() + k);
                            queue.add(tempD);
                            visited.add(tempD);
                            cont = true;
                        }
                    }
                    if(cont){
                        break;
                    }
                }
                if(cont){
                    break;
                }
            }
        }
        if(!solved1){
            parent.updateText("Cannot Solve Phase 1!");
            return null;
        }
        //manually restore cube to Phase 1
        CubieCube tempCube = cc;
        int size = moveList.size();
        for(int i = 0; i < size; i++){
            int move = moveList.get(i);
            for(int j = 0; j < (move % 3) + 1; j++){
                tempCube = model.DoMove(tempCube, TurnAxis.values()[(int)(move / 3)]);
            }
        }
        if(model.CornOriCoord(tempCube.c) != 0 || model.EdgeOriCoord(tempCube.e) != 0 || model.UDSliceCoord(tempCube) != 0){
            System.out.println("Cube not restored to Phase2!");
        }else{
            System.out.println("Cube restored to Phase2!");
        }

        //PHASE 2
        java.util.LinkedList<Tuple> queue2 = new java.util.LinkedList<Tuple>();
        java.util.LinkedList<Tuple> visited2 = new java.util.LinkedList<Tuple>();
        queue2.add(new Tuple(perm.CornPermCoord(tempCube), model.Phase2EdgePermCoord(tempCube), model.UDSliceSortedCoord(tempCube), model.CornPermCoord(tempCube.c), "", new java.util.LinkedList<Integer>()));
        visited2.add(new Tuple(perm.CornPermCoord(tempCube), model.Phase2EdgePermCoord(tempCube), model.UDSliceSortedCoord(tempCube), model.CornPermCoord(tempCube.c), "", new java.util.LinkedList<Integer>()));
        boolean solved2 = false;
        Tuple temp2 = null;
        java.util.LinkedList<Integer> moveList2 = new java.util.LinkedList<Integer>();
        int backup = 0;
        //while(!queue2.isEmpty()){
        //make sure that we haven't hit a snag
        while(backup < 18){
            //if there is nothing in the queue, we need to backtrace to generate more cubes
            if(queue2.isEmpty()){
               backup++;
               queue2.add(new Tuple(perm.CornPermCoord(tempCube), model.Phase2EdgePermCoord(tempCube), model.UDSliceSortedCoord(tempCube), model.CornPermCoord(tempCube.c), "", new java.util.LinkedList<Integer>()));
               for(int i = 0; i < backup; i++){
                   if(i == 0){
                       System.out.println("Backing up " + backup + " Steps");
                   }
                   int sizeofQ = queue2.size();
                   //apply number of moves to each element in the queue
                   for(int q = 0; q < sizeofQ; q++){
                        temp2 = queue2.removeFirst();
                        int oldUDSlice = temp2.UDSliceCoord;
                        int oldCornPerm = temp2.CornPermCoord;
                        int oldEdgePerm = temp2.EdgePermCoord;
                        int oldCornPermRaw = temp2.CornPermRawCoord;
                        int oldstep = prune.Phase2PruningTable[(int)(oldCornPerm / 16)][table.P2EdgePermSym[oldEdgePerm][oldCornPerm % 16]];
                        int oldSym = oldCornPerm % 16;
                        System.out.println("Backing up coordinates " + temp2.EdgePermCoord + " " + (int)(temp2.CornPermCoord / 16) + " " + temp2.CornPermCoord % 16 + " " + temp2.UDSliceCoord + " " + temp2.backtrace + " " + oldstep);
                        //apply all moves to cubes in the queue
                        for(TurnAxis l : TurnAxis.values()){
                            if(temp2.moves.size() > 0){
                                if((int)(temp2.moves.getLast() / 3) == l.ordinal()){
                                    continue;
                                }
                            }
                            if(temp2.moves.size() > 1){
                                if(((int)(temp2.moves.getLast() / 3)) == ((l.ordinal() + 3) % 6) && ((int)(temp2.moves.get(temp2.moves.size() - 2) / 3)) == l.ordinal()){
                                    continue;

                                }
                            }
                            for(int k = 0; k < 3; k++){
                                if(l != TurnAxis.U && l != TurnAxis.D){
                                    if(k != 1){
                                        continue;
                                    }
                                }
                                int newMove = model.SymMove[oldSym][3 * l.ordinal() + k];
                                int newCornPermRaw = table.P2CornPermTwistMove[(int)(oldCornPerm / 16)][newMove];
                                int newCornPerm = (int)(newCornPermRaw / 16);
                                int newCornPermNoSym = table.P2CornPermRawTwistMove[oldCornPermRaw][3 * l.ordinal() + k];
                                int newUDSliceRaw = table.P2UDSliceTwistMove[oldUDSlice][3 * l.ordinal() + k];
                                int newSym = model.SymComb[oldSym][newCornPermRaw % 16];
                                //adjust symmetry index if needed
                                if(table.P2CornPermSymInv[newCornPerm][newSym] != newCornPermNoSym){
                                    int j = 0;
                                    for(j = 0; j < 16; j++){
                                        if(table.P2CornPermSymInv[newCornPerm][j] == newCornPermNoSym){
                                            newSym = j;
                                            break;
                                        }
                                    }
                                    if(j == 16){
                                        System.out.println("Error fixing Symmetry Index!");
                                    }
                                }
                                int newEdgePermRaw = table.P2EdgePermTwistMove[oldEdgePerm][3 * l.ordinal() + k];
                                int newEdgePermSym = table.P2EdgePermSym[newEdgePermRaw][newSym];
                                int newstep = prune.Phase2PruningTable[(int)(newCornPermRaw / 16)][newEdgePermSym];
                                if(newstep >= oldstep){
                                    String tempMove = Move.values()[3 * l.ordinal() + k].name();
                                    java.util.LinkedList<Integer> tempMoveList = new java.util.LinkedList<Integer>(temp2.moves);
                                    tempMoveList.add(3 * l.ordinal() + k);
                                    Tuple tempT = new Tuple((newCornPerm * 16) + newSym, newEdgePermRaw, newUDSliceRaw, newCornPermNoSym, temp2.backtrace+" "+tempMove, tempMoveList);
                                    if(i == backup - 1){
                                        //on last step, check if cube has already been visited
                                        if(!visited2.contains(tempT)){
                                            queue2.addLast(tempT);
                                            visited2.add(tempT);
                                        }
                                    }else{
                                        queue2.addLast(tempT);
                                    }
                                }
                            }
                        }
                   }
               }
            } //end of backup
            //if nothing added, backup further
            if(queue2.isEmpty()){
                continue;
            }
            temp2 = queue2.removeFirst();
            if(temp2.moves.size() >= 18){
                continue;
            }
            if(temp2.EdgePermCoord == 0 && temp2.UDSliceCoord == 0 && (int)(temp2.CornPermCoord / 16) == 0){
                parent.updateText("Solved Phase 2! " + temp2.backtrace);
                solved2 = true;
                moveList.addAll(temp2.moves);
                break;
            }
            int oldUDSlice = temp2.UDSliceCoord;
            int oldCornPerm = temp2.CornPermCoord;
            int oldEdgePerm = temp2.EdgePermCoord;
            int oldCornPermRaw = temp2.CornPermRawCoord;
            int oldstep = prune.Phase2PruningTable[(int)(oldCornPerm / 16)][table.P2EdgePermSym[oldEdgePerm][oldCornPerm % 16]];
            int oldSym = oldCornPerm % 16;
            System.out.println("Checking coordinates " + temp2.EdgePermCoord + " " + (int)(temp2.CornPermCoord / 16) + " " + temp2.CornPermCoord % 16 + " " + temp2.UDSliceCoord + " " + temp2.backtrace + " " + oldstep);
            //apply all possible moves to cube
            for(TurnAxis l : TurnAxis.values()){
                if(temp2.moves.size() > 0){
                    if((int)(temp2.moves.getLast() / 3) == l.ordinal()){
                        continue;
                    }
                }
                if(temp2.moves.size() > 1){
                    if(((int)(temp2.moves.getLast() / 3)) == ((l.ordinal() + 3) % 6) && ((int)(temp2.moves.get(temp2.moves.size() - 2) / 3)) == l.ordinal()){
                        continue;

                    }
                }
                for(int k = 0; k < 3; k++){
                    if(l != TurnAxis.U && l != TurnAxis.D){
                        if(k != 1){
                            continue;
                        }
                    }
                    int newMove = model.SymMove[oldSym][3 * l.ordinal() + k];
                    int newCornPermRaw = table.P2CornPermTwistMove[(int)(oldCornPerm / 16)][newMove];
                    int newCornPerm = (int)(newCornPermRaw / 16);
                    int newCornPermNoSym = table.P2CornPermRawTwistMove[oldCornPermRaw][3 * l.ordinal() + k];
                    int newUDSliceRaw = table.P2UDSliceTwistMove[oldUDSlice][3 * l.ordinal() + k];
                    int newSym = model.SymComb[oldSym][newCornPermRaw % 16];
                    if(table.P2CornPermSymInv[newCornPerm][newSym] != newCornPermNoSym){
                        int j = 0;
                        for(j = 0; j < 16; j++){
                            if(table.P2CornPermSymInv[newCornPerm][j] == newCornPermNoSym){
                                newSym = j;
                                break;
                            }
                        }
                        if(j == 16){
                            System.out.println("Error fixing Symmetry Index!");
                        }
                    }
                    int newEdgePermRaw = table.P2EdgePermTwistMove[oldEdgePerm][3 * l.ordinal() + k];
                    int newEdgePermSym = table.P2EdgePermSym[newEdgePermRaw][newSym];
                    int newstep = prune.Phase2PruningTable[(int)(newCornPermRaw / 16)][newEdgePermSym];
                    //add all cubes with a lower pruning depth to the queue
                    if(newstep < oldstep){
                        String tempMove = Move.values()[3 * l.ordinal() + k].name();
                        java.util.LinkedList<Integer> tempMoveList = new java.util.LinkedList<Integer>(temp2.moves);
                        tempMoveList.add(3 * l.ordinal() + k);
                        Tuple tempT = new Tuple((newCornPerm * 16) + newSym, newEdgePermRaw, newUDSliceRaw, newCornPermNoSym, temp2.backtrace+" "+tempMove, tempMoveList);
                        if(!visited2.contains(tempT)){
                            queue2.addFirst(tempT);
                            visited2.add(tempT);
                        }
                    }
                }
            }
        }
        if(!solved2){
            parent.updateText("Cannot Solve Phase 2!");
            return null;
        }
        return moveList;
}






/*
    public static void reachable(TwistMoveTable table, FlipUDSlice flip, PruningTables prune, RubikModel2 model){
        CubieCube c = new CubieCube(true);
        for(int i = 0; i < flip.size; i++){
            for(int j = 0; j < 2187; j++){
                int oldstep = prune.Phase1PruningTable[i][j];
                boolean ok = false;
                if(oldstep == 0){
                    System.out.println("Found Base Node of " + i + " " + j);
                }
                for(TurnAxis l : TurnAxis.values()){
                    for(int k = 0; k < 3; k++){
                        int newFlipRaw = table.FlipUDSliceTwistMove[i][3 * l.ordinal() + k];
                        int UDSliceCoord = (int)(newFlipRaw / 16);
                        int newCornRaw = table.CornOriTwistMove[j][3 * l.ordinal() + k];
                        int newCornSym = table.CornOriSym[newCornRaw][newFlipRaw % 16];
                        int newstep = prune.Phase1PruningTable[(int)(newFlipRaw / 16)][newCornSym];
                        if(newstep < oldstep){
                            ok = true;
                            break;
                        }
                        int UDSliceRaw = flip.FlipUDSliceToRaw[UDSliceCoord][0];
                        /*
                        for(int x = 1; x < 16; x++){
                            if(table.FlipUDSliceSym[UDSliceCoord][x] == UDSliceRaw){
                                int newCornOriCoord = table.CornOriSym[newCornSym][x];
                                newstep = prune.Phase1PruningTable[UDSliceCoord][newCornOriCoord];
                                if(newstep < oldstep){
                                    ok = true;
                                    break;
                                }
                            }
                        }
                         * 
                    }
                    if(ok == true){
                        break;
                    }
                }
                if(ok != true){
                    System.out.println("Cannot find path for cube " + i + " " + j + " " + oldstep);
                }else{
                    ok = false;
                }
            }
            if(i % 100 == 0){
                System.out.println(i);
            }
        }
        System.out.println("DONE!");
    }
    */
}
