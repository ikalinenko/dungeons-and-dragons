package main.java.control;

import main.java.model.tiles.units.Unit;
import main.java.model.tiles.units.enemies.Monster;
import main.java.model.tiles.units.players.Warrior;
import main.java.utils.Position;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.FixedGenerator;

public class Program {

    /*
    public static void main(String[] var0) {
        if (var0.length < 1) {
            System.out.println("Error: this program needs a path to the levels directory as an argument.");
            System.exit(-1);
        }
    */

    public static void main(String[] args) {

        /*
        // Initialize board, player, and enemies
        // Assume you load these from the level files

        View main.java.view = new CLI();
        MessageCallBack messageCallBack = main.java.view.getMessageCallBack();

        Game game = new Game(board, messageCallBack);
        game.run();
         */

        /*
        // Initialize the board, player, and enemies
        // Example:
        // Board board = new Board(...);
        // View main.java.view = new CLI();
        // MessageCallBack messageCallBack = main.java.view.getMessageCallBack();
        // Game game = new Game(board, messageCallBack);

        InputReader inputReader;

        // Depending on your use case, you might choose one of the following:
        inputReader = new ScannerInputReader(); // For interactive console input
        // inputReader = new FileInputReader("path/to/input/file.txt"); // For file-based input
        // inputReader = new HardcodedInputReader(List.of("w", "a", "s", "d", "e", "q")); // For hardcoded input

        // Use inputReader in your game loop to get the player's actions
        // while (!game.isGameOver()) {
        //     String action = inputReader.nextAction();
        //     game.processAction(action);
        // }
         */

        Unit p = new Warrior("Player", 100, 10, 10, 5)
                .initialize(new Position(0, 0), new FixedGenerator(), System.out::println, () -> System.out.println("Player died"));
        System.out.println("Hello world!");

        Warrior w1 = new Warrior("John Wick", 300, 30, 4, 10);
        w1.initialize(new Position(0,0), new FixedGenerator(), System.out::println, () -> System.out.println("Player dies"));


        MessageCallBack m1 = (msg) -> System.out.println(msg);
        Warrior w2 = new Warrior("Jon Snow", 300, 30, 4, 3);
        w2.initialize(new Position(0,0), new FixedGenerator(), System.out::println, () -> System.out.println("Player dies"));
        w2.levelUp();
        w2.levelUp();
        w2.levelUp();

        Monster mon1 = new Monster('V', "Voldemort", 30, 4, 10, 5, 20);
        mon1.initialize(new Position(0,0), new FixedGenerator(), System.out::println, () -> System.out.println("Voldemort dies"));

        /*
        View cli = new CLI();
        MessageCallBack m2 = cli.getMessageCallBack();
        DeathCallBack d2 = () -> System.out.println("Player died");
        Rogue rogue1 = new Rogue("Player", 100, 10, 5, 3);
        rogue1.initialize(new Position(0,0), new FixedGenerator(), m2, d2);
        rogue1.levelUp();

         */
    }
}
