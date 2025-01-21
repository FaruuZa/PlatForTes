package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gamestates.Playing;
import static utilz.Constants.EnemyConstants.Tauro.*;
import utilz.LoadSave;

public class EnemyManager {

    private Playing playing;
    private Map<String, BufferedImage[]> animationsMapTauro = new HashMap<>();
    private ArrayList<Tauro> tauros = new ArrayList<>();
    private int enemyCounts;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        tauros = LoadSave.getTauro(playing.getLvlData());
        enemyCounts = tauros.size();
        System.out.println("Tauro Count: " + enemyCounts);
    }

    public boolean isEnemyClear() {
        for (Tauro tauro : tauros) {
            if (tauro.active) {
                return false;
            }
        }
        return true;
    }

    private void loadEnemyImages() {
        for (String action : new String[] { WALK, IDLE, HURT, DEAD, ATTACK }) {
            BufferedImage img = LoadSave.getSpriteAtlas(action, LoadSave.ENEMY_TAURO);
            // System.out.println("Loading animation for action: " + action); // Debug statement
            if (img == null) {
                System.err.println("Failed to load animation for action: " + action);
                continue; // Skip to the next action if loading fails
            }
            int spriteFrames = GetSpriteFrame(action);
            int[] spriteSize = GetSpriteSize(action);
            BufferedImage[] actionAnimations = new BufferedImage[spriteFrames];
            for (int i = 0; i < actionAnimations.length; i++) {
                int xOffset = i * spriteSize[0];
                if (xOffset + spriteSize[0] <= img.getWidth() && spriteSize[1] <= img.getHeight()) {
                    actionAnimations[i] = img.getSubimage(xOffset, 0, spriteSize[0], spriteSize[1]);
                } else {
                    System.err.println("Subimage exceeds image bounds for frame: " + i);
                }
            }
            animationsMapTauro.put(action, actionAnimations);
        }
    }

    public void update(int[][] lvlData, Player player) {
        if (isEnemyClear())
            playing.setLevelComplete(true);

        for (Tauro t : tauros)
            if (t.isActive())
                t.update(lvlData, player);
    }

    public void render(Graphics g, int xLvlOffset) {
        drawTauros(g, xLvlOffset);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Tauro t : tauros) {
            if (t.isActive())
                if (attackBox.intersects(t.getHitbox())) {
                    t.hurt(20);
                    return;
                }
        }
    }

    private void drawTauros(Graphics g, int xLvlOffset) {
        for (Tauro t : tauros) {
            if (t.isActive()) {
                if (t.arah == 1)
                    g.drawImage(animationsMapTauro.get(t.getEnemyState())[t.getAniIndex()],
                            (int) t.hitbox.x - xLvlOffset,
                            (int) t.hitbox.y,
                            (int) t.width, (int) t.height, null);
                if (t.arah == 0)
                    g.drawImage(flipImageHorizontally(animationsMapTauro.get(t.getEnemyState())[t.getAniIndex()]),
                            (int) t.hitbox.x - xLvlOffset,
                            (int) t.hitbox.y,
                            (int) t.width, (int) t.height, null);
                // t.drawAttackBox(g, xLvlOffset);
            }
        }

    }

    private BufferedImage flipImageHorizontally(BufferedImage imgAsal) {
        int width = imgAsal.getWidth();
        int height = imgAsal.getHeight();

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(x, y, imgAsal.getRGB(width - 1 - x, y));
            }
        }
        return flippedImage;
    }

    public void resetAll(int[][] lvlData) {
        for (Tauro tauro : tauros) {
            tauro.resetAll(lvlData);
        }
    }
}
