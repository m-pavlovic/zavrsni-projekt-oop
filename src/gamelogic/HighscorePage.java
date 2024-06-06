package gamelogic;
import gamelogic.HighscoreInterface;
import gamelogic.Player;
import gamelogic.PlayerInterface;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HighscorePage extends JFrame implements HighscoreInterface {
    private JTextArea scores;
    private ArrayList<PlayerInterface> highestScore;

    public HighscorePage() {
        setTitle("Highscores");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        scores = new JTextArea();
        add(new JScrollPane(scores));
        setScores(10);
    }

    @Override
    public void setScores(int limit) {
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
                        System.err.println("Invalid score format: " + parts[1]);
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading highscores file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Sortiraj listu koristeći implementaciju Comparable u klasi Player
        Collections.sort(highestScore, (a, b) -> ((Player) b).getScore() - ((Player) a).getScore());

        scores.setText(""); // Očisti prethodne rezultate
        for (int i = 0; i < limit && i < highestScore.size(); i++) {
            scores.append((i + 1) + ".) " + highestScore.get(i).toString() + "\n");
        }
    }
}
