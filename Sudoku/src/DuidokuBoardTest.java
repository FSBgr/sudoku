import sudoku.DuidokuBoard;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DuidokuBoardTest {

    @Test
    void pcMove() {
        DuidokuBoard board = new DuidokuBoard(4, false);

        int[] local = board.pcMove();
        boolean expected;

        expected = ((local[0]<=9) && (local[0]>=0));
        Assert.assertTrue(expected);

        expected = ((local[1]<=9) && (local[1]>=0));
        Assert.assertTrue(expected);

        expected = ((local[2]<=4) && (local[2]>=0));
        Assert.assertTrue(expected);

    }

    @Test
    void finished() {
        DuidokuBoard board = new DuidokuBoard(9, false);

        Assert.assertFalse(board.finished()); // Default loaded board is not full.

    }
}