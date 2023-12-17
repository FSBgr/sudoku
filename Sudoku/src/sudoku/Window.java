package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/** @author Christos Christidis, Alexandros Stergiopoulos
 * This class represents the main menu Sudoku.Window of the sudoku game. It includes all of the buttons required to do
 * all needed functions of the game (Choose game mode, login, check stats, check instructions and quit).
 */


public class Window extends JFrame {

    private Player player;
    private boolean greek;

    public Window(){
        /** Initializing player as "unknown", in case the user does not login before playing
         * Setting the locale and the value greek from it
         */
        ResourceBundle bundle;
        Locale locale= Locale.getDefault();
        //Locale locale= new Locale("el","GR");
        /**If language of system is not english us or greek, the locale is set in the english version
         */
        if(!locale.equals(new Locale("el","GR")) && !locale.equals(new Locale("en","US"))){
            locale= new Locale("en","US");
        }
        bundle=ResourceBundle.getBundle("Internationalization",locale);
        greek=locale.equals(new Locale("el","GR"));
        player = new Player("unknown");

        /** Frame contains 7 JButtons: 3 for each game mode, 1 for login, 1 for stats check, 1 for instructions and
         * 1 for game exit. Initializing frame properties (size, title), setting close operation, initializing
         * layout based, adding Sudoku.image and welcome message and initializing the layout where the rest of the components
         * will be added.
         */

        setTitle(bundle.getString("title"));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 340);
        setResizable(false);
        setLocationRelativeTo(null);
        JLabel welcomeMessage = new JLabel(bundle.getString("menuMessage"));
        welcomeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel picLabel= new JLabel();
        ImageIcon pic = new ImageIcon();
        String imgName = "image/sudoku.png";
        URL imageURL = getClass().getResource(imgName);
        if (imageURL != null) {
            pic = new ImageIcon(imageURL);
        }
        setIconImage(pic.getImage());
        picLabel.setIcon(pic);


        JButton mode1 = new JButton(bundle.getString("button1"));
        JButton mode2 = new JButton(bundle.getString("button2"));
        JButton mode3 = new JButton(bundle.getString("button3"));
        JButton login = new JButton(bundle.getString("button4"));
        JButton showStats = new JButton(bundle.getString("button5"));
        JButton howToPlay= new JButton(bundle.getString("button6"));
        JButton quit= new JButton(bundle.getString("button7"));

        /** Adding action listeners to the buttons that represent the 3 game modes of sudoku. All 3 work the same way.
         * At first it is asked by the user if they want to play wordoku via a JOptionPane. Then a window of the
         * corresponding game mode is created. Sudoku.Player is "unknown" unless user has previously logged in.
         */
        mode1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                /** JOptionPane:
                 * @returns 0 (Wordoku)
                 * @returns 1 (Not wordoku)
                 * @returns 2 or -1 (Cancellation or pressing the X button)
                 */
                int a=JOptionPane.showConfirmDialog(null,bundle.getString("wordokuOptionMessage"));
                if(a==2 || a==-1){
                    return;
                }
                ClassicMode classicMode = new ClassicMode(new ClassicBoard(a == 0), player,greek,bundle);
                classicMode.setVisible(true);


            }
        });

        mode2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int a=JOptionPane.showConfirmDialog(null,bundle.getString("wordokuOptionMessage"));
                if(a==2  || a==-1){
                    return;
                }
                KillerMode killerSudoku = new KillerMode(new KillerBoard(a==0), player,greek,bundle);
                killerSudoku.setVisible(true);
            }
        });

        mode3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int a=JOptionPane.showConfirmDialog(null,bundle.getString("wordokuOptionMessage"));
                if(a==2  || a==-1){
                    return;
                }
                DuidokuMode duidoku = new DuidokuMode(new DuidokuBoard(a==0), player,greek,bundle);
                duidoku.setVisible(true);
            }
        });

        /** Adding action listener for login button.
         * Message is shown to user to enter their username via OptionPane. In the event that they close the pane
         * without logging in, object Sudoku.Player status remains "unknown". If user inserts blank name, they are asked to
         * insert an actual String in order to login. Else, object Sudoku.Player's status is changed to the name inserted by
         * the user.
         */
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String response = JOptionPane.showInputDialog(bundle.getString("loginMessage"));
                if(response==null){
                    return;
                }
                if ( response.equals("")) {
                    JOptionPane.showMessageDialog(null, bundle.getString("loginErrorMessage"),"",JOptionPane.WARNING_MESSAGE);
                } else {
                    player.loginPlayer(response);

                    JOptionPane.showMessageDialog(null, bundle.getString("loginWelcomeMessage")+ " " + player.getName());
                }
            }
        });

        /** Action listener for button show stats button. If player status is "unknown", meaning haven't logged in, an error message appears. Else
         * the player's stats are printed on the screen.
         */
        showStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.getName().equals("unknown")){
                    JOptionPane.showMessageDialog(null, bundle.getString("showStatsError"),"",JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, player.toString(greek));
                }            }
        });

        /** Action listener for button How to Play
         * a big message with rules and instructions is shown
         */
        howToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, bundle.getString("instructions"));

            }
        });

        /** Action listener for Quit button. If clicked and confirmed, the frame closes.
         */
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a=JOptionPane.showConfirmDialog(null,bundle.getString("quitMessage"));
                if(a==0){
                    dispose();
                }
            }
        });

        /** Adding all of frames' components into the layout and making some spaces between them
         *
         */
        add(welcomeMessage,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(picLabel,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(mode1,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(mode2,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(mode3,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(login,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(showStats,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(howToPlay,Container.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(quit,Container.CENTER_ALIGNMENT);
        setVisible(true);
    }
}

