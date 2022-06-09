package kw.tripeak;

import com.kw.gdx.BaseGame;
import com.kw.gdx.ads.Constant;
import com.kw.gdx.annotation.GameInfo;

import kw.tripeak.screen.LoadingScreen;


@GameInfo(width = 1280,height = 720)
public class TripeakGame extends BaseGame {
    @Override
    protected void loadingView() {
        super.loadingView();
        Constant.viewColor.set(35.0F/255F,36.0F/255F,51.0F/255F,1);
        setScreen(new LoadingScreen(this));
    }
}
