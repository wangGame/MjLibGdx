package kw.mj.play;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import kw.mj.card.EveryCard;

public class NomalPlayer extends AbstarctPlay {

    public NomalPlayer(IPlayCallback out, IPlayCallback send) {
        super(out, send);
    }

    @Override
    public void setCard(Group panel) {
        super.setCard(panel);
        restCards();
    }

    @Override
    protected void restCards() {
        Group handCard = panel.findActor("HandCard_" + chair);
        int index = 0;
        for (Actor child : handCard.getChildren()) {
            if (child instanceof EveryCard){
                EveryCard temp = (EveryCard) child;
                temp.setDebug(true);
                temp.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        outCard(event.getListenerActor());
                    }
                });
                cards[index ++] = temp.getData();
            }
        }
    }
}
