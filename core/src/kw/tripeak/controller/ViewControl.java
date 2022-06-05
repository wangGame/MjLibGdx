package kw.tripeak.controller;

import com.badlogic.gdx.scenes.scene2d.Group;

import kw.tripeak.manager.GameSceneManager;
import kw.tripeak.view.ViewObject;

public class ViewControl {
    private static String ccNd_ViewNotify = "ccNd_ViewNotify";

    public void onViewNotify(ViewObject render){
        ViewObject pObject = (ViewObject) render;
        if (pObject.m_MainString == ViewObject.VIEW_SWITCH_MAIN_LAYER){
            if (pObject.m_subString == "GameLayer"){    //切换到游戏视图
//                GameSceneManager::getInstance().setRootLayer(GameLayer.create()->GetLayer());
            }
        }
    }
}
