package kw.tripeak.config;

public class GameConfig {
    static GameConfig m_pGameConfig = null;
    public float m_EffectsVolume;
    private GameConfig(){}

    public void loadConfig(){

    }  //重新加载配置
    public void saveConfig(){
//        UserDefault
    } //保存配置

    /**
     * 获取游戏配置单例
     * @return
     */
    public static GameConfig getInstance(){
//        UserDefault::getInstance()->setFloatForKey("EffectsVolume", m_EffectsVolume);
        if (m_pGameConfig == null){
            m_pGameConfig = new GameConfig();
        }
        return m_pGameConfig;
    }
}
