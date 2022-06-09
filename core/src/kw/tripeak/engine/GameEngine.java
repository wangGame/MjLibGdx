package kw.tripeak.engine;

import static kw.tripeak.cmd.GameCmd.GAME_PLAYER;
import static kw.tripeak.cmd.GameCmd.INVALID_CHAIR;
import static kw.tripeak.cmd.GameCmd.MAX_COUNT;
import static kw.tripeak.cmd.GameCmd.MAX_DISCARD;
import static kw.tripeak.cmd.GameCmd.MAX_INDEX;
import static kw.tripeak.cmd.GameCmd.MAX_REPERTORY;
import static kw.tripeak.cmd.GameCmd.MAX_WEAVE;
import static kw.tripeak.logic.GameLogic.WIK_G;
import static kw.tripeak.logic.GameLogic.WIK_NULL;

import com.kw.gdx.log.NLog;

import kw.tripeak.cmd.GameCmd;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.IPlayer;
import kw.tripeak.screen.IGameEngineEventListener;

public class GameEngine {
    static GameEngine pGameEngine = null;
    enum EstimateKind {
        EstimateKind_OutCard,            //出牌效验
        EstimateKind_GangCard,            //杠牌效验
    };

    public IPlayer m_pIPlayer[] = new IPlayer[GAME_PLAYER];        //游戏玩家
    public GameLogic m_GameLogic;
    public int m_lGameScoreTable[] = new int[GAME_PLAYER];           //记录总分
    public int iDiceCount;                              //骰子点数
    public int m_CurrChair;                              //当前椅子数量
    public int m_cbBankerUser;                           //庄家用户
    public int m_cbCardIndex[][] = new int[GAME_PLAYER][MAX_INDEX];    //用户扑克
    public int m_cbMa;                                   //买马数量
    public int m_cbProvideCard;                          //当前供应的牌
    public int m_cbProvideUser;                          //供应的玩家
    public int m_cbResumeUser;                           //暂存玩家，用于碰、杠后恢复
    public int m_cbCurrentUser;                          //当前操作的玩家
    public int m_cbOutCardUser;                          //出牌玩家
    public int m_cbOutCardData;                          //出牌数据
    public int m_cbDiscardCount[] = new int[GAME_PLAYER];            //出牌数据
    public int m_cbDiscardCard[][] = new int[GAME_PLAYER][MAX_DISCARD];//丢弃记录
    public boolean m_bResponse[] = new boolean[GAME_PLAYER];                //响应标志
    public boolean m_cbPassPeng[][] = new boolean[GAME_PLAYER][MAX_INDEX];    //用来检测放弃碰
    public boolean m_bGangStatus;                           //杠上状态
    public boolean m_bQiangGangStatus;                      //抢杆状态
    public int m_cbUserAction[] = new int[GAME_PLAYER];          //用户动作，可能包含多个动作
    public int m_cbTempUserAction[] = new int[GAME_PLAYER];      //暂存上一次的动作情况，用来恢复自摸点“过”的情况。
    public int m_cbOperateCard[] = new int[GAME_PLAYER];         //操作扑克
    public int m_cbPerformAction[] = new int[GAME_PLAYER];       //执行动作，只会包含一个动作
    public int m_cbFanShu[] = new int[GAME_PLAYER];              //上一圈番数，用来判定没过手不能胡第二家
    public int m_cbGangCount;                        //当前可以杠的数量
    public int m_cbGangCard[] = new int[MAX_WEAVE];              //可以杠的牌
    public int m_cbTargetUser;                       //胡牌玩家标识
    public int m_cbHuCard;                           //胡牌扑克
    public int m_cbHuKind[] = new int[GAME_PLAYER];              //胡牌类型
    public int m_cbHuSpecial[] = new int[GAME_PLAYER];           //胡牌一些特殊情况
    public int m_llHuRight[] = new int[GAME_PLAYER];            //胡牌权重
    public int m_cbSendCardData;                     //发牌扑克
    public int m_cbSendCardCount;                    //发牌数目
    public int m_cbOutCardCount;                     //出牌数目
    public int m_cbLeftCardCount;                    //剩余数目
    public int m_cbRepertoryCard[] = new int[MAX_REPERTORY];     //库存扑克
    public int m_cbWeaveItemCount[] = new int[GAME_PLAYER];      //组合数目
    public GameLogic.TagWeaveItem m_WeaveItemArray[][] = new GameLogic.TagWeaveItem[GAME_PLAYER][MAX_WEAVE];//组合扑克
    public void init(){
        m_cbLeftCardCount = 0;
        m_cbCurrentUser = INVALID_CHAIR;
        m_cbProvideUser = INVALID_CHAIR;
        m_cbResumeUser = INVALID_CHAIR;
        m_cbProvideCard = 0;
        m_cbSendCardCount = 0;
        m_cbSendCardData = 0;
        m_cbMa = 0;
    }    //初始化数据

    public boolean onGameStart() {

        m_cbRepertoryCard = m_GameLogic.shuffle(m_cbRepertoryCard, m_cbRepertoryCard.length);        //洗牌
        iDiceCount = (int) (Math.random() % 6 + 1 + Math.random() % 6 + 1);    //骰子点数
        if (m_cbBankerUser == INVALID_CHAIR) {
            m_cbBankerUser = (iDiceCount % GAME_PLAYER);        //确定庄家
        }
        m_cbLeftCardCount = m_cbRepertoryCard.length;  //剩余排
        for (int i = 0; i < m_CurrChair; i++) {
            m_cbLeftCardCount -= (MAX_COUNT - 1);            //发牌13张
            m_GameLogic.switchToCardIndex(m_cbRepertoryCard,
                    MAX_COUNT - 1, m_cbCardIndex[i]); //初始化用户扑克到 m_cbCardIndex 数组
        }
        //设置变量
        m_cbProvideCard = 0;            //初始化供应扑克
        m_cbProvideUser = INVALID_CHAIR;    //初始化供应玩家
        m_cbCurrentUser = m_cbBankerUser;    //设置当前操作玩家为庄家
        //构造数据
//        GameCmd.CMD_S_GameStart GameStart = new GameCmd().new CMD_S_GameStart();
//        GameStart.iDiceCount = iDiceCount;
//        GameStart.cbBankerUser = m_cbBankerUser;
//        GameStart.cbCurrentUser = m_cbCurrentUser;
//        GameStart.cbLeftCardCount = m_cbLeftCardCount - m_cbMa;
//
//        for (int i = 0; i < m_CurrChair; i++) {      //通知全部玩家开始游戏
//            m_GameLogic.switchToCardData(m_cbCardIndex[i], GameStart.cbCardData, MAX_COUNT);
//            if (m_pIPlayer[i].isAndroid()) {   //机器人作弊用，用于分析其他玩的牌
//                int bIndex = 1;
//                for (int j = 0; j < GAME_PLAYER; j++) {
//                    if (j == i) continue;
//                    m_GameLogic.switchToCardData(m_cbCardIndex[j],
//                            GameStart.cbCardData, MAX_COUNT);
//                }
//            }
//            IGameEngineEventListener pListener = m_pIPlayer[i].getGameEngineEventListener();
//            if (pListener != null) {
//                pListener.onGameStartEvent(GameStart);
//            }
//        }
//        dispatchCardData(m_cbCurrentUser);
        return true;
    }     //开始游戏
    public boolean onGameRestart(){
        init();
        onGameStart();
        return true;
    }   //重新开始
    public boolean onUserOutCard(GameCmd.CMD_C_OutCard OutCard){
        if (m_cbUserAction[m_cbCurrentUser] != WIK_NULL) return true;            //存在操作不允许出牌，需要等操作结束
        if (!m_GameLogic.removeCard(m_cbCardIndex[m_cbCurrentUser], OutCard.cbCardData)) { //删除扑克
            return true;
        }
        //用户切换
        m_cbProvideUser = m_cbCurrentUser;
        m_cbProvideCard = OutCard.cbCardData;
        m_cbGangCount = 0;                                                  //本手牌杠的情况还原
//        memset(m_cbGangCard, 0, sizeof(m_cbGangCard));                      //重置内存
        m_bGangStatus = false;                                              //只要出完牌杠状态为false
        m_bQiangGangStatus = false;                                         //抢杠状态为false
//        memset(m_cbTempUserAction, 0, sizeof(m_cbTempUserAction));          //清空临时动作
        m_cbTargetUser = 0;                                                 //重置胡牌人员
        m_cbUserAction[m_cbCurrentUser] = WIK_NULL;                         //出牌的玩家动作为NULL
        m_cbPerformAction[m_cbCurrentUser] = WIK_NULL;                      //默认动作为NULL
        //出牌记录
        m_cbOutCardCount++;
        m_cbOutCardUser = m_cbCurrentUser;
        m_cbOutCardData = OutCard.cbCardData;

        //构造数据
        GameCmd.CMD_S_OutCard SOutCard = new GameCmd().new CMD_S_OutCard();
//        memset(&SOutCard, 0, sizeof(CMD_S_OutCard));                        //初始化内存
        SOutCard.cbOutCardUser = m_cbProvideUser;                           //出牌的用户
        SOutCard.cbOutCardData = OutCard.cbCardData;                        //出牌的数据
        for (int i = 0; i < GAME_PLAYER; i++) {
            m_pIPlayer[i].getGameEngineEventListener().onOutCardEvent(SOutCard); //出牌时间
        }
        boolean bAroseAction = estimateUserRespond(m_cbCurrentUser, OutCard.cbCardData,
                EstimateKind.EstimateKind_OutCard);     //响应判断
        if (!bAroseAction) {
            m_cbCurrentUser = ((m_cbCurrentUser + m_CurrChair - 1) % m_CurrChair);          //切换当前玩家发牌
            dispatchCardData(m_cbCurrentUser);    //派发扑克
        }
        return true;
    }   //出牌命令
    public boolean onEventGameConclude(int cbChairID){
        return false;
    } //结束游戏
    public boolean onUserEnter(IPlayer pIPlayer){
        if (m_CurrChair >= GAME_PLAYER) {
            NLog.i("玩家已满，无法加入！");
            return false;
        }
        pIPlayer.setChairID(m_CurrChair++);
        m_pIPlayer[pIPlayer.getChairID()] = pIPlayer;
        for (int i = 0; i < m_CurrChair; i++) {      //通知全部玩家有人进入游戏
            IGameEngineEventListener pListener = m_pIPlayer[i].getGameEngineEventListener();
            if (pListener != null) {
                pListener.onUserEnterEvent(pIPlayer);
            }
        }
        if (m_CurrChair == GAME_PLAYER) {    //人满了，开始游戏
//            onGameStart();
        }
        return true;
    }    //玩家进入

    public boolean dispatchCardData(int cbCurrentUser) {
        return dispatchCardData(cbCurrentUser,false);
    }
    public boolean dispatchCardData(int cbCurrentUser, boolean bTail){

        if ((m_cbOutCardUser != INVALID_CHAIR) && (m_cbOutCardData != 0))                        //往出牌记录里添加上一位出牌数据
        {
            m_cbDiscardCount[m_cbOutCardUser]++;
            m_cbDiscardCard[m_cbOutCardUser][m_cbDiscardCount[m_cbOutCardUser] - 1] = m_cbOutCardData;
        }
        m_cbTargetUser = 0;                                                               //重置操作人员
        m_cbOutCardData = 0;                                                              //重置出牌数据
        m_cbOutCardUser = INVALID_CHAIR;                                                  //重置出牌人员
        m_cbGangCount = 0;                                                                //重置杠的数量
//        memset(m_cbGangCard, 0, sizeof(m_cbGangCard));                                    //重置杠的牌
//        memset(m_llHuRight, 0, sizeof(m_llHuRight));                                      //清空胡牌类型
//        memset(m_cbHuKind, 0, sizeof(m_cbHuKind));                                        //清空胡牌方式
//        memset(m_cbHuSpecial, 0, sizeof(m_cbHuSpecial));                                  //清空胡牌方式
//        memset(m_cbTempUserAction, 0, sizeof(m_cbTempUserAction));                        //清空临时动作
        m_cbCurrentUser = cbCurrentUser;                                                  //设置当前玩家
        m_cbFanShu[cbCurrentUser] = 0;                                                    //牌过手则设置本局番数为0
//        memset(m_cbPassPeng[cbCurrentUser], 0, sizeof(m_cbPassPeng[cbCurrentUser]));       //牌过手重置同一张牌碰牌检测
        //剩余牌 == 马数量
        if (m_cbLeftCardCount == m_cbMa) {                                                //没牌发了,荒庄结束
            m_cbHuCard = 0;
            m_cbProvideUser = INVALID_CHAIR;
            onEventGameConclude(INVALID_CHAIR);                                     //流局
            return true;
        }
        m_cbSendCardCount++;                                                              //发牌数据计数
        m_cbSendCardData = m_cbRepertoryCard[--m_cbLeftCardCount];                        //获取要发的具体牌
        m_cbCardIndex[cbCurrentUser][m_GameLogic.switchToCardIndex(m_cbSendCardData)]++; //将牌发给当前玩家
        m_cbProvideUser = cbCurrentUser;                                                  //设置供应用户为当前玩家
        m_cbProvideCard = m_cbSendCardData;                                               //设置供应扑克为当前发的牌
        if (m_cbLeftCardCount > 0)                                                        //暗杠判定，剩下的牌>1才能杠
        {
            GameLogic.tagGangCardResult GangCardResult = new GameLogic().new tagGangCardResult();
            m_cbUserAction[cbCurrentUser] |= m_GameLogic.
                    analyseGangCard(m_cbCardIndex[cbCurrentUser],
                            m_WeaveItemArray[cbCurrentUser], m_cbWeaveItemCount[cbCurrentUser], GangCardResult);
            if ((m_cbUserAction[cbCurrentUser] & WIK_G) != 0x0) {                                //判定是否杠牌
                //记录杠的数量
                m_cbGangCount = GangCardResult.cbCardCount;
//                memcpy(m_cbGangCard, GangCardResult.cbCardData, sizeof(m_cbGangCard));    //杠的数量
            }
        }
        //胡牌判断
        int cbTempCardIndex[] = new int[MAX_INDEX];
//        memcpy(cbTempCardIndex, m_cbCardIndex[m_cbCurrentUser], sizeof(cbTempCardIndex));
        m_GameLogic.removeCard(cbTempCardIndex, m_cbSendCardData);    //移除发的那张牌进行分析
        //如果胡牌则是自摸
        m_cbUserAction[cbCurrentUser] |= m_GameLogic.analyseHuCard(cbTempCardIndex, m_WeaveItemArray[cbCurrentUser], m_cbWeaveItemCount[cbCurrentUser], m_cbSendCardData, m_cbHuKind[cbCurrentUser], m_llHuRight[cbCurrentUser], m_cbHuSpecial[cbCurrentUser], m_cbSendCardCount, m_cbOutCardCount, m_bGangStatus, true, m_bQiangGangStatus, m_cbFanShu[cbCurrentUser], false);
        if (m_cbUserAction[cbCurrentUser] != WIK_NULL) {    //存在暗杠、拐弯杠、或者自摸
            m_cbTempUserAction[cbCurrentUser] = m_cbUserAction[cbCurrentUser];
        }
        //构造数据
        GameCmd.CMD_S_SendCard SendCard = new GameCmd().new CMD_S_SendCard();
        SendCard.cbCurrentUser = cbCurrentUser;
        SendCard.cbActionMask = m_cbUserAction[cbCurrentUser];
        SendCard.cbCardData = m_cbSendCardData;
        SendCard.cbGangCount = m_cbGangCount;
//        memcpy(&SendCard.cbGa/**/ngCard, m_cbGangCard, sizeof(m_cbGangCard));
        SendCard.bTail = bTail;
        for (int i = 0; i < GAME_PLAYER; i++) {
            m_pIPlayer[i].getGameEngineEventListener().onSendCardEvent(SendCard); //出牌数据只发送给当前玩家(网游不能这么发牌，网游其他人的cbCardData要重置防止 透视挂)
        }
        return true;
    }    //发牌
    public boolean estimateUserRespond(int cbCurrentUser, int cbCurrentCard, EstimateKind estimateKind){
        return false;
    }  //检测响应
    public boolean sendOperateNotify(){
        return false;
    }   //发送操作通知
    public static GameEngine GetGameEngine(){
        if (pGameEngine == null) {
            pGameEngine = new GameEngine();
        }
        return pGameEngine;
    }  //获取单例

    public GameEngine(){
        m_CurrChair = 0;
        m_cbBankerUser = INVALID_CHAIR;
        m_GameLogic = new GameLogic();
        init();
    }
    public boolean onUserOperateCard(GameCmd.CMD_C_OperateCard OperateCard){
        return false;
    }
}
