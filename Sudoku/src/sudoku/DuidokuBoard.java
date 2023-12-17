package sudoku;

import java.util.HashSet;
import java.util.Random;

/** Sudoku.DuidokuBoard is the class that represents the board of a Sudoku.Duidoku game
 * It inherits from the mother class Sudoku.Board and has no extra fields, only a few methods
 * so the pc can make a move on the board
 *
 */
public class DuidokuBoard extends Board {

    /** Default constructor that makes a 4x4 board
     *
     * @param wordoku is the game wordoku or not
     */
    DuidokuBoard(boolean wordoku){
        super(4,wordoku);
    }

    /** Constructor so that the board can be bigger
     *
     * @param size the size of the board
     * @param wordoku is the game wordoku or not
     */
    public DuidokuBoard(int size,boolean wordoku){
        super(size,wordoku);
    }

    /** Method is private because it is used by the method finished and by the pcMove
     *
     * @param x the number of row
     * @param y the number of column
     * @return if the position is empty or not
     */
    private boolean isPositionEmpty(int x,int y){
        return sudokuBoard[x][y]==0;
    }

    /** Method where the pc makes a random move, meaning it will place a number in a position
     * following the rules of sudoku. It generates three random numbers, two for the coordinates
     * of the position and one for the number. When the triplet consists of an empty position
     * and a number that can be legally put there, it makes the move
     * Before generating the numbers, it checks if the game has already finished and returns the triplet 0,0,0
     *
     * @return an int array: In 1st position is the x coordinate of the move made by the pc
     *                       In 2nd the y coordinates and in 3rd the number
     *                       When the game is finished before the pc made a move, the distinguishable number
     *                       is the third one, where it is only 0 then and only then
     */
    public int[] pcMove(){
        int[] coords = {0, 0, 0};
        if(finished()){
            return coords;
        }
        int x,y,number;
        Random r=new Random();
        do {
            x = r.nextInt(sizeOfBlock);
            y = r.nextInt(sizeOfBlock);
            number=r.nextInt(sizeOfBlock)+1;
            coords[0]= x;
            coords[1] = y;
            coords[2] = number;
        } while(!(isPositionEmpty(x,y)) || ((check(x,y,number))!=0));
        return coords;

    }

    /** Finished method by class Sudoku.Board needs overriding
     * here the game can be finished either by the original way (as in the super.finished) or if there
     * are empty boxes but no number can be inserted and follow all the rules
     * So first it checks the super and if there are some empty spots, it checks if at least in
     * one of them a number can be put. This is done by calling the hint method and seeing if the
     * hashSet is empty or not
     *
     * @return boolean value saying if the game is finished or not
     */
    @Override
    public boolean finished(){
        if(super.finished()) {
            return true;
        }
        for(int i=0;i<sizeOfBlock;i++){
            for (int j=0;j<sizeOfBlock;j++){
                if(isPositionEmpty(i,j)){
                    HashSet<Integer> posHint=hint(i,j);
                    if(!posHint.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
