package ru.netology.graphics.image;

public class TextColorSchemaClass implements TextColorSchema {
    protected char[] symbols = {'¶', 'ß', '0', 'k', 'y', 'L', '²', '.'};
    //                          0-31     64-95    128-159   192-223
    //                              32-63     96-127   160-191   224-255

    public TextColorSchemaClass() {
    }

    @Override
    public char convert(int color) {
        return symbols[color / 32];
        /*подробно
        if (color >= 0 && color < 32) {
            return colorSymbol[0];
        } else if (color >= 32 && color < 64) {
            return colorSymbol[1];
        } else if (color >= 64 && color < 96) {
            return colorSymbol[2];
        } else if (color >= 96 && color < 128) {
            return colorSymbol[3];
        } else if (color >= 128 && color < 160) {
            return colorSymbol[4];
        } else if (color >= 160 && color < 192) {
            return colorSymbol[5];
        } else if (color >= 192 && color < 224) {
            return colorSymbol[6];
        } else {
            return colorSymbol[7];
        }
        */
    }
}