package kw.tripeak.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.BufferedReader;
import java.util.ArrayList;

import kw.tripeak.constant.LevelConfig;
//import ogz.tripeaks.screen.GameScreen;

public class GameData {

    public PeakBean getData(){

        PeakBean bean = new PeakBean();
        FileHandle internal = Gdx.files.internal("level/Level"+ LevelConfig.levelNum +".txt");
        BufferedReader reader = new BufferedReader(internal.reader());
        StringBuilder str = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine())!= null) {
                str.append(line);
            }
        }catch (Exception e){
            System.out.println("------------------");
        }
        String value = str.toString();
        String[] split = value.split("m_Script");
        String s1 = split[1];
        String content = s1.substring(s1.indexOf("=")+3,s1.length()-1);
        JsonValue root = new JsonReader().parse(content);
        JsonValue jsonValue = root.get("GameDocuments").get("Board").child.get("CardList");
        JsonValue gameSetting = root.get("GameSetting");
        int handCardCount = gameSetting.get("handCardCount").asInt();
        bean.setNum(handCardCount);
        JsonValue child1 = jsonValue.child;
        JsonValue temp = child1;
//        JsonValue handCardList = root.get("GameDocuments").get("HandCardList");
//        bean.setHandSuit(handSuit.asInt());
//        bean.setHandRank(handRank.asInt());
        ArrayList<Board> arrayList = new ArrayList<>();
        while (temp!= null) {
            int id = temp.get("id").asInt();
            float baseX = temp.get("x").asFloat();
            float baseY = temp.get("y").asFloat();
            float rotation = temp.get("rotation").asFloat();
            int rank = temp.get("rank").asInt();
            int suit = temp.get("suit").asInt();
            int[] covers = temp.get("covers").asIntArray();

            temp = temp.next;
            Board board = new Board();
            board.setId(id);
            board.setPosX(baseX);
            board.setPoxY(baseY);
            board.setCovers(covers);
            board.setRank(rank);
            board.setSuit(suit);
            board.setRotation(rotation);
            arrayList.add(board);
        }
        bean.setBoards(arrayList);
        return bean;
    }


}
