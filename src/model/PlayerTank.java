package model;

public class PlayerTank extends Tank {
    private boolean isMoving;
    private boolean isFiring;
    private boolean isProtected;

    public PlayerTank(int tankType, MyPanel father, int x, int y) {
        super(tankType, father, x, y);
        this.setDir(Const.UP);
        this.setSpeed(Const.TANK_NOR_SPEED);
        this.setMoving(false);
        this.setFiring(false);
        this.setBulletType(Const.BULLET_NOR);
        this.setImg(new String[]{"src/res/drawable/tank_player_up.png", "src/res/drawable/tank_player_down.png", "src/res/drawable/tank_player_right.png", "src/res/drawable/tank_player_left.png"});
        this.setProtected(false);
    }

    @Override
    public boolean canFire() {
        long t = System.currentTimeMillis();
        if (t - this.getLastFireTime() > Const.PlayerFireGap && this.isFiring()) {
            this.setLastFireTime(t);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canMove() {
        this.updatePropState();
        if (!this.isMoving()) {
            return false;
        }
        int newX = this.getX();
        int newY = this.getY();
        switch (this.getDir()) {
            case Const.UP -> newY = this.getY() - this.getSpeed();
            case Const.DOWN -> newY = this.getY() + this.getSpeed();
            case Const.LEFT -> newX = this.getX() - this.getSpeed();
            case Const.RIGHT -> newX = this.getX() + this.getSpeed();
            default -> {
            }
        }

        // 如果超出边界
        if (Utils.isBeyondBorder(newX, newY, Const.TANK_WIDTH)) {
            return false;
        }

        // 如果和障碍物碰撞
        for (Barrier barrier : this.getMyPanel().getBarriers()) {
            if (barrier.getType() > Const.CAN_MOVE) {
                if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, barrier.getX(), barrier.getY(), Const.WIDTH)) {
                    return false;
                }
            }
        }

        // 如果和出生点碰撞
        for (Birth birth : this.getMyPanel().getBirths()) {
            if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
                return false;
            }
        }

        // 如果和坦克碰撞
        for (int i = 1; i < this.getMyPanel().getTanks().size(); i++) {
            if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, this.getMyPanel().getTanks().get(i).getX(), this.getMyPanel().getTanks().get(i).getY(), Const.TANK_WIDTH)) {
                return false;
            }
        }

        // 和buff相撞
        for (int i = 0; i < this.getMyPanel().getProps().size(); i++) {
            if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, this.getMyPanel().getProps().get(i).getX(), this.getMyPanel().getProps().get(i).getY(), Const.propWidth)) {
                this.eatProp(this.getMyPanel().getProps().get(i).getType());
                this.getMyPanel().getProps().get(i).setAlive(false);
            }
        }
        return true;
    }

    public void eatProp(int type) {
        switch (type) {
            case Const.changeBullet -> this.setBulletType(Const.BULLET_PRO);
            case Const.increase -> this.setSpeed(Const.Tank_INC_SPEED);
            case Const.protect -> this.setProtected(true);
            case Const.recovery -> this.setHp(Const.MAX_HP);
            default -> {
            }
        }
        if (type < Const.recovery) {
            this.setPropTime(System.currentTimeMillis(), type);
        }
    }

    public void updatePropState() {
        long t = System.currentTimeMillis();
        for (int i = 0; i < this.getPropTime().length; i++) {
            if (this.getPropTime()[i] != 0 && t - this.getPropTime()[i] > Const.last_time) {
                this.setPropTime(0, i);
                switch (i) {
                    case Const.changeBullet -> this.setBulletType(Const.BULLET_NOR);
                    case Const.increase -> this.setSpeed(Const.TANK_NOR_SPEED);
                    case Const.protect -> this.setProtected(false);
                    default -> {
                    }
                }
            }
        }
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    @Override
    public void onAttacked(int damage) {
        if (!this.isProtected()) {
            this.setHp(this.getHp() - damage);
        }
    }
}
