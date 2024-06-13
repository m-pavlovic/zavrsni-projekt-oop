package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class NewGameButtonAction implements ActionListener {
    private NewGameButtonActionListener listener;

    public void setNewGameButtonActionListener(NewGameButtonActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.newGameButtonActionPerformed(new NewGameButtonActionEvent(this));
        }
    }
}
