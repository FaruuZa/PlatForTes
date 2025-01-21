package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import main.Game;

public class Tauro extends Enemy {

    // attackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;
    private Random random = new Random();

    public Tauro(float x, float y, int width, int height) {
        super(x, y, width, height, 1);
        initHitbox(x, y, (int) (28 * Game.SCALE), (int) (48 * Game.SCALE));
        walkSpeed = 0.2f * Game.SCALE;
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (30 * Game.SCALE), (int) (48 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateBehaviour(lvlData, player);
        updateAttackBox();
        updateAnimationTick();
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void updateAttackBox() {
        if (arah == 1) {
            attackBox.x = hitbox.x + hitbox.width / 2;
        } else if (arah == 0) {
            attackBox.x = hitbox.x - hitbox.width / 3;
        }

        attackBox.y = hitbox.y + (Game.SCALE * 2);
    }

    private void updateBehaviour(int[][] lvlData, Player player) {

        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir) {
            updateInAir(lvlData);

        } else {
            switch (enemyState) {
                case IDLE:
                    newState(WALK);
                    break;
                case WALK:
                    aniSpeed = 120 / GetSpriteFrame(enemyState);
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    aniSpeed = (120 / GetSpriteFrame(enemyState)) * 2;
                    if (aniIndex == 0)
                        attackChecked = false;

                    if (aniIndex == 4 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
