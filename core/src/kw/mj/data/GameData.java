package kw.mj.data;

public class GameData {
    private int shufflePai[];//shuffle card
    private int shufferIndex;
    private final int allCardData[] = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,                        //筒子
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,                        //筒子
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,                        //筒子
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,                        //筒子
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,                        //万子
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,                        //万子
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,                        //万子
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,                        //万子
            0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29,                        //条子
            0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29,                        //条子
            0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29,                        //条子
            0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29,                        //条子
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,                                    //番子
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,                                    //番子
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,                                    //番子
            0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,                                    //番子
    };
    private int userCard[][] = new int[4][13];

    public int[] getAllCardData() {
        return allCardData;
    }

    public int touShaizi(){
        return (int) (Math.random() % 6 + 1 + Math.random() % 6 + 1);    //骰子点数
    }

    /**
     * 洗牌
     */
    public void shuffle(){
        shufferIndex = 0;
        shufflePai = new int[136];
        int []cbCardDataTemp = new int[allCardData.length];
        int index = 0;
        for (int i : allCardData) {
            cbCardDataTemp[index++] = i;
        }
        int cbRandCount = 0, cbPosition = 0;
        do {
            cbPosition = (Math.round(1) % (136 - cbRandCount));
            shufflePai[cbRandCount++] = cbCardDataTemp[cbPosition];
            cbCardDataTemp[cbPosition] = cbCardDataTemp[136 - cbRandCount];
        } while (cbRandCount < 136);
    }

    public void zhuaPai() {
        //抓的方式是  4 4 4 1  现在就先不管，直接从头开始，之后在修改投色子计算位置
        int index = 0;
        for (int i1 = 0; i1 < 3; i1++) {
            index++;
            for (int i = 0; i < 4; i++) {
                index = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    userCard[i][i1 * 4 + index++] = shufflePai[shufferIndex++];
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            userCard[i][12] = shufflePai[shufferIndex++];
        }
    }

    public int[][] getUserCard() {
        return userCard;
    }

    public int getNextCard() {
        return shufflePai[shufferIndex++];
    }
}
