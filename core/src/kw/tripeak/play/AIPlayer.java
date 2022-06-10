package kw.tripeak.play;

import kw.tripeak.engine.GameEngine;
import kw.tripeak.play.IPlayer;
import kw.tripeak.screen.IGameEngineEventListener;

public class AIPlayer extends IPlayer {
    public AIPlayer(PlayerSex sex, IGameEngineEventListener pGameEngineEventListener) {
        super(true,sex,pGameEngineEventListener);
        pGameEngineEventListener.setIPlayer(this);
    }

}
