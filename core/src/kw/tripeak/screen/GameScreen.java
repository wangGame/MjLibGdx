package kw.tripeak.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.kw.gdx.BaseGame;
import com.kw.gdx.ads.Constant;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.screen.BaseScreen;
import java.util.ArrayList;
import java.util.Collections;

import kw.tripeak.data.Board;
import kw.tripeak.data.GameData;
import kw.tripeak.data.PeakBean;
import kw.tripeak.dialog.SuccessDialog;
import kw.tripeak.group.CardGroup;

@ScreenResource("cocos/GameLayer.json")
public class GameScreen extends BaseScreen {

    public GameScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();

    }
}
