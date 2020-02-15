package com.example.assignment2;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class TicTacToeGame {
    private char[] grid;
    private boolean isXTurn;
    private boolean userStarts;
    private HashMap<String, Integer> wins;
    private boolean gameEnded;

    private static final String TAG = "TicTacToeGame";

    public TicTacToeGame() {
        grid = new char[9];
        isXTurn = true;
        userStarts = true;

        wins = new HashMap<>();
        wins.put("X", 0);
        wins.put("O", 0);

        gameEnded = false;
    }

    public TicTacToeGame(char[] grid, boolean isXTurn, boolean userStarts, int xWins, int oWins, boolean gameEnded) {
        this.grid = grid;
        this.isXTurn = isXTurn;
        this.userStarts = userStarts;
        wins = new HashMap<>();
        wins.put("X", xWins);
        wins.put("O", oWins);
        this.gameEnded = gameEnded;
    }

    public boolean getIsXTurn() { return isXTurn; }
    public char[] getGrid() { return grid; }
    public int getOWins() { return wins.get("O"); }
    public int getXWins() { return wins.get("X"); }
    public boolean getUserStarts() { return userStarts; }
    public boolean getGameEnded() { return gameEnded; }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean userPlay(int square) {
        return play(square);
    }

    public void cpuPlay() {
        play(getBestMove());
    }

    private int getBestMove() {
        int move = -1, bestScore = -100;

        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == '\u0000') {
                grid[i] = isXTurn ? 'X':'O';
                int score = minimax(grid, false, 0);

                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
                grid[i] = '\u0000';
            }
        }

        return move;
    }

    private int minimax(char[] exampleGrid, boolean isMax, int depth) {
        int bestScore = isMax ? -100 : 100;

        int[] winningLine = checkWinner(exampleGrid);

        if (winningLine != null) {
            if (Arrays.equals(winningLine, new int[]{0,0,0})) {
                return score('D', depth);
            } else {
                return score(exampleGrid[winningLine[0]], depth);
            }
        } else {
            for (int i = 0; i < exampleGrid.length; i++) {
                if (exampleGrid[i] == '\u0000') {
                    if (isMax) {
                        exampleGrid[i] = userStarts ? 'O':'X';

                        bestScore = Math.max(bestScore, minimax(exampleGrid, false, depth+1));
                    } else {
                        exampleGrid[i] = userStarts ? 'X':'O';

                        bestScore = Math.min(bestScore, minimax(exampleGrid, true, depth+1));
                    }

                    exampleGrid[i] = '\u0000';
                }
            }
            return bestScore;
        }
    }

    private int score(char winningSymbol, int depth) {
        if (userStarts) {
            switch (winningSymbol) {
                case 'X':
                    return depth-10;
                case 'O':
                    return 10-depth;
                default:
                    return 0;
            }
        } else {
            switch (winningSymbol) {
                case 'X':
                    return 10-depth;
                case 'O':
                    return depth-10;
                default:
                    return 0;
            }
        }
    }

    private boolean play(int square) {
        if (grid[square] != '\u0000') {
            Log.i(TAG, "Filled");
            return false;
        }
        grid[square] = isXTurn ? 'X':'O';
        isXTurn = !isXTurn;
        Log.i(TAG, printBoard());
        return true;
    }

    private String printBoard() {
        String result = "Board\n\n";
        for (int i = 0; i < grid.length; i++) {
            char item = grid[i] == '\u0000' ? ' ' : grid[i];
            if ((i+1) % 3 == 0) {
                result += item + "\n";
            } else {
                result += item + "|";
            }
        }
        return result;
    }

    public void addWinner(char symbol) {
        wins.replace(String.valueOf(symbol), wins.get(String.valueOf(symbol)) + 1);
    }

    public int[] checkWinner() {
        return checkWinner(grid);
    }

    public int[] checkWinner(char[] exampleGrid) {
        int[][] lines = {
                {0,1,2},
                {3,4,5},
                {6,7,8},
                {0,3,6},
                {1,4,7},
                {2,5,8},
                {0,4,8},
                {2,4,6}
        };

        for (int[] line : lines) {
            int a = line[0], b = line[1], c = line[2];
            if (exampleGrid[a] != '\u0000' && exampleGrid[a] == exampleGrid[b] && exampleGrid[a] == exampleGrid[c]) {

                return line; // winner
            }
        }

        boolean filled = true;
        for (char square : exampleGrid) {
            if (square == '\u0000') {
                filled = false;
            }
        }

        if (filled) {
            return new int[]{0,0,0}; //draw
        }

        return null; //not final
    }

    public void reset() {
        isXTurn = true;
        grid = new char[9];
        gameEnded = false;

        userStarts = !userStarts;

        if (!userStarts) {
            cpuPlay();
        }
    }
}
