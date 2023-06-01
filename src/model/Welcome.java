package model;

import javax.swing.*;
import java.awt.*;

public class Welcome extends JPanel {
    private final String bg;

    public Welcome(String path) {
        this.bg = path;
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().getImage(bg), 0, 0, Const.GAME_WIDTH + 200, Const.GAME_HEIGHT, this);
    }
}
