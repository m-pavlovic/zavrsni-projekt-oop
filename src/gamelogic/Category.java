package gamelogic;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Category {
    private static final Map<String, String[]> categories = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".\\data\\words"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String categoryName = parts[0];
                    String[] words = parts[1].split(",");
                    categories.put(categoryName, words);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomWordFromCategory(String categoryName) {
        String[] words = categories.get(categoryName);
        if (words == null || words.length == 0) {
            return "";
        }
        return words[new Random().nextInt(words.length)];
    }
}
