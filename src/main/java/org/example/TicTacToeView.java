package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TicTacToeView extends JFrame {
    private JButton[][] cells;
    private JLabel statusLabel;
    private JButton restartButton;
    private JPanel boardPanel;

    public TicTacToeView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tic-Tac-Toe AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // Status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(new Color(50, 50, 50));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusLabel = new JLabel("Your Turn (O)");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Board panel
        boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        boardPanel.setBackground(new Color(60, 63, 65));
        cells = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = new JButton();
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                cells[i][j].setBackground(new Color(240, 240, 240));
                cells[i][j].setFocusPainted(false);
                cells[i][j].setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 2));
                cells[i][j].setPreferredSize(new Dimension(80, 80));
                boardPanel.add(cells[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        restartButton = new JButton("New Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setBackground(new Color(70, 130, 180));
        restartButton.setForeground(Color.WHITE);
        restartButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        controlPanel.add(restartButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void setCellListener(ActionListener listener) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].addActionListener(listener);
            }
        }
    }

    public void setRestartListener(ActionListener listener) {
        restartButton.addActionListener(listener);
    }

    public void updateCell(int row, int col, char player) {
        JButton cell = cells[row][col];
        cell.setText(String.valueOf(player));
        cell.setEnabled(false);

        if (player == 'X') {
            cell.setForeground(new Color(220, 20, 60)); // Crimson red for AI
        } else {
            cell.setForeground(new Color(30, 144, 255)); // Dodger blue for human
        }
    }

    public void updateStatus(String status, boolean humanTurn) {
        statusLabel.setText(status);
        if (humanTurn) {
            statusLabel.setForeground(new Color(30, 144, 255)); // Blue for human turn
        } else {
            statusLabel.setForeground(new Color(220, 20, 60)); // Red for AI turn
        }
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setText("");
                cells[i][j].setEnabled(true);
                cells[i][j].setForeground(Color.BLACK);
            }
        }
    }

    public void showGameOver(String result) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setEnabled(false);
            }
        }

        // Show result dialog
        String message;
        Color color;
        if (result.contains("Win")) {
            message = result;
            color = result.contains("You") ? new Color(30, 144, 255) : new Color(220, 20, 60);
        } else {
            message = result;
            color = new Color(46, 139, 87); // Sea green for draw
        }

        JOptionPane.showMessageDialog(this, message, "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getCell(int row, int col) {
        return cells[row][col];
    }
}