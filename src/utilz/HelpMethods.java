package utilz;

import java.awt.geom.Rectangle2D;

import main.Game;

public class HelpMethods {

    public static boolean canMove(float x, float y, float width, float height, int[][] levelData) {
        if (!IsSolid(x, y, levelData)) {
            if (!IsSolid(x + width, y, levelData)) {
                // if (!IsSolid(x, y + height, levelData)) {
                // if (!IsSolid(x + width, y + height, levelData)) {
                return true;
                // }
                // }
            }
        }
        return false;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        return IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)
                || IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData);
    }

    private static boolean IsSolid(float x, float y, int[][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        int xIndex = (int) (x / Game.TILES_SIZE);
        int yIndex = (int) (y / Game.TILES_SIZE);

        return IsTileSolid(xIndex, yIndex, levelData);
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

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData, int arah) {
        // if (arah == 0)
        // return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secHitbox,
            int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secXTile = (int) (secHitbox.x / Game.TILES_SIZE);

        if (firstXTile < secXTile) {
            return IsAllTileWalkable(firstXTile, secXTile, yTile, lvlData);
        } else
            return IsAllTileWalkable(secXTile, firstXTile, yTile, lvlData);
    }

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++){
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if(!IsTileSolid(xStart+ i, y+1, lvlData)){
                return false;
            }
        }
        return true;
    }

    private static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
        int value = levelData[yTile][xTile];
        return value > 0;
    }
}
