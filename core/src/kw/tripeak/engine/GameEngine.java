package kw.tripeak.engine;

import static kw.tripeak.cmd.GameCmd.GAME_PLAYER;
import static kw.tripeak.cmd.GameCmd.INVALID_BYTE;
import static kw.tripeak.cmd.GameCmd.INVALID_CHAIR;
import static kw.tripeak.cmd.GameCmd.MAX_COUNT;
import static kw.tripeak.cmd.GameCmd.MAX_DISCARD;
import static kw.tripeak.cmd.GameCmd.MAX_INDEX;
import static kw.tripeak.cmd.GameCmd.MAX_REPERTORY;
import static kw.tripeak.cmd.GameCmd.MAX_WEAVE;
import static kw.tripeak.engine.GameEngine.EstimateKind.EstimateKind_GangCard;
import static kw.tripeak.engine.GameEngine.EstimateKind.EstimateKind_OutCard;
import static kw.tripeak.logic.GameLogic.CHK_QG;
import static kw.tripeak.logic.GameLogic.CHS_DH;
import static kw.tripeak.logic.GameLogic.MASK_VALUE;
import static kw.tripeak.logic.GameLogic.WIK_G;
import static kw.tripeak.logic.GameLogic.WIK_H;
import static kw.tripeak.logic.GameLogic.WIK_NULL;
import static kw.tripeak.logic.GameLogic.WIK_P;
import static kw.tripeak.util.FvMask._MASK_;

import com.kw.gdx.log.NLog;

import java.util.Arrays;

import kw.tripeak.cmd.GameCmd;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.IPlayer;
import kw.tripeak.screen.IGameEngineEventListener;
import kw.tripeak.util.FvMask;

public class GameEngine {
    private static GameEngine pGameEngine = null;
    public enum EstimateKind {
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
        m_cbCurrentUser = INVALID_CHAIR;
        m_cbProvideUser = INVALID_CHAIR;
        m_cbResumeUser = INVALID_CHAIR;
        m_cbLeftCardCount = 0;
        m_cbProvideCard = 0;
        m_cbSendCardCount = 0;
        m_cbSendCardData = 0;
        m_cbMa = 0;
    }    //初始化数据

    /**
     * 得到庄家    得到牌面
     *
     * 通知玩家
     * @return
     */
    public boolean onGameStart() {
        //游戏开始先打断牌面
        //洗牌
        m_cbRepertoryCard = m_GameLogic.shuffle(m_cbRepertoryCard, m_cbRepertoryCard.length);
        //投色子
        iDiceCount = (int) (Math.random() % 6 + 1 + Math.random() % 6 + 1);    //骰子点数
        //庄家
        if (m_cbBankerUser == INVALID_CHAIR) {
            m_cbBankerUser = (iDiceCount % GAME_PLAYER);        //确定庄家
        }

        //排名打乱之后还没有使用
        m_cbLeftCardCount = m_cbRepertoryCard.length;  //剩余牌
        //4个  然后将牌给用户放入到数组中
        for (int i = 0; i < m_CurrChair; i++) {
            m_cbLeftCardCount -= (MAX_COUNT - 1);            //发牌13张
            m_GameLogic.switchToCardIndex(m_cbRepertoryCard,
                    MAX_COUNT - 1, m_cbCardIndex[i]); //初始化用户扑克到 m_cbCardIndex 数组
        }
        //设置变量
        m_cbProvideCard = 0;            //初始化供应扑克
        m_cbProvideUser = INVALID_CHAIR;    //初始化供应玩家
        m_cbCurrentUser = m_cbBankerUser;    //设置当前操作玩家为庄家  通过随机得到庄家

        //构造数据
        GameCmd.CMD_S_GameStart gameStart = new GameCmd().new CMD_S_GameStart();
        gameStart.iDiceCount = iDiceCount;
        gameStart.cbBankerUser = m_cbBankerUser;
        gameStart.cbCurrentUser = m_cbCurrentUser;
        gameStart.cbLeftCardCount = m_cbLeftCardCount - m_cbMa;

        for (int i = 0; i < m_CurrChair; i++) {      //通知全部玩家开始游戏
            m_GameLogic.switchToCardData(m_cbCardIndex[i], gameStart.cbCardData, MAX_COUNT);
            if (m_pIPlayer[i].isAndroid()) {   //机器人作弊用，用于分析其他玩的牌
                int bIndex = 1;
                for (int j = 0; j < GAME_PLAYER; j++) {
                    if (j == i) continue;
                    m_GameLogic.switchToCardData(m_cbCardIndex[j],
                            gameStart.cbCardData, MAX_COUNT);
                }
            }
            IGameEngineEventListener pListener = m_pIPlayer[i].getGameEngineEventListener();
            if (pListener != null) {
                pListener.onGameStartEvent(gameStart);
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
                EstimateKind_OutCard);     //响应判断
        if (!bAroseAction) {
            m_cbCurrentUser = ((m_cbCurrentUser + m_CurrChair - 1) % m_CurrChair);          //切换当前玩家发牌
            dispatchCardData(m_cbCurrentUser);    //派发扑克
        }
        return true;
    }   //出牌命令
//    public boolean onEventGameConclude(int cbChairID){
//        return false;
//    } //结束游戏
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
            System.out.println(" man l ");
            onGameStart();
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
            GameLogic.tagGangCardResult GangCardResult = new GameLogic.tagGangCardResult();
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
        GameCmd.CMD_S_SendCard SendCard = new GameCmd.CMD_S_SendCard();
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
    public boolean estimateUserRespond(int cbCurrentUser, int cbCurrentCard, EstimateKind estimateKind) {
        boolean bAroseAction = false;    //变量定义，存在Action标识
        for (int i = 0; i < m_bResponse.length; i++) {
            m_bResponse[i] = false;
        }
        for (int i = 0; i < m_cbUserAction.length; i++) {
            m_cbUserAction[i] = 0;
        }
        for (int i = 0; i < m_cbPerformAction.length; i++) {
            m_cbPerformAction[i] = 0;
        }
        for (int i = 0; i < m_llHuRight.length; i++) {
            m_llHuRight[i] = 0;
        }
        for (int i = 0; i < m_cbHuKind.length; i++) {
            m_cbHuKind[i] = 0;
        }
        for (int i = 0; i < m_cbHuSpecial.length; i++) {
            m_cbHuSpecial[i] = 0;
        }
//        memset(m_bResponse, 0, sizeof(m_bResponse));                //清空用户响应
//        memset(m_cbUserAction, 0, sizeof(m_cbUserAction));          //用户动作
//        memset(m_cbPerformAction, 0, sizeof(m_cbPerformAction));    //完成的动作
//        memset(m_llHuRight, 0, sizeof(m_llHuRight));                //清空胡牌类型
//        memset(m_cbHuKind, 0, sizeof(m_cbHuKind));                  //清空胡牌方式
//        memset(m_cbHuSpecial, 0, sizeof(m_cbHuSpecial));            //清空胡牌特殊情况

        for (int i = 0; i < m_CurrChair; i++) {                 //动作判断
            if (cbCurrentUser == i) continue;                       //过滤当前出牌的玩家，即自己不能胡自己的牌
            if (estimateKind == EstimateKind_OutCard)               //出牌方式为普通出牌
            {
                if (!m_cbPassPeng[i][m_GameLogic.switchToCardIndex(cbCurrentCard)])            //检测可以碰牌、没碰上家的牌不能碰对家和下家的
                {
                    m_cbUserAction[i] |= m_GameLogic.estimatePengCard(m_cbCardIndex[i], cbCurrentCard);    //碰牌判断
                    if (m_cbUserAction[i]!=0 && WIK_P!= 0) {
                        m_cbPassPeng[i][m_GameLogic.switchToCardIndex(cbCurrentCard)] = true;
                    }
                }
                if (m_cbLeftCardCount > 0)                                                                  //有剩余的牌才能杠
                {
                    m_cbUserAction[i] |= m_GameLogic.estimateGangCard(m_cbCardIndex[i], cbCurrentCard);    //杠牌判断，明杠
                    if ((m_cbUserAction[i] & WIK_G) != 0x00) {                                              //有杠
                        m_cbGangCount = 1;
                        m_cbGangCard[0] = cbCurrentCard;
                    }
                }
            }
            int cbWeaveCount = m_cbWeaveItemCount[i];                                                    //获取组合牌的总数，既碰或者杠
            //此处需要处理番数一样不能 ，开始没胡，没过手也不能胡
            m_cbUserAction[i] |= m_GameLogic.analyseHuCard(m_cbCardIndex[i], m_WeaveItemArray[i], cbWeaveCount, cbCurrentCard, m_cbHuKind[i], m_llHuRight[i], m_cbHuSpecial[i], m_cbSendCardCount, m_cbOutCardCount, m_bGangStatus, false, m_bQiangGangStatus, m_cbFanShu[i], false);
            if (m_cbUserAction[i] != WIK_NULL) {                                                            //是否可以胡牌判定
                bAroseAction = true;
            }
        }
        if (bAroseAction)                           //如果存在碰、杠、胡的可能
        {
            m_cbProvideUser = cbCurrentUser;        //设置供应用户为当前出牌的用户
            m_cbProvideCard = cbCurrentCard;        //设置供应牌为当前出的牌
            m_cbResumeUser = cbCurrentUser;         //暂存当前用户
            m_cbCurrentUser = INVALID_CHAIR;        //设置当前用户为无效状态
            sendOperateNotify();                    //发送操作通知
            return true;
        }
        return false;
    }//检测响应
    public boolean sendOperateNotify(){
        int cbCount = 0;
        for (int i = 0; i < m_CurrChair; i++)                               //发送提示
        {
            if (m_cbUserAction[i] != WIK_NULL)                                  //如果存在动作
            {
                cbCount++;
                GameCmd.CMD_S_OperateNotify OperateNotify = new GameCmd.CMD_S_OperateNotify();                              //定义通知变量
//                memset(&OperateNotify, 0, sizeof(CMD_S_OperateNotify));         //通知
                OperateNotify.cbResumeUser = m_cbResumeUser;                    //设置谁出的牌
                OperateNotify.cbActionCard = m_cbProvideCard;                   //设置供应的牌
                OperateNotify.cbActionMask = m_cbUserAction[i];                 //设置动作类型
                OperateNotify.cbGangCount = m_cbGangCount;                      //杠的数量
//                memcpy(OperateNotify.cbGangCard, m_cbGangCard, m_cbGangCount);  //用于处理手上存在多个杠的情况
                m_pIPlayer[i].getGameEngineEventListener().onOperateNotifyEvent(OperateNotify);
            }
        }
        return true;
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

        //效验用户  注意：机器人有可能发生此断言
        int cbChairID = OperateCard.cbOperateUser;
        int cbOperateCode = OperateCard.cbOperateCode;
        int cbOperateCard = OperateCard.cbOperateCard;
        if ((cbChairID != m_cbCurrentUser) && (m_cbCurrentUser != INVALID_CHAIR)) {return true;}
        for (int i = 0; i < m_llHuRight.length; i++) {
            m_llHuRight[i] = 0;
        }

        for (int i = 0; i < m_cbHuKind.length; i++) {
            m_cbHuKind[i] = 0;
        }

        for (int i = 0; i < m_cbHuSpecial.length; i++) {
            m_cbHuSpecial[i] = 0;
        }

        //前面判断自己摸的牌，后面判断玩家打的牌
//        memset(m_llHuRight, 0, sizeof(m_llHuRight));            //清空胡牌类型
//        memset(m_cbHuKind, 0, sizeof(m_cbHuKind));              //清空胡牌方式
//        memset(m_cbHuSpecial, 0, sizeof(m_cbHuSpecial));        //清空胡牌方式

        if (m_cbCurrentUser == INVALID_CHAIR)                        //别的打的牌，需要处理一炮多响
        {
            if (m_bResponse[cbChairID]) {return true;}                                                               //判断是否重复处理，防止多次提交
            if ((cbOperateCode != WIK_NULL) && ((m_cbUserAction[cbChairID] & cbOperateCode) == 0x00)) {return true;} //操作状态不对
            int cbTargetUser = cbChairID;                                                                        //暂存目标玩家
            int cbTargetAction = cbOperateCode;                                                                  //暂存目标动作
            if (cbTargetAction != WIK_NULL) {                                                                        //不是"过"才加入权重
                FvMask.Add(m_cbTargetUser, _MASK_(cbChairID));                                                      //目标目标权重
            }
            int cbTargetActionRank = m_GameLogic.getUserActionRank(cbTargetAction);                             //获取当前操作的优先级
            m_bResponse[cbChairID] = true;                                                                           //已经处理，防止重复处理
            m_cbPerformAction[cbChairID] = cbOperateCode;                                                            //记录执行的操作，因为m_cbUserAction可能包含多个动作
            m_cbOperateCard[cbChairID] = m_cbProvideCard;                                                            //记录操作的牌

            //需要考虑一炮多响，基本思路通过cbTargetUser调整成数组方式
            for (int i = 0; i < m_CurrChair; i++)                                                                //遍历椅子
            {
                if (i == cbChairID) continue;                                                                        //过滤自己
                int cbUserAction = !m_bResponse[i] ? m_cbUserAction[i] : m_cbPerformAction[i];                   //获取每个玩家的动作，如果客户端已经处理，则使用具体动作
                if (cbUserAction == WIK_NULL)continue;                                                               //过滤无动作
                int cbUserActionRank = m_GameLogic.getUserActionRank(cbUserAction);                             //获取自身动作优先级
                if (cbUserActionRank > cbTargetActionRank)                                                           //如果存在优先级别高的则调整目标用户
                {
                    FvMask.Del(m_cbTargetUser, _MASK_(cbTargetUser));                                               //移除权重低的
                    cbTargetUser = i;                                                                                //优先级高的椅子
                    FvMask.Add(m_cbTargetUser, _MASK_(cbTargetUser));                                               //添加权重高的
                    cbTargetAction = cbUserAction;                                                                   //优先级高的动作
                }
                //动作优先级相同处理
                if (cbUserActionRank == cbTargetActionRank) {                                                        //只有只胡牌才会出现权重相同（一炮多响）
                    if (cbTargetAction != WIK_NULL) {
                        FvMask.Add(m_cbTargetUser, _MASK_(i));                                                      //目标目标权重
                    }
                }
            }
            for (int i = 0; i < m_CurrChair; i++)                                                                //遍历椅子
            {
                if (FvMask.HasAny(m_cbTargetUser, _MASK_(i))) {                                                     //检测能胡还没操作的
                if (!m_bResponse[i]) {return true;}                                                              //若优先级高的尚为操作，则等待，此处确保玩家一定进行了操作。
            }
            }

            if (cbTargetAction == WIK_NULL)                                                                          //放弃操作
            {
                if (m_bQiangGangStatus) {
                    m_bQiangGangStatus = false;
                }
                for (int i = 0; i < m_bResponse.length; i++) {
                    m_bResponse[i] = false;
                }
                for (int i = 0; i < m_cbUserAction.length; i++) {
                    m_cbUserAction[i] = 0;
                }
                Arrays.fill(m_cbOperateCard,0);
                Arrays.fill(m_cbPerformAction,0);
                //重置用户操作
//                memset(m_cbOperateCard, 0, sizeof(m_cbOperateCard));                                                 //重置操作的牌
//                memset(m_cbPerformAction, 0, sizeof(m_cbPerformAction));                                             //重置动作
                dispatchCardData(m_cbResumeUser);                                                                    //发送扑克
                return true;
            }
            //变量定义
            int cbTargetCard = m_cbOperateCard[cbTargetUser];                                                    //获取到目标牌
            m_cbOutCardData = 0;                                                                                     //将出的牌设置为0防止碰或杠之后桌面还显示
            //胡牌操作
            if (cbTargetAction == WIK_H)                                                                             //胡牌
            {
                m_cbHuCard = cbTargetCard;                                                                           //设置胡牌的那张牌
                for (int i = 0; i < m_CurrChair; i++) {
                    if (FvMask.HasAny(m_cbTargetUser, _MASK_(i))) {
                        int cbWeaveItemCount = m_cbWeaveItemCount[i];                                            //获取碰、杠组合总数
                        GameLogic.TagWeaveItem []pWeaveItem = m_WeaveItemArray[i];                                              //获取碰、杠组合
                        m_GameLogic.analyseHuCard(m_cbCardIndex[i], pWeaveItem, cbWeaveItemCount, m_cbHuCard, m_cbHuKind[i], m_llHuRight[i], m_cbHuSpecial[i], m_cbSendCardCount, m_cbOutCardCount, m_bGangStatus, false, m_bQiangGangStatus, m_cbFanShu[i], true);
                        if (m_llHuRight[i] != 0) {                                                                   //胡牌判定
                            m_cbCardIndex[i][m_GameLogic.switchToCardIndex(m_cbHuCard)]++;
                        }
                    }
                }
                if (m_bQiangGangStatus)                                                                              //如果是抢杠，设置对应杠的不算钱
                {
                    m_bQiangGangStatus = false;
                    for (int i = 0; i < m_cbWeaveItemCount[m_cbProvideUser]; i++)                                //遍历组合总数，设置牌
                    {
                        int cbWeaveKind = m_WeaveItemArray[m_cbProvideUser][i].cbWeaveKind;                      //获取组合类型
                        int cbCenterCard = m_WeaveItemArray[m_cbProvideUser][i].cbCenterCard;                    //获取组合牌
                        if ((cbCenterCard == m_cbProvideCard) && (cbWeaveKind == WIK_G))                             //如果组合为碰
                        {
                            m_WeaveItemArray[m_cbProvideUser][i].cbValid = false;                                        //被抢的杠设置为无效状态
                        }
                    }
                }
                onEventGameConclude(INVALID_CHAIR);                                                                  //结束游戏
                return true;
            }
            Arrays.fill(m_bResponse,false);
            Arrays.fill(m_cbUserAction,0);
            Arrays.fill(m_cbOperateCard,0);
            Arrays.fill(m_cbPerformAction,0);

            //用户状态
//            memset(m_bResponse, 0, sizeof(m_bResponse));
//            memset(m_cbUserAction, 0, sizeof(m_cbUserAction));
//            memset(m_cbOperateCard, 0, sizeof(m_cbOperateCard));
//            memset(m_cbPerformAction, 0, sizeof(m_cbPerformAction));

            int cbIndex = m_cbWeaveItemCount[cbTargetUser]++;                                                    //组合次数+1
            m_WeaveItemArray[cbTargetUser][cbIndex].cbPublicCard = true;                                             //明牌
            m_WeaveItemArray[cbTargetUser][cbIndex].cbCenterCard = cbTargetCard;                                     //牌
            m_WeaveItemArray[cbTargetUser][cbIndex].cbWeaveKind = cbTargetAction;                                    //目标动作
            m_WeaveItemArray[cbTargetUser][cbIndex].cbProvideUser = m_cbProvideUser;                                 //供应者
            m_WeaveItemArray[cbTargetUser][cbIndex].cbValid = true;                                                     //有效状态
            switch (cbTargetAction)                                                                                  //动作类型
            {
                case 1:                                                                                          //碰牌操作
                {
                    int cbRemoveCard[] = {cbTargetCard, cbTargetCard};                                           //设置两张牌
                    m_GameLogic.removeCard(m_cbCardIndex[cbTargetUser], cbRemoveCard,cbRemoveCard.length);        //手上删除这两张牌
                    break;
                }
                case 2:                                                                                          //杠牌操作
                {
                    int cbRemoveCard[] = {cbTargetCard, cbTargetCard, cbTargetCard};                             //设置那三张牌
                    m_GameLogic.removeCard(m_cbCardIndex[cbTargetUser], cbRemoveCard, cbRemoveCard.length);        //移除那三张牌
                    m_bGangStatus = true;                                                                            //是否可以杠开
                    break;
                }
                default:
                    break;
            }
            m_cbCurrentUser = cbTargetUser;                                                                           //设置当前玩家为目标玩家
            GameCmd.CMD_S_OperateResult OperateResult = new GameCmd.CMD_S_OperateResult();                                                                        //构造操作结果
            OperateResult.cbOperateUser = cbTargetUser;                                                               //操作玩家
            OperateResult.cbOperateCard = cbTargetCard;                                                               //操作扑克
            OperateResult.cbOperateCode = cbTargetAction;                                                             //操作动作
            OperateResult.cbProvideUser = m_cbProvideUser;                                                            //供应玩家
            m_cbTargetUser = 0;
            for (int j = 0; j < m_CurrChair; j++) {
                m_pIPlayer[j].getGameEngineEventListener().onOperateResultEvent(OperateResult);                     //操作结果
            }
            if (cbTargetAction == WIK_G)                                                                              //杠牌处理
            {
                m_bQiangGangStatus = true;    // 抢杠状态
                boolean bAroseAction = estimateUserRespond(m_cbCurrentUser, m_cbProvideCard, EstimateKind_GangCard);     //枪杠判定
                if (!bAroseAction)                                                                                    //不存在枪杠情况
                {
                    m_bQiangGangStatus = false;                                                                       //不枪杠
                    dispatchCardData(cbChairID, true);                                                                //杠发牌
                }
            }

            if (cbTargetAction == WIK_P) {
                if (m_cbLeftCardCount > 0)                                                                            //用杠碰牌，从新判定杠
                {
                    GameLogic.tagGangCardResult GangCardResult = new GameLogic.tagGangCardResult();
                    m_cbUserAction[m_cbCurrentUser] |= m_GameLogic.analyseGangCard(m_cbCardIndex[m_cbCurrentUser], m_WeaveItemArray[m_cbCurrentUser], m_cbWeaveItemCount[m_cbCurrentUser], GangCardResult);
                    if ((m_cbUserAction[m_cbCurrentUser] & WIK_G) != 0x0) {                                          //判定是否杠牌
                        m_cbGangCount = GangCardResult.cbCardCount;                                                  //杠的数量
//                        memcpy(m_cbGangCard, GangCardResult.cbCardData, sizeof(m_cbGangCard));                       //杠的牌
                        sendOperateNotify();
                    }
                }
            }
            return true;                                                                                                //操作流程结束
        }

        if (m_cbCurrentUser == cbChairID)                                                                               //当前用户为操作用户
        {
            if ((cbOperateCode != WIK_NULL) && ((m_cbUserAction[cbChairID] & cbOperateCode) == 0x00)) {return true;}    //操作状态不对
            if (!m_GameLogic.isValidCard(cbOperateCard)) {return true;};                                        //判断牌是否有效
            m_cbUserAction[m_cbCurrentUser] = WIK_NULL;                                                                  //重置用户全部可能动作
            m_cbPerformAction[m_cbCurrentUser] = WIK_NULL;                                                               //重置用户动作

            switch (cbOperateCode)                                                                                    //操作判定
            {
                case 2:                                                                                                //杠牌操作
                {
                    //变量定义
                    GameLogic.tagGangCardResult GangCardResult = new GameLogic.tagGangCardResult();                                                                   //杠牌判断
                    int action = m_GameLogic.analyseGangCard(m_cbCardIndex[cbChairID], m_WeaveItemArray[cbChairID], m_cbWeaveItemCount[cbChairID], GangCardResult);
                    if (action != WIK_G) {return true;}                                                                 //不是杠则返回
                    m_bGangStatus = true;                                                                               //设置能否杠开状态
                    //计算当前的杠的牌
                    int cbGangIndex = INVALID_BYTE;
                    for (int a = 0; a < GangCardResult.cbCardCount; a++) {
                        if (GangCardResult.cbCardData[a] == cbOperateCard)                                              //杠的牌为选择的那张
                        {
                            cbGangIndex = a;
                            break;
                        }
                    }
                    if (cbGangIndex == INVALID_BYTE)                                                                    //没找到，调整为发的牌
                    {
                        return true;
                    }
                    int cbCardIndex = m_GameLogic.switchToCardIndex(cbOperateCard);                                //将扑克转化成位置
                    if (GangCardResult.cbPublic[cbGangIndex] == 1) {                                                 //明杠
                        m_bQiangGangStatus = true;                                                                      //设置是否为能枪杠状态
                        for (int i = 0; i < m_cbWeaveItemCount[m_cbCurrentUser]; i++)                               //遍历组合总数，设置牌
                        {
                            int cbWeaveKind = m_WeaveItemArray[m_cbCurrentUser][i].cbWeaveKind;                     //获取组合类型
                            int cbCenterCard = m_WeaveItemArray[m_cbCurrentUser][i].cbCenterCard;                   //获取组合牌
                            if ((cbCenterCard == cbOperateCard) && (cbWeaveKind == WIK_P))                              //如果组合为碰
                            {
                                m_WeaveItemArray[m_cbCurrentUser][i].cbPublicCard = true;                               //牌公开
                                m_WeaveItemArray[m_cbCurrentUser][i].cbWeaveKind = cbOperateCode;                       //类型为杠
                                m_WeaveItemArray[m_cbCurrentUser][i].cbCenterCard = cbOperateCard;                      //牌的值
                                m_WeaveItemArray[m_cbCurrentUser][i].cbValid = true;                                       //2为转弯杠
                                if (m_cbSendCardData != cbOperateCard) {                                                //发的牌和杠的牌不一致，则不算分
                                    m_WeaveItemArray[m_cbCurrentUser][i].cbValid = false;                                   //后杠设置为无效
                                }
                                break;
                            }
                        }
                    }
                    if (GangCardResult.cbPublic[cbGangIndex]==0) {        //暗杠
                        int cbWeaveIndex = m_cbWeaveItemCount[m_cbCurrentUser]++;                                   //自身组合+1
                        m_WeaveItemArray[m_cbCurrentUser][cbWeaveIndex].cbPublicCard = false;                           //不公开
                        m_WeaveItemArray[m_cbCurrentUser][cbWeaveIndex].cbProvideUser = m_cbCurrentUser;                //供应人为自己
                        m_WeaveItemArray[m_cbCurrentUser][cbWeaveIndex].cbWeaveKind = cbOperateCode;                    //类型为杠
                        m_WeaveItemArray[m_cbCurrentUser][cbWeaveIndex].cbCenterCard = cbOperateCard;                   //牌的值
                        m_WeaveItemArray[m_cbCurrentUser][cbWeaveIndex].cbValid = true;                                    //有效状态
                    }

                    m_cbCardIndex[m_cbCurrentUser][cbCardIndex] = 0;                                                          //将手上的牌组合移除
                    GameCmd.CMD_S_OperateResult OperateResult = new GameCmd.CMD_S_OperateResult();                                                                        //构造结果操作结果
                    OperateResult.cbOperateUser = m_cbCurrentUser;                                                            //操作人
                    OperateResult.cbProvideUser = m_cbCurrentUser;                                                            //供应人
                    OperateResult.cbOperateCode = cbOperateCode;                                                              //操作类型
                    OperateResult.cbOperateCard = cbOperateCard;                                                              //供应的牌
                    m_cbTargetUser = 0;                                                                                       //执行碰或者杠需要重人员权重
                    for (int j = 0; j < m_CurrChair; j++) {
                        m_pIPlayer[j].getGameEngineEventListener().onOperateResultEvent(OperateResult);                     //操作结果事件
                    }
                    boolean bAroseAction = false;                                                                                //效验动作
                    if (GangCardResult.cbPublic[cbGangIndex] == 1) bAroseAction = estimateUserRespond(cbChairID, cbOperateCard, EstimateKind_GangCard);        //枪杠判定
                    if (!bAroseAction)                                                                                        //不存在枪杠情况
                    {
                        m_bQiangGangStatus = false;                                                                           //不枪杠
                        dispatchCardData(cbChairID, true);                                                                    //杠发牌
                    }
                    return true;
                }
                case 4:                                                                                                    //胡操作
                {
                    m_cbHuCard = cbOperateCard;                                                                                //设置牌
                    int cbWeaveItemCount = m_cbWeaveItemCount[m_cbCurrentUser];                                            //获取组合总数
                    GameLogic.TagWeaveItem[] pWeaveItem = m_WeaveItemArray[m_cbCurrentUser];                                              //获取全部组合
                    int cbTempCardIndex[] = new int[MAX_INDEX];//临时牌变量用来分析
//                    memcpy(cbTempCardIndex, m_cbCardIndex[m_cbCurrentUser], cbTempCardIndex));                          //设置值
                    m_GameLogic.removeCard(cbTempCardIndex, m_cbHuCard);                                                      //移除发的那张牌
                    m_GameLogic.analyseHuCard(cbTempCardIndex, pWeaveItem, cbWeaveItemCount, m_cbHuCard, m_cbHuKind[cbChairID], m_llHuRight[cbChairID], m_cbHuSpecial[cbChairID], m_cbSendCardCount, m_cbOutCardCount, m_bGangStatus, true, m_bQiangGangStatus, m_cbFanShu[cbChairID], true);
                    FvMask.Add(m_cbTargetUser, _MASK_(m_cbCurrentUser));                                                      //添加权重
                    onEventGameConclude(INVALID_CHAIR);                                                                        //结束游戏
                    return true;
                }
                default:
                    break;
            }
            return true;
        }

        return true;
    }
    /**
     * 游戏结束
     * @param cbChairID
     */
    boolean onEventGameConclude(int cbChairID) {
        NLog.e("----游戏结束----");
        int m_cbLastBankerUser = m_cbBankerUser;    //保存上局庄家

        GameCmd.CMD_S_GameEnd GameEnd = new GameCmd.CMD_S_GameEnd();
//        memset(&GameEnd, 0, sizeof(CMD_S_GameEnd));    //清空内存
        //=================================计算马==============================================
        int cbOkBird = 0;
        int cb0 = 0;                //买到0位置
        int cb1 = 0;                //买到1位置
        int cb2 = 0;                //买到2位置
        int cb3 = 0;                //买到3位置
        for (int i = 0; i < m_cbMa; i++) {
            int cbLast = --m_cbLeftCardCount;
            if (cbLast >= 0) {
                int cbBird = m_cbRepertoryCard[cbLast];
                int cbValue = ((cbBird & MASK_VALUE) % GAME_PLAYER);
                cbValue = ((cbValue - m_cbLastBankerUser + GAME_PLAYER) % GAME_PLAYER);
                if (cbValue == 1) {
                    cb0++;
                }
                if (cbValue == 2) {
                    cb1++;
                }
                if (cbValue == 3) {
                    cb2++;
                }
                if (cbValue == 0) {
                    cb3++;
                }
                GameEnd.cbMaCard[i] = cbBird;
            }
        }
        //椅子顺序为顺时针，拿牌顺序为逆时针
        int cbMaList[] = {0, 0, 0, 0};
        cbMaList[0] = cb0;
        cbMaList[1] = cb3;
        cbMaList[2] = cb2;
        cbMaList[3] = cb1;
        //结束信息
        GameEnd.cbProvideUser = m_cbProvideUser;
        GameEnd.cbHuUser = m_cbTargetUser;          //胡牌玩家，1左移 chairID位
        GameEnd.cbHuCard = m_cbHuCard;
        for (int i = 0; i < GAME_PLAYER; i++) { //结束信息
            GameEnd.cbCardCount[i] = m_GameLogic.switchToCardData(m_cbCardIndex[i], GameEnd.cbCardData[i], MAX_COUNT);
            GameEnd.dwHuRight[i] = m_llHuRight[i];
            GameEnd.cbHuKind[i] = m_cbHuKind[i];
            GameEnd.cbHuSpecial[i] = m_cbHuSpecial[i];
            GameEnd.cbWeaveCount[i] = m_cbWeaveItemCount[i];
            GameLogic.TagWeaveItem[] tagWeaveItems = m_WeaveItemArray[i];
//            memcpy(GameEnd.WeaveItemArray[i], m_WeaveItemArray[i], sizeof(m_WeaveItemArray[i]));
        }
        for (int i = 0; i < GAME_PLAYER; i++) {     //计算杠分
            for (int j = 0; j < m_cbWeaveItemCount[i]; j++) {
                if (m_WeaveItemArray[i][j].cbWeaveKind == WIK_G) {                //没被抢的杠、和后杠
                    if (m_WeaveItemArray[i][j].cbPublicCard == true && m_WeaveItemArray[i][j].cbProvideUser != i) {         //放的杠2番
                        int k = m_WeaveItemArray[i][j].cbProvideUser;
                        int cbGangScore = 1;
                        GameEnd.lGameScore[k] -= cbGangScore;
                        GameEnd.lGameScore[i] += cbGangScore;
                        //===================计算马分数开始=============================
                        //输分马
                        GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[k] * cbGangScore;
                        GameEnd.lGameScore[i] += cbMaList[k] * cbGangScore;
                        //赢分马
                        GameEnd.lGameScore[k] -= cbMaList[i] * cbGangScore;
                        GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * cbGangScore;
                        //===================计算马分数结束=============================

                        //记录常规积分
                        GameEnd.lNormalGameScore[k] -= cbGangScore;
                        GameEnd.lNormalGameScore[i] += cbGangScore;
                        //记录马积分
                        GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[k] * cbGangScore;
                        GameEnd.lMaGameScore[i] += cbMaList[k] * cbGangScore;
                        GameEnd.lMaGameScore[k] -= cbMaList[i] * cbGangScore;
                        GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * cbGangScore;
                    } else if (m_WeaveItemArray[i][j].cbPublicCard == false && m_WeaveItemArray[i][j].cbProvideUser == i) { //暗杠
                        for (int k = 0; k < GAME_PLAYER; k++) {
                            if (i != k) {
                                int cbGangScore = 2;
                                GameEnd.lGameScore[k] -= cbGangScore;
                                GameEnd.lGameScore[i] += cbGangScore;
                                //===================计算马分数开始=============================
                                //输分马
                                GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[k] * cbGangScore;
                                GameEnd.lGameScore[i] += cbMaList[k] * cbGangScore;
                                //赢分马
                                GameEnd.lGameScore[k] -= cbMaList[i] * cbGangScore;
                                GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * cbGangScore;
                                //===================计算马分数结束=============================
                                //记录常规积分
                                GameEnd.lNormalGameScore[k] -= cbGangScore;
                                GameEnd.lNormalGameScore[i] += cbGangScore;
                                //记录马积分
                                GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[k] * cbGangScore;
                                GameEnd.lMaGameScore[i] += cbMaList[k] * cbGangScore;
                                GameEnd.lMaGameScore[k] -= cbMaList[i] * cbGangScore;
                                GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * cbGangScore;

                            }
                        }
                    }
                }
            }
        }

        //统计积分
        if (m_cbTargetUser != 0x0 && m_cbProvideUser != INVALID_CHAIR)    //胡牌人员不为0、供应人员不为INVALID_CHAIR
        {
            //自摸类型
            if ((m_llHuRight[m_cbProvideUser] != 0x00) && (FvMask.HasAny(m_cbTargetUser, _MASK_(m_cbProvideUser)))) {
                //翻数计算
                int cbChiHuOrder = m_GameLogic.getHuFanShu(m_llHuRight[m_cbProvideUser], m_cbHuKind[m_cbProvideUser], m_cbHuSpecial[m_cbProvideUser]);

                //循环累计
                for (int i = 0; i < m_CurrChair; i++) {
                    if (i != m_cbProvideUser) {
                        GameEnd.lGameScore[i] -= cbChiHuOrder;
                        GameEnd.lGameScore[m_cbProvideUser] += cbChiHuOrder;
                        //===================自摸计算马分数开始=============================
                        //输分的马
                        GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[i] * cbChiHuOrder;
                        GameEnd.lGameScore[m_cbProvideUser] += cbMaList[i] * cbChiHuOrder;
                        //赢分的马
                        GameEnd.lGameScore[i] -= cbMaList[m_cbProvideUser] * cbChiHuOrder;
                        GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[m_cbProvideUser] * cbChiHuOrder;
                        //===================自摸计算马分数结束=============================
                        //记录常规积分
                        GameEnd.lNormalGameScore[i] -= cbChiHuOrder;
                        GameEnd.lNormalGameScore[m_cbProvideUser] += cbChiHuOrder;
                        //记录马积分
                        GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[i] * cbChiHuOrder;
                        GameEnd.lMaGameScore[m_cbProvideUser] += cbMaList[i] * cbChiHuOrder;
                        GameEnd.lMaGameScore[i] -= cbMaList[m_cbProvideUser] * cbChiHuOrder;
                        GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[m_cbProvideUser] * cbChiHuOrder;
                    }
                }
                //庄家设置
                m_cbBankerUser = m_cbProvideUser;
            } else {
                //捉炮类型，添加一炮多响计分
                int cbDistance = 0;
                for (int i = 0; i < m_CurrChair; i++) {
                    if (i == m_cbProvideUser) {
                        continue;
                    } //跳过放炮人本身
                    if ((m_llHuRight[i] != 0x0) && (i != m_cbProvideUser) && FvMask.HasAny
                    (m_cbTargetUser, _MASK_(i))){
                        //翻数计算
                        int cbChiHuOrder = m_GameLogic.getHuFanShu(m_llHuRight[i], m_cbHuKind[i], m_cbHuSpecial[i]);
                        if (((m_cbHuSpecial[i] & CHS_DH) != 0)) {    //如果是地胡
                            for (int j = 0; j < m_CurrChair; j++) {
                                if (i != j) {
                                    GameEnd.lGameScore[j] -= cbChiHuOrder;
                                    GameEnd.lGameScore[i] += cbChiHuOrder;
                                    //===================计算马分数开始=============================
                                    //输分马
                                    GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[j] * cbChiHuOrder;
                                    GameEnd.lGameScore[i] += cbMaList[j] * cbChiHuOrder;
                                    //赢分马
                                    GameEnd.lGameScore[j] -= cbMaList[i] * cbChiHuOrder;
                                    GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * cbChiHuOrder;
                                    //===================计算马分数结束=============================

                                    //记录常规积分
                                    GameEnd.lNormalGameScore[j] -= cbChiHuOrder;
                                    GameEnd.lNormalGameScore[i] += cbChiHuOrder;
                                    //记录马积分
                                    GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[j] * cbChiHuOrder;
                                    GameEnd.lMaGameScore[i] += cbMaList[j] * cbChiHuOrder;
                                    GameEnd.lMaGameScore[j] -= cbMaList[i] * cbChiHuOrder;
                                    GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * cbChiHuOrder;
                                }
                            }
                        } else {
                            GameEnd.lGameScore[m_cbProvideUser] -= cbChiHuOrder;
                            GameEnd.lGameScore[i] += cbChiHuOrder;
                            //===================计算马分数开始=============================
                            //输分马
                            GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * cbChiHuOrder;
                            GameEnd.lGameScore[i] += cbMaList[m_cbProvideUser] * cbChiHuOrder;
                            //赢分马
                            GameEnd.lGameScore[m_cbProvideUser] -= cbMaList[i] * cbChiHuOrder;
                            GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * cbChiHuOrder;
                            //===================计算马分数结束=============================

                            //记录常规积分
                            GameEnd.lNormalGameScore[m_cbProvideUser] -= cbChiHuOrder;
                            GameEnd.lNormalGameScore[i] += cbChiHuOrder;
                            //记录马积分
                            GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * cbChiHuOrder;
                            GameEnd.lMaGameScore[i] += cbMaList[m_cbProvideUser] * cbChiHuOrder;
                            GameEnd.lMaGameScore[m_cbProvideUser] -= cbMaList[i] * cbChiHuOrder;
                            GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * cbChiHuOrder;

                            //抢杠全包
                            if ((m_llHuRight[i] != 0x0) && (m_cbHuKind[i] & CHK_QG) != 0) {
                                for (int j = 0; j < m_CurrChair; j++) {
                                    if (j != i && j != m_cbProvideUser) {
                                        GameEnd.lGameScore[m_cbProvideUser] -= ((cbMaList[j] + 1) * cbChiHuOrder);
                                        GameEnd.lGameScore[i] += ((cbMaList[j] + 1) * cbChiHuOrder);
                                        //===================计算马分数开始=============================
                                        //输分的马
                                        GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        GameEnd.lGameScore[i] += cbMaList[m_cbProvideUser] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        //赢分的马
                                        GameEnd.lGameScore[m_cbProvideUser] -= cbMaList[i] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        //===================计算马分数结束=============================
                                        //记录常规积分
                                        GameEnd.lNormalGameScore[m_cbProvideUser] -= (cbMaList[j] + 1) * cbChiHuOrder;
                                        GameEnd.lNormalGameScore[i] += (cbMaList[j] + 1) * cbChiHuOrder;
                                        //记录马积分
                                        //输
                                        GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        GameEnd.lMaGameScore[i] += cbMaList[m_cbProvideUser] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        //输
                                        GameEnd.lMaGameScore[m_cbProvideUser] -= cbMaList[i] * ((cbMaList[j] + 1) * cbChiHuOrder);
                                        GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * ((cbMaList[j] + 1) * cbChiHuOrder);

                                    }
                                    if (j == m_cbProvideUser)    //马买到放炮的
                                    {
                                        GameEnd.lGameScore[m_cbProvideUser] -= cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lGameScore[i] += cbMaList[j] * cbChiHuOrder;
                                        //===================计算马分数开始=============================
                                        //输分的马
                                        GameEnd.lGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lGameScore[i] += cbMaList[m_cbProvideUser] * cbMaList[j] * cbChiHuOrder;
                                        //赢分的马
                                        GameEnd.lGameScore[m_cbProvideUser] -= cbMaList[i] * cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lGameScore[m_cbLastBankerUser] += cbMaList[i] * cbMaList[j] * cbChiHuOrder;
                                        //===================计算马分数结束=============================

                                        //记录常规积分
                                        GameEnd.lNormalGameScore[m_cbProvideUser] -= cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lNormalGameScore[i] += cbMaList[j] * cbChiHuOrder;
                                        //记录马积分
                                        //输
                                        GameEnd.lMaGameScore[m_cbLastBankerUser] -= cbMaList[m_cbProvideUser] * cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lMaGameScore[i] += cbMaList[m_cbProvideUser] * cbMaList[j] * cbChiHuOrder;
                                        //赢
                                        GameEnd.lMaGameScore[m_cbProvideUser] -= cbMaList[i] * cbMaList[j] * cbChiHuOrder;
                                        GameEnd.lMaGameScore[m_cbLastBankerUser] += cbMaList[i] * cbMaList[j] * cbChiHuOrder;

                                    }
                                }
                            }
                        }
                        int cbDistanceTemp = ((m_cbProvideUser < i) ? (i - m_cbProvideUser) : (i + 4 - m_cbProvideUser));
                        if (cbDistance < cbDistanceTemp) {  //一炮多响定庄
                            cbDistance = cbDistanceTemp;
                            m_cbBankerUser = i;    //庄家设置为离自己最近的胡牌玩家
                        }
                    }
                }
            }
        }


//        } else {//流局
//            GameEnd.cbHuUser = 0x00;        //没人胡牌
//        }
        //==================================计算桌子总分===========================================
        for (int i = 0; i < GAME_PLAYER; i++) {
            m_lGameScoreTable[i] += GameEnd.lGameScore[i];
        }
        for (int i = 0; i < GAME_PLAYER; i++) {    //赋值总分
            GameEnd.lGameScoreTable[i] = m_lGameScoreTable[i];
        }
        for (int i = 0; i < m_CurrChair; i++) {
            m_pIPlayer[i].getGameEngineEventListener().onGameEndEvent(GameEnd);   //游戏结束事件
        }
        return true;
    }


}
