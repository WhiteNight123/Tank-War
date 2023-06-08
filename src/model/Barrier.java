package model;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Barrier {
    private final int type;
    private final int x;
    private final int y;
    private final int width;
    private boolean alive;

    public Barrier(int type, int x, int y, int width) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.alive = true;
    }

    public static Vector<Barrier> readMap(int level) {
        // 从txt读取地图
        String path = "src/res/map/map" + level + ".txt";
        Vector<Barrier> barriers = new Vector<>();
        barriers.add(new Barrier(Const.HOME, 23 * Const.WIDTH, 37 * Const.WIDTH, Const.TANK_WIDTH));
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
                                    barriers.add(new Barrier(Const.GRASS, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH));
                            case '3' ->
                                    barriers.add(new Barrier(Const.BRICK, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH));
                            case '4' ->
                                    barriers.add(new Barrier(Const.STEEL, j * Const.WIDTH, row * Const.WIDTH, Const.WIDTH));
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }


    public boolean isAlive() {
        return alive;
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
