import java.util.ArrayList;
import java.util.LinkedList;

public class LRU extends MemoryReplacement_P{
    public LRU(String refStr, int frame) {
        this.refStr = refStr;
        this.refN = refStr.length();
        this.frame = frame;
    }

    @Override
    public LinkedList<Character>[] Run() {
        LinkedList<Character>[] list = new LinkedList[refN];
        ArrayList<Integer> hitList = new ArrayList<>(frame);

        for(int k =0; k<frame; k++){
            hitList.add(0);
        }

        for (int i = 0; i < refN; i++) {
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() != frame) {
                    list[i].add(refStr.charAt(i));
                    fault.put(i, list[i].size() - 1);
                    hitList.set(list[i].size() -1, 0);
                } else if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() == frame) { // migrate
                    int max = hitList.get(0);
                    int max_idx = 0;

                    for(int j = 1; j<frame; j++){
                        if(max < hitList.get(j)){
                            max_idx = j;
                            max = hitList.get(j);
                        }
                    }

                    list[i].remove(max_idx);
                    list[i].add(max_idx, refStr.charAt(i));
                    migrate.put(i, max_idx);
                    hitList.set(max_idx, 0);
                } else if (list[i].contains(refStr.charAt(i))) { // hit
                    int index = list[i].indexOf(refStr.charAt(i));
                    hit.put(i, index);
                    hitList.set(index, 0);
                }
            } else { // fault
                list[i].add(refStr.charAt(i));
                fault.put(i, refStr.indexOf(refStr.charAt(i)));
            }

            for(int k =0; k<list[i].size(); k++){
                hitList.set(k, hitList.get(k)+1);
            }
        }
        return list;
    }
}
