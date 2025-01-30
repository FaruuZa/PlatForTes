package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
    private JFrame jframe;

    // public int lebar = 700;
    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        // jframe.setSize(lebar, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.pack();
        jframe.setResizable(false);
        jframe.setTitle("TES GAME");
        jframe.setLocationRelativeTo(null);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // kosong
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowLostFocus();
            }
            
        });
        jframe.setVisible(true);
    }

    public void setTitle(String title){
        jframe.setTitle(title);
    }
}
