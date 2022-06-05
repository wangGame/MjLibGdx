package kw.tripeak.screen;

import kw.tripeak.cmd.GameCmd;
import kw.tripeak.play.IPlayer;

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
    public boolean onUserEnterEvent(IPlayer pIPlayer){
        return false;
    }

    /**
     * 游戏开始
     * @param GameStart
     * @return
     */
    public boolean onGameStartEvent(GameCmd.CMD_S_GameStart GameStart){
        return false;
    }

    /**
     * 发牌事件
     * @param SendCard
     * @return
     */
    public boolean onSendCardEvent(GameCmd.CMD_S_SendCard SendCard){
        return false;
    }

    /**
     * 出牌事件
     * @param OutCard
     * @return
     */
    public boolean onOutCardEvent(GameCmd.CMD_S_OutCard OutCard) {
        return false;
    }

    /**
     * 操作通知事件
     * @param OperateNotify
     * @return
     */
    public boolean onOperateNotifyEvent(GameCmd.CMD_S_OperateNotify OperateNotify){
        return false;
    }

    /**
     * 操作结果事件
     * @param OperateResult
     * @return
     */
    public boolean onOperateResultEvent(GameCmd.CMD_S_OperateResult OperateResult){
        return false;
    }

    /**
     * 游戏结束事件
     * @param GameEnd
     * @return
     */
    public boolean onGameEndEvent(GameCmd.CMD_S_GameEnd GameEnd){
        return false;
    }

};
