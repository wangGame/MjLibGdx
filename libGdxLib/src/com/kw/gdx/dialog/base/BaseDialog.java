package com.kw.gdx.dialog.base;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.ads.Constant;
import com.kw.gdx.audio.AudioProcess;
import com.kw.gdx.audio.AudioType;
import com.kw.gdx.basier.BseInterpolation;
import com.kw.gdx.cocosload.CocosResource;
import com.kw.gdx.dialog.DialogManager;

/**
 * Console.WriteLine(dt.ToString("HH:mm:ss.fffffff"))
 */
public class BaseDialog extends Group {
    protected Group dialogGroup;
    protected DialogManager.Type type = DialogManager.Type.closeOldShowCurr;
    protected float offsetX;
    protected float offsetY;
    protected float shadowTime = 0.1667F;
    protected boolean playOpenAudio = false;
    protected String openMusic = AudioType.MENU_MUSIC1;
    protected boolean playCloseAudio = false;
    protected String closeMusic = AudioType.MENU_MUSIC1;


    public float getShadowTime() {
        return shadowTime;
    }

    public BaseDialog(String dialogpath){
        if (dialogpath!=null){
            dialogGroup = CocosResource.loadFile(dialogpath);
            setSize(dialogGroup.getWidth(),dialogGroup.getHeight());
        }else {
            dialogGroup = new Group();
            dialogGroup.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        }
        setSize(dialogGroup.getWidth(),dialogGroup.getHeight());
        addActor(dialogGroup);
        setY(Constant.GAMEHIGHT/2, Align.center);
        setX(Constant.GAMEWIDTH/2,Align.center);
        offsetX = (Constant.GAMEWIDTH - Constant.STDWIDTH)/2;
        offsetY = (Constant.GAMEHIGHT - Constant.STDHIGHT) / 2;
    }

    public void close(){
        setOrigin(Align.center);
        addAction(
                Actions.parallel(
                    Actions.sequence(
                        Actions.scaleTo(0.8F,0.8F,0.1667F),
                        Actions.run(()->{
                            remove();
                        })
                    ), Actions.sequence(Actions.alpha(0,0.1667F))
                )
        );
    }

    protected void playOpenAudio(){
        if (playOpenAudio){
            AudioProcess.playSound(openMusic);
        }
    }

    protected void playCloseAudio(){
        if (playCloseAudio){
            AudioProcess.playSound(closeMusic);
        }
    }

    public DialogManager.Type getType() {
        return type;
    }

    public void show() {
        playOpenAudio();
        setAphlaZero();
//        AudioProcess.playSound(AudioType.TABPOPUP);
        setOrigin(Align.center);
//        addAction(Actions.sequence(Actions.scaleTo(0,0),Actions.scaleTo(1,1,0.3F, Interpolation.swingOut)));
        addAction(Actions.parallel(
                Actions.sequence(
                        Actions.alpha(0,0),
                        Actions.alpha(1,0.23333F)
                ), Actions.sequence(
                        Actions.scaleTo(0.7F,0.7F,0),
                        Actions.scaleTo(1.1F,1.1F,0.2333F,new BseInterpolation(0.25F,0F,0.75F,1.0F)),
                        Actions.scaleTo(1.0F,1.0F,0.1667F,new BseInterpolation(0.0F,0.03F,0.75F,1.0F)))

        ));
    }

    public void hide() {
        addAction(Actions.scaleTo(0,0,0.2F));
    }

    protected boolean showShadow = true;

    public boolean isShadow() {
        return showShadow;
    }

    public void other() {

    }

    public void setType(DialogManager.Type type) {
        this.type = type;
    }

    public int shadowCloseType = 0;

    public void setShadowCloseType(int shadowCloseType) {
        this.shadowCloseType = shadowCloseType;
    }

    public int getShadowCloseType() {
        return shadowCloseType;
    }

    public void extendsMethod(){

    }
}
