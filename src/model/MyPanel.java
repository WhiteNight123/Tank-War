package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

public class MyPanel extends JPanel {
    private final Vector<Tank> tanks;
    private final Vector<Bullet> bullets;
    private final Vector<Barrier> barriers;
    private final Vector<Explosion> explosions;
    private final Vector<Prop> props;
    private final Vector<Birth> births;
    private final int curLevel;
    private final Random random;
    private final Game game;
    private int curEnemyCnt;
    private int leftEnemy;
    private int scores;
    private long lastCanAddEneTime;
    boolean isPause;
    private final PlayerTank player1;
    private PlayerTank player2;

    public MyPanel(Game game, int level, boolean isDoubleGame) {
        this.game = game;
        this.setSize(Const.GAME_WIDTH + 200, Const.GAME_HEIGHT);
        tanks = new Vector<>();
        bullets = new Vector<>();
        explosions = new Vector<>();
        props = new Vector<>();
        births = new Vector<>();
        random = new Random();

        curLevel = level;
        curEnemyCnt = 2;
        this.setScores(0);
        this.barriers = Barrier.readMap(curLevel);
        // 一号玩家出生点
        tanks.add(new PlayerTank(Const.PLAYER, this, Const.player1_x * Const.WIDTH, Const.player1_y * Const.WIDTH));
        player1 = (PlayerTank) tanks.get(0);
        // 二号玩家出生点
        if (isDoubleGame) {
            tanks.add(new PlayerTank(Const.PLAYER, this, Const.player2_x * Const.WIDTH, Const.player2_y * Const.WIDTH));
            player2 = (PlayerTank) tanks.get(1);
        }
        tanks.add(new EnemyTank(Const.ENEMY, this, Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH));
        tanks.add(new EnemyTank(Const.ENEMY, this, Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH));
        this.leftEnemy = Const.Max_Enemy - this.getCurEnemyCnt();
        this.setLastCanAddEneTime(System.currentTimeMillis());
        Thread rePaintThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isPause) {
                    repaint();
                }
            }
        });
        rePaintThread.start();
        this.setDoubleBuffered(true);

        // 面板添加监听
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (isDoubleGame) {
                    switch (keyEvent.getKeyCode()) {
                        // esc暂停
                        case KeyEvent.VK_ESCAPE -> {
                            pause();
                        }
                        // 一号玩家
                        case KeyEvent.VK_W -> player1.setUp(true);
                        case KeyEvent.VK_S -> player1.setDown(true);
                        case KeyEvent.VK_D -> player1.setRight(true);
                        case KeyEvent.VK_A -> player1.setLeft(true);
                        case KeyEvent.VK_SPACE -> player1.setFire(true);

                        // 二号玩家
                        case KeyEvent.VK_UP -> player2.setUp(true);
                        case KeyEvent.VK_DOWN -> player2.setDown(true);
                        case KeyEvent.VK_RIGHT -> player2.setRight(true);
                        case KeyEvent.VK_LEFT -> player2.setLeft(true);
                        case KeyEvent.VK_ENTER -> player2.setFire(true);
                    }
                } else {
                    switch (keyEvent.getKeyCode()) {
                        // esc暂停
                        case KeyEvent.VK_ESCAPE -> {
                            pause();
                        }
                        // 一号玩家
                        case KeyEvent.VK_W -> player1.setUp(true);
                        case KeyEvent.VK_S -> player1.setDown(true);
                        case KeyEvent.VK_D -> player1.setRight(true);
                        case KeyEvent.VK_A -> player1.setLeft(true);
                        case KeyEvent.VK_SPACE -> player1.setFire(true);
                    }
                }
                setCurDir();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (isDoubleGame) {
                    switch (keyEvent.getKeyCode()) {
                        // 一号玩家
                        case KeyEvent.VK_W -> player1.setUp(false);
                        case KeyEvent.VK_S -> player1.setDown(false);
                        case KeyEvent.VK_D -> player1.setRight(false);
                        case KeyEvent.VK_A -> player1.setLeft(false);
                        case KeyEvent.VK_SPACE -> player1.setFire(false);
                        // 二号玩家
                        case KeyEvent.VK_UP -> player2.setUp(false);
                        case KeyEvent.VK_DOWN -> player2.setDown(false);
                        case KeyEvent.VK_RIGHT -> player2.setRight(false);
                        case KeyEvent.VK_LEFT -> player2.setLeft(false);
                        case KeyEvent.VK_ENTER -> player2.setFire(false);
                    }
                } else {
                    switch (keyEvent.getKeyCode()) {
                        // 一号玩家
                        case KeyEvent.VK_W -> player1.setUp(false);
                        case KeyEvent.VK_S -> player1.setDown(false);
                        case KeyEvent.VK_D -> player1.setRight(false);
                        case KeyEvent.VK_A -> player1.setLeft(false);
                        case KeyEvent.VK_SPACE -> player1.setFire(false);
                    }
                }
                setCurDir();
            }

            public void moveAndFire(PlayerTank player) {
                player.setFiring(player.getFire());
                if (!player.getUp() && !player.getDown() && !player.getRight() && !player.getLeft()) {
                    player.setMoving(false);
                } else {
                    // 如果开火间隔达不到直接return
                    if (player.getUp()) player.setDir(Const.UP);
                    if (player.getDown()) player.setDir(Const.DOWN);
                    if (player.getRight()) player.setDir(Const.RIGHT);
                    if (player.getLeft()) player.setDir(Const.LEFT);
                    player.setMoving(true);
                }
            }

            public void setCurDir() {
                moveAndFire(player1);
                if (isDoubleGame) {
                    moveAndFire(player2);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        // 画背景
        Graphics2D g = (Graphics2D) g1;
        g.drawImage(Toolkit.getDefaultToolkit().getImage("src/res/drawable/game_background.png"), 0, 0, Const.GAME_WIDTH, Const.GAME_HEIGHT, this);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("src/res/drawable/game_score_panel.png"), Const.GAME_WIDTH, 0, 200, Const.GAME_HEIGHT, this);
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(3));
        g.drawLine(Const.GAME_WIDTH, 0, Const.GAME_WIDTH, Const.GAME_HEIGHT);

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
                if (this.getCurLevel() < 2) {
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
        g.setFont(new Font("微软雅黑", Font.BOLD, 36));
        g.setColor(Color.RED);
        g.drawString(String.valueOf(this.getCurLevel()), Const.GAME_WIDTH + 88, 130);
        g.drawString(String.valueOf(this.getScores()), Const.GAME_WIDTH + 105, 260);
        g.drawString(String.valueOf(this.getLeftEnemy()), Const.GAME_WIDTH + 105, 335);
        g.drawString(String.valueOf(Const.Max_Enemy - this.getLeftEnemy() - this.getCurEnemyCnt()), Const.GAME_WIDTH + 105, 405);
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
                case 2 -> {
                    if (this.getRandom().nextInt(2) == 0) {
                        x = Const.Enemy_x1;
                    } else {
                        x = Const.Enemy_x2;
                    }
                }
                case -1 -> x = Const.Enemy_x1;
                case 1 -> x = Const.Enemy_x2;
                default -> {
                }
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
            if (Utils.isCollide(Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                a = 1;
            }
            if (Utils.isCollide(Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, tank.getX(), tank.getY(), Const.TANK_WIDTH)) {
                b = 1;
            }
        }

        for (Birth birth : this.births) {
            if (Utils.isCollide(Const.Enemy_x1 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
                a = 1;
            }
            if (Utils.isCollide(Const.Enemy_x2 * Const.WIDTH, Const.Enemy_y * Const.WIDTH, Const.TANK_WIDTH, birth.getX(), birth.getY(), Const.TANK_WIDTH)) {
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

    private void pause() {
        isPause = !isPause;
        JDialog dialog = new JDialog(game, "", true);
        dialog.setLayout(null);
        dialog.setBounds(550, 440, 400, 190);
        // 这个面板加载一张黑色的背景
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(43, 43, 43));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        panel.setBounds(0, 0, 400, 200);
        // 坦克图标
        JLabel focusIcon = new JLabel(new ImageIcon("src/res/drawable/game_select.png"));
        focusIcon.setBounds(50, 41, 45, 45);
        panel.add(focusIcon);

        Font font = new Font("微软雅黑", Font.PLAIN, 34);
        JButton continueGame = new JButton("继续游戏");
        continueGame.setBounds(110, 40, 205, 45);
        continueGame.setContentAreaFilled(false);
        continueGame.setFocusPainted(false);
        continueGame.setFont(font);
        continueGame.setForeground(Color.RED);
        continueGame.addActionListener(e -> {
            System.out.println("继续游戏");
            isPause = !isPause;
            dialog.dispose();
        });

        JButton backMain = new JButton("返回主菜单");
        backMain.setBounds(110, 110, 205, 45);
        backMain.setFont(font);
        backMain.setForeground(Color.RED);
        backMain.setFocusPainted(false);
        backMain.setContentAreaFilled(false);
        backMain.addActionListener(e -> {
            System.out.println("返回主菜单");
            dialog.dispose();
            game.welcome();
        });
        continueGame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    backMain.requestFocus();
                    focusIcon.setBounds(50, 111, 45, 40);
                }
            }
        });
        continueGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                focusIcon.setBounds(50, 41, 45, 40);
            }
        });
        backMain.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    continueGame.requestFocus();
                    focusIcon.setBounds(50, 41, 45, 40);
                }
            }
        });
        backMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                focusIcon.setBounds(50, 111, 45, 40);
            }
        });
        panel.add(continueGame);
        panel.add(backMain);
        dialog.add(panel);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
        dialog.setResizable(false);
    }

    public Vector<Barrier> getBarriers() {
        return barriers;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public Random getRandom() {
        return random;
    }

    public Vector<Tank> getTanks() {
        return tanks;
    }

    public Vector<Explosion> getExplosions() {
        return explosions;
    }

    public Vector<Prop> getProps() {
        return props;
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

    public void setLeftEnemy(int leftEnemy) {
        this.leftEnemy = leftEnemy;
    }

    public long getLastCanAddEneTime() {
        return lastCanAddEneTime;
    }

    public void setLastCanAddEneTime(long lastCanAddEneTime) {
        this.lastCanAddEneTime = lastCanAddEneTime;
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
}