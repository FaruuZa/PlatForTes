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
        public static final String GHOST = "ghost";
        public static final String GHOST_DEAD = "ghost_dead";

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
                case GHOST ->
                    12;
                case GHOST_DEAD ->
                    12;
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
                case GHOST -> {
                    a[0] = 128;
                    a[1] = 128;
                    return a;
                }
                case GHOST_DEAD -> {
                    a[0] = 128;
                    a[1] = 128;
                    return a;
                }
                default -> {
                    return a;
                }
            }
        }

    }

    public static class EnemyConstants {
        public static final int TAURO = 1;

        public static class Tauro {
            public static final String IDLE = "idle";
            public static final String WALK = "walk";
            public static final String DEAD = "dead";
            public static final String IDLE2 = "idle2";
            public static final String ATTACK = "attack";
            public static final int DRAW_OFFSET_X = (int) (26 * Game.SCALE);
            public static final int DRAW_OFFSET_Y = (int) (9 * Game.SCALE);

            public static int GetSpriteFrame(String tauro_action) {
                return switch (tauro_action) {
                    case IDLE ->
                        5; // 4
                    case ATTACK ->
                        7;
                    case WALK ->
                        8; // 8
                    case DEAD ->
                        10;
                    case IDLE2 ->
                        5;
                    default ->
                        0;
                };
            }

            public static int[] GetSpriteSize(String tauro_action) {
                int[] a = { 128, 128 };
                return a;
            }
        }

        public static int GetMaxHP(int enemyType) {
            return switch (enemyType) {
                case 1 -> 150; // Tauro
                case 2 -> 50;
                default -> 0;
            };
        }
        public static int GetDamage(int enemyType) {
            return switch (enemyType) {
                case 1 -> 2; // Tauro
                case 2 -> 1;
                default -> 0;
            };
        }
    }
}
