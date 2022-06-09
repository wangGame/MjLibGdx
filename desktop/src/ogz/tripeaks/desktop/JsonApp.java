package ogz.tripeaks.desktop;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class JsonApp {
    public static void main(String[] args) {
        try {
            method2();

        }catch (Exception e){
        }
    }

    public static void method2()throws Exception {
        BufferedReader bufferedReader
                = new BufferedReader(
                new FileReader(new File("level/Level20.txt")));
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine())!= null) {
            str.append(line);
        }
        String s = str.toString();
        String[] split = s.split("m_Script");
        String s1 = split[1];
        String content = s1.substring(s1.indexOf("=")+3,s1.length()-1);
        JsonValue root = new JsonReader().parse(content);
        JsonValue jsonValue = root.get("GameDocuments").get("Board").child.get("CardList");
        JsonValue child1 = jsonValue.child;
        JsonValue temp = child1;
        while (temp!= null) {
            System.out.println(temp.get("id")+" "+temp.get("x")+"  "+temp.get("y")+"  "+temp.get("rotation")+"  "+temp.get("rank")+"  "+temp.get("suit")+"  "+temp.get("rotation"));
            temp = temp.next;
        }
    }

    public void method1()throws Exception {
        BufferedReader bufferedReader
                = new BufferedReader(
                new FileReader(new File("level/Level1.txt")));
        bufferedReader.readLine();
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        String content = s.substring(s.indexOf("=")+3,s.length()-1);
        System.out.println(content);
        String ss = "{\"PanelSetting\":{\"PanelWidth\":1920.0},\"GameSetting\":{\"gameModel\":0,\"handCardCount\":12},\"GameDocuments\":{\"Board\":[{\"CardList\":[{\"id\":0,\"x\":-400,\"y\":190,\"rotation\":0.0,\"rank\":2,\"suit\":4,\"covers\":[12,6,5],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":1,\"x\":-150,\"y\":190,\"rotation\":0.0,\"rank\":3,\"suit\":3,\"covers\":[7],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":2,\"x\":0,\"y\":190,\"rotation\":0.0,\"rank\":4,\"suit\":2,\"covers\":[14,8,7],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":3,\"x\":150,\"y\":190,\"rotation\":0.0,\"rank\":3,\"suit\":1,\"covers\":[8],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":4,\"x\":400,\"y\":190,\"rotation\":0.0,\"rank\":2,\"suit\":4,\"covers\":[16,10,9],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":5,\"x\":-475,\"y\":20,\"rotation\":0.0,\"rank\":1,\"suit\":3,\"covers\":[12,11],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":6,\"x\":-325,\"y\":20,\"rotation\":0.0,\"rank\":13,\"suit\":2,\"covers\":[13,12],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":7,\"x\":-75,\"y\":20,\"rotation\":0.0,\"rank\":11,\"suit\":1,\"covers\":[14],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":8,\"x\":75,\"y\":20,\"rotation\":0.0,\"rank\":12,\"suit\":4,\"covers\":[14],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":9,\"x\":325,\"y\":20,\"rotation\":0.0,\"rank\":10,\"suit\":3,\"covers\":[16,15],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":10,\"x\":475,\"y\":20,\"rotation\":0.0,\"rank\":9,\"suit\":2,\"covers\":[17,16],\"type\":2,\"faceUp\":0,\"isLadder\":false},{\"id\":11,\"x\":-550,\"y\":-150,\"rotation\":0.0,\"rank\":2,\"suit\":3,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":12,\"x\":-400,\"y\":-150,\"rotation\":0.0,\"rank\":3,\"suit\":4,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":13,\"x\":-249,\"y\":-150,\"rotation\":0.0,\"rank\":4,\"suit\":1,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":14,\"x\":0,\"y\":-150,\"rotation\":0.0,\"rank\":5,\"suit\":2,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":15,\"x\":249,\"y\":-150,\"rotation\":0.0,\"rank\":6,\"suit\":3,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":16,\"x\":400,\"y\":-150,\"rotation\":0.0,\"rank\":7,\"suit\":4,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false},{\"id\":17,\"x\":550,\"y\":-150,\"rotation\":0.0,\"rank\":8,\"suit\":1,\"covers\":[],\"type\":2,\"faceUp\":1,\"isLadder\":false}],\"LockList\":[]},{\"CardList\":[],\"LockList\":[]},{\"CardList\":[],\"LockList\":[]},{\"CardList\":[],\"LockList\":[]}]}}";
        JsonValue root = new JsonReader().parse(content);
        JsonValue child = root;
        JsonValue panelSetting = child.get("PanelSetting");
        JsonValue jsonValue = root.get("GameDocuments").get("Board").child.get("CardList");
        System.out.println("00000000000000000000000000000");
        JsonValue child1 = jsonValue.child;
        JsonValue temp = child1;
        while (temp!= null) {
            System.out.println(temp.get("id")+" "+temp.get("x")+"  "+temp.get("y")+"  "+temp.get("rotation")+"  "+temp.get("rank")+"  "+temp.get("suit")+"  "+temp.get("rotation"));
            temp = temp.next;
        }
    }
}
