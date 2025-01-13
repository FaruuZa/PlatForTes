package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;

public class KeyboardInput implements KeyListener {
    private GamePanel gPanel;

    public KeyboardInput(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed if not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }
}
