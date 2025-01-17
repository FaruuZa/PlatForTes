package entities;

import java.awt.Graphics;
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
    private BufferedImage[] animationTauro;
    private ArrayList<Tauro> tauros = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        tauros = LoadSave.getTauro(playing.getLvlData());
        System.out.println("Tauro Count: " + tauros.size());
    }

    private void loadEnemyImages() {
        for (String action : new String[] { WALK, IDLE, IDLE2, DEAD, ATTACK }) {
            BufferedImage img = LoadSave.getSpriteAtlas(action, LoadSave.ENEMY_TAURO);
            System.out.println("Loading animation for action: " + action); // Debug statement
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

    public void update() {
        for (Tauro t : tauros)
            t.update();
    }

    public void render(Graphics g, int xLvlOffset) {
        drawTauros(g, xLvlOffset);
    }

    private void drawTauros(Graphics g, int xLvlOffset) {
        for (Tauro t : tauros)
            g.drawImage(animationsMapTauro.get(t.getEnemyState())[t.getAniIndex()], (int) t.hitbox.x - xLvlOffset, (int) t.hitbox.y,
                    (int) t.width, (int) t.height, null);
    }
}
