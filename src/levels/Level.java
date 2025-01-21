package levels;

public class Level {
    private int[][] lvlData;
    private LevelGenerator generator = new LevelGenerator();
    
    public Level(int width, int height, int maxEnemy){
        lvlData = generator.generateLevel(width, height, maxEnemy);
    }

    public int[][] getLevelData(){
        return lvlData;
    }

}
