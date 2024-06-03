package org.fhv.amongus.movementservice;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoundaryGenerator {

    public static void main(String[] args) throws IOException {
        File file = new File("movement-service/src/main/resources/ghostMode_shipmask.png");

        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();
        System.out.println("Image dimensions: " + width + "x" + height);

        List<List<Integer>> result = new ArrayList<>();

        // Define the color range for green
        int minRed = 0, maxRed = 35;
        int minGreen = 245, maxGreen = 255;
        int minBlue = 0, maxBlue = 8;

        for (int y = 0; y < height; y++) {
            List<Integer> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y), true);

                if (color.getRed() >= minRed && color.getRed() <= maxRed &&
                        color.getGreen() >= minGreen && color.getGreen() <= maxGreen &&
                        color.getBlue() >= minBlue && color.getBlue() <= maxBlue) {
                    row.add(x);
                }
            }
            if (!row.isEmpty()) {
                result.add(row);
            }
        }

        String jsonResult = new Gson().toJson(result);
        Path path = Paths.get("mapBoundsGhostMode.json");
        Files.write(path, Collections.singleton(jsonResult), StandardCharsets.UTF_8);
    }
}
