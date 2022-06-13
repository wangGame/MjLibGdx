package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import kw.mj.card.EveryCard;

public class AbstarctPlay extends Actor {
    private IPlayCallback peakCard;
    private IPlayCallback outCard;
    protected int chair;
    protected int[] cards;
    private Group panel;

    public AbstarctPlay(IPlayCallback peakCard, IPlayCallback outCard) {
        this.peakCard = peakCard;
        this.outCard = outCard;
        cards = new int[14];
    }

    public void peakCard(){
        peakCard.call(chair,null);
    }

    public void outCard(Actor actor){
        outCard.call(chair,actor);
    }

    public void setChair(int chair) {
        this.chair = chair;
    }

    public void setCard(Group panel) {
        this.panel = panel;
    }

    //揭牌之后的
    public void setEvery(EveryCard everyCard){
        cards[13] = everyCard.getData();
        everyCard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                outCard(event.getTarget());
            }
        });
    }

    public void setData(int nextCard) {
        cards[13] = nextCard;
    }

    public EveryCard indexToEvery(int index){
        Group handCard = panel.findActor("HandCard_" + chair);
        for (Actor child : handCard.getChildren()) {
            if (child instanceof EveryCard) {
                EveryCard temp = (EveryCard) child;
                if (temp.getData() == index) {
                    return temp;
                }
            }
        }
        return null;
    }

    public interface IPlayCallback {
        public void call(int run,Actor actor);
    }
}
