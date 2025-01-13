package ui;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButtons extends PauseButtons {

    private BufferedImage slider;
    private BufferedImage[] buttons;
    private int index;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;

    public VolumeButtons(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.VOLUME_BUTTONS, "UI");
        buttons = new BufferedImage[3];
        for (int i = 0; i < buttons.length; i++)
            buttons[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH,
                VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;

    }

    public void render(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(buttons[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x) {
        if (x > maxX)
            x = maxX;
        if (x < minX)
            x = minX;
        else
            buttonX = x;

        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

}
