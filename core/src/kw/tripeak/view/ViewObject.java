package kw.tripeak.view;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ViewObject extends Group {
    private String m_MainString;   //主指令
    private String m_subString;    //子指令
    private String m_pObjectData;        //数据
    private int m_iDataSize;   //数据大小
    private static String VIEW_SWITCH_MAIN_LAYER = "SwitchMainLayer"; //切换主界面
    boolean init() {
        this.m_MainString = "";
        this.m_subString = "";
        this.m_pObjectData = null;
        this.m_iDataSize = 0;
        return true;
    }

    /**
     *  主指令
     * @param subString
     *  子指令
     * @return
     */
    static ViewObject create(String subString){
        return create(VIEW_SWITCH_MAIN_LAYER, subString);
    }

    /**
     * 创建
     * @param mainString
     *  主指令
     * @param subString
     *  子指令
     * @return
     */
    public static ViewObject create(String mainString,String subString){
        ViewObject pViewObject = new ViewObject();
        pViewObject.init();
        pViewObject.m_MainString = mainString;
        pViewObject.m_subString = subString;
        return pViewObject;
    }

    /**
     * 创建
     * @param mainString
     *  主指令
     * @param subString
     *  子指令
     * @param pData
     *  数据指针
     * @param iSize
     *  数据大小
     * @return
     */
    public static ViewObject create(String mainString, String subString, String pData, int iSize){
        ViewObject pViewObject = new ViewObject();
        pViewObject.create(mainString, subString);
        pViewObject.m_pObjectData = pData;
        pViewObject.m_iDataSize = iSize;
        return pViewObject;
    }
}
