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
 * This class represents the tables needed for the Corner Permutation Sym Coordinate
 */
import java.io.Serializable;
import rubiksolver.RubikModel2;

public class CornerPerm implements Serializable {

        private transient RubikModel2 model;
        public int[][] CornPermToRaw;
        public int size;

        /**
         * This calculates the CornPermToRaw table
         *
         * @param aModel - the RubikModel to use
         */
        public CornerPerm(RubikModel2 aModel){
            System.out.print("Initializing CornerPerm: [" );
            model = aModel;
            CornPermToRaw = new int[2768][2];
            for(int x = 0; x < 2768; x++){
                CornPermToRaw[x][0] = -1;
            }
            size = 0;
            CubieCube cc = new CubieCube(true);
            for(int i = 0; i < 40320; i++){ //UDSliceCoord
                cc.InvCornCoord(i);
                int min = model.CornPermRawReduce(cc);
                int l;
                for(l = 0; l < size; l++){
                    if(CornPermToRaw[l][0] == min){
                        CornPermToRaw[l][1]++;
                        break;
                    }
                }
                if(CornPermToRaw[l][0] != min){
                    CornPermToRaw[size][0] = min;
                    CornPermToRaw[size][1] = 0;
                    size++;
                }
                if(i % 2000 == 0){
                    System.out.print("=");
                }
            }
            System.out.println("] Done! Size of table: " + size);
        }

        /**
         * This function sets the model to use
         * @param aModel - the model to use
         */
        public void setModel(RubikModel2 aModel){
            model = aModel;
        }

        /**
         * This function calculates the Corner Permutation Coordinate of the given
         * cube
         * @param cc - the input cube
         * @return the Corner Permutation Sym Coordinate
         */
        public int CornPermCoord(CubieCube cc){
            int tempIdx = model.CornSymReduce(cc);
            CubieCube tempC = model.SthenSinv(cc, tempIdx);
            int temp = model.CornPermCoord(tempC.c);
            for(int i = 0; i < size; i++){
                if(CornPermToRaw[i][0] == temp){
                    return i * 16 + tempIdx;
                }
            }
            System.out.println("Error: Could not find CornPermCoord: " + temp + " " + size + " " + model.CornPermCoord(cc.c));
            return -1;
        }
}
