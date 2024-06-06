package gamelogic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

    private JFrame frame;
    private JTextField letterInput;
    private JLabel wordDisplay, playerNameLabel, scoreLabel;
    private JTextArea missedLetters;
    private JPanel gallowsPanel;

    private String wordToGuess;
    private List<Character> triedLettersList = new ArrayList<>();
    private int incorrectGuesses = 0;
    String playerName;
    private int score = 0;
    private Map<String, List<String>> categoriesMap = new HashMap<>();
    String category = IndexPage.categoryComboBox.getSelectedItem().toString();

    public Game(String playerName) {
        this.playerName = playerName;
        loadWordsFromFile();
        List<String> words = categoriesMap.get(category);
        if (words != null && !words.isEmpty()) {
            int randomIndex = new Random().nextInt(words.size());
            wordToGuess = words.get(randomIndex).toUpperCase();
            System.out.println(wordToGuess);
        } else {
            wordToGuess = "EXAMPLE"; // fallback word in case the category doesn't exist or has no words
        }
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Hangman Game");
        frame.setSize(800, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        gallowsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGallows(g);
            }
        };
        gallowsPanel.setPreferredSize(new Dimension(200, 200));

        missedLetters = new JTextArea(5, 15);
        missedLetters.setEditable(false);
        missedLetters.setText("Missed Letters: \n");

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(gallowsPanel, BorderLayout.NORTH);
        leftPanel.add(missedLetters, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.WEST);

        playerNameLabel = new JLabel(playerName);
        scoreLabel = new JLabel("Score: " + score);

        wordDisplay = new JLabel(generateWordDisplay());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(playerNameLabel);
        rightPanel.add(scoreLabel);
        rightPanel.add(wordDisplay);

        frame.add(rightPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        letterInput = new JTextField(1);
        JButton submitButton = new JButton("Guess");
        submitButton.addActionListener(new GuessButtonAction(this));
        inputPanel.add(new JLabel("Guess a letter:"));
        inputPanel.add(letterInput);
        inputPanel.add(submitButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    public void handleGuess() {
        String letter = letterInput.getText().toUpperCase();
        if (letter.length() != 1 || triedLettersList.contains(letter.charAt(0))) {
            JOptionPane.showMessageDialog(frame, "Enter a valid, new letter!");
            letterInput.setText("");
            return;
        }

        triedLettersList.add(letter.charAt(0));
        if (wordToGuess.contains(letter)) {
            wordDisplay.setText(generateWordDisplay());
        } else {
            incorrectGuesses++;
            missedLetters.append(letter + ", ");
            updateGallows();
        }
        letterInput.setText("");

        checkGameEnd();
    }

    private String generateWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (c == ' ') {
                display.append("    ");  // Dodajte dva razmaka za razmake u riječi
            } else if (triedLettersList.contains(c)) {
                display.append(c).append(" ");
            } else {
                display.append("_ ");
            }
        }
        return display.toString();
    }

    private void drawGallows(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 200);   // stub
        g.drawLine(0, 0, 100, 0);  // gornji prečnik
        g.drawLine(100, 0, 100, 20); // vješalo

        // Glava (nakon 1. pogreške)
        if (incorrectGuesses >= 1) {
            g.drawOval(80, 20, 40, 40);
        }

        // Tijelo (nakon 2. pogreške)
        if (incorrectGuesses >= 2) {
            g.drawLine(100, 60, 100, 120);
        }

        // Lijeva ruka (nakon 3. pogreške)
        if (incorrectGuesses >= 3) {
            g.drawLine(100, 60, 70, 100);
        }

        // Desna ruka (nakon 4. pogreške)
        if (incorrectGuesses >= 4) {
            g.drawLine(100, 60, 130, 100);
        }

        // Lijeva noga (nakon 5. pogreške)
        if (incorrectGuesses >= 5) {
            g.drawLine(100, 120, 70, 170);  // lijeva noga
        }
        // Desna noga (nakon 6. pogreške)
        if (incorrectGuesses >= 6) {
            g.drawLine(100, 120, 130, 170); // desna noga
        }
    }

    private void updateGallows() {
        gallowsPanel.repaint();
    }

    private void loadWordsFromFile() {
        String currentCategory = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/words"))) {
            String line;
            List<String> words = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("[")) {
                    if (words != null && !words.isEmpty()) {
                        categoriesMap.put(currentCategory, new ArrayList<>(words));
                    }
                    currentCategory = line.substring(1, line.length() - 1);
                    words = new ArrayList<>();
                } else {
                    if (words != null) {
                        words.add(line);
                    }
                }
            }
            if (words != null && !words.isEmpty()) {
                categoriesMap.put(currentCategory, words);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    private void checkGameEnd() {
        if (wordDisplay.getText().replace(" ", "").equals(wordToGuess)) {
            score += 10;
            updateScoreLabel();

            int choice = JOptionPane.showConfirmDialog(frame, "You guessed the word! Continue?", "Game over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                triedLettersList.clear();
                List<String> words = categoriesMap.get(category);
                if (words != null && !words.isEmpty()) {
                    int randomIndex = new Random().nextInt(words.size());
                    wordToGuess = words.get(randomIndex).toUpperCase();
                } else {
                    wordToGuess = "EXAMPLE"; // fallback word in case the category doesn't exist or has no words
                }
                wordDisplay.setText(generateWordDisplay());
                triedLettersList.clear();
                incorrectGuesses = 0;
                missedLetters.setText("Missed Letters: \n");
                updateGallows();

            } else {
                saveScore();
                saveToFile();
                frame.dispose();
                new HighscorePage().setVisible(true);
            }

        } else if (incorrectGuesses >= 6) {
            JOptionPane.showMessageDialog(frame, "Game over, you lost! The word was: " + wordToGuess);
            saveScore();
            saveToFile();
            frame.dispose();
            new HighscorePage().setVisible(true);
        }
    }

    public HashMap<String, Integer> saveScore() {
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put(playerName, score);
        return scores;
    }

    public void saveToFile() {
        HashMap<String, Integer> scores = saveScore();
        try (FileWriter writer = new FileWriter("data/highscores", true)) {
            for (String name : scores.keySet()) {
                writer.write(name + " " + scores.get(name) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }
}
