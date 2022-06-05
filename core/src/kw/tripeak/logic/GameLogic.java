package kw.tripeak.logic;

import java.util.ArrayList;
import static kw.tripeak.constant.Constant.INVALID_BYTE;
import static kw.tripeak.constant.Constant.MAX_COUNT;
import static kw.tripeak.constant.Constant.MAX_INDEX;
//import static kw.tripeak.constant.Constant.;


public class GameLogic {

    final int m_cbCardDataArray[] = {
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

    public static int MAX_REPERTORY = 136;                               //最大库存
    public static int MAX_WEAVE = 4;
    public static int MASK_COLOR = 0xF0;                                //花色掩码
    public static int MASK_VALUE = 0x0F;                                //数值掩码
    public static int FALSE      =  0;
    public static int TRUE       =  1;
    public static int WIK_NULL   =  0x00;        //过
    public static int WIK_P      =  0x01 ;       //碰
    public static int WIK_G      =  0x02;        //杠
    public static int WIK_H      =  0x04;        //胡
    public static int WIK_S      =  0x08;        //吃



//////////////////////////////////////////////////////////////////////////
//胡牌类型
    public static int CHR_NULL    = 0x00;    //没胡标识
    public static int CHR_PH      = 0x01;    //平胡
    public static int CHR_PPH     = 0x02;    //碰碰胡
    public static int CHR_QS      = 0x04;    //清色
    public static int CHR_DY      = 0x08;    //钓鱼
    public static int CHR_QD      = 0x10;    //七对
    public static int CHR_DH      = 0x20;    //地胡
    public static int CHR_TH      = 0x40;    //天胡


//胡牌方式
    public static int CHK_NULL   = 0x00 ;       //非胡
    public static int CHK_ZM     = 0x01 ;       //自摸
    public static int CHK_JP     = 0x02 ;       //接炮
    public static int CHK_QG     = 0x04 ;       //抢杠
    public static int CHK_GK     = 0x08 ;       //抢开

//特殊情况
    public static int CHS_NULL    = 0x00;        //无情况
    public static int CHS_DZ      = 0x01;        //单张
    public static int CHS_DH      = 0x02;        //地胡
    public static int CHS_TH      = 0x04;        //天胡
    public static int CHS_GP      = 0x08;        //有杠
    public static int CHS_KZ      = 0x10;        //卡张


    //////////////////////////////////////////////////////////////////////////
//类型子项
    class tagKindItem {
        int cbWeaveKind;                        //组合类型
        int cbCenterCard;                        //中心扑克
        int cbCardIndex[] = new int[3];                        //扑克索引
    };

    //组合子项
    public class tagWeaveItem {
        int cbWeaveKind;                        //组合类型
        int cbCenterCard;                        //中心扑克
        int cbPublicCard;                        //公开标志
        int cbProvideUser;                        //供应用户
        int cbValid;                            //有效标识
    };

    //杠牌结果
    class tagGangCardResult {
        int cbCardCount;                        //扑克数目
        int cbCardData[] = new int[MAX_WEAVE];                //扑克数据
        int cbPublic[] = new int[MAX_WEAVE];;                //公开标识
    };

    //分析子项
    class tagAnalyseItem {
        int cbCardEye;                            //牌眼扑克
        int cbWeaveKind[]  = new int[MAX_WEAVE];;                //组合类型
        int cbCenterCard[]  = new int[MAX_WEAVE];;            //中心扑克
    };

    //听牌
    class tagTingResult{
        int cbTingCount;                           //听牌数量
        int cbTingCard[] = new int[MAX_WEAVE];;                 //听的牌
    };
//////////////////////////////////////////////////////////////////////////
    //数组说明
    ArrayList<tagAnalyseItem> CAnalyseItemArray;
//    private  static int m_cbCardDataArray[] = new int[MAX_REPERTORY];    //扑克数据
    public void shuffle(int cbCardData[], int cbMaxCount){
//        double random = Math.random();
//        srand(static_cast<unsigned int>(time(NULL)));
        int []cbCardDataTemp = new int[m_cbCardDataArray.length];
//        memcpy(cbCardDataTemp, m_cbCardDataArray, sizeof(m_cbCardDataArray));
        int cbRandCount = 0, cbPosition = 0;
        do {
            cbPosition = (Math.round(1) % (cbMaxCount - cbRandCount));
            cbCardData[cbRandCount++] = cbCardDataTemp[cbPosition];
            cbCardDataTemp[cbPosition] = cbCardDataTemp[cbMaxCount - cbRandCount];
        } while (cbRandCount < cbMaxCount);
    }//洗牌
    boolean removeCard(int cbCardIndex[], int cbRemoveCard){
        int cbRemoveIndex = switchToCardIndex(cbRemoveCard);     //删除扑克
        if (cbCardIndex[cbRemoveIndex] > 0) {
            cbCardIndex[cbRemoveIndex]--;
            return true;
        }
        return false;
    }//删除扑克
    boolean removeCard(int cbCardIndex[], int cbRemoveCard[], int cbRemoveCount){

        for (int i = 0; i < cbRemoveCount; i++) {
            int cbRemoveIndex = switchToCardIndex(cbRemoveCard[i]);
            if (cbCardIndex[cbRemoveIndex] == 0) {
                for (int j = 0; j < i; j++) {
                    cbCardIndex[switchToCardIndex(cbRemoveCard[j])]++;
                }
                return false;
            } else {
                //删除扑克
                --cbCardIndex[cbRemoveIndex];
            }
        }
        return true;
    } //删除扑克
    boolean removeCard(int cbCardData[], int cbCardCount, int cbRemoveCard[], int cbRemoveCount){
        //定义变量
        int cbDeleteCount = 0, cbTempCardData[] = new int[MAX_COUNT];
        if (cbCardCount > cbTempCardData.length) return false;
//        memcpy(cbTempCardData, cbCardData, cbCardCount * sizeof(cbCardData[0]));
        for (int i = 0; i < cbRemoveCount; i++) {
            for (int j = 0; j < cbCardCount; j++) {
                if (cbRemoveCard[i] == cbTempCardData[j]) {
                    cbDeleteCount++;
                    cbTempCardData[j] = 0;
                    break;
                }
            }
        }
        //成功判断
        if (cbDeleteCount != cbRemoveCount) {
            return false;
        }
        //清理扑克
        int cbCardPos = 0;
        for (int i = 0; i < cbCardCount; i++) {
            if (cbTempCardData[i] != 0) {
                cbCardData[cbCardPos++] = cbTempCardData[i];
            }
        }
        return true;
    } //删除扑克
    boolean removeAllCard(int cbCardIndex[], int cbRemoveCard){
        int cbRemoveIndex = switchToCardIndex(cbRemoveCard);
        cbCardIndex[cbRemoveIndex] = 0;
        return true;
    }   //移除指定的牌
    //内部函数
    boolean isValidCard(int cbCardData){
        int cbValue = (cbCardData & MASK_VALUE);
        int cbColor = ((cbCardData & MASK_COLOR) >> 4);
        return (((cbValue >= 1) && (cbValue <= 9) && (cbColor <= 2)) || ((cbValue >= 1) && (cbValue <= 7) && (cbColor == 3)));
    }   //有效判断
    int switchToCardData(int cbCardIndex){
        return ((cbCardIndex / 9) << 4) | (cbCardIndex % 9 + 1);
//        return 0;
    } //扑克转换
    int switchToCardIndex(int cbCardData){
        return ((cbCardData & MASK_COLOR) >> 4) * 9 + (cbCardData & MASK_VALUE) - 1;
//        return 0;
    }  //扑克转换
    int switchToCardData(int cbCardIndex[], int cbCardData[], int bMaxCount){
        int bPosition = 0;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndex[i] != 0) {
                for (int j = 0; j < cbCardIndex[i]; j++) {
                    cbCardData[bPosition++] = switchToCardData(i);
                }
            }
        }
        return bPosition;
    } //扑克转换
    int switchToCardIndex(int cbCardData[], int cbCardCount, int cbCardIndex[]){
//        memset(cbCardIndex, 0, sizeof(int) * MAX_INDEX);
        //转换扑克
        for (int i = 0; i < cbCardCount; i++) {
            cbCardIndex[switchToCardIndex(cbCardData[i])]++;
        }
        return cbCardCount;
    } //扑克转换
    int getCardCount(int cbCardIndex[]){
        int cbCardCount = 0;
        for (int i = 0; i < MAX_INDEX; i++) {
            cbCardCount += cbCardIndex[i];
        }
        return cbCardCount;

    }  //扑克数目
    int getWeaveCard(int cbWeaveKind, int cbCenterCard, int cbCardBuffer[]) {

        if (cbWeaveKind == WIK_P)        //碰牌操作
            {
                //设置变量
                cbCardBuffer[0] = cbCenterCard;
                cbCardBuffer[1] = cbCenterCard;
                cbCardBuffer[2] = cbCenterCard;
                return 3;
            }

        if (cbWeaveKind == WIK_G)        //杠牌操作
            {
                //设置变量
                cbCardBuffer[0] = cbCenterCard;
                cbCardBuffer[1] = cbCenterCard;
                cbCardBuffer[2] = cbCenterCard;
                cbCardBuffer[3] = cbCenterCard;
                return 4;
            }
        return 0;

    }//组合扑克
    //动作判断
    public int estimatePengCard(int cbCardIndex[], int cbCurrentCard){
        return ((cbCardIndex[switchToCardIndex(cbCurrentCard)] >= 2) ? WIK_P : WIK_NULL);
    }//碰牌判断
    int estimateGangCard(int cbCardIndex[], int cbCurrentCard){
        return ((cbCardIndex[switchToCardIndex(cbCurrentCard)] == 3) ? WIK_G : WIK_NULL);
    } //杠牌判断
    //等级函数
    public int getUserActionRank(int cbUserAction){
        if ((cbUserAction & WIK_H) != 0x0) {return 3;}  //胡牌优先级
        if ((cbUserAction & WIK_G) != 0x0) {return 2;} //杠牌优先级
        if ((cbUserAction & WIK_P) != 0x0) {return 1;} //碰牌优先级
        return 0;
    } //动作等级
    int getHuFanShu( int huRight,  int huKind,  int huSpecial){
        return 1;
    } //胡牌分数
    public int analyseGangCard( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount, tagGangCardResult GangCardResult){
        //设置变量
        int cbActionMask = WIK_NULL;
//        memset(&GangCardResult, 0, sizeof(GangCardResult));
        //手上杠牌
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndex[i] == 4) {
                cbActionMask |= WIK_G;
                GangCardResult.cbPublic[GangCardResult.cbCardCount] = FALSE;
                GangCardResult.cbCardData[GangCardResult.cbCardCount++] = switchToCardData(i);
            }
        }
        //组合杠牌
        for (int i = 0; i < cbWeaveCount; i++) {
            if (WeaveItem[i].cbWeaveKind == WIK_P) {
                if (cbCardIndex[switchToCardIndex(WeaveItem[i].cbCenterCard)] == 1) {
                    cbActionMask |= WIK_G;
                    GangCardResult.cbPublic[GangCardResult.cbCardCount] = TRUE;
                    GangCardResult.cbCardData[GangCardResult.cbCardCount++] = WeaveItem[i].cbCenterCard;
                }
            }
        }
        return cbActionMask;

    } //杠牌分析
    int analyseHuCard( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount, int cbCurrentCard, int huKind, int huRight, int huSpecial,  int cbSendCardCount,  int cbOutCardCount,  boolean bGangStatus,  boolean bZimo,  boolean bQiangGangStatus, int cbFanShu,  boolean bCheck){
        //=================构造扑克开始
        int cbCardIndexTemp[] = new int[MAX_INDEX];
//        memcpy(cbCardIndexTemp, cbCardIndex, sizeof(cbCardIndexTemp));                //临时扑克，用来分析
        if (cbCurrentCard != 0) {cbCardIndexTemp[switchToCardIndex(cbCurrentCard)]++;}//14只牌
        //计算数目
        int cbCardCountTemp = getCardCount(cbCardIndexTemp);
        int cbCardCount = (cbCardCountTemp - 1);
        //=================构造扑克结束
        //分析扑克
        ArrayList<tagAnalyseItem> AnalyseItemArray = CAnalyseItemArray;
        AnalyseItemArray.clear();
        analyseCard(cbCardIndexTemp, cbCardCountTemp, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //*********************************分析胡牌方式开始**********************************************//
        //平胡
        huRight |= pingHu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //碰碰胡
        huRight |= pengPengHu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //清一色
        huRight |= qingSe(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //七对
        huRight |= qiDui(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //钓鱼
        huRight |= diaoYu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //********************************分析胡牌类型***************************************************//
        huKind |= ziMo(huRight, bGangStatus, bZimo);            //自摸
        huKind |= gangKai(huRight, bGangStatus, bZimo);         //杠开
        huKind |= qiangGang(huRight, bQiangGangStatus, bZimo);  //枪杆
        huKind |= jiePao(huRight, bQiangGangStatus, bZimo);     //接炮

        //*********************************分析特殊牌****************************************************//
        huSpecial |= gangPai(cbCardIndexTemp);                          //有杠
        huSpecial |= danZhang(cbCardCount);                             //单张牌
        huSpecial |= diHu(cbSendCardCount, cbOutCardCount);             //地胡
        huSpecial |= tianHu(cbSendCardCount, cbOutCardCount);           //天胡
        huSpecial |= kaZhang(cbCardIndex, WeaveItem, cbWeaveCount);     //卡张判定
        //************************************************************************************************//

        //结果判断
        if (huRight != 0x0) {                                                   //胡牌
        /*//有些地方麻将规则胡牌和番数有关，需要在此处理
        int cbFs = getHuFanShu(huRight, huKind, huSpecial);             //计算番数
        //如果bCheck为true，永远返回false，不存在枪杆
        if ((huKind & CHK_QG) == 0x0 && !bZimo) {                           //不是枪杆并且不是自摸才判定
            if (!bCheck && cbFs <= cbFanShu)                                //番数如果不大于这轮最大的番数则不可以胡牌，枪杆除外。
            {
                return WIK_NULL;
            }
        }
        cbFanShu = cbFs;                                                    //暂存番数
        if (cbFs < 0x2)                                                     //1分的平湖
        {
            if ((huKind & CHK_ZM) == 0x0 && (huKind & CHK_QG) == 0x0)       //麻将不能胡
            {
                return WIK_NULL;
            }
        }*/
            return WIK_H;
        }
        return WIK_NULL;

    }   //胡牌分析，返回胡牌类型
    int analyseHuCardCount( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount){
        int cbCount = 0;
        int cbCardIndexTemp[] = new int[MAX_INDEX];
//        memcpy(cbCardIndexTemp, cbCardIndex, sizeof(cbCardIndexTemp));
        for (int j = 0; j < MAX_INDEX; j++) {
            int cbCurrentCard = switchToCardData(j);
            if (analyseCanHuCard(cbCardIndexTemp, WeaveItem, cbWeaveCount, cbCurrentCard)) {
                cbCount++;
            }
        }
        return cbCount;
    }   //获取胡牌的数量
    boolean analyseCard( int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbItemCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        //效验数目
        if ((cbCardCount < 2) || (cbCardCount > MAX_COUNT) || ((cbCardCount - 2) % 3 != 0)) return false;
        //变量定义
        int cbKindItemCount = 0;
        tagKindItem KindItem[] = new tagKindItem[2 * MAX_INDEX];
//        memset(KindItem, 0, sizeof(KindItem));
        //需求判断
        int cbLessKindItem = ((cbCardCount - 2) / 3);
        //单吊判断
        if (cbLessKindItem == 0) {
            //牌眼判断
            for (int i = 0; i < MAX_INDEX; i++) {
                if (cbCardIndex[i] == 2) {
                    //变量定义
                    tagAnalyseItem AnalyseItem = new tagAnalyseItem();
//                    memset(&AnalyseItem, 0, sizeof(AnalyseItem));
                    //设置结果
                    for (int j = 0; j < cbItemCount; j++) {
                        AnalyseItem.cbWeaveKind[j] = WeaveItem[j].cbWeaveKind;
                        AnalyseItem.cbCenterCard[j] = WeaveItem[j].cbCenterCard;
                    }
                    AnalyseItem.cbCardEye = switchToCardData(i);
                    AnalyseItemArray.add(AnalyseItem);            //插入结果
                    return true;
                }
            }
            return false;
        }
        //拆分分析
        if (cbCardCount >= 3) {
            for (int i = 0; i < MAX_INDEX; i++) {
                //同牌判断
                if (cbCardIndex[i] >= 3) {
                    KindItem[cbKindItemCount].cbCenterCard = switchToCardData(i);
                    KindItem[cbKindItemCount].cbCardIndex[0] = i;
                    KindItem[cbKindItemCount].cbCardIndex[1] = i;
                    KindItem[cbKindItemCount].cbCardIndex[2] = i;
                    KindItem[cbKindItemCount++].cbWeaveKind = WIK_P;
                }
                //连牌判断
                if ((i < (MAX_INDEX - 2 - 7)) && (cbCardIndex[i] > 0) && ((i % 9) < 7)) {
                    for (int j = 1; j <= cbCardIndex[i]; j++) {
                        if ((cbCardIndex[i + 1] >= j) && (cbCardIndex[i + 2] >= j)) {
                            KindItem[cbKindItemCount].cbCenterCard = switchToCardData((i + 1));
                            KindItem[cbKindItemCount].cbCardIndex[0] = i;
                            KindItem[cbKindItemCount].cbCardIndex[1] = (i + 1);
                            KindItem[cbKindItemCount].cbCardIndex[2] = (i + 2);
                            KindItem[cbKindItemCount++].cbWeaveKind = WIK_S;
                        }
                    }
                }
            }
        }

        //组合分析
        if (cbKindItemCount >= cbLessKindItem) {
            //变量定义
            int cbCardIndexTemp[] = new int[MAX_INDEX];
//            memset(cbCardIndexTemp, 0, sizeof(cbCardIndexTemp));
            //变量定义
            int cbIndex[] = {0, 1, 2, 3};
            tagKindItem pKindItem[] = new tagKindItem[MAX_WEAVE];
//            memset(&pKindItem, 0, sizeof(pKindItem));
            //开始组合
            do {
                //设置变量
//                memcpy(cbCardIndexTemp, cbCardIndex, sizeof(cbCardIndexTemp));
                for (int i = 0; i < cbLessKindItem; i++) {
                    pKindItem[i] = KindItem[cbIndex[i]];
                }
                //数量判断
                boolean bEnoughCard = true;
                for (int i = 0; i < cbLessKindItem * 3; i++) {
                    //存在判断
                    int cbTempCardIndex = pKindItem[i / 3].cbCardIndex[i % 3];
                    if (cbCardIndexTemp[cbTempCardIndex] == 0) {
                        bEnoughCard = false;
                        break;
                    } else
                        cbCardIndexTemp[cbTempCardIndex]--;
                }


                if (bEnoughCard) {            //胡牌判断
                    int cbCardEye = 0;    //牌眼判断
                    //是否继续
                    for (int i = 0; i < MAX_INDEX; i++) {
                        if (cbCardIndexTemp[i] == 2) {
                            //眼牌数据
                            cbCardEye = switchToCardData(i);
                            //是否继续
                            break;
                        }
                    }

                    //组合类型
                    if (cbCardEye != 0) {
                        //变量定义
                        tagAnalyseItem AnalyseItem = new tagAnalyseItem();
//                        memset(&AnalyseItem, 0, size/of(AnalyseItem));
                        //设置组合
                        for (int i = 0; i < cbItemCount; i++) {
                            AnalyseItem.cbWeaveKind[i] = WeaveItem[i].cbWeaveKind;
                            AnalyseItem.cbCenterCard[i] = WeaveItem[i].cbCenterCard;
                        }
                        //设置牌型
                        for (int i = 0; i < cbLessKindItem; i++) {
                            AnalyseItem.cbWeaveKind[i + cbItemCount] = pKindItem[i].cbWeaveKind;
                            AnalyseItem.cbCenterCard[i + cbItemCount] = pKindItem[i].cbCenterCard;
                        }
                        AnalyseItem.cbCardEye = cbCardEye;      //设置牌眼
                        AnalyseItemArray.add(AnalyseItem);//插入结果
                    }
                }

                //设置索引
                if (cbIndex[cbLessKindItem - 1] == (cbKindItemCount - 1)) {
                    int i = (cbLessKindItem - 1);
                    for (; i > 0; i--) {
                        if ((cbIndex[i - 1] + 1) != cbIndex[i]) {
                            int cbNewIndex = cbIndex[i - 1];
                            for (int j = (i - 1); j < cbLessKindItem; j++)
                                cbIndex[j] = (cbNewIndex + j - i + 2);
                            break;
                        }
                    }

                    if (i == 0)
                        break;

                } else
                    cbIndex[cbLessKindItem - 1]++;

            } while (true);

        }
        return (AnalyseItemArray.size() > 0);
    }
    //分析扑克
    boolean analyseTingCard( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount){
        int cbCardIndexTemp[] = new int[MAX_INDEX];
//        memcpy(cbCardIndexTemp, cbCardIndex, sizeof(cbCardIndexTemp));
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndexTemp[i] == 0) continue;//空牌过滤
            cbCardIndexTemp[i]--;                  //假设出掉的牌
            for (int j = 0; j < MAX_INDEX; j++) {
                int cbCurrentCard = switchToCardData(j);
                if (analyseCanHuCard(cbCardIndexTemp, WeaveItem, cbWeaveCount, cbCurrentCard)) {
                    return true;
                }
            }
            cbCardIndexTemp[i]++;                 //还原假设的牌
        }
        return false;
    }    //是否听牌
    boolean analyseCanHuCard( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount, int cbCurrentCard){
        int cbCardIndexTemp[] = new int[MAX_INDEX];
//        memcpy(cbCardIndexTemp, cbCardIndex, sizeof(cbCardIndexTemp));                //临时扑克，用来分析
        if (cbCurrentCard != 0) {cbCardIndexTemp[switchToCardIndex(cbCurrentCard)]++;}//14只牌
        //计算数目
        int cbCardCountTemp = getCardCount(cbCardIndexTemp);
        int cbCardCount = (cbCardCountTemp - 1);
        //分析扑克
        ArrayList<tagAnalyseItem> AnalyseItemArray = new ArrayList<>();
        AnalyseItemArray.clear();
        analyseCard(cbCardIndexTemp, cbCardCountTemp, WeaveItem, cbWeaveCount, AnalyseItemArray);
        //是否能胡牌
        return canHu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray);
    }
    //分析是否可以胡牌
    boolean analyseTingCardResult( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount , tagTingResult tingResult){
        int cbCardIndexTemp[] = new int[MAX_INDEX];
        for (int j = 0; j < MAX_INDEX; j++) {
            int cbCurrentCard = switchToCardData(j);
            if (analyseCanHuCard(cbCardIndexTemp, WeaveItem, cbWeaveCount, cbCurrentCard)) {
                tingResult.cbTingCard[tingResult.cbTingCount++] = cbCurrentCard;
            }
        }
        return tingResult.cbTingCount > 0;
    }
    boolean canHu( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        if (cbWeaveCount == 0) {    //计算七对，	//不存在碰、杠
            int cbDuiCount = 0;
            for (int i = 0; i < MAX_INDEX; i++) {
                if (cbCardIndexTemp[i] == 2) //牌的数量存在奇数就是不是七对，否则是
                {
                    cbDuiCount++;
                }
            }
            if (cbDuiCount == 7) {
                return true;
            }
        }
        return AnalyseItemArray.size() > 0;
    } //能胡牌
    //胡牌类型
    int pingHu( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        if (AnalyseItemArray.size() > 0) {
            for (int i = 0; i < AnalyseItemArray.size(); i++) {
                boolean bLianCard = false, bPengCard = false;
                tagAnalyseItem pAnalyseItem = AnalyseItemArray.get(i);
                for (int j = 0; j < pAnalyseItem.cbWeaveKind.length; j++) {
                    int cbWeaveKind = pAnalyseItem.cbWeaveKind[j];
                    bPengCard = ((cbWeaveKind & (WIK_G | WIK_P)) != 0) ? true : bPengCard;
                    bLianCard = ((cbWeaveKind & (WIK_S)) != 0) ? true : bLianCard;
                }
                if (bLianCard == true) {//平胡
                    return CHR_PH;
                }
            }
        }
        return CHR_NULL;
    } //平胡
    int qingSe( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        int cbCardColor = 0xFF;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndexTemp[i] != 0) {
                int cbTempCardColor = (switchToCardData(i) & MASK_COLOR);

                if (cbCardColor == 0xFF) {//花色判断
                    cbCardColor = cbTempCardColor;
                }
                if (cbTempCardColor != cbCardColor) {
                    return CHR_NULL;
                }
            }
        }
        //组合判断
        for (int i = 0; i < cbWeaveCount; i++) {
            int cbCenterCard = WeaveItem[i].cbCenterCard;
            if ((cbCenterCard & MASK_COLOR) != cbCardColor) return CHR_NULL;
        }
        //对号判定
        if (canHu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray)) {
            return CHR_QS;
        }
        return CHR_NULL;
} //清一色
    int pengPengHu( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        //胡牌分析
        if (AnalyseItemArray.size() > 0) {
            //牌型分析
            for (int i = 0; i < AnalyseItemArray.size(); i++) {
                //变量定义
                boolean bLianCard = false, bPengCard = false;
                tagAnalyseItem pAnalyseItem = AnalyseItemArray.get(i);
                //牌型分析
                for (int j = 0; j < pAnalyseItem.cbWeaveKind.length; j++) {
                    int cbWeaveKind = pAnalyseItem.cbWeaveKind[j];
                    bPengCard = ((cbWeaveKind & (WIK_G | WIK_P)) != 0) ? true : bPengCard;
                    bLianCard = ((cbWeaveKind & (WIK_S)) != 0) ? true : bLianCard;
                }
                //碰碰胡
                if (!bLianCard && bPengCard) {
                    return CHR_PPH;
                }
            }
        }
        return CHR_NULL;
    }//碰碰胡
    int qiDui( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        //不存在碰、杠
        if (cbWeaveCount > 0) return CHR_NULL;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndexTemp[i] == 1 || cbCardIndexTemp[i] == 3) //牌的数量存在奇数就是不是七对，否则是
            {
                return CHR_NULL;
            }
        }
        return CHR_QD;
    }//七对
    int diaoYu( int cbCardIndexTemp[],  int cbCardCountTemp,  int cbCardIndex[],  int cbCardCount, tagWeaveItem WeaveItem[], int cbWeaveCount, ArrayList<tagAnalyseItem> AnalyseItemArray){
        //碰碰胡
        if (pengPengHu(cbCardIndexTemp, cbCardCountTemp, cbCardIndex, cbCardCount, WeaveItem, cbWeaveCount, AnalyseItemArray) == CHR_PPH
                && (cbCardCountTemp == 2)) {
            return CHR_DY;
        }
        return CHR_NULL;
    }//单钓
     //一些判断
    public int gangPai( int cbCardIndex[]){
        int cbCardColor = 0xFF;
        for (int i = 0; i < MAX_INDEX; i++) {
            if (cbCardIndex[i] == 4) {
                return CHS_GP;
            }
        }
        return CHS_NULL;
    }    //手上有杠
    int danZhang( int cbCardCount){
        if (cbCardCount == 2) {
            return CHS_DZ;
        }
        return CHS_NULL;

    }    //手上剩下一张牌
    int tianHu( int m_cbSendCardCount,  int m_cbOutCardCount){
        if ((m_cbSendCardCount == 1) && (m_cbOutCardCount == 0)) {
            return CHS_TH;
        }
        return CHS_NULL;
    }
    //天胡
    int diHu( int m_cbSendCardCount,  int m_cbOutCardCount){
        if ((m_cbSendCardCount == 1) && (m_cbOutCardCount == 1)) {
            return CHS_DH;
        }
        return CHS_NULL;
    }//地胡
    int kaZhang( int cbCardIndex[], tagWeaveItem WeaveItem[], int cbWeaveCount){
        if (analyseHuCardCount(cbCardIndex, WeaveItem, cbWeaveCount) == 1) {
            return CHS_KZ;
        }
        return CHS_NULL;
    }  //卡张
    private int ziMo(int llHuRight,  boolean bGangStatus,  boolean bZimo){
        if ((llHuRight != 0x0) && bZimo == true && bGangStatus == false) {
            return CHK_ZM;
        }
        return CHK_NULL;
    }//自摸
    int gangKai(int llHuRight,  boolean bGangStatus,  boolean bZimo){
        if ((llHuRight != 0x0) && bZimo == true && bGangStatus == true) {
            return CHK_GK;
        }
        return CHK_NULL;
    }//杠开
    int qiangGang(int llHuRight,  boolean bGangStatus,  boolean bZimo){
        if ((llHuRight != 0x0) && bZimo == false && bGangStatus == true) {
            return CHK_QG;
        }
        return CHK_NULL;
    }
    //枪杠
    int jiePao(int llHuRight,  boolean bGangStatus,  boolean bZimo){
        if ((llHuRight != 0x0) && bZimo == false && bGangStatus == false) {
            return CHK_JP;
        }
        return CHK_NULL;
    }
//接炮
}
