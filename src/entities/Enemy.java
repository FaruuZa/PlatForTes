package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;

import java.awt.Graphics;

import utilz.Constants.EnemyConstants.Tauro;

public abstract class Enemy extends Entity {

    private int aniIndex, enemytype;
    private int aniTick, aniSpeed;
    private String enemyState = IDLE;

    public Enemy(float x, float y, int width, int height, int enemytype) {
        super(x, y, width, height);
        this.enemytype = enemytype;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= 120 / Tauro.GetSpriteFrame(enemyState)) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= Tauro.GetSpriteFrame(enemyState)) {
                aniIndex = 0;
            }
        }
    }

    public void render(Graphics g) {

    }

    public void update() {
        updateAnimationTick();
        updateHitbox();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public String getEnemyState() {
        return enemyState;
    }
}
