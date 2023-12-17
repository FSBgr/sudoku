package sudoku;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**@author Alexandros Stergiopoulos, Christos Christidis
 *
 * The class Sudoku.KillerBoard represents a board for a puzzle of a killer sudoku
 * Killer sudoku has the same rules as the classic one with one more
 * The board is given with every box belonging in a group where the sum of the numbers
 * inside a group equals a number given before
 * It is more challenging finding a way to store a killer board and check if the new rule
 * applies when the player wants to put a number
 * The Sudoku.KillerBoard extends the class Sudoku.ClassicBoard because it needs every method of this class
 * with some changes, but also needs some more methods
 *
 */
public class KillerBoard extends ClassicBoard {
    /** To store the Killer Sudoku.Board, except the fields of Sudoku.ClassicBoard, we need
     * to somehow store the color groups with their sum and to match the boxes with their
     * color group
     * For the storing of the sums, a HashMap is used where the key is an Integer that stores the color code
     * and the value is another integer holding the sum of the specific color code. The usage of an
     * Integer for a key happened to keep the logic separate from the GUI, and also because the board doesn't
     * have a rule about how many color groups there will be. Eg one killer board may have 29 color codes
     * and another can have 38. So the idea of having a String as a key that stores the specific color
     * that will be used. But to keep the idea of coloring the groups, there is a private field of an array
     * of Colors. For this version, there are 12 different colors, but there are methods to add colors
     */
    private ArrayList<Color> colors;
    private HashMap<Integer, Integer> colorSums;
    private int[][] colorBoard;

    /** General constructor without a code for puzzle
     *
     * @param size size of board
     * @param puzzles number of puzzles
     * @param wordoku is wordoku or not
     */
    public KillerBoard(int size, int puzzles,boolean wordoku){
        super(size,puzzles, wordoku);
        colorBoard= new int[size][size];
        colorSums= new HashMap<>();
        final Color VERY_LIGHT_RED = new Color(255, 101, 121);
        final Color VERY_LIGHT_GREEN = new Color(88, 144, 79);
        final Color LIGHT_YELLOW = new Color(254, 225, 162);
        colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(LIGHT_YELLOW);
        colors.add(Color.YELLOW);
        colors.add(Color.PINK);
        colors.add(Color.magenta);
        colors.add(Color.cyan);
        colors.add(Color.lightGray);
        colors.add(Color.white);
        colors.add(Color.orange);
        colors.add(VERY_LIGHT_GREEN);
        colors.add(VERY_LIGHT_RED);
    }


    /** Default constructor used in this version with 9 for size and 10 for No of puzzles
     *
     * @param wordoku is wordoku of not
     */
    public KillerBoard(boolean wordoku){
        this(9,10,wordoku);
    }

    /** General constructor for board with code and initialization
     *
     * @param size size of board
     * @param puzzles No of puzzles
     * @param wordoku is wordoku or not
     * @param code code of killer puzzle
     * @param fileName name of file that contains the boards
     */
    public KillerBoard(int size, int puzzles, boolean wordoku, int code, String fileName){
        this(size,puzzles,wordoku);
        if(code>puzzles){
            throw new IllegalArgumentException("code must be lesser than number of puzzles");
        }
        if(code<1){
            throw new IllegalArgumentException("code must be positive number");
        }
        this.code=code;
        loadBoard(fileName);
    }

    /** Constructor to load a specific board from this version
     *
     * @param wordoku is wordoku or not
     * @param code code of Killer Sudoku.Board
     */
    public KillerBoard( boolean wordoku, int code){
        this(9,10,wordoku,code,"Killer Sudoku Puzzles.txt");
    }

    /** Method to create and add a new RGB color. Based on the three int RGB params, a new color is created and then added to
     * the colors' set.
     * @param r, red
     * @param g, green
     * @param b, blue
     */
   public void addColor(int r, int g, int b){
       final Color NEW_COLOR = new Color(r, g, b);
       colors.add(NEW_COLOR);

   }


    /**
     * Method to delete a color. Checking if the corresponding color to the RGB params exists within the colors' set
     * and removes it.
     * @param r
     * @param g
     * @param b
     * @throws IllegalArgumentException if the color does not exist in the set.
     */
   public void deleteColor(int r, int g, int b){
       final Color TO_BE_REMOVED = new Color(r, g, b);
       if(!colors.remove(TO_BE_REMOVED)) {
           throw new IllegalArgumentException("No such color in the ArrayList");
       }
   }

    /** Method which returns the size of ArrayList colors
     *
     * @return how many colors there are inside
     */
    public int howManyColors(){
       return colors.size();
    }

    /** Method to get a specific color
     *
     * @param index the index of the color you want to take
     * @return the color with index number
     */
    public Color numberToColor(int index){
        return colors.get(index);
    }

    /** Method used by GUI to color the boxes
     *
     * @return the entire  colorBoard
     */

     int[][] getColorBoard(){return colorBoard;}

    /** Method needs overriding, but it only changes the HashSet parameter on the method findNonPlayedGame
     *
     * @param player a player needed for their stats
     * @param fileName the name of the file that contains the puzzles
     */
    @Override
    public void startNewGameForPlayer(Player player, String fileName){
        findNonPlayedGame(player.getKillerSudokuGamesWon());
        loadBoard(fileName);
    }

    /** Method that changes only the name of the file
     *
     * @param player a player needed for their stats
     */
    @Override
    public void startNewGameForPlayer(Player player) {
        startNewGameForPlayer(player, "Killer Sudoku Puzzles.txt");
    }

//    public void showColorBoard(){  //for tests only
////        for(int i=0;i<sizeOfBlock;i++){
////            for(int j=0;j<sizeOfBlock;j++){
////                System.out.print(colorBoard[i][j]+ " ");
////            }
////            System.out.println();
////        }
////    }
////    public void showColorCodes(){  //for tests only
////        for(int key:colorSums.keySet()){
////            System.out.print(key);
////            System.out.println(" " + colorSums.get(key));
////        }
////    }

    /** Loads the board, specifically fills the colorBoard and the HashMap colorSums
     * Called always after the object has been given a code either by the method findNonPlayedGame
     * or by a constructor.
     * The file has a specific way to store a board. Firstly, the code of the board is on the first row alone
     * Below that is the colorBoard, which is a 9x9 area filled with numbers representing
     * the colorGroup the position belongs all separated with a space. Eg if in a row the first 4 boxes are on the color
     * group with code 5, the 5th and 6th one in the group with code 6 and the rest boxes each belongs to
     * a different group with codes 7, 8 and 4, the row will be like this:
     * 5 5 5 5 6 6 7 8 4
     * After the colorBoard, the codes and sums come all in a row, with each codeSum being:
     * colorCode sumOfColorCode and all the codeSums are separated with commas
     * Eg of board No 7:
     * 7
     * 1 2 3 4 4 5 5 6 7
     * 1 2 3 4 8 8 9 6 7
     * 1 10 3 4 8 8 9 11 11
     * 1 10 12 12 13 13 14 14 15
     * 1 16 16 17 17 13 14 14 15
     * 18 19 19 17 20 20 14 14 14
     * 18 18 21 21 22 22 14 23 24
     * 25 25 25 26 26 26 23 23 24
     * 27 27 26 26 28 28 29 29 24
     * 1 24,2 13,3 15,4 22,5 14,6 17,7 5,8 14,9 5,10 6,11 13,...
     * The way these are stored is to help separate them with String method split
     *
     * @throws IOException if something goes wrong with the file that has the puzzles stored
     * @param fileName the name of the file that stores the killer sudoku puzzles
     */
    @Override
    protected void loadBoard(String fileName){
        try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.equals(Integer.toString(code))) {
                    for (int i = 0; i < sizeOfBlock; i++) {
                        line = read.readLine();
                        String[] row = line.split(" ");
                        for (int j = 0; j < sizeOfBlock; j++) {
                            int temp= Integer.parseInt(row[j]);
                            colorBoard[i][j] = temp;
                        }
                    }
                    line=read.readLine();
                    String[] colorCodes=line.split(",");
                    for(String codes:colorCodes){
                        String[] codeSum=codes.split(" ");
                        int code=Integer.parseInt(codeSum[0]);
                        int sum=Integer.parseInt(codeSum[1]);
                        colorSums.put(code,sum);
                    }
                }
            }
        } catch (IOException exc) {
            System.out.println("Problem with file");
        } catch (NumberFormatException exc){
            System.err.println("Problem with storing of the killer board in file");
        }
    }

    /** Check method needs overriding because there are new rules
     * the number 4 is returned when the number the player wants to put breaks the rule
     * of the color sum.
     *
     * @param x the number of the row the player wants to put the number
     * @param y the number of the column the player wants to put the number
     * @param number the number the player wants to put the number
     *               In wordoku, the char will be converted from before with the method charToNumber
     * @return a code that shows which rule is broken, or 0 to show that the move was made
     */
    @Override
    public int check(int x, int y, int number){
        if (!checkColor(x, y, number)) {
            return 4;
        }
        return super.check(x,y,number);
    }

    /** Method to check if the number will somehow break the rule
     * The ways that the rule can be broken are these:
     * 1) If you put this number, the sum will be greater that the one needed to be placed
     * 2) If you put the number, the sum is exactly as it needs to be, but there are empty
     * boxes of the color group
     * 3) If you put the number, the sum is lesser that the one needed to be and the player
     * used the last box of this color group
     * 4) If you put the number, the sum is lesser than it needs to be and the remaining boxes
     * of the color group cannot reach the sum because the number the player wants to put is too small
     *
     * @param x number of row
     * @param y number of column
     * @param number number the player wants to put
     * @return whether the move can be made or not
     */
    private boolean checkColor(int x, int y, int number){
        if(number<1 || number>sizeOfBlock){ //weird result but must be like that so the super.check
            return true;                    // will decline the number with the right mistake code
        }
        int maxSum=colorSums.get(colorBoard[x][y]);
        int blocksOfColor=0;
        int blocksOfColorFilled=0;
        int previousSum=0;
        for(int i=0;i<sizeOfBlock;i++){
            for(int j=0;j<sizeOfBlock;j++){
                if(colorBoard[i][j]==colorBoard[x][y]){
                    if(sudokuBoard[i][j]!=0){
                        previousSum+=sudokuBoard[i][j];
                        blocksOfColorFilled++;
                    }
                    blocksOfColor++;
                }
            }
        }
        if(previousSum+number > maxSum){
            return false;
        }
        if(previousSum+number==maxSum && blocksOfColorFilled+1<blocksOfColor){ //+1 for the block that would be filled
            return false;
        }
        if( previousSum + number<maxSum) {
            if (blocksOfColor == blocksOfColorFilled + 1) {
                return false;
            }
            return previousSum + number + (blocksOfColor - blocksOfColorFilled - 1) * sizeOfBlock >= maxSum;
        }
        return true;
    }

    /** Returns the corresponding sum of the key of a color
     *
     * @param key
     * @return the sum of color with code key
     */
    public int getColorSum(int key){
        return colorSums.get(key);
    }

    /** Hint method needs overriding because of the new rule
     * The method works by taking all the numbers that can be put with the previous rules and then
     * it checks each of these to see if they break the new rule
     *
     * @param x the number of row
     * @param y the number of column
     * @return a set of numbers that can be put in position x,y following the rules
     */
    @Override
    public HashSet<Integer> hint(int x, int y){
        HashSet<Integer> hintNumbers=super.hint(x,y);
        hintNumbers.removeIf(number -> !checkColor(x, y, number));
        return hintNumbers;
    }

}
