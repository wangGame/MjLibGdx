package kw.tripeak.cmd;

public class GameCmd {

    public static int INVALID_CHAIR             =   0xFF       ;                       //无效椅子
    public static int INVALID_BYTE              =   0xFF       ;                       //无效BYTE
    public static int GAME_PLAYER               =   4          ;                       //玩家数量
    public static int MAX_WEAVE                 =   4          ;                       //最大组合
    public static int MAX_INDEX                 =   34         ;                       //最大索引
    public static int MAX_REPERTORY             =   136        ;                       //最大库存
    public static int MAX_COUNT                 =   14         ;                       //最大数
    public static int MAX_MA                    =   8          ;                       //最大马数
    public static int MAX_DISCARD               =   60;

    public class CMD_WeaveItem {
        public int cbWeaveKind;                        //组合类型
        public int cbCenterCard;                       //中心扑克
        public int cbPublicCard;                       //公开标志
        public int cbProvideUser;                      //供应用户
        public int cbValid;                            //有效标识
    };

    //游戏开始
    public class CMD_S_GameStart {
        public int iDiceCount;                         //骰子点数
        public int cbBankerUser;                        //庄家用户
        public int cbCurrentUser;                       //当前用户
        public int cbCardData[] = new int[MAX_COUNT * GAME_PLAYER]; //扑克列表
        public int cbLeftCardCount;                     //剩余数目
    };

    //出牌命令
    public class CMD_S_OutCard {
        public int cbOutCardUser;                        //出牌用户
        public int cbOutCardData;                        //出牌扑克
    };

    //发牌命令
    public class CMD_S_SendCard {
        public int cbCardData;                            //扑克数据
        public int cbActionMask;                        //动作掩码
        public int cbCurrentUser;                        //当前用户
        public int cbGangCount;                        //可以杠的数量
        public int []cbGangCard = new int[MAX_WEAVE];              //杠的牌
        public boolean bTail;                                //末尾发牌
    };


    //操作提示
    public class CMD_S_OperateNotify {
        public int cbResumeUser;                        //还原用户
        public int cbActionMask;                        //动作掩码
        public int cbActionCard;                        //动作扑克
        public int cbGangCount;                         //可以杠的数量
        public int cbGangCard[] = new int[MAX_WEAVE];               //可以杠的牌
    };

    //操作结果
    public class CMD_S_OperateResult {
        public int cbOperateUser;                        //操作用户
        public int cbProvideUser;                        //供应用户
        public int cbOperateCode;                        //操作代码
        public int cbOperateCard;                        //操作扑克
    };

    //游戏结束
    public class CMD_S_GameEnd {
        public int cbCardCount[] = new int[GAME_PLAYER];                //扑克总数
        public int cbCardData[][] = new int[GAME_PLAYER][MAX_COUNT];      //扑克数据
        public int cbHuUser;                                //胡牌人员
        public int cbProvideUser;                           //供应用户
        public int cbHuCard;                                //供应扑克
        public int dwHuRight[] = new int[GAME_PLAYER];                 //胡牌类型
        public int cbHuKind[] = new int[GAME_PLAYER];                   //胡牌方式
        public int cbHuSpecial[] = new int[GAME_PLAYER];                //特殊情况
        public int cbWeaveCount[] = new int[GAME_PLAYER];               //组合数量
        public CMD_WeaveItem WeaveItemArray[][] = new CMD_WeaveItem[GAME_PLAYER][MAX_WEAVE];    //组合扑克
        public int cbMaCard[] = new int[MAX_MA];                                //扎鸟
        public int lMaGameScore[] = new int[GAME_PLAYER];                      //单局马积分
        public int lNormalGameScore[]= new int[GAME_PLAYER];                  //单局常规积分
        public int lGameScore[] = new int[GAME_PLAYER];                //单局积分
        public int lGameScoreTable[] = new int[GAME_PLAYER];           //总局积分
    };

    //出牌命令
    public class CMD_C_OutCard {
        public int cbCardData;                            //扑克数据
    };

    //操作命令
    public class CMD_C_OperateCard {
        public int cbOperateUser;                         //操作玩家
        public int cbOperateCode;                         //操作代码
        public int cbOperateCard;                         //操作扑克
    };
}
