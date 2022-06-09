package kw.tripeak.asset;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.kw.gdx.annotation.FtResource;
import com.kw.gdx.audio.Asset;

public class FontResource {
    private static FontResource instance;
    @FtResource("fonts/outline.fnt")
    public BitmapFont aoutline_30;

    public static FontResource getInstance(){
        if (instance == null){
            instance = new FontResource();
        }
        return instance;
    }


    public void loadRes(){
        Asset.getAsset().loadAsset(this);
    }

    public void getRes(){
        Asset.getAsset().getResource(this);
    }
//    @Override
//    public void dispose() {
//        instance = null;
//    }
}
