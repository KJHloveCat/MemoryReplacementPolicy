import java.util.HashMap;
import java.util.LinkedList;

public class OPT extends MemoryReplacement_P {

    OPT(String refStr, int frame) {
        this.refStr = refStr;
        this.refN = refStr.length();
        this.frame = frame;
    }

    @Override
    public LinkedList<Character>[] Run() {
        LinkedList<Character>[] list = new LinkedList[refN];

        for (int i = 0; i < refN; i++) {
            char currentChar = refStr.charAt(i);
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if (!list[i].contains(currentChar) && list[i - 1].size() != frame) {
                    list[i].add(currentChar);
                    fault.put(i, list[i].size()-1);
                } else if (!list[i].contains(currentChar) && list[i - 1].size() == frame) {
                    int index = 0;
                    int maxLeft = 0;
                    boolean fixed = false;
                    String rightStr = refStr.substring(i);
                    for (int j = 0; j < frame; j++) {
                        int left = rightStr.indexOf(list[i].get(j));
                        if(left == -1 && !fixed){
                            fixed = true;
                            index = j;
                        } else if(maxLeft < left && !fixed) {
                            maxLeft = left;
                            index = j;
                        }
                    }
                        list[i].remove(index);
                        list[i].add(index, refStr.charAt(i));
                        migrate.put(i, index);
                } else if (list[i].contains(refStr.charAt(i))) { // hit
                    hit.put(i, list[i].indexOf(refStr.charAt(i)));
                }
            } else {
                list[i].add(refStr.charAt(i));
                fault.put(i, refStr.indexOf(refStr.charAt(i)));
            }

        }
        return list;
    }
}
