package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.Button.*;

public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private int yOffsetCenter = B_HEIGHT / 2;
    private Gamestate state;
    private BufferedImage[] images;
    boolean mouseOver=false, mousePressed=false;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();

    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MENU_BUTTONS, "UI");

        // Check if the temp image is loaded successfully
        if (temp == null) {
            System.err.println("Failed to load the sprite atlas.");
            return;
        }
        // Ensure the x-coordinate does not exceed the width of the temp image
        for (int i = 0; i < images.length; i++) {
            int x = i * B_WIDTH_DEFAULT;
            int y = rowIndex * B_HEIGHT_DEFAULT;

            if (x + B_WIDTH_DEFAULT > temp.getWidth()) {
                System.out.println("x+B_WIDTH: " + (x + B_WIDTH_DEFAULT));
                System.err.println("Subimage x-coordinate exceeds the width of the sprite.");
                return;
            }
            if (y + B_HEIGHT_DEFAULT > temp.getHeight()) {
                System.out.println("y+B_HEIGHT " + (y + B_HEIGHT_DEFAULT));
                System.err.println("Subimage y-coordinate exceeds the height of the sprite atlas.");
                return;
            }

            images[i] = temp.getSubimage(x, y, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void render(Graphics g) {
        g.drawImage(images[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;

    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void ApplyGamestate() {
        Gamestate.state = state;
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }
}
