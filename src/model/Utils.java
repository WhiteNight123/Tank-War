package model;

public class Utils {
    public static boolean isCollide(int x1, int y1, int width1, int x2, int y2, int width2) {
        // 100 100 60       160 160 60
        return (y2 < y1 || y2 < y1 + width1) && (y1 < y2 || y1 < y2 + width2) && (x2 < x1 || x2 < x1 + width1) && (x1 < x2 || x1 < x2 + width2);
    }

    // 边界
    public static boolean isBeyondBorder(int x, int y, int width) {
        return x < 0 || y < 0 || x > Const.GAME_WIDTH - width || y > Const.GAME_HEIGHT - width;
    }
}
