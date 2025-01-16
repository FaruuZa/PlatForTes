package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.IOException; // Import IOException
import java.awt.image.BufferedImage; // Import BufferedImage
import levels.LevelManager; // Import LevelManager
import java.io.InputStream; // Import InputStream
import javax.imageio.ImageIO; // Import ImageIO

import main.Game;

public class LoadSave {

    public static final String PLAYER = "player";
    public static final String LEVEL = "level";
    public static final String LEVEL_ONE_DATA = "level_one_data";
    public static final String MENU_BUTTONS= "menu_buttons";
    public static final String MENU_BACKGROUND= "menu_background";
    public static final String PAUSE_BACKGROUND = "pause_background";
    public static final String SOUND_BUTTONS = "sound_buttons";
    public static final String URM_BUTTONS = "urm_buttons";
	public static final String VOLUME_BUTTONS = "volume_buttons";
	public static final String TILES = "tiles";
	public static final String GRASS_BACKGROUND = "wood_background";

    public static BufferedImage getSpriteAtlas(String file, String folder) {
        String path = "/res/" + folder + "/" + file + ".png";
        try (InputStream is = LoadSave.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Image not found: " + path);
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Error loading player atlas: " + e.getMessage());
            return null; // Return null if loading fails
        }
    }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA, LEVEL);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }

}
