import org.junit.Assert;
import sudoku.Player;
import org.junit.jupiter.api.Test;
import java.util.HashSet;

class PlayerTest {
    public PlayerTest(){

    }

    @Test
    void getName() {
        Player nameTestEnglish= new Player("Alex");
        Assert.assertEquals("Alex",nameTestEnglish.getName());
        Player nameTestGreek= new Player("Αλέξανδρος");
        Assert.assertEquals("Αλέξανδρος",nameTestGreek.getName());
        Player nameTestEmpty= new Player();
        Assert.assertEquals("unknown",nameTestEmpty.getName());
    }

    @Test
    void duidokuWon() {
        Player threeWins= new Player();
        threeWins.duidokuWon();
        threeWins.duidokuWon();
        threeWins.duidokuWon();
        int expected= 3;
        Assert.assertEquals(expected,threeWins.getDuidokuWins());
        Player noWins= new Player();
        expected=0;
        Assert.assertEquals(expected,noWins.getDuidokuWins());

    }

    @Test
    void duidokuLost() {
        Player twoLoses= new Player();
        twoLoses.duidokuLost();
        twoLoses.duidokuLost();
        int expected= 2;
        Assert.assertEquals(expected,twoLoses.getDuidokuLoses());
        Player noLoses= new Player();
        expected=0;
        Assert.assertEquals(expected,noLoses.getDuidokuLoses());
    }

    @Test
    void getClassicSudokuGamesWon() {
        Player fourClassics= new Player("Alex");
        fourClassics.addClassicSudoku(1);
        fourClassics.addClassicSudoku(5);
        fourClassics.addClassicSudoku(7);
        fourClassics.addClassicSudoku(8);
        HashSet<Integer> expectedSet= new HashSet<>();
        expectedSet.add(1);
        expectedSet.add(5);
        expectedSet.add(7);
        expectedSet.add(8);
        Assert.assertEquals(expectedSet,fourClassics.getClassicSudokuGamesWon());
        Player emptySet= new Player("Chris");
        expectedSet= new HashSet<>();
        Assert.assertEquals(expectedSet,emptySet.getClassicSudokuGamesWon());
    }

    @Test
    void getKillerSudokuGamesWon() {
        Player fiveKillers= new Player();
        fiveKillers.addKillerSudoku(1);
        fiveKillers.addKillerSudoku(3);
        fiveKillers.addKillerSudoku(4);
        fiveKillers.addKillerSudoku(7);
        fiveKillers.addKillerSudoku(9);
        HashSet<Integer> expectedSet= new HashSet<>();
        expectedSet.add(1);
        expectedSet.add(3);
        expectedSet.add(4);
        expectedSet.add(7);
        expectedSet.add(9);
        Assert.assertEquals(expectedSet,fiveKillers.getKillerSudokuGamesWon());
        Player emptySet= new Player();
        expectedSet= new HashSet<>();
        Assert.assertEquals(expectedSet,emptySet.getKillerSudokuGamesWon());
    }

    @Test
    void testToString() {
        Player player= new Player("Alex");
        player.duidokuLost();
        player.duidokuLost();
        player.duidokuWon();
        player.addKillerSudoku(1);
        player.addKillerSudoku(3);
        player.addClassicSudoku(2);
        String expected= player.getName()  + "\n Classics won: " + player.getClassicSudokuGamesWon().size() +
                "\n Killers won: " + player.getKillerSudokuGamesWon().size() +
                "\n Duidoku wins: " + player.getDuidokuWins() + "\n Duidoku loses: " + player.getDuidokuLoses();
        Assert.assertEquals(expected,player.toString(false));
        expected= player.getName() + "\n Κλασσικά που νικήσατε: " + player.getClassicSudokuGamesWon().size() +
                "\n Killers που νικήσατε: " + player.getKillerSudokuGamesWon().size() +
                "\n Νίκες στο Duidoku: " + player.getDuidokuWins() + "\n Ήττες στο Duidoku: " + player.getDuidokuLoses();
        Assert.assertEquals(expected,player.toString(true));

    }

    @Test
    void loginPlayer() {
        Player nonExistingPlayer= new Player();
        nonExistingPlayer.loginPlayer("Alex","testPlayers.txt");
        Player expected= new Player("Alex");
        Assert.assertEquals(expected.toString(false),nonExistingPlayer.toString(false));

        Player existingPlayer= new Player();
        existingPlayer.loginPlayer("Chris","testPlayers.txt");
        expected=new Player("Chris");
        expected.duidokuWon();
        expected.duidokuWon();
        expected.duidokuLost();
        expected.addClassicSudoku(1);
        expected.addClassicSudoku(4);
        expected.addClassicSudoku(6);
        expected.addKillerSudoku(4);
        expected.addKillerSudoku(7);
        Assert.assertEquals(expected.toString(false),existingPlayer.toString(false));
    }
}