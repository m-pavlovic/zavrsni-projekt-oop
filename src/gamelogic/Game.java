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
    private static final String STATS_FILE = "data/player_stats.txt";
    private String category; // Inicijalizacija varijable category

    public Game(String playerName) {
        this.playerName = playerName;
        loadWordsFromFile();
        initializeGUI();
        startNewGame();
    }

    private void initializeGUI() {
        frame = new JFrame("Hangman Game");
        frame.setSize(800, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        frame.setJMenuBar(new GameMenuBar(this)); // Dodajemo MenuBar

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

        wordDisplay = new JLabel("");

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

    public void startNewGame() {
        triedLettersList.clear();
        incorrectGuesses = 0;
        score = 0;
        category = (String) IndexPage.categoryComboBox.getSelectedItem(); // Inicijalizacija kategorije
        List<String> words = categoriesMap.get(category);
        if (words != null && !words.isEmpty()) {
            int randomIndex = new Random().nextInt(words.size());
            wordToGuess = words.get(randomIndex).toUpperCase();
        } else {
            wordToGuess = "EXAMPLE"; // fallback word in case the category doesn't exist or has no words
        }
        wordDisplay.setText(generateWordDisplay());
        missedLetters.setText("Missed Letters: \n");
        updateGallows();
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
                if (wordDisplay.getText().replace(" ", "").equals(wordToGuess)) {
                    score += 10;
                    updateScoreLabel();
                    continueOrEndGame(true);
                    return;
                }
            } else {
                incorrectGuesses++;
                missedLetters.append(letter + ", ");
                updateGallows();
            }
        } else {
            if (input.equals(wordToGuess)) {
                wordDisplay.setText(wordToGuess);
                score += 10;
                updateScoreLabel();
                continueOrEndGame(true);
                return;
            } else {
                incorrectGuesses++;
                JOptionPane.showMessageDialog(frame, "Incorrect guess!");
                updateGallows();
            }
        }
        letterInput.setText("");
        if (incorrectGuesses >= 6) {
            JOptionPane.showMessageDialog(frame, "Game over, you lost! The word was: " + wordToGuess);
            endGame();
        }
    }

    private void continueOrEndGame(boolean guessedWord) {
        if (guessedWord) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You guessed the word!");
            int choice = JOptionPane.showConfirmDialog(frame, "Continue?", "Game over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                startNewGame();
                return;
            }
        }
        endGame();
    }

    private String generateWordDisplay() {
        if (wordToGuess == null) {
            return "";
        }
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

    private void endGame() {
        saveScore();
        frame.dispose();
        new HighscorePage().setVisible(true);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    public void saveScore() {
        try (FileWriter writer = new FileWriter(STATS_FILE, true)) {
            writer.write(playerName + " " + score + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    public void showAllPlayersStats() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
            String line;
            StringBuilder stats = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stats.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, stats.toString(), "All Players Stats", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading statistics file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showPlayerStats() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_FILE))) {
            String line;
            StringBuilder stats = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(playerName)) {
                    stats.append(line).append("\n");
                }
            }
            JOptionPane.showMessageDialog(frame, stats.toString(), "My Stats", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading statistics file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
