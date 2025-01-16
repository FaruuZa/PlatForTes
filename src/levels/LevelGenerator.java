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
    private static final int TILE_22 = 22;
    private static final int TILE_144 = 144;
    private static final int TILE_145 = 145;
    private static final int TILE_146 = 146;

    private static final float SPECIAL_TILE_CHANCE = 0.2f;
    private int maxHeight, maxWidth;

    public int[][] generateLevel(int width, int height) {
        this.maxHeight = height;
        this.maxWidth = width;
        int[][] level = new int[maxHeight][maxWidth];
        generateGroundAndPlatforms(level);
        return level;
    }

    private void generateGroundAndPlatforms(int[][] level) {
        for (int y = maxHeight - 1; y >= 0; y--) {
            for (int x = 0; x < maxWidth; x++) {
                if (y == maxHeight - 1) {
                    // Ground layer
                    int empChance = random.nextFloat() < 0.5 ? EMPTY_TILE : GROUND_TILE_1;
                    level[y][x] = x == 0 ? empChance : getNextGroundTile(level[y][x - 1], x, y, level);
                } else if (checkTileBelowRules(level, x, y) != -1) {
                    int belowTileResult = checkTileBelowRules(level, x, y);
                    if (belowTileResult != EMPTY_TILE) {
                        level[y][x] = belowTileResult;
                        continue;
                    }
                } else {
                    level[y][x] = getNextPlat(level, x, y);
                }
            }
        }
    }

    private int getNextPlat(int[][] level, int x, int y) {
        if (x > 0) {
            int leftTile = level[y][x - 1];
            if (leftTile != EMPTY_TILE)
                switch (leftTile) {
                    case TILE_22:
                        break;
                    case TILE_144:
                        if (x < maxWidth - 1) {
                            int diagBelow = level[y + 1][x + 1];
                            if (diagBelow != EMPTY_TILE)
                                return TILE_146;
                            return random.nextFloat() < 0.6f ? TILE_145 : TILE_146;
                        }
                    case TILE_145:
                        if (x < maxWidth - 1) {
                            int diagBelow = level[y + 1][x + 1];
                            if (diagBelow != EMPTY_TILE)
                                return TILE_146;
                            return random.nextFloat() < 0.5f ? TILE_145 : TILE_146;
                        }
                    case TILE_146:
                        break;
                }
            else {
                if (y < maxHeight - 3 && y > 0) {
                    int bot2 = level[y + 3][x];
                    int bot3 = level[y + 2][x];
                    if (bot2 == EMPTY_TILE && bot3 == EMPTY_TILE) {
                        if (x < maxWidth - 1) {
                            int diagBelow = level[y + 1][x + 1];
                            if (diagBelow == EMPTY_TILE) {
                                float chance = random.nextFloat();
                                if (chance < 0.1f)
                                    return TILE_22;
                                if (chance < 0.25f)
                                    return TILE_144;
                                return EMPTY_TILE;
                            }
                        }
                    }
                }
            }
        }

        return EMPTY_TILE;
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
            case GROUND_TILE_3:
                return EMPTY_TILE;
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
        if (y >= maxHeight - 1)
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
                return TILE_42;
            case TILE_12:
                if (x > 0) {
                    int leftTile = level[y][x - 1];
                    if (leftTile == GROUND_TILE_1 || leftTile == GROUND_TILE_2)
                        return GROUND_TILE_2;
                }
                return TILE_12;
            case TILE_22:
                return EMPTY_TILE;
            case TILE_144:
                return EMPTY_TILE;
            case TILE_145:
                return EMPTY_TILE;
            case TILE_146:
                return EMPTY_TILE;
            default:
                return -1;
        }
    }
}
