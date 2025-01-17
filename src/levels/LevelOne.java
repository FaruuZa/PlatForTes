package levels;

import main.Game;

public class LevelOne {
    private LevelManager levelManager;
    private LevelGenerator levelGenerator = new LevelGenerator();
    private int[][] levelData; // 2D array to represent the level layout

    public LevelOne(LevelManager levelManager) {
        this.levelManager = levelManager;
        levelData = levelGenerator.generateLevel(100, Game.TILES_IN_HEIGHT);
    }

    public int[][] getLevelData() {
        return levelData;
    }
}
