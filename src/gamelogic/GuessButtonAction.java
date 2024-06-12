package gamelogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuessButtonAction implements ActionListener {
    private GuessButtonActionListener listener;

    public void setGuessButtonActionListener(GuessButtonActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.guessButtonActionPerformed(new GuessButtonActionEvent(this));
        }
    }
}
