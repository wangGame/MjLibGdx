//package kw.tripeak.dialog;
//
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Group;
//
//public class BaseUILayer extends Actor {
//
//    /**
//     * Cocostudio UI节点
//     */
//    protected Group m_pLayer; //层节点
//
//
//    /**
//     * 设置触屏监听
//     * @param pNode
//     *  根节点
//     * @param button
//     *  监听按钮组件
//     * @param imageView
//     *  监听ImageView组件
//     */
//    void setTouchEventListener(Group pNode, boolean button/* = true*/, boolean imageView /*= false*/){
//
//    }
//
//    /**
//     * 触屏事件
//     * @param ref
//     * @param eventType
//     */
//    void onTouch(Ref *ref, ui::Widget::TouchEventType eventType);
//
//    /**
//     * 从Csb文件读取
//     */
//    virtual void initLayer();
//
//    /**
//     * 触屏开始
//     * @param pWidget
//     * @param pName
//     */
//    virtual void onTouchBegan(ui::Widget *pWidget, const char *pName);
//
//    /**
//     * 触屏移动
//     * @param pWidget
//     * @param pName
//     */
//    virtual void onTouchMoved(ui::Widget *pWidget, const char *pName);
//
//    /**
//     * 触屏取消
//     * @param pWidget
//     * @param pName
//     */
//    virtual void onTouchCanceled(ui::Widget *pWidget, const char *pName);
//
//    /**
//     * 触屏结束
//     * @param pWidget
//     * @param pName
//     */
//    virtual void onTouchEnded(ui::Widget *pWidget, const char *pName);
//
//    public:
//
//    /**
//     * 获取Layer
//     * @return
//     */
//    virtual Node *GetLayer() = 0;
//}
