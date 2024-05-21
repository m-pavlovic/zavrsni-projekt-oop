package gamelogic;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayButtonAction implements ActionListener {

    private IndexPage indexPage;
    private JTextField enterName;
    public PlayButtonAction(IndexPage indexPage, JTextField enterName) {
        this.indexPage = indexPage;
        this.enterName = enterName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (enterName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter your name");
        } else {
            String name = enterName.getText();
            String category = (String) indexPage.categoryComboBox.getSelectedItem();
            new Game(name);
            indexPage.dispose();
        }
    }

}
