package ui;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.UrmButton.*;
import static utilz.Constants.UI.VolumeButtons.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class PauseOverlay {

    private BufferedImage backgroundImg;
    private int bgX, bgY, bgWidth, bgHeight;
    private SoundButton sfxButton, musicButton;
    private UrmButton unpauseB, replayB, menuB;
    private Playing playing;
    private VolumeButtons volumeButtons;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButtons = new VolumeButtons(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        int menuX = ((Game.GAME_WIDTH - (bgWidth / 5)) / 2) - (int) (URM_SIZE * 1.3);
        int replayX = ((Game.GAME_WIDTH - (bgWidth / 5)) / 2);
        int unpauseX = ((Game.GAME_WIDTH - (bgWidth / 5)) / 2) + (int) (URM_SIZE * 1.3);
        int urmY = (int) (325 * Game.SCALE);
        menuB = new UrmButton(menuX, urmY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, urmY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, urmY, URM_SIZE, URM_SIZE, 0);
    }

    private void createSoundButtons() {
        int soundX = (int) ((Game.GAME_WIDTH - (bgWidth / 5)) / 2) + (int) (URM_SIZE*2);
        int musicY = (int) (145 * Game.SCALE);
        int sfxY = (int) (191 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.getSpriteAtlas(LoadSave.PAUSE_BACKGROUND, "UI");
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = (Game.GAME_WIDTH - bgWidth) / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        unpauseB.update();
        replayB.update();
        menuB.update();

        volumeButtons.update();
    }

    public void render(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);
        // Sound Buttons
        musicButton.render(g);
        sfxButton.render(g);
        // Urm Buttons
        unpauseB.render(g);
        replayB.render(g);
        menuB.render(g);
        // Volume Buttons
        volumeButtons.render(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, volumeButtons))
            volumeButtons.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpause();
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed())
                System.out.println("Replay");
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpause();
            }
        }

        musicButton.setMousePressed(false);
        sfxButton.setMousePressed(false);
        unpauseB.setMousePressed(false);
        replayB.setMousePressed(false);
        menuB.setMousePressed(false);
        volumeButtons.setMousePressed(false);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButtons.isMousePressed())
            volumeButtons.changeX(e.getX());
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        menuB.setMouseOver(false);
        volumeButtons.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, volumeButtons))
            volumeButtons.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButtons b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

}
