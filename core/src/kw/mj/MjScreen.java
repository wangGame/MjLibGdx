package kw.mj;

import static kw.tripeak.constant.Constant.GAME_PLAYER;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kw.gdx.BaseGame;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;

import java.util.Comparator;

import kw.bean.UserTask;
import kw.mj.card.EveryCard;
import kw.mj.data.GameData;
import kw.mj.play.AIplayer;
import kw.mj.play.AbstarctPlay;
import kw.mj.play.NomalPlayer;

/**
 * 游戏界面  和  游戏逻辑
 */
@ScreenResource("cocos/GameLayer.json")
public class MjScreen extends BaseScreen {
    private int zhuangjia;
    private GameData data;
    private Group playerPanel[] = new Group[4];
    private Group faceFrame[] = new Group[4];
//    private Group m_pOperateNotifyGroup;//碰  胡 等操作
//    private Label m_pTextCardNum;
    private AbstarctPlay[] abstarctPlays = new AbstarctPlay[4];
    private int currentPlayer;
    private int playCardsPos[] = new int[4];
    private int gameSendCard;
    private GameLoagic loagic;
    private UserTask task;

    public MjScreen(BaseGame game) {
        super(game);
        task = new UserTask();
    }

    @Override
    public void initView() {
        super.initView();
        initInstance();
        createPeople();
        initData();
        showHandPai();
        //发牌然后出牌
        peakCard();
    }

    private void initInstance() {
        //逻辑
        loagic = new GameLoagic();
        //初始化头像节点数组
        for (int i = 0; i < GAME_PLAYER; i++) {
            //四个人的脸
            faceFrame[i] = rootView.findActor("face_frame_" + i);
            //四个人的牌放的位置
            playerPanel[i] = rootView.findActor("PlayerPanel_" + i);
        }
//        //操作节点 （用户）
//        m_pOperateNotifyGroup = rootView.findActor("OperateNotifyGroup");
//        //展示还剩多少牌了
//        m_pTextCardNum = rootView.findActor("Text_LeftCard");   //操作节点
    }

    private void peakCard() {
        //第一个用户  从这里开始，需要系统发牌
        task.setCurrentUser(currentPlayer);
        abstarctPlays[switchViewChairID(currentPlayer++)].peakCard();
    }

    //发牌和打牌
    private void reciveCard(int chair) {
        //发牌
        gameSendCard = data.getNextCard();
        task.setCurrentCardData(gameSendCard);
        int viewChairID = chair;
        Group playerPanel = rootView.findActor("PlayerPanel_"+viewChairID);
        if(viewChairID == 0) {
            Group recvHandCard = playerPanel.findActor("RecvHandCard_" + viewChairID);
            String handCardImagePath = getHandCardImagePath(viewChairID, gameSendCard);
            EveryCard everyCard = new EveryCard(handCardImagePath);
            recvHandCard.addActor(everyCard);
            recvHandCard.setVisible(true);
            everyCard.setData(gameSendCard);
            everyCard.setY(100);
            abstarctPlays[chair].setEvery(everyCard);
        }else {
            Image image = playerPanel.findActor("RecvCard_" + viewChairID);
            image.setVisible(true);
            abstarctPlays[chair].setData(gameSendCard);
        }
    }

    private void createPeople() {
        abstarctPlays[0] = new NomalPlayer(new AbstarctPlay.IPlayCallback() {
            @Override
            public void call(int chair,Actor actor) {
                reciveCard(chair);
            }
        }, new AbstarctPlay.IPlayCallback() {
            @Override
            public void call(int chair,Actor actor) {
                sendCard(chair,actor);
            }
        });
        abstarctPlays[0].setChair(0);
        stage.addActor(abstarctPlays[0]);
        for (int i = 1; i < 4; i++) {
            abstarctPlays[i] = new AIplayer(new AbstarctPlay.IPlayCallback() {
                @Override
                public void call(int chair,Actor actor) {
                    reciveCard(chair);
                }
            }, new AbstarctPlay.IPlayCallback() {
                @Override
                public void call(int chair,Actor actor) {
                    sendCard(chair,actor);
                }
            });
            abstarctPlays[i].setChair(i);
            stage.addActor(abstarctPlays[i]);
        }
    }

    private void sendCard(int chair,Actor actor) {
        int sendCardsData = gameSendCard;
        if (actor instanceof EveryCard){
            sendCardsData = ((EveryCard) actor).getData();
        }
        Group playerPanel = findActor("PlayerPanel_"+chair);
        if (chair == 0) {
            playerPanel.findActor("RecvHandCard_" + chair).setVisible(false);
        }else {
            playerPanel.findActor("RecvCard_" + chair).setVisible(false);
        }
        Group discardCard = rootView.findActor("DiscardCard_" + chair);
        String handCardImagePath = getHandCardImagePath1(chair, sendCardsData);
        EveryCard everyCard = new EveryCard(handCardImagePath);
        if (chair==1||chair==3){
            everyCard.setY(playCardsPos[chair]*60);
        }else {
            everyCard.setX(playCardsPos[chair] * 70);
        }
        discardCard.addActor(everyCard);
        playCardsPos[chair]++;
        Group handCard_0 = playerPanel.findActor("HandCard_" + chair);
        if (gameSendCard != sendCardsData) {
            handCardImagePath = getHandCardImagePath(chair, gameSendCard);
            everyCard = new EveryCard(handCardImagePath);
            everyCard.setData(gameSendCard);
            handCard_0.addActor(everyCard);
            handCard_0.removeActor(actor);
        }
        SnapshotArray<Actor> children = handCard_0.getChildren();
        sortCard(chair);
        fuwei();
        for (Actor child : children) {
            buju(chair,child);
        }

        boolean flag = false;
        int targetPlay = 0;

        //判断其他三个
        for (int i = 1; i < 4; i++) {
            if (i!=chair){
                System.out.println(i+"-- user --"+chair+"--- card ---- "+sendCardsData);
                flag = panduan(sendCardsData,abstarctPlays[i].getCards());
                if (flag){
                    targetPlay = i;
                    break;
                }
            }
        }
        if (flag){
            actions(sendCardsData,targetPlay);
        }else {
            peakCard();
        }
    }

    public void actions(int cardsData, int targetPlayer){
        //发牌和打牌
            //发牌
            gameSendCard = cardsData;
            int viewChairID = targetPlayer;
            Group playerPanel = rootView.findActor("PlayerPanel_"+viewChairID);
            if(viewChairID == 0) {
                Group recvHandCard = playerPanel.findActor("RecvHandCard_" + viewChairID);
                String handCardImagePath = getHandCardImagePath(viewChairID, gameSendCard);
                EveryCard everyCard = new EveryCard(handCardImagePath);
                recvHandCard.addActor(everyCard);
                recvHandCard.setVisible(true);
                everyCard.setData(gameSendCard);
                everyCard.setY(100);
                abstarctPlays[targetPlayer].setEvery(everyCard);
            }else {
                Image image = playerPanel.findActor("RecvCard_" + viewChairID);
                image.setVisible(true);
//                abstarctPlays[targetPlayer].setData(gameSendCard);
                abstarctPlays[targetPlayer].setData(gameSendCard);
            }
    }

    public boolean panduan(int target,int[] cards){
        boolean flag = false;
        if (loagic.estimateGangCard(cards,target)) {
            System.out.println("gang--------"+target);
            flag = true;
        }
        if (loagic.estimatePengCard(cards,target)) {
            System.out.println("peng--------");
            flag  =true;
        }
        if (loagic.estimateChiCard(cards,target)) {
            System.out.println("chi--------");
            flag = true;
        }
        return flag;
    }

    public void initData(){
        data = new GameData();
        zhuangjia = data.touShaizi();
        currentPlayer = zhuangjia;
        data.shuffle();
        data.zhuaPai();
    }

    public void fuwei(){
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }

    private void showHandPai() {
        fuwei();
        int[][] userCard = data.getUserCard();
        for (int i = 0; i < 4; i++) {
            int viewChairID = switchViewChairID(i);
            int[] ints = userCard[viewChairID];
            for (int anInt : ints) {
                showCard(viewChairID,anInt);
            }
        }
        for (int i = 0; i < 4; i++) {
            int viewChairID = switchViewChairID(i);
            SnapshotArray<Actor> snapshotArray = sortCard(viewChairID);
            playerPanel[viewChairID].findActor("HandCard_"+viewChairID);
            fuwei();
            for (Actor o : snapshotArray) {
                buju(viewChairID,o);
            }
        }


        for (int i = 0; i < 4; i++) {
            abstarctPlays[i].setCard(playerPanel[i]);
        }
    }

    private SnapshotArray sortCard(int viewChairId) {
        Group handCard_0 = playerPanel[viewChairId].findActor("HandCard_"+viewChairId);
        SnapshotArray<Actor> children = handCard_0.getChildren();
        children.sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor o1, Actor o2) {
                if (o1 instanceof EveryCard && o2 instanceof EveryCard){
                    return ((EveryCard)(o1)).getData() - ((EveryCard)o2).getData();
                }else {
                    return 0;
                }
            }
        });
        return new SnapshotArray(children);
    }

    private float x1 = 0;
    private float y1 = 0;
    private float x2 = 0;
    private float y2 = 0;
    private void showCard(int viewChairId,int ints){
        String handCardImagePath = getHandCardImagePath(viewChairId, ints);
        EveryCard everyCard = null;
        Group handCard_0 = playerPanel[viewChairId].findActor("HandCard_"+viewChairId);
        everyCard = new EveryCard(handCardImagePath);
        everyCard.setData(ints);
        buju(viewChairId,everyCard);
        handCard_0.addActor(everyCard);
    }

    public void buju(int viewChairId,Actor everyCard){
        switch (viewChairId){
            case 0:
                everyCard.setPosition(x1+=76,0);
                break;
            case 1:
                everyCard.setPosition(0,y1+=40);
                break;
            case 2:
                everyCard.setPosition(x2+=76,0);
                break;
            case 3:
                everyCard.setPosition(0,y2+=40);
                break;
        }
    }

    /**
     * 手上的牌路径
     *
     * @param cbViewID
     * @param cbData
     * @return
     */
    private String getHandCardImagePath(int cbViewID, int cbData) {
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

    private String getHandCardImagePath1(int cbViewID, int cbData) {
        String strImagePath = "";
        String s = (((cbData & 0xF0) >> 4) + 1) + "" + (cbData & 0x0F);
        switch (cbViewID) {
            case 0: {
                strImagePath = "2/mingmah_" + s + ".png";
                break;
            }
            case 1: {
                strImagePath = "1/mingmah_" + s + ".png";
                break;
            }
            case 2: {
                strImagePath = "2/mingmah_" + s + ".png";
                break;
            }
            case 3: {
                strImagePath = "3/mingmah_" + s + ".png";
                break;
            }
            default:
                break;
        }
        return strImagePath;
    }

    public int switchViewChairID(int cbChairID) {
        return (cbChairID + 4 - zhuangjia) % 4;
    }
}
