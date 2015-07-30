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
 * This class represents a set of Oriented Edges
 * @author russfeld
 */
import java.io.Serializable;
import rubiksolver.RubikModel2.*;

public class EdgeCubie implements Serializable{
        public OrientedEdge[] a;
        
        public EdgeCubie(){
            a = new OrientedEdge[Edge.values().length];
        }

        /**
         * This function creates a solved EdgeCubie
         * @param solved - whether to create a solved cube
         */
        public EdgeCubie(boolean solved){
            a = new OrientedEdge[Edge.values().length];
            if(solved){
                for(Edge ed : Edge.values()){
                    a[ed.ordinal()] = new OrientedEdge(ed, 0, 0);
                }
            }
        }

        /**
         * This function creates a cubie with the given Edge Orientation Coordinate
         * @param EdgeOriCoord - the given coordinate
         */
        public void InvCoord(int EdgeOriCoord){
            int sum = 0;
            for(int i = Edge.values().length - 2; i >= 0; i--){
                int temp = EdgeOriCoord % 2;
                a[i].o = temp;
                sum += temp;
                EdgeOriCoord = (int)(EdgeOriCoord / 2);
            }
            a[Edge.values().length - 1].o = 1 - ((sum + 1) % 2);
        }

        /**
         * This function creates a cubie with the given UDSlice Coordinate
         * @param UDSliceCoord - the given coordinate
         */
        public void UDSliceCoord(int UDSliceCoord){
            for(Edge ed : Edge.values()){
                a[ed.ordinal()] = new OrientedEdge(ed, 0, 0);
            }
            for(int i = 11; i >= 3; i--){
                for(int j = i - 1; j >= 2; j--){
                    for(int k = j - 1; k >= 1; k--){
                        for(int l = k - 1; l >=0; l--){
                            if(UDSliceCoord-- == 0){
                                OrientedEdge temp = a[8];
                                a[8] = a[l];
                                a[l] = temp;
                                
                                temp = a[9];
                                a[9] = a[k];
                                a[k] = temp;
                                
                                temp = a[10];
                                a[10] = a[j];
                                a[j] = temp;
                                
                                temp = a[11];
                                a[11] = a[i];
                                a[i] = temp;
                                return;
                            }
                        }
                    }
                }
            }
        }

        /**
         * This function creates a cubie with the given Edge Permutation Coordinate
         * @param EdgePermCoord - the given coordinate
         */
        public void InvEdgeCoord(int EdgePermCoord){
            for(Edge co : Edge.values()){
                a[co.ordinal()] = new OrientedEdge(co, 0, 0);
            }
            int x = EdgePermCoord;
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
                    OrientedEdge temp = a[i - j];
                    a[i - j] = a[i - j + 1];
                    a[i - j + 1] = temp;
                }
            }
        }

        /**
         * This function creates a cubie with the given Phase2 UDSlice Coordinate
         * @param P2UDSliceCoord - the given coordinate
         */
        public void InvP2UDSliceCoord(int P2UDSliceCoord){
            for(Edge co : Edge.values()){
                a[co.ordinal()] = new OrientedEdge(co, 0, 0);
            }
            int x = P2UDSliceCoord;
            int[] cord = new int[3];
            cord[2] = (int)(x / 6); //3!
            x = x - (6 * cord[2]);
            cord[1] = (int)(x / 2); //2!
            x = x - (2 * cord[1]);
            cord[0] = x;
            for(int i = 11; i > 8; i--){
                for(int j = cord[i - 9]; j > 0; j--){
                    OrientedEdge temp = a[i - j];
                    a[i - j] = a[i - j + 1];
                    a[i - j + 1] = temp;
                }
            }
        }
    }