package model;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private JPanel panel;
    private int curLevel;

    public Game() {
        setTitle("Tank War");
        this.setSize(Const.GAME_WIDTH + 15 + 200, Const.GAME_HEIGHT + 38);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        curLevel = 1;
        setResizable(false);
    }

    public void welcome() {
        String bg = "src/res/drawable/welcome.png";
        JPanel wel = new Welcome(bg);
        JButton beginGame = new JButton("开始游戏");
        beginGame.setBounds(500, 460, 180, 60);
        beginGame.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        beginGame.setFocusPainted(false);
        beginGame.setForeground(Color.RED);
        beginGame.setContentAreaFilled(false);
        beginGame.addActionListener(actionEvent -> {
            gameOver();
            //playGame();
        });
        wel.add(beginGame);
        wel.add(this.getExitGameButton(0));
        this.setPanel(wel);
    }

//    public void playGame() {
//        this.setPanel(new MyPanel(this, this.curLevel));
//        this.getContentPane().requestFocus();
//    }

    public void gameOver() {
        String bg = "src/res/drawable/game_over.png";
        JPanel over = new Welcome(bg);
        over.add(this.getBackMainButton());
        over.add(this.getExitGameButton(60));
        this.setPanel(over);
    }

    public void win() {
        String bg = "src/res/drawable/win.png";
        JPanel win = new Welcome(bg);
        win.add(this.getBackMainButton());
        win.add(this.getExitGameButton(60));
        this.setPanel(win);
    }

    public void passGame() {
        String bg = "src/res/drawable/win.png";
        JPanel pass = new Welcome(bg);
        JButton nextLevel = new JButton("下一关");
        nextLevel.setBounds(516, 450, 150, 60);
        nextLevel.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        nextLevel.setFocusPainted(false);
        nextLevel.setForeground(Color.RED);
        nextLevel.setContentAreaFilled(false);
        nextLevel.addActionListener(actionEvent -> {
            setCurLevel(getCurLevel() + 1);
            //playGame();
        });
        pass.add(nextLevel);
        pass.add(this.getExitGameButton(60));
        pass.add(this.getBackMainButton());
        this.setPanel(pass);
    }


    public JButton getExitGameButton(int distance) {
        JButton exitGame = new JButton("退出游戏");
        exitGame.setBounds(500, 560 + distance, 180, 60);
        exitGame.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        exitGame.setForeground(Color.RED);
        exitGame.setContentAreaFilled(false);
        exitGame.addActionListener(actionEvent -> {
            passGame();
            //System.exit(0);
        });
        return exitGame;
    }

    public JButton getBackMainButton() {
        JButton backMain = new JButton("返回主菜单");
        backMain.setBounds(480, 530, 220, 60);
        backMain.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        backMain.setFocusPainted(false);
        backMain.setForeground(Color.RED);
        backMain.setContentAreaFilled(false);
        backMain.addActionListener(actionEvent -> welcome());
        return backMain;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
        panel.setLayout(null);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    public int getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.welcome();
    }
}