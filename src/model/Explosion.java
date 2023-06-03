package model;

import java.awt.*;

public class Explosion {
    private final int width;
    private final String[] img = new String[]{"src/res/drawable/explosion_tank", "src/res/drawable/explosion_bullet"};
    private final int total;
    private int x;
    private int y;
    private int current;
    private int type;   // 判断坦克还是子弹
    private MyPanel myPanel;
    private boolean isAlive;
    private long lastExplosionTime;

    public Explosion(int x, int y, int type, MyPanel myPanel) {
        this.setX(x);
        this.setY(y);
        this.setType(type);
        this.current = 1;
        if (type == 0) {
            width = Const.TANK_WIDTH;
            total = 5;
        } else {
            width = Const.WIDTH;
            total = 3;
        }
        this.setMyPanel(myPanel);
        this.setAlive(true);
        this.setLastExplosionTime(System.currentTimeMillis());
    }

    public void draw(Graphics g) {
        long t = System.currentTimeMillis();
        String path = this.getImg()[this.getType()] + this.getCurrent() + ".png";
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path), this.getX(), this.getY(), this.getWidth(), this.getWidth(), this.getMyPanel());
        if (t - this.getLastExplosionTime() >= 200) {
            current++;
            this.setLastExplosionTime(t);
        }
        this.setAlive(current != total);
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

    public int getCurrent() {
        return current;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getImg() {
        return img;
    }

    public int getWidth() {
        return width;
    }

    public MyPanel getMyPanel() {
        return myPanel;
    }

    public void setMyPanel(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public long getLastExplosionTime() {
        return lastExplosionTime;
    }

    public void setLastExplosionTime(long lastExplosionTime) {
        this.lastExplosionTime = lastExplosionTime;
    }
}