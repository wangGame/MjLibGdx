package com.kw.gdx.audio;

import com.badlogic.gdx.assets.AssetManager;

public abstract class AAsset {
    protected String name;
//    private static AssetManager assetManager;
    public static MusicAsset registerMusicAsset(String name,AssetManager am) {
//        assetManager = am;
        MusicAsset ma = new MusicAsset(name,am);
        return ma;
    }

    public abstract void load(); // 同步

    public abstract void loading(AssetManager assetManager); // 异步

    public abstract void finished(AssetManager assetManager); // 异步完成

    public abstract void dispose(AssetManager assetManager);

    public String getName() {
        return name;
    }

    public static SoundAsset registerSoundAsset(String name) {
        SoundAsset sa = new SoundAsset(name);
        return sa;
    }

}
