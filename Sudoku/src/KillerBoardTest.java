import sudoku.KillerBoard;
import sudoku.Player;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.awt.*;

class KillerBoardTest {

    @Test
    void howManyColors() {
        KillerBoard board = new KillerBoard(9,10, false);
        Assert.assertEquals(12, board.howManyColors() );

        board.addColor(10,11,12);
        board.addColor(19,21,37);
        Assert.assertEquals(14, board.howManyColors());

        board.deleteColor(10, 11, 12);
        Assert.assertEquals(13, board.howManyColors());


    }

    @Test
    void numberToColor() {
        KillerBoard board = new KillerBoard(9, 10,false);
        Assert.assertEquals(Color.RED, board.numberToColor(0));
        final Color TO_BE_TESTED = new Color(12,13,21);

        board.addColor(12,13,21);

        Assert.assertEquals(TO_BE_TESTED, board.numberToColor(12));

    }

    @Test
    void check() {
        KillerBoard board = new KillerBoard(9, 10,true);
        board.startNewGameForPlayer(new Player("unknown"));
        Assert.assertTrue(board.check(0,0,1)<=4 && board.check(0,0,1)>=-1);
        Assert.assertTrue(board.check(7,2,4)<=4 && board.check(7,2,4)>=-1);
        Assert.assertTrue(board.check(11,18,20)<=4 && board.check(11,18,20)>=-1);
    }

    @Test
    void getColorSum() {
        KillerBoard board = new KillerBoard(9,10,false, 1,"Killer Sudoku Puzzles.txt");
        Assert.assertEquals(15, board.getColorSum(2));
        Assert.assertEquals(16,board.getColorSum(5));
    }

}