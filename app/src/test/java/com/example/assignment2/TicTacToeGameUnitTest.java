package com.example.assignment2;

import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacToeGameUnitTest {
    @Test
    public void playGame() {
        TicTacToeGame game = new TicTacToeGame();
        game.userPlay(5);
        game.cpuPlay();
        game.userPlay(3);
        game.cpuPlay();
        game.userPlay(4);
        game.cpuPlay();
        game.userPlay(2);
        game.cpuPlay();
        game.userPlay(9);
        assertEquals(new int[]{0,0,0}, game.checkWinner());
    }
}
