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
 * This class represents a set of Oriented Corners
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class CornerCubie implements Serializable{
        public OrientedCorner[] a;
        
        public CornerCubie(){
            a = new OrientedCorner[Corner.values().length];
        }

        /**
         * Generates a solved CornerCubie
         * @param solved
         */
        public CornerCubie(boolean solved){
            a = new OrientedCorner[Corner.values().length];
            if(solved){
                for(Corner co : Corner.values()){
                    a[co.ordinal()] = new OrientedCorner(co, 0);
                }
            }
        }

        /**
         * Creates a cube with the given Corner Orientation Coordinate
         *
         * @param CornOriCoord - the input coordinate
         */
        public void InvCoord(int CornOriCoord){
            int sum = 0;
            for(int i = Corner.values().length - 2; i >= 0; i--){
                int temp = CornOriCoord % 3;
                a[i].o = temp;
                sum += temp;
                CornOriCoord = (int)(CornOriCoord / 3);
            }
            a[Corner.values().length - 1].o = 2 - ((sum + 2) % 3);
        }

        /**
         * Creates a cube with the given Corner Permutation Coordinate
         *
         * @param CornPermCoord - the input coordinate
         */
        public void InvCornCoord(int CornPermCoord){
            for(Corner co : Corner.values()){
                a[co.ordinal()] = new OrientedCorner(co, 0);
            }
            int x = CornPermCoord;
            int[] cord = new int[7];
            cord[6] = (int)(x / 5040); //7!
            x = x - (5040 * cord[6]);
            cord[5] = (int)(x / 720); //6!
            x = x - (720 * cord[5]);
            cord[4] = (int)(x / 120); //5!
            x = x - (120 * cord[4]);
            cord[3] = (int)(x / 24); //4!
            x = x - (24 * cord[3]);
            cord[2] = (int)(x / 6); //3!
            x = x - (6 * cord[2]);
            cord[1] = (int)(x / 2); //2!
            x = x - (2 * cord[1]);
            cord[0] = x;
            for(int i = 7; i > 0; i--){
                for(int j = cord[i - 1]; j > 0; j--){
                    OrientedCorner temp = a[i - j];
                    a[i - j] = a[i - j + 1];
                    a[i - j + 1] = temp;
                }
            }
        }
    }
