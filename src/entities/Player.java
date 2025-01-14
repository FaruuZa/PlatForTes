package entities;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.HelpMethods.*;

import java.util.HashMap; // Import HashMap
import java.util.Map; // Import Map

import levels.LevelOne;
import main.Game;

public class Player extends Entity {
    // private BufferedImage img;
    private Map<String, BufferedImage[]> animationsMap = new HashMap<>();
    private BufferedImage[] animations; // Define animations array
    private String playerAction = IDLE;
    public boolean right = false, left = false, mkiri = false, jump = false;
    private int aniTick, aniIndex, aniSpeed = Math.round(120 / GetSpriteFrame(playerAction));
    private boolean moving = false, attacking = false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;

    // hitbox
    private float xDrawOffset = 25 * Game.SCALE;
    private float yDrawOffset = 12 * Game.SCALE;

    // jump / gravity
    private float airSpeed = 0f;
    private float gravity = 0.06f * Game.SCALE;
    private float jumpSpeed = -2.8f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = true;

    public Player(float x, float y) {
        super(x, y, (int) (64* Game.SCALE), (int) (58 * Game.SCALE));
        loadAllAnimations(); // Load all animations at once
        initHitbox(x, y, (int) (24 * Game.SCALE), (int) (31 * Game.SCALE)); // Further reduce hitbox height
    }

    private void loadAllAnimations() {
        for (String action : new String[] { IDLE, RUN, ATTACK, JUMP_START, JUMP_END }) {
            BufferedImage img = LoadSave.getSpriteAtlas(action, LoadSave.PLAYER);
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
            animationsMap.put(action, actionAnimations);
        }
    }

    public void loadLvlData(LevelOne level) {
        this.lvlData = level.getLevelData();
    }

    public void render(Graphics g) {
        // update();
        if (animations != null) { 
            BufferedImage currentAnimation = animations[aniIndex];
            if (mkiri) {
                currentAnimation = flipImageHorizontally(currentAnimation); // Flip the image if facing left
            }
            g.drawImage(currentAnimation, (int) (hitbox.x - xDrawOffset),
                    (int) (hitbox.y - yDrawOffset),
                    width + (playerAction == ATTACK ? (this.width / 5) : 0), height + (playerAction == ATTACK ? 1 : 0),
                    null);
        } else {
            System.out.println("Animations not loaded.");
            System.out.println(playerAction);
        }
        // drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        try {
            // if (attacking) {
            //     aniSpeed = 10; // percepat
            // } else {
            //     aniSpeed = 30;
            // }
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= animations.length) {
                    if (inAir)
                        aniIndex = animations.length - 1;
                    else
                        aniIndex = 0;
                    attacking = false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating animation");
        }
    }

    private void updatePos() {
        moving = false;

        if (jump) {
            jump();
        }
        if (!right && !left && !inAir) {
            return;
        }

        float xSpeed = 0;

        if (right) {
            xSpeed += playerSpeed;
            if (!attacking)
                mkiri = false;
        } else if (left) {
            xSpeed -= playerSpeed;
            if (!attacking)
                mkiri = true;
        }

        if (!inAir) {
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            yDrawOffset = 30 * Game.SCALE;
            if (canMove(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPos(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;

                updateXPos(xSpeed);
            }
        } else {

            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        yDrawOffset = 12 * Game.SCALE;
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (canMove(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
    }

    private void setAnimation() {
        String startAni = playerAction;
        aniSpeed = Math.round(120 / GetSpriteFrame(playerAction));
        if (moving) {
            if (!playerAction.equals(RUN)) {
                playerAction = RUN;
            }
        } else {
            if (!playerAction.equals(IDLE)) {
                playerAction = IDLE;
            }
        }

        if (inAir) {
            if (airSpeed >= 0) { // Falling
                playerAction = JUMP_END;
            } else {
                playerAction = JUMP_START;
            }
        }

        if (attacking) {
            if (!playerAction.equals(ATTACK)) {
                playerAction = ATTACK;
            }
        }

        // Set the current animation based on playerAction
        if (startAni != playerAction) {
            aniIndex = 0; // Reset animation index
        }
        animations = animationsMap.get(playerAction);
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
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

    public void setAttacking(boolean atking) {
        this.attacking = atking;
    }

    public void resetDir() {
        left = false;
        right = false;
        attacking = false;
    }
}
