package main.java;

import main.java.control.initializers.TileFactory;
import main.java.control.initializers.LevelInitializer;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.utils.generators.RandomGenerator;
import main.java.view.ScannerInputReader;
import main.java.model.game.Board;
import main.java.model.game.Game;
import main.java.model.game.Level;
import main.java.model.tiles.units.players.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            return;
        }

        String levelsDirectory = args[0];


    /*
    public static void main(String[] args) throws Exception {
        //String levelsDirectory = "src/main/resources/levels_dir";
        String levelsDirectory = "C:\\Users\\AM\\levels_dir";
     */


        // Initialize tile factory and player options
        TileFactory tileFactory = new TileFactory();
        List<Supplier<Player>> playerSuppliers = tileFactory.getPlayerTypes();
        System.out.println("Select player:");
        for (int i = 0; i < playerSuppliers.size(); i++) {
            Player playerInstance = tileFactory.producePlayer(i + 1);
            System.out.println((i + 1) + ". " + playerInstance.description());
        }

        // User selects a player
        Scanner scanner = new Scanner(System.in);
        int playerChoice = 0;
        while (playerChoice < 1 || playerChoice > playerSuppliers.size()) {
            System.out.print("Enter a number between 1 and " + playerSuppliers.size() + ": ");
            try {
                playerChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                playerChoice = 0;
            }
        }

        // Initialize callbacks, generator, input reader
        Generator generator = new RandomGenerator(); // Fixed/Random
        MessageCallBack cb = System.out::println;
        DeathCallBack dcb = () -> System.out.println("You Lost.");
        ScannerInputReader inputReader = new ScannerInputReader(); // FileInput/Hardcoded/Scanner

        try {
            // Process first level
            var pathToLevel1 = Files.list(Paths.get(levelsDirectory))
                    .sorted()
                    .collect(Collectors.toList())
                    .get(0);
            String levelPath = pathToLevel1.toString();

            // Initialize the level
            LevelInitializer levelInit = new LevelInitializer(playerChoice, generator, cb, dcb);
            levelInit.initLevel(levelPath);

            // Load the board
            Board board = levelInit.getBoard();

            // Initialize the level and game with the new board
            Level level = new Level(board, inputReader, cb, levelInit);
            Game game = new Game(levelsDirectory, levelInit, inputReader, board, cb);
            game.setLevel(level);

            // Run the game
            game.run();

        } catch (IOException e) {
            System.out.println("Error loading levels: " + e.getMessage());
        }
    }
}
