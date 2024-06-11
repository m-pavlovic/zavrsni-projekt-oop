package gamelogic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Game {

    private JFrame frame;
    private JTextField letterInput;
    private JLabel wordDisplay, playerNameLabel, scoreLabel;
    private JTextArea missedLetters;
    private JPanel gallowsPanel;

    private String wordToGuess;
    private List<Character> triedLettersList = new ArrayList<>();
    private int incorrectGuesses = 0;
    private String playerName;
    private int score = 0;
    private int wordsGuessed = 0;
    private int gamesPlayed = 0;
    private Map<String, List<String>> categoriesMap = new HashMap<>();
    private Map<String, Integer> categoryScores = new HashMap<>();
    public static final String STATS_FILE = "data/player_stats.txt";
    private String category;

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

        frame.setJMenuBar(new GameMenuBar(this));

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
        letterInput = new JTextField(10);
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
        category = (String) IndexPage.categoryComboBox.getSelectedItem();
        List<String> words = categoriesMap.get(category);
        if (words != null && !words.isEmpty()) {
            int randomIndex = new Random().nextInt(words.size());
            wordToGuess = words.get(randomIndex).toUpperCase();
        } else {
            wordToGuess = "EXAMPLE";
        }
        wordDisplay.setText(generateWordDisplay());
        missedLetters.setText("Missed Letters: \n");
        updateGallows();
        letterInput.setText("");
        gamesPlayed++;
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
                    wordsGuessed++;
                    categoryScores.put(category, categoryScores.getOrDefault(category, 0) + 10);
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
                wordsGuessed++;
                categoryScores.put(category, categoryScores.getOrDefault(category, 0) + 10);
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
            int choice = JOptionPane.showConfirmDialog(frame, "You guessed the word! Continue?", "Game over", JOptionPane.YES_NO_OPTION);
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
                display.append("    ");
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
        g.drawLine(0, 0, 0, 200);
        g.drawLine(0, 0, 100, 0);
        g.drawLine(100, 0, 100, 20);

        if (incorrectGuesses >= 1) {
            g.drawOval(80, 20, 40, 40);
        }
        if (incorrectGuesses >= 2) {
            g.drawLine(100, 60, 100, 120);
        }
        if (incorrectGuesses >= 3) {
            g.drawLine(100, 60, 70, 100);
        }
        if (incorrectGuesses >= 4) {
            g.drawLine(100, 60, 130, 100);
        }
        if (incorrectGuesses >= 5) {
            g.drawLine(100, 120, 70, 170);
        }
        if (incorrectGuesses >= 6) {
            g.drawLine(100, 120, 130, 170);
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
        new HighscorePage(this, false).setVisible(true);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    public void saveScore() {
        try (FileWriter writer = new FileWriter(STATS_FILE, true)) {
            writer.write(playerName + " " + score + " " + wordsGuessed + " " + gamesPlayed + " ");
            for (Map.Entry<String, Integer> entry : categoryScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + " ");
            }
            writer.write("\n");
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    public void showAllPlayersStats() {
        new HighscorePage(this, false).setVisible(true);
    }

    public void showPlayerStats() {
        new HighscorePage(this, true).setVisible(true);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public String getPlayerName() {
        return playerName;
    }
}
