package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import kw.mj.card.EveryCard;

public class AbstarctPlay extends Actor {
    private IPlayCallback peakCard;
    private IPlayCallback outCard;
    protected int chair;
    protected int[] cards;
    protected Group panel;
    protected int size;

    public AbstarctPlay(IPlayCallback peakCard, IPlayCallback outCard) {
        this.peakCard = peakCard;
        this.outCard = outCard;
        cards = new int[14];
    }

    public void peakCard(){
        peakCard.call(chair,null);
    }

    public int[] getCards() {
        return cards;
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
        restCards();
        everyCard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                outCard(event.getListenerActor());
                System.out.println("---------------------------------");
            }
        });
    }


    public void setData(int nextCard) {
        cards[13] = nextCard;
        restCards();
        addAction(Actions.delay(1F,Actions.run(()->{
            restCards();
            int card = cards[(int) (cards.length * Math.random())];
            EveryCard everyCard = indexToEvery(card);
            outCard(everyCard);
        })));
        size = 13;
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

    protected void restCards() {

    }

    public void removeCard(int card) {
        Group handCard = panel.findActor("HandCard_" + chair);
        Actor childTemp = null;
        for (Actor child : handCard.getChildren()) {
            if (((EveryCard)(child)).getData() == card) {
                childTemp = child;
            }
        }
        handCard.removeActor(childTemp);
        for (int i = 0; i < cards.length; i++) {
            cards[i] = 0;
        }
        restCards();
    }

    public interface IPlayCallback {
        public void call(int run,Actor actor);
    }
}
