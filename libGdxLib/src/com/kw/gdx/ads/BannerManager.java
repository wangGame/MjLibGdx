package com.kw.gdx.ads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;

public class BannerManager {
    private Stage stage;
    private BannerView view;
    private static boolean isVisible;
    public boolean currentVisible;

    public BannerManager(Stage stage) {
        this.stage = stage;
    }

    public void toFront() {
        if (currentVisible != isVisible){
            currentVisible = isVisible;
            view.setVisible(currentVisible);
        }
        view.toFront();

    }

    public static void setVisible(boolean visible){
        isVisible = visible;
    }

    public void init(float v){
        if (view != null) {
            view.remove();
            view=null;
        }
        view = new BannerView();
        view.setPosition(Constant.GAMEWIDTH/2,v,Align.bottom);
        stage.addActor(view);
        currentVisible = true;
    }

    public BannerView getView(float offsetY) {
        return view;
    }


    public void showBanner(boolean visible){
        view.setVisible(true);
    }
}
