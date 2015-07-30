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
 * This class represent the tables for the FlipUDSlice Sym Coordinates
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2;

public class FlipUDSlice implements Serializable {

        private transient RubikModel2 model;
        public int[][] FlipUDSliceToRaw;
        public int size;

        /**
         * Calcultates the FlipUDSliceToRaw table
         * @param aModel - the RubikModel to use
         */
        public FlipUDSlice(RubikModel2 aModel){
            System.out.print("Initializing FlipUDSlice: [" );
            model = aModel;
            FlipUDSliceToRaw = new int[64430][2];
            for(int x = 0; x < 64430; x++){
                FlipUDSliceToRaw[x][0] = -1;
            }
            size = 0;
            CubieCube cc = new CubieCube(true);
            for(int i = 0; i < 495; i++){ //UDSliceCoord
                cc.InvCoord(i);
                for(int j = 0; j < 2048; j++){ //EdgeOriCoord
                    cc.InvCoord(0, j);
                    int min = model.UDSliceRawReduce(cc);
                    int l;
                    for(l = 0; l < size; l++){
                        if(FlipUDSliceToRaw[l][0] == min){
                            FlipUDSliceToRaw[l][1]++;
                            break;
                        }
                    }
                    if(FlipUDSliceToRaw[l][0] != min){
                        FlipUDSliceToRaw[size][0] = min;
                        FlipUDSliceToRaw[size][1] = 0;
                        size++;
                    }
                }
                if(i % 20 == 0){
                    System.out.print("=");
                }
            }
            System.out.println("] Done! Size of table: " + size);
        }

        /**
         * Sets the model to use
         * @param aModel - the model to use
         */
        public void setModel(RubikModel2 aModel){
            model = aModel;
        }

        /**
         * Calculates the FlipUDSlice Sym Coordinate for the given cube
         * @param cc - the given cube
         * @return - the FlipUDSlice Coordinate
         */
        public int FlipUDSliceCoord(CubieCube cc){
            int tempIdx = model.SymReduce(cc);
            CubieCube tempC = model.SthenSinv(cc, tempIdx);
            int temp = (model.UDSliceCoord(tempC) * 2048) + model.EdgeOriCoord(tempC.e);
            for(int i = 0; i < size; i++){
                if(FlipUDSliceToRaw[i][0] == temp){
                    return i * 16 + tempIdx;
                }
            }
            System.out.println("Error: Could not find UDSliceRawCoord: " + temp + " " + size + " " + model.UDSliceCoord(cc) + " " + model.EdgeOriCoord(cc.e));
            return -1;
        }
}
