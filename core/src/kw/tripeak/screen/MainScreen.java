package kw.tripeak.screen;

//import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;
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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kw.gdx.BaseGame;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.audio.Asset;
import com.kw.gdx.cocosload.CocosResource;
import com.kw.gdx.log.NLog;
import com.kw.gdx.screen.BaseScreen;

import kw.tripeak.Ai.AIEngine;
import kw.tripeak.cmd.GameCmd;
import kw.tripeak.engine.GameEngine;
import kw.tripeak.group.ImageView;
import kw.tripeak.logic.GameLogic;
import kw.tripeak.play.IPlayer;
import kw.tripeak.play.RealPlayer;
import kw.tripeak.util.UIHelper;

@ScreenResource("cocos/GameLayer.json")
public class MainScreen extends BaseScreen {
    //游戏引擎
    GameEngine m_GameEngine;
    //游戏逻辑
    GameLogic m_GameLogic;
    //头像信息节点
    Group m_FaceFrame[] = new Group[4];
    //玩家牌的区域
    Group m_PlayerPanel[] = new Group[4];
    //全部玩家
    IPlayer m_Players[];
    //操作组
    Group m_pOperateNotifyGroup;
    //正在出牌的节点
    Group m_pOutCard;
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
        initUI();
    }

    private int cardPosition;

    private void initUI() {
        m_GameEngine.onGameStart();
        int[] m_cbRepertoryCard = m_GameEngine.m_cbRepertoryCard;
        int index = 0;

        for (int i = 0; i < m_cbCardIndex.length; i++) {
            int viewChairID = switchViewChairID(i);
//            int viewChairID = i;
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
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                        handCard_0.addActor(pCard);
                        System.out.println("------------------------");
                        x += 76;

                    }
                }
                if (viewChairID == 1) {
                    Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_1");
                    Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_1");

                    System.out.println(viewChairID);
                        if (handCard_0==null)continue;
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
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
                        handCard_0.addActor(pCard);
                        x += 40;
                    }
                }

            if (viewChairID == 2) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_2");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_2");

                System.out.println(viewChairID);
                if (handCard_0==null)continue;
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
                    handCard_0.addActor(pCard);
                    x += 76;
                }
            }
            if (viewChairID == 3) {
                Group handCard_0 = m_PlayerPanel[viewChairID].findActor("HandCard_3");
                Group comb_0 = m_PlayerPanel[viewChairID].findActor("comb_3");

                System.out.println(viewChairID);
                if (handCard_0==null)continue;
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
        return new Image(Asset.getAsset().getTexture(getHandCardImagePath(viewChairID,bCardDatum)));
    }


/**
 * 手上的牌路径
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
     * @param cbChairID
     * @return
     */
    public int switchViewChairID(int cbChairID) {
        m_CurPlayer = 4;
        return (cbChairID + m_CurPlayer - m_MeChairID) % m_CurPlayer;
    }

    private void initInstance() {
        m_GameEngine = GameEngine.GetGameEngine();  //构造游戏引擎
        m_GameLogic = new GameLogic();
        m_WeaveItemArray = new GameLogic.TagWeaveItem[GAME_PLAYER][MAX_WEAVE];
        m_iOutCardTimeOut = 30;
//        m_CurPlayer = 0;
//        m_MeChairID = 0;
//        m_bOperate = false;
//        m_bMove = false;
//        m_pOperateNotifyGroup = null;
//        m_pTextCardNum = null;
//        m_pOutCard = null;
//        //间隔多久创建一个电脑
//        for (int i = 4; i > 0; i--) {
//            aiEnterGame();
//        }
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
    }

    void sendCardTimerUpdate(float f) {
        m_iOutCardTimeOut = ((m_iOutCardTimeOut-- < 1) ? 0 : m_iOutCardTimeOut);
        ImageView pTimer1 = (ImageView) (UIHelper.seekNodeByName(m_Player, "Image_Timer_1"));
        ImageView pTimer0 = (ImageView) (UIHelper.seekNodeByName(m_Player, "Image_Timer_0"));
        int t = m_iOutCardTimeOut / 10;   //十位
        int g = m_iOutCardTimeOut % 10;   //各位
        pTimer1.loadTexture("res/GameLayer/timer_" + g + ".png");
        pTimer0.loadTexture("res/GameLayer/timer_" + t + ".png");

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
                        GameCmd.CMD_C_OutCard OutCard = new GameCmd().new CMD_C_OutCard();
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
//    void onOperateTouch(Ref *ref, ui::Widget::TouchEventType eventType){
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
//                        OperateCard.cbOperateCard = static_cast<uint8_t >(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_gang") == 0) {
//                        OperateCard.cbOperateCode = WIK_G;
//                        OperateCard.cbOperateCard = static_cast<uint8_t>(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_peng") == 0) {
//                        OperateCard.cbOperateCode = WIK_P;
//                        OperateCard.cbOperateCard = static_cast<uint8_t>(iTag);
//                    }
//                    if (strcmp(btnName.c_str(), "btn_guo") == 0) {
//                        OperateCard.cbOperateCode = WIK_NULL;
//                        OperateCard.cbOperateCard = static_cast<uint8_t>(iTag);
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
//    }//操作事件
//    public boolean onUserEnterEvent(IPlayer pIPlayer){
//        m_Players[m_CurPlayer++] = pIPlayer;
//        for (char i = 0; i < m_CurPlayer; i++) {
//            //显示头像
//            ImageView pImageHeader = (ImageView) UIHelper.seekNodeByName(m_FaceFrame[i], "Image_Header");  //头像
//            //得分
//            Label pTextScore = (Label) UIHelper.seekNodeByName(m_FaceFrame[i], "Text_Score");
//            pTextScore.setText("0");     //进来分数为0
//            int i1 = m_Players[i].getSex() == IPlayer.PlayerSex.FEMALE ? 0 : 1;
//            pImageHeader.loadTexture("res/GameLayer/im_defaulthead_"+ i1 +".png");    //设置头像
//        }
//        //显示头像
//        return true;
//    }            //玩家进入事件
//    public boolean onGameStartEvent(GameCmd.CMD_S_GameStart GameStart){
//        NLog.i("接收到游戏开始事件");   //接收到游戏开始事件
//        initGame();
//        //调整头像位置
////        m_FaceFrame[0].runAction(MoveTo::create(0.50f, Vec2(0080.00f, 250.00f)));
////        m_FaceFrame[1].runAction(MoveTo::create(0.50f, Vec2(0080.00f, 380.00f)));
////        m_FaceFrame[2].runAction(MoveTo::create(0.50f, Vec2(1060.00f, 640.00f)));
////        m_FaceFrame[3].runAction(MoveTo::create(0.50f, Vec2(1200.00f, 380.00f)));
//        //剩余的牌
//        m_cbLeftCardCount = GameStart.cbLeftCardCount;
//        m_cbBankerChair = GameStart.cbBankerUser;
//        m_GameLogic.switchToCardIndex(GameStart.cbCardData, MAX_COUNT - 1, m_cbCardIndex[m_MeChairID]);
//        //界面显示
//        m_pTextCardNum.setText(m_cbLeftCardCount);
//        showAndUpdateHandCard();
//        showAndUpdateDiscardCard();
//        return true;
//    }//游戏开始事件
//
    public boolean onSendCardEvent(GameCmd.CMD_S_SendCard sendCard){
        m_iOutCardTimeOut = 30; //重置计时器
        m_cbLeftCardCount--;
        if (sendCard.cbCurrentUser == m_MeChairID) {
            m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(sendCard.cbCardData)]++;
        }
        return false;
    }       //发牌事件
//
//    public boolean onOutCardEvent(GameCmd.CMD_S_OutCard OutCard){
//        if (OutCard.cbOutCardUser == m_MeChairID) {
//            m_cbCardIndex[m_MeChairID][m_GameLogic.switchToCardIndex(OutCard.cbOutCardData)]--;
//        }
//        m_cbDiscardCard[OutCard.cbOutCardUser][m_cbDiscardCount[OutCard.cbOutCardUser]++] = OutCard.cbOutCardData;
//        int cbViewID = switchViewChairID(OutCard.cbOutCardUser);
////        cocostudio::timeline::ActionTimeline *action = CSLoader::createTimeline("res/SignAnim.csb");
////        action.gotoFrameAndPlay(0, 10, true);
////        Node *pSignNode = CSLoader::createNode("res/SignAnim.csb");
////        pSignNode.setName("SignAnim");
////        std::vector<Node *> aChildren;
////        aChildren.clear();
////        UIHelper::getChildren(m_pLayer, "SignAnim", aChildren);
////        for (auto &subWidget : aChildren) {
////            subWidget.removeFromParent();
////        }
////        ui::ImageView *pRecvCard = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(m_PlayerPanel[cbViewID], utility::toString("RecvCard_", (int) cbViewID)));
////        if (pRecvCard) {
////            pRecvCard.setVisible(false);
////        }
//        switch (cbViewID) {
//            case 0: {
//                showAndUpdateHandCard();                     //更新手上的牌
//                Group pRecvCardList = (Group) UIHelper.seekNodeByName(m_PlayerPanel[cbViewID], "RecvHandCard_0");
//                pRecvCardList.clearChildren(); //移除出牌位置的牌
//                Group pDiscardCard0 = (Group) UIHelper.seekNodeByName(m_Player, "DiscardCard_0");//显示出的牌
//                pDiscardCard0.clearChildren();
//                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //12
//                float x = 0;
//                float y = 0;
//                for (int i = 0; i < bDiscardCount; i++) {
//                    int col = i % 12;
//                    int row = (i / 12);
//                    x = (col == 0) ? 0 : x;  //X复位
//                    y = row * 90;
//                    ImageView pCard0 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
//                    pCard0.setOrigin(0,0);
//                    pCard0.setPosition(new Vector2(x, y));
//                    pCard0.setZIndex(10 - row);
//                    x += 76;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
////                        pSignNode.set(0.5, 0.5);
////                        pSignNode.setPosition(Vec2(39, 110));
////                        pCard0.addChild(pSignNode);
////                        pSignNode.runAction(action);
//                    }
//                    pDiscardCard0.addActor(pCard0);
//                }
//                pDiscardCard0.setScale(0.7F, 0.7F);
//                //听牌判断
//                int cbWeaveItemCount = m_cbWeaveItemCount[OutCard.cbOutCardUser];
//                GameLogic.TagWeaveItem []pTagWeaveItem = m_WeaveItemArray[OutCard.cbOutCardUser];
//                int cbCardIndex[] = m_cbCardIndex[OutCard.cbOutCardUser];
//                showTingResult(cbCardIndex, pTagWeaveItem, cbWeaveItemCount);
//                break;
//            }
//            case 1: {
//                //显示出的牌
//                Group pDiscardCard1 = (Group) UIHelper.seekNodeByName(m_Player, "DiscardCard_1");
//                pDiscardCard1.clearChildren();
//                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //
//                float x = 0;
//                float y = 0;
//                for (int i = 0; i < bDiscardCount; i++) {
//                    int col = (i % 11);
//                    int row = (i / 11);
//                    y = (col == 0) ? 0 : y;  //X复位
//                    x = 116 * row;
//                    ImageView pCard1 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
//                    pCard1.setOrigin(0, 0);
//                    pCard1.setPosition(x, 740 - y);
//                    pCard1.setZIndex(col);
//                    y += 74;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
////                        pSignNode.setAnchorPoint(Vec2(0.5, 0.5));
////                        pSignNode.setPosition(Vec2(81, 110));
////                        pCard1.addChild(pSignNode);
////                        pSignNode.runAction(action);
//                    }
//                    pDiscardCard1.addActor(pCard1);
//                }
//                pDiscardCard1.setScale(0.5F, 0.5F);
//                break;
//            }
//            case 2: {
//                Group pDiscardCard2 = (Group) UIHelper.seekNodeByName(m_Player, "DiscardCard_2");
//                pDiscardCard2.clearChildren();
//                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //12
//                float x = 0;
//                float y = 0;
//                for (int i = 0; i < bDiscardCount; i++) {
//                    int col = i % 12;
//                    int row = i / 12;
//                    x = (col == 0) ? 0 : x;  //X复位
//                    y = 90 - row * 90;
//                    ImageView pCard2 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
//                    pCard2.setOrigin(0, 0);
//                    pCard2.setPosition(x, y);
//                    pCard2.setZIndex(row);
//                    x += 76;
//                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
////                        pSignNode.setAnchorPoint(Vec2(0.5, 0.5));
////                        pSignNode.setPosition(Vec2(39, 59));
////                        pCard2.addChild(pSignNode);
////                        pSignNode.runAction(action);
//                    }
//                    pDiscardCard2.addActor(pCard2);
//                }
//                pDiscardCard2.setScale(0.7F, 0.7F);
//
//                break;
//            }
//            case 3: {
//                //显示出的牌
//                Group pDiscardCard3 = (Group) UIHelper.seekNodeByName(m_Player, "DiscardCard_3");
//                pDiscardCard3.clearChildren();
//                int bDiscardCount = m_cbDiscardCount[OutCard.cbOutCardUser]; //
//                float x = 0;
//                float y = 0;
//                for (int i = 0; i < bDiscardCount; i++) {
//                    int col = (i % 11);
//                    int row = (i / 11);
//                    y = (col == 0) ? 0 : y;  //X复位
//                    x = 240 - (116 * row);
//                    ImageView pCard3 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[OutCard.cbOutCardUser][i]);
//                    pCard3.setOrigin(0, 0);
//                    pCard3.setPosition(x, y);
//                    pCard3.setZIndex(20 - col);
//                    y += 74;
////                    if (i == bDiscardCount - 1) {   //最后一张，添加动画效果
////                        pSignNode.setAnchorPoint(Vec2(0.5, 0.5));
////                        pSignNode.setPosition(Vec2(39, 110));
////                        pCard3.addChild(pSignNode);
////                        pSignNode.runAction(action);
////                    }
//                    pDiscardCard3.addActor(pCard3);
//                }
//                pDiscardCard3.setScale(0.5F, 0.5F);
//                break;
//            }
//            default:
//                break;
//        }
//        for (int j = 0; j < GAME_PLAYER; ++j) {   //发牌后隐藏导航
//            ImageView pHighlight = (ImageView) UIHelper.seekNodeByName(m_Player, "Image_Wheel_"+ j);
//            pHighlight.setVisible(false);
//        }
////        playSound(::toString("raw/Mahjong/", (IPlayer::FEMALE == m_Players[OutCard.cbOutCardUser].getSex() ? "female" : "male"), "/mjt", utility::toString((
////                (OutCard.cbOutCardData & MASK_COLOR)
////                        >> 4) + 1, "_", OutCard.cbOutCardData & MASK_VALUE), ".mp3"));    //播放牌的声音
//        return true;
//    }          //出牌事件
//    public boolean onOperateNotifyEvent(GameCmd.CMD_S_OperateNotify OperateNotify){
//        return showOperateNotify(OperateNotify);
//    }   //操作通知事件
//    public boolean onOperateResultEvent(GameCmd.CMD_S_OperateResult OperateResult){
//        return false;
//    }   //操作结果事件
//    public boolean onGameEndEvent(GameCmd.CMD_S_GameEnd GameEnd){
//        return false;
//    }                     //移除特效
//    public boolean showAndUpdateHandCard(){
//        for (uint8_t i = 0; i < m_CurPlayer; i++) {
//            uint8_t viewChairID = switchViewChairID(i);
//            uint8_t bCardData[MAX_COUNT];  //手上的牌
//            memset(bCardData, 0, sizeof(bCardData));
//            m_GameLogic->switchToCardData(m_cbCardIndex[i], bCardData, MAX_COUNT);
//            uint8_t cbWeaveItemCount = m_cbWeaveItemCount[i]; //组合数量
//            tagWeaveItem WeaveItemArray[MAX_WEAVE];         //组合
//            memcpy(WeaveItemArray, m_WeaveItemArray[i], sizeof(WeaveItemArray));
//            switch (viewChairID) {
//                case 0: {
//                    ui::Layout *pHandCard0 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "HandCard_0"));
//                    ui::Layout *pComb0 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "comb_0"));  //组合
//                    pHandCard0->removeAllChildren();
//                    pComb0->removeAllChildren();
//                    int x = 0;  //宽起始位置
//                    for (uint8_t j = 0; j < cbWeaveItemCount; j++) {
//                        tagWeaveItem weaveItem = WeaveItemArray[j];
//                        Node *pWeaveNode = NULL;
//                        if (weaveItem.cbWeaveKind == WIK_G) {
//                            pWeaveNode = CSLoader::createNode("res/Gang0.csb");
//                        }
//                        if (weaveItem.cbWeaveKind == WIK_P) {
//                            pWeaveNode = CSLoader::createNode("res/Peng0.csb");
//                        }
//                        assert(pWeaveNode != NULL);
//                        pWeaveNode->setPosition(Vec2(x, 0));
//                    const std::string &strImagePath = getDiscardCardImagePath(0, weaveItem.cbCenterCard);
//                    const std::string &strBackImagePath = getBackCardImagePath(0, weaveItem.cbCenterCard);
//                        ui::ImageView *pImageRight = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_right"));
//                        ui::ImageView *pImageLeft = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_left"));
//                        ui::ImageView *pImageCenter = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_center"));
//                        pImageRight->loadTexture(strImagePath);
//                        pImageCenter->loadTexture(strImagePath);
//                        pImageLeft->loadTexture(strImagePath);
//                        switch (switchViewChairID(weaveItem.cbProvideUser)) {
//                            case 0: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageCenter->loadTexture(strImagePath);
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
//                        pComb0->addChild(pWeaveNode);
//                        x += 228;
//                    }
//                    for (uint8_t j = 0; j < MAX_COUNT - 1 - (3 * cbWeaveItemCount); j++) {
//                        ui::ImageView *pCard = createHandCardImageView(viewChairID, bCardData[j]);
//                        pCard->setAnchorPoint(Vec2(0, 0));
//                        pCard->setPosition(Vec2(x, 0));
//                        pCard->setTouchEnabled(true);
//                        pCard->setTag(bCardData[j]);
//                        pCard->setName(utility::toString("bt_card_", (int) bCardData[j]));
//                        pCard->addTouchEventListener(CC_CALLBACK_2(GameLayer::onCardTouch, this));
//                        pHandCard0->addChild(pCard);
//                        x += 76;
//                    }
//                    break;
//                }
//                case 1: {
//                    ui::Layout *pHandCard1 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "HandCard_1"));
//                    ui::Layout *pComb1 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "comb_1"));  //组合
//                    pHandCard1->removeAllChildren();
//                    pComb1->removeAllChildren();
//                    int y = 800;  //高起始位置
//                    for (uint8_t j = 0; j < cbWeaveItemCount; j++) {
//                        y -= 160;
//                        tagWeaveItem weaveItem = WeaveItemArray[j];
//                        Node *pWeaveNode = NULL;
//                        if (weaveItem.cbWeaveKind == WIK_G) {
//                            pWeaveNode = CSLoader::createNode("res/Gang1.csb");
//                        }
//                        if (weaveItem.cbWeaveKind == WIK_P) {
//                            pWeaveNode = CSLoader::createNode("res/Peng1.csb");
//                        }
//                        assert(pWeaveNode != NULL);
//                        pWeaveNode->setPosition(Vec2(0, y + 20));
//                    const std::string &strImagePath = getDiscardCardImagePath(1, weaveItem.cbCenterCard);
//                    const std::string &strBackImagePath = getBackCardImagePath(1, weaveItem.cbCenterCard);
//                        ui::ImageView *pImageRight = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_right"));
//                        ui::ImageView *pImageLeft = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_left"));
//                        ui::ImageView *pImageCenter = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_center"));
//                        pImageRight->loadTexture(strImagePath);
//                        pImageCenter->loadTexture(strImagePath);
//                        pImageLeft->loadTexture(strImagePath);
//                        switch (switchViewChairID(weaveItem.cbProvideUser)) {
//                            case 0: {
//                                pImageRight->loadTexture(strBackImagePath);
//                                break;
//                            }
//                            case 1: {
//                                if (weaveItem.cbPublicCard == FALSE) {    //暗杠
//                                    pImageRight->loadTexture(strBackImagePath);
//                                    pImageCenter->loadTexture(strBackImagePath);
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
//                        pComb1->addChild(pWeaveNode);
//                    }
//                    for (uint8_t j = 0; j < MAX_COUNT - 1 - (3 * cbWeaveItemCount); j++) {
//                        y -= 60;
//                        ui::ImageView *pCard = createHandCardImageView(viewChairID, bCardData[j]);
//                        pCard->setAnchorPoint(Vec2(0, 0));
//                        pCard->setPosition(Vec2(0, y - 20));
//                        pCard->setLocalZOrder(j);
//                        pHandCard1->addChild(pCard);
//                    }
//                    break;
//                }
//                case 2: {
//                    ui::Layout *pHandCard2 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "HandCard_2"));
//                    ui::Layout *pComb2 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "comb_2"));  //组合
//                    pHandCard2->removeAllChildren();
//                    pComb2->removeAllChildren();
//                    int x = 1027;  //宽起始位置
//                    for (uint8_t j = 0; j < cbWeaveItemCount; j++) {
//                        x -= 228;
//                        tagWeaveItem weaveItem = WeaveItemArray[j];
//                        Node *pWeaveNode = NULL;
//                        if (weaveItem.cbWeaveKind == WIK_G) {
//                            pWeaveNode = CSLoader::createNode("res/Gang0.csb");
//                        }
//                        if (weaveItem.cbWeaveKind == WIK_P) {
//                            pWeaveNode = CSLoader::createNode("res/Peng0.csb");
//                        }
//                        assert(pWeaveNode != NULL);
//                        pWeaveNode->setPosition(Vec2(x + 23, 0));
//                    const std::string &strImagePath = getDiscardCardImagePath(2, weaveItem.cbCenterCard);
//                    const std::string &strBackImagePath = getBackCardImagePath(2, weaveItem.cbCenterCard);
//                        ui::ImageView *pImageRight = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_right"));
//                        ui::ImageView *pImageLeft = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_left"));
//                        ui::ImageView *pImageCenter = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_center"));
//                        pImageRight->loadTexture(strImagePath);
//                        pImageCenter->loadTexture(strImagePath);
//                        pImageLeft->loadTexture(strImagePath);
//                        switch (switchViewChairID(weaveItem.cbProvideUser)) {
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
//                                    pImageCenter->loadTexture(strBackImagePath);
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
//                        pComb2->addChild(pWeaveNode);
//                    }
//                    for (uint8_t j = 0; j < MAX_COUNT - 1 - (3 * cbWeaveItemCount); j++) {
//                        x -= 76;
//                        ui::ImageView *pCard = createHandCardImageView(viewChairID, bCardData[j]);
//                        pCard->setAnchorPoint(Vec2(0, 0));
//                        pCard->setPosition(Vec2(x, 0));
//                        pHandCard2->addChild(pCard);
//                    }
//                    break;
//                }
//                case 3: {
//                    ui::Layout *pHandCard3 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "HandCard_3"));
//                    ui::Layout *pComb3 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_PlayerPanel[viewChairID], "comb_3"));  //组合
//                    pHandCard3->removeAllChildren();
//                    pComb3->removeAllChildren();
//                    int y = 0;  //高起始位置
//                    for (uint8_t j = 0; j < cbWeaveItemCount; j++) {
//                        tagWeaveItem weaveItem = WeaveItemArray[j];
//                        Node *pWeaveNode = NULL;
//                        if (weaveItem.cbWeaveKind == WIK_G) {
//                            pWeaveNode = CSLoader::createNode("res/Gang1.csb");
//                        }
//                        if (weaveItem.cbWeaveKind == WIK_P) {
//                            pWeaveNode = CSLoader::createNode("res/Peng1.csb");
//                        }
//                        assert(pWeaveNode != NULL);
//                        pWeaveNode->setPosition(Vec2(0, y - 10));
//                    const std::string &strImagePath = getDiscardCardImagePath(3, weaveItem.cbCenterCard);
//                    const std::string &strBackImagePath = getBackCardImagePath(3, weaveItem.cbCenterCard);
//                        ui::ImageView *pImageRight = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_right"));
//                        ui::ImageView *pImageLeft = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_left"));
//                        ui::ImageView *pImageCenter = dynamic_cast<ui::ImageView *>(UIHelper::seekNodeByName(pWeaveNode, "Image_center"));
//                        pImageRight->loadTexture(strImagePath);
//                        pImageCenter->loadTexture(strImagePath);
//                        pImageLeft->loadTexture(strImagePath);
//                        switch (switchViewChairID(weaveItem.cbProvideUser)) {
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
//                                    pImageCenter->loadTexture(strBackImagePath);
//                                    pImageLeft->loadTexture(strBackImagePath);
//                                }
//                                break;
//                            }
//                            default:
//                                break;
//                        }
//                        pComb3->addChild(pWeaveNode);
//                        y += 160;
//                    }
//                    for (uint8_t j = 0; j < MAX_COUNT - 1 - (3 * cbWeaveItemCount); j++) {
//                        ui::ImageView *pCard = createHandCardImageView(viewChairID, bCardData[j]);
//                        pCard->setAnchorPoint(Vec2(0, 0));
//                        pCard->setPosition(Vec2(0, y - 20));
//                        pCard->setLocalZOrder(MAX_COUNT - j);
//                        pHandCard3->addChild(pCard);
//                        y += 60;
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//        return true;
//    }                                     //显示手上的牌
    boolean showAndUpdateDiscardCard(){
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
                        pCard0->setAnchorPoint(Vec2(0, 0));
                        pCard0->setPosition(Vec2(x, y));
                        pCard0->setLocalZOrder(10 - row);
                        x += 76;
                        pDiscardCard0->addChild(pCard0);
                    }
                    pDiscardCard0->setScale(0.7, 0.7);
                    break;
                }
                case 1: {
                    //显示出的牌
                    ui::Layout *pDiscardCard1 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_1"));
                    pDiscardCard1->removeAllChildren();
                    uint8_t bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (uint8_t i = 0; i < bDiscardCount; i++) {
                        uint8_t col = static_cast<uint8_t>(i % 11);
                        uint8_t row = static_cast<uint8_t>(i / 11);
                        y = (col == 0) ? 0 : y;  //X复位
                        x = 116 * row;
                        ui::ImageView *pCard1 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard1->setAnchorPoint(Vec2(0, 0));
                        pCard1->setPosition(Vec2(x, 740 - y));
                        pCard1->setLocalZOrder(col);
                        y += 74;
                        pDiscardCard1->addChild(pCard1);
                    }
                    pDiscardCard1->setScale(0.5, 0.5);
                    break;
                }
                case 2: {
                    ui::Layout *pDiscardCard2 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_2"));
                    pDiscardCard2->removeAllChildren();
                    uint8_t bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (uint8_t i = 0; i < bDiscardCount; i++) {
                        uint8_t col = static_cast<uint8_t>(i % 12);
                        uint8_t row = static_cast<uint8_t>(i / 12);
                        x = (col == 0) ? 0 : x;  //X复位
                        y = 90 - row * 90;
                        ui::ImageView *pCard2 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard2->setAnchorPoint(Vec2(0, 0));
                        pCard2->setPosition(Vec2(x, y));
                        pCard2->setLocalZOrder(row);
                        x += 76;
                        pDiscardCard2->addChild(pCard2);
                    }
                    pDiscardCard2->setScale(0.7, 0.7);

                    break;
                }
                case 3: {
                    //显示出的牌
                    ui::Layout *pDiscardCard3 = dynamic_cast<ui::Layout *>(UIHelper::seekNodeByName(m_pLayer, "DiscardCard_3"));
                    pDiscardCard3->removeAllChildren();
                    uint8_t bDiscardCount = m_cbDiscardCount[cbChairID];
                    float x = 0;
                    float y = 0;
                    for (uint8_t i = 0; i < bDiscardCount; i++) {
                        uint8_t col = static_cast<uint8_t>(i % 11);
                        uint8_t row = static_cast<uint8_t>(i / 11);
                        y = (col == 0) ? 0 : y;  //X复位
                        x = 240 - (116 * row);
                        ui::ImageView *pCard3 = createDiscardCardImageView(cbViewID, m_cbDiscardCard[cbChairID][i]);
                        pCard3->setAnchorPoint(Vec2(0, 0));
                        pCard3->setPosition(Vec2(x, y));
                        pCard3->setLocalZOrder(20 - col);
                        y += 74;
                        pDiscardCard3->addChild(pCard3);
                    }
                    pDiscardCard3->setScale(0.5, 0.5);
                    break;
                }
                default:
                    break;
            }
        }
        return true;
    }

    private Image createDiscardCardImageView(int cbViewID, int cbData) {
        getDiscardCardImagePath(cbViewID, cbData));
    }
//    //显示桌上的牌
//    boolean showSendCard(GameCmd.CMD_S_SendCard SendCard){
//        return false;
//    }                                     //发牌显示
//    boolean showOperateNotify(GameCmd.CMD_S_OperateNotify OperateNotify){
//        if (OperateNotify.cbActionMask == WIK_NULL) {
//            return true; //无动作
//        }
//        m_pOperateNotifyGroup.clearChildren();
//        m_pOperateNotifyGroup.setVisible(true);
//        float x = 500.0f;
//        float y = 65.0f;
//        if ((OperateNotify.cbActionMask & WIK_H) != 0) {
//            Group pHuNode = CocosResource.loadFile("res/BtnHu.json");
//            pHuNode.setPosition(x, y);
//            ImageView pHuBtn = (ImageView) pHuNode.getChildren().get(0);
//            pHuBtn.setTag(OperateNotify.cbActionCard);
////            pHuBtn.addListener(onOperateTouch, this));
//            m_pOperateNotifyGroup.addActor(pHuNode);
//            x -= 160;
//        }
//        if ((OperateNotify.cbActionMask & WIK_G) != 0) {
//            for (int i = 0; i < OperateNotify.cbGangCount; i++) {
//                Group pGangNode = CocosResource.loadFile("res/BtnGang.csb");
//                pGangNode.setPosition(x, y + (i * 120));
//                Group pGangBtn = (Group) pGangNode.getChildren().get(0);
//                ImageView pImgGangCard = (ImageView) UIHelper.seekNodeByName(pGangBtn,"ImgGangCard");
//                pImgGangCard.loadTexture(getDiscardCardImagePath(0, OperateNotify.cbGangCard[i]));
//                pImgGangCard.setVisible(OperateNotify.cbGangCount > 1);
//                pGangBtn.setName(OperateNotify.cbGangCard[i]+"");
////                pGangBtn.addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
//                m_pOperateNotifyGroup.addActor(pGangNode);
//            }
//            x -= 160;
//        }
//        if ((OperateNotify.cbActionMask & WIK_P) != 0) {
//            Group pPengNode = CocosResource.loadFile("res/BtnPeng.csb");
//            pPengNode.setPosition(x, y);
//            Group pPengBtn = (Group) pPengNode.getChildren().get(0);
////            pPengBtn->setTag(OperateNotify.cbActionCard);
////            pPengBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
////            m_pOperateNotifyGroup->addChild(pPengNode);
//            x -= 160;
//        }
////        Node *pGuoNode = CSLoader::createNode("res/BtnGuo.csb");
////        pGuoNode->setPosition(Vec2(x, y));
////        ui::Button *pGuoBtn = dynamic_cast<ui::Button *>(pGuoNode->getChildren().at(0));
////        pGuoBtn->setTag(OperateNotify.cbActionCard);
////        pGuoBtn->addTouchEventListener(CC_CALLBACK_2(GameLayer::onOperateTouch, this));
////        m_pOperateNotifyGroup->addChild(pGuoNode);
//        return true;
//    }                      //显示操作通知
//    boolean showAndPlayOperateEffect(int cbViewID,int cbOperateCode, boolean bZm){
//        float x = 0;
//        float y = 0;
//        switch (cbViewID) {
//            case 0: {   //自己操作反馈
//                x = 640.0f;
//                y = 200.0f;
//                break;
//            }
//            case 1: {
//                x = 440.0f;
//                y = 360.0f;
//                break;
//            }
//            case 2: {
//                x = 640.0f;
//                y = 520.0f;
//                break;
//            }
//            case 3: {
//                x = 840.0f;
//                y = 360.0f;
//                break;
//            }
//            default:
//                break;
//        }
//        std::string strEffect = "";
//        switch (cbOperateCode) {
//            case WIK_NULL: {
//                break;
//            }
//            case WIK_P: {
//                strEffect = "res/EffectPeng.csb";
//                break;
//            }
//            case WIK_G: {
//                strEffect = "res/EffectGang.csb";
//                break;
//            }
//            case WIK_H: {
//                strEffect = bZm ? "res/EffectZm.csb" : "res/EffectHu.csb";
//                break;
//            }
//            default:
//                break;
//        }
//        if (strEffect != "") {
//            std::string strNodeName = "EffectNode";
//            Node *pEffectNode = CSLoader::createNode(strEffect);
//            pEffectNode->setPosition(Vec2(x, y));
//            cocostudio::timeline::ActionTimeline *action = CSLoader::createTimeline(strEffect);
//            action->gotoFrameAndPlay(0, false);
//            pEffectNode->setName(strNodeName);
//            m_pLayer->addChild(pEffectNode);
//            if (cbOperateCode != WIK_H) {    //胡牌不自动删除动画
//                action->setLastFrameCallFunc(CC_CALLBACK_0(GameLayer::removeEffectNode, this, strNodeName));
//            }
//            pEffectNode->runAction(action);
//        }
//        return true;
//    }//播放特效
//    boolean showTingResult(int cbCardIndex[], GameLogic.TagWeaveItem WeaveItem[], int cbWeaveCount){
//        return false;
//
//    }   //显示听牌的结果
//    boolean showAndUpdateUserScore(int lGameScoreTable[]){
//        return false;
//    }             //更新分数
//    ImageView createHandCardImageView(int cbViewID, int cbData){
//        return ui::ImageView::create(getHandCardImagePath(cbViewID, cbData));
//
//    }   //创建牌的ImageView
//    ImageView createDiscardCardImageView(int cbViewID, int cbData){
//        return ui::ImageView::create(getDiscardCardImagePath(cbViewID, cbData));
//    }//创建出牌的ImageView
//    String getDiscardCardImagePath(int cbViewID, int cbData){
//        std::string strImagePath = "";
//        switch (cbViewID) {
//            case 0: {
//                strImagePath = "res/GameLayer/Mahjong/2/mingmah_" + utility::toString(((cbData & MASK_COLOR)
//                        >> 4) + 1, cbData & MASK_VALUE) + ".png";
//                break;
//            }
//            case 1: {
//                strImagePath = "res/GameLayer/Mahjong/3/mingmah_" + utility::toString(((cbData & MASK_COLOR)
//                        >> 4) + 1, cbData & MASK_VALUE) + ".png";
//                break;
//            }
//            case 2: {
//                strImagePath = "res/GameLayer/Mahjong/2/mingmah_" + utility::toString(((cbData & MASK_COLOR)
//                        >> 4) + 1, cbData & MASK_VALUE) + ".png";
//                break;
//            }
//            case 3: {
//                strImagePath = "res/GameLayer/Mahjong/1/mingmah_" + utility::toString(((cbData & MASK_COLOR)
//                        >> 4) + 1, cbData & MASK_VALUE) + ".png";
//                break;
//            }
//            default:
//                break;
//        }
//        return strImagePath;
//    }
//    //获取牌的图片路径
//    String getBackCardImagePath(int cbViewID, int cbData){
//        std::string strImagePath = "";
//        switch (cbViewID) {
//            case 0: {
//                strImagePath = "res/GameLayer/Mahjong/2/mingmah_00.png";
//                break;
//            }
//            case 1: {
//                strImagePath = "res/GameLayer/Mahjong/1/mingmah_00.png";
//                break;
//            }
//            case 2: {
//                strImagePath = "res/GameLayer/Mahjong/2/mingmah_00.png";
//                break;
//            }
//            case 3: {
//                strImagePath = "res/GameLayer/Mahjong/1/mingmah_00.png";
//                break;
//            }
//            default:
//                break;
//        }
//        return strImagePath;
//    }      //获取牌背面的路径
//    String getHandCardImagePath(int cbViewID, int cbData){
//        std::string strImagePath = "";
//        switch (cbViewID) {
//            case 0: {
//                strImagePath = "res/GameLayer/Mahjong/2/handmah_" + utility::toString(((cbData & MASK_COLOR)
//                        >> 4) + 1, cbData & MASK_VALUE) + ".png";
//                break;
//            }
//            case 1: {
//                strImagePath = "res/GameLayer/Mahjong/hand_left.png";
//                break;
//            }
//            case 2: {
//                strImagePath = "res/GameLayer/Mahjong/hand_top.png";
//                break;
//            }
//            case 3: {
//                strImagePath = "res/GameLayer/Mahjong/hand_right.png";
//                break;
//            }
//            default:
//                break;
//        }
//        return strImagePath;
//    }         //获取手上的牌图片路径
//    int switchViewChairID(int cbChairID){
//        return 0;
//    }   //椅子位置切换成视图位置
//    int switchChairViewID(int cbViewID){
//        return 0;
//    }    //视图位置切换成椅子位置
//    void playSound(String file){}               //播放声音
//    void removeEffectNode(String strNodeName){
//        std::vector<Node *> aChildren;
//        aChildren.clear();
//        UIHelper::getChildren(m_pLayer, strNodeName, aChildren);
//        for (auto &subChild : aChildren) {
//            subChild->removeFromParent();
//        }
//    } //移除特效
//    void exitGame(Group pNode){}                     //退出游戏


}