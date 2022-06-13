package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class AIplayer extends AbstarctPlay {
//    //先得到当前用户的牌面
//    private int cards[];

    public AIplayer(IPlayCallback peakCard, IPlayCallback outCard) {
        super(peakCard, outCard);
    }

    @Override
    public void peakCard() {
        super.peakCard();
        addAction(Actions.delay(1F,Actions.run(()->{
            outCard();
        })));
    }
}
