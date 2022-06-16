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

import kw.mj.MjScreen;
import kw.tripeak.asset.FontResource;
import majiang_algorithm.AIUtil;
import majiang_algorithm.HuUtil;

/**
 * 主页面
 */
@ScreenResource("cocos/HelloLayer.json")
public class LoadingScreen extends BaseScreen {
    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        HuUtil.load();
        AIUtil.load();

        HuUtil.load();

        Actor startBtn = findActor("Button_Logon");
        startBtn.setTouchable(Touchable.enabled);
        startBtn.addListener(new ButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setScreen(MjScreen.class);
            }
        });
    }
}
