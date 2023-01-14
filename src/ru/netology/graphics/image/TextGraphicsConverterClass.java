package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

import static java.lang.Math.max;

public class TextGraphicsConverterClass implements TextGraphicsConverter {
    private int widthSet; // ширина картинки заданная сеттером
    private int heightSet; // высота картинки заданная сеттером
    private double maxRatio;
    private TextColorSchema schema;

    public TextGraphicsConverterClass() {
        schema = new TextColorSchemaClass();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        // скачаем картинку из интернета
        BufferedImage img = ImageIO.read(new URL(url));

        int widthDef = img.getWidth(); // ширина картинки-оригинала
        int heightDef = img.getHeight(); // высота картинки-оригинала

        // проверка на максимально допустимое соотношение сторон изображения
        if (maxRatio > 0 && ((double) widthDef / heightDef > maxRatio)) {
            throw new BadImageSizeException((double) widthDef / heightDef, maxRatio);
        }

        int widthNew; // ширина картинки новая
        int heightNew; // высота картинки новая

        // проверка на максимально допустимые ширину и/или высоту
        if (widthSet < widthDef && heightSet < heightDef && widthSet != 0 && heightSet != 0) {
            double widthRatio = (double) widthDef / widthSet;
            double heightRatio = (double) heightDef / heightSet;
            widthNew = (int) (widthDef / max(widthRatio, heightRatio));
            heightNew = (int) (heightDef / max(widthRatio, heightRatio));
        } else if (widthSet != 0 && widthSet < widthDef && heightSet == 0) {
            widthNew = widthSet;
            heightNew = heightDef * widthSet / widthDef;
        } else if (widthSet == 0 && heightSet != 0 && heightSet < heightDef) {
            widthNew = widthDef * heightSet / heightDef;
            heightNew = heightSet;
        } else {
            widthNew = widthDef;
            heightNew = heightDef;
        }

        /*подробно
        if (widthSet == 0 && heightSet == 0) {
            widthNew = widthDef;
            heightNew = heightDef;
        } else if (widthSet !=0 && widthSet < widthDef && heightSet == 0) {
            widthNew = widthSet;
            heightNew = heightDef * widthSet / widthDef;
        } else if (widthSet == 0 && heightSet != 0 && heightSet < heightDef) {
            widthNew = widthDef * heightSet / heightDef;
            heightNew = heightSet;
        } else if (widthSet < widthDef && heightSet < heightDef) {
            double widthRatio = widthDef / widthSet;
            double heightRatio = heightDef / heightSet;
            widthNew = (int) (widthDef / max(widthRatio, heightRatio));
            heightNew = (int) (heightDef / max(widthRatio, heightRatio));
        } else {
            widthNew = widthDef;
            heightNew = heightDef;
        }
        */

        Image scaledImage = img.getScaledInstance(widthNew, heightNew, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(widthNew, heightNew, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        WritableRaster bwRaster = bwImg.getRaster();

        char[][] symbolsArr = new char[heightNew][widthNew];

        for (int h = 0; h < heightNew; h++) {
            for (int w = 0; w < widthNew; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                //запоминаем символ c в двумерном массиве
                symbolsArr[h][w] = c;
            }
        }

        // осталось собрать все символы в один большой текст - лучше через 2 повторяющихся символа
        StringBuilder sb = new StringBuilder();
        for (char[] chars : symbolsArr) {
            for (char aChar : chars) {
                sb.append(aChar);
                sb.append(aChar);
            }
            sb.append("\n");
        }

        // возвращаем собранный текст
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int widthSet) {
        this.widthSet = widthSet;
    }

    @Override
    public void setMaxHeight(int heightSet) {
        this.heightSet = heightSet;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}