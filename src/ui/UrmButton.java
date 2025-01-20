package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.UrmButton.*;
import utilz.LoadSave;

public class UrmButton extends PauseButtons {

    private BufferedImage[] images;
    private int rowIndex, index;
    private boolean mousePressed, mouseOver;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImg();
    }

    private void loadImg() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.URM_BUTTONS, "UI");
        images = new BufferedImage[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT,
                    URM_SIZE_DEFAULT);
        }
    }

    public void render(Graphics g) {
        g.drawImage(images[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;

        if (mousePressed)
            index = 2;

    }

    public void resetBoolean() {
        mousePressed = false;
        mouseOver = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        System.out.println(index);
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}