package entities;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.HelpMethods.*;

import java.util.HashMap; // Import HashMap
import java.util.Map; // Import Map

import gamestates.Playing;
import levels.LevelOne;
import main.Game;

public class Player extends Entity {
    // private BufferedImage img;
    private Map<String, BufferedImage[]> animationsMap = new HashMap<>();
    private BufferedImage[] animations; // Define animations array
    private String playerAction = IDLE;
    public boolean right = false, left = false, mkiri = false, jump = false, dead = false;
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

    // ghost
    private Ghost clone;

    // UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 10;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // attack box
    private Rectangle2D.Float attackBox;

    private boolean attackChecked = false;
    private Playing playing;

    public Player(float x, float y, Playing playing) {
        super(x, y, (int) (64 * Game.SCALE), (int) (58 * Game.SCALE));
        loadAllAnimations(); // Load all animations at once
        initHitbox(x, y, (int) (24 * Game.SCALE), (int) (31 * Game.SCALE)); // Further reduce hitbox height
        initAttackBox();
        this.playing = playing;
        clone = new Ghost(x, y, this);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE), (int) (45 * Game.SCALE));
    }

    private void loadAllAnimations() {
        for (String action : new String[] { IDLE, RUN, ATTACK, JUMP_START, JUMP_END, DEAD }) {
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
        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR, LoadSave.PLAYER);
    }

    public void loadLvlData(int[][] level) {
        this.lvlData = level;
    }

    public void render(Graphics g, int xLvlOffset) {
        if (animations != null) {
            BufferedImage currentAnimation = animations[aniIndex];
            if (mkiri) {
                currentAnimation = flipImageHorizontally(currentAnimation); // Flip the image if facing left
            }
            clone.render(g, xLvlOffset);
            g.drawImage(currentAnimation, (int) (hitbox.x - xDrawOffset) - xLvlOffset,
                    (int) (hitbox.y - yDrawOffset),
                    width + (playerAction == ATTACK ? (this.width / 5) : 0),
                    height + (playerAction == ATTACK ? 1 : 0),
                    null);
        } else {
            System.out.println("Animations not loaded.");
            System.out.println(playerAction);
        }
        // drawHitbox(g);
        drawUI(g);
        // drawAttackBox(g, xLvlOffset);
    }

    private void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        try {
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= animations.length) {
                    if (inAir)
                        aniIndex = animations.length - 1;
                    else
                        aniIndex = 0;
                    attacking = false;
                    attackChecked = false;
                    if (dead) {
                        aniIndex = animations.length - 1;
                        playing.setGameOver(true);
                    }
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
            else {
                xSpeed /= 2;
            }
        } else if (left) {
            xSpeed -= playerSpeed;
            if (!attacking)
                mkiri = true;
            else {
                xSpeed /= 2;
            }
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {
            yDrawOffset = 30 * Game.SCALE;
            if (canMove(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                // if (enabled)
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPos(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                // if (enable)
                updateXPos(xSpeed);
                if (hitbox.y + hitbox.height >= Game.GAME_HEIGHT - 10) {
                    changeHealth(-999);
                }
            }
        } else {
            // if (enable)
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

    public void changeHealth(int value) {
        if (dead)
            return;
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;
            // gameOver
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;

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
            playerAction = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if (dead) {
            playerAction = DEAD;
            attacking = false;
            jump = false;
            moving = false;
        }

        // Set the current animation based on playerAction
        if (startAni != playerAction) {
            aniIndex = 0; // Reset animation index
        }
        animations = animationsMap.get(playerAction);
    }

    public void update() {

        if (!dead) {
            updateHealthBar();
            updateAttackBox();

            updatePos();
            if (attacking)
                checkAttack();
            clone.update();
        }
        setAnimation();
        updateAnimationTick();
    }

    private void checkAttack() {
        if (!attackChecked) {
            if (aniIndex == 1 || aniIndex == 5) {
                playing.checkEnemyHit(attackBox);
                attackChecked = true;
            }
        }
    }

    private void updateAttackBox() {
        if (!mkiri) {
            attackBox.x = hitbox.x;
        } else if (mkiri) {
            attackBox.x = hitbox.x - hitbox.width;
        }

        attackBox.y = hitbox.y - (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
        if (currentHealth <= 0) {
            dead = true;
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

    public void setAttacking(boolean atking) {
        this.attacking = atking;
    }

    public void resetDir() {
        left = false;
        right = false;
        attacking = false;
    }

    public void moveGhost() {
        if (clone.isAlive()) {
            if (clone.isMove()) {
                clone.setMove(false, mkiri);
            } else {
                clone.setMove(true, mkiri);
            }
        }
    }

    public void moveToGhost() {
        if (canMove(clone.x, clone.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x = clone.x;
            hitbox.y = clone.y;
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        }
    }

    public void ghostSkill() {
        if (clone.isAlive() && !clone.isDead()) {
            clone.killGhost();
        } else if (!clone.isAlive() && !clone.isDead()) {
            clone.spawnGhost(mkiri);
        }
    }

    public void resetAll() {
        resetDir();
        resetInAir();
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;
        dead = false;

        clone.resetAll();

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

}
