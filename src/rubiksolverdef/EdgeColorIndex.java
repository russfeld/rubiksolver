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
 * This class represents an Edge Color Index
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class EdgeColorIndex implements Serializable{
        public ColorIndex[][] a;
        
        public EdgeColorIndex(){
            a = new ColorIndex[Edge.values().length][2];
        }
    }
