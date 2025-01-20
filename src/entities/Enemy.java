package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Enemy extends Entity {

    protected int aniIndex, enemytype;
    protected int aniTick, aniSpeed;
    protected String enemyState = IDLE;
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed = 0f;
    protected float gravity = 0.04f * Game.SCALE;
    protected int arah = 1;
    protected float walkSpeed = 0.4f * Game.SCALE;
    protected int tileY;
    protected float attackRange = Game.TILES_SIZE / 2;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true, attackChecked = false;

    protected int maxFrame; // max frame of animation

    public Enemy(float x, float y, int width, int height, int enemytype) {
        super(x, y, width, height);
        this.enemytype = enemytype;
        initHitbox(x, y, width, height);
        maxHealth = GetMaxHP(enemytype);
        currentHealth = maxHealth;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.hitbox.y / Game.TILES_SIZE);

        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        }
        return false;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            arah = 1;
        else
            arah = 0;// 0 = left, 1 = right
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackRange * 10;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackRange;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }
    }

    protected void updateInAir(int[][] lvlData) {
        if (canMove(hitbox.x, hitbox.y + fallSpeed, width, height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPos(hitbox, fallSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE) + 1;
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (arah == 0) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        // if (canMove(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height,
        // lvlData)) {
        // hitbox.x += xSpeed;
        // return;
        // }
        // }

        if (!IsWall(hitbox, xSpeed, lvlData, arah)) {
            if (IsFloor(hitbox, xSpeed, lvlData, arah)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= maxFrame) {
                aniIndex = 0;// reset to first frame

                switch (enemyState) {
                    case ATTACK -> enemyState = IDLE; // kalo nemu yg hit tambahin
                    case DEAD -> active = false;
                }
            }
        }
    }

    public void update(int[][] lvlData) {

    }

    protected void changeWalkDir() {
        arah = (arah == 0) ? 1 : 0;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public String getEnemyState() {
        return enemyState;
    }

    protected void newState(String enemyState) {
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
        maxFrame = GetSpriteFrame(enemyState);
        aniSpeed = 120 / maxFrame;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);

    }

    public boolean isActive() {
        return active;
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetDamage(enemytype));
        attackChecked = true;// set true to prevent multiple hit detection
    }

    public void resetAll(int[][] lvlData) {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = false;
        inAir = false;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;

    }
}
