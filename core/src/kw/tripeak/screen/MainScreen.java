package kw.tripeak.screen;

import static kw.tripeak.cmd.GameCmd.MAX_COUNT;
import static kw.tripeak.constant.Constant.GAME_PLAYER;
import static kw.tripeak.constant.Constant.INVALID_CHAIR;
import static kw.tripeak.constant.Constant.MAX_DISCARD;
import static kw.tripeak.constant.Constant.MAX_INDEX;
import static kw.tripeak.constant.Constant.MAX_WEAVE;
import static kw.tripeak.logic.GameLogic.MASK_COLOR;
import static kw.tripeak.logic.GameLogic.MASK_VALUE;
import static kw.tripeak.logic.GameLogic.WIK_G;
import static kw.tripeak.logic.GameLogic.WIK_H;
import static kw.tripeak.logic.GameLogic.WIK_NULL;
import static kw.tripeak.logic.GameLogic.WIK_P;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kw.gdx.BaseGame;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.audio.Asset;
import com.kw.gdx.log.NLog;
import com.kw.gdx.screen.BaseScreen;

import kw.tripeak.Ai.AIEngine;
import kw.tripeak.cmd.GameCmd;
import kw.tripeak.engine.GameEngine;
import kw.tripeak.group.ImageView;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.AIPlayer;
import kw.tripeak.play.IPlayer;
import kw.tripeak.play.RealPlayer;
import kw.tripeak.util.FvMask;
import kw.tripeak.util.UIHelper;

@ScreenResource("cocos/GameLayer.json")
public class MainScreen extends BaseScreen implements IGameEngineEventListener {
    //游戏引擎
    private GameEngine m_GameEngine;
    //游戏逻辑
    private GameLogic m_GameLogic;
    //头像信息节点
    private Group m_FaceFrame[] = new Group[4];
    //玩家牌的区域
    Group m_PlayerPanel[] = new Group[4];
    //全部玩家
    IPlayer m_Players[] = new IPlayer[4];
    //操作组
    Group m_pOperateNotifyGroup;
    //正在出牌的节点
    Actor m_pOutCard;
    //剩余牌数量
    Label m_pTextCardNum;
    //当前玩家数量
    int m_CurPlayer;
    //自己的位置
    int m_MeChairID;
    //操作时间
    int m_iOutCardTimeOut;
    //游戏变量
    //组合
    private GameLogic.TagWeaveItem[][] m_WeaveItemArray;
    //玩家牌
    int m_cbCardIndex[][] = new int[GAME_PLAYER][MAX_INDEX];
    //组合数目
    int m_cbWeaveItemCount[] = new int[GAME_PLAYER];
    //丢弃数目
    int m_cbDiscardCount[] = new int[GAME_PLAYER];
    //丢弃记录
    int m_cbDiscardCard[][] = new int[GAME_PLAYER][MAX_DISCARD];
    //剩余
    int m_cbLeftCardCount;
    //庄
    int m_cbBankerChair;
    //是否可操作
    boolean m_bOperate;
    //是否移动牌
    boolean m_bMove;
    //记录位置
    Vector2 m_startVec = new Vector2(0, 0);
    //偏移
    float m_outY = 30;
    float m_cardPosY = 0.0f;
    Group m_Player;

    public MainScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        initInstance();
        initGame();
        initLayer();
        //通过延迟的方式来模拟用户的加入
        for (int i = 0; i < 3; i++) {
            aiEnterGame();
        }

        showAndUpdateHandCard();
        showAndUpdateDiscardCard();
    }

    private int cardPosition;

    private void showAndUpdateHandCard() {
//        m_GameEngine.onGameStart();
        int[] m_cbRepertoryCard = m_GameEngine.m_cbRepertoryCard;
        int index = 0;
        for (int i = 0; i < m_cbCardIndex.length; i++) {
            int viewChairID = switchViewChairID(i);
//            viewChairID = i;
//            System.out.println(viewChairID);
            int cbWeaveItemCount = m_cbWeaveItemCount[i];
            int bCardData[] = new int[MAX_COUNT];  //手上的牌
//            m_GameLogic.switchToCardData(m_cbCardIndex[i], bCardData, MAX_COUNT);
            for (int i1 = 0; i1 < MAX_COUNT; i1++) {
                bCardData[i1] = m_cbRepertoryCard[cardPosition++];
            }
            int x = 0;
//            switch(viewChairID){
            if (viewChairID == 0) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_0");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_0");

                handCard_0.clearChildren();
                comb_0.clearChildren();
                for (int i1 = 0; i1 < m_cbWeaveItemCount.length; i1++) {
                    GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
//                        weaveItem.
                }
                for (int i1 = 0; i1 < MAX_COUNT - 1 - (3 * cbWeaveItemCount); i1++) {
                    Image pCard = createHandCardImageView(viewChairID, bCardData[i1]);
                    pCard.setOrigin(0, 0);
                    pCard.setPosition(x, 0);
                    pCard.setTouchable(Touchable.enabled);
//                        pCard->se(bCardData[i1]);
                    pCard.setName("bt_card_" + (int) bCardData[i1]);
                    pCard.addListener(clickListener);
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                    handCard_0.addActor(pCard);
                    x += 76;

                }
            }
            if (viewChairID == 1) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_1");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_1");
                if (handCard_0 == null) continue;
                handCard_0.clearChildren();
                comb_0.clearChildren();
                for (int i1 = 0; i1 < m_cbWeaveItemCount.length; i1++) {
                    GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
//                        weaveItem.
                }
                for (int i1 = 0; i1 < MAX_COUNT - 1 - (3 * cbWeaveItemCount); i1++) {
                    Image pCard = createHandCardImageView(viewChairID, bCardData[i1]);
                    pCard.setOrigin(0, 0);
                    pCard.setPosition(0, x);
                    pCard.setTouchable(Touchable.enabled);
//                        pCard->se(bCardData[i1]);
                    pCard.setName("bt_card_" + (int) bCardData[i1]);
                    pCard.addListener(clickListener);
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                    handCard_0.addActor(pCard);
                    x += 40;
                }
            }

            if (viewChairID == 2) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_2");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_2");
                if (handCard_0 == null) continue;
                handCard_0.clearChildren();
                comb_0.clearChildren();
                for (int i1 = 0; i1 < m_cbWeaveItemCount.length; i1++) {
                    GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
//                        weaveItem.
                }
                for (int i1 = 0; i1 < MAX_COUNT - 1 - (3 * cbWeaveItemCount); i1++) {
                    Image pCard = createHandCardImageView(viewChairID, bCardData[i1]);
                    pCard.setOrigin(0, 0);
                    pCard.setPosition(x, 0);
                    pCard.setTouchable(Touchable.enabled);
//                        pCard->se(bCardData[i1]);
                    pCard.setName("bt_card_" + (int) bCardData[i1]);
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                    pCard.addListener(clickListener);
                    handCard_0.addActor(pCard);
                    x += 76;
                }
            }
            if (viewChairID == 3) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_3");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_3");
                if (handCard_0 == null) continue;
                handCard_0.clearChildren();
                comb_0.clearChildren();
                for (int i1 = 0; i1 < m_cbWeaveItemCount.length; i1++) {
                    GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
//                        weaveItem.
                }
                for (int i1 = 0; i1 < MAX_COUNT - 1 - (3 * cbWeaveItemCount); i1++) {
                    Image pCard = createHandCardImageView(viewChairID, bCardData[i1]);
                    pCard.setOrigin(0, 0);
                    pCard.setPosition(0, x);
                    pCard.setTouchable(Touchable.enabled);
//                        pCard->se(bCardData[i1]);
                    pCard.setName("bt_card_" + (int) bCardData[i1]);
                    pCard.addListener(clickListener);
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                    handCard_0.addActor(pCard);
                    x += 40;
                }
            }

            for (int i1 = 0; i1 < m_cbCardIndex[i].length; i1++) {
                m_cbCardIndex[i][i1] = m_cbRepertoryCard[index++];
            }
        }
//        System.out.println("------------------------");
//        int Y = 0;
//        int X = 0;
//        for (int i = 0; i < m_cbCardIndex.length; i++) {
//            System.out.println("this is "+i+ " majiang");
//            for (int i1 = 0; i1 < m_cbCardIndex[0].length; i1++) {
////                System.out.print(" ");
//                String s = (((m_cbCardIndex[i][i1] & 0xF0) >> 4) + 1) + ""
//                        + (m_cbCardIndex[i][i1] & 0x0F);
//                Image image = new Image(Asset.getAsset().getTexture("2/handmah_"+s+".png"));
//                addActor(image);
//                image.setPosition(X+=30,Y);
//            }
//            X = 0;
//            Y += 200;
//            System.out.println();
//        }
    }

    private Image createHandCardImageView(int viewChairID, int bCardDatum) {
        return new Image(Asset.getAsset().getTexture(getHandCardImagePath(viewChairID, bCardDatum)));
    }


    /**
     * 手上的牌路径
     *
     * @param cbViewID
     * @param cbData
     * @return
     */
    public String getHandCardImagePath(int cbViewID, int cbData) {
        String strImagePath = "";
        switch (cbViewID) {
            case 0: {
                String s = (((cbData & 0xF0) >> 4) + 1) + ""
                        + (cbData & 0x0F);

                strImagePath = "2/handmah_" + s + ".png";
                break;
            }
            case 1: {
                strImagePath = "2/hand_left.png";
                break;
            }
            case 2: {
                strImagePath = "2/hand_top.png";
                break;
            }
            case 3: {
                strImagePath = "2/hand_right.png";
                break;
            }
            default:
                break;
        }
        return strImagePath;
    }


    /**
     * 椅子视图切换成界面视图
     *
     * @param cbChairID
     * @return
     */
    public int switchViewChairID(int cbChairID) {
        return (cbChairID + m_CurPlayer - m_MeChairID) % m_CurPlayer;
    }

    private void initInstance() {
        this.m_CurPlayer = 0;
        this.m_MeChairID = 0;
        this.m_GameEngine = GameEngine.GetGameEngine();  //构造游戏引擎
        this.m_GameLogic = new GameLogic();
        this.m_bOperate = false;
        this.m_bMove = false;
        this.m_WeaveItemArray = new GameLogic.TagWeaveItem[GAME_PLAYER][MAX_WEAVE];
        this.m_iOutCardTimeOut = 30;
    }

    void initGame() {
        m_cbLeftCardCount = 0;
        m_cbBankerChair = INVALID_CHAIR;
    }                    //初始化游戏变量

    void aiEnterGame() {
        //机器人玩家加入游戏，返回false说明已经满了，随机生成性别
        m_GameEngine.onUserEnter(new AIPlayer(Math.random() % 2 == 0 ?
                IPlayer.PlayerSex.MALE : IPlayer.PlayerSex.FEMALE, new AIEngine()));
    }          //机器人进入游戏


    public void initLayer() {
        NLog.i("GameLayer initLayer");
        m_Player = rootView;
        //得到四个人的脸
        for (int i = 0; i < GAME_PLAYER; i++) {       //初始化头像节点数组
            //四个人的脸
            m_FaceFrame[i] = (Group) UIHelper.seekNodeByTag(m_Player, "face_frame_" + i);
            //四个人的牌放的位置
            m_PlayerPanel[i] = (Group) UIHelper.seekNodeByTag(m_Player, "PlayerPanel_" + i);
        }
        //操作节点 （用户）
        m_pOperateNotifyGroup = (Group) UIHelper.seekNodeByTag(m_Player, "OperateNotifyGroup");
//        还剩多少牌了
        m_pTextCardNum = (Label) UIHelper.seekNodeByTag(m_Player, "Text_LeftCard");   //操作节点
        RealPlayer pIPlayer = new RealPlayer(IPlayer.PlayerSex.MALE, this);
        m_GameEngine.onUserEnter(pIPlayer);    //玩家加入游戏
    }

    void sendCardTimerUpdate() {
//        m_iOutCardTimeOut = ((m_iOutCardTimeOut-- < 1) ? 0 : m_iOutCardTimeOut);
//        ImageView pTimer1 = (ImageView) (UIHelper.seekNodeByName(m_Player, "Image_Timer_1"));
//        ImageView pTimer0 = (ImageView) (UIHelper.seekNodeByName(m_Player, "Image_Timer_0"));
//        int t = m_iOutCardTimeOut / 10;   //十位
//        int g = m_iOutCardTimeOut % 10;   //各位
//        pTimer1.loadTexture("res/GameLayer/timer_" + g + ".png");
//        pTimer0.loadTexture("res/GameLayer/timer_" + t + ".png");

    }  //倒计时

    void onCardTouch() {
        //触摸牌事件操作
        if (m_pOutCard != null) return;
        if (m_bOperate) {
            ImageView pCard = new ImageView();
            //从玩家牌的种查找
            Group pHandCard0 = (Group) UIHelper.seekNodeByName(m_PlayerPanel[0], "HandCard_0");
            //第一步让所有的牌先回归到0，（复位）
            SnapshotArray<Actor> children = pHandCard0.getChildren();
            for (Actor child : children) {
                child.setY(0);
            }
//            const auto &aChildren = pHandCard0->getChildren();
//            for (auto &subWidget : aChildren) {
//                Node *child = dynamic_cast<Node *>(subWidget);
//                if (pCard != child) {
//                    child->setPositionY(0.0f);
//                }
//            }
            Group pRecvHandCard0 = (Group) UIHelper.seekNodeByName(m_PlayerPanel[0], "RecvHandCard_0");
//      新揭开的牌需要归位
//      1.首先将Y设置为1；
//      2.
            for (Actor child : pRecvHandCard0.getChildren()) {
                if (pCard != child) {
                    child.setY(0);
                }
            }

            if (pCard != null) {
                String btCardName = pCard.getName();
                //出牌状态
                int status = 0;
                if (status == 1) {
                    m_pOutCard = null;         //正在出牌状态
                    m_bMove = false;                                //是否移动牌状态
                    m_startVec = pCard.getPosition();              //记录开始位置
                    //听牌判断
                    int cbCardData = pCard.getTag();   //牌
                    int cbWeaveItemCount = m_cbWeaveItemCount[m_MeChairID];
                    GameLogic.TagWeaveItem[] pTagWeaveItem = m_WeaveItemArray[m_MeChairID];
                    int[] cbTingCard = new int[MAX_INDEX];
//                memset(&cbTingCard, 0, sizeof(cbTingCard));
                    int[] cbCardIndex = m_cbCardIndex[m_MeChairID];
//                memcpy(&cbTingCard, cbCardIndex, sizeof(cbTingCard));   //内存拷贝
                    cbTingCard[m_GameLogic.switchToCardIndex(cbCardData)]--;   //移除要出的牌进行分析
//                showTingResult(cbTingCard, pTagWeaveItem, cbWeaveItemCount);
                } else if (status == 2) {
                    Vector2 a = pCard.getPosition();//开始位置
                    Vector2 b = pCard.getPosition();//移动的位置
                    pCard.setPosition(m_startVec.add(b.sub(a)));       //移动
                    if (b.y - a.y > 60 || Math.abs(b.x - a.x) > 30) {    //移动判定
                        m_bMove = true;
                    }
                } else {
                    m_pOutCard = null;                             //结束出牌状态
                    Vector2 endVec = pCard.getPosition();
                    if (endVec.y - m_startVec.y > 118) {
                        NLog.i("out card");
                        GameCmd.CMD_C_OutCard OutCard = new GameCmd.CMD_C_OutCard();
//                    memset(&OutCard, 0, sizeof(CMD_C_OutCard));
                        OutCard.cbCardData = pCard.getTag();
                        m_GameEngine.onUserOutCard(OutCard);
                    } else {
                        if (m_startVec.y == m_cardPosY) {                               //正常状态
                            if (m_bMove) {
                                pCard.setPosition(m_startVec);                         //移动
                            } else {
                                pCard.setPosition(m_startVec.add(new Vector2(0, m_outY)));       //移动
                            }
                        } else if (m_startVec.y == m_cardPosY + m_outY) {               //选牌状态
                            Vector2 a = pCard.getPosition();                    //触摸上半部分，撤销出牌
                            if (a.y > 118) {
                                pCard.setPosition(m_startVec.sub(new Vector2(0, m_outY)));
                            } else {                                                   //触摸下半部分，出牌
                                pCard.setPosition(m_startVec);
                                GameCmd.CMD_C_OutCard OutCard = null;
//                            memset( & OutCard, 0, sizeof(CMD_C_OutCard));
                                OutCard.cbCardData = (pCard.getTag());
                                m_GameEngine.onUserOutCard(OutCard);
                            }
                        } else {
                            pCard.setPosition(m_startVec);    //移动
                        }
                    }
                }
            }
        }   //触摸牌的事件

    }

    //显示手上的牌
    boolean showAndUpdateDiscardCard() {
        for (int cbChairID = 0; cbChairID < m_CurPlayer; cbChairID++) {
            int cbViewID = switchViewChairID(cbChairID);
            switch (cbViewID) {
                case 0: {
                    Group pDiscardCard0 = m_Player.findActor("DiscardCard_0");
                    pDiscardCard0.clearChildren();
                    int bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (int i = 0; i < bDiscardCount; i++) {
                        int col = i % 12;
                        int row = i / 12;
                        x = (col == 0) ? 0 : x;  //X复位
                        y = row * 90;
                        Image pCard0 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard0.setOrigin(0, 0);
                        pCard0.setPosition(x, y);
                        pCard0.setZIndex(10 - row);
                        x += 76;
                        pDiscardCard0.addActor(pCard0);
                    }
                    pDiscardCard0.setScale(0.7F, 0.7F);
                    break;
                }
                case 1: {
                    //显示出的牌
                    Group pDiscardCard1 = m_Player.findActor("DiscardCard_1");
//                    ui::Layout *pDiscardCard1 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_1"));
                    pDiscardCard1.clearChildren();
                    int bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (int i = 0; i < bDiscardCount; i++) {
                        int col = i % 11;
                        int row = i / 11;
                        y = (col == 0) ? 0 : y;  //X复位
                        x = 116 * row;
                        Image pCard1 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard1.setOrigin(0, 0);
                        pCard1.setPosition(x, 740 - y);
                        pCard1.setZIndex(col);
                        y += 74;
                        pDiscardCard1.addActor(pCard1);
                    }
                    pDiscardCard1.setScale(0.5F, 0.5F);
                    break;
                }
                case 2: {
                    Group pDiscardCard2 = m_Player.findActor("DiscardCard_2");
//                    ui::Layout *pDiscardCard2 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_2"));
                    pDiscardCard2.clearChildren();
                    int bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (int i = 0; i < bDiscardCount; i++) {
                        int col = (i % 12);
                        int row = (i / 12);
                        x = (col == 0) ? 0 : x;  //X复位
                        y = 90 - row * 90;

                        Image pCard2 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard2.setOrigin(0, 0);
                        pCard2.setPosition(x, y);
                        pCard2.setZIndex(row);
                        x += 76;
                        pDiscardCard2.addActor(pCard2);
                    }
                    pDiscardCard2.setScale(0.7F, 0.7F);

                    break;
                }
                case 3: {
                    //显示出的牌
                    Group pDiscardCard3 = m_Player.findActor("DiscardCard_3");
//                    ui::Layout *pDiscardCard3 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_3"));
//                    pDiscardCard3.removeAllChildren();
                    pDiscardCard3.clearChildren();
                    int bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (int i = 0; i < bDiscardCount; i++) {
                        int col = (i % 11);
                        int row = (i / 11);
                        y = (col == 0) ? 0 : y;  //X复位
                        x = 240 - (116 * row);
                        Image pCard3 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard3.setOrigin(0, 0);
                        pCard3.setPosition(x, y);
                        pCard3.setZIndex(20 - col);
                        y += 74;
                        pDiscardCard3.addActor(pCard3);
                    }
                    pDiscardCard3.setScale(0.5F, 0.5F);
                    break;
                }
                default:
                    break;
            }
        }
        return true;
    }

    private Image createDiscardCardImageView(int cbViewID, int cbData) {
        return new Image(new Texture(getDiscardCardImagePath(cbViewID, cbData)));
    }


    @Override
    public void setIPlayer(IPlayer pIPlayer) {

    }

    @Override
    public boolean onUserEnterEvent(IPlayer pIPlayer) {
        m_Players[m_CurPlayer++] = pIPlayer;
        /// <summary>
        ///这个为啥是循环，不是很明白
        /// </summary>
        /// <param name="pIPlayer"></param>
        /// <returns></returns>
        for (int i = 0; i < m_CurPlayer; i++) {           //显示头像
            Image pImageHeader = m_FaceFrame[i].findActor("Image_Header");  //头像
            Label pTextScore = m_FaceFrame[i].findActor("Text_Score");  //头像
            pTextScore.setText("0");     //进来分数为0
//            pImageHeader.seloadTexture(utility::toString("res/GameLayer/im_defaulthead_", m_Players[i]->getSex() == IPlayer::FEMALE ? 0 : 1, ".png"));    //设置头像
        }
        if (m_CurPlayer == GAME_PLAYER) {
//            unschedule(CC_SCHEDULE_SELECTOR(GameLayer::aiEnterGame));   //人满，关闭ai加入任务
            return true;
        } //显示头像
        return true;
    }

    @Override
    public boolean onGameStartEvent(GameCmd.CMD_S_GameStart GameStart) {
        initGame();
        m_cbLeftCardCount = GameStart.cbLeftCardCount;
        m_cbBankerChair = GameStart.cbBankerUser;
        m_GameLogic.switchToCardIndex(GameStart.cbCardData, MAX_COUNT - 1, m_cbCardIndex[m_MeChairID]);
        //界面显示
        m_pTextCardNum.setText(m_cbLeftCardCount + "");
//        showAndUpdateHandCard();
        showAndUpdateDiscardCard();
        return true;
    }

    /**
     * 发牌事件
     *
     * @param SendCard
     * @return
     */

    public boolean onSendCardEvent(GameCmd.CMD_S_SendCard SendCard) {
        m_iOutCardTimeOut = 30; //重置计时器
        m_cbLeftCardCount--;
        if (SendCard.cbCurrentUser == m_MeChairID) {
            m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(SendCard.cbCardData)]++;
        }
        return showSendCard(SendCard);
    }

    @Override
    public boolean onOutCardEvent(GameCmd.CMD_S_OutCard OutCard) {
        if (OutCard.cbOutCardUser == m_MeChairID) {
            m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(OutCard.cbOutCardData)]--;
        }
        m_cbDiscardCard[OutCard.cbOutCardUser][m_cbDiscardCount[OutCard.cbOutCardUser]++] = OutCard.cbOutCardData;
        int cbViewID = switchViewChairID(OutCard.cbOutCardUser);
//        cocostudio::timeline::ActionTimeline *action = CSLoader::createTimeline("res/SignAnim.csb");
//        action->gotoFrameAndPlay(0, 10, true);
//        Node *pSignNode = CSLoader::createNode("res/SignAnim.csb");
//        pSignNode->setName("SignAnim");
//        std::vector<Node *> aChildren;
//        aChildren.clear();
//        UIHelper::getChildren(m_pLayer, "SignAnim", aChildren);
//        for (auto &subWidget : aChildren) {
//            subWidget->removeFromParent();
//        }
        Image pRecvCard = (Image) UIHelper.seekNodeByName(m_PlayerPanel[cbViewID], "RecvCard_" + cbViewID);
        if (pRecvCard != null) {
            pRecvCard.setVisible(false);
        }
        switch (cbViewID) {
            case 0: {
                showAndUpdateHandCard();                     //更新手上的牌
                Group pRecvCardList = (Group) UIHelper.seekNodeByName(m_PlayerPanel[cbViewID], "RecvHandCard_0");
                pRecvCardList.clearChildren(); //移除出牌位置的牌
                Group pDiscardCard0 = (Group) UIHelper.seekNodeByName(m_Player, "DiscardCard_0");//显示出的牌
                pDiscardCard0.clearChildren();
                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //12
                float x = 0;
                float y = 0;
                for (int i = 0; i < bDiscardCount; i++) {
                    int col = (i % 12);
                    int row = (i / 12);
                    x = (col == 0) ? 0 : x;  //X复位
                    y = row * 90;
                    Image pCard0 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
                    pCard0.setOrigin(0, 0);
                    pCard0.setPosition(x, y);
                    pCard0.setZIndex(10 - row);
                    x += 76;
                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
//                        pSignNode.set(Vec2(0.5, 0.5));
//                        pSignNode->setPosition(Vec2(39, 110));
//                        pCard0->addChild(pSignNode);
//                        pSignNode->runAction(action);
                    }
                    pDiscardCard0.addActor(pCard0);
                }
                pDiscardCard0.setScale(0.7F, 0.7F);
                //听牌判断
                int cbWeaveItemCount = m_cbWeaveItemCount[OutCard.cbOutCardUser];
                GameLogic.TagWeaveItem[] pTagWeaveItem = m_WeaveItemArray[OutCard.cbOutCardUser];
                int[] cbCardIndex = m_cbCardIndex[OutCard.cbOutCardUser];
                showTingResult(cbCardIndex, pTagWeaveItem, cbWeaveItemCount);
                break;
            }
            case 1: {
                //显示出的牌
                Group pDiscardCard1 = m_Player.findActor("DiscardCard_1");
                pDiscardCard1.clearChildren();
                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //
                float x = 0;
                float y = 0;
                for (int i = 0; i < bDiscardCount; i++) {
                    int col = (i % 11);
                    int row = (i / 11);
                    y = (col == 0) ? 0 : y;  //X复位
                    x = 116 * row;
                    Image pCard1 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
                    pCard1.setOrigin(0, 0);
                    pCard1.setPosition(x, 740 - y);
                    pCard1.setZIndex(col);
                    y += 74;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
//                        pSignNode.setO(Vec2(0.5, 0.5));
//                        pSignNode.setPosition(Vec2(81, 110));
//                        pCard1->addChild(pSignNode);
//                        pSignNode->runAction(action);
//                    }
                    pDiscardCard1.addActor(pCard1);
                }
                pDiscardCard1.setScale(0.5F, 0.5F);
                break;
            }
            case 2: {
                Group pDiscardCard2 = m_Player.findActor("DiscardCard_2");
                pDiscardCard2.clearChildren();
                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //12
                float x = 0;
                float y = 0;
                for (int i = 0; i < bDiscardCount; i++) {
                    int col = (i % 12);
                    int row = (i / 12);
                    x = (col == 0) ? 0 : x;  //X复位
                    y = 90 - row * 90;
                    Image pCard2 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
                    pCard2.setOrigin(0, 0);
                    pCard2.setPosition(x, y);
                    pCard2.setZIndex(row);
                    x += 76;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
//                        pSignNode->setAnchorPoint(Vec2(0.5, 0.5));
//                        pSignNode->setPosition(Vec2(39, 59));
//                        pCard2->addChild(pSignNode);
//                        pSignNode->runAction(action);
//                    }
                    pDiscardCard2.addActor(pCard2);
                }
                pDiscardCard2.setScale(0.7F, 0.7F);

                break;
            }
            case 3: {
                //显示出的牌
                Group pDiscardCard3 = m_Player.findActor("DiscardCard_3");
                pDiscardCard3.clearChildren();
                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //
                float x = 0;
                float y = 0;
                for (int i = 0; i < bDiscardCount; i++) {
                    int col = (i % 11);
                    int row = (i / 11);
                    y = (col == 0) ? 0 : y;  //X复位
                    x = 240 - (116 * row);
                    Image pCard3 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
                    pCard3.setOrigin(0, 0);
                    pCard3.setPosition(x, y);
                    pCard3.setZIndex(20 - col);
                    y += 74;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
//                        pSignNode->setAnchorPoint(Vec2(0.5, 0.5));
//                        pSignNode->setPosition(Vec2(39, 110));
//                        pCard3->addChild(pSignNode);
//                        pSignNode->runAction(action);
//                    }
                    pDiscardCard3.addActor(pCard3);
                }
                pDiscardCard3.setScale(0.5F, 0.5F);
                break;
            }
            default:
                break;
        }
        for (int j = 0; j < GAME_PLAYER; ++j) {   //发牌后隐藏导航
            Image pHighlight = m_Player.findActor("Image_Wheel_" + j);
            pHighlight.setVisible(false);
        }
//        playSound(utility::toString("raw/Mahjong/", (IPlayer::FEMALE == m_Players[OutCard.cbOutCardUser]->getSex() ? "female" : "male"), "/mjt", utility::toString((
//                (OutCard.cbOutCardData & MASK_COLOR)
//                        >> 4) + 1, "_", OutCard.cbOutCardData & MASK_VALUE), ".mp3"));    //播放牌的声音
        return true;
    }


    boolean showTingResult(int[] cbCardIndex, GameLogic.TagWeaveItem[] WeaveItem, int cbWeaveCount) {
        GameLogic.tagTingResult tingResult = new GameLogic.tagTingResult();
//        memset(&tingResult, 0, sizeof(tagTingResult));
        Group pTingNode = m_PlayerPanel[m_MeChairID].findActor("ting_0");
        if (m_GameLogic.analyseTingCardResult(cbCardIndex, WeaveItem, cbWeaveCount, tingResult)) {  //听牌
            pTingNode.setVisible(true);
            Group pTingCard = (Group) UIHelper.seekNodeByName(m_PlayerPanel[m_MeChairID], "ting_card");
            pTingCard.clearChildren();
            for (int i = 0; i < tingResult.cbTingCount; i++) {  //循环听的牌
                Image pTingCardImage = createDiscardCardImageView(0, tingResult.cbTingCard[i]);
                pTingCardImage.setOrigin(0, 0);
                pTingCardImage.setPosition((76 * i + ((76.0f * 3) - (76.0f * tingResult.cbTingCount)) / 2.0f), 0);
                pTingCard.addActor(pTingCardImage);
            }
        } else {
            pTingNode.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOperateNotifyEvent(GameCmd.CMD_S_OperateNotify OperateNotify) {
        return showOperateNotify(OperateNotify);
    }

    @Override
    public boolean onOperateResultEvent(GameCmd.CMD_S_OperateResult OperateResult) {
        GameLogic.TagWeaveItem weaveItem = new GameLogic.TagWeaveItem();
//        memset(&weaveItem, 0, sizeof(tagWeaveItem));
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
                if (OperateResult.cbOperateUser == m_MeChairID) {  //自己
                    int cbReomveCard[] = {OperateResult.cbOperateCard, OperateResult.cbOperateCard};
                    m_GameLogic.removeCard(m_cbCardIndex[OperateResult.cbOperateUser], cbReomveCard, cbReomveCard.length);
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

        //更新界面
        int cbViewID = switchViewChairID(OperateResult.cbOperateUser);
        switch (cbViewID) {
            case 0: {   //自己操作反馈
                m_pOperateNotifyGroup.clearChildren();
                m_pOperateNotifyGroup.setVisible(false);
                Image pHighlight = (Image) (UIHelper.seekNodeByName(m_Player, "Image_Wheel_0"));
                pHighlight.setVisible(true);
                break;
            }
            default:
                break;
        }
        String strSex = (IPlayer.PlayerSex.FEMALE ==
                m_Players[OperateResult.cbOperateUser].getSex() ? "female" : "male");
        switch (OperateResult.cbOperateCode) {
            case 0: {
                break;
            }
            case 1: {
//                playSound(utility::toString("raw/Mahjong/", strSex, "/peng.mp3"));
                if (cbViewID == 0) {                    //如果是自己，碰完需要出牌
                    int bTempCardData[] = new int[MAX_COUNT];   //手上的牌
//                    memset(bTempCardData, 0, sizeof(bTempCardData));
                    m_GameLogic.switchToCardData(m_cbCardIndex[OperateResult.cbOperateUser], bTempCardData, MAX_COUNT);
                    int cbWeaveItemCount = m_cbWeaveItemCount[OperateResult.cbOperateUser]; //组合数量
                    GameCmd.CMD_S_SendCard SendCard = new GameCmd.CMD_S_SendCard();
//                    memset(&SendCard, 0, siz/eof(CMD_S_SendCard));
                    SendCard.cbCurrentUser = OperateResult.cbOperateUser;
                    SendCard.cbCardData = bTempCardData[MAX_COUNT - (cbWeaveItemCount * 3) - 1];
                    showSendCard(SendCard);    //模拟发送一张牌
                }
                //移除桌上的哪张牌
                m_cbDiscardCount[OperateResult.cbProvideUser]--;  //移除桌上的牌
                showAndUpdateDiscardCard();
                break;
            }
            case 2: {
//                playSound(utility::toString("raw/Mahjong/", strSex, "/gang.mp3"));
                if (OperateResult.cbProvideUser != OperateResult.cbOperateUser) {     //放的杠
                    m_cbDiscardCount[OperateResult.cbProvideUser]--;  //移除桌上的牌
                    showAndUpdateDiscardCard();
                } else {                                                            //移除出牌位置的牌
                    Group pRecvCardList = (Group) UIHelper.seekNodeByName(m_PlayerPanel[0], "RecvHandCard_0");
                    pRecvCardList.clearChildren();
                }
                break;
            }
            case 4: { //胡牌动作放在游戏结束 信息中
                if (OperateResult.cbOperateUser == OperateResult.cbProvideUser) {  //自摸
//                    playSound(utility::toString("raw/Mahjong/", strSex, "/zimo.mp3"));
                } else {
//                    playSound(utility::toString("raw/Mahjong/", strSex, "/hu.mp3"));
                }
                break;
            }
            default:
                break;
        }
//        showAndPlayOperateEffect(cbViewID, OperateResult.cbOperateCode, OperateResult.cbProvideUser == OperateResult.cbOperateUser);
        showAndUpdateHandCard();
        return true;
    }

    @Override
    public boolean onGameEndEvent(GameCmd.CMD_S_GameEnd GameEnd) {
        showAndUpdateUserScore(GameEnd.lGameScoreTable);    //更新分数
        for (int i = 0; i < GAME_PLAYER; i++) {                    //播放音效
            if (FvMask.HasAny(GameEnd.cbHuUser, FvMask._MASK_(i))) {
                String strSex = (IPlayer.PlayerSex.FEMALE ==
                        m_Players[i].getSex() ? "female" : "male");
                int cbViewID = switchViewChairID(i);
                if (FvMask.HasAny(GameEnd.cbHuUser, FvMask._MASK_(GameEnd.cbProvideUser))) {  //自摸
//                    playSound(utility::toString("raw/Mahjong/", strSex, "/zimo.mp3"));
//                    showAndPlayOperateEffect(cbViewID, WIK_H, true); //播放自摸动画
                } else {
//                    playSound(utility::toString("raw/Mahjong/", strSex, "/hu.mp3"));
//                    showAndPlayOperateEffect(cbViewID, WIK_H, false); //播放自摸动画
                }
            }
        }

        Group pTingNode = (Group) UIHelper.seekNodeByName(m_PlayerPanel[0], "ting_0");    //隐藏听牌
        pTingNode.setVisible(false);
        m_pOperateNotifyGroup.clearChildren();
        m_pOperateNotifyGroup.setVisible(false);             //移除通知

        //显示结算界面
//        m_pGameOverNode = CSLoader::createNode("GameOverLayer.csb");
//        m_pGameOverNode->setAnchorPoint(Vec2(0.5, 0.5));
//        m_pGameOverNode->setPosition(GameSceneManager::getInstance()->getVisibleSize() / 2);
//        m_pLayer->addChild(m_pGameOverNode);
//        ui::Button *pOverClose = dynamic_cast<ui::Button *>(UIHelper::seekNodeByName(m_pGameOverNode, "Button_Over_Close"));    //关闭结算界面按钮
//        pOverClose->addTouchEventListener(CC_CALLBACK_2(GameLayer::onTouch, this));
//        ui::ImageView *pOverResultImage = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(m_pGameOverNode, "ImageView_Over_Result"));
//        pOverResultImage->loadTexture((GameEnd.lGameScore[m_MeChairID] < 0) ? "res/GameOverLayer/result_lose.png" : "res/GameOverLayer/result_win.png");
//        if (GameEnd.cbHuUser == 0x00) {
//            pOverResultImage->loadTexture("res/GameOverLayer/result_draw.png");  //流局
//        }
//        //显示牌
//        for (int i = 0; i < m_CurPlayer; i++) {
//            int cbViewID = switchViewChairID(i);
//            Node *pPlayerViewNode = UIHelper::seekNodeByName(m_pGameOverNode, "PlayerView_" + utility::toString((int) cbViewID));
//            ui::ImageView *pOverImgHead = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pPlayerViewNode, "ImageView_Over_Head")); //头像
//            ui::Text *pOverScoreText = dynamic_cast<ui::Text *>(UIHelper::seekNodeByName(pPlayerViewNode, "Text_Over_Score"));       //分数
//            pOverImgHead->loadTexture(utility::toString("res/GameLayer/im_defaulthead_", m_Players[i]->getSex() == IPlayer::FEMALE ? 0 : 1, ".png"));    //设置头像
//            pOverScoreText->setString(((GameEnd.lGameScore[i] >= 0) ? utility::toString("+", GameEnd.lGameScore[i]) : utility::toString(GameEnd.lGameScore[i])));
//            ui::Layout *pOverHandCard = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(pPlayerViewNode, "Panel_Over_Hand_Card"));
//            pOverHandCard->removeAllChildren();
//            ui::ImageView *pOverHuCard = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pPlayerViewNode, "ImageView_Over_Card"));  //胡的那张
//            ui::ImageView *pOverHuFlag = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pPlayerViewNode, "ImageView_Over_Hu"));  //胡的那张
//            pOverHuCard->setVisible(false);
//            pOverHuFlag->setVisible(false);
//            if (m_cbBankerChair == i) { //庄
//                ui::Layout *pOverPanelLeft = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(pPlayerViewNode, "Panel_Over_Left"));
//                ui::ImageView *pOverBanker = ui::ImageView::create("res/GameOverLayer/over_banker.png");
//                pOverBanker->setAnchorPoint(Vec2(0.5, 0.5));
//                pOverBanker->setPosition(Vec2(20, 96));
//                pOverPanelLeft->addChild(pOverBanker);
//            }
//
//            int cbWeaveCount = GameEnd.cbWeaveCount[i];
//            if (FvMask::HasAny(GameEnd.cbHuUser, _MASK_(i))) {
//                //胡牌手上移除胡的那张牌
//                m_GameLogic->removeCard(GameEnd.cbCardData[i], GameEnd.cbCardCount[i], &GameEnd.cbHuCard, 1);
//                GameEnd.cbCardCount[i]--;
//                pOverHuCard->loadTexture(getDiscardCardImagePath(0, GameEnd.cbHuCard));
//                pOverHuCard->setVisible(true);
//                pOverHuFlag->setVisible(true);
//            }
//            float x = 0;
//            for (int j = 0; j < cbWeaveCount; j++) {   //组合数量
//                CMD_WeaveItem weaveItem = GameEnd.WeaveItemArray[i][j];
//                Node *pWeaveNode = NULL;
//                if (weaveItem.cbWeaveKind == WIK_G) {
//                    pWeaveNode = CSLoader::createNode("res/Gang0.csb");
//                }
//                if (weaveItem.cbWeaveKind == WIK_P) {
//                    pWeaveNode = CSLoader::createNode("res/Peng0.csb");
//                }
//                pWeaveNode->setScale(0.6f, 0.6f);
//                pWeaveNode->setPosition(Vec2(x, 0));
//            const std::string &strImagePath = getDiscardCardImagePath(0, weaveItem.cbCenterCard);
//            const std::string &strBackImagePath = getDiscardCardImagePath(0, weaveItem.cbCenterCard);
//                ui::ImageView *pImageRight = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_right"));
//                ui::ImageView *pImageLeft = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_left"));
//                ui::ImageView *pImageCenter = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_center"));
//                pImageRight->loadTexture(strImagePath);
//                pImageCenter->loadTexture(strImagePath);
//                pImageLeft->loadTexture(strImagePath);
//                int wProvideViewID = switchViewChairID(weaveItem.cbProvideUser);
//                switch (cbViewID) {
//                    case 0:
//                        switch (wProvideViewID) {
//                            case 0: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageLeft->loadTexture(strBackImagePath);
//                                }
//                                break;
//                            }
//                            case 1: {
//                                pImageLeft->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 2: {
//                                pImageCenter->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 3: {
//                                pImageRight->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        break;
//                    case 1:
//                        switch (wProvideViewID) {
//                            case 0: {
//                                pImageRight->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 1: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageLeft->loadTexture(strBackImagePath);
//                                }
//                                break;
//                            }
//                            case 2: {
//                                pImageLeft->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 3: {
//                                pImageCenter->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        break;
//                    case 2:
//                        switch (wProvideViewID) {
//                            case 0: {
//                                pImageCenter->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 1: {
//                                pImageLeft->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 2: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageLeft->loadTexture(strBackImagePath);
//                                }
//                                break;
//                            }
//                            case 3: {
//                                pImageRight->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        break;
//                    case 3:
//                        switch (wProvideViewID) {
//                            case 0: {
//                                pImageRight->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 1: {
//                                pImageCenter->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 2: {
//                                pImageLeft->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 3: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageLeft->loadTexture(strBackImagePath);
//                                }
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//
//                pOverHandCard.addActor(pWeaveNode);
//                x += 132;
//            }
//            for (int j = 0; j < MAX_COUNT - 1 - (3 * cbWeaveCount); j++) {
//                ui::ImageView *pCard = createDiscardCardImageView(0, GameEnd.cbCardData[i][j]);
//                pCard->setAnchorPoint(Vec2(0, 0));
//                pCard->setPosition(Vec2(x, 0));
//                pCard->setScale(0.6f, 0.6f);
//                pOverHandCard.(pCard);
//                x += 43;
//            }
//        }

        return true;
    }

    boolean showAndUpdateUserScore(int lGameScoreTable[]) {
        for (int i = 0; i < GAME_PLAYER; i++) {
            int cbViewID = switchViewChairID(i);
            Label pScore = (Label) UIHelper.seekNodeByName(m_FaceFrame[cbViewID], "Gold_Label");
            if (pScore != null) {
                pScore.setText("" + lGameScoreTable[i]);
            }
        }
        return true;
    }


    /**
     * 发牌显示
     *
     * @param SendCard
     * @return
     */
    boolean showSendCard(GameCmd.CMD_S_SendCard SendCard) {
        m_pOperateNotifyGroup.clearChildren();
        m_pOperateNotifyGroup.setVisible(true);
//        sendCardTimerUpdate();
//        sendCardTimerUpdate), 1.0f);    //出牌计时
//        sendCardTimerUpdate(1.0f);//立即执行
        m_pTextCardNum.setText(m_cbLeftCardCount + "");
        int cbViewID = switchViewChairID(SendCard.cbCurrentUser);
        m_bOperate = false;
        switch (cbViewID) {
            case 0: {
                m_bOperate = true;   //允许选牌
                Group pRecvCardList = m_PlayerPanel[cbViewID].findActor("RecvHandCard_0");
//                ui::Layout *pRecvCardList = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[cbViewID], utility::toString("RecvHandCard_0")));
                pRecvCardList.clearChildren();
                Image pCard = createHandCardImageView(cbViewID, SendCard.cbCardData);
                pCard.setOrigin(0, 0);
                pCard.setPosition(0, 0);
                pCard.setTouchable(Touchable.enabled);
//                pCard.setTag(SendCard.cbCardData);
                pCard.setName("bt_card_" + (int) SendCard.cbCardData);
                pCard.addListener(clickListener);
                pRecvCardList.addActor(pCard);
                m_PlayerPanel[cbViewID].findActor("ting_0").setVisible(false);
//                UIHelper::seekNodeByName(m_PlayerPanel[cbViewID], "ting_0")->setVisible(false); //拿到牌，隐藏听牌界面
                if (SendCard.cbActionMask != WIK_NULL) {//发的牌存在动作，模拟发送操作通知
                    GameCmd.CMD_S_OperateNotify OperateNotify = new GameCmd.CMD_S_OperateNotify();
//                    memset(&OperateNotify, 0, sizeof(CMD_S_OperateNotify));
                    OperateNotify.cbActionMask = SendCard.cbActionMask;
                    OperateNotify.cbActionCard = SendCard.cbCardData;
                    OperateNotify.cbGangCount = SendCard.cbGangCount;
//                    memcpy(OperateNotify.cbGangCard, SendCard.cbGangCard, sizeof(OperateNotify.cbGangCard));
                    showOperateNotify(OperateNotify);
                }
                break;
            }
            case 1:
            case 2:
            case 3:
            default:
                m_bOperate = false;  //不允许操作
                break;
        }
        for (int i = 0; i < GAME_PLAYER; i++) {
            m_PlayerPanel[cbViewID].findActor("RecvCard_" + i);
            Image pRecvCard = m_PlayerPanel[cbViewID].findActor("RecvCard_" + i);
            if (pRecvCard != null) {
                pRecvCard.setVisible(cbViewID == i);
            }
            Image pHighlight = m_Player.findActor("Image_Wheel_" + i);
            if (pHighlight != null) {
                pHighlight.setVisible(cbViewID == i);
            }
        }
        return true;
    }


    /**
     * 展示操作层
     *
     * @param OperateNotify
     * @return
     */
    boolean showOperateNotify(GameCmd.CMD_S_OperateNotify OperateNotify) {
        if (OperateNotify.cbActionMask == WIK_NULL) {
            return true; //无动作
        }
        m_pOperateNotifyGroup.clearChildren();
        m_pOperateNotifyGroup.setVisible(true);
        float x = 500.0f;
        float y = 65.0f;
        if ((OperateNotify.cbActionMask & WIK_H) != 0) {
            //hu
//            Node pHuNode = CSLoader::createNode("res/BtnHu.csb");
//            pHuNode->setPosition(Vec2(x, y));
//            ui::Button *pHuBtn = dynamic_cast<ui::Button *>(pHuNode->getChildren().at(0));
//            pHuBtn->setTag(OperateNotify.cbActionCard);
//            pHuBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
//            m_pOperateNotifyGroup->addChild(pHuNode);
//            x -= 160;
        }
        if ((OperateNotify.cbActionMask & WIK_G) != 0) {
            for (int i = 0; i < OperateNotify.cbGangCount; i++) {
//                Node *pGangNode = CSLoader::createNode("res/BtnGang.csb");
//                pGangNode->setPosition(Vec2(x, y + (i * 120)));
//                ui::Button *pGangBtn = dynamic_cast<ui::Button *>(pGangNode->getChildren().at(0));
//                ui::ImageView *pImgGangCard = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pGangBtn, "ImgGangCard"));
//                pImgGangCard->loadTexture(getDiscardCardImagePath(0, OperateNotify.cbGangCard[i]));
//                pImgGangCard->setVisible(OperateNotify.cbGangCount > 1);
//                pGangBtn->setTag(OperateNotify.cbGangCard[i]);
//                pGangBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
//                m_pOperateNotifyGroup->addChild(pGangNode);
            }
            x -= 160;
        }
        if ((OperateNotify.cbActionMask & WIK_P) != 0) {
//            Node *pPengNode = CSLoader::createNode("res/BtnPeng.csb");
//            pPengNode->setPosition(Vec2(x, y));
//            ui::Button *pPengBtn = dynamic_cast<ui::Button *>(pPengNode->getChildren().at(0));
//            pPengBtn->setTag(OperateNotify.cbActionCard);
//            pPengBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
//            m_pOperateNotifyGroup->addChild(pPengNode);
            x -= 160;
        }
//        Node *pGuoNode = CSLoader::createNode("res/BtnGuo.csb");
//        pGuoNode->setPosition(Vec2(x, y));
//        ui::Button *pGuoBtn = dynamic_cast<ui::Button *>(pGuoNode->getChildren().at(0));
//        pGuoBtn->setTag(OperateNotify.cbActionCard);
//        pGuoBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
//        m_pOperateNotifyGroup->addChild(pGuoNode);
        return true;
    }


    /**
     * 桌面显示的牌
     *
     * @param cbViewID
     * @param cbData
     * @return
     */
    String getDiscardCardImagePath(int cbViewID, int cbData) {
        String strImagePath = "";
        switch (cbViewID) {
            case 0: {
                strImagePath = "2/mingmah_" + (((cbData & MASK_COLOR)
                        >> 4) + 1) + (cbData & MASK_VALUE) + ".png";
                break;
            }
            case 1: {
                strImagePath = "2/mingmah_" + (((cbData & MASK_COLOR)
                        >> 4) + 1) + (cbData & MASK_VALUE) + ".png";
                break;
            }
            case 2: {
                strImagePath = "2/mingmah_" + (((cbData & MASK_COLOR)
                        >> 4) + 1) + (cbData & MASK_VALUE) + ".png";
                break;
            }
            case 3: {
                strImagePath = "2/mingmah_" + (((cbData & MASK_COLOR)
                        >> 4) + 1) + (cbData & MASK_VALUE) + ".png";
                break;
            }
            default:
                break;
        }
        return strImagePath;
    }


    /**
     * 背面
     *
     * @param cbViewID
     * @return
     */
    String getBackCardImagePath(int cbViewID) {
        String strImagePath = "";
        switch (cbViewID) {
            case 0: {
                strImagePath = "res/GameLayer/Mahjong/2/mingmah_00.png";
                break;
            }
            case 1: {
                strImagePath = "res/GameLayer/Mahjong/1/mingmah_00.png";
                break;
            }
            case 2: {
                strImagePath = "res/GameLayer/Mahjong/2/mingmah_00.png";
                break;
            }
            case 3: {
                strImagePath = "res/GameLayer/Mahjong/1/mingmah_00.png";
                break;
            }
            default:
                break;
        }
        return strImagePath;
    }

    public void exit() {
        Gdx.app.exit();
    }

    int switchChairViewID(int cbViewID) {
        return (cbViewID + m_MeChairID) % m_CurPlayer;
    }

//    0------------------------------------------------------------
//    public void onTouchEnded(Widget *pWidget, const char *pName) {
//        if (strcmp(pName, "Button_Over_Close") == 0) {    //关闭按钮
//            m_pGameOverNode->removeFromParent();
//            m_pGameOverNode = NULL;
//            m_GameEngine->onGameRestart();              //重新开始游戏
//        } else if (strcmp(pName, "Button_Exit") == 0) {     //退出游戏按钮
//            GameSceneManager::getInstance()->confirm("退出游戏后，本局游戏将直接结束无法恢复，确定是否退出？", false, false, this, CC_CALLFUNCN_SELECTOR(GameLayer::exitGame));
//        } else if (strcmp(pName, "Button_Set") == 0) {      //游戏设置按钮
//            m_pLayer->addChild(SetLayer::create()->GetLayer()); //显示设置层
//        }
//    }

    /**
     * 出牌
     *
     * @param ref
     * @param eventType
     */

    private ClickListener clickListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
//            onCardTouch(Ref *ref, ui::Widget::TouchEventType eventType) {
//            if (m_pOutCard != null) return;
            if (m_bOperate) {
                Actor pCard = event.getTarget();
                Group pHandCard0 = m_PlayerPanel[0].findActor("HandCard_0");
                SnapshotArray<Actor> aChildren = pHandCard0.getChildren();
                for (Actor actor : aChildren) {
//                        Node *child = dynamic_cast<Node *>(subWidget);
                    if (actor != pCard) {
                        actor.setY(0.0f);
                    }
                }
                Group pRecvHandCard0 = m_PlayerPanel[0].findActor("RecvHandCard_0");
                SnapshotArray<Actor> bChildren = pRecvHandCard0.getChildren();
                for (Actor actor : bChildren) {
                    if (pCard != actor) {
                        actor.setY(0.0f);
                    }
                }
                if (pCard != null) {
                    String btCardName = pCard.getName();
//                            case ui::Widget::TouchEventType::BEGAN: {
                    m_pOutCard = pCard;         //正在出牌状态
                    m_bMove = false;                                //是否移动牌状态
                    m_startVec.x = pCard.getX();              //记录开始位置
                    m_startVec.y = pCard.getY();
                    //听牌判断
//                        int cbCardData = pCard.getTag();   //牌
                    int cbWeaveItemCount = m_cbWeaveItemCount[m_MeChairID];
                    GameLogic.TagWeaveItem[] pTagWeaveItem = m_WeaveItemArray[m_MeChairID];
                    int cbTingCard[] = new int[MAX_INDEX];
//                        memset(&cbTingCard, 0, sizeof(cbTingCard));
                    int cbCardIndex[] = m_cbCardIndex[m_MeChairID];
//                        memcpy(&cbTingCard, cbCardIndex, sizeof(cbTingCard));   //内存拷贝
//                        cbTingCard[m_GameLogic.switchToCardIndex(cbCardData)]--;   //移除要出的牌进行分析
                    showTingResult(cbTingCard, pTagWeaveItem, cbWeaveItemCount);
//                        break;
                }
//                            case ui::Widget::TouchEventType::MOVED: {
//                                Vec2 a = pCard->getTouchBeganPosition();
//                                Vec2 b = pCard->getTouchMovePosition();
//                                pCard->setPosition(m_startVec + (b - a));       //移动
//                                if (b.y - a.y > 60 || abs(b.x - a.x) > 30) {    //移动判定
//                                    m_bMove = true;
//                                }
//                                break;
////                            }
//                            case ui::Widget::TouchEventType::CANCELED:
//                            case ui::Widget::TouchEventType::ENDED: {
//                                m_pOutCard = NULL;                             //结束出牌状态
                Vector2 endVec = new Vector2(pCard.getX(), pCard.getY());
                if (endVec.y - m_startVec.y > 118) {
                    NLog.i("out card");
                    GameCmd.CMD_C_OutCard OutCard = new GameCmd.CMD_C_OutCard();
//                                    memset(&OutCard, 0, sizeof(CMD_C_OutCard));
//                                    OutCard.cbCardData = pCard->getTag();
                    m_GameEngine.onUserOutCard(OutCard);
                } else {
                    if (m_startVec.y == m_cardPosY) {                               //正常状态
                        if (m_bMove) {
                            pCard.setPosition(m_startVec);                         //移动
                        } else {
                            pCard.setPosition(m_startVec.x, m_startVec.y + m_outY);       //移动
                        }
                    } else if (m_startVec.y == m_cardPosY + m_outY) {               //选牌状态
//                                        Vector2 a = pCard.getTouchBeganPosition();                    //触摸上半部分，撤销出牌
                        if (y > 118) {
                            pCard.setPosition(m_startVec.x, m_startVec.y + m_outY);
                        } else {                                                   //触摸下半部分，出牌
                            pCard.setPosition(m_startVec);
                            GameCmd.CMD_C_OutCard OutCard = new GameCmd.CMD_C_OutCard();
//                                            memset(&OutCard, 0, sizeof(CMD_C_OutCard));
//                                            OutCard.cbCardData = (pCard->getTag());
                            m_GameEngine.onUserOutCard(OutCard);
                        }
                    } else {
                        pCard.setPosition(m_startVec);    //移动
                    }
                }

                        }
//                    }
//                }
//            }


            };


//    void GameLayer::onOperateTouch(Ref *ref, ui::Widget::TouchEventType eventType) {
//        ui::Button *pRef = dynamic_cast<ui::Button *>(ref);
//        if (pRef != NULL) {
//            std::string btnName = pRef->getName();
//            int iTag = pRef->getTag();
//            switch (eventType) {
//                case ui::Widget::TouchEventType::ENDED:
//                    CMD_C_OperateCard OperateCard;
//                    memset(&OperateCard, 0, sizeof(CMD_C_OperateCard));
//                    if (strcmp(btnName.c_str(), "btn_hu") == 0) {
//                        OperateCard.cbOperateCode = WIK_H;
//                        OperateCard.cbOperateCard = static_cast<int >(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_gang") == 0) {
//                        OperateCard.cbOperateCode = WIK_G;
//                        OperateCard.cbOperateCard = static_cast<int>(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_peng") == 0) {
//                        OperateCard.cbOperateCode = WIK_P;
//                        OperateCard.cbOperateCard = static_cast<int>(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_guo") == 0) {
//                        OperateCard.cbOperateCode = WIK_NULL;
//                        OperateCard.cbOperateCard = static_cast<int>(iTag);
//                        m_pOperateNotifyGroup->removeAllChildren();
//                        m_pOperateNotifyGroup->setVisible(false);
//                    }
//                    OperateCard.cbOperateUser = m_MeChairID;
//                    m_GameEngine->onUserOperateCard(OperateCard);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    };
}