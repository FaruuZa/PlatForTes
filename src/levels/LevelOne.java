package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;

public class LevelOne {
    private LevelManager levelManager;
    private LevelGenerator levelGenerator = new LevelGenerator();
    private int[][] levelData; // 2D array to represent the level layout

    public LevelOne(LevelManager levelManager) {
        this.levelManager = levelManager;
        levelData = levelGenerator.generateLevel();
    }

    public int[][] getLevelData() {
        // Example level data (0 represents empty space, 1 represents a tile)
        // return levelData = new int[][] {
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0, 0 },
        // { 55, 56, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0, 0 },
        // { 42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 164, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0, 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 },
        // { 55, 56, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        // 2, 3 }
        // };
        return levelData;
    }

    private static final Random random = new Random();

    private int lastGroundTile = 0;
    private int lastDecoTile = 0;

    private int[][] generateLevel() {
        int[][] level = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];

        // Generate ground layer with variations
        int currentX = 0;
        while (currentX < Game.TILES_IN_WIDTH) {
            switch (lastGroundTile) {
                case 0:
                    // level[Game.TILES_IN_HEIGHT - 1][currentX] = random.nextFloat() < 0.4f ? 1 :
                    // 2;
                    level[Game.TILES_IN_HEIGHT - 1][currentX] = 1;
                    if (currentX > 2) {
                        if (level[Game.TILES_IN_HEIGHT - 1][currentX - 1] != 0) {
                            level[Game.TILES_IN_HEIGHT - 1][currentX] = random.nextFloat() < 0.7f ? 1 : 0;
                        } else if (level[Game.TILES_IN_HEIGHT - 1][currentX - 2] == 0) {
                            level[Game.TILES_IN_HEIGHT - 1][currentX] = 1;
                        } else {
                            level[Game.TILES_IN_HEIGHT - 1][currentX] = 0;
                        }
                    }
                    break;
                case 1:
                    level[Game.TILES_IN_HEIGHT - 1][currentX] = random.nextFloat() < 0.7f ? 2 : 3;
                    break;
                case 2:
                    level[Game.TILES_IN_HEIGHT - 1][currentX] = random.nextFloat() < 0.7f ? 2 : 3;
                    break;
                case 3:

                    level[Game.TILES_IN_HEIGHT - 1][currentX] = 0;
                    break;
            }
            lastGroundTile = level[Game.TILES_IN_HEIGHT - 1][currentX];
            currentX++;
        }

        // Add above-ground decorations
        for (int y = 0; y < Game.TILES_IN_HEIGHT - 1; y++) {
            currentX = 0;
            while (currentX < Game.TILES_IN_WIDTH) {
                if (lastDecoTile == 0 && level[y][currentX] == 0) {
                    // Can place 7, 22, or 122
                    float chance = random.nextFloat();
                    if (chance < 0.2f)
                        level[y][currentX] = 7;
                    else if (chance < 0.3f)
                        level[y][currentX] = 22;
                    else if (chance < 0.4f)
                        level[y][currentX] = 122;
                    else if (chance < 1f)
                        level[y][currentX] = 0;
                } else if (lastDecoTile == 7) {
                    level[y][currentX] = 8;
                } else if (lastDecoTile == 22) {
                    level[y][currentX] = 0; // Force empty space after 22
                } else if (lastDecoTile == 122) {
                    level[y][currentX] = random.nextBoolean() ? 123 : 124;
                } else if (lastDecoTile == 123) {
                    level[y][currentX] = random.nextBoolean() ? 123 : 124;
                } else if (lastDecoTile == 124) {
                    level[y][currentX] = 0; // Force empty space after 124
                } 
                lastDecoTile = level[y][currentX];
                currentX++;
            }
            lastDecoTile = 0; // Reset for next row
        }

        // Ensure safe starting area
        level[Game.TILES_IN_HEIGHT - 2][0] = 0;
        level[Game.TILES_IN_HEIGHT - 2][1] = 0;
        level[Game.TILES_IN_HEIGHT - 2][2] = 0;
        level[Game.TILES_IN_HEIGHT - 1][0] = 1;
        level[Game.TILES_IN_HEIGHT - 1][1] = 2;
        if (level[Game.TILES_IN_HEIGHT - 1][3] != 0)
            level[Game.TILES_IN_HEIGHT - 1][2] = 2;
        else
            level[Game.TILES_IN_HEIGHT - 1][2] = 3;
        return level;
    }

}
