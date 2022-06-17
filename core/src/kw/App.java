package kw;

import kw.mj.data.GameData;

public class App {
    public static void main(String[] args) {
        GameData data = new GameData();
        int[] allCardData = data.getAllCardData();
        for (int allCardDatum : allCardData) {
            System.out.println(witchToCardIndex(allCardDatum));
        }
    }

    static int MASK_COLOR = 0xF0;                                //花色掩码
    static int MASK_VALUE = 0x0F;                                //数值掩码

    static int witchToCardIndex(int cbCardData) {
        return ((cbCardData & MASK_COLOR) >> 4) * 9 + (cbCardData & MASK_VALUE) - 1;
    }
}
