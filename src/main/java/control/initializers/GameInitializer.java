package main.java.control.initializers;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class GameInitializer {
    private String levelsPath;
    private int currentLevelIndex;

    public GameInitializer(String levelsPath) {
        this.levelsPath = levelsPath;
        this.currentLevelIndex = 0;
    }

    public String getLevelPath(int levelIndex) {
        return Paths.get(levelsPath, "level" + levelIndex + ".txt").toString();
    }

    public int getTotalLevels() {
        try {
            Path levelsDir = Paths.get(levelsPath);
            try (Stream<Path> files = Files.list(levelsDir)) {
                return (int) files
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".txt"))
                        .count();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error counting level files: " + e.getMessage(), e);
        }
    }

    public void loadNextLevel(LevelInitializer levelInitializer) {
        currentLevelIndex++;
        if (currentLevelIndex <= getTotalLevels()) {
            String nextLevelPath = getLevelPath(currentLevelIndex);
            levelInitializer.initLevel(nextLevelPath);
        }
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }
}
