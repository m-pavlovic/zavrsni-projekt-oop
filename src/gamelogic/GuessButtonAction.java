package gamelogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuessButtonAction implements ActionListener {

    private Game game;

    public GuessButtonAction(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.handleGuess();
    }
}
