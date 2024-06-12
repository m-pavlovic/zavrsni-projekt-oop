package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonAction implements ActionListener {
    private ExitButtonActionListener listener;

    public void setExitButtonActionListener(ExitButtonActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.exitButtonActionPerformed(new ExitButtonActionEvent(this));
        }
    }
}
