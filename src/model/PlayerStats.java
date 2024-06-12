package model;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats implements Comparable<PlayerStats> {
    private String playerName;
    private int score;
    private int wordsGuessed;
    private int gamesPlayed;
    private Map<String, Integer> categoryScores;

    public PlayerStats(String playerName, int score, int wordsGuessed, int gamesPlayed) {
        this.playerName = playerName;
        this.score = score;
        this.wordsGuessed = wordsGuessed;
        this.gamesPlayed = gamesPlayed;
        this.categoryScores = new HashMap<>();
    }

    public PlayerStats(String playerName, int score, int wordsGuessed, int gamesPlayed,
                       Map<String, Integer> categoryScores) {
        this.playerName = playerName;
        this.score = score;
        this.wordsGuessed = wordsGuessed;
        this.gamesPlayed = gamesPlayed;
        this.categoryScores = categoryScores;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getWordsGuessed() {
        return wordsGuessed;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public Map<String, Integer> getCategoryScores() {
        return categoryScores;
    }

    public void addCategoryScore(String category, int score) {
        categoryScores.put(category, score);
    }

    @Override
    public int compareTo(PlayerStats other) {
        return Integer.compare(other.score, this.score);
    }
}
