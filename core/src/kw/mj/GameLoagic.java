package kw.mj;

import com.badlogic.gdx.utils.Array;

import kw.bean.ActionType;

public class GameLoagic {
    //用户有2张这样的牌面        碰
    public ActionType estimatePengCard(int playCard[], int cbCurrentCard){
        if (containNum(playCard,cbCurrentCard)>=2) {
            ActionType actionType = new ActionType();
            actionType.setType(2);
            actionType.setCards(new int[]{
                    cbCurrentCard,
                    cbCurrentCard,
                    cbCurrentCard
            });
            return actionType;
        }
        return null;
    }

    public int containNum(int card[],int target){
        int num = 0;
        for (int i : card) {
            if (target == i) {
                num ++ ;
            }
        }
        return num;
    }

    //用户有三张
    public ActionType estimateGangCard(int cbCardIndex[], int cbCurrentCard){
//        return ((cbCardIndex[switchToCardIndex(cbCurrentCard)] == 3) ? WIK_G : WIK_NULL);
        if (containNum(cbCardIndex,cbCurrentCard)>=3) {
            ActionType actionType = new ActionType();
            actionType.setType(1);
            actionType.setCards(new int[]{
                    cbCurrentCard,
                    cbCurrentCard,
                    cbCurrentCard,
                    cbCurrentCard
            });
            return actionType;
        }
        return null;
    } //杠牌判断


    public int getUserActionRank(int cbUserAction){
//        if ((cbUserAction & WIK_H) != 0x0) {return 3;}  //胡牌优先级
//        if ((cbUserAction & WIK_G) != 0x0) {return 2;} //杠牌优先级
//        if ((cbUserAction & WIK_P) != 0x0) {return 1;} //碰牌优先级
        return 0;
    } //动作等级

    public Array<ActionType> estimateChiCard(int[] cards, int target) {
        ActionType pre = contains(cards, target - 2, target - 1, target);
        ActionType mid = contains(cards, target - 1, target + 1, target);
        ActionType end = contains(cards, target + 2, target + 1, target);
        Array<ActionType> actionTypes = new Array<>();
        if (pre!=null){
            actionTypes.add(pre);
        }
        if (mid!=null){
            actionTypes.add(mid);
        }
        if (end!=null){
            actionTypes.add(end);
        }
        return actionTypes;
    }

    public ActionType contains(int cards[],int target1,int target2,int target){
        ActionType actionType = new ActionType();
        actionType.setType(3);
        if (target>26)return null;
        int min = 0;
        int max = 0;
        if (target>=0&&target<=8){
            min = 0;
            max = 8;
        }else if (target>=9&&target<=17){
            min = 9;
            max = 17;
        }else if (target>=18&&target<=26){
            min = 18;
            max = 26;
        }
        boolean cardOne = false;
        boolean cardTwo = false;
        for (int card : cards) {
            if (card>max){
                return null;
            }
            if (card<min){
                return null;
            }
            if (card == target1) {
                cardOne = true;
            }else if (card == target2){
                cardTwo = true;
            }
        }
        if (cardOne&&cardTwo){
            actionType.setCards(new int[]{
                    target,target1,target2
            });
            return actionType;
        }
        return null;
    }

    public void hu(){

    }
}