package com.kw.gdx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kw.gdx.BaseGame;
import com.kw.gdx.ads.BannerManager;
import com.kw.gdx.ads.BannerView;
import com.kw.gdx.ads.Constant;
import com.kw.gdx.annotation.AnnotationInfo;
import com.kw.gdx.annotation.ScreenResource;
import com.kw.gdx.cocosload.CocosResource;
import com.kw.gdx.dialog.DialogManager;
import com.kw.gdx.dialog.base.BaseDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BaseScreen implements Screen {
    protected Stage stage;
    protected Group rootView;
    protected String viewpath;
    protected float offsetY;
    protected float offsetX;
    protected boolean back;
    protected BaseGame game;
    protected DialogManager dialogManager;
    private BannerManager bannerManager;

    public BaseScreen(BaseGame game){
        this.game = game;
        stage = new Stage(getStageViewport(), getBatch());
        offsetY = (Constant.GAMEHIGHT - Constant.HIGHT)/2;
        offsetX = (Constant.GAMEWIDTH - Constant.WIDTH)/2;
        bannerManager = new BannerManager(stage);
        bannerManager.init(-offsetY);
        bannerManager.setVisible(false);
        dialogManager = new DialogManager(stage);
    }

    protected void initAnnotation(){

    }

    private Batch getBatch() {
        return game.getBatch();
    }

    private Viewport getStageViewport() {
        return game.getStageViewport();
    }

    public void touchDisable(){
        Gdx.input.setInputProcessor(null);
    }

    public void touchEnable(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        initTouch();
        initRootView();
        initAnnotation();
        initView();
    }

    public void initView(){}

    private void initRootView() {
        ScreenResource annotation = AnnotationInfo.checkClassAnnotation(this, ScreenResource.class);
        if (annotation!=null){
            viewpath = annotation.value();
            rootView = CocosResource.loadFile(viewpath);
            stage.addActor(rootView);
            rootView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }
    }

    private void initTouch() {
        stage.addListener(BackInputListener());
        touchEnable();
    }

    private InputListener BackInputListener() {
        return new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)) {
                    back = true;
                    back();
                }
                return super.keyDown(event, keycode);
            }
        };
    }

    protected BaseDialog back() {
        back = false;
        BaseDialog back = dialogManager.back();
        return null;
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
//        banner.toFront();
        bannerManager.toFront();
//        o.toFront();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        if (viewpath!=null) {
            CocosResource.unLoadFile(viewpath);
        }
    }

    public void addActor(Actor addActor){
        stage.addActor(addActor);
    }

    public void setScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

    public void setScreen(Class<? extends BaseScreen> t) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(game);
            game.setScreen(baseScreen);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T extends Actor> T findActor(String name){
        return rootView.findActor(name);
    }

}

