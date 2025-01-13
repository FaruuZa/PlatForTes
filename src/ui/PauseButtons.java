package ui;

import java.awt.Rectangle;

public class PauseButtons {

    protected int x, y, width, height;
    protected Rectangle bounds;

    public PauseButtons(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();

    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        createBounds();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        createBounds();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        createBounds();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        createBounds();
    }

    public Rectangle getBounds() {
        return bounds;
    }


}
