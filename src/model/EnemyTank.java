package model;

public class EnemyTank extends Tank {
    private long lastChangeDirTime;
    private int id;

    public EnemyTank(int tankType, MyPanel father, int x, int y) {
        super(tankType, father, x, y);
        // 这个id用来遍历的时候与自己分别开
        this.setId(this.getMyPanel().getTanks().size());
        this.setDir(this.getRandom().nextInt(4) + 1);
        this.setSpeed(Const.TANK_NOR_SPEED);
        this.setBulletType(Const.BULLET_NOR);
        this.setLastChangeDirTime(System.currentTimeMillis());
        this.setImg(new String[]{"src/res/drawable/tank_enemy_up.png", "src/res/drawable/tank_enemy_down.png", "src/res/drawable/tank_enemy_right.png", "src/res/drawable/tank_enemy_left.png"});
    }

    /***
     * 敌人坦克开火函数，判断间隔，摇随机数，开火
     * 目前设置每隔1秒，摇一次随机数，判断奇偶 1/2
     */
    @Override
    public boolean canFire() {
        long t = System.currentTimeMillis();
        if (t - this.getLastFireTime() > Const.EnemyFireGap + this.getRandom().nextInt(1000)) {
            this.setLastFireTime(t);
            return true;
        } else {
            return false;
        }
    }

    public int getDisOfAim() {
        int[] aim = getAim();
        return (int) Math.sqrt(Math.pow(aim[0] - this.getX(), 2) + Math.pow(aim[1] - this.getY(), 2));
    }

    public int[] getAim() {
        int aimX, aimY;
        if (this.getId() % 2 == 0) {
            aimX = this.getMyPanel().getTanks().get(0).getX();
            aimY = this.getMyPanel().getTanks().get(0).getY();
        } else {
            aimX = this.getMyPanel().getBarriers().get(0).getX();
            aimY = this.getMyPanel().getBarriers().get(0).getY();
        }
        return new int[]{aimX, aimY};
    }

    public int changeDir() {
        int dir = this.getDir(); // 这是原来的方向
        int[] aim = this.getAim();
        int aimX = aim[0];
        int aimY = aim[1];
        int dis = getDisOfAim();
        long t = System.currentTimeMillis();

        if (t - this.getLastChangeDirTime() >= Const.CHANGE_DIR_GAP + this.getRandom().nextInt(1000)) {
            if (dis < Const.Short_Dis) {
                if (this.getHp() < 25) {
                    // 逃跑
                    dir = escape(aimX, aimY);
                } else {
                    // 距离近， 血量厚
                    dir = pursuit(aimX, aimY);
                }
            } else if (dis > Const.Long_Dis) {
                // 距离远
                if (this.getHp() < 25) {
                    dir = randomDir();
                } else {
                    // 距离远，血量厚
                    dir = pursuit(aimX, aimY);
                }
            } else {
                dir = pursuit(aimX, aimY);
            }
            this.setLastChangeDirTime(t);
        }
        return dir;
    }

    public void updateDecSpeed() {
        long t = System.currentTimeMillis();
        if (this.getPropTime()[0] != 0 && t - this.getPropTime()[0] > Const.last_time) {
            this.setPropTime(0, Const.changeBullet);
            this.setSpeed(Const.TANK_NOR_SPEED);
        }
    }


    /**
     * 逃跑
     *
     * @param aimX 目标横坐标
     * @param aimY 目标纵坐标，传入玩家坦克或者home
     * @return 方向
     */
    public int escape(int aimX, int aimY) {
        int x = this.getX();
        int y = this.getY();
        int dir;
        if (this.getRandom().nextInt(2) == 0) {
            // 0.5概率改变 X
            if (aimX > x) {
                if (this.getRandom().nextInt(100) < 20) {
                    // 2/5 概率逃跑失败
                    dir = Const.RIGHT;
                } else {
                    // 4/5 逃跑成功
                    dir = Const.LEFT;
                }
            } else {
                if (this.getRandom().nextInt(100) < 20) {
                    dir = Const.LEFT;
                } else {
                    dir = Const.RIGHT;
                }
            }
        } else {
            // 0.5 概率改变 y
            if (aimY > y) {
                if (this.getRandom().nextInt(100) < 20) {
                    // 2/5 概率逃跑失败
                    dir = Const.DOWN;
                } else {
                    // 4/5 逃跑成功
                    dir = Const.UP;
                }
            } else {
                if (this.getRandom().nextInt(100) < 20) {
                    dir = Const.UP;
                } else {
                    dir = Const.DOWN;
                }
            }
        }
        return dir;
    }

    public int pursuit(int aimX, int aimY) {
        int x = this.getX();
        int y = this.getY();
        int dir = this.getDir();
        if (this.getRandom().nextInt(2) == 0) {
            // 0.5概率改变 X
            if (aimX > x) {
                if (this.getRandom().nextInt(100) < 20) {
                    // 2/5 概率追击失败
                    dir = Const.LEFT;
                } else {
                    // 4/5 追击成功
                    dir = Const.RIGHT;
                }
            } else {
                if (this.getRandom().nextInt(100) < 20) {
                    dir = Const.RIGHT;
                } else {
                    dir = Const.LEFT;
                }
            }
        } else {
            // 0.5 概率改变 y
            if (aimY > y) {
                if (this.getRandom().nextInt(100) < 20) {
                    // 2/5 概率逃跑失败
                    dir = Const.UP;
                } else {
                    // 4/5 逃跑成功
                    dir = Const.DOWN;
                }
            } else {
                if (this.getRandom().nextInt(100) < 20) {
                    dir = Const.DOWN;
                } else {
                    dir = Const.UP;
                }
            }
        }
        return dir;
    }

    public int randomDir() {
        return this.getRandom().nextInt(4) + 1; // 1 - 4
    }


    @Override
    public void move() {
        int dir;
        int[] next;
        // 用来改变前进方向
        this.updateDecSpeed();
        dir = this.changeDir();
        next = getMoveAim(dir);     // 得到下一个坐标数组
        if (canMove(next[0], next[1])) {
            this.setDir(dir);
            this.setX(next[0]);
            this.setY(next[1]);
        }
    }

    public int[] getMoveAim(int dir) {
        int newX = this.getX();
        int newY = this.getY();
        switch (dir) {
            case Const.UP -> newY -= this.getSpeed();
            case Const.DOWN -> newY += this.getSpeed();
            case Const.LEFT -> newX -= this.getSpeed();
            case Const.RIGHT -> newX += this.getSpeed();
            default -> {
            }
        }
        return new int[]{newX, newY};
    }


    public boolean canMove(int newX, int newY) {

        if (Utils.isBeyondBorder(newX, newY, Const.TANK_WIDTH)) {
            return false;
        }


        // 判断和出生点相撞
        for (int i = 0; i < this.getMyPanel().getBirths().size(); i++) {
            Birth birth = this.getMyPanel().getBirths().get(i);
            if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
                return false;
            }
        }

        // 判断和其他的坦克碰撞
        // 2 号老是穿过 1 号
        for (int i = 0; i < this.getMyPanel().getTanks().size(); i++) {
            Tank tank = this.getMyPanel().getTanks().get(i);
            if (this.getMyPanel().getTanks().get(i) != this) {
                if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                    //System.out.println("我的ID："+this.getId()+", "+"碰到了ID："+i);
                    return false;
                }
            }
        }

        // 如果和障碍物碰撞
        for (Barrier barrier : this.getMyPanel().getBarriers()) {
            if (barrier.getType() > Const.CAN_MOVE) {
                if (Utils.isCollide(newX, newY, Const.TANK_WIDTH, barrier.getX(), barrier.getY(), Const.WIDTH)) {
                    return false;
                }
            }
        }

        return true;
    }


    public long getLastChangeDirTime() {
        return lastChangeDirTime;
    }

    public void setLastChangeDirTime(long lastChangeDirTime) {
        this.lastChangeDirTime = lastChangeDirTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}