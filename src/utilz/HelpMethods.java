package utilz;

import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {

    public static boolean canMove(float x, float y, float width, float height, int[][] levelData) {
        if (!isSolid(x, y, levelData)) {
            if (!isSolid(x + width, y, levelData)) {
                // if (!isSolid(x, y + height, levelData)) {
                    // if (!isSolid(x + width, y + height, levelData)) {
                        return true;
                    // }
                // }
            }
        }
        return false;
    }
    
    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        return isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData) || isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData);
    }
    
    private static boolean isSolid(float x, float y, int[][] levelData) {
        if (x < 0 || x >= Game.GAME_WIDTH) 
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;
    
        int xIndex = (int) (x / Game.TILES_SIZE);
        int yIndex = (int) (y / Game.TILES_SIZE);
    
        int value = levelData[yIndex][xIndex];
    
        return value > 0;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;// Get the tile position next to player
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPos(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // falling
            int tileYPos = currentTile * Game.TILES_SIZE;// Get the tile position next to player
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            // jump
            return currentTile * Game.TILES_SIZE;
        }
    }

}
