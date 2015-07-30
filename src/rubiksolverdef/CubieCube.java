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

package rubiksolverdef;

import java.io.Serializable;

/**
 * This represents a Rubik's Cube as a CornerCubie and an EdgeCubie
 * @author russfeld
 */
public class CubieCube implements Serializable{
    public CornerCubie c;
    public EdgeCubie e;
    
    public CubieCube(){
        c = new CornerCubie();
        e = new EdgeCubie();
    }

    /**
     * This creates a solved cube
     *
     * @param solved - whether to create a solved cube
     */
    public CubieCube(boolean solved){
        c = new CornerCubie(solved);
        e = new EdgeCubie(solved);
    }

    /**
     * This function creates a cube with the given coordinates
     * @param CornOriCoord - the Corner Orientation Coordinate
     * @param EdgeOriCoord - the Edge Orientation Coordinate
     */
    public void InvCoord(int CornOriCoord, int EdgeOriCoord) {
        c.InvCoord(CornOriCoord);
        e.InvCoord(EdgeOriCoord);
    }

    /**
     * This function creates a cube with the given Corner Permutation
     * Coordinate
     * @param CornPermCoord - the input coordinate
     */
    public void InvCornCoord(int CornPermCoord){
        c.InvCornCoord(CornPermCoord);
    }

    /**
     * This function creates a cube with the given Edge Permutation Coordinate
     * @param EdgePermCoord - the input coordinate
     */
    public void InvEdgeCoord(int EdgePermCoord){
        e.InvEdgeCoord(EdgePermCoord);
    }

    /**
     * This function creates a cube with the given Phase 2 UDSliceCoordinate
     * @param UDSliceCoord - the given coordinate
     */
    public void InvP2UDSliceCoord(int UDSliceCoord){
        e.InvP2UDSliceCoord(UDSliceCoord);
    }

    /**
     * This function creates a cube with the given UDSlice Coordinate
     * @param UDSliceCoord - the given coordinate
     */
    public void InvCoord(int UDSliceCoord){
        e.UDSliceCoord(UDSliceCoord);
    }
    
    
}
