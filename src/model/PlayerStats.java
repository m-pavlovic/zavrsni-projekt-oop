package model;


import java.util.HashMap;
import java.util.Map;

//klasa koja cuva podatke o bodovima, imenu, pogodenim rijecima, bodovima kategorija itd

public class PlayerStats implements PlayerStatsInterface, Comparable<PlayerStats> {
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

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getWordsGuessed() {
        return wordsGuessed;
    }

    @Override
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    @Override
    public Map<String, Integer> getCategoryScores() {
        return categoryScores;
    }

    @Override
    public void addCategoryScore(String category, int score) {
        categoryScores.put(category, score);
    }

    @Override
    public int compareTo(PlayerStats other) {
        return Integer.compare(other.score, this.score);
    }
}
