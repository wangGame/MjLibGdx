package kw.tripeak.Ai;

import static kw.tripeak.cmd.GameCmd.GAME_PLAYER;
import static kw.tripeak.cmd.GameCmd.INVALID_CHAIR;
import static kw.tripeak.cmd.GameCmd.MAX_COUNT;
import static kw.tripeak.cmd.GameCmd.MAX_DISCARD;
import static kw.tripeak.cmd.GameCmd.MAX_INDEX;
import static kw.tripeak.cmd.GameCmd.MAX_WEAVE;
import static kw.tripeak.logic.GameLogic.WIK_G;
import static kw.tripeak.logic.GameLogic.WIK_H;
import static kw.tripeak.logic.GameLogic.WIK_NULL;
import static kw.tripeak.logic.GameLogic.WIK_P;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.log.NLog;

import kw.tripeak.cmd.GameCmd;
import kw.tripeak.engine.GameEngine;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.IPlayer;
import kw.tripeak.screen.IGameEngineEventListener;

public class AIEngine extends Actor implements IGameEngineEventListener {
    private GameEngine m_GameEngine;//游戏引擎
    private GameLogic m_GameLogic;//游戏逻辑
    private int m_cbSendCardData;//AI玩家
    private IPlayer m_MePlayer;//发送的牌
    private GameLogic.TagWeaveItem m_WeaveItemArray[][];                 //组合
    private int m_cbCardIndex[][];                         //玩家牌
    private int m_cbWeaveItemCount[] = new int[GAME_PLAYER];                               //组合数目
    private int m_cbDiscardCount[] = new int[GAME_PLAYER];                                 //丢弃数目
    private int m_cbDiscardCard[][];                     //丢弃记录
    private int m_cbLeftCardCount;                                             //剩余
    private int m_cbBankerChair;                                               //庄
    private int m_MeChairID;                                                   //自己的位置

    public AIEngine() {
        m_GameEngine = GameEngine.GetGameEngine();
        m_GameLogic = new GameLogic();
        m_cbSendCardData = 0;
        m_MeChairID = INVALID_CHAIR;
        initGame();
//        GameSceneManager.getInstance().getScene()->add(this, -1);
        //将节点加入到场景，用于启动定时任务
    }

    /**
     * 初始化游戏变量
     */
    void initGame(){
        m_cbSendCardData = 0;
        m_cbLeftCardCount = 0;
        m_cbBankerChair = INVALID_CHAIR;
        m_cbCardIndex = new int[GAME_PLAYER][MAX_INDEX];
        m_WeaveItemArray = new GameLogic.TagWeaveItem[GAME_PLAYER][MAX_WEAVE];
        m_cbDiscardCard = new int[GAME_PLAYER][MAX_DISCARD];
    }

    public void setIPlayer(IPlayer pIPlayer) {
        this.m_MePlayer = pIPlayer;
    }

    public boolean onUserEnterEvent(IPlayer pIPlayer) {
        this.m_MeChairID = m_MePlayer.getChairID();
        return true;
    }

    public boolean onGameStartEvent(GameCmd.CMD_S_GameStart GameStart) {
        NLog.i("机器人接收到游戏开始事件");
        initGame();
        m_cbLeftCardCount = GameStart.cbLeftCardCount;
        m_cbBankerChair = GameStart.cbBankerUser;
        m_GameLogic.switchToCardIndex(GameStart.cbCardData,
                MAX_COUNT - 1, m_cbCardIndex[m_MeChairID]);
        return true;
    }

    public boolean onSendCardEvent(GameCmd.CMD_S_SendCard SendCard) {
        if (SendCard.cbCurrentUser == m_MeChairID) { //出牌
            m_cbLeftCardCount--;
            if (SendCard.cbCurrentUser == m_MeChairID) {
                NLog.i("机器人接收到发牌事件");
                m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(SendCard.cbCardData)]++;
            }
            m_cbSendCardData = SendCard.cbCardData;
            sendCard(0);
        }
        return true;
    }

    /**
     * 出牌事件
     * @param OutCard
     * @return
     */
    public boolean onOutCardEvent(GameCmd.CMD_S_OutCard OutCard) {
        if (OutCard.cbOutCardUser == m_MeChairID) {
            NLog.i("机器人接收到出牌事件");
            m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(OutCard.cbOutCardData)]--;
        }
        m_cbDiscardCard[OutCard.cbOutCardUser][m_cbDiscardCount[OutCard.cbOutCardUser]++] = OutCard.cbOutCardData;
        return true;
    }

    @Override
    public boolean onOperateNotifyEvent(GameCmd.CMD_S_OperateNotify OperateNotify) {
        NLog.e("机器人接收到操作通知事件");
        if (OperateNotify.cbActionMask == WIK_NULL) {
            return true; //无动作
        }
        GameCmd.CMD_C_OperateCard OperateCard = new GameCmd.CMD_C_OperateCard();
//        memset(&OperateCard, 0, sizeof(CMD_C_OperateCard));     //重置内存
        OperateCard.cbOperateUser = m_MeChairID;   //操作的玩家
        if ((OperateNotify.cbActionMask & WIK_H) != 0) {        //胡的优先级最高
            OperateCard.cbOperateCode = WIK_H;
            OperateCard.cbOperateCard = OperateNotify.cbActionCard;
        } else if ((OperateNotify.cbActionMask & WIK_G) != 0) { //杠的优先级第二
            OperateCard.cbOperateCode = WIK_G;
            OperateCard.cbOperateCard = OperateNotify.cbGangCard[0];//杠第一个
        } else if ((OperateNotify.cbActionMask & WIK_P) != 0) { //碰的优先级第三
            OperateCard.cbOperateCode = WIK_P;
            OperateCard.cbOperateCard = OperateNotify.cbActionCard;
        }
        return m_GameEngine.onUserOperateCard(OperateCard);
    }

    @Override
    public boolean onOperateResultEvent(GameCmd.CMD_S_OperateResult OperateResult) {
        NLog.i("机器人接收到操作结果事件");
        GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
        switch (OperateResult.cbOperateCode) {
            case 0: {
                break;
            }
            case 1: {
                weaveItem.cbWeaveKind = WIK_P;
                weaveItem.cbCenterCard = OperateResult.cbOperateCard;
                weaveItem.cbPublicCard = true;
                weaveItem.cbProvideUser = OperateResult.cbProvideUser;
                weaveItem.cbValid = true;
                m_WeaveItemArray[OperateResult.cbOperateUser][m_cbWeaveItemCount[OperateResult.cbOperateUser]++] = weaveItem;
                if (OperateResult.cbOperateUser == m_MeChairID) { //自己出牌操作
                    int cbReomveCard[] = {OperateResult.cbOperateCard, OperateResult.cbOperateCard};
                    m_GameLogic.removeCard(m_cbCardIndex[OperateResult.cbOperateUser], cbReomveCard, cbReomveCard.length);
                    int cbTempCardData[] = new int[MAX_COUNT];
                    m_GameLogic.switchToCardData(m_cbCardIndex[m_MeChairID], cbTempCardData, (MAX_COUNT - 1 - (m_cbWeaveItemCount[m_MeChairID] * 3))); //碰完需要出一张
                    m_cbSendCardData = cbTempCardData[0];
                    sendCard(0);
                }
                break;
            }
            case 2: {
                weaveItem.cbWeaveKind = WIK_G;
                weaveItem.cbCenterCard = OperateResult.cbOperateCard;
                boolean cbPublicCard = (OperateResult.cbOperateUser == OperateResult.cbProvideUser) ? false : true;
                int j = -1;
                for (int i = 0; i < m_cbWeaveItemCount[OperateResult.cbOperateUser]; i++) {
                    GameLogic.TagWeaveItem tempWeaveItem = m_WeaveItemArray[OperateResult.cbOperateUser][i];
                    if (tempWeaveItem.cbCenterCard == OperateResult.cbOperateCard) {   //之前已经存在
                        cbPublicCard = true;
                        j = i;
                    }
                }
                weaveItem.cbPublicCard = cbPublicCard;
                weaveItem.cbProvideUser = OperateResult.cbProvideUser;
                weaveItem.cbValid = true;
                if (j == -1) {
                    m_WeaveItemArray[OperateResult.cbOperateUser][m_cbWeaveItemCount[OperateResult.cbOperateUser]++] = weaveItem;
                } else {
                    m_WeaveItemArray[OperateResult.cbOperateUser][j] = weaveItem;
                }
                if (OperateResult.cbOperateUser == m_MeChairID) {  //自己
                    m_GameLogic.removeAllCard(m_cbCardIndex[OperateResult.cbOperateUser], OperateResult.cbOperateCard);
                }
                break;
            }
            case 4: {
                break;
            }
            default:
                break;
        }
        return true;
    }

    /**
     * 结束
     * @param GameEnd
     * @return
     */
    public boolean onGameEndEvent(GameCmd.CMD_S_GameEnd GameEnd) {
        NLog.i("机器人接收到游戏结束事件");
        return true;
    }

    /**
     * 出牌
     * @param f
     */
    public void sendCard(float f) {
        NLog.i("机器人出牌:%x",m_cbSendCardData);
        GameCmd.CMD_C_OutCard OutCard = new GameCmd.CMD_C_OutCard();
//        memset(&OutCard, 0, sizeof(CMD_C_OutCard));
        OutCard.cbCardData = m_cbSendCardData;
        m_GameEngine.onUserOutCard(OutCard);
    }
}
