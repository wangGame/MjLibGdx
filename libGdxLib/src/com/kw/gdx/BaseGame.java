package com.kw.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kw.gdx.ads.Constant;
import com.kw.gdx.annotation.AnnotationInfo;
import com.kw.gdx.annotation.GameInfo;
import com.kw.gdx.dialog.DialogManager;


public class BaseGame extends Game {
    private Batch batch;
    private Viewport stageViewport;

    @Override
    public void create() {
        initData();
        initInstance();
        initViewport();
        Gdx.app.postRunnable(()->{
            loadingView();
        });
    }

    private void initData() {
        GameInfo info = AnnotationInfo.checkClassAnnotation(this,GameInfo.class);
        if (info!=null) {
            Constant.WIDTH = info.width();
            Constant.HIGHT = info.height();
            Constant.batchType = info.batch();
            Constant.viewportType = info.viewportType();
        }
    }

    protected void loadingView(){}

    private void initInstance(){
        Gdx.input.setCatchBackKey(true);

    }

    private void initViewport() {
        if (Constant.viewportType == Constant.EXTENDVIEWPORT) {
            stageViewport = new ExtendViewport(Constant.WIDTH, Constant.HIGHT);
        }else if (Constant.viewportType == Constant.FITVIEWPORT){
            stageViewport = new FitViewport(Constant.WIDTH, Constant.HIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
        viewPortResize(width, height);
    }

    private void viewPortResize(int width, int height) {
        stageViewport.update(width,height);
        stageViewport.apply();
        Constant.GAMEWIDTH = stageViewport.getWorldWidth();
        Constant.GAMEHIGHT = stageViewport.getWorldHeight();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(Constant.viewColor.r, Constant.viewColor.g, Constant.viewColor.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.render();
    }

    public Viewport getStageViewport() {
        return stageViewport;
    }

    public Batch getBatch() {
        if (batch==null) {
            if (Constant.batchType == Constant.COUPOLYGONBATCH) {
                batch = new CpuPolygonSpriteBatch();
            }else if (Constant.batchType == Constant.SPRITEBATCH){
                batch = new SpriteBatch();
            }
        }
        if (batch== null){
            batch = new CpuPolygonSpriteBatch();
        }
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch!=null) {
            batch.dispose();
            batch = null;
        }
        otherDispose();
    }

    public void otherDispose(){

    }
}
