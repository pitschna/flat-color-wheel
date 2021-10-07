package ch.pitschna.flatcolorwheel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlatColorWheel {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("run like this:");
            System.out.println("java FlatColorWheel  <folder> ");
            return;
        }
        String fileFolder = args[0];

        System.out.println("Folder with picture: " + fileFolder);

        // put image together
        BufferedImage image = new BufferedImage(1536*3, 1536*3,
                BufferedImage.TYPE_INT_RGB);
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int x = 0; x < 1536; x++) {
            if (x < 256) {
                red = 255;
                green = x;
                blue = 0;
            } else if (x < 512) {
                red = getColorWhite(511, x);
                green = 255;
                blue = 0;
            } else if (x < 768) {
                red = 0;
                green = 255;
                blue = getColorWhite(x, 512);
            } else if (x < 1024) {
                red = 0;
                green = getColorWhite(1023, x);
                blue = 255;
            } else if (x < 1280) {
                red = getColorWhite(x, 1024);
                green = 0;
                blue = 255;
            } else {
                red = 255;
                green = 0;
                blue = getColorWhite(1535, x);
            }
            int redY = red;
            int greenY = green;
            int blueY = blue;
            for (int y = 0; y < 1536; y++) {
                if (y < 768) {
                    redY = getColorBlack(red, y);
                    greenY = getColorBlack(green, y);
                    blueY = getColorBlack(blue, y);
                } else {
                    redY = getColorWhite(255, getColorBlack(255 - red, 1536 - y));
                    greenY = getColorWhite(255, getColorBlack(255 - green, 1536 - y));
                    blueY = getColorWhite(255, getColorBlack(255 - blue, 1536 - y));
                }
                for (int i = 0; i < 3; i++)
                    for (int j = 0; j < 3; j++)
                        image.setRGB(x * 3 + i, y * 3 + j, getRgb(redY, greenY, blueY));
            }
        }

        String fileName = fileFolder + "/bla.jpg";
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        try {
            ImageIO.write(image, "jpg", new File(fileName.replace(".", timeStamp + ".")));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Done! \n\t" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    private static int getColorWhite(int i, int color) {
        return i - color;
    }

    //private static int getColorBlack(int color, int y) {
     //   return (color * y) / 768;
   // }
    private static int getColorBlack(int color, int y) {
        return (int) ((color * Math.sqrt(y)) / (Math.sqrt(768)));
    }

    private static int getRgb(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }
}
