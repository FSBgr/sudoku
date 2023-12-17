package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.ResourceBundle;

/** @author Christos Christidis
 *
 * This class represents the window/frame where the classic version of the game is played. It consists of a nxn grid
 * of JTextfields. All messages/texts/strings that are printed to the screen(user) are in the language of the user's
 * system (greek or english).
 *
 */
public class ClassicMode extends JFrame {
    /** Constructor of the Frame. The whole body of the code was included within the constructor, since this is a
     * GUI class. Because of its nature, it's difficult to describe what happens in this block of javadoc, since plenty
     * of internal classes and methods are created and/or overrode. For that matter, each internal class and function
     * has its own javadoc.
     *
     * @param board
     * @param player
     * @param greek
     * @param bundle
     */
    public ClassicMode(ClassicBoard board, Player player, boolean greek, ResourceBundle bundle) {


        /** Setting the title of the frame based on the language (greek/english) via the resource bundle, setting
         * default size and close operation. Adding a new grid layout based on the size of the board with one extra row
         * in order to include a check button later on. Starting new game from object board. Setting the font that will
         * be used in the textFields.
         */
        setTitle(bundle.getString("classicModeTitle"));
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        board.startNewGameForPlayer(player);
        setLayout(new GridLayout(board.getSize() + 1, board.getSize()));
        Font font = new Font("Times New Roman", Font.BOLD, 20);

        /** Button that checks the progress of the game
         */
        JButton checkProgress = new JButton();
        checkProgress.addActionListener(new ActionListener() {

            /**Action listener of button checkProgress. When clicked, it checks if the board is full and then shows the finish message to
             * the player (via a JOptionPane while updating the stats of the player. In the event that the board is not full,
             * it prints the corresponding message
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board.finished()) {
                    JOptionPane.showMessageDialog(null, bundle.getString("checkMessageFinished"));
                    player.addClassicSudoku(board.getCode());
                    player.changePlayerFile();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, bundle.getString("checkMessageNotFinished"), "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        /** Creating a 2 dimensional array of JTextField (based on the size of the board)
         * that represents the sudoku grid (boxes) in which the player will be able to place their moves.
         */
        JTextField[][] boxes = new JTextField[board.getSize()][board.getSize()];
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {

                /**Double for-loop for the permeation of each element of array boxes. Within this loop, all mouse and
                 *key listeners will be added to each text field of the array.
                 *
                 * Reserving memory space for each element of array boxes and setting its size to 50x25
                 *
                 */
                boxes[i][j] = new JTextField(1);
                boxes[i][j].setSize(50, 25);

                int finalI = i;
                int finalJ = j;

                /** Declaring two new int variables with the value of i,j in each cycle of the loop that will be used
                 * later on in each listener.
                 */
                if(board.sudokuBoard[i][j]!=0) {
                    if (!board.isWordoku()) {
                        boxes[i][j].setText(String.valueOf(board.sudokuBoard[i][j]));
                    } else {
                        boxes[i][j].setText(Character.toString(board.numberToChar(board.sudokuBoard[i][j], greek)));
                    }
                    boxes[i][j].setEditable(false);
                }

                /** Checking if the game is played with numbers or letters and then loading the initial values of the
                 * sudoku puzzle on the interface and making those text fields not editable so the user will not be able
                 * to change their value.
                 *
                 */

                boxes[i][j].addMouseListener(new MouseListener() {
                    /**
                     * Every time the player left-clicks on an editable box of the grid, its value is set to blank
                     *(erases the number that was in there) and also updates the board in object board so there
                     *is no conflict with the logic and the interface. This is the way the player can delete
                     *a number at any given moment. Number deletion can also be done via backspace.
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
                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            if (boxes[finalI][finalJ].isEditable()) {
                                boxes[finalI][finalJ].setText("");
                                board.delete(finalI, finalJ);
                            }
                        }

                        if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                            if (!boxes[finalI][finalJ].isEditable()) {
                                return;
                            }
                            HashSet<Integer> hints = board.hint(finalI, finalJ);
                            if (hints.isEmpty()) {
                                JOptionPane.showMessageDialog(null, bundle.getString("hintMessageEmpty"), "", JOptionPane.WARNING_MESSAGE);
                            }
                            else {
                                StringBuilder showHint = new StringBuilder();
                                if (!board.isWordoku()) {
                                    showHint.append(bundle.getString("hintMessageNumber")).append("\n");
                                    for (int number : hints) {
                                        showHint.append(number).append(" ");
                                    }
                                } else {
                                    showHint.append(bundle.getString("hintMessageLetters")).append("\n");
                                    for (int number : hints) {
                                        showHint.append(board.numberToChar(number, greek)).append(" ");
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


                /**  Adding key listeners to each and every element of array boxes
                 */
                boxes[i][j].addKeyListener(new KeyListener() {
                    /**
                     * This method makes sure that only 1 number or letter is put in every box of the grid.
                     * After a String has been typed in the box, it is checked if the text within the box is larger
                     * or shorter than 1 char. If it's not, only the first char of the string is taken into
                     * consideration (saved in variable c) and the rest is consumed.
                     * @param keyEvent represents the keyboard button pressed while the user was in the box
                     */

                    @Override
                    public void keyTyped(KeyEvent keyEvent) {

                        if (boxes[finalI][finalJ].getText().length() > 0) {
                            keyEvent.consume();
                            board.delete(finalI,finalJ);
                        }
                        char c = keyEvent.getKeyChar();
                        if (c == '\b')
                            keyEvent.consume();

                        if (c==KeyEvent.VK_BACK_SPACE){
                            keyEvent.consume();
                            board.sudokuBoard[finalI][finalJ] =0;
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
                     * grid.
                     * @param keyEvent
                     */


                    @Override
                    public void keyReleased(KeyEvent keyEvent) {
                        if (!boxes[finalI][finalJ].isEditable()) {
                            return;
                        }

                        /** Declaring a new int toBePutNumber variable for later use, getting the primer char of the
                         * keyEvent
                         */

                        int toBePutNumber;
                        if (isPrintableChar(keyEvent.getKeyChar())) {
                            String inp = String.valueOf(keyEvent.getKeyChar());
                            /** In case that primer char of keyEvent is printable, its String value is saved
                             * in variable inp for later use.
                             */

                            if (!board.isWordoku()) {
                                /** If the game is not wordoku, the int equivalent of inp is saved into temp variable
                                 * Getting the number-code of this move via method check of object board in order
                                 * to verify the validity of this move.
                                 */
                                try {
                                    toBePutNumber = Integer.parseInt(inp);

                                } catch (NumberFormatException exc){
                                    toBePutNumber=board.getSize() +1; //something bigger than sizeOfBoard so the check method will return -1
                                }
                                boxes[finalI][finalJ].setText("");
                                board.delete(finalI,finalJ);
                                switch (board.check(finalI, finalJ, toBePutNumber)) {
                                    case 1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("rowErrorNumber"), "", JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 1 means number is already in he same row, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 2: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("columnErrorNumber"), "", JOptionPane.ERROR_MESSAGE);
                                        break;

                                        /** result = 2 means number is already in the same column, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 3: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("blockErrorNumber"), "", JOptionPane.ERROR_MESSAGE);
                                        break;

                                        /** result = 3 means number is already in the same sub-grid (3x3), corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case -1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("correctInputMessageNumbers") + board.getSize(), "", JOptionPane.WARNING_MESSAGE);
                                        break;

                                        /** result = -1 means number is is out of bounds, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    default: {
                                        boxes[finalI][finalJ].setText(inp);
                                        /** Default operation is to set the box text same as the primer char of the
                                         * keyEvent (provided that char is printable)
                                         */
                                    }
                                }
                            }
                            else {
                                /** Else here means it's a wordoku game
                                 * Getting the equivalent integer of the char via charToNumber method of object
                                 * board. Getting the number-code of this move via method check of object board in order
                                 * to verify the validity of this move.
                                 */

                                toBePutNumber = board.charToNumber(inp.charAt(0), greek);
                                int result = board.check(finalI, finalJ, toBePutNumber);
                                boxes[finalI][finalJ].setText("");
                                switch (result) {
                                    case 1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("rowErrorLetter"), "", JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 1 means letter is already in he same row, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         * */
                                    }
                                    case 2: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("columnErrorLetter"), "", JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 2 means letter is already in the same column, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case 3: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("blockErrorLetter"), "", JOptionPane.ERROR_MESSAGE);
                                        break;
                                        /** result = 3 means letter is already in the same sub-grid (3x3), corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    case -1: {
                                        JOptionPane.showMessageDialog(boxes[finalI][finalJ], bundle.getString("correctInputMessageLetters") + board.numberToChar(board.getSize(), greek), "", JOptionPane.WARNING_MESSAGE);
                                        break;
                                        /** result = -1 means letter is is out of bounds, corresponding message is
                                         * printed on the screen bia JOptionPane, switch-case breaks.
                                         */
                                    }
                                    default: {
                                        boxes[finalI][finalJ].setText(inp.toUpperCase());
                                        /** Default operation is to set the box text same as the primer char of the
                                         * keyEvent (provided that char is printable)
                                         */
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

        /** Setting the background color of checkProgress button as green, setting its text as the check mark/symbol
         * via its unicode value and adding it into the GridLayout
         */
        checkProgress.setBackground(Color.GREEN);
        checkProgress.setText("\u2713");
        add(checkProgress);

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