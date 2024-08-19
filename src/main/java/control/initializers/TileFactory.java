package main.java.control.initializers;

import main.java.model.tiles.Empty;
import main.java.model.tiles.Tile;
import main.java.model.tiles.Wall;
import main.java.model.tiles.units.enemies.Boss;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.enemies.Monster;
import main.java.model.tiles.units.enemies.Trap;
import main.java.model.tiles.units.enemies.Monster;
import main.java.model.tiles.units.enemies.Trap;
import main.java.model.tiles.units.players.*;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TileFactory {
    private Player p;
    public static final List<Supplier<Player>> playerTypes= Arrays.asList(
            () -> new Warrior("Jon Snow", 300, 30, 4, 3),
            () -> new Warrior("The Hound", 400, 20, 6, 5),
            () -> new Mage("Melisandre", 100, 5, 1, 300, 30 ,15, 5, 6),
            () -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
            () -> new Rogue("Arya Stark", 150, 40, 2, 20),
            () -> new Rogue("Bronn", 250, 35, 3, 50),
            () -> new Hunter("Ygritte", 220, 30, 2, 6)
    );

    public static final Map<Character, Supplier<Enemy>> enemyTypes = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('s', () -> new Monster('s', "Lannister Soldier", 80, 8, 3, 3, 25)),
            new AbstractMap.SimpleEntry<>('k', () -> new Monster('k', "Lannister Knight", 200, 14, 8, 4, 50)),
            new AbstractMap.SimpleEntry<>('q', () -> new Monster('q', "Queen’s Guard", 400, 20, 15, 5, 100)),
            new AbstractMap.SimpleEntry<>('z', () -> new Monster('z', "Wright", 600, 30, 15, 3, 100)),
            new AbstractMap.SimpleEntry<>('b', () -> new Monster('b', "Bear-Wright", 1000, 75, 30, 4, 250)),
            new AbstractMap.SimpleEntry<>('g', () -> new Monster('g', "Giant-Wright", 1500, 100, 40, 5, 500)),
            new AbstractMap.SimpleEntry<>('w', () -> new Monster('w', "White Walker", 2000, 150, 50, 6, 1000)),
            new AbstractMap.SimpleEntry<>('M', () -> new Boss('M', "The Mountain", 1000, 60, 25, 6, 500, 5)),
            new AbstractMap.SimpleEntry<>('C', () -> new Boss('C', "Queen Cersei", 100, 10, 10, 1, 1000, 8)),
            new AbstractMap.SimpleEntry<>('K', () -> new Boss('K', "Night’s King", 5000, 300, 150, 8, 5000, 3)),
            new AbstractMap.SimpleEntry<>('B', () -> new Trap('B', "Bonus Trap", 1, 1, 1, 250, 1, 5)),
            new AbstractMap.SimpleEntry<>('Q', () -> new Trap('Q', "Queen’s Trap", 250, 50, 10, 100, 3, 7)),
            new AbstractMap.SimpleEntry<>('D', () -> new Trap('D', "Death Trap", 500, 100, 20, 250, 1, 10))
    );

    public TileFactory() {
    }

    public Player producePlayer(int playerID, Position p, Generator gen, MessageCallBack cb, DeathCallBack dcb) {
        Supplier<Player> supplier = playerTypes.get(playerID - 1); // Ensure playerID is valid
        Player player = supplier.get();
        player.initialize(p, gen, cb, dcb);
        return player;
    }

    public Player producePlayer(int playerID) {
        Supplier<Player> supp = playerTypes.get(playerID-1);
        this.p = supp.get();
        return this.p;
    }

    /*
    public Player producePlayer(int playerID) {
        Supplier<Player> supplier = playerTypes.get(playerID - 1); // Ensure playerID is valid
        return supplier != null ? supplier.get() : null;
    }
     */

    public Player producePlayer() {
        return this.p;
    }

    public Enemy produceEnemy(char tile, Position p, Generator g, MessageCallBack cb, DeathCallBack dcb) {
        Enemy e = enemyTypes.get(tile).get();
        e.initialize(p, g, cb, dcb);
        return e;
    }

    public Tile produceEmpty(Position p){
        return new Empty().initialize(p);
    }

    public Tile produceWall(Position p){
        return new Wall().initialize(p);
    }

    public List<Supplier<Player>> getPlayerTypes() {
        return playerTypes;
    }
}