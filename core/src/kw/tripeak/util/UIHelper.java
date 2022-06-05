package kw.tripeak.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class UIHelper {

    public Actor seekNodeByTag(Group group, String name){
        return null;
    }
/**
 * 根据Tag获取节点
 * @param root
 * @param tag
 * @return
 */
//    Node *UIHelper::seekNodeByTag(Node *root, int tag) {
//        if (!root) {
//            return nullptr;
//        }
//        if (root->getTag() == tag) {
//            return root;
//        }
//    const auto &arrayRootChildren = root->getChildren();
//        ssize_t length = arrayRootChildren.size();
//        for (ssize_t i = 0; i < length; i++) {
//            Node *child = dynamic_cast<Node *>(arrayRootChildren.at(i));
//            if (child) {
//                Node *res = seekNodeByTag(child, tag);
//                if (res != nullptr) {
//                    return res;
//                }
//            }
//        }
//        return nullptr;
//    }

}
