package levels;

import java.util.Random;
import main.Game;

public class LevelGenerator {
    private static final int GROUND_TILE_1 = 1;
    private static final int GROUND_TILE_2 = 2;
    private static final int GROUND_TILE_3 = 3;
    private static final int EMPTY_TILE = 0;
    private static final int TILE_12 = 12;
    private static final int TILE_42 = 42;
    private static final int TILE_43 = 43;
    private static final int TILE_55 = 55;
    private static final int TILE_57 = 57;

    private static final float SPECIAL_TILE_CHANCE = 0.2f;

    public int[][] generateLevel() {
        int[][] level = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        generateGroundAndPlatforms(level);
        return level;
    }

    private void generateGroundAndPlatforms(int[][] level) {
        for (int y = Game.TILES_IN_HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                if (y == Game.TILES_IN_HEIGHT - 1) {
                    // Ground layer
                    level[y][x] = x == 0 ? GROUND_TILE_1 : getNextGroundTile(level[y][x - 1], x, y, level);
                } else if (checkTileBelowRules(level, x, y) != EMPTY_TILE) {
                    int belowTileResult = checkTileBelowRules(level, x, y);
                    if (belowTileResult != EMPTY_TILE) {
                        level[y][x] = belowTileResult;
                        continue;
                    }
                }
            }
        }
    }

    private int getNextGroundTile(int previousTile, int x, int y, int[][] level) {
        // First check tile below rules
        int tileBelowResult = checkTileBelowRules(level, x, y);
        if (tileBelowResult != EMPTY_TILE) {
            return tileBelowResult;
        }

        // Then check horizontal sequence rules
        switch (previousTile) {
            case GROUND_TILE_1:
            case GROUND_TILE_2:
                return random.nextFloat() < SPECIAL_TILE_CHANCE ? TILE_57 : GROUND_TILE_2;

            case TILE_57:
                float chance = random.nextFloat();
                if (chance < 0.33f)
                    return TILE_12;
                if (chance < 0.66f)
                    return TILE_42;
                return TILE_55;

            case TILE_42:
                return EMPTY_TILE;

            case TILE_55:
                return random.nextFloat() < 0.5f ? GROUND_TILE_2 : GROUND_TILE_3;

            case TILE_12:
                return random.nextFloat() < 0.5f ? TILE_12 : TILE_42;

            default:
                return GROUND_TILE_1;
        }
    }

    private Random random = new Random();

    private int checkTileBelowRules(int[][] level, int x, int y) {
        if (y >= Game.TILES_IN_HEIGHT - 1)
            return EMPTY_TILE;

        int tileBelow = level[y + 1][x];
        switch (tileBelow) {
            case TILE_57:
                return random.nextFloat() < 0.5f ? TILE_43 : GROUND_TILE_1;
            case TILE_43:
                return random.nextFloat() < 0.5f ? TILE_43 : GROUND_TILE_1;
            case TILE_55:
                if (x > 0) {
                    int leftTile = level[y][x - 1];
                    if (leftTile == TILE_43 || leftTile == TILE_12)
                        return TILE_42;
                    if (leftTile == GROUND_TILE_1 || leftTile == GROUND_TILE_2)
                        return GROUND_TILE_3;
                }
                return TILE_42;
            case TILE_42:
                if (x > 0) {
                    int leftTile = level[y][x - 1];
                    if (leftTile == GROUND_TILE_1 || leftTile == GROUND_TILE_2)
                        return GROUND_TILE_3;
                }
                return random.nextFloat() < 0.5f ? TILE_42 : GROUND_TILE_3;
            case TILE_12:
                if (x > 0) {
                    int leftTile = level[y][x - 1];
                    if (leftTile == GROUND_TILE_1 || leftTile == GROUND_TILE_2)
                        return GROUND_TILE_2;
                }
                return TILE_12;
            default:
                return EMPTY_TILE;
        }
    }
}
