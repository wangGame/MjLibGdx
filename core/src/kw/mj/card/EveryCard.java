package kw.mj.card;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.audio.Asset;

import kw.mj.play.AbstarctPlay;

public class EveryCard extends Group {
    private Image cardImage;
    private int data;

    public EveryCard(String path){
        cardImage = new Image(Asset.getAsset().getTexture(path));
        addActor(cardImage);
        setSize(cardImage.getWidth(),cardImage.getHeight());
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
