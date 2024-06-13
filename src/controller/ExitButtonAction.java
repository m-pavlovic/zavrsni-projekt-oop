package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//klasa koja implementira actionlistener za rukovanje dogadajem pritiska na gumb, posrednik između komponente i akcije koja ce se dogodit kad se pritisne gumb
public class ExitButtonAction implements ActionListener {
    private ExitButtonActionListener listener; //referenca na objekt koji implementira ExitButtonActionListener sucelje

    //metoda koja postavlja listener
    public void setExitButtonActionListener(ExitButtonActionListener listener) {
        this.listener = listener;
    }

    //poziva metodu na listeneru kada se pritisne gumb
    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.exitButtonActionPerformed(new ExitButtonActionEvent(this));
        }
    }
}
