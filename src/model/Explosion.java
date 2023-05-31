package model;

import java.awt.*;

public class Explosion {
    private int x;
    private int y;
    private int current;
    private int type;   // 判断坦克还是子弹
    private int width;
    private String[] img = new String[]{"src/res/drawable/tank_explosion", "src/res/drawable/bullet_explosion"};
    private MyPanel father;
    private int total;
    private boolean isAlive;
    private long lastExplosionTime;

    public Explosion(int x, int y, int type, MyPanel father) {
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
        this.setFather(father);
        this.setAlive(true);
        this.setLastExplosionTime(System.currentTimeMillis());
    }

    public void draw(Graphics g) {
        long t = System.currentTimeMillis();
        String path = this.getImg()[this.getType()] + this.getCurrent() + ".gif";
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path), this.getX(), this.getY(), this.getWidth(), this.getWidth(), this.getFather());
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

    public void setCurrent(int current) {
        this.current = current;
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

    public void setImg(String[] img) {
        this.img = img;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public MyPanel getFather() {
        return father;
    }

    public void setFather(MyPanel father) {
        this.father = father;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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