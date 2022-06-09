package com.esotericsoftware.spine.loader;

import com.kw.gdx.ads.PixmapImage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.kw.gdx.audio.Asset;

public class SpineActor extends Actor {
    protected Skeleton skeleton;
    private AnimationState state;
    private SkeletonRenderer renderer;
    private AnimationStateData animData;
    private String path;
    private float rootBoneScaleX  = 1,rootBoneScaleY=1;
    private float offsetX,offsetY;
    private AssetManager assetamnagerinstance;
    private boolean active;

    public SpineActor(String path) {
        this.path = path;
        flushAsset();
        init(path);
    }

    public SpineActor(String path, boolean flag) {
        this.path = path;
        flushAsset();
        init(path);
    }

    public SpineActor(String path, String atlas) {
        this.path = path;
        assetamnagerinstance = Asset.getAsset().assetManager;
        if(!assetamnagerinstance.isLoaded(path+".json")) {
            SkeletonDataLoader.SkeletonDataParameter mainSkeletonParameter = new SkeletonDataLoader.SkeletonDataParameter();
            mainSkeletonParameter.atlasfile = atlas;
            assetamnagerinstance.load(path + ".json", SkeletonData.class,mainSkeletonParameter);
            assetamnagerinstance.finishLoading();
        }
        init(path);
    }


    public void flushAsset(){
        assetamnagerinstance = Asset.getAsset().assetManager;
        if(!assetamnagerinstance.isLoaded(path+".json")) {
            assetamnagerinstance.load(path + ".json", SkeletonData.class);
            assetamnagerinstance.finishLoading();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setColor(Color color) {
        skeleton.setColor(color);
        super.setColor(color);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        skeleton.getRootBone().setPosition(width/2,height/2);
    }

    public void init(String path) {
        renderer = Asset.getAsset().getRenderer();
        SkeletonData data = assetamnagerinstance.get(path+".json");
        skeleton = new Skeleton(data);
        animData = new AnimationStateData(data);
        state = new AnimationState(animData);
        setPosition(0,0);
    }

    public void setAnimation(String name, boolean loop){
//        System.out.println(name+"=================name");
        getAnimaState().clearListeners();
        state.setAnimation(0,name,loop);
    }

    public void setSpineOffset(float offsetX,float offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void setAnimation(String name,String name2){
        setAnimation(name,false);
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                setAnimation(name2,true);
            }
        });
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        skeleton.getRootBone().setScale(scaleXY);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        skeleton.setPosition(getX() + offsetX,getY() + offsetY);
    }

    @Override
    public void setRotation(float degrees) {
        skeleton.getRootBone().setRotation(degrees);
        super.setRotation(degrees);
    }

    public void setSkin(String name){
        skeleton.setSkin(name);
        skeleton.setSlotsToSetupPose();
    }

    public void setAttachment(String s, String s1){
        skeleton.setAttachment(s,s1);
    }


    public void dispose(){
        remove();
    }


    public void unloadSpine(){
        if(Asset.getAsset().assetManager.isLoaded(path+".json")){
            Asset.getAsset().assetManager.unload(path+".json");
        }
        remove();
    }

    @Override
    public void addAction(Action action) {
        super.addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float alpha = this.color.a*parentAlpha;
        Color color = skeleton.getColor();
        float oldAlpha = color.a;
        skeleton.getColor().a *= alpha;
        if (customize){
            skeleton.getRootBone().setScale(rootBoneScaleX*scaleX,rootBoneScaleY*scaleY);
        }

        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        skeleton.updateWorldTransform();
        int src = batch.getBlendSrcFunc();
        int dst = batch.getBlendDstFunc();
        if(batch instanceof PolygonSpriteBatch){
//            System.out.println(path);
            renderer.draw((PolygonSpriteBatch)batch,skeleton);
        }else {
            renderer.draw(batch, skeleton);
        }
        batch.setBlendFunction(src,dst);
        color.a = oldAlpha;
    }

    @Override
    public Stage getStage() {
        return super.getStage();
    }

    public AnimationState getAnimaState(){
        return state;
    }
    public SkeletonData getsData() {
        return skeleton.getData();
    }

    private boolean customize = true;
    public void setCustomizeScale(boolean b) {
        this.customize = b;
    }

    public void setTimeScale(float timeScale){
        state.setTimeScale(timeScale);
    }

    public void completeDispose() {
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                remove();
            }
        });
    }

    public void completeRomove() {
        getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                remove();
            }
        });
    }

    public void setFlipX(){
//        skeleton.setFlipX(false);
//        skeleton.setFlipY(false);
    }
}
