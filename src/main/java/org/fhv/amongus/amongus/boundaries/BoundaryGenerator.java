package org.fhv.amongus.amongus.boundaries;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.*;
import java.util.List;

public class BoundaryGenerator {

    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources/shipmask.png");
        BufferedImage image = ImageIO.read(file);
        int width = image.getWidth();
        int height = image.getHeight();

        List<List<Integer>> result = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            List<Integer> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y), true);
                if (color.getRed() == 0 && color.getGreen() == 255 && color.getBlue() == 0) {
                    row.add(x);
                }
            }
            if (!row.isEmpty()) {
                result.add(row);
            }
        }

        String jsonResult = new Gson().toJson(result);
        Path path = Paths.get("mapBounds.json");
        Files.write(path, Collections.singleton(jsonResult), StandardCharsets.UTF_8);
    }
}

