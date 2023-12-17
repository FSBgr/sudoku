package sudoku;

import java.util.HashSet;

/**@author Alexandros Stergiopoulos
 *
 * The class Sudoku.Board represents a general sudoku board with no specific size
 * It exists as a mother class for the specific modes like classic, killer etc.
 * It isn't supposed to have any objects made by that class
 * The methods it has are for the general rules of all sudoku boards, which are that
 * every row, column and block must have unique numbers on them
 *
 *
 *
 */
public  class Board {
    /**
     * two dimensioned int array that stores the board (must be same dimensions?)
     * Note that all the fields are protected so that a class that inherits this one
     * can have direct access to them
     */
    int[][] sudokuBoard;

    /**
     * the size of row/column and the square root of it as it is used frequently
     */
    int sizeOfBlock;
    private int sqrtSizeOfBlock;

    /**
     * boolean field to determine if the board will be filled with numbers or letters(wordoku)
     * the difference is only in the eye of the player, because the array will always store numbers and just
     * convert them in letters for the GUI
     */
    private boolean wordoku;

    /** General constructor
     * sets the parameters to the fields and initializes the array on 0
     *
     * @throws IllegalArgumentException for negative size of array
     * @param size the size of the board (usually 9)
     * @param wordoku boolean value to determine if the board will be wordoku or not
     */
    public Board(int size, boolean wordoku){
        if(size<1) {
            throw new IllegalArgumentException("size must be positive");
        }
        this.wordoku=wordoku;
        sizeOfBlock=size;
        sqrtSizeOfBlock=(int)Math.sqrt(size);
        sudokuBoard= new int[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                sudokuBoard[i][j] = 0; //initialization to 0 to distinguish an empty spot
            }
        }

    }

    /**Constructor for default classic 9x9 board (not wordoku)
     *
     */
    public Board() {
        this(9, false);
    }

    /** Getter for wordoku
     *
     * @return wordoku
     */
    public boolean isWordoku(){
        return wordoku;
    }

    /** Getter for size of array
     *
     * @return size of array
     */
    public int getSize(){
        return sizeOfBlock;
    }

    /** Number to char Converter
     * Converts the numbers 1-9 (for size=9) to the letters A-I
     * using the unicode of the letters and numbers
     * If the parameter is illegal (out of bounds for the size) if returns a specific value to
     * distinguish the "false" input
     *
     * @param number the number
     * @param greek shows if the output letter must be greek or not
     * @return the char or - for illegal input
     */
    public char numberToChar(int number,boolean greek){
        if(greek){
            return numberToCharGreek(number);
        }
        if(number <1 || number > sizeOfBlock )
            return '-';
        return (char)(number + 64);
    }

    /** numberToChar For greek version
     * private because it is called only from method numberToChar
     *
     * @param number
     * @return the greek char or - for illegal input
     */
    private char numberToCharGreek(int number){
        if(number <1 || number > sizeOfBlock )
            return '-';
        return (char) (number + 912);
    }

    /** Char to number Converter
     * Converts the letters A-I or a-i ( eg for size=9) to the numbers 1-9
     * using the unicode of the letters and numbers
     * If the parameter is illegal (out of bounds for the size) if returns a specific value to
     * distinguish the "false" input
     *
     * @param letter the char
     * @param greek shows if the input letter is greek or not
     * @return the number or -1 for illegal input
     */
    public int charToNumber(char letter,boolean greek){ //65 is unicode of A, so from unicode we subtract 64 to transform the letter
        if(greek){
            return charToNumberGreek(letter);
        }
        letter=Character.toUpperCase(letter);
        if ((int)(letter) -64<1 ||(int)(letter) -64 >sizeOfBlock ) {
            return -1;
        }
        return (int)(letter) -64;
    }

    /** Char to number Converter for greek version
     * Converts the letters Α-Ι or α-ι ( eg for size=9) to the numbers 1-9
     * using the unicode of the letters and numbers
     * If the parameter is illegal (out of bounds for the size) if returns a specific value to
     * distinguish the "false" input
     * The first if transforms the character to Upper Case with the usage of unicode
     *Private because it is only called by method charToNumber
     *
     * @param letter the char
     * @return the number or -1 for illegal input
     */
    private int charToNumberGreek(char letter){
        if((int)letter>944 && (int)letter<970){  //944 is unicode of α and 970 of ω
            letter-=32; //making the letter to upper case
        }
        if ((int)(letter) - 912 <1 ||(int)(letter) -912 >sizeOfBlock ) {
            return -1;
        }
        return (int)(letter) -912; //912 is unicode of Α

    }

    /** Checks to see if the move is legal and makes is, or return a number to
     * distinguish the specific rule (the first one than finds actually) that would be broken by the move
     * Each check is done by a different method
     *
     * @param x the number of the row the player wants to put the number
     * @param y the number of the column the player wants to put the number
     * @param number the number the player wants to put the number
     *               In wordoku, the char will be converted from before with the method charToNumber
     * @return 0 when the move is following all the rules and thus is made
     *         -1 if the parameters x,y are out of bounds or the number is greater than the size
     *         1 if the move breaks the rule of the row
     *         2 if the move breaks the rule of the column
     *         3 if the move breaks the rule of the block
     *
     */
    public  int check(int x, int y, int number) {
        try {
            if(number>sizeOfBlock || number<1)
                return -1;
            if (!checkRow(x, number))  {
                return 1;
            } else if (!checkColumn(y, number)) {
                return 2;
            } else if(!checkBlock(x, y, number)) {
                return 3;
            } else {
                place(x, y, number);
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException exc){
            return -1;
        }
    }

    /** Checks if a move would break the rule of the row
     * The method checks if the same number is already put in the row
     * the player wants to put their number
     * Private because only check can call it
     *
     * @param x number of row
     * @param number the number the player wants to put
     * @return if the move can be made or not
     */
    private boolean checkRow(int x, int number){
        for (int i=0;i<sizeOfBlock;i++) {
            if (sudokuBoard[x][i] == number) {
                return false;
            }
        }
        return true;
    }

    /** Checks if a move would break the rule of the column
     * The method checks if the same number is already put in the column
     * the player wants to put their number
     * Private because only check can call it
     *
     * @param y number of column
     * @param number the number the player wants to put
     * @return if the move can be made or not
     */
    private boolean checkColumn(int y, int number){
        for (int i=0;i<sizeOfBlock;i++) {
            if (sudokuBoard[i][y] == number) {
                return false;
            }
        }
        return true;
    }

    /** Checks if a move would break the rule of the block
     * Firstly, it tracks the first box of the small block the player wants to put the number
     * then it scans the small block to check if the number is already in one of the boxes
     * Private because only check can call it
     *
     * @param x number of row
     * @param y number of column
     * @param number number
     * @return if the move can be made of not
     */
    private boolean checkBlock(int x, int y, int number){
        int blockX, blockY;
        blockX = x - (x %sqrtSizeOfBlock);
        blockY = y - (y % sqrtSizeOfBlock);
        for (int i = 0; i <sqrtSizeOfBlock; i++ ) {
            for (int j = 0; j <sqrtSizeOfBlock; j++) {
                if (sudokuBoard[blockX+i][blockY + j] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Places the number in the box in position x,y
     * Private because only check can call it
     *
     * @param x number of row
     * @param y number of column
     * @param number number the player wants to put
     */
    private void place(int x, int y, int number){
        sudokuBoard[x][y] = number;
    }

    /** Deletes the number that was put on the position x,y
     * Used by GUI to delete illegal inputs
     *
     * @param x number of row
     * @param y number of column
     */
    void delete(int x, int y){
        sudokuBoard[x][y]=0;
    }

    /**Checks all the positions in the board and if they are filled with a number, then the puzzle
     * is solved
     * If even one spot has a zero in it, then the game is not finished yet
     *
     * @return if the game if finished or not
     */
    public  boolean finished(){
        for(int i=0;i<sizeOfBlock;i++){
            for(int j=0;j<sizeOfBlock;j++){
                if(sudokuBoard[i][j]==0){
                    return false;
                }
            }
        }
        return true;
    }

    /** Checks all the numbers in the position x,y and if they can be legally placed , they are
     * added in a HashSet
     *
     * @param x the number of row
     * @param y the number of column
     * @return the numbers that can be placed in position x,y
     */
    public  HashSet<Integer> hint(int x, int y){
        HashSet<Integer> hintNumbers= new HashSet<>();
        for(int i=1;i<=sizeOfBlock;i++){
            if (checkRow(x, i) && checkColumn(y, i) && checkBlock(x,y,i)){
                hintNumbers.add(i);
            }
        }
        return hintNumbers;
    }
}