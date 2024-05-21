package gamelogic;

import java.util.Comparator;

public class Player{

    private String name;

    private int highscore;

    public Player(String name, int highscore) {
        this.name = name;
        this.highscore = highscore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    @Override
    public String toString() {
        return name + " " + highscore + "\n";
    }

    public static Comparator<Player> highScoreCompare = new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            return Integer.compare(o2.getHighscore(),o1.getHighscore());
        }
    };

}
