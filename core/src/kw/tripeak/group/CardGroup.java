package kw.tripeak.group;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.utils.ImageUtils;

import kw.tripeak.data.Board;

public class CardGroup extends Group {
    private Image imageBg;
    private Image imageFront;
    private Image cardData;
    private Image smallCardData;
    private Image huase;
    private int posx;
    private int posy;
    private int index;
    private boolean select;
    private int suit;

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }

    public CardGroup(Board board){
        this.index = board.getRank();
        setSize(144, 222);
        imageBg = new Image(new Texture("images/cards/peak_front.png"));
        imageFront = new Image(new Texture("images/cards/peak_back.png"));
        if (board.getSuit()==1||board.getSuit()==3){
            cardData = new Image(new Texture("numTexture/r"+index+".png"));
            smallCardData = new Image(new Texture("numTexture/sr"+index+".png"));
        }else {
            cardData = new Image(new Texture("numTexture/b"+index+".png"));
            smallCardData = new Image(new Texture("numTexture/sb"+index+".png"));
        }
        huase = new Image(new Texture("numTexture/s"+board.getSuit()+".png"));
        this.suit = board.getSuit();
        addActor(cardData);
        addActor(imageBg);
        addActor(smallCardData);
        addActor(huase);
        addActor(imageFront);
        imageBg.setSize(getWidth(),getHeight());
        imageFront.setSize(getWidth(),getHeight());
        cardData.setOrigin(Align.center);
        imageBg.setPosition(getWidth()/2,getHeight()/2, Align.center);
        imageFront.setPosition(getWidth()/2,getHeight()/2,Align.center);
        cardData.setPosition(getWidth()/2,getHeight()/2-20,Align.center);
        smallCardData.setPosition(30,getHeight()-40,Align.center);
        huase.setPosition(getWidth()-30,getHeight()-40,Align.center);
        addListener(new ButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                listener.click(CardGroup.this);
            }
        });
        setTouchable(Touchable.disabled);
        setOrigin(Align.center);
        setId(board.getId());
        setRotation(board.getRotation());
        setPosition(board.getPosX(),board.getPoxY(),Align.bottom);
    }


    public void select(boolean select){
        this.select = select;
        if (select){
            setTouchable(Touchable.enabled);
            imageFront.setVisible(false);
            cardData.toFront();
        }
    }

    public boolean isSelect() {
        return select;
    }

    public void onlySelect(boolean select){
        this.select = select;
        if (select){
            imageFront.setVisible(false);
            cardData.toFront();
        }
    }

    public void onlyTouch(){
        setTouchable(Touchable.enabled);
    }

    public void setPosX(int posx) {
        this.posx = posx;
    }

    public void setPosY(int posy) {
        this.posy = posy;
    }

    public int getPosX() {
        return posx;
    }

    public int getPosY(){
        return posy;
    }

    private CardListener listener;
    public void setCardListener(CardListener cardListener){
        this.listener = cardListener;
    }

    public int packNum() {
        return index % 13;
    }

    public int getIndex() {
        return index;
    }

    public void update(int index,int suit) {
//        cardData = new Image(new Texture("numTexture/b"+index+".png"));
//        addActor(cardData);
//        cardData.setOrigin(Align.center);
//        cardData.setPosition(getWidth()/2,getHeight()/2,Align.center);
        if (suit==1||suit==3){
            ImageUtils.changeImageTexture(cardData,new Texture("numTexture/r"+index+".png"));
            ImageUtils.changeImageTexture(smallCardData,new Texture("numTexture/sr"+index+".png"));
//            huase = new Image(new Texture("numTexture/s"+suit+".png"));
        }else {

            ImageUtils.changeImageTexture(cardData,new Texture("numTexture/b"+index+".png"));
            ImageUtils.changeImageTexture(smallCardData,new Texture("numTexture/sb"+index+".png"));
        }
        ImageUtils.changeImageTexture(huase,new Texture("numTexture/s"+suit+".png"));

        this.suit = suit;
        this.index = index;
    }

    private int id;
    public void setId(int id) {
        this.id = id;
    }

    private int[] arr;
    public void check(int arr[]){
        this.arr = arr;
    }

    public int[] getArr() {
        return arr;
    }

    public int getid() {
        return id;
    }

    public interface CardListener{
        public void click(CardGroup cardGroup);
    }
}
