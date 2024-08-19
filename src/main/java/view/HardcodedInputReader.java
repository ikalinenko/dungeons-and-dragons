package main.java.view;

import java.util.List;

public class HardcodedInputReader implements InputReader {
    private List<String> actions;
    private int currentIndex;

    public HardcodedInputReader(List<String> actions) {
        this.actions = actions;
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
