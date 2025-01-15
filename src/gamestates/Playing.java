package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager lmanager;
    private boolean paused;
    private PauseOverlay pauseOverlay;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        lmanager = new LevelManager(game);
        player = new Player(80, Game.GAME_HEIGHT - Game.TILES_SIZE * 2);
        player.loadLvlData(lmanager.getCurrentLevel());
        pauseOverlay = new PauseOverlay(this);
    }

    @Override
    public void update() {
        if (!paused) {
            lmanager.update();
            player.update();
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void render(Graphics g) {
        lmanager.render(g);
        player.render(g);

        if (paused)
            pauseOverlay.render(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverlay.mousePressed(e);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
            case KeyEvent.VK_S:
                if (player.isEnable())
                    player.setEnable(false);
                else
                    player.setEnable(true);
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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

}
