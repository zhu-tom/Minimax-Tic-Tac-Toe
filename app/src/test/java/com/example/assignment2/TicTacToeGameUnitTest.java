package com.example.assignment2;

import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacToeGameUnitTest {
    @Test
    public void playGameDraw() {
        TicTacToeGame game = new TicTacToeGame();
        game.userPlay(4);
        game.cpuPlay();
        game.userPlay(2);
        game.cpuPlay();
        game.userPlay(3);
        game.cpuPlay();
        game.userPlay(1);
        game.cpuPlay();
        game.userPlay(8);
        assertArrayEquals(new int[]{0,0,0}, game.getWinningLine());
    }

    @Test
    public void playGameCpuWin() {
        TicTacToeGame game = new TicTacToeGame();
        game.userPlay(3);
        game.cpuPlay();
        game.userPlay(5);
        game.cpuPlay();
        game.userPlay(8);
        game.cpuPlay();
        game.userPlay(6);
        game.cpuPlay();
        assertArrayEquals(new int[]{0,1,2},game.getWinningLine());
    }
}
