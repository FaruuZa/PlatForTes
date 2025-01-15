package entities;

import static utilz.Constants.PlayerConstants.ATTACK;
import static utilz.Constants.PlayerConstants.GetSpriteFrame;
import static utilz.Constants.PlayerConstants.GetSpriteSize;
import static utilz.Constants.PlayerConstants.IDLE;
import static utilz.Constants.PlayerConstants.JUMP_END;
import static utilz.Constants.PlayerConstants.JUMP_START;
import static utilz.Constants.PlayerConstants.RUN;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Ghost extends Entity {
    private Player player;
    private boolean move = false;
    private boolean mKiri = false;
    private Map<String, BufferedImage[]> animationsMap = new HashMap<>();
    private BufferedImage[] images;
    private int aniTick, aniIndex, aniSpeed = 30;
    public String aksi = "ghost";
    public boolean a = false, b = true;
    private int ghostSpeed = (int) (1 * Game.SCALE);

    public Ghost(float x, float y, Player player) {
        super(x, y, (int) (50 * Game.SCALE), (int) (50 * Game.SCALE));
        initHitbox(x, y, (int) (24 * Game.SCALE), (int) (31 * Game.SCALE)); // Further reduce hitbox height
        loadAllAnimations();
        this.player = player;
    }

    private void loadAllAnimations() {
        for (String action : new String[] { "ghost", "ghost_dead" }) {
            BufferedImage img = LoadSave.getSpriteAtlas(action, LoadSave.PLAYER);
            System.out.println("Loading animation for action: " + action); // Debug statement
            if (img == null) {
                System.err.println("Failed to load animation for action: " + action);
                continue; // Skip to the next action if loading fails
            }
            int spriteFrames = 12;
            int[] spriteSize = { 128, 128 };
            BufferedImage[] actionAnimations = new BufferedImage[spriteFrames];
            for (int i = 0; i < actionAnimations.length; i++) {
                int xOffset = i * spriteSize[0];
                if (xOffset + spriteSize[0] <= img.getWidth() && spriteSize[1] <= img.getHeight()) {
                    actionAnimations[i] = img.getSubimage(xOffset, 0, spriteSize[0], spriteSize[1]);
                } else {
                    System.err.println("Subimage exceeds image bounds for frame: " + i);
                }
            }
            animationsMap.put(action, actionAnimations);
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

    public void update() {
        if (move) {
            updatePos();
            updateHitbox();
        }
        setAnimation();
        updateAnimationTick();
    }

    public void updatePos() {
        x = mKiri ? x - ghostSpeed : x + ghostSpeed;
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aksi.equals("ghost"))
            aniSpeed = 30;
        else
            aniSpeed = 10;
        try {
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= images.length - 1) {
                    aniIndex = 0;
                    if (aksi.equals("ghost_dead")) {
                        aksi = "ghost";
                        player.moveToGhost();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating animation");
        }
    }

    public void render(Graphics g) {
        if (move || aksi.equals("ghost_dead")) {
            if (mKiri) {
                g.drawImage(flipImageHorizontally(images[aniIndex]), (int) x, (int) y, (int) (32 * Game.SCALE),
                        (int) (32 * Game.SCALE), null);
            } else {
                g.drawImage(images[aniIndex], (int) x, (int) y, (int) (32 * Game.SCALE), (int) (32 * Game.SCALE), null);
            }
        }
    }

    public void setAnimation() {
        switch (aksi) {
            case "ghost":
                images = animationsMap.get("ghost");
                break;
            case "ghost_dead":
                images = animationsMap.get("ghost_dead");
                break;
            default:
                break;
        }
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move, boolean mKiri) {
        aniIndex = 0;
        this.move = move;
        if (move) {
            x = player.hitbox.x;
            y = player.hitbox.y;
            aksi = "ghost";
            this.mKiri = mKiri;
        } else {
            aksi = "ghost_dead";
        }
    }
}
