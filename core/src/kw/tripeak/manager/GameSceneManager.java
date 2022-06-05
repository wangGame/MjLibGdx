package kw.tripeak.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.screen.BaseScreen;

public class GameSceneManager {
    //场景
    BaseScreen m_pCurrentScene;
    //根节点
    Group m_pRootLayer;
    /**
     * 构造函数
     */
    public GameSceneManager(){}

//    /**
//     * 析构函数
//     */
//    ~GameSceneManager();

    /**
     * 设置当前场景
     * @param pScene
     */
    void setScene(BaseScreen pScene){

    }

    /**
     * 获取当前场景
     * @return
     */
    BaseScreen getScene(){
        return null;
    }

    /**
     * 设置Layer为根节点
     * @param pLayer
     */
    void setRootLayer(Group pLayer){

    }

    /**
     * 提示框
     * @param strContent
     */
    void alert(String strContent, boolean autoClose, boolean keep,Group okTarget, Runnable okSelector){

    }

    /**
     * 选择框
     * @param strContent
     */
    void confirm(String strContent, boolean autoClose, boolean keep, Group okTarget,Runnable okSelector, Group cancelTarget,
                 Runnable cancelSelector){

    }

    /**
     * 移除弹出框节点
     */
    void removeAlertTag(){

    }

    /**
     * 获取可见区域大小
     * @return
     */
    Vector2 getVisibleSize(){
        return new Vector2();
    }

    /**
     * 结束游戏
     */
    void end(){

    }

    /**
     * 获取GameSceneManager单例
     * @return
     */
    public static GameSceneManager getInstance(){
        return null;
    }

}