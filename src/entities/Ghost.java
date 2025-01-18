package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import main.Game;
import utilz.LoadSave;

public class Ghost extends Entity {
    private Player player;
    private boolean move = false;
    private boolean mKiri = false;
    private Map<String, BufferedImage[]> animationsMap = new HashMap<>();
    private BufferedImage[] images;
    private int aniTick, aniIndex, aniSpeed = 30;
    private String aksi = "";
    private boolean alive = false, dead = false;
    private int ghostSpeed = (int) (1 * Game.SCALE);

    public Ghost(float x, float y, Player player) {
        super(x, y, (int) (50 * Game.SCALE), (int) (50 * Game.SCALE));
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
        if (alive) {
            if (move) {
                updatePos();
            }
            updateAnimationTick();
            setAnimation();
        }
    }

    public void updatePos() {
        x = mKiri ? x - ghostSpeed : x + ghostSpeed;

    }

    private void updateAnimationTick() {
        aniTick++;
        if (aksi.equals("ghost"))
            aniSpeed = 30;
        else
            aniSpeed = 15;
        try {
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= images.length - 1) {
                    aniIndex = 0;
                    if (aksi == "ghost_dead") {
                        if (dead && alive) {
                            dead = false;
                            alive = false;
                            move = false;
                            player.moveToGhost();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating animation");
        }
    }

    public void render(Graphics g, int xLvlOffset) {
        if (isAlive()) {
            if (mKiri) {
                g.drawImage(flipImageHorizontally(images[aniIndex]), (int) (x + 6 * Game.SCALE) - xLvlOffset,
                        (int) (y - 6 * Game.SCALE),
                        (int) (34 * Game.SCALE),
                        (int) (34 * Game.SCALE), null);
            } else {
                g.drawImage(images[aniIndex], (int) (x - 6 * Game.SCALE) - xLvlOffset, (int) (y - 6 * Game.SCALE),
                        (int) (34 * Game.SCALE),
                        (int) (34 * Game.SCALE), null);
            }
        }
    }

    public void setAnimation() {
        String aksiTemp = aksi;

        if (isAlive())
            if (!aksi.equals("ghost"))
                aksi = "ghost";
        if (isDead() && isAlive())
            if (!aksi.equals("ghost_dead"))
                aksi = "ghost_dead";

        if (aksiTemp != aksi) {
            aniIndex = 0;
            images = animationsMap.get(aksi);
        }
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move, boolean mKiri) {
        if (alive)
            this.move = move;
    }

    public void spawnGhost(boolean mKiri) {
        if (!alive) {
            x = player.hitbox.x;
            y = player.hitbox.y;
            this.mKiri = mKiri;
            alive = true;
        }
    }

    public void killGhost() {
        if (alive)
            dead = true;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void resetAll() {
        aksi = "";
        alive = false;
        dead = false;
    }
}
