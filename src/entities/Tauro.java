package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;

import main.Game;

public class Tauro extends Enemy {
    private int[] widHeight;

    public Tauro(float x, float y, int width, int height) {
        super(x, y, width, height, 1);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (28 * Game.SCALE));
    }
}
