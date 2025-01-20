package levels;

import java.awt.Graphics; // Import Graphics
import java.awt.image.BufferedImage;
import java.util.ArrayList; // Import ArrayList
import java.util.HashMap; // Import HashMap
import java.util.List; // Import List
import java.util.Map; // Import Map

import main.Game;
import utilz.LoadSave;

public class LevelManager {
    private Game game;
    // private LevelOne levelOne = new LevelOne(this);
    private LevelGenerator levelGenerator;

    // New asset map
    private Map<String, List<BufferedImage>> assetMap = new HashMap<>();
    private int[][] levelData;

    public LevelManager(Game game) {
        this.game = game;
        this.levelGenerator = new LevelGenerator();
        loadAssets(); // Load assets when the LevelManager is initialized
        loadLevelData();
    }

    private void loadLevelData() {
        levelData = levelGenerator.generateLevel(100, Game.TILES_IN_HEIGHT);
    }

    public int[][] getLevelData() {
        return levelData;
    }

    private void loadAssets() {
        // Initialize the asset lists in the map
        String[] assetNames = { "Tiles" };
        for (String assetName : assetNames) {
            assetMap.put(assetName, new ArrayList<>());
            // Load the assets using LoadSave.getSpriteAtlas
            BufferedImage img = LoadSave.getSpriteAtlas(assetName.toLowerCase(), LoadSave.LEVEL);
            if (img != null) {
                // Assuming each asset is a grid of sprites, slice them
                int spriteWidth = 32; // Example width of each sprite
                int spriteHeight = 32; // Example height of each sprite
                int rows = img.getHeight() / spriteHeight;
                int cols = img.getWidth() / spriteWidth;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        BufferedImage sprite = img.getSubimage(col * spriteWidth, row * spriteHeight, spriteWidth,
                                spriteHeight);
                        if (isImageTransparent(sprite)) {
                            continue;
                        }
                        assetMap.get(assetName).add(sprite);
                    }
                }
            } else {
                System.err.println("Failed to load assets for: " + assetName);
            }
        }
    }

    public boolean isImageTransparent(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if ((img.getRGB(x, y) >> 24) != 0x00) { // Check alpha value
                    return false; // Found a non-transparent pixel
                }
            }
        }
        return true; // All pixels are transparent
    }

    public BufferedImage getTile(int index) {
        // Assuming you have a method to get the tile from the assetMap
        return assetMap.get("Tiles").get(index - 1); // Adjust index as needed
    }

    public void render(Graphics g, int xLvlOffset) {
        for (int row = 0; row < levelData.length; row++) {
            for (int col = 0; col < levelData[row].length; col++) {
                int tileIndex = levelData[row][col];
                if (tileIndex > 0) { // If it's not empty
                    BufferedImage tile = getTile(tileIndex); // Assuming a method to get the tile
                    g.drawImage(tile, Game.TILES_SIZE * col - xLvlOffset, row * Game.TILES_SIZE, Game.TILES_SIZE,
                            Game.TILES_SIZE,
                            null); // Adjust positions as needed
                }
            }
        }
    }

    public void update() {
        // Update logic here
    }

    public int[][] getCurrentLevel() {
        return levelData;
    }
}
