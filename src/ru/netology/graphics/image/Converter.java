package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {

    private double maxRatio = 99999;
    private int maxWidth = 99999;
    private int maxHeight = 99999;
    private TextColorSchema schema = new ColorConverter();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int width = img.getWidth();
        int height = img.getHeight();
        int ratio = Math.max(width, height) / Math.min(width, height);
        if (ratio > maxRatio) throw new BadImageSizeException(ratio, maxRatio);
        double coeff = 1;
        if (width > maxWidth || height > maxHeight) {
            coeff = Math.max((double) width / maxWidth, (double) height / maxHeight);
        }
        int newWidth = (int) Math.round(width / coeff);
        int newHeight = (int) Math.round(height / coeff);
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder picture = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                picture.append(c).append(c);
            }
            picture.append("\n");
        }
        return picture.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
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
