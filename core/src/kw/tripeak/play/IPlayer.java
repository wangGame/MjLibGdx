package kw.tripeak.play;

import kw.tripeak.engine.GameEngine;
import kw.tripeak.screen.IGameEngineEventListener;

public class IPlayer {
    //玩家性别
    public enum PlayerSex {
        MALE,            //男
        FEMALE           //女
    };

    protected boolean m_Android;          //是否机器人标识
    int m_ChairID; //椅子编号
    IGameEngineEventListener m_pGameEngineEventListener; //游戏事件监听
    PlayerSex m_Sex;     //性别

    public IPlayer(PlayerSex sex , IGameEngineEventListener pGameEngineEventListener) {
        this(false,sex,pGameEngineEventListener);
    };
    public IPlayer(boolean android, PlayerSex sex , IGameEngineEventListener pGameEngineEventListener) {
        m_Android = android;
        m_Sex = sex;
        m_pGameEngineEventListener = pGameEngineEventListener;
    };  //构造函数
//    ~IPlayer() {
//
//    }; //析构函数
    /**
     * 是否为机器人
     * @return
     */
    public boolean isAndroid() {
        return m_Android;
    };

    /**
     * 设置椅子
     * @param chairID
     */
    public void setChairID(int chairID) {
        m_ChairID = chairID;
    }

    /**
     * 获取椅子编号
     * @return
     */
    public int getChairID() {
        return m_ChairID;
    }

    /**
     * 设置监听
     * @param pGameEngineEventListener
     */
    public void setGameEngineEventListener(IGameEngineEventListener pGameEngineEventListener) {
        m_pGameEngineEventListener = pGameEngineEventListener;
    }

    /**
     * 获取监听
     * @return
     */
    public IGameEngineEventListener getGameEngineEventListener() {
        return m_pGameEngineEventListener;
    }

    /**
     * 获取性别
     * @return
     */
    public PlayerSex getSex(){
        return m_Sex;
    }
}
