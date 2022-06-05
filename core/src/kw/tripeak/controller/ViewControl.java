package kw.tripeak.controller;

import com.badlogic.gdx.scenes.scene2d.Group;

import kw.tripeak.view.ViewObject;

public class ViewControl {
    private static String ccNd_ViewNotify = "ccNd_ViewNotify";

    public void onViewNotify(Group render){
        if (render == NULL){    //进入游戏
            GameSceneManager::getInstance()->setRootLayer(HelloLayer::create()->GetLayer());
            return;
        }
        ViewObject pObject = (ViewObject *) render;
        if (pObject->m_MainString == VIEW_SWITCH_MAIN_LAYER){
            if (pObject->m_subString == "GameLayer"){    //切换到游戏视图
                GameSceneManager::getInstance()->setRootLayer(GameLayer::create()->GetLayer());
            }
        }
    }
}
