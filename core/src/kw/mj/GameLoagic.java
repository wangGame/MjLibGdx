package kw.mj;

public class GameLoagic {
    //用户有2张这样的牌面        碰
    public int estimatePengCard(int playCard[], int cbCurrentCard){

        return 0;
    }


    //用户有三张
    public int estimateGangCard(int cbCardIndex[], int cbCurrentCard){
//        return ((cbCardIndex[switchToCardIndex(cbCurrentCard)] == 3) ? WIK_G : WIK_NULL);
        return 0;
    } //杠牌判断


    public int getUserActionRank(int cbUserAction){
//        if ((cbUserAction & WIK_H) != 0x0) {return 3;}  //胡牌优先级
//        if ((cbUserAction & WIK_G) != 0x0) {return 2;} //杠牌优先级
//        if ((cbUserAction & WIK_P) != 0x0) {return 1;} //碰牌优先级
        return 0;
    } //动作等级
}