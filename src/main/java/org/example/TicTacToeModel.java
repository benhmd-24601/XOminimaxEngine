package org.example;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeModel {
    public static final char AI = 'X';
    public static final char HUMAN = 'O';
    public static final char EMPTY = ' ';
    public static final int N = 3;

    private char[][] board;
    private boolean gameOver;
    private String gameResult;
    private boolean humanTurn;

    public TicTacToeModel() {
        initBoard();
        gameOver = false;
        humanTurn = true;
    }

    public void initBoard() {
        board = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public boolean makeHumanMove(int r, int c) {
        if (isValidMove(r, c) && !gameOver && humanTurn) {
            board[r][c] = HUMAN;
            humanTurn = false;
            checkGameStatus();
            return true;
        }
        return false;
    }

    public void makeAIMove() {
        if (gameOver || humanTurn) return;

        int[] best = findBestMove();
        if (best[0] != -1 && best[1] != -1) {
            board[best[0]][best[1]] = AI;
            humanTurn = true;
            checkGameStatus();
        }
    }

    private void checkGameStatus() {
        int score = evaluate();
        if (score == 10) {
            gameOver = true;
            gameResult = "AI Wins!";
        } else if (score == -10) {
            gameOver = true;
            gameResult = "You Win!";
        } else if (!isMovesLeft()) {
            gameOver = true;
            gameResult = "It's a Draw!";
        }
    }

    public boolean isValidMove(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N && board[r][c] == EMPTY;
    }

    public boolean isMovesLeft() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == EMPTY) return true;
            }
        }
        return false;
    }

    public int evaluate() {
        // Check rows
        for (int i = 0; i < N; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0] == AI ? 10 : -10;
            }
        }
        // Check columns
        for (int j = 0; j < N; j++) {
            if (board[0][j] != EMPTY && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j] == AI ? 10 : -10;
            }
        }
        // Check diagonals
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0] == AI ? 10 : -10;
        }
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2] == AI ? 10 : -10;
        }
        return 0;
    }

    private int alphabeta(int depth, int alpha, int beta, boolean isMax) {
        int score = evaluate();
        if (score == 10) return score - depth;  // Prefer quicker wins
        if (score == -10) return score + depth; // Prefer quicker blocks
        if (!isMovesLeft()) return 0;

        if (isMax) {
            int value = Integer.MIN_VALUE;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI;
                        value = Math.max(value, alphabeta(depth + 1, alpha, beta, false));
                        board[i][j] = EMPTY;
                        alpha = Math.max(alpha, value);
                        if (alpha >= beta) return value;
                    }
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN;
                        value = Math.min(value, alphabeta(depth + 1, alpha, beta, true));
                        board[i][j] = EMPTY;
                        beta = Math.min(beta, value);
                        if (alpha >= beta) return value;
                    }
                }
            }
            return value;
        }
    }

    private int[] findWinningMove(char player) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = player;
                    int score = evaluate();
                    board[i][j] = EMPTY;
                    if ((player == AI && score == 10) || (player == HUMAN && score == -10)) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return null;
    }

    private int[] findBestMove() {
        // 1. Check for immediate win
        int[] winNow = findWinningMove(AI);
        if (winNow != null) {
            System.out.println("AI: Winning move at " + winNow[0] + "," + winNow[1]);
            return winNow;
        }

        // 2. Check for blocking opponent's win
        int[] blockNow = findWinningMove(HUMAN);
        if (blockNow != null) {
            System.out.println("AI: Blocking move at " + blockNow[0] + "," + blockNow[1]);
            return blockNow;
        }

        // 3. Try center first
        if (board[1][1] == EMPTY) {
            System.out.println("AI: Taking center");
            return new int[]{1, 1};
        }

        // 4. Use alpha-beta with strategic move ordering
        int bestVal = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        // Get all possible moves with strategic ordering
        List<int[]> moves = getStrategicMoves();

        for (int[] move : moves) {
            int i = move[0], j = move[1];
            board[i][j] = AI;
            int moveVal = alphabeta(0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board[i][j] = EMPTY;

            if (moveVal > bestVal) {
                bestVal = moveVal;
                bestRow = i;
                bestCol = j;
            }
        }

        System.out.println("AI: Best move at " + bestRow + "," + bestCol + " with value " + bestVal);
        return new int[]{bestRow, bestCol};
    }

    private List<int[]> getStrategicMoves() {
        List<int[]> moves = new ArrayList<>();
        List<int[]> corners = new ArrayList<>();
        List<int[]> edges = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == EMPTY) {
                    if ((i == 0 && j == 0) || (i == 0 && j == 2) ||
                            (i == 2 && j == 0) || (i == 2 && j == 2)) {
                        corners.add(new int[]{i, j});
                    } else if ((i == 0 && j == 1) || (i == 1 && j == 0) ||
                            (i == 1 && j == 2) || (i == 2 && j == 1)) {
                        edges.add(new int[]{i, j});
                    } else {
                        moves.add(new int[]{i, j}); // Center (already taken in this case)
                    }
                }
            }
        }

        // Order: corners first, then edges
        moves.addAll(0, corners);
        moves.addAll(edges);

        return moves;
    }

    // --- Getters ---
    public char[][] getBoard() { return board; }
    public boolean isGameOver() { return gameOver; }
    public String getGameResult() { return gameResult; }
    public boolean isHumanTurn() { return humanTurn; }
    public char getCell(int r, int c) { return board[r][c]; }
}