package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;
import main.Game;

public class Tauro extends Enemy {

    public Tauro(float x, float y, int width, int height) {
        super(x, y, width, height, 1);
        initHitbox(x, y, (int) (28 * Game.SCALE), (int) (48 * Game.SCALE));
        walkSpeed = 0.3f * Game.SCALE;
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player) {

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

                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                default:
                    break;
            }
        }
    }
}
