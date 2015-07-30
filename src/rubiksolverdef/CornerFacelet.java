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
 * This class represents a single Corner Facelet
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class CornerFacelet implements Serializable{
        public Face[][] a;
        
        public CornerFacelet(){
            a = new Face[Corner.values().length][3];
        }
    }