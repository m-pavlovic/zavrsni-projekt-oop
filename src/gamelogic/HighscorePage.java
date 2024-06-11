package gamelogic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscorePage extends JFrame {
    private JTextArea scores;
    private List<PlayerStats> playerStatsList;

    public HighscorePage(Game game) {
        setTitle("Highscores");
        setSize(800, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        scores = new JTextArea();
        scores.setEditable(false);
        add(new JScrollPane(scores), BorderLayout.CENTER);

        loadScores();

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

    private void loadScores() {
        playerStatsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Game.STATS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    String playerName = parts[0];
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

        scores.setText("Player Name\tScore\tWords Guessed\tGames Played\n");
        for (PlayerStats stats : playerStatsList) {
            scores.append(stats.getPlayerName() + "\t" + stats.getScore() + "\t" + stats.getWordsGuessed() + "\t" + stats.getGamesPlayed() + "\n");
            for (String category : stats.getCategoryScores().keySet()) {
                scores.append("\t" + category + ": " + stats.getCategoryScores().get(category) + "\n");
            }
        }
    }
}
