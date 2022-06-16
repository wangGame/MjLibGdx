package majiang_algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjzhaoxin on 2017/12/4.
 */
public class HuUtil {
    public static boolean isHu(List<Integer> input, int guiCard) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }
        int guiNum = cards.get(guiCard - 1);
        cards.set(guiCard - 1, 0);
        return isHuCard(cards, guiNum);
    }

    public static boolean isHuExtra(List<Integer> input, List<Integer> guiCard, int extra) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }

        int guiNum = 0;
        for (int gui : guiCard) {
            guiNum += cards.get(gui - 1);
            cards.set(gui - 1, 0);
        }

        if (extra != 0) {
            cards.set(extra - 1, cards.get(extra - 1) + 1);
        }

        return isHuCard(cards, guiNum);
    }

    public static boolean isHuCard(List<Integer> cards, int guiNum) {
        long wan_key = 0;
        long tong_key = 0;
        long tiao_key = 0;
        long feng_key = 0;
        long jian_key = 0;

        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.WAN9; i++) {
            int num = cards.get(i - 1);
            wan_key = wan_key * 10 + num;
        }
        for (int i = MaJiangDef.TONG1; i <= MaJiangDef.TONG9; i++) {
            int num = cards.get(i - 1);
            tong_key = tong_key * 10 + num;
        }
        for (int i = MaJiangDef.TIAO1; i <= MaJiangDef.TIAO9; i++) {
            int num = cards.get(i - 1);
            tiao_key = tiao_key * 10 + num;
        }
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.FENG_BEI; i++) {
            int num = cards.get(i - 1);
            feng_key = feng_key * 10 + num;
        }
        for (int i = MaJiangDef.JIAN_ZHONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            jian_key = jian_key * 10 + num;
        }

        List<List<HuTableInfo>> tmp = new ArrayList<>();
        if (wan_key != 0) {
            List<HuTableInfo> wanHuTableInfo = HuTable.table.get(wan_key);
            tmp.add(wanHuTableInfo);
        }
        if (tong_key != 0) {
            List<HuTableInfo> tongHuTableInfo = HuTable.table.get(tong_key);
            tmp.add(tongHuTableInfo);
        }
        if (tiao_key != 0) {
            List<HuTableInfo> tiaoHuTableInfo = HuTable.table.get(tiao_key);
            tmp.add(tiaoHuTableInfo);
        }
        if (feng_key != 0) {
            List<HuTableInfo> fengHuTableInfo = HuTableFeng.table.get(feng_key);
            tmp.add(fengHuTableInfo);
        }
        if (jian_key != 0) {
            List<HuTableInfo> jianHuTableInfo = HuTableJian.table.get(jian_key);
            tmp.add(jianHuTableInfo);
        }

        List<List<HuTableInfo>> tmp1 = new ArrayList<>();
        for (List<HuTableInfo> huTableInfos : tmp) {
            if (huTableInfos == null) {
                return false;
            }
            List<HuTableInfo> tmp2 = new ArrayList<>();
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    tmp2.add(huTableInfo);
                }
            }
            if (tmp2.isEmpty()) {
                return false;
            }
            tmp1.add(tmp2);
        }

        return isHuTableInfo(tmp1, 0, guiNum, false);
    }

    private static boolean isHuTableInfo(List<List<HuTableInfo>> tmp, int index, int guiNum, boolean jiang) {
        if (index >= tmp.size()) {
            return (guiNum % 3 == 0 && jiang == true) || (guiNum % 3 == 2 && jiang == false);
        }
        List<HuTableInfo> huTableInfos = tmp.get(index);
        for (HuTableInfo huTableInfo : huTableInfos) {
            if (jiang) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum && huTableInfo.jiang == false) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, jiang)) {
                        return true;
                    }
                }
            } else {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, huTableInfo.jiang)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> isTing(List<Integer> input, int guiCard) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }
        int guiNum = cards.get(guiCard - 1);
        cards.set(guiCard - 1, 0);

        return isTingCard(cards, guiNum);
    }

    public static List<Integer> isTingExtra(List<Integer> input, List<Integer> guiCard) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }

        int guiNum = 0;
        for (int gui : guiCard) {
            guiNum += cards.get(gui - 1);
            cards.set(gui - 1, 0);
        }

        return isTingCard(cards, guiNum);
    }

    public static List<Integer> isTingCard(List<Integer> cards, int guiNum) {
        long wan_key = 0;
        long tong_key = 0;
        long tiao_key = 0;
        long feng_key = 0;
        long jian_key = 0;

        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.WAN9; i++) {
            int num = cards.get(i - 1);
            wan_key = wan_key * 10 + num;
        }
        for (int i = MaJiangDef.TONG1; i <= MaJiangDef.TONG9; i++) {
            int num = cards.get(i - 1);
            tong_key = tong_key * 10 + num;
        }
        for (int i = MaJiangDef.TIAO1; i <= MaJiangDef.TIAO9; i++) {
            int num = cards.get(i - 1);
            tiao_key = tiao_key * 10 + num;
        }
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.FENG_BEI; i++) {
            int num = cards.get(i - 1);
            feng_key = feng_key * 10 + num;
        }
        for (int i = MaJiangDef.JIAN_ZHONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            jian_key = jian_key * 10 + num;
        }

        List<Integer> tmpType = new ArrayList<>();
        List<List<HuTableInfo>> tmpTing = new ArrayList<>();
        List<List<HuTableInfo>> tmp = new ArrayList<>();

        List<HuTableInfo> wanHuTableInfo = HuTable.table.get(wan_key);
        if (wanHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(wanHuTableInfo);
        if (wan_key != 0) {
            tmpType.add(MaJiangDef.TYPE_WAN);
            tmp.add(wanHuTableInfo);
        }
        List<HuTableInfo> tongHuTableInfo = HuTable.table.get(tong_key);
        if (tongHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(tongHuTableInfo);
        if (tong_key != 0) {
            tmpType.add(MaJiangDef.TYPE_TONG);
            tmp.add(tongHuTableInfo);
        }
        List<HuTableInfo> tiaoHuTableInfo = HuTable.table.get(tiao_key);
        if (tiaoHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(tiaoHuTableInfo);
        if (tiao_key != 0) {
            tmpType.add(MaJiangDef.TYPE_TIAO);
            tmp.add(tiaoHuTableInfo);
        }
        List<HuTableInfo> fengHuTableInfo = HuTableFeng.table.get(feng_key);
        if (fengHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(fengHuTableInfo);
        if (feng_key != 0) {
            tmpType.add(MaJiangDef.TYPE_FENG);
            tmp.add(fengHuTableInfo);
        }
        List<HuTableInfo> jianHuTableInfo = HuTableJian.table.get(jian_key);
        if (jianHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(jianHuTableInfo);
        if (jian_key != 0) {
            tmpType.add(MaJiangDef.TYPE_JIAN);
            tmp.add(jianHuTableInfo);
        }

        List<Integer> ret = new ArrayList<>();
        for (int type = MaJiangDef.TYPE_WAN; type <= MaJiangDef.TYPE_JIAN; type++) {
            List<HuTableInfo> huTableInfos = tmpTing.get(type - 1);
            int[] cache = new int[9];
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai != null && huTableInfo.needGui <= guiNum) {
                    boolean cached = true;
                    for (int j = 0; j < huTableInfo.hupai.length; j++) {
                        if (huTableInfo.hupai[j] > 0 && cache[j] == 0) {
                            cached = false;
                            break;
                        }
                    }

                    if (!cached && isTingHuTableInfo(tmpType, tmp, 0, guiNum - huTableInfo.needGui, huTableInfo.jiang,
                            type)) {
                        for (int j = 0; j < huTableInfo.hupai.length; j++) {
                            if (huTableInfo.hupai[j] > 0) {
                                if (cache[j] == 0) {
                                    ret.add(MaJiangDef.toCard(type, j));
                                }
                                cache[j]++;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static boolean isTingHuTableInfo(List<Integer> tmpType, List<List<HuTableInfo>> tmp, int index, int guiNum,
                                             boolean jiang, int tingType) {
        if (index >= tmp.size()) {
            return guiNum == 0 && jiang == true;
        }
        if (tmpType.get(index) == tingType) {
            return isTingHuTableInfo(tmpType, tmp, index + 1, guiNum, jiang, tingType);
        }
        List<HuTableInfo> huTableInfos = tmp.get(index);
        for (HuTableInfo huTableInfo : huTableInfos) {
            if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                if (jiang) {
                    if (huTableInfo.jiang == false) {
                        if (isTingHuTableInfo(tmpType, tmp, index + 1, guiNum - huTableInfo.needGui, jiang, tingType)) {
                            return true;
                        }
                    }
                } else {
                    if (isTingHuTableInfo(tmpType, tmp, index + 1, guiNum - huTableInfo.needGui, huTableInfo.jiang,
                            tingType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void gen() {
        HuTableJian.gen();
        HuTableFeng.gen();
        HuTable.gen();
    }

    public synchronized static void load() {
        HuTableJian.load();
        HuTableFeng.load();
        HuTable.load();
    }

    public static void testHu() {
        String init = "1万,1万";
        String gui = "1万";
        List<Integer> cards = MaJiangDef.stringToCards(init);
        System.out.println(HuUtil.isHu(cards, MaJiangDef.stringToCard(gui)));
    }

    public static void testTing() {
        String init = "1万,1万,1筒,3筒,2筒,2条,3条,4条,东,东";
        String gui = "1筒";
        List<Integer> cards = MaJiangDef.stringToCards(init);
        System.out.println(MaJiangDef.cardsToString(HuUtil.isTing(cards, MaJiangDef.stringToCard(gui))));
        System.out.println(MaJiangDef.cardsToString(HuUtil.isTingExtra(cards, MaJiangDef.stringToCards(gui))));
    }

    public static void main(String[] args) {
        // 需要生成文件时 加上gen()
        //gen();
        load();
        testHu();
        testTing();
    }
}
