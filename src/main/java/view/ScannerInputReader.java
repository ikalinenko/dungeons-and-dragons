package main.java.view;

import java.util.Scanner;

public class ScannerInputReader implements InputReader {
    private Scanner scanner;

    public ScannerInputReader() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String nextAction() {
        System.out.print("Enter your next action (w/a/s/d/e/q): ");
        return scanner.nextLine().trim();
    }
}
