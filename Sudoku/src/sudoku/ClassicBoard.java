package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

/** @author Alexandros Stergiopoulos & Christos Christidis
 *
 * Sudoku.ClassicBoard is the class that represents the board of a classic sudoku
 * It extends the class Sudoku.Board, that has methods that every sudoku game must have
 * So this class has methods mostly about loading a board
 *
 */
public class ClassicBoard extends Board {
    /**
     * Beside the fields that it has because it inherits Sudoku.Board, this class
     * has a field about the code of a specific sudoku puzzle game and another field that
     * stores the number of the different games there are on a file that has all the puzzles
     * Fields are protected because a lot of different game modes can be based on the classic game
     * and have some extra details
     */
    private int numberOfPuzzles;
    int code;


    /** General constructor without loading a board
     *
     * @throws IllegalArgumentException for negative number of puzzles
     * @param size size of board
     * @param puzzles number of puzzles
     * @param wordoku is wordoku or not
     */
    ClassicBoard(int size, int puzzles, boolean wordoku) {
        super(size, wordoku);
        code = 0;
        if(puzzles<1)
            throw new IllegalArgumentException("puzzles must be positive number");
        numberOfPuzzles=puzzles;
    }

    /** Constructor for Sudoku.ClassicBoard
     * code initializes to 0 (from the other constructor) and therefore the board isn't loaded with some numbers
     * numberOfPuzzles in this version is 10
     *
     * @param size the size of the board
     * @param wordoku tells if the board is wordoku or not
     */
    public ClassicBoard(int size, boolean wordoku) {
        this(size,10,wordoku);
    }

    /** Default Constructor for Sudoku.ClassicBoard that is used in this version
     * code initializes to 0 and therefore the board isn't loaded with some numbers
     * numberOfPuzzles in this version is 10
     * the size in the default classic is 9
     *
     * @param wordoku tells if the board is wordoku or not
     */
    ClassicBoard(boolean wordoku) {
        this(9,10,wordoku);
    }

    /** Constructor that immediately loads a puzzle
     *
     * @throws IllegalArgumentException for negative code or code greater than the number of puzzles
     * @param size size of board
     * @param wordoku is wordoku or not
     * @param puzzles number of puzzles
     * @param code the code of the puzzle that will be loaded
     */
    public ClassicBoard(int size,boolean wordoku, int puzzles, int code,String fileName){
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

    /** Constructor for classicBoard if you want to load a specific puzzle knowing its code
     * After the initialization of the fields, it immediately loads the board because the code is given
     *
     * @param size the size of the board
     * @param wordoku tells if the board is wordoku or not
     * @param code the code of the puzzle
     */
    public ClassicBoard(int size,boolean wordoku, int code){
        this(size,wordoku,10,code,"Classic Sudoku Puzzles.txt");
    }

    /** Getter for numberOfPuzzles
     *  built in case the number of puzzles changes in some other version
     *
     * @return numberOfPuzzles
     */
    public int getNumberOfPuzzles(){return numberOfPuzzles;}

    /** Setter for numberOfPuzzles
     * In case another version will increase the number of puzzles in the file that stores them
     *
     * @param size the new number of puzzles
     */
    public void setNumberOfPuzzles(int size){numberOfPuzzles=size;}

    /** Getter for code of puzzle
     *
     * @return the code of the puzzle
     */
    public int getCode() {
        return code;
    }

    /** Method where given a set of codes, it sets the object with a random code not belonging in the set
     * If the set is empty or full(has all the codes) it sets the first random number it generates
     * If not, it generates numbers from 1 to numberOfPuzzles until one of them isn't on the Set
     * The method is protected, because it needs to be inherited in games like killer that extend this class
     *
     * @param wonGames HashSet with codes of already won games
     */
    void findNonPlayedGame(HashSet<Integer> wonGames) {
        Random r = new Random();
        if (wonGames.size() == numberOfPuzzles || wonGames.isEmpty()) {
            code = r.nextInt(numberOfPuzzles) + 1;
        } else {
            do {
                code = r.nextInt(numberOfPuzzles) + 1;
            } while (wonGames.contains(code));
        }
    }

    /** loads a game for a player that they haven't played before
     * Used if another programmes stores the boards on different a file
     *
     * @param player a player needed for their stats
     * @param fileName the name of the file that contains the puzzles
     */
    public void startNewGameForPlayer(Player player, String fileName) {
        findNonPlayedGame(player.getClassicSudokuGamesWon());
        loadBoard(fileName);
    }

    /** loads a game for a player that they haven't played before
     * The method used by GUI that simply calls two protected methods
     * Default method for this version where the file name is Classic Sudoku Puzzles.txt
     *
     * @param player a player needed for their stats
     */
    public void startNewGameForPlayer(Player player) {
        startNewGameForPlayer(player,"Classic Sudoku Puzzles.txt");
    }

    /** After a code has been given on the field code (by the constructor or the method findNonPlayedGame)
     * the method scans the file "Classic Sudoku Puzzles" that has all the games stored and loads the one
     * with the code of the object
     * The file has a certain way that stores the boards, which is: On a single line there is the code of the
     * puzzle (the code is an integer number and on this library  we start with code:1) and below is the board
     * The boards have some positions filled with the correct numbers, and the empty spots have
     * a zero in their place. The numbers are separated by a space
     * eg the board with code 4 is:
     * 4
     * 2 1 0 0 0 0 0 0 0
     * 0 7 9 0 0 0 0 6 4
     * 5 3 0 2 0 6 0 0 0
     * 0 0 0 9 1 0 0 7 2
     * 0 5 7 6 0 2 3 4 0
     * 9 6 0 0 7 4 0 0 0
     * 0 0 0 8 0 7 0 1 6
     * 7 2 0 0 0 0 5 9 0
     * 0 0 0 0 0 0 0 2 8
     *
     * @throws IOException for problem with the file
     * @param fileName the name of the file that stores the classic sudoku puzzles
     */
    protected void loadBoard(String fileName) {
        try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.equals(Integer.toString(code))) {
                    for (int i = 0; i < sizeOfBlock; i++) {
                        line = read.readLine();
                        String[] row = line.split(" ");
                        for (int j = 0; j < sizeOfBlock; j++) {
                            sudokuBoard[i][j] = Integer.parseInt(row[j]);
                        }
                    }
                }
            }
        } catch (IOException exc) {
            System.out.println("Problem with file");
        } catch (NumberFormatException exc) {
            System.err.println("Problem with storing of board in file");
        }
    }
}
