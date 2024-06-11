package gamelogic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HighscorePage extends JFrame {
    private JTable scoreTable;
    private List<PlayerStats> playerStatsList;

    public HighscorePage(Game game, boolean showCurrentPlayerOnly) {
        setTitle("Highscores");
        setSize(800, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        scoreTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        if (showCurrentPlayerOnly) {
            loadPlayerStats(game.getPlayerName());
        } else {
            loadScores(null);
        }

        JPanel buttonPanel = new JPanel();
        JButton homeButton = new JButton("Home Screen");
        JButton backButton = new JButton("Back to the Game");
        JButton exitButton = new JButton("Exit");

        homeButton.addActionListener(e -> {
            dispose();
            new IndexPage().setVisible(true);
        });

        backButton.addActionListener(e -> {
            dispose();
            game.setVisible(true);
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(homeButton);
        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadScores(String filterPlayer) {
        playerStatsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Game.STATS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    String playerName = parts[0];
                    if (filterPlayer != null && !filterPlayer.equals(playerName)) {
                        continue;
                    }
                    int score = Integer.parseInt(parts[1]);
                    int wordsGuessed = Integer.parseInt(parts[2]);
                    int gamesPlayed = Integer.parseInt(parts[3]);
                    PlayerStats playerStats = new PlayerStats(playerName, score, wordsGuessed, gamesPlayed);

                    for (int i = 4; i < parts.length; i++) {
                        String[] categoryScore = parts[i].split(":");
                        if (categoryScore.length == 2) {
                            playerStats.addCategoryScore(categoryScore[0], Integer.parseInt(categoryScore[1]));
                        }
                    }
                    playerStatsList.add(playerStats);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading statistics file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Collections.sort(playerStatsList);

        String[] columnNames = {"Player Name", "Score", "Words Guessed", "Games Played", "Category Scores"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (PlayerStats stats : playerStatsList) {
            StringBuilder categoryScores = new StringBuilder();
            for (String category : stats.getCategoryScores().keySet()) {
                categoryScores.append(category).append(": ").append(stats.getCategoryScores().get(category)).append(" ");
            }
            Object[] rowData = {
                    stats.getPlayerName(),
                    stats.getScore(),
                    stats.getWordsGuessed(),
                    stats.getGamesPlayed(),
                    categoryScores.toString()
            };
            model.addRow(rowData);
        }
        scoreTable.setModel(model);
    }

    private void loadPlayerStats(String playerName) {
        playerStatsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Game.STATS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals(playerName)) {
                    int score = Integer.parseInt(parts[1]);
                    int wordsGuessed = Integer.parseInt(parts[2]);
                    int gamesPlayed = Integer.parseInt(parts[3]);
                    PlayerStats playerStats = new PlayerStats(playerName, score, wordsGuessed, gamesPlayed);

                    for (int i = 4; i < parts.length; i++) {
                        String[] categoryScore = parts[i].split(":");
                        if (categoryScore.length == 2) {
                            playerStats.addCategoryScore(categoryScore[0], Integer.parseInt(categoryScore[1]));
                        }
                    }
                    playerStatsList.add(playerStats);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading statistics file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Collections.sort(playerStatsList);

        String[] columnNames = {"Score", "Words Guessed", "Games Played", "Category Scores"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (PlayerStats stats : playerStatsList) {
            StringBuilder categoryScores = new StringBuilder();
            for (String category : stats.getCategoryScores().keySet()) {
                categoryScores.append(category).append(": ").append(stats.getCategoryScores().get(category)).append(" ");
            }
            Object[] rowData = {
                    stats.getScore(),
                    stats.getWordsGuessed(),
                    stats.getGamesPlayed(),
                    categoryScores.toString()
            };
            model.addRow(rowData);
        }
        scoreTable.setModel(model);
    }
}
