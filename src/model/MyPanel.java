package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Vector;

public class MyPanel extends JPanel {
    private Vector<Tank> tanks;
    private Vector<Bullet> bullets;
    private Vector<Barrier> barriers;
    private Vector<Explosion> explosions;
    private Vector<Prop> props;
    private Vector<Birth> births;
    private int curLevel;
    private Thread rePaintThread = null;
    private Random random;
    private int curEnemyCnt;
    private int leftEnemy;
    private int scores;
    private long lastCanAddEneTime;
    private Game game;
    private final static String[] level = new String[]{"一", "二"};

    public MyPanel(Game game, int level) {
        this.game = game;
        this.setSize(Const.GAME_WIDTH + 200, Const.GAME_HEIGHT);
        tanks = new Vector<Tank>();
        bullets = new Vector<Bullet>();
        explosions = new Vector<Explosion>();
        props = new Vector<Prop>();
        births = new Vector<Birth>();
        random = new Random();

        curLevel = level;
        curEnemyCnt = 2;
        this.setScores(0);
        this.barriers = Barrier.readMap(curLevel, this);
        tanks.add(new PlayerTank(Const.PLAYER, this, 17 * Const.WIDTH, 37 * Const.WIDTH));
        tanks.add(new EnemyTank(Const.ENEMY, this, Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH));
        tanks.add(new EnemyTank(Const.ENEMY, this, Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH));
        this.leftEnemy = Const.Max_Enemy - this.getCurEnemyCnt();
        this.setLastCanAddEneTime(System.currentTimeMillis());
        rePaintThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }

            }
        };
        rePaintThread.start();
        this.setDoubleBuffered(true);

        // 面板添加监听
        this.addKeyListener(new KeyListener() {
            boolean up = false, down = false, left = false, right = false;
            boolean fire = false;

            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP -> up = true;
                    case KeyEvent.VK_DOWN -> down = true;
                    case KeyEvent.VK_RIGHT -> right = true;
                    case KeyEvent.VK_LEFT -> left = true;
                    case KeyEvent.VK_SPACE -> fire = true;
                }
                setCurDir();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP -> up = false;
                    case KeyEvent.VK_DOWN -> down = false;
                    case KeyEvent.VK_RIGHT -> right = false;
                    case KeyEvent.VK_LEFT -> left = false;
                    case KeyEvent.VK_SPACE -> fire = false;
                }
                setCurDir();
            }

            public void setCurDir() {

                PlayerTank player = (PlayerTank) tanks.get(0);
                if (!fire) {
                    player.setFiring(false);
                } else {
                    player.setFiring(true);
                }
                if (!up && !down && !right && !left) {
                    player.setMoving(false);
                } else {
                    // 如果开火间隔达不到直接return
                    if (up) player.setDir(Const.UP);
                    if (down) player.setDir(Const.DOWN);
                    if (right) player.setDir(Const.RIGHT);
                    if (left) player.setDir(Const.LEFT);
                    player.setMoving(true);
                }
            }

        });

    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        // 画背景
        Graphics2D g = (Graphics2D) g1;
        g.drawImage(Toolkit.getDefaultToolkit().getImage("pictures/map/gameBg.jpg"), 0, 0, Const.GAME_WIDTH, Const.GAME_HEIGHT, this);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("pictures/map/scorePanel.jpg"), Const.GAME_WIDTH, 0, 200, Const.GAME_HEIGHT, this);
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(3));
        g.drawLine(Const.GAME_WIDTH, 0, Const.GAME_WIDTH, Const.GAME_HEIGHT);
        // 需要使用for int i 循环，不能使用 for each 循环

        // 画出生地
        for (int i = 0; i < births.size(); i++) {
            if (!births.get(i).isAlive()) {
                this.getTanks().add(new EnemyTank(Const.ENEMY, this, births.get(i).getX(), births.get(i).getY()));
                births.remove(i);
            } else {
                births.get(i).draw(g);
            }
        }

        // 画坦克
        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).move();
            tanks.get(i).fire();
            if (!tanks.get(i).isAlive()) {
                if (i == 0) {
                    this.game.gameOver();
                } else {
                    this.setScores(this.getScores() + 100);
                }
                tanks.remove(i);
                this.setCurEnemyCnt(this.getCurEnemyCnt() - 1);
            } else {
                tanks.get(i).draw(g);
            }

            if (this.getTanks().get(0).isAlive() && this.getCurEnemyCnt() == 0 && this.getLeftEnemy() == 0) {
                if (this.getCurLevel() < 4) {
                    this.game.passGame();
                } else {
                    this.game.win();
                }
            }
        }
        // 画子弹
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).move();
            if (!bullets.get(i).isAlive()) {
                bullets.remove(i);
            } else {
                bullets.get(i).draw(g);
            }
        }

        // 画爆炸
        for (int i = 0; i < explosions.size(); i++) {
            if (!explosions.get(i).isAlive()) {
                if (explosions.get(i).getType() == 0) {
                    // 添加buff
                    this.addProp(explosions.get(i).getX() + 15, explosions.get(i).getY() + 15);
                }
                explosions.remove(i);
            } else {
                explosions.get(i).draw(g);
            }
        }

        // 画buff
        for (int i = 0; i < props.size(); i++) {
            if (!props.get(i).isAlive()) {
                props.remove(i);
                this.setScores(this.getScores() + 500);
            } else {
                props.get(i).draw(g);
            }
        }

        // 画障碍物
        for (int i = 0; i < barriers.size(); i++) {
            if (!barriers.get(i).isAlive()) {
                if (i == 0) {
                    this.game.gameOver();
                }
                barriers.remove(i);
            } else {
                barriers.get(i).draw(g, this);
            }
        }

        // 写分数，写击杀，剩余
        g.setFont(new Font("微软雅黑", Font.PLAIN, 28));
        g.setColor(Color.black);
        g.drawString(level[this.getCurLevel() - 1], Const.GAME_WIDTH + 85, 110);
        g.drawString(String.valueOf(this.getScores()), Const.GAME_WIDTH + 100, 190);
        g.drawString(String.valueOf(this.getLeftEnemy()), Const.GAME_WIDTH + 100, 280);
        g.drawString(String.valueOf(Const.Max_Enemy - this.getLeftEnemy() - this.getCurEnemyCnt()), Const.GAME_WIDTH + 100, 360);
        this.addEnemyTank();
    }

    public void addEnemyTank() {
        int total = this.getNumOfBirthPlace();
        int x = 0;
        int y = Const.Enemy_y;
        long t = System.currentTimeMillis();
        if (this.getCurEnemyCnt() < Const.Max_Cur_Enemy && this.leftEnemy > 0 && total != 0) {
            if (t - this.getLastCanAddEneTime() < Const.Add_Gap) {
                return;
            }
            switch (total) {
                case 2:
                    if (this.getRandom().nextInt(2) == 0) {
                        x = Const.Enemy_x1;
                    } else {
                        x = Const.Enemy_x2;
                    }
                    break;
                case -1:
                    x = Const.Enemy_x1;
                    break;
                case 1:
                    x = Const.Enemy_x2;
                    break;
                default:
                    break;
            }
            x *= Const.WIDTH;
            y *= Const.WIDTH;
            this.births.add(new Birth(x, y, this));
            this.setCurEnemyCnt(this.getCurEnemyCnt() + 1);
            this.setLeftEnemy(this.getLeftEnemy() - 1);
        } else {
            this.setLastCanAddEneTime(t);
        }
    }

    /**
     * @return 2 两个都可以
     * 1  右侧
     * -1  左侧
     * 0  都不可
     */
    private int getNumOfBirthPlace() {
        int a = 0;
        int b = 0;
        for (int i = 0; i < this.getTanks().size(); i++) {
            Tank tank = this.getTanks().get(i);
            if (CollDete.isCollide(Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                a = 1;
            }

            if (CollDete.isCollide(Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                b = 1;
            }
        }

        for (int i = 0; i < this.births.size(); i++) {
            Birth birth = this.births.get(i);
            if (CollDete.isCollide(Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
                a = 1;
            }
            if (CollDete.isCollide(Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
                b = 1;
            }
        }
        if (a == 0 && b == 0) {
            return 2;
        } else if (a != 0 && b == 0) {
            return 1;
        } else if (a == 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public void addProp(int x, int y) {
        if (this.random.nextInt(Const.P_prop) == 0) {
            this.getProps().add(new Prop(x, y, this.random.nextInt(4), this));
        }
    }
//    void

    /**
     * get and set
     *
     * @return get
     */
    public Vector<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(Vector<Barrier> barriers) {
        this.barriers = barriers;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Vector<Tank> getTanks() {
        return tanks;
    }

    public void setTanks(Vector<Tank> tanks) {
        this.tanks = tanks;
    }

    public Vector<Explosion> getExplosions() {
        return explosions;
    }

    public void setExplosions(Vector<Explosion> explosions) {
        this.explosions = explosions;
    }

    public Thread getRePaintThread() {
        return rePaintThread;
    }

    public void setRePaintThread(Thread rePaintThread) {
        this.rePaintThread = rePaintThread;
    }

    public Vector<Prop> getProps() {
        return props;
    }

    public void setProps(Vector<Prop> props) {
        this.props = props;
    }

    public int getCurEnemyCnt() {
        return curEnemyCnt;
    }

    public void setCurEnemyCnt(int curEnemyCnt) {
        this.curEnemyCnt = curEnemyCnt;
    }

    public int getLeftEnemy() {
        return leftEnemy;
    }

    public long getLastCanAddEneTime() {
        return lastCanAddEneTime;
    }

    public void setLastCanAddEneTime(long lastCanAddEneTime) {
        this.lastCanAddEneTime = lastCanAddEneTime;
    }

    public void setLeftEnemy(int leftEnemy) {
        this.leftEnemy = leftEnemy;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public Vector<Birth> getBirths() {
        return births;
    }

    public void setBirths(Vector<Birth> births) {
        this.births = births;
    }

    public GameFrame getGame() {
        return game;
    }

    public void setGame(GameFrame game) {
        this.game = game;
    }

    public static String[] getLevel() {
        return level;
    }
}

