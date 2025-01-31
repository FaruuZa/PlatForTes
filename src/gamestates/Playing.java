package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.CompletedOverlay;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager lmanager;
    private EnemyManager eManager;
    private boolean paused;
    private PauseOverlay pauseOverlay;
    private CompletedOverlay lCompletedOverlay;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;

    BufferedImage backgroundImg, backgroundImg2, water, rockb, rockf;

    private boolean gameOver = false, levelCompleted = false;
    private GameOverOverlay gameOverOverlay;

    private int[] playerPos;

    public Playing(Game game) {
        super(game);
        loadLevel();
        initClasses();
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMAGE, "level");
        backgroundImg2 = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_IMAGE2, "level");
        water = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_WATER, "level");
        rockb = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_ROCKB, "level");
        rockf = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BG_ROCKF, "level");
    }

    private void loadLevel() {
        lmanager = new LevelManager(game);
    }

    private void initClasses() {
        eManager = new EnemyManager(this);
        lvlTilesWide = getLvlData()[0].length;
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffsetX = maxTilesOffset * Game.TILES_SIZE;
        playerPos = LoadSave.getPlayerPos(getLvlData());
        player = new Player(playerPos[1], playerPos[0], this);
        player.loadLvlData(getLvlData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        lCompletedOverlay = new CompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            lCompletedOverlay.update();
        } else {
            lmanager.update();
            eManager.update(getLvlData(), getPlayer());
            player.update();
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLevelOffsetX)
            xLvlOffset = maxLevelOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void render(Graphics g) {
        drawBackGround(g);
        lmanager.render(g, xLvlOffset);
        eManager.render(g, xLvlOffset);
        player.render(g, xLvlOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.render(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (levelCompleted)
            lCompletedOverlay.render(g);

    }

    public void drawBackGround(Graphics g) {

        for (int i = 0; i < 3; i++) {
            g.drawImage(backgroundImg, (int) (i * (Game.GAME_WIDTH / 3)), 0, Game.GAME_WIDTH / 3, Game.GAME_HEIGHT,
                    null);
            g.drawImage(backgroundImg2, (int) (i * (Game.GAME_WIDTH / 3)), 0, Game.GAME_WIDTH / 3, Game.GAME_HEIGHT,
                    null);
            g.drawImage(water, (int) (i * (Game.GAME_WIDTH / 3)), 0, Game.GAME_WIDTH / 3, Game.GAME_HEIGHT, null);
            g.drawImage(rockb, (int) (i * (Game.GAME_WIDTH / 3)), 0, Game.GAME_WIDTH / 3, Game.GAME_HEIGHT, null);
            g.drawImage(rockf, (int) (i * (Game.GAME_WIDTH / 3)), 0, Game.GAME_WIDTH / 3, Game.GAME_HEIGHT, null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverlay.mousePressed(e);
        else if (levelCompleted)
            lCompletedOverlay.mousePressed(e);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);
        else if (levelCompleted)
            lCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseMoved(e);
        else if (levelCompleted)
            lCompletedOverlay.mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    // Jump functionality will be implemented later
                    break;
                case KeyEvent.VK_A:
                    player.left = true;
                    break;
                case KeyEvent.VK_D:
                    player.right = true;
                    break;
                case KeyEvent.VK_SPACE:
                    player.jump = true;
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    paused = true;
                    break;
                case KeyEvent.VK_E:
                    player.moveGhost();
                    break;
                case KeyEvent.VK_S:
                    player.ghostSkill();
                    break;
                default:
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.left = false;
                    break;
                case KeyEvent.VK_D:
                    player.right = false;
                    break;
                case KeyEvent.VK_SPACE:
                    player.jump = false;
                    break;
                default:
                    break;
            }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowLostFocus() {
        player.resetDir();
    }

    public void unpause() {
        paused = false;
    }

    public int[][] getLvlData() {
        return lmanager.getCurrentLevel();
    }

    public void resetAll() {
        // reset playing, enemy, player
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        eManager.resetAll(getLvlData());
        initClasses();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        eManager.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean b) {
        this.gameOver = b;
    }

    public void setLevelComplete(boolean b) {
        this.levelCompleted = b;
    }

    public void nextLevel() {
        lmanager.nextLevel();
        resetAll();
    }

}
