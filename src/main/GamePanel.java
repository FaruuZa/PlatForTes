package main;

import Input.KeyboardInput;
import Input.MouseInput;

import java.awt.Color; // Import Color
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;;

public class GamePanel extends JPanel {

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        setFocusable(true);
        addKeyListener(new KeyboardInput(this));
        addMouseListener(new MouseInput(this));
        addMouseMotionListener(new MouseInput(this));
    }

    public void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void updateGame() {
        // Update game logic here
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Set the background color
        g.setColor(new Color(30, 40, 60));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
