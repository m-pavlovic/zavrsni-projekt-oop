package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HighscoreManager {

    private static final String STATS_FILE = "data/player_stats.txt";
    private List<PlayerStats> playerStatsList;

    public HighscoreManager() {
        playerStatsList = new ArrayList<>();
        loadPlayerStats();
    }

    private void loadPlayerStats() { //ucitava statistiku igraca iz filea
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
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
            e.printStackTrace();
        }
    }


    //sprema stats igraca u file
    public void savePlayerStats(String playerName, int score, int wordsGuessed, int gamesPlayed,
                                Map<String, Integer> categoryScores) {
        playerStatsList.add(new PlayerStats(playerName, score, wordsGuessed, gamesPlayed, categoryScores));
        savePlayerStatsToFile();
    }

    private void savePlayerStatsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE))) {
            for (PlayerStats stats : playerStatsList) {
                writer.write(stats.getPlayerName() + " " + stats.getScore() + " " + stats.getWordsGuessed() + " " +
                        stats.getGamesPlayed() + " ");
                for (Map.Entry<String, Integer> entry : stats.getCategoryScores().entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue() + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public List<PlayerStats> getTopPlayerStats(int limit) {
//        Collections.sort(playerStatsList);
//        return playerStatsList.subList(0, Math.min(limit, playerStatsList.size()));
//    }
}
