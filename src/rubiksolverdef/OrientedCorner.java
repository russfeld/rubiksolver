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

/**
 * This class represents an Oriented Corner
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class OrientedCorner implements Serializable{
    public Corner c; //corner
    public int o; //orientation (0 = no twist, 1 = clockwise twist, 2 = counterclockwise twist}

    /**
     * Constructs a new Oriented Corner
     * @param aC - the Corner
     * @param aO - the Orientation
     */
    public OrientedCorner(Corner aC, int aO){
        c = aC;
        o = aO;
    }
}
