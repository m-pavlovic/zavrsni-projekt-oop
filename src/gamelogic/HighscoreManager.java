package gamelogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreManager {

    private static final String HIGHSCORES_FILE = "data/highscores";
    private List<Score> scores;

    public HighscoreManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - "); // assuming format "PlayerName - Score"
                if (parts.length < 2) {
                    continue; // skip any malformed lines
                }
                String playerName = parts[0];
                int score = Integer.parseInt(parts[1]);
                scores.add(new Score(playerName, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(String playerName, int score) {
        scores.add(new Score(playerName, score));
        saveScores();
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORES_FILE))) {
            for (Score score : scores) {
                writer.write(score.getPlayerName() + " - " + score.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Score> getTopScores(int limit) {
        Collections.sort(scores);
        return scores.subList(0, Math.min(limit, scores.size()));
    }

    public static class Score implements Comparable<Score> {
        private String playerName;
        private int score;

        public Score(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(Score other) {
            return other.score - this.score; // descending order
        }
    }
}
