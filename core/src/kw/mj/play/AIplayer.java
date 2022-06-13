package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import kw.mj.card.EveryCard;

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
            restCards();
            int card = cards[(int) (cards.length * Math.random())];
            EveryCard everyCard = indexToEvery(card);
            outCard(everyCard);
        })));
    }

    @Override
    public void setCard(Group panel) {
        super.setCard(panel);
    }

    @Override
    protected void restCards() {
        Group handCard = panel.findActor("HandCard_" + chair);
        int index = 0;
        for (Actor child : handCard.getChildren()) {
            if (child instanceof EveryCard){
                EveryCard temp = (EveryCard) child;
                temp.setDebug(true);
                cards[index ++] = temp.getData();
            }
        }
    }
}
