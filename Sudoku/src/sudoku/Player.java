package sudoku;

import java.io.*;
import java.util.HashSet;

/**@author Alexandros Stergiopoulos
 *
 * The class player represents a user playing the game sudoku
 *
 * Note that an object of the class player will always exist even if the player wants to play anonymous
 * To distinguish the anonymous player, we give them the name "unknown" so the stats of the
 * anonymous player won't be stored anywhere.
 */
public class Player {
    /** The class consists of a name, the wins and loses they have on mode Sudoku.Duidoku
     * and the sudoku and killer sudoku games they've won
     * The last two are stored in HashSets and they contain the codes of the games they've won
     *
     */
    private String name;
    private int duidokuWins;
    private int duidokuLoses;
    private HashSet<Integer> classicSudokuGamesWon;
    private HashSet<Integer> killerSudokuGamesWon;

    /** Constructor
     *
     * @param aName the name of the user
     */
    public Player(String aName){
        name = aName;
        duidokuWins = 0;
        duidokuLoses = 0;
        classicSudokuGamesWon = new HashSet<>();
        killerSudokuGamesWon = new HashSet<>();
    }

    /** Default Constructor that the GUI uses with name unknown
     *
     */
    public Player(){
        this("unknown");
    }


    /** Getter for the name
     *
     * @return the name of player
     */
    public String getName(){return name;}


    /**Getter for duidokuWins
     *
     * @return duidokuWins
     */
    public int getDuidokuWins(){return duidokuWins;}

    /** Getter for duidokuLoses
     *
     * @return duidokuLoses
     */
    public int getDuidokuLoses(){return duidokuLoses;}

    /** Method that adds the code of a ClassicSudoku
     *
     * @param code the code of a ClassicSudoku
     */
    public void addClassicSudoku(int code){
        classicSudokuGamesWon.add(code);
    }

    /** Method that adds the code of a Sudoku.KillerSudoku
     *
     * @param code the code of a Sudoku.KillerSudoku
     */
    public void addKillerSudoku(int code){
        killerSudokuGamesWon.add(code);
    }

    /** Method that adds one win on the duidokuWins
     *
     */
    public void duidokuWon() {
        duidokuWins++;
    }

    /** Method that adds one lose on the duidokuLoses
     *
     */
    public void duidokuLost(){
        duidokuLoses++;
    }

    /** Used on classic board classes to load a classic game that the player hasn't played yet
     *
     * @return the HashSet of ClassicSudokuGamesWon
     */
    public HashSet<Integer> getClassicSudokuGamesWon(){return classicSudokuGamesWon;}

    /** Used on killer board classes to load a killer game that the player hasn't played yet
     *
     * @return the HashSet of KillerSudokuGamesWon
     */
    public HashSet<Integer> getKillerSudokuGamesWon(){return killerSudokuGamesWon;}

    /** toString method (not override, so System.out.println(player) won't work
     * If the language is greek, then the string is written in greek
     *
     * @param greek if language is greek or not
     * @return how we want to show the stats of the player
     */
    public String toString(boolean greek){
        if(greek){
            return name + "\n Κλασσικά που νικήσατε: " + classicSudokuGamesWon.size() +
                    "\n Killers που νικήσατε: " + killerSudokuGamesWon.size() +
                    "\n Νίκες στο Duidoku: " + duidokuWins + "\n Ήττες στο Duidoku: " + duidokuLoses;
        }
        return name + "\n Classics won: " + classicSudokuGamesWon.size() +
                "\n Killers won: " + killerSudokuGamesWon.size() +
                "\n Duidoku wins: " + duidokuWins + "\n Duidoku loses: " + duidokuLoses;
    }


    /** When a player wants to login with a different name, the method either loads the stats of the player
     * or makes a "new" player like a constructor
     * General method for any filename
     *
     * @param name the new name that the user will use
     * @param fileName name of the file
     */
    public void loginPlayer(String name,String fileName) {
        if(playerFoundOnFile(name,fileName)){
            loadStatsFromFile(name,fileName);
        } else {
            this.name=name;
            duidokuWins=0;
            duidokuLoses=0;
            classicSudokuGamesWon = new HashSet<>();
            killerSudokuGamesWon = new HashSet<>();
        }
    }

    /**Default method for this version where fileName is Players.txt
     *
    */
    void loginPlayer(String name) { loginPlayer(name,"Players.txt");}

    /** Searches on the file to check whether the player has played again (the player's stats
     * are on the file)
     *
     * @throws IOException if there is any problem with the file
     * @param newName the name that is going to be searched on the file
     * @param fileName name of file that players are stored
     * @return true if the name exists on the file or false if it doesn't
     */
    private boolean playerFoundOnFile(String newName, String fileName){
        try(BufferedReader read= new BufferedReader( new FileReader(fileName))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (line.startsWith(newName + ",")) {
                    return true;
                }
            }
        } catch (IOException exp){
            System.err.println("problem with file");
        }
        return false;
    }

    /** Called only when the newName has been found on the file
     * It then finds again the line of the player's stats and stores them on the fields of the object
     * the players are all stored in a certain way to be loaded correctly on the file "Players.txt"
     * The way that a player is stored is like this:
     * name,duidokuWins,duidokuLoses,the codes of the classicSudoku games that are won separated by a
     * space,the codes of the killerSudoku games that are won separated by a space
     * eg a player named Alex with 3 wins, 5 loses and won the classics 1,5,7,8 and the
     * killers 2,3,6,7,8 is:
     * Alex,3,5,1 5 7 8 ,2 3 6 7 8
     *
     * The reason behind this particular way they are stored is to separate them
     * easily with String.split method
     * @throws IOException if there is a problem with the file
     * @throws NumberFormatException  if the stats were not stored in the particular way described above
     * @param newName the name that the user will now use
     * @param fileName the name of the file
     */
    private void loadStatsFromFile(String newName,String fileName) {
        try(BufferedReader read= new BufferedReader( new FileReader(fileName))){
            String line;
            while((line=read.readLine()) != null){
                if(line.startsWith(newName+",")) {
                    String[] parts= line.split(",");
                    name=parts[0];
                    duidokuWins=Integer.parseInt(parts[1]);
                    duidokuLoses=Integer.parseInt(parts[2]);
                    String[] classicWins = parts[3].split(" ");
                    if(!classicWins[0].equals("0")) {
                        for (String classicWin : classicWins) {
                            addClassicSudoku(Integer.parseInt(classicWin));
                        }
                    }
                    String[] killerWins = parts[4].split(" ");
                    if(!killerWins[0].equals("0")) {
                        for (String killerCode : killerWins) {
                            addKillerSudoku(Integer.parseInt(killerCode));
                        }
                    }
                }
            }
        } catch (IOException exp){
            System.err.println("loadStats exception");
        } catch (NumberFormatException exc){
            System.err.println("file storing is wrong");
            name="unknown";
            duidokuWins=0;
            duidokuLoses=0;
            classicSudokuGamesWon = new HashSet<>();
            killerSudokuGamesWon = new HashSet<>();
        }
    }

    /** Changes the file with the new stats of the player
     * To do that, it stores all the other names on a temp txt file and if it finds the player, it changes
     * the line with the updated stats. If the player wasn't on the file before, they will be added
     * to the bottom. After that, is copies the entire temp file line by line and pastes it to the original one.
     * In the end, it deletes the temp file
     *
     * This method is always called after a change of a field, even in the anonymous play
     * So if the player has played anonymously (as discussed above they will have the name unknown) the
     * change will not happen
     *
     * General method for any file
     * @param fileName name of file
     */
    public void changePlayerFile(String fileName) {
        if(name.equals("unknown")){
            return; // won't need an update on anonymous plays
        }
        boolean playerExisted=playerFoundOnFile(this.name,fileName);
        StringBuilder playerLine = new StringBuilder(); //bringing the stats on the way they are stored on the file
        playerLine.append(name).append(",").append(duidokuWins).append(",").append(duidokuLoses).append(",");
        if (classicSudokuGamesWon.isEmpty()) {
            playerLine.append("0,");
        } else {
            for (int classicCode : classicSudokuGamesWon) {
                playerLine.append(classicCode).append(" ");
            }
            playerLine.append(",");
        }
        if (killerSudokuGamesWon.isEmpty()) {
            playerLine.append("0");
        } else {
            for (int killerCode : killerSudokuGamesWon) {
                playerLine.append(killerCode).append(" ");
            }
        }
        try (BufferedReader read = new BufferedReader(new FileReader(fileName)) // overwriting the others
               ; PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tempPlayers.txt")))) { //in temp file
            String line;
                while ((line = read.readLine()) != null) {
                    if (line.startsWith(name + ",")) { //in line of particular player we put the new stats
                        out.println(playerLine.toString());
                    } else {
                        out.println(line);
                    }
                }
                if(!playerExisted){
                    out.println(playerLine.toString());
                }
            } catch (IOException exp) {
            // System.out.println("problem");
        }
        try (BufferedReader read = new BufferedReader(new FileReader("tempPlayers.txt"))
             ; PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            String line;
            while ((line = read.readLine()) != null) {
                if (!line.equals("\n")) {  //a blank line always shows up so we don't overwrite it
                    out.println(line);
                }
            }
        } catch (IOException exp) {
            // System.out.println("problem");
        }
        File file = new File("tempPlayers.txt");  //delete of tempFile
        file.delete();
    }

    /**Default method for this version with fileName Players.txt
     *
     */
     void changePlayerFile() {
        changePlayerFile("Players.txt");
    }

}
