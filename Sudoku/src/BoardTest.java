import sudoku.Board;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class BoardTest {

    @Test
    void isWordoku() {
        Board wordokuBoard= new Board(9,true);
        Assert.assertTrue(wordokuBoard.isWordoku());
        Board notWordokuBoard= new Board(9,false);
        Assert.assertFalse(notWordokuBoard.isWordoku());
        Board defaultNotWordoku= new Board();
        Assert.assertFalse(defaultNotWordoku.isWordoku());
    }

    @Test
    void getSize() {
        Board classicSized= new Board(9,false);
        int expected=9;
        Assert.assertEquals(expected,classicSized.getSize());
        Board defaultSize= new Board();
        Assert.assertEquals(expected,defaultSize.getSize());
        Board biggerBoard= new Board(25,false);
        expected=25;
        Assert.assertEquals(expected,biggerBoard.getSize());
    }

    @Test
    void numberToChar() {
        Board classicSizedBoard= new Board();
        char expected='A'; //for english letters
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(1,false));
        expected='D';
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(4,false));
        expected='-';
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(10,false));
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(0,false));
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(-4,false));

        Board biggerSize= new Board(16,false);
        expected='K';
        Assert.assertEquals(expected,biggerSize.numberToChar(11,false));
        expected='Α';  //for greek letters
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(1,true));
        expected='Γ';
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(3,true));
        expected='-';
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(10,true));
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(0,true));
        Assert.assertEquals(expected,classicSizedBoard.numberToChar(-4,true));
        expected='Λ';
        Assert.assertEquals(expected,biggerSize.numberToChar(11,true));
    }


    @Test
    void charToNumber() {
        Board classicSizedBoard= new Board(); //for english letters
        int expected= 2;
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('b',false));
        expected=5;
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('E',false));
        expected=-1;
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('9',false));
        Assert.assertEquals(expected,classicSizedBoard.charToNumber(' ',false));
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('P',false));
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('p',false));
        Board biggerSize= new Board(16,false);
        expected=12;
        Assert.assertEquals(expected,biggerSize.charToNumber('L',false));
        expected=3; //for greek letters
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('γ',true));
        expected=4;
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('Δ',true));
        expected=-1;
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('Π',true));
        Assert.assertEquals(expected,classicSizedBoard.charToNumber('ψ',true));
        Assert.assertEquals(expected,classicSizedBoard.charToNumber(' ',true));



    }

    @Test
    void check() {
        Board classicBoard= new Board();
        int expected=0;
        Assert.assertEquals(expected,classicBoard.check(0,0,1));
        expected=1;
        Assert.assertEquals(expected,classicBoard.check(0,1,1));
        expected=2;
        Assert.assertEquals(expected,classicBoard.check(1,0,1));
        expected=3;
        Assert.assertEquals(expected,classicBoard.check(1,1,1));
        expected=-1;
        Assert.assertEquals(expected,classicBoard.check(0,1,18));
        Assert.assertEquals(expected,classicBoard.check(0,10,4));
        Assert.assertEquals(expected,classicBoard.check(-2,1,6));

    }


    @Test
    void hint() {
        Board board= new Board();
        board.check(0,0,1);
        board.check(0,1,5);
        board.check(6,0,9);
        board.check(1,8,7);
        board.check(1,6,8);
        board.check(1,2,3);
        HashSet<Integer> expected= new HashSet<>();
        expected.add(2);
        expected.add(4);
        expected.add(6);
        Assert.assertEquals(expected,board.hint(1,0));
    }
}