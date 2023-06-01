package model;

import java.awt.*;

public class Birth {
    private final static String[] img = new String[]{"src/res/drawable/birth_star0.png", "src/res/drawable/birth_star1.png", "src/res/drawable/birth_star2.png", "src/res/drawable/birth_star3.png"};
    private final static int width = Const.TANK_WIDTH;
    private final int x;
    private final int y;
    private final MyPanel myPanel;
    private int cur;
    private boolean isAlive;
    private long lastPutImgTime;

    public Birth(int x, int y, MyPanel myPanel) {
        this.x = x;
        this.y = y;
        this.cur = 0;
        this.isAlive = true;
        this.lastPutImgTime = System.currentTimeMillis();
        this.myPanel = myPanel;
    }

    public void draw(Graphics g) {
        long t = System.currentTimeMillis();
        g.drawImage(Toolkit.getDefaultToolkit().getImage(img[cur]), this.getX(), this.getY(), width, width, this.getMyPanel());
        if (t - this.getLastPutImgTime() > Const.Img_Gap) {
            cur++;
            this.setLastPutImgTime(t);
        }
        this.setAlive(cur != 4);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public long getLastPutImgTime() {
        return lastPutImgTime;
    }

    public void setLastPutImgTime(long lastPutImgTime) {
        this.lastPutImgTime = lastPutImgTime;
    }

    public MyPanel getMyPanel() {
        return myPanel;
    }
}
