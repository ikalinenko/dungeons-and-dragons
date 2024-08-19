package main.java.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileInputReader implements InputReader {
    private BufferedReader reader;
    private List<String> actions;
    private int currentIndex;

    public FileInputReader(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.actions = reader.lines().toList();
        this.currentIndex = 0;
    }

    @Override
    public String nextAction() {
        if (currentIndex < actions.size()) {
            return actions.get(currentIndex++).trim();
        }
        return "q"; // Default to 'q' (do nothing) if all actions have been read
    }
}
