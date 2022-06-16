package kw.mj;

public class GameLoagic {
    //用户有2张这样的牌面        碰
    public boolean estimatePengCard(int playCard[], int cbCurrentCard){
        return containNum(playCard,cbCurrentCard)>=2;
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
    public boolean estimateGangCard(int cbCardIndex[], int cbCurrentCard){
//        return ((cbCardIndex[switchToCardIndex(cbCurrentCard)] == 3) ? WIK_G : WIK_NULL);
        return containNum(cbCardIndex,cbCurrentCard)>=3;
    } //杠牌判断


    public int getUserActionRank(int cbUserAction){
//        if ((cbUserAction & WIK_H) != 0x0) {return 3;}  //胡牌优先级
//        if ((cbUserAction & WIK_G) != 0x0) {return 2;} //杠牌优先级
//        if ((cbUserAction & WIK_P) != 0x0) {return 1;} //碰牌优先级
        return 0;
    } //动作等级

    public boolean estimateChiCard(int[] cards, int target) {
        return contains(cards,target-2,target-1,target) ||
                contains(cards,target-1,target+1,target)||
                contains(cards,target+2,target+1,target);
    }

    public boolean contains(int cards[],int target1,int target2,int target){
        if (target>26)return false;
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
                return false;
            }
            if (card<min){
                return false;
            }
            if (card == target1) {
                cardOne = true;
            }else if (card == target2){
                cardTwo = true;
            }
        }
        return cardOne&&cardTwo;
    }

    public void hu(){

    }
}