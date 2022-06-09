package kw.tripeak.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kw.gdx.BaseGame;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.audio.Asset;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.screen.BaseScreen;

import kw.tripeak.asset.FontResource;

@ScreenResource("cocos/HelloLayer.json")
public class LoadingScreen extends BaseScreen {
    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        Actor button_logon = findActor("Button_Logon");
        button_logon.setTouchable(Touchable.enabled);
        button_logon.addListener(new ButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setScreen(MainScreen.class);
            }
        });
    }
}
