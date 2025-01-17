package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;
import static utilz.HelpMethods.*;

import java.awt.Graphics;

import main.Game;
import utilz.Constants.EnemyConstants.Tauro;

public abstract class Enemy extends Entity {

    private int aniIndex, enemytype;
    private int aniTick, aniSpeed;
    private String enemyState = IDLE;
    private boolean firstUpdate = true;
    private boolean inAir = false;
    private float fallSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private int arah = 1;
    private float walkSpeed = 1.0f * Game.SCALE;

    private int maxFrame; // max frame of animation

    public Enemy(float x, float y, int width, int height, int enemytype) {
        super(x, y, width, height);
        this.enemytype = enemytype;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= maxFrame) {
                aniIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimation();
        updateHitbox();
        updateAnimationTick();
    }

    private void updateAnimation() {
        if (enemyState.equals(IDLE)) {
            maxFrame = Tauro.GetSpriteFrame(IDLE);
            aniSpeed = 120 / maxFrame;
        }else if(enemyState.equals(WALK)){
            maxFrame = Tauro.GetSpriteFrame(WALK);
            aniSpeed = 120 / maxFrame;
        }

    }

    private void updateMove(int[][] lvlData) {

        if (firstUpdate) {
            if (!isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        if (inAir) {
            if (canMove(hitbox.x, hitbox.y + fallSpeed, width, height, lvlData)) {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitbox.y = GetEntityYPos(hitbox, fallSpeed);
            }

        } else {
            switch (enemyState) {
                case IDLE:
                    enemyState = WALK;
                    break;
                case WALK:
                    float xSpeed = 0;

                    if (arah == 0) {
                        xSpeed = -walkSpeed;
                    } 
                    // else {
                    //     xSpeed = walkSpeed;
                    // }

                    // if (canMove(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                        // if (isFloor(hitbox, xSpeed, lvlData)) {
                            // System.out.println(xSpeed);
                            hitbox.x += xSpeed;
                            // return;
                        // }
                    // }

                    // changeWalkDir();

                    break;
                default:
                    break;
            }
        }
    }

    private void changeWalkDir() {
        arah = (arah == 0) ? 1 : 0;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public String getEnemyState() {
        return enemyState;
    }
}
