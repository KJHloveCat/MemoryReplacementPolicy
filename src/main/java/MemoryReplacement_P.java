import java.util.HashMap;
import java.util.LinkedList;

class MemoryReplacement_P {
    String refStr2;
    String refStr = "";
    int refN = 0;
    int frame = 0;
    HashMap<Integer, Integer> fault = new HashMap<>();
    HashMap<Integer, Integer> migrate = new HashMap<>();
    HashMap<Integer, Integer> hit = new HashMap<>();

    public LinkedList<Character>[] Run(){
        LinkedList<Character>[] list = new LinkedList[refN];
        return list;
    }
}
