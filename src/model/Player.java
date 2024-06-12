package model;

public class Player implements PlayerInterface, Comparable<Player> {
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

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.getScore(), this.getScore());
    }

    @Override
    public String toString() {
        return name + " " + score;
    }
}
