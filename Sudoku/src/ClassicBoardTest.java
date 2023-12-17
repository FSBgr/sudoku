import sudoku.ClassicBoard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import sudoku.Player;

class ClassicBoardTest {

    @Test
    void getNumberOfPuzzles() {
        ClassicBoard board = new ClassicBoard(9, false);

        Assert.assertEquals(10, board.getNumberOfPuzzles());

        board.setNumberOfPuzzles(11);

        Assert.assertEquals(11, board.getNumberOfPuzzles());

    }

    @Test
    void getCode() {
        ClassicBoard board = new ClassicBoard(9, true);

        Assert.assertTrue(board.getCode()<=10);
    }

    @Test
    void startNewGameForPlayer(){
        ClassicBoard board= new ClassicBoard(9,false);
        Player player= new Player();
        board.startNewGameForPlayer(player);
        Assert.assertTrue(board.getCode()>0 && board.getCode()<11);

        player.addClassicSudoku(1);
        player.addClassicSudoku(2);
        player.addClassicSudoku(4);
        player.addClassicSudoku(5);
        player.addClassicSudoku(6);
        player.addClassicSudoku(7);
        player.addClassicSudoku(8);
        player.addClassicSudoku(10);
        board.startNewGameForPlayer(player);
        Assert.assertTrue(board.getCode()==3 || board.getCode()==9);


    }
}