package kw.tripeak.screen;

import kw.tripeak.engine.GameEngine;
import kw.tripeak.play.IPlayer;

public class AIPlayer extends IPlayer {
    public AIPlayer(PlayerSex sex, IGameEngineEventListener pGameEngineEventListener) {
        super(sex,pGameEngineEventListener);
        pGameEngineEventListener.setIPlayer(this);
    }

}
