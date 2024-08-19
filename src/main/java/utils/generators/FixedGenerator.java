package main.java.utils.generators;

public class FixedGenerator implements Generator {

    @Override
    public int generate(int value) {
        return value / 2;
    }
}
