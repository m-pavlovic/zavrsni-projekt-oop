package gamelogic;

import java.util.Comparator;

public class Player implements PlayerInterface {
    private String name;
    private int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    public static Comparator<Player> highScoreCompare = new Comparator<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(p2.getScore(), p1.getScore());
        }
    };

    @Override
    public String toString() {
        return name + " " + score;
    }
}

