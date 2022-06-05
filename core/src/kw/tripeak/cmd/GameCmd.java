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
        int cbWeaveKind;                        //组合类型
        int cbCenterCard;                       //中心扑克
        int cbPublicCard;                       //公开标志
        int cbProvideUser;                      //供应用户
        int cbValid;                            //有效标识
    };

    //游戏开始
    public class CMD_S_GameStart {
        int iDiceCount;                         //骰子点数
        int cbBankerUser;                        //庄家用户
        int cbCurrentUser;                       //当前用户
        int cbCardData[] = new int[MAX_COUNT * GAME_PLAYER]; //扑克列表
        int cbLeftCardCount;                     //剩余数目
    };

    //出牌命令
    public class CMD_S_OutCard {
        int cbOutCardUser;                        //出牌用户
        int cbOutCardData;                        //出牌扑克
    };

    //发牌命令
    public class CMD_S_SendCard {
        int cbCardData;                            //扑克数据
        int cbActionMask;                        //动作掩码
        int cbCurrentUser;                        //当前用户
        int cbGangCount;                        //可以杠的数量
        int []cbGangCard = new int[MAX_WEAVE];              //杠的牌
        boolean bTail;                                //末尾发牌
    };


    //操作提示
    public class CMD_S_OperateNotify {
        int cbResumeUser;                        //还原用户
        int cbActionMask;                        //动作掩码
        int cbActionCard;                        //动作扑克
        int cbGangCount;                         //可以杠的数量
        int cbGangCard[] = new int[MAX_WEAVE];               //可以杠的牌
    };

    //操作结果
    public class CMD_S_OperateResult {
        int cbOperateUser;                        //操作用户
        int cbProvideUser;                        //供应用户
        int cbOperateCode;                        //操作代码
        int cbOperateCard;                        //操作扑克
    };

    //游戏结束
    public class CMD_S_GameEnd {
        int cbCardCount[] = new int[GAME_PLAYER];                //扑克总数
        int cbCardData[][] = new int[GAME_PLAYER][MAX_COUNT];      //扑克数据
        int cbHuUser;                                //胡牌人员
        int cbProvideUser;                           //供应用户
        int cbHuCard;                                //供应扑克
        int dwHuRight[] = new int[GAME_PLAYER];                 //胡牌类型
        int cbHuKind[] = new int[GAME_PLAYER];                   //胡牌方式
        int cbHuSpecial[] = new int[GAME_PLAYER];                //特殊情况
        int cbWeaveCount[] = new int[GAME_PLAYER];               //组合数量
        CMD_WeaveItem WeaveItemArray[][] = new CMD_WeaveItem[GAME_PLAYER][MAX_WEAVE];    //组合扑克
        int cbMaCard[] = new int[MAX_MA];                                //扎鸟
        int lMaGameScore[] = new int[GAME_PLAYER];                      //单局马积分
        int lNormalGameScore[]= new int[GAME_PLAYER];                  //单局常规积分
        int lGameScore[] = new int[GAME_PLAYER];                //单局积分
        int lGameScoreTable[] = new int[GAME_PLAYER];           //总局积分
    };

    //出牌命令
    public class CMD_C_OutCard {
        int cbCardData;                            //扑克数据
    };

    //操作命令
    public class CMD_C_OperateCard {
        int cbOperateUser;                         //操作玩家
        int cbOperateCode;                         //操作代码
        int cbOperateCard;                         //操作扑克
    };
}
