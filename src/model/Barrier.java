package model;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Barrier {
    private int type;
    private int x;
    private int y;
    private int width;
    private boolean alive;
    private MyPanel myPanel;

    public Barrier(int type, int x, int y, int width, MyPanel myPanel) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.alive = true;
        this.myPanel = myPanel;
    }

    public static Vector<Barrier> readMap(int level, MyPanel myPanel) {
        // 从txt读取地图
        String path = "map/map" + level + ".txt";
        Vector<Barrier> barriers = new Vector<>();
        barriers.add(new Barrier(Const.HOME, 23 * Const.WIDTH, 37 * Const.WIDTH, Const.TANK_WIDTH, myPanel));
        BufferedReader reader;
        int row = 0;
        String line;
        try {
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) > '0' && line.charAt(j) < '9') {
                        switch (line.charAt(j)) {
                            case '2' ->
                                    barriers.add(new Barrier(Const.GRASS, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH, myPanel));
                            case '3' ->
                                    barriers.add(new Barrier(Const.BRICK, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH, myPanel));
                            case '4' ->
                                    barriers.add(new Barrier(Const.STEEL, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH, myPanel));
                            default -> {
                            }
                        }
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barriers;
    }

    public void draw(Graphics g, MyPanel myPanel) {
        String path = null;
        switch (this.type) {
            case Const.HOME -> path = "src/res/drawable/barrier_home.png";
            case Const.GRASS -> path = "src/res/drawable/barrier_grass.png";
            case Const.BRICK -> path = "src/res/drawable/barrier_brick.png";
            case Const.STEEL -> path = "src/res/drawable/barrier_steel.png";
            default -> {
            }
        }
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path), this.getX(), this.getY(), this.getWidth(), this.getWidth(), myPanel);
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isAlive() {
        return alive;
    }

    public MyPanel getMyPanel() {
        return myPanel;
    }

    public void setMyPanel(MyPanel myPanel) {
        this.myPanel = myPanel;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
