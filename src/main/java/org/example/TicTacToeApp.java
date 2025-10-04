package org.example;

import javax.swing.*;

public class TicTacToeApp {
    public static void main(String[] args) {
        // Run on EDT without changing look and feel
        SwingUtilities.invokeLater(() -> {
            TicTacToeModel model = new TicTacToeModel();
            TicTacToeView view = new TicTacToeView();
            TicTacToeController controller = new TicTacToeController(model, view);

            controller.startGame();
        });
    }
}