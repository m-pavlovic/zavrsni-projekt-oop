package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//rukuje dogadajem pritiska na guess gumb, postavlja listener i prosljeduje dogadaj game klasi
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
