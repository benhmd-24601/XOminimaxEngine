package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeController {
    private TicTacToeModel model;
    private TicTacToeView view;

    public TicTacToeController(TicTacToeModel model, TicTacToeView view) {
        this.model = model;
        this.view = view;

        initializeController();
    }

    private void initializeController() {
        // Add cell click listeners
        view.setCellListener(new CellClickListener());

        // Add restart button listener
        view.setRestartListener(new RestartClickListener());

        // Initial status update
        updateView();
    }

    private class CellClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.isGameOver() || !model.isHumanTurn()) {
                return;
            }

            // Find which cell was clicked
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (e.getSource() == view.getCell(i, j)) {
                        handleHumanMove(i, j);
                        return;
                    }
                }
            }
        }
    }

    private class RestartClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            restartGame();
        }
    }

    private void handleHumanMove(int row, int col) {
        if (model.makeHumanMove(row, col)) {
            updateView();

            // If game continues, let AI make a move
            if (!model.isGameOver()) {
                // Small delay for better UX
                Timer timer = new Timer(500, e -> {
                    model.makeAIMove();
                    updateView();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void updateView() {
        // Update board
        char[][] board = model.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != TicTacToeModel.EMPTY) {
                    view.updateCell(i, j, board[i][j]);
                }
            }
        }

        // Update status
        if (model.isGameOver()) {
            view.updateStatus("Game Over: " + model.getGameResult(), false);
        } else {
            String status = model.isHumanTurn() ? "Your Turn (O)" : "AI Thinking... (X)";
            view.updateStatus(status, model.isHumanTurn());
        }
    }

    private void restartGame() {
        // Create a new model instance to ensure complete reset
        model = new TicTacToeModel();
        view.resetBoard();
        view.updateStatus("Your Turn (O)", true);

        // Re-enable all cells and ensure they're clickable
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                view.getCell(i, j).setEnabled(true);
            }
        }
    }

    public void startGame() {
        view.setVisible(true);
    }
}