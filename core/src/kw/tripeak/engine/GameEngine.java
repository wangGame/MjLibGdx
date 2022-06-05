package kw.tripeak.engine;

import static kw.tripeak.cmd.GameCmd.GAME_PLAYER;
import static kw.tripeak.cmd.GameCmd.INVALID_CHAIR;
import static kw.tripeak.cmd.GameCmd.MAX_COUNT;
import static kw.tripeak.cmd.GameCmd.MAX_DISCARD;
import static kw.tripeak.cmd.GameCmd.MAX_INDEX;
import static kw.tripeak.cmd.GameCmd.MAX_REPERTORY;
import static kw.tripeak.cmd.GameCmd.MAX_WEAVE;

import com.kw.gdx.log.NLog;

import kw.tripeak.cmd.GameCmd;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.IPlayer;

public class GameEngine {
    static GameEngine pGameEngine = null;
    enum EstimateKind {
        EstimateKind_OutCard,            //出牌效验
        EstimateKind_GangCard,            //杠牌效验
    };
    public class IGameEngineEventListener {
        /**
         * 设置玩家
         * @param pIPlayer
         */
        public void setIPlayer(IPlayer pIPlayer) {
        }

        /**
         * 玩家进入通知
         * @param pIPlayer
         * @return
         */
        boolean onUserEnterEvent(IPlayer pIPlayer){
            return false;
        }

        /**
         * 游戏开始
         * @param GameStart
         * @return
         */
         boolean onGameStartEvent(GameCmd.CMD_S_GameStart GameStart){
             return false;
         }

        /**
         * 发牌事件
         * @param SendCard
         * @return
         */
        boolean onSendCardEvent(GameCmd.CMD_S_SendCard SendCard){
            return false;
        }

        /**
         * 出牌事件
         * @param OutCard
         * @return
         */
        boolean onOutCardEvent(GameCmd.CMD_S_OutCard OutCard) {
            return false;
        }

        /**
         * 操作通知事件
         * @param OperateNotify
         * @return
         */
         boolean onOperateNotifyEvent(GameCmd.CMD_S_OperateNotify OperateNotify){
             return false;
         }

        /**
         * 操作结果事件
         * @param OperateResult
         * @return
         */
         boolean onOperateResultEvent(GameCmd.CMD_S_OperateResult OperateResult){
             return false;
         }

        /**
         * 游戏结束事件
         * @param GameEnd
         * @return
         */
         boolean onGameEndEvent(GameCmd.CMD_S_GameEnd GameEnd){
             return false;
         }

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
    public GameLogic.tagWeaveItem m_WeaveItemArray[][] = new GameLogic.tagWeaveItem[GAME_PLAYER][MAX_WEAVE];//组合扑克
    public void init(){
        m_cbLeftCardCount = 0;
        m_cbCurrentUser = INVALID_CHAIR;
        m_cbProvideUser = INVALID_CHAIR;
        m_cbResumeUser = INVALID_CHAIR;
        m_cbProvideCard = 0;
        m_cbSendCardCount = 0;
        m_cbSendCardData = 0;
        m_cbMa = 0;
//        memset(m_cbGangCard, 0, sizeof(m_cbGangCard));                                    //重置杠的牌
//        memset(m_llHuRight, 0, sizeof(m_llHuRight));                                      //清空胡牌类型
//        memset(m_cbHuKind, 0, sizeof(m_cbHuKind));                                        //清空胡牌方式
//        memset(m_cbHuSpecial, 0, sizeof(m_cbHuSpecial));                                  //清空胡牌方式
//        memset(m_cbTempUserAction, 0, sizeof(m_cbTempUserAction));                        //清空临时动作
//        memset(m_cbWeaveItemCount, 0, sizeof(m_cbWeaveItemCount));                        //清空组合
//        memset(m_cbUserAction, 0, sizeof(m_cbUserAction));                                //玩家动作
//        memset(m_cbTempUserAction, 0, sizeof(m_cbTempUserAction));                        //临时动作
//        memset(m_cbOperateCard, 0, sizeof(m_cbOperateCard));                              //操作的牌
//        memset(m_cbPerformAction, 0, sizeof(m_cbPerformAction));                          //自动默认动作
//        memset(m_cbFanShu, 0, sizeof(m_cbFanShu));                                        //结算番数
//        memset(m_lGameScoreTable, 0, sizeof(m_lGameScoreTable));                          //桌子分
//        for (uint8_t i = 0; i < GAME_PLAYER; i++) {
//            memset(&m_cbCardIndex[i], 0, sizeof(m_cbCardIndex[i]));
//            memset(&m_cbPassPeng[i], 0, sizeof(m_cbPassPeng[i]));
//            memset(&m_cbDiscardCard[i], 0, sizeof(m_cbDiscardCard[i]));
//            memset(&m_WeaveItemArray[i], 0, sizeof(m_WeaveItemArray[i]));
//        }
    }    //初始化数据
    public boolean onGameStart(){
        m_GameLogic.shuffle(m_cbRepertoryCard, m_cbRepertoryCard.length);        //洗牌
        iDiceCount = (int) (Math.random() % 6 + 1 + Math.random() % 6 + 1);    //骰子点数
        if (m_cbBankerUser == INVALID_CHAIR) {
            m_cbBankerUser = (iDiceCount % GAME_PLAYER);        //确定庄家
        }
        m_cbLeftCardCount = m_cbRepertoryCard.length;  //剩余排
        for (int i = 0; i < m_CurrChair; i++) {
            m_cbLeftCardCount -= (MAX_COUNT - 1);            //发牌13张
            m_GameLogic.switchToCardIndex(m_cbRepertoryCard[m_cbLeftCardCount], MAX_COUNT - 1, m_cbCardIndex[i]); //初始化用户扑克到 m_cbCardIndex 数组
        }
        //设置变量
        m_cbProvideCard = 0;            //初始化供应扑克
        m_cbProvideUser = INVALID_CHAIR;    //初始化供应玩家
        m_cbCurrentUser = m_cbBankerUser;    //设置当前操作玩家为庄家
        //构造数据
        CMD_S_GameStart GameStart;
        GameStart.iDiceCount = iDiceCount;
        GameStart.cbBankerUser = m_cbBankerUser;
        GameStart.cbCurrentUser = m_cbCurrentUser;
        GameStart.cbLeftCardCount = m_cbLeftCardCount - m_cbMa;

        for (int i = 0; i < m_CurrChair; i++) {      //通知全部玩家开始游戏
            m_GameLogic->switchToCardData(m_cbCardIndex[i], GameStart.cbCardData, MAX_COUNT);
            if (m_pIPlayer[i]->isAndroid()) {   //机器人作弊用，用于分析其他玩的牌
                uint8_t bIndex = 1;
                for (uint8_t j = 0; j < GAME_PLAYER; j++) {
                    if (j == i) continue;
                    m_GameLogic->switchToCardData(m_cbCardIndex[j], &GameStart.cbCardData[MAX_COUNT * bIndex++], MAX_COUNT);
                }
            }
            IGameEngineEventListener *pListener = m_pIPlayer[i]->getGameEngineEventListener();
            if (pListener != NULL) {
                pListener->onGameStartEvent(GameStart);
            }
        }
        dispatchCardData(m_cbCurrentUser);
        return true;    m_GameLogic->shuffle(m_cbRepertoryCard, sizeof(m_cbRepertoryCard));        //洗牌
        iDiceCount = static_cast<uint32_t>(rand() % 6 + 1 + rand() % 6 + 1);    //骰子点数
        if (m_cbBankerUser == INVALID_CHAIR) {
            m_cbBankerUser = static_cast<uint8_t>(iDiceCount % GAME_PLAYER);        //确定庄家
        }
        m_cbLeftCardCount = sizeof(m_cbRepertoryCard);  //剩余排
        for (uint8_t i = 0; i < m_CurrChair; i++) {
            m_cbLeftCardCount -= (MAX_COUNT - 1);            //发牌13张
            m_GameLogic->switchToCardIndex(&m_cbRepertoryCard[m_cbLeftCardCount], MAX_COUNT - 1, m_cbCardIndex[i]); //初始化用户扑克到 m_cbCardIndex 数组
        }
        //设置变量
        m_cbProvideCard = 0;            //初始化供应扑克
        m_cbProvideUser = INVALID_CHAIR;    //初始化供应玩家
        m_cbCurrentUser = m_cbBankerUser;    //设置当前操作玩家为庄家
        //构造数据
        CMD_S_GameStart GameStart;
        GameStart.iDiceCount = iDiceCount;
        GameStart.cbBankerUser = m_cbBankerUser;
        GameStart.cbCurrentUser = m_cbCurrentUser;
        GameStart.cbLeftCardCount = m_cbLeftCardCount - m_cbMa;

        for (int i = 0; i < m_CurrChair; i++) {      //通知全部玩家开始游戏
            m_GameLogic->switchToCardData(m_cbCardIndex[i], GameStart.cbCardData, MAX_COUNT);
            if (m_pIPlayer[i]->isAndroid()) {   //机器人作弊用，用于分析其他玩的牌
                uint8_t bIndex = 1;
                for (uint8_t j = 0; j < GAME_PLAYER; j++) {
                    if (j == i) continue;
                    m_GameLogic->switchToCardData(m_cbCardIndex[j], &GameStart.cbCardData[MAX_COUNT * bIndex++], MAX_COUNT);
                }
            }
            IGameEngineEventListener *pListener = m_pIPlayer[i]->getGameEngineEventListener();
            if (pListener != NULL) {
                pListener->onGameStartEvent(GameStart);
            }
        }
        dispatchCardData(m_cbCurrentUser);
        return true;
    }     //开始游戏
    public boolean onGameRestart(){
        init();
        onGameStart();
        return true;
    }   //重新开始
    public boolean onUserOutCard(GameCmd.CMD_C_OutCard OutCard){
        return false;
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
            if (pListener != NULL) {
                pListener->onUserEnterEvent(pIPlayer);
            }
        }
        if (m_CurrChair == GAME_PLAYER) {    //人满了，开始游戏
            onGameStart();
        }
        return true;
    }    //玩家进入
    public boolean dispatchCardData(int cbCurrentUser, boolean bTail){
        return false;
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
