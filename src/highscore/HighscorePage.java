package highscore;

import gamelogic.PlayAgainButtonAction;
import gamelogic.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscorePage extends JFrame {
    private List<Player> highestScore;
    private JButton playAgainButton;
    private JTextArea scores;
    Font f = new Font("serif",Font.PLAIN, 26);
    public HighscorePage() {

        setTitle("Hangman");
        setPreferredSize(new Dimension(800, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);


        // Top players label
        JLabel title = new JLabel("Top 10 players");
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        scores = new JTextArea();
        scores.setEditable(false);
        //scores.setPreferredSize(new Dimension(500, 380));
        setScores(10);
        scores.setFont(f);
        add(new JScrollPane(scores), BorderLayout.CENTER);

        // Table of scores
        String[] columns = {"Name", "Score"};

        // Menu and Play Again buttons
        JPanel filler = new JPanel();

        playAgainButton = new JButton("Play Again");
        filler.add(playAgainButton,BorderLayout.CENTER);
        add(filler,BorderLayout.SOUTH);

        pack();

        playAgainButton.addActionListener(new PlayAgainButtonAction(this));
    }private void setScores(int limit) {
        highestScore = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/highscores"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2 && !parts[1].isEmpty()) {
                    try {
                        int score = Integer.parseInt(parts[1]);
                        highestScore.add(new Player(parts[0], score));
                    } catch (NumberFormatException e) {
                        // Log error and continue to the next line
                        System.err.println("Invalid score format: " + parts[1]);
                    }
                } else {
                    // Log error and continue to the next line
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading highscores file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        Collections.sort(highestScore, Player.highScoreCompare);
        for (int i = 0; i < limit && i < highestScore.size(); i++) {
            scores.append((i + 1) + ".) " + highestScore.get(i).toString());
        }
    }

}

