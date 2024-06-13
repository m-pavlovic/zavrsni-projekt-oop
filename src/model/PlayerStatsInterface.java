package model;

import java.util.Map;

public interface PlayerStatsInterface {
    String getPlayerName();
    int getScore();
    int getWordsGuessed();
    int getGamesPlayed();
    Map<String, Integer> getCategoryScores();
    void addCategoryScore(String category, int score);
}

