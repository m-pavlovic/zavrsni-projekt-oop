package gamelogic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayButtonAction implements ActionListener {
    private PlayButtonActionListener listener;
    private JTextField enterName;
    private JComboBox<String> categoryComboBox;

    public PlayButtonAction(JTextField enterName, JComboBox<String> categoryComboBox) {
        this.enterName = enterName;
        this.categoryComboBox = categoryComboBox;
    }

    public void setPlayButtonActionListener(PlayButtonActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String playerName = enterName.getText();
        String category = (String) categoryComboBox.getSelectedItem();

        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter your name");
        } else {
            if (listener != null) {
                listener.playButtonActionPerformed(new PlayButtonActionEvent(this, playerName, category));
            }
        }
    }
}
