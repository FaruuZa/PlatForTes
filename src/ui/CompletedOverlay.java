package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.UI.UrmButton.*;

public class CompletedOverlay {

    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgWidth, bgHeight;

    public CompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButton();
    }

    private void initButton() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);

    }

    public void initImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.COMPLETED_IMG, "UI");
        bgWidth = (int) (img.getWidth() * Game.SCALE);
        bgHeight = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void update() {
        menu.update();
        next.update();
    }

    public void render(Graphics g) {
        g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
        next.render(g);
        menu.render(g);
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e))
            next.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed())
                System.out.println("menu pressed");
        } else if (isIn(next, e))
            if (menu.isMousePressed())
                System.out.println("next pressed");

        menu.resetBoolean();
        next.resetBoolean();
    }

    public boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}