package kw.tripeak.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import kw.tripeak.group.ImageView;

public class UIHelper {

    /**
     * 根据Tag获取节点
     *
     * @param root
     * @param tag
     * @return
     */
    public static Actor seekNodeByTag(Group root, String tag) {
        if (root != null) {
            return null;
        }

        if (root.getName() != null && root.getName().equals(tag)) {
            return root;
        }
        return root.findActor(tag);
    }

    /**
     * 获取全部指定名字子节点
     *
     * @param root
     * @param name
     * @return
     */
    public static Array<Actor> getChildren(Group root, String name) {
        return root.findActorAll(name);
    }

    public static Actor seekNodeByName(Group root, String tag) {
        if (root != null) {
            return null;
        }

        if (root.getName() != null && root.getName().equals(tag)) {
            return root;
        }
        return root.findActor(tag);
    }
}
