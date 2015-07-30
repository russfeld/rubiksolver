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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import rubiksolverdef.*;
import java.io.*;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.UIManager;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import rubiksolver.RubikModel2.*;

/**
 * This class is the main GUI for the Rubik Solver Program
 */
public class RubikDisplay2 extends javax.swing.JFrame implements ActionListener{
    
    static private RubikModel2 model;
    static private TwistMoveTable table;
    static private FlipUDSlice flip;
    static private CornerPerm perm;
    static private PruningTables prune;
    static private ViewCube2 view;
    static private CubieCube cube;
    static private JButton[][] up, down, left, right, back, front;
    static private boolean canEdit;
    static private LinkedList<Integer> moveList;
    static private int nextMove;
    /** Creates new form RubikDisplay */
    public RubikDisplay2() {
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
         } catch (Exception e) {
                    e.printStackTrace();
         }
        initComponents();
        File input, output;
        ObjectInputStream in;
        ObjectOutputStream out;
        model = null;
        nextMove = 0;
        moveList = null;
        long time1 = System.currentTimeMillis();
        //Initializes each structure by testing if it exists
        try{
            input = new File("model.dat");
            in = new ObjectInputStream(new FileInputStream(input));
            model = (RubikModel2)in.readObject();
            in.close();
            System.out.println("Model read from file");
        }catch (Exception e){
            System.out.println("Error reading model from file");
            //e.printStackTrace();
        }
        //if unable to read from file, will recalculate
        if(model == null){
            model = new RubikModel2();
            try{
                output = new File("model.dat");
                out = new ObjectOutputStream(new FileOutputStream(output));
                out.writeObject(model);
                out.close();
            }catch(Exception e){
                System.out.println("Error writing model");
                e.printStackTrace();
            }
            System.out.println("Model created and saved");
        }
        long time2 = System.currentTimeMillis();
        flip = null;
        try{
            input = new File("flip.dat");
            in = new ObjectInputStream(new FileInputStream(input));
            flip = (FlipUDSlice)in.readObject();
            flip.setModel(model);
            in.close();
            System.out.println("FlipUDSlice read from file");
        }catch (Exception e){
            System.out.println("Error reading FlipUDSlice from file");
            //e.printStackTrace();
        }
        if(flip == null){
            flip = new FlipUDSlice(model);
            try{
                output = new File("flip.dat");
                out = new ObjectOutputStream(new FileOutputStream(output));
                out.writeObject(flip);
                out.close();
            }catch(Exception e){
                System.out.println("Error writing FlipUDSlice");
                e.printStackTrace();
            }
            System.out.println("FlipUDSlice created and saved");
        }
        long time6 = System.currentTimeMillis();
        perm = null;
        try{
            input = new File("perm.dat");
            in = new ObjectInputStream(new FileInputStream(input));
            perm = (CornerPerm)in.readObject();
            perm.setModel(model);
            in.close();
            System.out.println("CornerPerm read from file");
        }catch (Exception e){
            System.out.println("Error reading CornerPerm from file");
            //e.printStackTrace();
        }
        if(perm == null){
            perm = new CornerPerm(model);
            try{
                output = new File("perm.dat");
                out = new ObjectOutputStream(new FileOutputStream(output));
                out.writeObject(perm);
                out.close();
            }catch(Exception e){
                System.out.println("Error writing CornerPerm");
                e.printStackTrace();
            }
            System.out.println("CornerPerm created and saved");
        }
        long time3 = System.currentTimeMillis();
        table = null;
        try{
            input = new File("twist.dat");
            in = new ObjectInputStream(new FileInputStream(input));
            table = (TwistMoveTable)in.readObject();
            in.close();
            System.out.println("TwistMoveTable read from file");
        }catch (Exception e){
            System.out.println("Error reading TwistMoveTable from file");
            //e.printStackTrace();
        }
        if(table == null){
            table = new TwistMoveTable(model, flip, perm);
            try{
                output = new File("twist.dat");
                out = new ObjectOutputStream(new FileOutputStream(output));
                out.writeObject(table);
                out.close();
            }catch(Exception e){
                System.out.println("Error writing TwistMoveTable");
                e.printStackTrace();
            }
            System.out.println("TwistMoveTable created and saved");
        }
        long time4 = System.currentTimeMillis();
        prune = null;
        try{
            input = new File("prune.dat");
            in = new ObjectInputStream(new FileInputStream(input));
            prune = (PruningTables)in.readObject();
            in.close();
            System.out.println("PruningTables read from file");
        }catch (Exception e){
            System.out.println("Error reading PruningTables from file");
            //e.printStackTrace();
        }
        if(prune == null){
            prune = new PruningTables(model, flip, table, perm);
            try{
                output = new File("prune.dat");
                out = new ObjectOutputStream(new FileOutputStream(output));
                out.writeObject(prune);
                out.close();
            }catch(Exception e){
                System.out.println("Error writing PruningTables");
                e.printStackTrace();
            }
            System.out.println("PruningTables created and saved");
        }
        long time5 = System.currentTimeMillis();
        System.out.println("Model Initialization took " + (time2 - time1) + "ms");
        System.out.println("FlipUDSlice Initialization took " + (time6 - time2) + "ms");
        System.out.println("CornerPerm Initialization took " + (time3 - time6) + "ms");
        System.out.println("Table Initialization took " + (time4 - time3) + "ms");
        System.out.println("PruningTable Initialization took " + (time5 - time4) + "ms");
        this.setSize(800, 600);
        up = new JButton[3][3];
        down = new JButton[3][3];
        left = new JButton[3][3];
        right = new JButton[3][3];
        back = new JButton[3][3];
        front = new JButton[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                up[i][j] = new JButton();
                up[i][j].addActionListener(this);
                up[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(up[i][j], new AbsoluteConstraints(461 + 110 + 35 * i, 110 + 5 + 35 * j));
                down[i][j] = new JButton();
                down[i][j].addActionListener(this);
                down[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(down[i][j], new AbsoluteConstraints(461 + 110 + 35 * i, 110 + 215 + 35 * j));
                left[i][j] = new JButton();
                left[i][j].addActionListener(this);
                left[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(left[i][j], new AbsoluteConstraints(461 + 5 + 35 * i, 110 + 110 + 35 * j));
                right[i][j] = new JButton();
                right[i][j].addActionListener(this);
                right[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(right[i][j], new AbsoluteConstraints(461 + 215 + 35 * i, 110 + 110 + 35 * j));
                back[i][j] = new JButton();
                back[i][j].addActionListener(this);
                back[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(back[i][j], new AbsoluteConstraints(461 + 110 + 35 * i, 110 + 320 + 35 * j));
                front[i][j] = new JButton();
                front[i][j].addActionListener(this);
                front[i][j].setPreferredSize(new Dimension(30, 30));
                this.getContentPane().add(front[i][j], new AbsoluteConstraints(461 + 110 + 35 * i, 110 + 110 + 35 * j));
            }
        }
        cube = new CubieCube(true);
        updateC();
    }

    /**
     * Updates the text on the top of the GUI
     * @param input - text to add
     */
    public void updateText(String input){
        jTextArea1.setText(jTextArea1.getText() + "\n" + input);
    }

    public void updateView(){
        CubieCube cc = cube;
        int corner = cc.c.a[Corner.URF.ordinal()].c.ordinal();
        int ori = cc.c.a[Corner.URF.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            up[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            right[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            front[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            up[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            right[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            front[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.UFL.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.UFL.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            up[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            front[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            left[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            up[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            front[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            left[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.ULB.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.ULB.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            up[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            left[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            back[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            up[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            left[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            back[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.UBR.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.UBR.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            up[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            back[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            right[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            up[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            back[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            right[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.DFR.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.DFR.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            down[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            front[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            right[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            down[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            front[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            right[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.DLF.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.DLF.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            down[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            left[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            front[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            down[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            left[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            front[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.DBL.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.DBL.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            down[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            back[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            left[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            down[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            back[0][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            left[0][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        corner = cc.c.a[Corner.DRB.ordinal()].c.ordinal();
        ori = cc.c.a[Corner.DRB.ordinal()].o;
        if(ori % 3 == 2){
            ori -= 1;
        }else if(ori % 3 == 1){
            ori += 1;
        }
        if(ori >= 3){
            down[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori % 3]));
            right[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
            back[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
        }else{
            down[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][ori]));
            right[2][2].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 1) % 3]));
            back[2][0].setBackground(model.ColorIndexToColor(model.CCI.a[corner][(ori + 2) % 3]));
        }

        int edge = cc.e.a[Edge.UR.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.UR.ordinal()].o;
        up[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        right[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.UF.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.UF.ordinal()].o;
        up[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        front[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.UL.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.UL.ordinal()].o;
        up[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        left[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.UB.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.UB.ordinal()].o;
        up[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        back[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.DR.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.DR.ordinal()].o;
        down[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        right[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.DF.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.DF.ordinal()].o;
        down[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        front[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.DL.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.DL.ordinal()].o;
        down[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        left[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.DB.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.DB.ordinal()].o;
        down[1][2].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        back[1][0].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.FR.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.FR.ordinal()].o;
        front[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        right[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.FL.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.FL.ordinal()].o;
        front[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        left[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.BL.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.BL.ordinal()].o;
        back[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        left[0][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        edge = cc.e.a[Edge.BR.ordinal()].e.ordinal();
        ori = cc.e.a[Edge.BR.ordinal()].o;
        back[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][ori]));
        right[2][1].setBackground(model.ColorIndexToColor(model.ECI.a[edge][(ori + 1) % 2]));

        up[1][1].setBackground(Color.ORANGE);
        down[1][1].setBackground(Color.RED);
        left[1][1].setBackground(Color.GREEN);
        right[1][1].setBackground(Color.BLUE);
        back[1][1].setBackground(Color.YELLOW);
        front[1][1].setBackground(Color.WHITE);

        up[1][1].setEnabled(false);
        down[1][1].setEnabled(false);
        left[1][1].setEnabled(false);
        right[1][1].setEnabled(false);
        back[1][1].setEnabled(false);
        front[1][1].setEnabled(false);
    }

    /**
     * Updates the coordinates displayed on the GUI
     */
    public void updateC(){
        if(!canEdit){
            jTextField6.setText(""+model.CornOriCoord(cube.c));
            jTextField5.setText(""+model.EdgeOriCoord(cube.e));
            jTextField4.setText(""+model.UDSliceCoord(cube));
            int flipUD = flip.FlipUDSliceCoord(cube);
            jTextField3.setText(""+(int)(flipUD/ 16) + " " + (int)(flipUD % 16));
            jTextField2.setText(""+prune.Phase1PruningTable[(int)(flipUD / 16)][table.CornOriSym[model.CornOriCoord(cube.c)][flipUD % 16]]);
            jTextField11.setText(""+model.CornPermCoord(cube.c));
            jTextField10.setText(""+model.EdgePermCoord(cube.e));
            jTextField9.setText(""+model.UDSliceSortedCoord(cube));
            if(model.CornOriCoord(cube.c) == 0 && model.EdgeOriCoord(cube.e) == 0 && (int)(flipUD / 16) == 0){ //phase2
                jTextField12.setText(""+model.Phase2EdgePermCoord(cube));
                int CornPerm = perm.CornPermCoord(cube);
                jTextField8.setText("" + (int)(perm.CornPermCoord(cube) / 16) + " " + CornPerm % 16);
                jTextField7.setText("" + prune.Phase2PruningTable[(int)(CornPerm / 16)][table.P2EdgePermSym[model.Phase2EdgePermCoord(cube)][CornPerm % 16]]);
            }
            updateView();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_input = new javax.swing.JButton();
        btn_New = new javax.swing.JButton();
        btn_Solve = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btn_save = new javax.swing.JButton();
        btn_Solve1 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        btn_Solve2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_input.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_input.setText("Edit Cube Layout");
        btn_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_inputActionPerformed(evt);
            }
        });
        getContentPane().add(btn_input, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 160, 30));

        btn_New.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_New.setText("Create Solved Cube");
        btn_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewActionPerformed(evt);
            }
        });
        getContentPane().add(btn_New, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 160, 30));

        btn_Solve.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_Solve.setText("Solve");
        btn_Solve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SolveActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Solve, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 70, 60));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton2.setText("U");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 50, 40));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton3.setText("D");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, 50, 40));

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton4.setText("L");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 190, 50, 40));

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton5.setText("R");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 50, 40));

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton6.setText("F");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 50, 40));

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton7.setText("B");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, 50, 40));

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton8.setText("Sym Inverted");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 360, 110, -1));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, 50, 290));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Moves");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 170, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Russ's Rubik Solver 1.0");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Coordinates");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 270, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Solution");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 770, 20));

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton9.setText("S Then Sinv");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 110, -1));

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton10.setText("Sinv Then S");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, 110, -1));

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton11.setText("Symmetry");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 330, 110, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Actions");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 160, 30));
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Prune Depth");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 80, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("CornOri");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 90, 20));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("CornPerm");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 80, 20));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("EdgeOri");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 90, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Prune Depth");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 90, 20));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("UDSlice");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 90, 20));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Solution");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 390, 120, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("EdgePerm");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 70, 20));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("UDSliceSorted");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 100, 20));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("FlipUDSlice");
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 80, 20));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("CornPermSym");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 90, 20));

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 350, 140, -1));

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 140, -1));

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, 140, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 140, -1));

        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, 140, -1));

        jTextField7.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 480, 140, -1));

        jTextField8.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, 140, -1));

        jTextField9.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 440, 140, -1));

        jTextField10.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, 140, -1));

        jTextField11.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 380, 140, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("P2EdgePerm");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 110, 20));

        jTextField12.setFont(new java.awt.Font("Tahoma", 0, 14));
        getContentPane().add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 420, 140, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 770, 50));

        btn_save.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_save.setText("Save");
        btn_save.setEnabled(false);
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        getContentPane().add(btn_save, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 160, 30));

        btn_Solve1.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_Solve1.setText("Step Forward");
        btn_Solve1.setEnabled(false);
        btn_Solve1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Solve1ActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Solve1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 420, 120, 40));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Symmetries");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 240, 110, 30));

        btn_Solve2.setFont(new java.awt.Font("Tahoma", 0, 14));
        btn_Solve2.setText("Step Back");
        btn_Solve2.setEnabled(false);
        btn_Solve2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Solve2ActionPerformed(evt);
            }
        });
        getContentPane().add(btn_Solve2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 470, 120, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void btn_SolveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SolveActionPerformed

        if(JOptionPane.showConfirmDialog(this, "Are you sure?") == JOptionPane.YES_OPTION){
            jTextArea1.setText("");
            moveList = RubikSolver2.solve(cube, table, flip, prune, model, perm, this);
            if(moveList != null){
                btn_Solve1.setEnabled(true);
                btn_Solve2.setEnabled(true);
                nextMove = 0;
            }else{
                btn_Solve1.setEnabled(false);
                btn_Solve2.setEnabled(false);
                nextMove = 0;
            }
        }
    }//GEN-LAST:event_btn_SolveActionPerformed

    private void btn_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewActionPerformed
        cube = new CubieCube(true);
        btn_Solve1.setEnabled(false);
        btn_Solve2.setEnabled(false);
        updateC();
    }//GEN-LAST:event_btn_NewActionPerformed

    private void btn_inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inputActionPerformed
        if(cube == null){
            cube = new CubieCube(true);
        }
        canEdit = true;
        btn_save.setEnabled(true);
        btn_Solve1.setEnabled(false);
        btn_Solve2.setEnabled(false);
        btn_input.setEnabled(false);
        btn_Solve.setEnabled(false);
    }//GEN-LAST:event_btn_inputActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cube = model.DoMove(cube, TurnAxis.U);
        updateC();
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cube = model.DoMove(cube, TurnAxis.D);
        updateC();
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        cube = model.DoMove(cube, TurnAxis.L);
        updateC();
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cube = model.DoMove(cube, TurnAxis.R);
        updateC();
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        cube = model.DoMove(cube, TurnAxis.F);
        updateC();
}//GEN-LAST:event_jButton6ActionPerformed

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        cube = model.DoMove(cube, TurnAxis.B);
        updateC();
}//GEN-LAST:event_jButton7ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    if(jList1.getSelectedIndex() < 0) return;
    cube = model.DoSym(cube, jList1.getSelectedIndices()[0], true);
    updateC();
}//GEN-LAST:event_jButton8ActionPerformed

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
    if(jList1.getSelectedIndex() < 0) return;
    cube = model.SthenSinv(cube, jList1.getSelectedIndices()[0]);
    updateC();
}//GEN-LAST:event_jButton9ActionPerformed

private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
    if(jList1.getSelectedIndex() < 0) return;
    cube = model.SinvThenS(cube, jList1.getSelectedIndices()[0]);
    updateC();
}//GEN-LAST:event_jButton10ActionPerformed

private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
    if(jList1.getSelectedIndex() < 0) return;
    cube = model.DoSym(cube, jList1.getSelectedIndices()[0], false);
    updateC();
}//GEN-LAST:event_jButton11ActionPerformed

private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
    CubieCube cc = new CubieCube();
    try{
        cc.c.a[Corner.URF.ordinal()] = model.IDCorner(up[2][2].getBackground(), right[0][0].getBackground(), front[2][0].getBackground());
        cc.c.a[Corner.UFL.ordinal()] = model.IDCorner(up[0][2].getBackground(), front[0][0].getBackground(), left[2][0].getBackground());
        cc.c.a[Corner.ULB.ordinal()] = model.IDCorner(up[0][0].getBackground(), left[0][0].getBackground(), back[0][2].getBackground());
        cc.c.a[Corner.UBR.ordinal()] = model.IDCorner(up[2][0].getBackground(), back[2][2].getBackground(), right[2][0].getBackground());
        cc.c.a[Corner.DFR.ordinal()] = model.IDCorner(down[2][0].getBackground(), front[2][2].getBackground(), right[0][2].getBackground());
        cc.c.a[Corner.DLF.ordinal()] = model.IDCorner(down[0][0].getBackground(), left[2][2].getBackground(), front[0][2].getBackground());
        cc.c.a[Corner.DBL.ordinal()] = model.IDCorner(down[0][2].getBackground(), back[0][0].getBackground(), left[0][2].getBackground());
        cc.c.a[Corner.DRB.ordinal()] = model.IDCorner(down[2][2].getBackground(), right[2][2].getBackground(), back[2][0].getBackground());

        cc.e.a[Edge.UR.ordinal()] = model.IDEdge(up[2][1].getBackground(), right[1][0].getBackground());
        cc.e.a[Edge.UF.ordinal()] = model.IDEdge(up[1][2].getBackground(), front[1][0].getBackground());
        cc.e.a[Edge.UL.ordinal()] = model.IDEdge(up[0][1].getBackground(), left[1][0].getBackground());
        cc.e.a[Edge.UB.ordinal()] = model.IDEdge(up[1][0].getBackground(), back[1][2].getBackground());
        cc.e.a[Edge.DR.ordinal()] = model.IDEdge(down[2][1].getBackground(), right[1][2].getBackground());
        cc.e.a[Edge.DF.ordinal()] = model.IDEdge(down[1][0].getBackground(), front[1][2].getBackground());
        cc.e.a[Edge.DL.ordinal()] = model.IDEdge(down[0][1].getBackground(), left[1][2].getBackground());
        cc.e.a[Edge.DB.ordinal()] = model.IDEdge(down[1][2].getBackground(), back[1][0].getBackground());
        cc.e.a[Edge.FR.ordinal()] = model.IDEdge(front[2][1].getBackground(), right[0][1].getBackground());
        cc.e.a[Edge.FL.ordinal()] = model.IDEdge(front[0][1].getBackground(), left[2][1].getBackground());
        cc.e.a[Edge.BL.ordinal()] = model.IDEdge(back[0][1].getBackground(), left[0][1].getBackground());
        cc.e.a[Edge.BR.ordinal()] = model.IDEdge(back[2][1].getBackground(), right[2][1].getBackground());
    }catch(Exception e){
        JOptionPane.showMessageDialog(this, "Cube is not valid!");
        return;
    }
    if(model.verifyCube(cc)){
        cube = cc;
        canEdit = false;
        updateC();
        btn_Solve.setEnabled(true);
        btn_input.setEnabled(true);
        btn_save.setEnabled(false);
    }else{
        JOptionPane.showMessageDialog(this, "Cube is not valid!");
    }
}//GEN-LAST:event_btn_saveActionPerformed

private void btn_Solve1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Solve1ActionPerformed
    if(nextMove >= 0 && nextMove < moveList.size()){
        int move = moveList.get(nextMove);
        for(int j = 0; j < (move % 3) + 1; j++){
            cube = model.DoMove(cube, TurnAxis.values()[(int)(move / 3)]);
        }
        nextMove++;
        updateC();
    }
}//GEN-LAST:event_btn_Solve1ActionPerformed

private void btn_Solve2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Solve2ActionPerformed
    if(nextMove > 0 && nextMove <= moveList.size()){
        nextMove--;
        int move = moveList.get(nextMove);
        for(int j = 4; j > (move % 3) + 1; j--){
            cube = model.DoMove(cube, TurnAxis.values()[(int)(move / 3)]);
        }
        updateC();
    }
}//GEN-LAST:event_btn_Solve2ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RubikDisplay2().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_New;
    private javax.swing.JButton btn_Solve;
    private javax.swing.JButton btn_Solve1;
    private javax.swing.JButton btn_Solve2;
    private javax.swing.JButton btn_input;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        if(canEdit){
            JButton pressed = (JButton)e.getSource();
            if(pressed.getBackground() == Color.WHITE){
                pressed.setBackground(Color.RED);
            }else if(pressed.getBackground() == Color.RED){
                pressed.setBackground(Color.ORANGE);
            }else if(pressed.getBackground() == Color.ORANGE){
                pressed.setBackground(Color.YELLOW);
            }else if(pressed.getBackground() == Color.YELLOW){
                pressed.setBackground(Color.GREEN);
            }else if(pressed.getBackground() == Color.GREEN){
                pressed.setBackground(Color.BLUE);
            }else if(pressed.getBackground() == Color.BLUE){
                pressed.setBackground(Color.WHITE);
            }
        }
    }
    
}
