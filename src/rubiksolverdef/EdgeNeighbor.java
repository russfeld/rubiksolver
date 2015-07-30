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
 * This class represents the neighbors of an edge
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class EdgeNeighbor implements Serializable{
        public Corner[][] a;
        
        public EdgeNeighbor(){
            a = new Corner[Edge.values().length][2];
        }
    }
