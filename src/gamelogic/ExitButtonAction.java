package gamelogic;

import java.awt.event.ActionEvent;

public class ExitButtonAction implements ExitButtonActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}

