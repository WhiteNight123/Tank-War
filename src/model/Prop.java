package model;

import java.awt.*;

public class Prop {
    private static final String[] img = new String[]{"src/res/drawable/buff_increase_damage.png", "src/res/drawable/buff_increase_speed.png", "src/res/drawable/shield.png", "src/res/drawable/recover.png"};
    private int type;
    private int x;
    private int y;
    private boolean isAlive;
    private long time;
    private MyPanel myPanel;
    private long lastTime;
    private int cnt;

    public Prop(int x, int y, int type, MyPanel myPanel) {
        this.setX(x);
        this.setY(y);
        this.setType(type);
        this.setAlive(true);
        this.setTime(System.currentTimeMillis());
        this.setLastTime(System.currentTimeMillis());
        this.setMyPanel(myPanel);
        this.cnt = 0;
    }

    public MyPanel getMyPanel() {
        return this.myPanel;
    }

    public void setMyPanel(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

    public void draw(Graphics g) {
        long t = System.currentTimeMillis();
        if (t - this.getTime() <= Const.prop_gap / 2) {
            g.drawImage(Toolkit.getDefaultToolkit().getImage(img[this.getType()]), this.getX(), this.getY(), Const.propWidth, Const.propWidth, this.getMyPanel());
        } else if (t - this.getTime() <= Const.prop_gap) {
            if (t - this.getLastTime() >= 500) {
                this.cnt++;
                this.setLastTime(t);
            }
            if (cnt % 2 == 0) {
                g.drawImage(Toolkit.getDefaultToolkit().getImage(img[this.getType()]), this.getX(), this.getY(), Const.propWidth, Const.propWidth, this.getMyPanel());
            }
        } else {
            this.setAlive(false);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
