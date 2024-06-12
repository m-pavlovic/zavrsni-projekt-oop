package controller;

import java.util.EventObject;

public class PlayButtonActionEvent extends EventObject {
    private String playerName;
    private String category;

    public PlayButtonActionEvent(Object source, String playerName, String category) {
        super(source);
        this.playerName = playerName;
        this.category = category;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCategory() {
        return category;
    }
}
