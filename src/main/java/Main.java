package main.java;

import main.java.control.initializers.BoardLoader;
import main.java.control.initializers.TileFactory;
import main.java.control.initializers.LevelInitializer;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.FixedGenerator;
import main.java.utils.generators.Generator;
import main.java.view.ScannerInputReader;
import main.java.model.game.Board;
import main.java.model.game.Game;
import main.java.model.game.Level;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.FixedGenerator;
import main.java.utils.generators.Generator;
import main.java.view.ScannerInputReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.awt.SystemColor.control;

public class Main {

    /*
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <levels_directory_path>");
            return;
        }

     */


    public static void main(String[] args) throws Exception {
        //String levelsDirectoryPath = "src/main/resources/levels_dir";
        String levelsDirectoryPath = "C:\\Users\\AM\\levels_dir";

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

        // Initialize callbacks and generator
        Generator generator = new FixedGenerator(); // or any other generator you prefer
        MessageCallBack cb = System.out::println;
        DeathCallBack dcb = () -> System.out.println("Player has died.");

        // Use LevelInitializer with playerChoice
        LevelInitializer levelInitializer = new LevelInitializer(playerChoice);

        // Initialize input reader
        ScannerInputReader inputReader = new ScannerInputReader();

        try {
                /*
                // Process each level
            for (var path : Files.list(Paths.get(levelsDirectoryPath))
                    .filter(Files::isRegularFile)
                    .sorted()
                    .toList()) {
                 */

            // Process first level
            var pathToLevel1 = Files.list(Paths.get(levelsDirectoryPath))
                    .sorted()
                    .collect(Collectors.toList())
                    .get(0);
            String levelPath = pathToLevel1.toString();
            //System.out.println("Loading level: " + levelPath);

            // Initialize the level
            levelInitializer.initLevel(levelPath);

            // Load the board
            BoardLoader boardLoader = new BoardLoader(levelInitializer.getTiles(), levelInitializer.getPlayer(), levelInitializer.getEnemies(), levelPath);
            Board board = boardLoader.loadBoard();

            // Initialize the level and game with the new board
            Level level = new Level(board, inputReader, cb, levelInitializer);
            Game game = new Game(board, levelInitializer, inputReader, cb, dcb);
            game.setLevel(level);

            // Run the game
            game.run();

            // Check if the player is alive after the level ends
            if (!board.getPlayer().alive()) {
                System.out.println("Game over.");
            }
        } catch (IOException e) {
            System.out.println("Error loading levels: " + e.getMessage());
        }
    }





    /*
    private static int calculateWidth(String levelPath) {
        try {
            // Calculate the width of the board based on the first line of the level file
            String firstLine = Files.readAllLines(Paths.get(levelPath)).get(0);
            return firstLine.length();
        } catch (IOException e) {
            throw new RuntimeException("Error calculating board width: " + e.getMessage());
        }
    }
     */
}
