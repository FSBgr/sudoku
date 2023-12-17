package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.ResourceBundle;

/** @author Christos Christidis
 *
 * This class represents the window/frame where the duidoku mode of the game is played.
 */

public class DuidokuMode extends JFrame{
    /**
     * Constructor of the Frame. The whole body of the code was included within the constructor, since this is a
     * GUI class. Because of its nature, it's difficult to describe what happens in this block of javadoc, since plenty
     * of internal classes and methods are created and/or overrode. For that matter, each internal class and function
     * has its own javadoc.
     *
     * @param board
     * @param player
     * @param greek
     * @param bundle
     */

    public DuidokuMode(DuidokuBoard board, Player player, boolean greek, ResourceBundle bundle){

        /** Setting the title of the frame based on the language (greek/english) via the resource bundle, setting
         * default size and close operation. Adding a new grid layout based on the size of the board with one extra row
         * in order to include a check button later on. Starting new game from object board. Setting the font that will
         * be used in the textFields.
         */
        setTitle(bundle.getString("button3"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(board.getSize(),board.getSize()));
        Font font = new Font("Times New Roman", Font.BOLD, 40);

        /** Creating a 2 dimensional array of JTextField (based on the size of the board)
         * that represents the sudoku grid (boxes) in which the player will be able to place their moves.
         */
        JTextField[][] boxes = new JTextField[board.getSize()][board.getSize()];

        for(int i=0; i<board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {

                /**Double for-loop for the permeation of each element of array boxes. Within this loop, all mouse and
                 *key listeners will be added to each text field of the array.
                 *
                 * Reserving memory space for each element of array boxes and setting its size to 50x25
                 *
                 */

                boxes[i][j] = new JTextField(0);
                boxes[i][j].setSize(1, 1);

                /** Declaring two new int variables with the value of i,j in each cycle of the loop that will be used
                 * later on in each listener.
                 */

                int finalI = i;
                int finalJ = j;

                /** Adding mouse listener to each element of the array boxes*/

                boxes[i][j].addMouseListener(new MouseListener() {

                     /**
                     *
                     * Every time the player left-clicks on an editable box of the grid, its value is set to blank
                     * (erases the number that was in there) and also updates the board in object board so there
                     * is no conflict with the logic and the interface. This is the way the player can delete
                     * a number at any given moment. Number deletion can also be done via backspace.
                     * Every time the player right-clicks on a non-editable box, nothing happens, otherwise the hash set
                     * that contains the hints (numbers or letters a player can put on current box)
                     * is loaded on a local set via the method hints of object board. If the set
                     * is empty, it means there is no number that can be placed on said box. Message is printed to
                     * the user that they need to readjust their moves. If the set is not empty, the hints on the screen
                     * via a JOptionPane (numbers or letters for wordoku).
                     *
                     * @param mouseEvent represents the mouse click(right or left)
                     */

                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if(boxes[finalI][finalJ].isEditable()) {
                            boxes[finalI][finalJ].setText("");
                            board.delete(finalI, finalJ);
                        }
                        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                            if(!boxes[finalI][finalJ].isEditable()){
                                return;
                            }
                            HashSet<Integer> hints = board.hint(finalI, finalJ);
                            if(hints.isEmpty()){
                                JOptionPane.showMessageDialog(null, bundle.getString("hintMessageEmpty"),"",JOptionPane.WARNING_MESSAGE);
                            } else {
                                StringBuilder showHint = new StringBuilder();
                                if(!board.isWordoku()){
                                    showHint.append(bundle.getString("hintMessageNumber")).append("\n");
                                    for(int number:hints){
                                        showHint.append(number).append(" ");
                                    }
                                }
                                else {
                                    showHint.append(bundle.getString("hintMessageLetters")).append("\n");
                                    for(int number:hints){
                                        showHint.append(board.numberToChar(number,greek)).append(" ");
                                    }
                                }
                                JOptionPane.showMessageDialog(null, showHint);
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                });

                /**Adding key listeners to each and every element of array boxes.*/

                boxes[i][j].addKeyListener(new KeyListener() {
                    /**
                     * This method makes sure that only 1 number or letter is put in every box of the grid.
                     *After a String has been typed in the box, it is checked if the text within the box is larger
                     *or shorter than 1 char. If it's not, only the first char of the string is taken into
                     * consideration (saved in variable c) and the rest is consumed.
                     *
                     * @param keyEvent represents the keyboard button pressed while the user was in the box
                     */

                    @Override
                    public void keyTyped(KeyEvent keyEvent) {

                        boolean max = boxes[finalI][finalJ].getText().length() > 0;
                        if (max) {
                            keyEvent.consume();
                            board.delete(finalI,finalJ);
                        }
                        char c = keyEvent.getKeyChar();

                        if (c==KeyEvent.VK_BACK_SPACE){
                            keyEvent.consume();
                            board.sudokuBoard[finalI][finalJ] = 0;
                        }
                    }

                    /** This method checks if the primer char (first char of keyEvent) is printable or not. If
                     * it's not, the keyEvent is consumed and nothing is placed in the box.
                     * @param keyEvent
                     */
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (isPrintableChar(keyEvent.getKeyChar())) {
                            keyEvent.consume();
                        }
                    }

                      /** Within this method it decided if the move the player wants to do is valid and also the text
                      * of the box is set. If the box that the user wanted to insert a number is not editable, nothing happens.
                      * The main concept is that the primer char of the keyEvent is checked to be valid
                      * and then setting the text of the box (numbers or letters for wordoku), only if the move is valid.
                      * The validity of the move is decided based on the number returned from method check of object
                      * board in a switch-case statement.
                      * If a valid move is placed, object board is also updated in order to keep track of what is in the
                      * grid and then setting the box as non editable, since this mode is competitive and what has been
                      * done cannot be undone. Then pc decides its move, by sending an array of 3 elements which is saved into
                      * local int array coords. First element of array represents row, second column and third
                      * the value of the move (row and column values declare in which box the pc will place its move).
                      * If the value is equal to 0, it means that it was not able to make any valid
                      * moves and therefore the player has won. Otherwise, the value of the third
                      * element of array coords is placed into the box (number or the corresponding
                      * letter of that number if it's a wordoku game). This happens every time the player
                      * makes a move. Then it is checked if the board is full or if the player has no
                      * other available moves. In that event, a message of loss is displayed on the screen.
                      *
                      * @param keyEvent
                     */

                    public void keyReleased(KeyEvent keyEvent) {

                        /** If the box that the user wanted to insert a number is not editable, nothing happens
                         *
                         */
                        if(!boxes[finalI][finalJ].isEditable()){
                            return;
                        }

                        /** Declaring a new int toBePutNumber variable for later use, getting the primer char of the
                         * keyEvent
                         */
                        int toBePutNumber;
                        if (isPrintableChar(keyEvent.getKeyChar())) {
                            /** In case that primer char of keyEvent is printable, its String value is saved
                             * in variable inp for later use.
                             */
                            String inp = boxes[finalI][finalJ].getText();

                            if (!board.isWordoku()) {

                                /** If the game is not wordoku, the int equivalent of inp is saved into temp variable
                                 *
                                 */
                                try {
                                    toBePutNumber = Integer.parseInt(inp);
                                } catch (NumberFormatException exc){
                                    toBePutNumber=board.getSize() +1; //making it go to case -1
                                }
                                boxes[finalI][finalJ].setText("");
                                board.delete(finalI,finalJ);
                                switch (board.check(finalI, finalJ, toBePutNumber)) {
                                    case 1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("rowErrorNumber"),"",JOptionPane.ERROR_MESSAGE);
                                        boxes[finalI][finalJ].setText("");
                                        break;
                                        /** result = 1 means number is already in he same row, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 2: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("columnErrorNumber"),"",JOptionPane.ERROR_MESSAGE);
                                        boxes[finalI][finalJ].setText("");
                                        break;
                                        /** result = 2 means number is already in the same column, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 3: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("blockErrorNumber"), "", JOptionPane.ERROR_MESSAGE);
                                        boxes[finalI][finalJ].setText("");
                                        break;
                                        /** result = 3 means number is already in the same sub-grid (3x3), corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case -1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("correctInputMessageNumbers") + board.getSize(), "", JOptionPane.WARNING_MESSAGE);
                                        boxes[finalI][finalJ].setText("");
                                        break;
                                        /** result = -1 means number is is out of bounds, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    default: {
                                        /** Default operation in case the char is printable is to set the box text as
                                         * the primer char of keyEvent
                                         */
                                        boxes[finalI][finalJ].setText(inp);
                                        boxes[finalI][finalJ].setEditable(false);

                                            /**PC makes move as described*/

                                            int[] coords = board.pcMove();
                                            if (coords[2] == 0) {
                                                JOptionPane.showMessageDialog(null, bundle.getString("duidokuWinMessage"));
                                                player.duidokuWon();
                                                player.changePlayerFile();
                                                dispose();
                                                return;
                                            } else {
                                                boxes[coords[0]][coords[1]].setText(String.valueOf(coords[2]));
                                                boxes[coords[0]][coords[1]].setEditable(false);
                                            }
                                            if (board.finished()) {
                                                JOptionPane.showMessageDialog(null, bundle.getString("duidokuLoseMessage"),"",JOptionPane.ERROR_MESSAGE);
                                                player.duidokuLost();
                                                player.changePlayerFile();
                                                dispose();
                                            }
                                        }
                                    }
                            }
                            else {
                                /** Else here means it's a wordoku game
                                 *
                                 */
                                //try {
                                    toBePutNumber = board.charToNumber(inp.charAt(0), greek);
                              //  } catch (){

                              //  }
                                boxes[finalI][finalJ].setText("");
                                switch (board.check(finalI, finalJ, toBePutNumber)) {
                                    case 1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("rowErrorLetter"),"",JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 1 means letter is already in he same row, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 2: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("columnErrorLetter"),"",JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 2 means letter is already in the same column, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 3: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("blockErrorLetter"),"",JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 3 means letter is already in the same sub-grid (3x3), corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case -1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("correctInputMessageLetters") + board.numberToChar(board.getSize(), greek),"",JOptionPane.WARNING_MESSAGE);
                                        break;
                                        /** result = -1 means letter is is out of bounds, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    default: {
                                        /** Default operation in case the char is printable is to set the box text as
                                         *the primer char of keyEvent
                                         */
                                        boxes[finalI][finalJ].setText(inp.toUpperCase());
                                        boxes[finalI][finalJ].setEditable(false);

                                        /** PC makes move as described
                                         *
                                         */
                                        int[] coords = board.pcMove();
                                        if (coords[2] == 0) {
                                            JOptionPane.showMessageDialog(null, bundle.getString("duidokuWinMessage"));
                                            dispose();
                                            player.duidokuWon();
                                            player.changePlayerFile();
                                            return;
                                        } else {
                                            boxes[coords[0]][coords[1]].setText(Character.toString(board.numberToChar(coords[2],greek)));
                                            boxes[coords[0]][coords[1]].setEditable(false);
                                        }
                                        if (board.finished()) {
                                            JOptionPane.showMessageDialog(null, bundle.getString("duidokuLoseMessage"),"",JOptionPane.ERROR_MESSAGE);
                                            player.duidokuLost();
                                            player.changePlayerFile();
                                            dispose();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                /** Setting text size and font, centering the text and adding the box into the GridLayout
                 *
                 */
                boxes[i][j].setFont(font);
                boxes[i][j].setHorizontalAlignment(JTextField.CENTER);
                add(boxes[i][j]);
            }
        }
    }

    /** Checks if a character is printable by using some built-in java methods.
     *
     * @param c
     * @return
     */

    private boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }
}

