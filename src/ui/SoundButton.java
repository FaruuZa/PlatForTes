package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButtons {

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImg();
    }

    private void loadSoundImg() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUND_BUTTONS, "UI");
        soundImgs = new BufferedImage[2][3];
        for (int i = 0; i < soundImgs.length; i++) {
            for (int j = 0; j < soundImgs[i].length; j++) {
                soundImgs[i][j] = temp.getSubimage(j * SOUND_SIZE_DEFAULT, i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT,
                        SOUND_SIZE_DEFAULT);
            }
        }
    }

    public void update() {
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;
        if (mouseOver) {
            colIndex = 1;
        }
        if (mousePressed) {
            colIndex = 2;
        }

    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public void render(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
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
