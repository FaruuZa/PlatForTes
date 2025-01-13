package utilz;

import main.Game;

public class Constants {

    public static class UI {
        public static class Button {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class UrmButton {
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class PlayerConstants {

        public static final String IDLE = "idle";
        public static final String RUN = "Run";
        public static final String DEAD = "dead";
        public static final String JUMP_START = "jump-Start";
        public static final String JUMP_END = "jump-End";
        public static final String ATTACK = "attack";

        public static int GetSpriteFrame(String player_action) {
            return switch (player_action) {
                case IDLE ->
                    4; // 4
                case ATTACK ->
                    8;
                case RUN ->
                    8; // 8
                case DEAD ->
                    8;
                case JUMP_START ->
                    4;
                case JUMP_END ->
                    3;
                default ->
                    4;
            };
        }

        public static int[] a;

        public static int[] GetSpriteSize(String player_action) {
            a = new int[2];
            a[0] = 64;
            a[1] = 80;
            switch (player_action) {
                case IDLE -> {

                    return a;
                }
                case ATTACK -> {
                    a[0] = 96;
                    return a;
                }
                case RUN -> {
                    a[0] = 80;
                    return a;
                }
                case DEAD -> {
                    a[0] = 80;
                    a[1] = 64;
                    return a;
                }
                case JUMP_START -> {
                    a[0] = 64;
                    a[1] = 64;
                    return a;
                }
                case JUMP_END -> {
                    a[0] = 64;
                    a[1] = 64;
                    return a;
                }
                default -> {
                    return a;
                }
            }
        }

    }

    public static class LevelConstants {
        public static final String BUILDINGS = "Buildings";
        public static final String HIVES = "Hives";
        public static final String INTERIORS = "Interiors";
        public static final String TILES = "Tiles";
        public static final String ROCKS = "Rocks";
        public static final String TREES = "Trees";
    }
}
