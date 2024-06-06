package gamelogic;

import java.awt.event.ActionEvent;

public class GuessButtonAction implements GuessButtonActionListener {
    private Game game;

    public GuessButtonAction(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.handleGuess();
    }
}
