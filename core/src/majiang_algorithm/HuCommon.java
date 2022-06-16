package majiang_algorithm;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HuCommon {
    public static ConcurrentHashMap<Long, List<HuTableInfo>> table;
    public static int N;
    public static String NAME;
    public static String[] CARD;
    public static boolean huLian;

    public static void main(String[] args) {
        HuCommon.table = new ConcurrentHashMap<>();
        HuCommon.N = 9;
        HuCommon.NAME = "normal";
        HuCommon.CARD = HuTable.names;
        HuCommon.huLian = true;
        check_hu(333000004);
    }

    public static void gen() {
        final HashSet<Long> card = new HashSet<>();

        for (int i = 0; i <= 14; i++) {
            int[] num = new int[N];
            gen_card(card, num, 0, i);
        }

        System.out.println(card.size());

        try {
            File file = new File("majiang_clien_" + NAME + ".txt");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            final FileOutputStream out = new FileOutputStream(file, true);

            File file1 = new File("majiang_server_" + NAME + ".txt");
            if (file1.exists()) {
                file1.delete();
            }
            file1.createNewFile();
            final FileOutputStream out1 = new FileOutputStream(file1, true);

            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:majiang.db");
            Statement stmt = c.createStatement();
            stmt.executeUpdate(("drop table if exists " + NAME + "; CREATE TABLE [" + NAME
                    + "] (  [card] INT,   [gui] INT,   [jiang] INT,   [hu] INT);\n\n CREATE INDEX [card" + NAME
                    + "] ON [" + NAME + "](   [card]);\n"));
            stmt.executeUpdate("BEGIN;");

            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);

            final long begin = System.currentTimeMillis();
            final AtomicInteger i = new AtomicInteger(0);
            for (final long l : card) {
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            check_hu(l);
                            output(l, out);
                            output_server(l, out1);
                            output_sql(l, stmt);

                            i.addAndGet(1);
                            long now = System.currentTimeMillis();
                            float per = (float) (now - begin) / i.intValue();
                            synchronized (HuCommon.class) {
                                System.out.println((float) i.intValue() / card.size() + " 需要"
                                        + per * (card.size() - i.intValue()) / 60 / 1000 + "分" + " 用时"
                                        + (now - begin) / 60 / 1000 + "分" + " 速度"
                                        + i.intValue() / ((float) (now - begin) / 1000) + "条/秒");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            fixedThreadPool.shutdown();
            while (!fixedThreadPool.isTerminated()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            out.close();
            out1.close();

            stmt.executeUpdate("COMMIT;");
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void output_server(long card, FileOutputStream out) throws Exception {
        long key = card;

        List<HuTableInfo> huTableInfos = table.get(card);
        if (!huTableInfos.isEmpty()) {
            for (HuTableInfo huTableInfo : huTableInfos) {
                String str = key + " ";
                str += huTableInfo.needGui + " ";
                str += huTableInfo.jiang ? "1 " : "0 ";
                if (huTableInfo.hupai == null) {
                    str += "-1";
                } else {
                    int hu = 0;
                    for (int i : huTableInfo.hupai) {
                        hu = hu * 10 + i;
                    }
                    str += hu + "";
                }
                str += " ";
                str += show_card(key) + " ";
                str += "鬼" + huTableInfo.needGui + " ";
                str += huTableInfo.jiang ? "有将 " : "无将 ";

                if (huTableInfo.hupai == null) {
                    str += "胡了";
                } else {
                    int index = 1;
                    for (int i : huTableInfo.hupai) {
                        if (i > 0) {
                            str += "胡" + CARD[index - 1];
                        }
                        index++;
                    }
                }

                str += "\n";
                synchronized (HuCommon.class) {
                    out.write(str.toString().getBytes("utf-8"));
                }
            }
        }
    }

    private static void output_sql(long card, Statement stmt) throws Exception {
        long key = card;

        List<HuTableInfo> huTableInfos = table.get(card);
        if (!huTableInfos.isEmpty()) {
            for (HuTableInfo huTableInfo : huTableInfos) {
                String str = "INSERT INTO " + NAME + "( card, gui, jiang, hu) VALUES (" + key + ", ";
                str += huTableInfo.needGui + ", ";
                str += huTableInfo.jiang ? "1, " : "0, ";
                if (huTableInfo.hupai == null) {
                    str += "-1";
                } else {
                    int hu = 0;
                    for (int i : huTableInfo.hupai) {
                        hu = hu * 10 + i;
                    }
                    str += hu + "";
                }
                str += ");\n";
                synchronized (HuCommon.class) {
                    stmt.execute(str);
                }
            }
        }
    }

    private static void output(long card, FileOutputStream out) throws Exception {
        long key = card;

        List<HuTableInfo> huTableInfos = table.get(card);
        if (!huTableInfos.isEmpty()) {
            for (HuTableInfo huTableInfo : huTableInfos) {
                String str = key + " ";
                str += huTableInfo.needGui + " ";
                str += huTableInfo.jiang ? "1 " : "0 ";
                if (huTableInfo.hupai == null) {
                    str += "-1";
                } else {
                    int hu = 0;
                    for (int i : huTableInfo.hupai) {
                        hu = hu * 10 + i;
                    }
                    str += hu + "";
                }
                str += "\n";
                synchronized (HuCommon.class) {
                    out.write(str.toString().getBytes("utf-8"));
                }
            }
        }
    }

    public static void check_hu(long card) {
        int[] num = new int[N];
        long tmp = card;
        for (int i = 0; i < N; i++) {
            num[N - 1 - i] = (int) (tmp % 10);
            tmp = tmp / 10;
        }

        int total = 0;
        for (int i = 0; i < N; i++) {
            total += num[i];
        }

        HashSet<HuInfo> huInfos = new HashSet<>();

        for (int guinum = 0; guinum <= 8 && total + guinum <= 14; guinum++) {
            int[] tmpnum = new int[N];
            HashSet<Long> tmpcard = new HashSet<>();
            gen_card(tmpcard, tmpnum, 0, guinum);

            for (long tmpgui : tmpcard) {
                int[] tmpguinum = new int[N];
                long tt = tmpgui;
                for (int i = 0; i < N; i++) {
                    tmpguinum[N - 1 - i] = (int) (tt % 10);
                    tt = tt / 10;
                }

                boolean max = false;
                for (int i = 0; i < N; i++) {
                    num[i] += tmpguinum[i];
                    if (num[i] > 4) {
                        max = true;
                    }
                }

                if (!max) {
                    check_hu(huInfos, num, -1, -1, guinum);
                }
                for (int i = 0; i < N && !max; i++) {
                    num[i]++;
                    if (num[i] <= 4) {
                        check_hu(huInfos, num, -1, i, guinum);
                    }
                    num[i]--;
                }

                for (int i = 0; i < N; i++) {
                    num[i] -= tmpguinum[i];
                }
            }
        }

        HashMap<Integer, HuTableInfo> huTableInfos = new HashMap<>();
        for (HuInfo huInfo : huInfos) {
            int key = huInfo.needGui * 10 + (huInfo.jiang != -1 ? 1 : 0);

            if (huTableInfos.get(key) != null) {
                if (huInfo.hupai == -1) {
                    huTableInfos.get(key).hupai = null;
                }
                if (huTableInfos.get(key).hupai != null) {
                    if (huTableInfos.get(key).hupai[huInfo.hupai] == 0) {
                        huTableInfos.get(key).hupai[huInfo.hupai]++;
                    }
                }
            } else {
                HuTableInfo huTableInfo = new HuTableInfo();
                huTableInfo.needGui = huInfo.needGui;
                huTableInfo.jiang = huInfo.jiang != -1;
                huTableInfo.hupai = new byte[N];
                if (huInfo.hupai == -1) {
                    huTableInfo.hupai = null;
                } else {
                    huTableInfo.hupai[huInfo.hupai]++;
                }
                huTableInfos.put(key, huTableInfo);
            }
        }

        List<HuTableInfo> tmphu = new ArrayList<>();
        tmphu.addAll(huTableInfos.values());
        table.put(card, tmphu);
    }

    public static void check_hu(HashSet<HuInfo> huInfos, int[] num, int jiang, int in, int gui) {
        if (huLian) {
            for (int i = 0; i < N; i++) {
                if (num[i] > 0 && i + 1 < N && num[i + 1] > 0 && i + 2 < N && num[i + 2] > 0) {
                    num[i]--;
                    num[i + 1]--;
                    num[i + 2]--;
                    check_hu(huInfos, num, jiang, in, gui);
                    num[i]++;
                    num[i + 1]++;
                    num[i + 2]++;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            if (num[i] >= 2 && jiang == -1) {
                num[i] -= 2;
                check_hu(huInfos, num, i, in, gui);
                num[i] += 2;
            }
        }

        for (int i = 0; i < N; i++) {
            if (num[i] >= 3) {
                num[i] -= 3;
                check_hu(huInfos, num, jiang, in, gui);
                num[i] += 3;
            }
        }

        for (int i = 0; i < N; i++) {
            if (num[i] != 0) {
                return;
            }
        }

        HuInfo huInfo = new HuInfo();
        huInfo.hupai = (byte) in;
        huInfo.jiang = (byte) jiang;
        huInfo.needGui = (byte) gui;
        huInfos.add(huInfo);
    }

    private static void gen_card(HashSet<Long> card, int num[], int index, int total) {
        if (index == N - 1) {
            if (total > 4) {
                return;
            }
            num[index] = total;

            long ret = 0;
            for (int c : num) {
                ret = ret * 10 + c;
            }
            card.add(ret);
            return;
        }
        for (int i = 0; i <= 4; i++) {
            if (i <= total) {
                num[index] = i;
            } else {
                num[index] = 0;
            }
            gen_card(card, num, index + 1, total - num[index]);
        }
    }

    public static String show_card(long card) {
        int[] num = new int[N];
        long tmp = card;
        for (int i = 0; i < N; i++) {
            num[N - 1 - i] = (int) (tmp % 10);
            tmp = tmp / 10;
        }
        String ret = "";
        int index = 1;
        for (int i : num) {
            String str1 = CARD[index - 1];
            for (int j = 0; j < i; j++) {
                ret += str1 + "";
            }
            index++;
        }
        return ret;
    }

    public static void load() {
        try {
            FileInputStream inputStream = new FileInputStream("majiang_clien_" + NAME + ".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> lines = new ArrayList<>();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                lines.add(str);
            }
            load(lines);

            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load(List<String> lines) {
        int total = 0;
        try {
            for (String str : lines) {
                String[] strs = str.split(" ");
                long key = Long.parseLong(strs[0]);
                int gui = Integer.parseInt(strs[1]);
                int jiang = Integer.parseInt(strs[2]);
                int hu = Integer.parseInt(strs[3]);

                List<HuTableInfo> huTableInfos = table.get(key);
                if (huTableInfos == null) {
                    huTableInfos = new ArrayList<>();
                    table.put(key, huTableInfos);
                }

                byte[] num = new byte[N];
                long tmp = hu;
                for (int i = 0; i < N; i++) {
                    num[N - 1 - i] = (byte) (tmp % 10);
                    tmp = tmp / 10;
                }
                HuTableInfo huTableInfo = new HuTableInfo();
                huTableInfo.needGui = (byte) gui;
                huTableInfo.jiang = jiang != 0;
                huTableInfo.hupai = hu == -1 ? null : num;
                huTableInfos.add(huTableInfo);
                total++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
