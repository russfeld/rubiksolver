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
 * This class is used to store the coordinates for Phase1 of the
 * solving algorithm
 */
public class Duple {
    public int CornOriCoord;
    public int EdgeOriCoord;
    public int UDSliceCoord;
    public int FlipUDSliceCoord;
    public String backtrace;

    /**
     * This is the constructor for the Duple class
     *
     * @param int x - Corner Orientation Coordinate
     * @param int y - Edge Orientation Coordinate
     * @param int z - UD Slice Coordinate
     * @param int w - Flip UDSlice Sym Coordinate
     * @param String bt - backtrace of moves done to cube
     */
    public Duple(int x, int y, int z, int w, String bt){
        CornOriCoord = x;
        EdgeOriCoord = z;
        UDSliceCoord = w;
        FlipUDSliceCoord = y;
        backtrace = bt;
    }

    @Override
    /**
     * Takes an obejct to compare to this Duple. Returns true if the coordinates
     * of the duple are the same
     *
     * @param Object inputO - the object to compare to this
     * @return boolean - true if equal, else false
     */
    public boolean equals(Object inputO){
        try{
            Duple input = (Duple)inputO;
            if(CornOriCoord == input.CornOriCoord && FlipUDSliceCoord == input.FlipUDSliceCoord && input.EdgeOriCoord == EdgeOriCoord && input.UDSliceCoord == UDSliceCoord){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }

    @Override
    /**
     * Generated hashCode function from NetBeans
     *
     * @return int - the hashCode of this class
     */
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.CornOriCoord;
        hash = 61 * hash + this.FlipUDSliceCoord;
        return hash;
    }
}
