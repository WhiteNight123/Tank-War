package model;

import java.awt.*;
import java.util.Random;

public class Bullet {
    private int x;
    private int y;
    private int damage;
    private int dir;
    private int bulletType;
    private int speed;  // 射速
    private int belongTo;   // 谁发出的， 敌方还是玩家
    private boolean alive; // 是否存在， 如果是 false则播放爆炸图片
    private MyPanel myPanel;

    public Bullet(int x, int y, int dir, int bulletType, int belongTo, MyPanel myPanel) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.bulletType = bulletType;
        this.belongTo = belongTo;
        this.alive = true;
        this.setMyPanel(myPanel);

        switch (bulletType) {
            case Const.BULLET_NOR -> {
                this.speed = Const.BULLET_NOR_SPEED;
                this.damage = Const.NOR_DAMAGE + new Random().nextInt(Const.DAMAGE_RAND);
            }
            case Const.BULLET_PRO -> {
                this.speed = Const.BULLET_PRO_SPEED;
                this.damage = Const.PRO_DAMAGE + new Random().nextInt(Const.DAMAGE_RAND);
            }
            default -> {
            }
        }
    }

    public void move() {
        if (canMove()) {
            switch (this.getDir()) {
                case Const.UP -> this.setY(this.getY() - this.getSpeed());
                case Const.DOWN -> this.setY(this.getY() + this.getSpeed());
                case Const.RIGHT -> this.setX(this.getX() + this.getSpeed());
                case Const.LEFT -> this.setX(this.getX() - this.getSpeed());
                default -> {
                }
            }
        }
    }

    public boolean canMove() {
        int newX = this.getX();
        int newY = this.getY();
        switch (this.getDir()) {
            case Const.UP -> newY -= this.getSpeed();
            case Const.DOWN -> newY += this.getSpeed();
            case Const.RIGHT -> newX += this.getSpeed();
            case Const.LEFT -> newX -= this.getSpeed();
            default -> {
            }
        }
        // 如果超出边界
        if (Utils.isBeyondBorder(newX, newY, Const.WIDTH)) {
            this.setAlive(false);
            this.getMyPanel().getExplosions().add(new Explosion(this.getX(), this.getY(), 1, this.getMyPanel()));
            return false;
        }

        // 如果和障碍物碰撞
        for (Barrier barrier : this.getMyPanel().getBarriers()) {
            if (barrier.getType() > Const.CAN_MOVE) {
                if (Utils.isCollide(newX, newY, Const.WIDTH, barrier.getX(), barrier.getY(), barrier.getWidth())) {
                    if (barrier.getType() == Const.BRICK || barrier.getType() == Const.HOME) {
                        barrier.setAlive(false);
                    }
                    this.getMyPanel().getExplosions().add(new Explosion(this.getX(), this.getY(), 1, this.getMyPanel()));
                    this.setAlive(false);
                    return false;
                }
            }
        }

        // 如果和子弹碰撞
        for (int i = 0; i < this.getMyPanel().getBullets().size(); i++) {
            Bullet bullet = this.getMyPanel().getBullets().get(i);
            if (bullet.getBelongTo() != this.getBelongTo() && this.getMyPanel().getBullets().get(i) != this) {
                if (Utils.isCollide(newX, newY, Const.WIDTH, bullet.getX(), bullet.getY(), Const.WIDTH)) {
                    bullet.setAlive(false);
                    this.setAlive(false);
                    return false;
                }
            }
        }

        // 如果和坦克碰撞
        for (Tank tank : this.getMyPanel().getTanks()) {
            if (tank.getTankType() != this.getBelongTo()) {
                if (Utils.isCollide(newX, newY, Const.WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                    tank.onAttacked(this.getDamage());
                    if (!tank.isAlive()) {
                        this.getMyPanel().getExplosions().add(new Explosion(tank.getX(), tank.getY(), 0, this.getMyPanel()));
                    }
                    if (this.getBulletType() == Const.BULLET_PRO) {
                        tank.setSpeed(Const.Tank_DEC_SPEED);
                        tank.setPropTime(System.currentTimeMillis(), Const.changeBullet);
                    }
                    this.setAlive(false);
                    this.getMyPanel().getExplosions().add(new Explosion(this.getX(), this.getY(), 1, this.getMyPanel()));
                    return false;
                }
            }
        }
        return true;
    }

    public void draw(Graphics g) {
        String path = null;
        if (this.getBulletType() == Const.BULLET_NOR) {
            path = "src/res/drawable/bullet_normal.png";
        } else {
            path = "src/res/drawable/bullet_pro.png";
        }
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path), this.getX(), this.getY(), Const.WIDTH, Const.WIDTH, this.getMyPanel());
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(int belongTo) {
        this.belongTo = belongTo;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public MyPanel getMyPanel() {
        return myPanel;
    }

    public void setMyPanel(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

}