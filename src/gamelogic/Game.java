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
        letterInput = new JTextField(10);  // Increased the input area for more characters
        JButton submitButton = new JButton("Guess");
        submitButton.addActionListener(new GuessButtonAction(this));
        inputPanel.add(new JLabel("Guess a letter or the word:"));
        inputPanel.add(letterInput);
        inputPanel.add(submitButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void handleGuess() {
        String input = letterInput.getText().toUpperCase();
        if (input.length() == 1) {
            char letter = input.charAt(0);
            if (triedLettersList.contains(letter) || !Character.isLetter(letter)) {
                JOptionPane.showMessageDialog(frame, "Enter a valid, new letter!");
                letterInput.setText("");
                return;
            }
            triedLettersList.add(letter);
            if (wordToGuess.contains(String.valueOf(letter))) {
                wordDisplay.setText(generateWordDisplay());
            } else {
                incorrectGuesses++;
                missedLetters.append(letter + ", ");
                updateGallows();
            }
        } else {
            if (input.equals(wordToGuess)) {
                wordDisplay.setText(wordToGuess);
                score += 10;
                endGame(true);
            } else {
                incorrectGuesses++;
                JOptionPane.showMessageDialog(frame, "Incorrect guess!");
                updateGallows();
            }
        }
        letterInput.setText("");
        checkGameEnd();
    }

    private String generateWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (c == ' ') {
                display.append("    ");  // Add spaces for spaces in the word
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
        g.drawLine(0, 0, 0, 200);   // pole
        g.drawLine(0, 0, 100, 0);  // top beam
        g.drawLine(100, 0, 100, 20); // rope

        // Head (after 1st wrong guess)
        if (incorrectGuesses >= 1) {
            g.drawOval(80, 20, 40, 40);
        }

        // Body (after 2nd wrong guess)
        if (incorrectGuesses >= 2) {
            g.drawLine(100, 60, 100, 120);
        }

        // Left arm (after 3rd wrong guess)
        if (incorrectGuesses >= 3) {
            g.drawLine(100, 60, 70, 100);
        }

        // Right arm (after 4th wrong guess)
        if (incorrectGuesses >= 4) {
            g.drawLine(100, 60, 130, 100);
        }

        // Left leg (after 5th wrong guess)
        if (incorrectGuesses >= 5) {
            g.drawLine(100, 120, 70, 170);  // left leg
        }
        // Right leg (after 6th wrong guess)
        if (incorrectGuesses >= 6) {
            g.drawLine(100, 120, 130, 170); // right leg
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
                endGame(false);
            }
        } else if (incorrectGuesses >= 6) {
            JOptionPane.showMessageDialog(frame, "Game over, you lost! The word was: " + wordToGuess);
            endGame(false);
        }
    }

    private void endGame(boolean guessedWord) {
        if (guessedWord) {
            score += 10;
            updateScoreLabel();
        }
        saveScore();
        saveToFile();
        frame.dispose();
        new HighscorePage().setVisible(true);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
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
