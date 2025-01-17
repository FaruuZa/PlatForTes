package entities;

import static utilz.Constants.EnemyConstants.Tauro.*;

public class Tauro extends Enemy {
    private int[] widHeight;
    public Tauro(float x, float y, int width, int height){
        super(x, y, width , height, 1);
    }
}
