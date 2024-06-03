package gamelogic;

import highscore.HighscorePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayAgainButtonAction implements ActionListener {
    private HighscorePage page;

    public PlayAgainButtonAction(HighscorePage page) {
        this.page = page;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new IndexPage();
        page.dispose();
    }
}
