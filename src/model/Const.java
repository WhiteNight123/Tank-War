package model;

public class Const {
    public final static int GAME_WIDTH = 1000;
    public final static int GAME_HEIGHT = 800;
    public final static int TANK_WIDTH = 60;
    public final static int WIDTH = 20;
    public final static int PLAYER = 1;
    public final static int ENEMY = 0;

    // 控制
    public final static int UP = 1;
    public final static int DOWN = 2;
    public final static int RIGHT = 3;
    public final static int LEFT = 4;

    // 坦克
    public final static int MAX_HP = 100;

    // 障碍物
    public final static int NONE = 0;
    public final static int GRASS = 2;
    public final static int CAN_MOVE = 2;
    public final static int BRICK = 3;
    public final static int STEEL = 4;
    public final static int HOME = 5;

    // 子弹伤害
    public final static int BULLET_NOR = 1;
    public final static int BULLET_PRO = 2;
    public final static int NOR_DAMAGE = 20;
    public final static int PRO_DAMAGE = 30;
    public final static int DAMAGE_RAND = 10; // 伤害浮动
    public final static int BULLET_NOR_SPEED = 10;
    public final static int BULLET_PRO_SPEED = 13;

    // 出生地
    public final static int Enemy_x1 = 0;
    public final static int Enemy_x2 = 47;
    public final static int Enemy_y = 0;
    public final static int Max_Cur_Enemy = 4;
    public final static int Max_Enemy = 20;
}
