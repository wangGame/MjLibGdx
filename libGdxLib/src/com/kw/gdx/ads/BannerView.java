package com.kw.gdx.ads;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BannerView extends Group {
    public BannerView(){
        this(pxToDp(300),pxToDp(50));
    }

    public BannerView(float bannerWidth, float bannerHight) {
        PixmapImage image = new PixmapImage((int)bannerWidth,(int)bannerHight);
        Image image1 = new Image(image.getPixmap());
        addActor(image1);
        setSize(image1.getWidth(),image1.getHeight());
    }

    public static float pxToDp(float dp){
        return (float) (dp*Constant.vvv + 0.5F);
    }
}
