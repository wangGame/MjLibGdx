package com.kw.gdx.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.audio.Asset;

public class EffectTool extends Actor {
    private ParticleEffect effect;
    private String path;
    private AssetManager assetamnagerinstance;
    private float h;
    private float w;

    public EffectTool(String path){
        this.path = path;
        assetamnagerinstance = Asset.assetManager;
        if (!assetamnagerinstance.isLoaded(path)){
            assetamnagerinstance.load(path, ParticleEffect.class);
            assetamnagerinstance.finishLoading();
        }
        init();
    }


    public EffectTool(String path, String atlasFile){
        this.path = path;
        assetamnagerinstance = Asset.assetManager;
        if (!assetamnagerinstance.isLoaded(path)){
            ParticleEffectLoader.ParticleEffectParameter
                    particleEffectParameter =
                    new ParticleEffectLoader.ParticleEffectParameter();
            particleEffectParameter.atlasFile = atlasFile;
            assetamnagerinstance.load(path, ParticleEffect.class,particleEffectParameter);
            assetamnagerinstance.finishLoading();
        }
        init();
    }

    public void setEffectScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        //缩放粒子  回到池中恢复     motionScaleFactor发射器的速度
        effect.scaleEffect(scaleX,scaleY,1);
    }

    public void init(){
        effect = assetamnagerinstance.get(path);
        effect = new ParticleEffect(effect);
        effect.reset();
        effect.start();
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public boolean isComplate(){
        return effect.isComplete();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        effect.setPosition(x,y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        effect.setPosition(x,y);
    }

    private boolean flag = false;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible())return;
        int blendSrcFunc = batch.getBlendSrcFunc();
        int blendDstFunc = batch.getBlendDstFunc();
        if (flag) {
            if (clipBegin(0, 0, w, h)) {
                effect.draw(batch, Gdx.graphics.getDeltaTime());
                clipEnd();
            }
        }else {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.setBlendFunction(blendSrcFunc,blendDstFunc);
    }

    public void dispose(){
        try {
            assetamnagerinstance.unload(path);
        }catch (Exception e){
            System.out.println("已经释放！");
        }
    }

}

