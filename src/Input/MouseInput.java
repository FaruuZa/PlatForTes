package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import gamestates.Gamestate;
import main.GamePanel;

public class MouseInput implements MouseListener, MouseMotionListener {
    private GamePanel gPanel;

    public MouseInput(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().mouseClicked(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().mousePressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().mouseReleased(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                break;
            default:
                break;
        }
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING:
                gPanel.getGame().getPlaying().mouseDragged(e);
                break;
        
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gPanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gPanel.getGame().getPlaying().mouseMoved(e);
                break;
            default:
                break;

        }
    }

}
