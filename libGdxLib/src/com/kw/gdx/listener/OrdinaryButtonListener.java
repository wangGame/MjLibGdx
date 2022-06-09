package com.kw.gdx.listener;


import com.kw.gdx.audio.AudioProcess;
import com.kw.gdx.audio.AudioType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class OrdinaryButtonListener extends ButtonListener {
    private String audioName;

    public OrdinaryButtonListener(Actor target){
        super(target);
    }

    public OrdinaryButtonListener(float scale){
        super(scale);
    }

    public OrdinaryButtonListener(String audioName){
        this.audioName = audioName;
    }

    public OrdinaryButtonListener(){
        super();
    }

    @Override
    public void clickEffect() {
        super.clickEffect();
        if (audioName == null) {
            AudioProcess.playSound(AudioType.BUTTONPRESS);
        }else if (!"".equals(audioName)){
            AudioProcess.playSound(audioName);
        }
    }

    @Override
    public void touchDownEffect() {

    }

    @Override
    protected void releaseEffect() {
        super.releaseEffect();
    }
}
