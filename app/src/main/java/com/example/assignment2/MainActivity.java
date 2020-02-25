package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BOARD_KEY = "Board";
    private static final String IS_X_TURN_KEY = "IsXTurn";
    private static final String USER_STARTS_KEY = "UserStarts";
    private static final String X_WINS_KEY = "XWins";
    private static final String O_WINS_KEY = "OWins";
    private static final String WIN_LINE_KEY = "WinLine";
    private static final String GAME_ENDED_KEY = "GameEnded";

    private Button[] mButtonGrid;
    private TicTacToeGame mGame;
    private Button mReset;
    private int[] mWinLine;
    private Handler mHandler;
    private boolean mWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaiting = false;

        mHandler = new Handler();

        mButtonGrid = new Button[]{
                findViewById(R.id.square11), findViewById(R.id.square12), findViewById(R.id.square13),
                findViewById(R.id.square21), findViewById(R.id.square22), findViewById(R.id.square23),
                findViewById(R.id.square31), findViewById(R.id.square32), findViewById(R.id.square33)
        };

        if (savedInstanceState != null) {
            char[] board = savedInstanceState.getCharArray(BOARD_KEY);
            boolean isXTurn = savedInstanceState.getBoolean(IS_X_TURN_KEY);
            boolean userStarts = savedInstanceState.getBoolean(USER_STARTS_KEY);
            int xWins = savedInstanceState.getInt(X_WINS_KEY);
            int oWins = savedInstanceState.getInt(O_WINS_KEY);
            boolean gameEnded = savedInstanceState.getBoolean(GAME_ENDED_KEY);
            mGame = new TicTacToeGame(board, isXTurn, userStarts, xWins, oWins, gameEnded);

            mWinLine = savedInstanceState.getIntArray(WIN_LINE_KEY);
            if (mGame.getGameEnded()) {
                for (int index:mWinLine) {
                    mButtonGrid[index].setBackgroundColor(getResources().getColor(R.color.colorWinner, getTheme()));
                }
            }

            updateBoard();
        } else {
            mGame = new TicTacToeGame();
        }

        for (int i = 0; i < mButtonGrid.length; i++) {
            final int index = i;
            mButtonGrid[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mWaiting) {
                        if (!mGame.getGameEnded()) {
                            if (!mGame.userPlay(index)) {
                                return;
                            }
                            checkEndGame();
                            updateBoard();
                        }

                        if (!mGame.getGameEnded()) {
                            mWaiting = true;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mGame.cpuPlay();
                                    checkEndGame();
                                    updateBoard();
                                    mWaiting = false;
                                }
                            }, 1000);
                        }
                    }
                }
            });
        }

        mReset = findViewById(R.id.reset);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.reset();
                updateBoard();

                for (Button btn : mButtonGrid) {
                    btn.setBackgroundColor(getResources().getColor(R.color.colorButtonNormal, getTheme()));
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putCharArray(BOARD_KEY, mGame.getGrid());
        savedInstanceState.putBoolean(IS_X_TURN_KEY, mGame.getIsXTurn());
        savedInstanceState.putBoolean(USER_STARTS_KEY, mGame.getUserStarts());
        savedInstanceState.putInt(X_WINS_KEY, mGame.getXWins());
        savedInstanceState.putInt(O_WINS_KEY, mGame.getOWins());

        // game is in end state
        boolean gameEnded = mGame.getGameEnded();
        savedInstanceState.putBoolean(GAME_ENDED_KEY, gameEnded);
        if (gameEnded) {
            savedInstanceState.putIntArray(WIN_LINE_KEY, mWinLine);
        }
    }

    private void checkEndGame() {
        if (mGame.getGameEnded() == false) return;

        int[] line = mGame.getWinningLine();

        char symbol = !mGame.getIsXTurn() ? 'X' : 'O'; // get previous turn (winner)

        if (Arrays.equals(line, new int[]{0,0,0})) {
            Toast.makeText(MainActivity.this, "Draw", Toast.LENGTH_LONG).show();
        }
        else {
            mGame.addWinner(symbol);
            mWinLine = line;
            for (int index : line) {
                mButtonGrid[index].setBackgroundColor(getResources().getColor(R.color.colorWinner, getTheme()));
            }
            Toast.makeText(MainActivity.this, symbol + " Wins", Toast.LENGTH_LONG).show();
        }
    }

    private void updateBoard() {
        char[] grid = mGame.getGrid();
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] != '\u0000') {
                mButtonGrid[i].setText(String.valueOf(grid[i]));
            }
            else {
                mButtonGrid[i].setText("");
            }
        }
        TextView oWins = findViewById(R.id.oWins);
        TextView xWins = findViewById(R.id.xWins);
        oWins.setText(String.valueOf(mGame.getOWins()));
        xWins.setText(String.valueOf(mGame.getXWins()));
    }


}
