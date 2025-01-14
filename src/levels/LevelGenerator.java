package levels;

import java.util.Random;
import main.Game;

public class LevelGenerator {
    private static final int GROUND_TILE_1 = 1;
    private static final int GROUND_TILE_2 = 2;
    private static final int GROUND_TILE_3 = 3;
    private static final int EMPTY_TILE = 0;
    private static final int DECORATION_7 = 7;
    private static final int DECORATION_8 = 8;
    private static final int DECORATION_22 = 22;
    private static final int DECORATION_122 = 122;
    private static final int DECORATION_123 = 123;
    private static final int DECORATION_124 = 124;
    private static final int TILE_12 = 12;
    private static final int TILE_42 = 42;
    private static final int TILE_43 = 43;
    private static final int TILE_55 = 55;
    private static final int TILE_57 = 57;

    private static final int MIN_VERTICAL_GAP = 2;
    private static final float PLATFORM_START_PROBABILITY = 0.4f;
    private static final int PLATFORM_VERTICAL_SPACING = 4;
    private static final float SPECIAL_TILE_CHANCE = 0.3f;

    public int[][] generateLevel() {
        int[][] level = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        generateGroundAndPlatforms(level);
        addAboveGroundDecorations(level);
        ensureSafeStartingArea(level);
        return level;
    }

    private void generateGroundAndPlatforms(int[][] level) {
        for (int y = Game.TILES_IN_HEIGHT - 1; y >= 0; y--) {
            int platformLength = 0;
            for (int x = 0; x < Game.TILES_IN_WIDTH; x++) {
                if (y == Game.TILES_IN_HEIGHT - 1) {
                    // Ground layer
                    level[y][x] = x == 0 ? GROUND_TILE_1 : getNextGroundTile(level[y][x - 1], x, y, level);
                } else if ((y + 1) % PLATFORM_VERTICAL_SPACING == 0) {
                    int belowTileResult = checkTileBelowRules(level, x, y);
                    if (belowTileResult != EMPTY_TILE) {
                        level[y][x] = belowTileResult;
                        continue;
                    }
                    // Platform layer
                    if (shouldStartPlatform(level, x, y) || platformLength > 0) {
                        platformLength++;
                        level[y][x] = getNextPlatformTile(level, x, y, platformLength);
                        if (platformLength >= MAX_PLATFORM_LENGTH ||
                                (platformLength >= MIN_PLATFORM_LENGTH
                                        && random.nextFloat() > PLATFORM_CHAIN_PROBABILITY)) {
                            platformLength = 0;
                            x += 2; // Add gap
                        }
                    } else {
                        platformLength = 0;
                    }
                }
            }
        }
    }

    private int getNextPlatformTile(int[][] level, int x, int y, int length) {
        // Check tile below
        int tileBelow = y < Game.TILES_IN_HEIGHT - 1 ? level[y + 1][x] : EMPTY_TILE;
        switch (tileBelow) {
            case TILE_57:
                return random.nextFloat() < 0.5f ? TILE_43 : GROUND_TILE_1;
            case TILE_43:
                return random.nextFloat() < 0.5f ? TILE_43 : GROUND_TILE_1;
            case TILE_55:
                return random.nextFloat() < 0.5f ? TILE_42 : GROUND_TILE_3;
            case TILE_42:
                return random.nextFloat() < 0.5f ? TILE_42 : GROUND_TILE_3;
            case TILE_12:
                if (x > 0 && level[y][x - 1] == GROUND_TILE_1)
                    return GROUND_TILE_2;
                return TILE_12;
        }

        // Check left tile
        if (x > 0) {
            int leftTile = level[y][x - 1];
            switch (leftTile) {
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
            }
        }

        return length == 1 ? GROUND_TILE_1 : GROUND_TILE_2;
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
                if (chance < 0.33f) return TILE_12;
                if (chance < 0.66f) return TILE_42;
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

   private boolean shouldStartPlatform(int[][] level, int x, int y) {
        if (!hasEnoughVerticalSpace(level, x, y))
            return false;
        if (x == 0)
            return false;
        if (level[y][x - 1] != EMPTY_TILE)
            return false;
        return random.nextFloat() < PLATFORM_START_PROBABILITY;
    }

    private Random random = new Random();
    
    private int lastDecoTile;

    private boolean hasEnoughVerticalSpace(int[][] level, int x, int y) {
        // Check space above
        for (int i = 1; i <= MIN_VERTICAL_GAP; i++) {
            if (y - i >= 0 && level[y - i][x] != EMPTY_TILE) {
                return random.nextBoolean() ? false : true;
            }
        }
        return true;
    }

    private void addAboveGroundDecorations(int[][] level) {
        for (int y = 0; y < Game.TILES_IN_HEIGHT - 1; y++) {
            for (int currentX = 0; currentX < Game.TILES_IN_WIDTH; currentX++) {
                level[y][currentX] = getDecorationTile(y, currentX, level);
                lastDecoTile = level[y][currentX];
            }
            lastDecoTile = 0; // Reset for next row
        }
    }

    private int getDecorationTile(int y, int currentX, int[][] level) {
        if (!hasEnoughVerticalSpace(level, currentX, y)) {
            return EMPTY_TILE;
        }
        if (lastDecoTile == 0 && level[y][currentX] == EMPTY_TILE) {
            return generateDecorationTile();
        } else if (lastDecoTile == DECORATION_7) {
            return DECORATION_8;
        } else if (lastDecoTile == DECORATION_22) {
            return EMPTY_TILE; // Force empty space after 22
        } else if (lastDecoTile == DECORATION_122) {
            return random.nextBoolean() ? DECORATION_123 : DECORATION_124;
        } else if (lastDecoTile == DECORATION_123 || lastDecoTile == DECORATION_124) {
            return EMPTY_TILE; // Force empty space after 124
        }
        return EMPTY_TILE; // Default
    }

    private int generateDecorationTile() {
        float chance = random.nextFloat();
        if (chance < 0.2f)
            return DECORATION_7;
        if (chance < 0.3f)
            return DECORATION_22;
        if (chance < 0.4f)
            return DECORATION_122;
        return EMPTY_TILE; // Default
    }

    private void ensureSafeStartingArea(int[][] level) {
        level[Game.TILES_IN_HEIGHT - 2][0] = EMPTY_TILE;
        level[Game.TILES_IN_HEIGHT - 2][1] = EMPTY_TILE;
        level[Game.TILES_IN_HEIGHT - 2][2] = EMPTY_TILE;
        if (level[Game.TILES_IN_HEIGHT - 1][3] == EMPTY_TILE || level[Game.TILES_IN_HEIGHT - 1][2] == EMPTY_TILE ||
                level[Game.TILES_IN_HEIGHT - 1][1] == EMPTY_TILE) {
            generateLevel();
        } 
    }

    private static final float PLATFORM_CHAIN_PROBABILITY = 0.7f;
    private static final int MIN_PLATFORM_LENGTH = 3;
    private static final int MAX_PLATFORM_LENGTH = 6;
    
    private int checkTileBelowRules(int[][] level, int x, int y) {
        if (y >= Game.TILES_IN_HEIGHT - 1) return EMPTY_TILE;
        
        int tileBelow = level[y + 1][x];
        switch (tileBelow) {
            case TILE_57:
            case TILE_43:
                return random.nextFloat() < 0.5f ? TILE_43 : GROUND_TILE_1;
                
            case TILE_55:
            case TILE_42:
                return random.nextFloat() < 0.5f ? TILE_42 : GROUND_TILE_3;
                
            case TILE_12:
                if (x > 0) {
                    int leftTile = level[y][x-1];
                    return leftTile == GROUND_TILE_1 ? GROUND_TILE_2 : TILE_12;
                }
                return TILE_12;
                
            default:
                return EMPTY_TILE;
        }
    }
}
