package model;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private int curLevel;
    private boolean isDoubleGame;

    public Game() {
        setTitle("Tank War");
        this.setSize(Const.GAME_WIDTH + 15 + 200, Const.GAME_HEIGHT + 38);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/res/drawable/game_icon.png").getImage());
        curLevel = 1;
        setResizable(false);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.welcome();
    }

    public void welcome() {
        String bg = "src/res/drawable/game_welcome.png";
        JPanel wel = new Welcome(bg);
        wel.add(this.getSingleGameButton());
        wel.add(this.getDoubleGameButton());
        wel.add(this.getExitGameButton(10));
        this.setPanel(wel);
    }

    public void playGame() {
        this.setPanel(new MyPanel(this, this.curLevel, this.isDoubleGame));
        this.getContentPane().requestFocus();
    }

    public void gameOver() {
        String bg = "src/res/drawable/game_over.png";
        JPanel over = new Welcome(bg);
        over.add(this.getBackMainButton());
        over.add(this.getExitGameButton(60));
        this.setPanel(over);
    }

    public void win() {
        String bg = "src/res/drawable/game_win.png";
        JPanel win = new Welcome(bg);
        win.add(this.getBackMainButton());
        win.add(this.getExitGameButton(60));
        this.setPanel(win);
    }

    public void passGame() {
        String bg = "src/res/drawable/game_win.png";
        JPanel pass = new Welcome(bg);

        pass.add(this.getNextLevel());
        pass.add(this.getExitGameButton(60));
        pass.add(this.getBackMainButton());
        this.setPanel(pass);
    }

    public JButton getNextLevel(){
        JButton nextLevel = new JButton("下一关");
        nextLevel.setBounds(516, 450, 150, 60);
        nextLevel.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        nextLevel.setFocusPainted(false);
        nextLevel.setForeground(Color.RED);
        nextLevel.setContentAreaFilled(false);
        nextLevel.addActionListener(actionEvent -> {
            setCurLevel(getCurLevel() + 1);
            playGame();
        });
        return nextLevel;
    }

    public JButton getSingleGameButton() {
        JButton singleGame = new JButton("单人游戏");
        singleGame.setBounds(500, 420, 180, 60);
        singleGame.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        singleGame.setFocusPainted(false);
        singleGame.setForeground(Color.RED);
        singleGame.setContentAreaFilled(false);
        singleGame.addActionListener(actionEvent -> {
            isDoubleGame = false;
            playGame();
        });
        return singleGame;
    }

    public JButton getDoubleGameButton() {
        JButton doubleGame = new JButton("多人游戏");
        doubleGame.setBounds(500, 495, 180, 60);
        doubleGame.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        doubleGame.setFocusPainted(false);
        doubleGame.setForeground(Color.RED);
        doubleGame.setContentAreaFilled(false);
        doubleGame.addActionListener(actionEvent -> {
            isDoubleGame = true;
            playGame();
        });
        return doubleGame;
    }

    public JButton getExitGameButton(int distance) {
        JButton exitGame = new JButton("退出游戏");
        exitGame.setBounds(500, 560 + distance, 180, 60);
        exitGame.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        exitGame.setForeground(Color.RED);
        exitGame.setContentAreaFilled(false);
        exitGame.addActionListener(actionEvent -> {
            System.exit(0);
        });
        return exitGame;
    }

    public JButton getBackMainButton() {
        JButton backMain = new JButton("返回主菜单");
        backMain.setBounds(480, 535, 220, 60);
        backMain.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        backMain.setFocusPainted(false);
        backMain.setForeground(Color.RED);
        backMain.setContentAreaFilled(false);
        backMain.addActionListener(actionEvent -> welcome());
        return backMain;
    }

    public void setPanel(JPanel panel) {
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
}