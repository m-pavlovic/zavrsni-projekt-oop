package gamelogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonAction implements ActionListener {

    private IndexPage idx;

    public ExitButtonAction(IndexPage idx) {
        this.idx = idx;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}

