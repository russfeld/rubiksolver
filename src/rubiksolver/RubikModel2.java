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

import java.awt.Color;
import java.io.Serializable;
import rubiksolverdef.*;
import java.lang.Math.*;

/**
 * This class initializes and represents the main underlying structure for the
 * Rubik's Cube puzzle
 *
 * See http://kociemba.org/math/CubeDefs.htm
 */
public class RubikModel2 implements Serializable {
    
    public static enum TurnAxis {U, R, F, D, L, B};
    public static enum Move {Ux1, Ux2, Ux3 ,Rx1, Rx2, Rx3, Fx1, Fx2, Fx3, Dx1, Dx2, Dx3, Lx1, Lx2, Lx3, Bx1, Bx2, Bx3};
    public static enum ColorIndex {UCol, RCol, FCol, DCol, LCol, BCol, NoCol};
    public static enum Colors{Orange, Blue, White, Red, Green, Yellow, Black};
    public static enum Corner {URF, UFL, ULB, UBR, DFR, DLF, DBL, DRB};
    public static enum Edge {UR, UF, UL, UB, DR, DF, DL, DB, FR, FL, BL, BR};
    public static enum Face {U1, U2, U3, U4, U5, U6, U7, U8, U9, R1, R2, R3, R4, R5, R6, R7, R8, R9, F1, F2, F3, F4, F5, F6, F7, F8, F9, D1, D2, D3, D4, D5, D6, D7, D8, D9, L1, L2, L3, L4, L5, L6, L7, L8, L9, B1, B2, B3, B4, B5, B6, B7, B8, B9};
    public static enum Symmetry {S_URF3, S_F2, S_U4, S_LR2};
    
    public CornerColorIndex CCI;
    public CornerFacelet CF;
    public EdgeColorIndex ECI;
    public EdgeColorIndex ECIAlt;
    public EdgeFacelet EF;
    public EdgeFacelet EFAlt;
    public EdgeNeighbor EN;
    public Facelet[] FaceletMove;
    public Facelet[] FaceletSym;
    public CornerCubie[] CornerCubieMove;
    public CornerCubie[] CornerCubieSym;
    public EdgeCubie[] EdgeCubieMove;
    public EdgeCubie[] EdgeCubieSym;
    public CubieCube[] Symmetries;
    public CubieCube[] SymmetriesInv;
    public int[][] SymMove;
    public int[][] SymMoveInv;
    public int[][] SymComb;
    public int[][] SymFace;
    
    /** Creates a new instance of RubikModel2 */
    public RubikModel2() {
        //Colors of Corner Cubies
        CCI = new CornerColorIndex();
        CCI.a[Corner.URF.ordinal()][0] = ColorIndex.UCol;
        CCI.a[Corner.URF.ordinal()][1] = ColorIndex.RCol;
        CCI.a[Corner.URF.ordinal()][2] = ColorIndex.FCol;
        
        CCI.a[Corner.UFL.ordinal()][0] = ColorIndex.UCol;
        CCI.a[Corner.UFL.ordinal()][1] = ColorIndex.FCol;
        CCI.a[Corner.UFL.ordinal()][2] = ColorIndex.LCol;
        
        CCI.a[Corner.ULB.ordinal()][0] = ColorIndex.UCol;
        CCI.a[Corner.ULB.ordinal()][1] = ColorIndex.LCol;
        CCI.a[Corner.ULB.ordinal()][2] = ColorIndex.BCol;
        
        CCI.a[Corner.UBR.ordinal()][0] = ColorIndex.UCol;
        CCI.a[Corner.UBR.ordinal()][1] = ColorIndex.BCol;
        CCI.a[Corner.UBR.ordinal()][2] = ColorIndex.RCol;
        
        CCI.a[Corner.DFR.ordinal()][0] = ColorIndex.DCol;
        CCI.a[Corner.DFR.ordinal()][1] = ColorIndex.FCol;
        CCI.a[Corner.DFR.ordinal()][2] = ColorIndex.RCol;
        
        CCI.a[Corner.DLF.ordinal()][0] = ColorIndex.DCol;
        CCI.a[Corner.DLF.ordinal()][1] = ColorIndex.LCol;
        CCI.a[Corner.DLF.ordinal()][2] = ColorIndex.FCol;
        
        CCI.a[Corner.DBL.ordinal()][0] = ColorIndex.DCol;
        CCI.a[Corner.DBL.ordinal()][1] = ColorIndex.BCol;
        CCI.a[Corner.DBL.ordinal()][2] = ColorIndex.LCol;
        
        CCI.a[Corner.DRB.ordinal()][0] = ColorIndex.DCol;
        CCI.a[Corner.DRB.ordinal()][1] = ColorIndex.RCol;
        CCI.a[Corner.DRB.ordinal()][2] = ColorIndex.BCol;
        
        
        //involved facelets of corner cubies
        CF = new CornerFacelet();
        CF.a[Corner.URF.ordinal()][0] = Face.U9;
        CF.a[Corner.URF.ordinal()][1] = Face.R1;
        CF.a[Corner.URF.ordinal()][2] = Face.F3;
        
        CF.a[Corner.UFL.ordinal()][0] = Face.U7;
        CF.a[Corner.UFL.ordinal()][1] = Face.F1;
        CF.a[Corner.UFL.ordinal()][2] = Face.L3;
        
        CF.a[Corner.ULB.ordinal()][0] = Face.U1;
        CF.a[Corner.ULB.ordinal()][1] = Face.L1;
        CF.a[Corner.ULB.ordinal()][2] = Face.B3;
        
        CF.a[Corner.UBR.ordinal()][0] = Face.U3;
        CF.a[Corner.UBR.ordinal()][1] = Face.B1;
        CF.a[Corner.UBR.ordinal()][2] = Face.R3;
        
        CF.a[Corner.DFR.ordinal()][0] = Face.D3;
        CF.a[Corner.DFR.ordinal()][1] = Face.F9;
        CF.a[Corner.DFR.ordinal()][2] = Face.R7;
        
        CF.a[Corner.DLF.ordinal()][0] = Face.D1;
        CF.a[Corner.DLF.ordinal()][1] = Face.L9;
        CF.a[Corner.DLF.ordinal()][2] = Face.F7;
        
        CF.a[Corner.DBL.ordinal()][0] = Face.D7;
        CF.a[Corner.DBL.ordinal()][1] = Face.B9;
        CF.a[Corner.DBL.ordinal()][2] = Face.L7;
        
        CF.a[Corner.DRB.ordinal()][0] = Face.D9;
        CF.a[Corner.DRB.ordinal()][1] = Face.R9;
        CF.a[Corner.DRB.ordinal()][2] = Face.B7;
        
        //Colors of the edge cubies
        ECI = new EdgeColorIndex();
        ECI.a[Edge.UR.ordinal()][0] = ColorIndex.UCol;
        ECI.a[Edge.UR.ordinal()][1] = ColorIndex.RCol;
        
        ECI.a[Edge.UF.ordinal()][0] = ColorIndex.UCol;
        ECI.a[Edge.UF.ordinal()][1] = ColorIndex.FCol;
        
        ECI.a[Edge.UL.ordinal()][0] = ColorIndex.UCol;
        ECI.a[Edge.UL.ordinal()][1] = ColorIndex.LCol;
        
        ECI.a[Edge.UB.ordinal()][0] = ColorIndex.UCol;
        ECI.a[Edge.UB.ordinal()][1] = ColorIndex.BCol;
        
        ECI.a[Edge.DR.ordinal()][0] = ColorIndex.DCol;
        ECI.a[Edge.DR.ordinal()][1] = ColorIndex.RCol;
        
        ECI.a[Edge.DF.ordinal()][0] = ColorIndex.DCol;
        ECI.a[Edge.DF.ordinal()][1] = ColorIndex.FCol;
        
        ECI.a[Edge.DL.ordinal()][0] = ColorIndex.DCol;
        ECI.a[Edge.DL.ordinal()][1] = ColorIndex.LCol;
        
        ECI.a[Edge.DB.ordinal()][0] = ColorIndex.DCol;
        ECI.a[Edge.DB.ordinal()][1] = ColorIndex.BCol;
        
        ECI.a[Edge.FR.ordinal()][0] = ColorIndex.FCol;
        ECI.a[Edge.FR.ordinal()][1] = ColorIndex.RCol;
        
        ECI.a[Edge.FL.ordinal()][0] = ColorIndex.FCol;
        ECI.a[Edge.FL.ordinal()][1] = ColorIndex.LCol;
        
        ECI.a[Edge.BL.ordinal()][0] = ColorIndex.BCol;
        ECI.a[Edge.BL.ordinal()][1] = ColorIndex.LCol;
        
        ECI.a[Edge.BR.ordinal()][0] = ColorIndex.BCol;
        ECI.a[Edge.BR.ordinal()][1] = ColorIndex.RCol;
        
        //Alternate ECI
        ECIAlt = new EdgeColorIndex();
        ECIAlt.a[Edge.UR.ordinal()][0] = ColorIndex.RCol;
        ECIAlt.a[Edge.UR.ordinal()][1] = ColorIndex.UCol;
        
        ECIAlt.a[Edge.UF.ordinal()][0] = ColorIndex.UCol;
        ECIAlt.a[Edge.UF.ordinal()][1] = ColorIndex.FCol;
        
        ECIAlt.a[Edge.UL.ordinal()][0] = ColorIndex.LCol;
        ECIAlt.a[Edge.UL.ordinal()][1] = ColorIndex.UCol;
        
        ECIAlt.a[Edge.UB.ordinal()][0] = ColorIndex.UCol;
        ECIAlt.a[Edge.UB.ordinal()][1] = ColorIndex.BCol;
        
        ECIAlt.a[Edge.DR.ordinal()][0] = ColorIndex.RCol;
        ECIAlt.a[Edge.DR.ordinal()][1] = ColorIndex.DCol;
        
        ECIAlt.a[Edge.DF.ordinal()][0] = ColorIndex.DCol;
        ECIAlt.a[Edge.DF.ordinal()][1] = ColorIndex.FCol;
        
        ECIAlt.a[Edge.DL.ordinal()][0] = ColorIndex.LCol;
        ECIAlt.a[Edge.DL.ordinal()][1] = ColorIndex.DCol;
        
        ECIAlt.a[Edge.DB.ordinal()][0] = ColorIndex.DCol;
        ECIAlt.a[Edge.DB.ordinal()][1] = ColorIndex.BCol;
        
        ECIAlt.a[Edge.FR.ordinal()][0] = ColorIndex.FCol;
        ECIAlt.a[Edge.FR.ordinal()][1] = ColorIndex.RCol;
        
        ECIAlt.a[Edge.FL.ordinal()][0] = ColorIndex.FCol;
        ECIAlt.a[Edge.FL.ordinal()][1] = ColorIndex.LCol;
        
        ECIAlt.a[Edge.BL.ordinal()][0] = ColorIndex.BCol;
        ECIAlt.a[Edge.BL.ordinal()][1] = ColorIndex.LCol;
        
        ECIAlt.a[Edge.BR.ordinal()][0] = ColorIndex.BCol;
        ECIAlt.a[Edge.BR.ordinal()][1] = ColorIndex.RCol;
        
        //involved facelets of the edge cubies
        EF = new EdgeFacelet();
        EF.a[Edge.UR.ordinal()][0] = Face.U6;
        EF.a[Edge.UR.ordinal()][1] = Face.R2;
        
        EF.a[Edge.UF.ordinal()][0] = Face.U8;
        EF.a[Edge.UF.ordinal()][1] = Face.F2;
        
        EF.a[Edge.UL.ordinal()][0] = Face.U4;
        EF.a[Edge.UL.ordinal()][1] = Face.L2;
        
        EF.a[Edge.UB.ordinal()][0] = Face.U2;
        EF.a[Edge.UB.ordinal()][1] = Face.B2;
        
        EF.a[Edge.DR.ordinal()][0] = Face.D6;
        EF.a[Edge.DR.ordinal()][1] = Face.R8;
        
        EF.a[Edge.DF.ordinal()][0] = Face.D2;
        EF.a[Edge.DF.ordinal()][1] = Face.F8;
        
        EF.a[Edge.DL.ordinal()][0] = Face.D4;
        EF.a[Edge.DL.ordinal()][1] = Face.L8;
        
        EF.a[Edge.DB.ordinal()][0] = Face.D8;
        EF.a[Edge.DB.ordinal()][1] = Face.B8;
        
        EF.a[Edge.FR.ordinal()][0] = Face.F6;
        EF.a[Edge.FR.ordinal()][1] = Face.R4;
        
        EF.a[Edge.FL.ordinal()][0] = Face.F4;
        EF.a[Edge.FL.ordinal()][1] = Face.L6;
        
        EF.a[Edge.BL.ordinal()][0] = Face.B6;
        EF.a[Edge.BL.ordinal()][1] = Face.L4;
        
        EF.a[Edge.BR.ordinal()][0] = Face.B4;
        EF.a[Edge.BR.ordinal()][1] = Face.R6;
        
        //Alt EF
        EFAlt = new EdgeFacelet();
        EFAlt.a[Edge.UR.ordinal()][0] = Face.R2;
        EFAlt.a[Edge.UR.ordinal()][1] = Face.U6;
        
        EFAlt.a[Edge.UF.ordinal()][0] = Face.U8;
        EFAlt.a[Edge.UF.ordinal()][1] = Face.F2;
        
        EFAlt.a[Edge.UL.ordinal()][0] = Face.L2;
        EFAlt.a[Edge.UL.ordinal()][1] = Face.U4;
        
        EFAlt.a[Edge.UB.ordinal()][0] = Face.U2;
        EFAlt.a[Edge.UB.ordinal()][1] = Face.B2;
        
        EFAlt.a[Edge.DR.ordinal()][0] = Face.R8;
        EFAlt.a[Edge.DR.ordinal()][1] = Face.D6;
        
        EFAlt.a[Edge.DF.ordinal()][0] = Face.D2;
        EFAlt.a[Edge.DF.ordinal()][1] = Face.F8;
        
        EFAlt.a[Edge.DL.ordinal()][0] = Face.L8;
        EFAlt.a[Edge.DL.ordinal()][1] = Face.D4;
        
        EFAlt.a[Edge.DB.ordinal()][0] = Face.D8;
        EFAlt.a[Edge.DB.ordinal()][1] = Face.B8;
        
        EFAlt.a[Edge.FR.ordinal()][0] = Face.F6;
        EFAlt.a[Edge.FR.ordinal()][1] = Face.R4;
        
        EFAlt.a[Edge.FL.ordinal()][0] = Face.F4;
        EFAlt.a[Edge.FL.ordinal()][1] = Face.L6;
        
        EFAlt.a[Edge.BL.ordinal()][0] = Face.B6;
        EFAlt.a[Edge.BL.ordinal()][1] = Face.L4;
        
        EFAlt.a[Edge.BR.ordinal()][0] = Face.B4;
        EFAlt.a[Edge.BR.ordinal()][1] = Face.R6;
        
        //neighbor corners of the edges
        EN = new EdgeNeighbor();
        EN.a[Edge.UR.ordinal()][0] = Corner.URF;
        EN.a[Edge.UR.ordinal()][1] = Corner.UBR;
        
        EN.a[Edge.UF.ordinal()][0] = Corner.UFL;
        EN.a[Edge.UF.ordinal()][1] = Corner.URF;
        
        EN.a[Edge.UL.ordinal()][0] = Corner.ULB;
        EN.a[Edge.UL.ordinal()][1] = Corner.UFL;
        
        EN.a[Edge.UB.ordinal()][0] = Corner.UBR;
        EN.a[Edge.UB.ordinal()][1] = Corner.ULB;
        
        EN.a[Edge.DR.ordinal()][0] = Corner.DRB;
        EN.a[Edge.DR.ordinal()][1] = Corner.DFR;
        
        EN.a[Edge.DF.ordinal()][0] = Corner.DFR;
        EN.a[Edge.DF.ordinal()][1] = Corner.DLF;
        
        EN.a[Edge.DL.ordinal()][0] = Corner.DLF;
        EN.a[Edge.DL.ordinal()][1] = Corner.DBL;
        
        EN.a[Edge.DB.ordinal()][0] = Corner.DBL;
        EN.a[Edge.DB.ordinal()][1] = Corner.DRB;
        
        EN.a[Edge.FR.ordinal()][0] = Corner.URF;
        EN.a[Edge.FR.ordinal()][1] = Corner.DFR;
        
        EN.a[Edge.FL.ordinal()][0] = Corner.DLF;
        EN.a[Edge.FL.ordinal()][1] = Corner.UFL;
        
        EN.a[Edge.BL.ordinal()][0] = Corner.DBL;
        EN.a[Edge.BL.ordinal()][1] = Corner.ULB;
        
        EN.a[Edge.BR.ordinal()][0] = Corner.UBR;
        EN.a[Edge.BR.ordinal()][1] = Corner.DRB;
        
        
        
        //permutations of the facelets by faceturns
        FaceletMove = new Facelet[TurnAxis.values().length];
        Face[] temp = {Face.U3, Face.U6, Face.U9, Face.U2, Face.U5, Face.U8, Face.U1, Face.U4, Face.U7, 
                       Face.F1, Face.F2, Face.F3, Face.R4, Face.R5, Face.R6, Face.R7, Face.R8, Face.R9,
                       Face.L1, Face.L2, Face.L3, Face.F4, Face.F5, Face.F6, Face.F7, Face.F8, Face.F9,
                       Face.D1, Face.D2, Face.D3, Face.D4, Face.D5, Face.D6, Face.D7, Face.D8, Face.D9,
                       Face.B1, Face.B2, Face.B3, Face.L4, Face.L5, Face.L6, Face.L7, Face.L8, Face.L9,
                       Face.R1, Face.R2, Face.R3, Face.B4, Face.B5, Face.B6, Face.B7, Face.B8, Face.B9};
        FaceletMove[TurnAxis.U.ordinal()] = new Facelet();
        System.arraycopy(temp, 0, FaceletMove[TurnAxis.U.ordinal()].a ,0, Face.values().length);
        
        Face[] temp2= {Face.U1, Face.U2, Face.B7, Face.U4, Face.U5, Face.B4, Face.U7, Face.U8, Face.B1, 
                       Face.R3, Face.R6, Face.R9, Face.R2, Face.R5, Face.R8, Face.R1, Face.R4, Face.R7,
                       Face.F1, Face.F2, Face.U3, Face.F4, Face.F5, Face.U6, Face.F7, Face.F8, Face.U9,
                       Face.D1, Face.D2, Face.F3, Face.D4, Face.D5, Face.F6, Face.D7, Face.D8, Face.F9,
                       Face.L1, Face.L2, Face.L3, Face.L4, Face.L5, Face.L6, Face.L7, Face.L8, Face.L9,
                       Face.D9, Face.B2, Face.B3, Face.D6, Face.B5, Face.B6, Face.D3, Face.B8, Face.B9};
        FaceletMove[TurnAxis.R.ordinal()] = new Facelet();
        System.arraycopy(temp2, 0, FaceletMove[TurnAxis.R.ordinal()].a ,0, Face.values().length);
        
        Face[] temp3= {Face.U1, Face.U2, Face.U3, Face.U4, Face.U5, Face.B4, Face.R1, Face.R4, Face.R7, 
                       Face.D3, Face.R2, Face.R3, Face.D2, Face.R5, Face.R6, Face.D1, Face.R8, Face.R9,
                       Face.F3, Face.F6, Face.F9, Face.F2, Face.F5, Face.F8, Face.F1, Face.F4, Face.F7,
                       Face.L3, Face.L6, Face.L9, Face.D4, Face.D5, Face.D6, Face.D7, Face.D8, Face.D9,
                       Face.L1, Face.L2, Face.U9, Face.L4, Face.L5, Face.U8, Face.L7, Face.L8, Face.U7,
                       Face.B1, Face.B2, Face.B3, Face.B4, Face.B5, Face.B6, Face.B7, Face.B8, Face.B9};
        FaceletMove[TurnAxis.F.ordinal()] = new Facelet();
        System.arraycopy(temp3, 0, FaceletMove[TurnAxis.F.ordinal()].a ,0, Face.values().length);
        
        Face[] temp4= {Face.U1, Face.U2, Face.U3, Face.U4, Face.U5, Face.U6, Face.U7, Face.U8, Face.U9, 
                       Face.R1, Face.R2, Face.R3, Face.R4, Face.R5, Face.R6, Face.B7, Face.B8, Face.B9,
                       Face.F1, Face.F2, Face.F3, Face.F4, Face.F5, Face.F6, Face.R7, Face.R8, Face.R9,
                       Face.D3, Face.D6, Face.D9, Face.D2, Face.D5, Face.D8, Face.D1, Face.D4, Face.D7,
                       Face.L1, Face.L2, Face.L3, Face.L4, Face.L5, Face.L6, Face.F7, Face.F8, Face.F9,
                       Face.B1, Face.B2, Face.B3, Face.B4, Face.B5, Face.B6, Face.L7, Face.L8, Face.L9};
        FaceletMove[TurnAxis.D.ordinal()] = new Facelet();
        System.arraycopy(temp4, 0, FaceletMove[TurnAxis.D.ordinal()].a ,0, Face.values().length);
        
        Face[] temp5= {Face.F1, Face.U2, Face.U3, Face.F4, Face.U5, Face.U6, Face.F7, Face.U8, Face.U9, 
                       Face.R1, Face.R2, Face.R3, Face.R4, Face.R5, Face.R6, Face.R7, Face.R8, Face.R9,
                       Face.D1, Face.F2, Face.F3, Face.D4, Face.F5, Face.F6, Face.D7, Face.F8, Face.F9,
                       Face.B9, Face.D2, Face.D3, Face.B6, Face.D5, Face.D6, Face.B3, Face.D8, Face.D9,
                       Face.L3, Face.L6, Face.L9, Face.L2, Face.L5, Face.L8, Face.L1, Face.L4, Face.L7,
                       Face.B1, Face.B2, Face.U7, Face.B4, Face.B5, Face.U4, Face.B7, Face.B8, Face.U1};
        FaceletMove[TurnAxis.L.ordinal()] = new Facelet();
        System.arraycopy(temp5, 0, FaceletMove[TurnAxis.L.ordinal()].a ,0, Face.values().length);
        
        Face[] temp6= {Face.L7, Face.L4, Face.L1, Face.U4, Face.U5, Face.U6, Face.U7, Face.U8, Face.U9, 
                       Face.R1, Face.R2, Face.U1, Face.R4, Face.R5, Face.U2, Face.R7, Face.R8, Face.U3,
                       Face.F1, Face.F2, Face.F3, Face.F4, Face.F5, Face.F6, Face.F7, Face.F8, Face.F9,
                       Face.D1, Face.D2, Face.D3, Face.D4, Face.D5, Face.D6, Face.R9, Face.R6, Face.R3,
                       Face.D7, Face.L2, Face.L3, Face.D8, Face.L5, Face.L6, Face.D9, Face.L8, Face.L9,
                       Face.B3, Face.B6, Face.B9, Face.B2, Face.B5, Face.B8, Face.B1, Face.B4, Face.B7};
        FaceletMove[TurnAxis.B.ordinal()] = new Facelet();
        System.arraycopy(temp6, 0, FaceletMove[TurnAxis.B.ordinal()].a ,0, Face.values().length);
        
        
        
        //Basic symmety transformations
        FaceletSym = new Facelet[Symmetry.values().length];
        Face[] temp7= {Face.R9, Face.R8, Face.R7, Face.R6, Face.R5, Face.R4, Face.R3, Face.R2, Face.R1, 
                       Face.F3, Face.F6, Face.F9, Face.F2, Face.F5, Face.F8, Face.F1, Face.F4, Face.F7,
                       Face.U3, Face.U6, Face.U9, Face.U2, Face.U5, Face.U8, Face.U1, Face.U4, Face.U7,
                       Face.L1, Face.L2, Face.L3, Face.L4, Face.L5, Face.L6, Face.L7, Face.L8, Face.L9,
                       Face.B7, Face.B4, Face.B1, Face.B8, Face.B5, Face.B2, Face.B9, Face.B6, Face.B3,
                       Face.D3, Face.D6, Face.D9, Face.D2, Face.D5, Face.D8, Face.D1, Face.D4, Face.D7};
        FaceletSym[Symmetry.S_URF3.ordinal()] = new Facelet();
        System.arraycopy(temp7, 0, FaceletSym[Symmetry.S_URF3.ordinal()].a ,0, Face.values().length);
        
        Face[] temp8= {Face.D9, Face.D8, Face.D7, Face.D6, Face.D5, Face.D4, Face.D3, Face.D2, Face.D1, 
                       Face.L9, Face.L8, Face.L7, Face.L6, Face.L5, Face.L4, Face.L3, Face.L2, Face.L1,
                       Face.F9, Face.F8, Face.F7, Face.F6, Face.F5, Face.F4, Face.F3, Face.F2, Face.F1,
                       Face.U9, Face.U8, Face.U7, Face.U6, Face.U5, Face.U4, Face.U3, Face.U2, Face.U1,
                       Face.R9, Face.R8, Face.R7, Face.R6, Face.R5, Face.R4, Face.R3, Face.R2, Face.R1,
                       Face.B9, Face.B8, Face.B7, Face.B6, Face.B5, Face.B4, Face.B3, Face.B2, Face.B1};
        FaceletSym[Symmetry.S_F2.ordinal()] = new Facelet();
        System.arraycopy(temp8, 0, FaceletSym[Symmetry.S_F2.ordinal()].a ,0, Face.values().length);
        
        Face[] temp9= {Face.U3, Face.U6, Face.U9, Face.U2, Face.U5, Face.U8, Face.U1, Face.U4, Face.U7, 
                       Face.F1, Face.F2, Face.F3, Face.F4, Face.F5, Face.F6, Face.F7, Face.F8, Face.F9,
                       Face.L1, Face.L2, Face.L3, Face.L4, Face.L5, Face.L6, Face.L7, Face.L8, Face.L9,
                       Face.D7, Face.D4, Face.D1, Face.D8, Face.D5, Face.D2, Face.D9, Face.D6, Face.D3,
                       Face.B1, Face.B2, Face.B3, Face.B4, Face.B5, Face.B6, Face.B7, Face.B8, Face.B9,
                       Face.R1, Face.R2, Face.R3, Face.R4, Face.R5, Face.R6, Face.R7, Face.R8, Face.R9};
        FaceletSym[Symmetry.S_U4.ordinal()] = new Facelet();
        System.arraycopy(temp9, 0, FaceletSym[Symmetry.S_U4.ordinal()].a ,0, Face.values().length);
        
        Face[] temp10={Face.U3, Face.U2, Face.U1, Face.U6, Face.U5, Face.U4, Face.U9, Face.U8, Face.U7, 
                       Face.L3, Face.L2, Face.L1, Face.L6, Face.L5, Face.L4, Face.L9, Face.L8, Face.L7,
                       Face.F3, Face.F2, Face.F1, Face.F6, Face.F5, Face.F4, Face.F9, Face.F8, Face.F7,
                       Face.D3, Face.D2, Face.D1, Face.D6, Face.D5, Face.D4, Face.D9, Face.D8, Face.D7,
                       Face.R3, Face.R2, Face.R1, Face.R6, Face.R5, Face.R4, Face.R9, Face.R8, Face.R7,
                       Face.B3, Face.B2, Face.B1, Face.B6, Face.B5, Face.B4, Face.B9, Face.B8, Face.B7};
        FaceletSym[Symmetry.S_LR2.ordinal()] = new Facelet();
        System.arraycopy(temp10, 0, FaceletSym[Symmetry.S_LR2.ordinal()].a ,0, Face.values().length);
        
        
        //Positional Changes of the corner cubies by face turns
        CornerCubieMove = new CornerCubie[Move.values().length];
        OrientedCorner[] temp11 = {new OrientedCorner(Corner.UBR, 0), new OrientedCorner(Corner.URF, 0),
                                   new OrientedCorner(Corner.UFL, 0), new OrientedCorner(Corner.ULB, 0),
                                   new OrientedCorner(Corner.DFR, 0), new OrientedCorner(Corner.DLF, 0),
                                   new OrientedCorner(Corner.DBL, 0), new OrientedCorner(Corner.DRB, 0)};
        CornerCubieMove[TurnAxis.U.ordinal()] = new CornerCubie();
        System.arraycopy(temp11, 0, CornerCubieMove[TurnAxis.U.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp12 = {new OrientedCorner(Corner.DFR, 2), new OrientedCorner(Corner.UFL, 0),
                                   new OrientedCorner(Corner.ULB, 0), new OrientedCorner(Corner.URF, 1),
                                   new OrientedCorner(Corner.DRB, 1), new OrientedCorner(Corner.DLF, 0),
                                   new OrientedCorner(Corner.DBL, 0), new OrientedCorner(Corner.UBR, 2)};
        CornerCubieMove[TurnAxis.R.ordinal()] = new CornerCubie();
        System.arraycopy(temp12, 0, CornerCubieMove[TurnAxis.R.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp13 = {new OrientedCorner(Corner.UFL, 1), new OrientedCorner(Corner.DLF, 2),
                                   new OrientedCorner(Corner.ULB, 0), new OrientedCorner(Corner.UBR, 0),
                                   new OrientedCorner(Corner.URF, 2), new OrientedCorner(Corner.DFR, 1),
                                   new OrientedCorner(Corner.DBL, 0), new OrientedCorner(Corner.DRB, 0)};
        CornerCubieMove[TurnAxis.F.ordinal()] = new CornerCubie();
        System.arraycopy(temp13, 0, CornerCubieMove[TurnAxis.F.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp14 = {new OrientedCorner(Corner.URF, 0), new OrientedCorner(Corner.UFL, 0),
                                   new OrientedCorner(Corner.ULB, 0), new OrientedCorner(Corner.UBR, 0),
                                   new OrientedCorner(Corner.DLF, 0), new OrientedCorner(Corner.DBL, 0),
                                   new OrientedCorner(Corner.DRB, 0), new OrientedCorner(Corner.DFR, 0)};
        CornerCubieMove[TurnAxis.D.ordinal()] = new CornerCubie();
        System.arraycopy(temp14, 0, CornerCubieMove[TurnAxis.D.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp15 = {new OrientedCorner(Corner.URF, 0), new OrientedCorner(Corner.ULB, 1),
                                   new OrientedCorner(Corner.DBL, 2), new OrientedCorner(Corner.UBR, 0),
                                   new OrientedCorner(Corner.DFR, 0), new OrientedCorner(Corner.UFL, 2),
                                   new OrientedCorner(Corner.DLF, 1), new OrientedCorner(Corner.DRB, 0)};
        CornerCubieMove[TurnAxis.L.ordinal()] = new CornerCubie();
        System.arraycopy(temp15, 0, CornerCubieMove[TurnAxis.L.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp16 = {new OrientedCorner(Corner.URF, 0), new OrientedCorner(Corner.UFL, 0),
                                   new OrientedCorner(Corner.UBR, 1), new OrientedCorner(Corner.DRB, 2),
                                   new OrientedCorner(Corner.DFR, 0), new OrientedCorner(Corner.DLF, 0),
                                   new OrientedCorner(Corner.ULB, 2), new OrientedCorner(Corner.DBL, 1)};
        CornerCubieMove[TurnAxis.B.ordinal()] = new CornerCubie();
        System.arraycopy(temp16, 0, CornerCubieMove[TurnAxis.B.ordinal()].a, 0, Corner.values().length);
        
        
        
        
        //corner cubie moves by basic symmetry transformations
        CornerCubieSym = new CornerCubie[Symmetry.values().length];
        OrientedCorner[] temp17 = {new OrientedCorner(Corner.URF, 1), new OrientedCorner(Corner.DFR, 2),
                                   new OrientedCorner(Corner.DLF, 1), new OrientedCorner(Corner.UFL, 2),
                                   new OrientedCorner(Corner.UBR, 2), new OrientedCorner(Corner.DRB, 1),
                                   new OrientedCorner(Corner.DBL, 2), new OrientedCorner(Corner.ULB, 1)};
        CornerCubieSym[Symmetry.S_URF3.ordinal()] = new CornerCubie();
        System.arraycopy(temp17, 0, CornerCubieSym[Symmetry.S_URF3.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp18 = {new OrientedCorner(Corner.DLF, 0), new OrientedCorner(Corner.DFR, 0),
                                   new OrientedCorner(Corner.DRB, 0), new OrientedCorner(Corner.DBL, 0),
                                   new OrientedCorner(Corner.UFL, 0), new OrientedCorner(Corner.URF, 0),
                                   new OrientedCorner(Corner.UBR, 0), new OrientedCorner(Corner.ULB, 0)};
        CornerCubieSym[Symmetry.S_F2.ordinal()] = new CornerCubie();
        System.arraycopy(temp18, 0, CornerCubieSym[Symmetry.S_F2.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp19 = {new OrientedCorner(Corner.UBR, 0), new OrientedCorner(Corner.URF, 0),
                                   new OrientedCorner(Corner.UFL, 0), new OrientedCorner(Corner.ULB, 0),
                                   new OrientedCorner(Corner.DRB, 0), new OrientedCorner(Corner.DFR, 0),
                                   new OrientedCorner(Corner.DLF, 0), new OrientedCorner(Corner.DBL, 0)};
        CornerCubieSym[Symmetry.S_U4.ordinal()] = new CornerCubie();
        System.arraycopy(temp19, 0, CornerCubieSym[Symmetry.S_U4.ordinal()].a, 0, Corner.values().length);
        
        OrientedCorner[] temp20 = {new OrientedCorner(Corner.UFL, 3), new OrientedCorner(Corner.URF, 3),
                                   new OrientedCorner(Corner.UBR, 3), new OrientedCorner(Corner.ULB, 3),
                                   new OrientedCorner(Corner.DLF, 3), new OrientedCorner(Corner.DFR, 3),
                                   new OrientedCorner(Corner.DRB, 3), new OrientedCorner(Corner.DBL, 3)};
        CornerCubieSym[Symmetry.S_LR2.ordinal()] = new CornerCubie();
        System.arraycopy(temp20, 0, CornerCubieSym[Symmetry.S_LR2.ordinal()].a, 0, Corner.values().length);
        
        
        //positional changes of edgecubies by face turns
        EdgeCubieMove = new EdgeCubie[TurnAxis.values().length];
        OrientedEdge[] temp21 = {new OrientedEdge(Edge.UB, 0, 1), new OrientedEdge(Edge.UR, 0, 1),
                                 new OrientedEdge(Edge.UF, 0, 1), new OrientedEdge(Edge.UL, 0, 1),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.FR, 0, 0), new OrientedEdge(Edge.FL, 0, 0),
                                 new OrientedEdge(Edge.BL, 0, 0), new OrientedEdge(Edge.BR, 0, 0)};
        EdgeCubieMove[TurnAxis.U.ordinal()] = new EdgeCubie();
        System.arraycopy(temp21, 0, EdgeCubieMove[TurnAxis.U.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp22 = {new OrientedEdge(Edge.FR, 0, 1), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.BR, 0, 1), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.DR, 0, 1), new OrientedEdge(Edge.FL, 0, 0),
                                 new OrientedEdge(Edge.BL, 0, 0), new OrientedEdge(Edge.UR, 0, 1)};
        EdgeCubieMove[TurnAxis.R.ordinal()] = new EdgeCubie();
        System.arraycopy(temp22, 0, EdgeCubieMove[TurnAxis.R.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp23 = {new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.FL, 1, 1),
                                 new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.FR, 1, 1),
                                 new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.UF, 1, 1), new OrientedEdge(Edge.DF, 1, 1),
                                 new OrientedEdge(Edge.BL, 0, 0), new OrientedEdge(Edge.BR, 0, 0)};
        EdgeCubieMove[TurnAxis.F.ordinal()] = new EdgeCubie();
        System.arraycopy(temp23, 0, EdgeCubieMove[TurnAxis.F.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp24 = {new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.DF, 0, 1), new OrientedEdge(Edge.DL, 0, 1),
                                 new OrientedEdge(Edge.DB, 0, 1), new OrientedEdge(Edge.DR, 0, 1),
                                 new OrientedEdge(Edge.FR, 0, 0), new OrientedEdge(Edge.FL, 0, 0),
                                 new OrientedEdge(Edge.BL, 0, 0), new OrientedEdge(Edge.BR, 0, 0)};
        EdgeCubieMove[TurnAxis.D.ordinal()] = new EdgeCubie();
        System.arraycopy(temp24, 0, EdgeCubieMove[TurnAxis.D.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp25 = {new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.BL, 0, 1), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.FL, 0, 1), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.FR, 0, 0), new OrientedEdge(Edge.UL, 0, 1),
                                 new OrientedEdge(Edge.DL, 0, 1), new OrientedEdge(Edge.BR, 0, 0)};
        EdgeCubieMove[TurnAxis.L.ordinal()] = new EdgeCubie();
        System.arraycopy(temp25, 0, EdgeCubieMove[TurnAxis.L.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp26 = {new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.BR, 1, 1),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.BL, 1, 1),
                                 new OrientedEdge(Edge.FR, 0, 0), new OrientedEdge(Edge.FL, 0, 0),
                                 new OrientedEdge(Edge.UB, 1, 1), new OrientedEdge(Edge.DB, 1, 1)};
        EdgeCubieMove[TurnAxis.B.ordinal()] = new EdgeCubie();
        System.arraycopy(temp26, 0, EdgeCubieMove[TurnAxis.B.ordinal()].a, 0, Edge.values().length);
        
        
        
        //positional changes of edge cubies by symmetry transformations
        EdgeCubieSym = new EdgeCubie[Symmetry.values().length];
        OrientedEdge[] temp27 = {new OrientedEdge(Edge.UF, 1, 0), new OrientedEdge(Edge.FR, 0, 0),
                                 new OrientedEdge(Edge.DF, 1, 0), new OrientedEdge(Edge.FL, 0, 0),
                                 new OrientedEdge(Edge.UB, 1, 0), new OrientedEdge(Edge.BR, 0, 0),
                                 new OrientedEdge(Edge.DB, 1, 0), new OrientedEdge(Edge.BL, 0, 0),
                                 new OrientedEdge(Edge.UR, 1, 0), new OrientedEdge(Edge.DR, 1, 0),
                                 new OrientedEdge(Edge.DL, 1, 0), new OrientedEdge(Edge.UL, 1, 0)};
        EdgeCubieSym[Symmetry.S_URF3.ordinal()] = new EdgeCubie();
        System.arraycopy(temp27, 0, EdgeCubieSym[Symmetry.S_URF3.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp28 = {new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.FL, 0, 0), new OrientedEdge(Edge.FR, 0, 0),
                                 new OrientedEdge(Edge.BR, 0, 0), new OrientedEdge(Edge.BL, 0, 0)};
        EdgeCubieSym[Symmetry.S_F2.ordinal()] = new EdgeCubie();
        System.arraycopy(temp28, 0, EdgeCubieSym[Symmetry.S_F2.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp29 = {new OrientedEdge(Edge.UB, 0, 1), new OrientedEdge(Edge.UR, 0, 1),
                                 new OrientedEdge(Edge.UF, 0, 1), new OrientedEdge(Edge.UL, 0, 1),
                                 new OrientedEdge(Edge.DB, 0, 1), new OrientedEdge(Edge.DR, 0, 1),
                                 new OrientedEdge(Edge.DF, 0, 1), new OrientedEdge(Edge.DL, 0, 1),
                                 new OrientedEdge(Edge.BR, 1, 1), new OrientedEdge(Edge.FR, 1, 1),
                                 new OrientedEdge(Edge.FL, 1, 1), new OrientedEdge(Edge.BL, 1, 1)};
        EdgeCubieSym[Symmetry.S_U4.ordinal()] = new EdgeCubie();
        System.arraycopy(temp29, 0, EdgeCubieSym[Symmetry.S_U4.ordinal()].a, 0, Edge.values().length);
        
        OrientedEdge[] temp30 = {new OrientedEdge(Edge.UL, 0, 0), new OrientedEdge(Edge.UF, 0, 0),
                                 new OrientedEdge(Edge.UR, 0, 0), new OrientedEdge(Edge.UB, 0, 0),
                                 new OrientedEdge(Edge.DL, 0, 0), new OrientedEdge(Edge.DF, 0, 0),
                                 new OrientedEdge(Edge.DR, 0, 0), new OrientedEdge(Edge.DB, 0, 0),
                                 new OrientedEdge(Edge.FL, 0, 0), new OrientedEdge(Edge.FR, 0, 0),
                                 new OrientedEdge(Edge.BR, 0, 0), new OrientedEdge(Edge.BL, 0, 0)};
        EdgeCubieSym[Symmetry.S_LR2.ordinal()] = new EdgeCubie();
        System.arraycopy(temp30, 0, EdgeCubieSym[Symmetry.S_LR2.ordinal()].a, 0, Edge.values().length);
        
        
        Symmetries = new CubieCube[16];
        CubieCube cc = new CubieCube(true);
        for(int i = 0; i < 16; i++){
            Symmetries[i] = new CubieCube();
            Symmetries[i].c = CornSym(cc.c, i, false);
            Symmetries[i].e = EdgeSym(cc.e, i, false);
        }
        
        SymmetriesInv = new CubieCube[16];
        for(int i = 0; i < 16; i++){
            SymmetriesInv[i] = new CubieCube();
            SymmetriesInv[i].c = CornSym(cc.c, i, true);
            SymmetriesInv[i].e = EdgeSym(cc.e, i, true);
        }
        
        SymMove = new int[16][Move.values().length];
        int[] temp31 = {Move.Ux1.ordinal(), Move.Ux2.ordinal(), Move.Ux3.ordinal(), Move.Rx1.ordinal(),
                        Move.Rx2.ordinal(), Move.Rx3.ordinal(), Move.Fx1.ordinal(), Move.Fx2.ordinal(),
                        Move.Fx3.ordinal(), Move.Dx1.ordinal(), Move.Dx2.ordinal(), Move.Dx3.ordinal(),
                        Move.Lx1.ordinal(), Move.Lx2.ordinal(), Move.Lx3.ordinal(), Move.Bx1.ordinal(),
                        Move.Bx2.ordinal(), Move.Bx3.ordinal()};
        System.arraycopy(temp31, 0, SymMove[0], 0, Move.values().length);

        int[] temp32 = {Move.Ux3.ordinal(), Move.Ux2.ordinal(), Move.Ux1.ordinal(), Move.Lx3.ordinal(), 
                        Move.Lx2.ordinal(), Move.Lx1.ordinal(), Move.Fx3.ordinal(), Move.Fx2.ordinal(), 
                        Move.Fx1.ordinal(), Move.Dx3.ordinal(), Move.Dx2.ordinal(), Move.Dx1.ordinal(), 
                        Move.Rx3.ordinal(), Move.Rx2.ordinal(), Move.Rx1.ordinal(), Move.Bx3.ordinal(), 
                        Move.Bx2.ordinal(), Move.Bx1.ordinal()};
        System.arraycopy(temp32, 0, SymMove[1], 0, Move.values().length);

        int[] temp33 = {Move.Ux1.ordinal(), Move.Ux2.ordinal(), Move.Ux3.ordinal(), Move.Bx1.ordinal(),
                        Move.Bx2.ordinal(), Move.Bx3.ordinal(), Move.Rx1.ordinal(), Move.Rx2.ordinal(),
                        Move.Rx3.ordinal(), Move.Dx1.ordinal(), Move.Dx2.ordinal(), Move.Dx3.ordinal(),
                        Move.Fx1.ordinal(), Move.Fx2.ordinal(), Move.Fx3.ordinal(), Move.Lx1.ordinal(),
                        Move.Lx2.ordinal(), Move.Lx3.ordinal()};
        System.arraycopy(temp33, 0, SymMove[2], 0, Move.values().length);

        int[] temp34 = {Move.Dx1.ordinal(), Move.Dx2.ordinal(), Move.Dx3.ordinal(), Move.Lx1.ordinal(),
                        Move.Lx2.ordinal(), Move.Lx3.ordinal(), Move.Fx1.ordinal(), Move.Fx2.ordinal(),
                        Move.Fx3.ordinal(), Move.Ux1.ordinal(), Move.Ux2.ordinal(), Move.Ux3.ordinal(),
                        Move.Rx1.ordinal(), Move.Rx2.ordinal(), Move.Rx3.ordinal(), Move.Bx1.ordinal(),
                        Move.Bx2.ordinal(), Move.Bx3.ordinal()};
        System.arraycopy(temp34, 0, SymMove[8], 0, Move.values().length);

        SymMove[3] = SymCopy(2, 1);
        SymMove[4] = SymCopy(2, 2);
        SymMove[5] = SymCopy(4, 1);
        SymMove[6] = SymCopy(4, 2);
        SymMove[7] = SymCopy(6, 1);
        SymMove[9] = SymCopy(8, 1);
        SymMove[10] = SymCopy(8, 2);
        SymMove[11] = SymCopy(10, 1);
        SymMove[12] = SymCopy(10, 2);
        SymMove[13] = SymCopy(12, 1);
        SymMove[14] = SymCopy(12, 2);
        SymMove[15] = SymCopy(14, 1);

        SymMoveInv = new int[16][Move.values().length];
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < Move.values().length; j++){
                SymMoveInv[i][SymMove[i][j]] = j;
            }
        }

        SymFace = new int[16][6];
        int[] temp35 = {0, 1, 2, 3, 4, 5};
        System.arraycopy(temp35, 0, SymFace[0], 0, 6);

        int[] temp36 = {0, 3, 2, 1, 4, 5};
        System.arraycopy(temp36, 0, SymFace[1], 0, 6);

        int[] temp37 = {0, 2, 3, 4, 1, 5};
        System.arraycopy(temp37, 0, SymFace[2], 0, 6);

        int[] temp38 = {5, 3, 2, 1, 4, 0};
        System.arraycopy(temp38, 0, SymFace[8], 0, 6);

        SymFace[3] = MakeFaceSym(2, 1);
        SymFace[4] = MakeFaceSym(2, 2);
        SymFace[5] = MakeFaceSym(4, 1);
        SymFace[6] = MakeFaceSym(4, 2);
        SymFace[7] = MakeFaceSym(6, 1);
        SymFace[9] = MakeFaceSym(8, 1);
        SymFace[10] = MakeFaceSym(8, 2);
        SymFace[11] = MakeFaceSym(10, 1);
        SymFace[12] = MakeFaceSym(10, 2);
        SymFace[13] = MakeFaceSym(12, 1);
        SymFace[14] = MakeFaceSym(12, 2);
        SymFace[15] = MakeFaceSym(14, 1);

        SymComb = new int[16][16];
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                int[] tempInt = MakeFaceSym(i, j);
                for(int k = 0; k < 16; k++){
                    boolean ok = true;
                    for(int l = 0; l < 6; l++){
                        if(tempInt[l] != SymFace[k][l]){
                            ok = false;
                            break;
                        }
                    }
                    if(ok){
                        SymComb[i][j] = k;
                    }
                }
            }
        }
    }

    /**
     * Fills the SymFace array by combining two Syms
     * @param x - first sym coord
     * @param y - second sym coord
     * @return the resulting array
     */
    private int[] MakeFaceSym(int x, int y){
        int[] temp = new int[6];
        for(int i = 0; i < 6; i++){
            temp[i] = SymFace[x][SymFace[y][i]];
        }
        return temp;
    }

    /**
     * SymCopy creates the SymMove array by combining too
     * symmetries
     * @param x - the first Sym Index
     * @param y - the first Sym Index
     * @return the created symmetry
     */
    private int[] SymCopy(int x, int y){
        int[] temp = new int[Move.values().length];
        for(Move move : Move.values()){
            temp[move.ordinal()] = SymMove[x][SymMove[y][move.ordinal()]];
        }
        return temp;
    }

    /**
     * This converts the face color to an actual Java color
     *
     *
     * @param c - the Color as input
     * @return - the Java Color
     */
    public Color ColorsToColor(Colors c){
        switch(c){
            case Red:
                return Color.RED;
            case Orange:
                return Color.ORANGE;
            case Yellow:
                return Color.YELLOW;
            case Green:
                return Color.GREEN;
            case Blue:
                return Color.BLUE;
            case White:
                return Color.WHITE;
            default:
                return Color.BLACK;
        }
    }

    /**
     * This converts an index for a color to a Java Color
     *
     * @param c - the index of the color
     * @return the corresponding color
     */
    public Color ColorIndexToColor(ColorIndex c){
        return ColorsToColor(Colors.values()[c.ordinal()]);
    }

    /**
     * This converts a Java color to a color index
     *
     * @param c - the Color input
     * @return the ColorIndex corresponding to that color
     */
    public ColorIndex ColorToColorIndex(Color c){
        if(c == Color.RED){
            return ColorIndex.values()[Colors.Red.ordinal()];
        }else if(c == Color.ORANGE){
            return ColorIndex.values()[Colors.Orange.ordinal()];
        }else if(c == Color.YELLOW){
            return ColorIndex.values()[Colors.Yellow.ordinal()];
        }else if(c == Color.GREEN){
            return ColorIndex.values()[Colors.Green.ordinal()];
        }else if(c == Color.BLUE){
            return ColorIndex.values()[Colors.Blue.ordinal()];
        }else if(c == Color.WHITE){
            return ColorIndex.values()[Colors.White.ordinal()];
        }else{
            return ColorIndex.values()[Colors.Red.ordinal()];
        }
    }

    /**
     * This function takes three Java Colors and converts it to an
     * Oriented Corner. Used in the creating and saving of a cube
     *
     * @param a - first color
     * @param b - second color
     * @param c - third color
     * @return the OrientedColor
     */
    public OrientedCorner IDCorner(Color a, Color b, Color c) throws Exception{
        ColorIndex x = ColorToColorIndex(a);
        ColorIndex y = ColorToColorIndex(b);
        ColorIndex z = ColorToColorIndex(c);
        Corner temp = null;
        boolean found = false;
        for (Corner co : Corner.values()){
            if(CCI.a[co.ordinal()][0] == x || CCI.a[co.ordinal()][0] == y || CCI.a[co.ordinal()][0] == z){
                if(CCI.a[co.ordinal()][1] == x || CCI.a[co.ordinal()][1] == y || CCI.a[co.ordinal()][1] == z){
                    if(CCI.a[co.ordinal()][2] == x || CCI.a[co.ordinal()][2] == y || CCI.a[co.ordinal()][2] == z){
                        temp = co;
                        found = true;
                        break;
                    }
                }
            }
        }
        if(!found){
            throw new Exception("Corner Not Found!");
        }
        int i;
        for(i = 0; i < 3; i++){
            if(CCI.a[temp.ordinal()][i] == x){
                break;
            }
        }
        if(CCI.a[temp.ordinal()][(i + 1) % 3] != y){
            i = i + 3;
        }
        if(i % 3 == 2){
           i -= 1;
        }else if (i % 3 == 1){
            //i += 2;
            i += 1;
        }
        return new OrientedCorner(temp, i);
    }

    /**
     * This takes two Colors as input and returns the OrientedEdge
     * that corresponds to them
     *
     * @param a - the first color
     * @param b - the second color
     * @return the corresponding OrientedEdge
     */
    public OrientedEdge IDEdge(Color a, Color b) throws Exception{
        ColorIndex x = ColorToColorIndex(a);
        ColorIndex y = ColorToColorIndex(b);
        Edge temp = null;
        boolean found = false;
        for (Edge ed : Edge.values()){
            if(ECI.a[ed.ordinal()][0] == x || ECI.a[ed.ordinal()][0] == y){
                if(ECI.a[ed.ordinal()][1] == x || ECI.a[ed.ordinal()][1] == y){
                    temp = ed;
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            throw new Exception("Edge Not Found!");
        }
        int i, j;
        for(i = 0; i < 2; i++){
            if(ECI.a[temp.ordinal()][i] == x){
                break;
            }
        }
        for(j = 0; j < 2; j++){
            if(ECIAlt.a[temp.ordinal()][i] == x){
                break;
            }
        }
        return new OrientedEdge(temp, i, j);
    }

    public boolean verifyCube(CubieCube cube){
        int sum = 0;
        for(Corner co: Corner.values()){
            boolean found = false;
            sum += cube.c.a[co.ordinal()].o;
            for(int i = 0; i < Corner.values().length; i++){
                if(cube.c.a[i].c == co){
                    found = true;
                }
            }
            if(!found){
                return false;
            }
        }
        if(sum % 3 != 0){
            return false;
        }
        sum = 0;
        for(Edge ed: Edge.values()){
            boolean found = false;
            sum += cube.e.a[ed.ordinal()].o;
            for(int i = 0; i < Edge.values().length; i++){
                if(cube.e.a[i].e == ed){
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        if(sum % 2 != 0){
            return false;
        }
        return true;
    }

    /**
     * CornMult multiplies one corer by another, simulating a more or symmetry
     *
     * @param a - First Cubie
     * @param b - Second Cubie
     * @return the resulting cubie
     */
    public CornerCubie CornMult(CornerCubie a, CornerCubie b){
        CornerCubie prod = new CornerCubie(true);
        int oriA, oriB, ori;
        ori = 0;
        for(Corner co: Corner.values()){
            prod.a[co.ordinal()].c = (a.a[(b.a[co.ordinal()]).c.ordinal()]).c;
            oriA = a.a[b.a[co.ordinal()].c.ordinal()].o;
            oriB = b.a[co.ordinal()].o;
            if(oriA < 3 && oriB < 3){
                ori = oriA + oriB;
                if(ori >=3) ori = ori-3;
            }else if(oriA < 3 && oriB >= 3){
                ori = oriA + oriB;
                if(ori >= 6) ori = ori - 3;
            }else if(oriA >= 3 && oriB < 3){
                ori = oriA - oriB;
                if(ori < 3) ori = ori + 3;
            }else if(oriA >= 3 && oriB >= 3){
                ori = oriA - oriB;
                if(ori < 0) ori = ori + 3;
            }
            prod.a[co.ordinal()].o = ori;
        }
        return prod;
    }

    /**
     * EdgeMult takes two EdgeCubies and multiplies them, simulating a symmetry or
     * move
     *
     * @param a - the first Cubie
     * @param b - the second Cubie
     * @return - the result
     */
    public EdgeCubie EdgeMult(EdgeCubie a, EdgeCubie b){
        EdgeCubie prod = new EdgeCubie(true);
        int ori = 0, oriA = 0;
        for(Edge ed: Edge.values()){
            prod.a[ed.ordinal()].e = a.a[b.a[ed.ordinal()].e.ordinal()].e;
            ori = b.a[ed.ordinal()].o + a.a[b.a[ed.ordinal()].e.ordinal()].o;
            oriA = b.a[ed.ordinal()].oA + a.a[b.a[ed.ordinal()].e.ordinal()].oA;
            if(ori == 2) ori = 0;
            prod.a[ed.ordinal()].o = ori;
            if(oriA == 2) oriA = 0;
            prod.a[ed.ordinal()].oA = oriA;
        }
        return prod;
    }

    /**
     * This function inverts the permutation of an EdgeCubie
     *
     * @param a - the Input Cubie;
     * @return - the resulting Cubie
     */
    public EdgeCubie EdgeInv(EdgeCubie a){
        EdgeCubie inv = new EdgeCubie(true);
        for(Edge ed : Edge.values()){
            inv.a[a.a[ed.ordinal()].e.ordinal()].e = ed;
        }
        for(Edge ed : Edge.values()){
            inv.a[ed.ordinal()].o = a.a[inv.a[ed.ordinal()].e.ordinal()].o;
        }
        for(Edge ed : Edge.values()){
            inv.a[ed.ordinal()].oA = a.a[inv.a[ed.ordinal()].e.ordinal()].oA;
        }
        return inv;
    }

    /**
     * This function inverts the permutation of an CornerCubie
     *
     * @param a - the Input Cubie;
     * @return - the resulting Cubie
     */
    public CornerCubie CornInv(CornerCubie a){
        CornerCubie inv = new CornerCubie(true);
        int ori = 0;
        for(Corner co : Corner.values()){
            inv.a[a.a[co.ordinal()].c.ordinal()].c = co;
        }
        for(Corner co : Corner.values()){
            ori = a.a[inv.a[co.ordinal()].c.ordinal()].o;
            if(ori >= 3){
                inv.a[co.ordinal()].o = ori;
            }else{
                inv.a[co.ordinal()].o = ori;
                if(inv.a[co.ordinal()].o < 0) inv.a[co.ordinal()].o = inv.a[co.ordinal()].o + 3;
            }
        }
        return inv;
    }

    /**
     * This function calculates the Corner Orientation Coordinate of a Cubie
     *
     * @param c - the input Cube
     * @return - the Corner Orientation coordinate
     */
    public int CornOriCoord(CornerCubie c){
        int s = 0;
        boolean invert = false;
        int count = 0;
        for(Corner co: Corner.values()){
            if(c.a[co.ordinal()].o >= 3){
                invert = true;
                count++;
            }
        }
        //if the cube is inverted, invert it back before calculating the coordinate
        if(invert){
            CornerCubie temp = CornMult(Symmetries[1].c, c);
            temp = CornMult(temp, SymmetriesInv[1].c);
            for(Corner co: Corner.values()){
                s = s + ((temp.a[co.ordinal()].o % 3) * (int)Math.pow(3,(6 - co.ordinal())));
            }
        }else{
            for(Corner co: Corner.values()){
                s = s + ((c.a[co.ordinal()].o % 3) * (int)Math.pow(3,(6 - co.ordinal())));
            }
        }
        return s;
    }

    /**
     * This function calculates the Edge Orientation Coordinate
     *
     * @param e - the Edge Cubie
     * @return - the Edge Orientation Coordinate
     */
    public int EdgeOriCoord(EdgeCubie e){
        int s = 0;
        for(Edge ed: Edge.values()){
            s = s + ((e.a[ed.ordinal()].o) * (int)Math.pow(2, (10 - ed.ordinal())));
        }
        return s;
    }

    /**
     * This function calculates the Corner Permutation Coordinate
     *
     * @param corn - the CornerCubie
     * @return - the Corner Permutation Coordinate
     */
    public int CornPermCoord(CornerCubie corn){
        int x = 0;
        for(int i = Corner.values().length - 1; i > 0; i--){
            int s = 0;
            for(int j = i - 1; j >= 0; j--){
                if(corn.a[j].c.ordinal() > corn.a[i].c.ordinal()) s++;
            }
            x = (x + s) * i;
        }
        return x;
    }

    /**
     * This function calculates the Edge Permutation Coordinate
     *
     * @param ed - the EdgeCubie to calculate
     * @return - the Edge Permutation Coordinate
     */
    public int EdgePermCoord(EdgeCubie ed){
        int x = 0;
        for(int i = Edge.values().length - 1; i > 0; i--){
            int s = 0;
            for(int j = i - 1; j >= 0; j--){
                if(ed.a[j].e.ordinal() > ed.a[i].e.ordinal()) s++;
            }
            x = (x + s) * i;
        }
        return x;
    }

    /**
     * This function does a single symmetry to a CornerCubie
     *
     * @param c - the Corner
     * @param index - the SymIndex
     * @param invert - true to invert
     * @return - the resulting CornerCubie
     */
    public CornerCubie CornSym(CornerCubie c, int index, boolean invert){
        int x1, x2, x3, x4;
        x1 = (int)(index / 16);
        index = index - (x1 * 16);
        x2 = (int)(index / 8);
        index = index - (x2 * 8);
        x3 = (int)(index / 2);
        x4 = index - (x3 * 2);
        if(invert){
            for(int i = x4; i < 2; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_LR2.ordinal()]);
            }
            for(int i = x3; i < 4; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_U4.ordinal()]);
            }
            for(int i = x2; i < 2; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_F2.ordinal()]);
            }
            for(int i = x1; i < 3; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_URF3.ordinal()]);
            }
        }else{
            for(int i = 0; i < x1; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_URF3.ordinal()]);
            }
            for(int i = 0; i < x2; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_F2.ordinal()]);
            }
            for(int i = 0; i < x3; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_U4.ordinal()]);
            }
            for(int i = 0; i < x4; i++){
                c = CornMult(c, CornerCubieSym[Symmetry.S_LR2.ordinal()]);
            }
        }
        return c;
    }

    /**
     * This function does a single symmetry to a EdgeCubie
     *
     * @param e - the Edge
     * @param index - the SymIndex
     * @param invert - true to invert
     * @return - the resulting EdgeCubie
     */
    public EdgeCubie EdgeSym(EdgeCubie e, int index, boolean invert){
        int x1, x2, x3, x4;
        x1 = (int)(index / 16);
        index = index - (x1 * 16);
        x2 = (int)(index / 8);
        index = index - (x2 * 8);
        x3 = (int)(index / 2);
        x4 = index - (x3 * 2);
        if(invert){
            for(int i = x4; i < 2; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_LR2.ordinal()]);
            }
            for(int i = x3; i < 4; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_U4.ordinal()]);
            }
            for(int i = x2; i < 2; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_F2.ordinal()]);
            }
            for(int i = x1; i < 3; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_URF3.ordinal()]);
            }
        }else{
            for(int i = 0; i < x1; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_URF3.ordinal()]);
            }
            for(int i = 0; i < x2; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_F2.ordinal()]);
            }
            for(int i = 0; i < x3; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_U4.ordinal()]);
            }
            for(int i = 0; i < x4; i++){
                e = EdgeMult(e, EdgeCubieSym[Symmetry.S_LR2.ordinal()]);
            }
        }
        return e;
    }

    /**
     * This function applies a move by it's turn axis to a CubieCube
     *
     * @param cc - the input CubieCube
     * @param turn - the TurnAxis
     * @return - the resulting CubieCube
     */
    public CubieCube DoMove(CubieCube cc, TurnAxis turn){
        CubieCube temp = new CubieCube();
        temp.c = CornMult(cc.c, CornerCubieMove[turn.ordinal()]);
        temp.e = EdgeMult(cc.e, EdgeCubieMove[turn.ordinal()]);
        return temp;
    }

    /**
     * This function does a single symmetry to a CubieCube
     *
     * @param cc - the Input cube
     * @param symIndex - the SymIndex
     * @param invert - whether to invert the symmetry
     * @return - the resulting cube
     */
    public CubieCube DoSym(CubieCube cc, int symIndex, boolean invert){
        CubieCube temp = new CubieCube();
        temp.c = CornSym(cc.c, symIndex, invert);
        temp.e = EdgeSym(cc.e, symIndex, invert);
        return temp;
    }

    /**
     * This calculates the UDSliceCoordinate
     *
     * @param cc - the cube as input
     * @return - the UDSliceCoordinate
     */
    public int UDSliceCoord(CubieCube cc){
        int s = 0;
        boolean[] occupied = {false, false, false, false, false, false, false, false, false, false, false, false};
        for(Edge ed : Edge.values()){
            if(cc.e.a[ed.ordinal()].e.ordinal() >= Edge.FR.ordinal()) occupied[ed.ordinal()] = true;
        }
        int k = 3, n = 11;
        while (k >=0){
            if(occupied[n]) k--;
            else s = s + choose(n, k);
            n--;
        }
        return s;
    }

    /**
     * This function simulates the math Choose (n choose k)
     * @param n - choose this number
     * @param k - from this number
     * @return - the result
     */
    private int choose(int n, int k){
        return (int)(factorial(n) / (factorial(k) * factorial(n - k)));
    }

    /**
     * This function simulates the math factorial (n!)
     *
     * @param n - the number to use
     * @return - the result
     */
    private int factorial(int n){
        if(n <= 1){
            return 1;
        }else{
            return n * factorial(n - 1);
        }
    }

    /**
     * This function calculates the Phase2 Edge Permutation Coordinate
     *
     * @param cc - the input Cubie
     * @return - the Phase 2 Edge Permutation Coordinate
     */
    public int Phase2EdgePermCoord(CubieCube cc){
        int x = 0;
        for(int i = Edge.DB.ordinal(); i > 0; i--){
            int s = 0;
            for(int j = i - 1; j >= 0; j--){
                if(cc.e.a[j].e.ordinal() > cc.e.a[i].e.ordinal()) s++;
            }
            x = (x + s) * i;
        }
        return x;
    }

    /**
     * This function calculates the UDSlice Sorted Coordinate for Phase 2
     *
     * @param cc - the CubieCube as input
     * @return - the UDSlice Sorted Coordinate
     */
    public int UDSliceSortedCoord(CubieCube cc){
        int i= 0;
        Edge[] arr = new Edge[4];
        for(Edge ed : Edge.values()){
            Edge e = cc.e.a[ed.ordinal()].e;
            if(e == Edge.FR || e == Edge.FL || e == Edge.BL || e == Edge.BR){
                arr[i] = e;
                i++;
            }
        }
        int x = 0;
        for(int j = 3; j > 0; j--){
            int s = 0;
            for(int k = j - 1; k >= 0; k--){
                if(arr[k].ordinal() > arr[j].ordinal()) s++;
            }
            x = (x + s) * j;
        }
        return (UDSliceCoord(cc) * 24) + x;
    }

    /**
     * This function does S(i) * CC * Sinv(i)
     *
     * @param cc - the input Cubie
     * @param symIdx - the symmetry index
     * @return - the resulting cubie
     */
    public CubieCube SthenSinv(CubieCube cc, int symIdx){
        CubieCube temp = new CubieCube();
        temp.c = CornMult(Symmetries[symIdx].c, cc.c);
        temp.e = EdgeMult(Symmetries[symIdx].e, cc.e);
        temp.c = CornMult(temp.c, SymmetriesInv[symIdx].c);
        temp.e = EdgeMult(temp.e, SymmetriesInv[symIdx].e);
        return temp;
    }

    /**
     * This function does Sinv(i) * CC * S(i)
     *
     * @param cc - the input Cubie
     * @param symIdx - the symmetry index
     * @return - the resulting cubie
     */
    public CubieCube SinvThenS(CubieCube cc, int symIdx){
        CubieCube temp = new CubieCube();
        temp.c = CornMult(SymmetriesInv[symIdx].c, cc.c);
        temp.e = EdgeMult(SymmetriesInv[symIdx].e, cc.e);
        temp.c = CornMult(temp.c, Symmetries[symIdx].c);
        temp.e = EdgeMult(temp.e, Symmetries[symIdx].e);
        return temp;
    }
    
    /**
     * This function applies all 16 symmetries to a cubie and calculates the
     * minimum UDSlice coordinate
     *
     * @param cc - the input cubie
     * @return - the minimum FlipUDSlice Coordinate
     */
    public int UDSliceRawReduce(CubieCube cc){
        int min = Integer.MAX_VALUE;
        for(int k = 0; k < 16; k++){
            CubieCube tempC = SthenSinv(cc, k);
            int temp = (UDSliceCoord(tempC) * 2048) + EdgeOriCoord(tempC.e);
            if(temp < min){
                min = temp;
            }
        }
        return min;
    }

    /**
     * This function applies all 16 symmetries to a cubie and calculates the
     * minimum Corner Permutation Coordinate
     *
     * @param cc - the input Cubie
     * @return - the minimum Corner Permutation Coordinate
     */
    public int CornPermRawReduce(CubieCube cc){
        int min = Integer.MAX_VALUE;
        for(int k = 0; k < 16; k++){
            CubieCube tempC = SthenSinv(cc, k);
            int temp = CornPermCoord(tempC.c);
            if(temp < min){
                min = temp;
            }
        }
        return min;
    }

    /**
     * This function returns the symindex that corresponds to the CubieCube with
     * the minimum FlipUDSlice Coordinate
     *
     * @param cc - the input CubieCube
     * @return - the Symindex that reduces the cube to minimum
     */
    public int SymReduce(CubieCube cc){
        int min = Integer.MAX_VALUE;
        int minIdx = 0;
        for(int k = 0; k < 16; k++){
            CubieCube tempC = SthenSinv(cc, k);
            int temp = (UDSliceCoord(tempC) * 2048) + EdgeOriCoord(tempC.e);
            if(temp < min){
                min = temp;
                minIdx = k;
            }
        }
        return minIdx;
    }

    /**
     * This function returns the symindex that corresponds to the CubieCube with
     * the minimum Corner Permuation Coordinate
     *
     * @param cc - the input cubie
     * @return - the minimum SymIndex
     */
    public int CornSymReduce(CubieCube cc){
        int min = Integer.MAX_VALUE;
        int minIdx = 0;
        for(int k = 0; k < 16; k++){
            //CubieCube tempC = new CubieCube();
            //tempC.c = CornMult(cc.c, SymmetriesInv[k].c);
            //tempC.e = EdgeMult(cc.e, SymmetriesInv[k].e);
            CubieCube tempC = SthenSinv(cc, k);
            int temp = CornPermCoord(tempC.c);
            if(temp < min){
                min = temp;
                minIdx = k;
            }
        }
        return minIdx;
    }
}
