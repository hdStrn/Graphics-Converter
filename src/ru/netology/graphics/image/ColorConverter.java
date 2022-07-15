package ru.netology.graphics.image;

public class ColorConverter implements TextColorSchema {

    private char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};
    @Override
    public char convert(int color) {
        return symbols[color / 32];
    }
}
