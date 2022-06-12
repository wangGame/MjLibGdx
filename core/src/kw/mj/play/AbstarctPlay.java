package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AbstarctPlay extends Actor {
    private IPlayCallback peakCard;
    private IPlayCallback outCard;
    private int chair;

    public AbstarctPlay(IPlayCallback peakCard, IPlayCallback outCard) {
        this.peakCard = peakCard;
        this.outCard = outCard;
    }

    public void peakCard(){
        peakCard.call(chair);
    }

    public void outCard(){
        outCard.call(chair);
    }

    public void setChair(int chair) {
        this.chair = chair;
    }

    public void setCard(Actor actor) {
        actor.setDebug(true);
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                outCard();
            }
        });
    }

    public interface IPlayCallback {
        public void call(int run);
    }
}
