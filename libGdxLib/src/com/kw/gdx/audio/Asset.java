package com.kw.gdx.audio;

import com.badlogic.gdx.utils.I18NBundle;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kw.gdx.annotation.FtResource;
import com.kw.gdx.annotation.I18BundleAnnotation;
import com.kw.gdx.annotation.SpineResource;
import com.kw.gdx.annotation.TextureReginAnnotation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.SkeletonData;
import com.kw.gdx.log.NLog;
import com.esotericsoftware.spine.loader.SkeletonDataLoader;
import com.ui.ManagerUIEditor;
import com.ui.loader.ManagerUILoader;
import com.ui.plist.PlistAtlas;
import com.ui.plist.PlistAtlasLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Asset implements Disposable {
    private static Asset asset;
    public static AssetManager assetManager;
    private int i=0;

    public void loadAsset(Object ob) {
        Field[] declaredFields = ob.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] annotations = declaredField.getAnnotations();
            if (annotations.length>0) {
                Annotation annotation = annotations[0];
                if (annotation instanceof SpineResource){
                    SpineResource annotation1 = (SpineResource) annotation;
                    if (annotation1.isSpine()) {
                        try {
                            assetManager.load((String)declaredField.get(ob), SkeletonData.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            assetManager.load((String)declaredField.get(ob), ParticleEffect.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(annotation instanceof FtResource){
//                    FtResource annotation = AnnotationInfo.checkFeildAnnotation(field, FtResource.class);
                    FtResource annotation1 = (FtResource) annotation;
                    try {
                        BitmapFontLoader.BitmapFontParameter parameter = null;
                        parameter = new BitmapFontLoader.BitmapFontParameter();
                        parameter.genMipMaps = true;
                        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
                        parameter.magFilter = Texture.TextureFilter.Linear;
                        assetManager.load(annotation1.value(), BitmapFont.class,parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof TextureReginAnnotation){
                    TextureReginAnnotation textureReginAnnotation = (TextureReginAnnotation)annotation;
                    assetManager.load(textureReginAnnotation.value(), TextureAtlas.class);
                }else if (annotation instanceof I18BundleAnnotation){
                    I18BundleAnnotation i18BundleAnnotation = (I18BundleAnnotation) annotation;
                    assetManager.load(i18BundleAnnotation.value(), I18NBundle.class);
                }
            }
        }
    }

    public void getResource(Object ob){
        Field[] declaredFields = ob.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] annotations = declaredField.getAnnotations();
            if (annotations.length>0) {
                Annotation annotation = annotations[0];
                if(annotation instanceof FtResource){
                    FtResource ftResource = ((FtResource)annotation);
                    try {
                        declaredField.set(ob,assetManager.get(ftResource.value(), BitmapFont.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof TextureReginAnnotation){
                    TextureReginAnnotation reginAnnotation = (TextureReginAnnotation) annotation;
                    try {
                        declaredField.set(ob,assetManager.get(reginAnnotation.value(), TextureAtlas.class));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof I18BundleAnnotation){
                    I18BundleAnnotation i18BundleAnnotation = (I18BundleAnnotation) annotation;
                    try {
                        declaredField.set(ob,assetManager.get(i18BundleAnnotation.value(), I18NBundle.class));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public TextureAtlas getAtlas(String path){
        assetManager.load(path,TextureAtlas.class);
        assetManager.finishLoading();
        return assetManager.get(path,TextureAtlas.class);
    }

    public Texture getTexture(String path){
        if (!Gdx.files.internal(path).exists()){
            NLog.e("%s resouce not exist",path);
            return null;
        }
        if (!assetManager.isLoaded(path)) {
            TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.minFilter = Texture.TextureFilter.Linear;
            assetManager.load(path, Texture.class,parameter);
            assetManager.finishLoading();
        }
        Texture texture = assetManager.get(path, Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return assetManager.get(path,Texture.class);
    }

    public Sprite getSprite(String path){
        Texture texture = getTexture(path);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Sprite sprite = new Sprite(texture);
        return sprite;
    }

    public void disposeTexture(String path){
        if (assetManager.isLoaded(path)) {
            NLog.i("%s dispose ",path);
            assetManager.unload(path);
        }
    }

    private Asset(){
        i++;
        if (i>=2){
            throw new RuntimeException("gun");
        }
        assetManager = getAssetManager();
    }

    private AssetManager getAssetManager(){
        if (assetManager == null){
            assetManager = new AssetManager();
            assetManager.setLoader(TiledMap.class,new TmxMapLoader());
            assetManager.setLoader(ManagerUIEditor.class,new ManagerUILoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(PlistAtlas.class, new PlistAtlasLoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(SkeletonData.class,new SkeletonDataLoader(assetManager.getFileHandleResolver()));
//            assetManager.setLoader(TextureAtlas.class,new MiniTextureAtlasLoader(assetManager.getFileHandleResolver(), Configuration.scale));
//            assetManager.setLoader(Texture.class,new MiniTextureLoader(assetManager.getFileHandleResolver(),Configuration.scale));
        }
        return assetManager;
    }

    public static Asset getAsset() {
        if (asset==null){
            asset = new Asset();
        }
        return asset;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        assetManager = null;
        asset = null;
    }

    private SkeletonRenderer renderer;

    public SkeletonRenderer getRenderer() {
        if (renderer == null){
            renderer = new SkeletonRenderer();
        }
        return renderer;
    }
}
