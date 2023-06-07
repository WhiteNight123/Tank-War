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
    public final static int TANK_NOR_SPEED = 5;
    public final static int Tank_INC_SPEED = 10;
    public final static int Tank_DEC_SPEED = 3;
    public final static int MAX_HP = 100;

    // 追击
    public final static int Short_Dis = 200;
    public final static int Long_Dis = 500;
    public final static int CHANGE_DIR_GAP = 1500;  // 2秒
    public final static int Img_Gap = 200;

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
    public final static int PlayerFireGap = 200;
    public final static int EnemyFireGap = 1000;

    // buff
    public final static int propWidth = 30;
    public final static int prop_gap = 10000;
    public final static int changeBullet = 0;
    public final static int increase = 1;
    public final static int protect = 2;
    public final static int recovery = 3;
    public final static int P_prop = 5;
    public final static int last_time = 20000;

    // 出生地
    public final static int player1_x = 17;
    public final static int player1_y = 37;
    public final static int player2_x = 29;
    public final static int player2_y = 37;
    public final static int Enemy_x1 = 0;
    public final static int Enemy_x2 = 47;
    public final static int Enemy_y = 0;
    public final static int Max_Cur_Enemy = 4;
    public final static int Add_Gap = 2000;
}
