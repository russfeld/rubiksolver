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
 * This class represents an oriented edge
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class OrientedEdge implements Serializable{
        public Edge e; //edge
        public int o; //orientation (0 = no twist, 1 = twist}
        public int oA; //???

        /**
         * This constructs the oriented edge
         * @param aE - the edge
         * @param aO - the orientation
         * @param aOA - the alternate orientation
         */
        public OrientedEdge(Edge aE, int aO, int aOA){
            e = aE;
            o = aO;
            oA = aOA;
        }
    }
