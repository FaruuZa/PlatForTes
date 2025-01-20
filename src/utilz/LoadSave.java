package utilz;

import java.awt.image.BufferedImage;

import java.io.IOException; // Import IOException
import java.io.InputStream; // Import InputStream
import javax.imageio.ImageIO; // Import ImageIO

import entities.Tauro;
import main.Game;
import java.util.ArrayList;

public class LoadSave {

    public static final String PLAYER = "player";
    public static final String LEVEL = "level";
    public static final String LEVEL_ONE_DATA = "level_one_data";
    public static final String MENU_BUTTONS = "menu_buttons";
    public static final String MENU_BACKGROUND = "menu_background";
    public static final String PAUSE_BACKGROUND = "pause_background";
    public static final String SOUND_BUTTONS = "sound_buttons";
    public static final String URM_BUTTONS = "urm_buttons";
    public static final String VOLUME_BUTTONS = "volume_buttons";
    public static final String TILES = "tiles";
    public static final String MENU_BG_IMAGE = "wood_background";
    public static final String PLAYING_BG_IMAGE = "1bg";
    public static final String PLAYING_BG_IMAGE2 = "2bg2";
    public static final String PLAYING_BG_WATER = "3water";
    public static final String PLAYING_BG_ROCKB = "4rockback";
    public static final String PLAYING_BG_ROCKF = "5rockfront";
    public static final String ENEMY_TAURO = "enemies/tauro";
    public static final String STATUS_BAR = "health_power_bar";
    public static final String COMPLETED_IMG = "completed_sprite";

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

    public static ArrayList<Tauro> getTauro(int[][] lvlData) {
        ArrayList<Tauro> tauros = new ArrayList<>();
        for (int y = 0; y < lvlData.length; y++) {
            for (int x = 0; x < lvlData[y].length; x++) {
                if (lvlData[y][x] == -2)
                    tauros.add(new Tauro(x * Game.TILES_SIZE, y * Game.TILES_SIZE, (int) (42 * Game.SCALE),
                            (int) (50 * Game.SCALE)));
            }
        }
        return tauros;
    }

    public static int[] getPlayerPos(int[][] lvlData) {
        int xPos=10, yPos=10;
        for (int y = 0; y < lvlData.length; y++) {
            for (int x = 0; x < lvlData[y].length; x++) {
                if (lvlData[y][x] == -1) {
                    xPos = x * Game.TILES_SIZE;
                    yPos = y * Game.TILES_SIZE;
                }
            }
        }
        return new int[]{yPos, xPos};
    }

}
