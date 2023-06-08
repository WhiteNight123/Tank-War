package model;

import java.awt.*;
import java.util.Random;

public class Tank implements AttackCallable {
    private final int tankType;
    private final Random random;
    private final long[] propTime;
    private int x;
    private int y;
    private int dir;
    private int hp;
    private int bulletType;
    private boolean isAlive;
    private int speed;
    private MyPanel myPanel;
    private long lastFireTime;
    private String[] img;

    // 传入的是 敌人、玩家
    public Tank(int tankType, MyPanel myPanel, int x, int y) {
        this.setX(x);
        this.setY(y);
        this.hp = Const.MAX_HP;
        this.isAlive = true;
        this.tankType = tankType;
        this.setMyPanel(myPanel);
        this.random = new Random();
        this.setLastFireTime(System.currentTimeMillis());
        propTime = new long[]{0, 0, 0};
    }

    public void move() {
        if (canMove()) {
            switch (this.dir) {
                case Const.UP -> this.setY(this.getY() - speed);
                case Const.DOWN -> this.setY(this.getY() + speed);
                case Const.LEFT -> this.setX(this.getX() - speed);
                case Const.RIGHT -> this.setX(this.getX() + speed);
            }
        }
    }

    /**
     * 等待被重写
     *
     * @return true or false
     */
    public boolean canMove() {
        return false;
    }

    public void fire() {
        if (canFire()) {
            addBullet();
        }
    }

    /**
     * 是否可以发射子弹,对于不同的子类需要重写
     *
     * @return true 可以  false 不可以
     */
    public boolean canFire() {
        return false;
    }

    public void addBullet() {
        int x = this.getX();
        int y = this.getY();
        switch (this.getDir()) {
            case Const.UP -> {
                x += Const.WIDTH;
                y -= Const.WIDTH;
            }
            case Const.DOWN -> {
                x += Const.WIDTH;
                y += Const.TANK_WIDTH;
            }
            case Const.RIGHT -> {
                x += Const.TANK_WIDTH;
                y += Const.WIDTH;
            }
            case Const.LEFT -> {
                x -= Const.WIDTH;
                y += Const.WIDTH;
            }
        }

        this.getMyPanel().getBullets().add(new Bullet(x, y, this.getDir(), this.getBulletType(), this.getTankType(), this.getMyPanel()));
    }

    // 画血条
    public void draw(Graphics g1) {
        int width = 50;
        int y;
        Graphics2D g = (Graphics2D) g1;
        String path = this.getImg()[this.getDir() - 1];
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path), this.getX(), this.getY(), Const.TANK_WIDTH, Const.TANK_WIDTH, this.getMyPanel());
        int hp = this.getHp();
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(3));
        if (this.getY() <= 10) {
            y = this.getY() + Const.TANK_WIDTH + 10;
        } else {
            y = this.getY() - 10;
        }
        g.drawRect(this.getX() + 5, y, width, 5);
        g.setColor(Color.red);
        g.fillRect(this.getX() + 5, y, width * hp / Const.MAX_HP, 5);
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public long getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
            this.setAlive(false);
        } else {
            this.hp = hp;
        }
    }

    public MyPanel getMyPanel() {
        return myPanel;
    }

    public void setMyPanel(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }

    public int getTankType() {
        return tankType;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Random getRandom() {
        return random;
    }

    public String[] getImg() {
        return img;
    }

    public void setImg(String[] img) {
        this.img = img;
    }

    public long[] getPropTime() {
        return propTime;
    }

    public void setPropTime(long time, int type) {
        this.propTime[type] = time;
    }

    @Override
    public void onAttacked(int damage) {
        this.setHp(this.getHp() - damage);
    }
}
