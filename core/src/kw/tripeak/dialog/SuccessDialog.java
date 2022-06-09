package kw.tripeak.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kw.gdx.dialog.base.BaseDialog;
import com.kw.gdx.listener.ButtonListener;

import kw.tripeak.constant.LevelConfig;

public class SuccessDialog extends BaseDialog {
    private Runnable runnable;
    public SuccessDialog(Runnable runnable) {
        super("cocos/success.json");
        this.runnable = runnable;
        show();
    }

    @Override
    public void show() {
        super.show();
        LevelConfig.levelNum++;

        Actor btn_bg_6 = findActor("btn_bg_6");
        btn_bg_6.setTouchable(Touchable.enabled);
        btn_bg_6.addListener(new ButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                runnable.run();
            }
        });
    }
}
