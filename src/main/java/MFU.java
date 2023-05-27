import java.util.ArrayList;
import java.util.LinkedList;

public class MFU extends MemoryReplacement_P{

    public MFU(String refStr, int frame) {
        this.refStr = refStr;
        this.refN = refStr.length();
        this.frame = frame;
    }

    public static int CountChar(String str, char ch){
        return str.length() - str.replace(String.valueOf(ch),"").length();
    }

    @Override
    public LinkedList<Character>[] Run() {
        LinkedList<Character>[] list = new LinkedList[refN];
        ArrayList<Integer> notUsed = new ArrayList<>(frame);
        ArrayList<Integer> hitList = new ArrayList<>(frame);

        for (int i = 0; i < refN; i++) {
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() != frame) {
                    list[i].add(refStr.charAt(i));
                    fault.put(i, list[i].size() - 1);
                    notUsed.add(0);
                    hitList.add(0);
                } else if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() == frame) { // migrate

                    int maxNum_idx = 0;
                    int maxHit = hitList.get(0);

                    for(int j=0; j<frame; j++){
                        int temp = hitList.get(j);

                        if(maxHit < temp){
                            maxNum_idx = j;
                            maxHit = temp;
                        }
                    }

                    int maxNotUsed = notUsed.get(maxNum_idx);
                    for(int j=maxNum_idx; j<frame; j++){
                        int temp = notUsed.get(j);
                        if(maxNotUsed < temp && maxHit == hitList.get(j)){
                            maxNotUsed = temp;
                            maxNum_idx = j;
                        }
                    }

                    list[i].remove(maxNum_idx);
                    list[i].add(maxNum_idx, refStr.charAt(i));
                    migrate.put(i, maxNum_idx);
                    notUsed.set(maxNum_idx, 0);
                    hitList.set(maxNum_idx, 0);
                } else if (list[i].contains(refStr.charAt(i))) { // hit
                    int index = list[i].indexOf(refStr.charAt(i));
                    hit.put(i, index);
                    notUsed.set(index, 0);
                    hitList.set(index, hitList.get(index) + 1);
                }
            } else { // fault
                list[i].add(refStr.charAt(i));
                fault.put(i, refStr.indexOf(refStr.charAt(i)));
                notUsed.add(0);
                hitList.add(0);
            }

            for(int j=0; j<notUsed.size(); j++){
                notUsed.set(j, notUsed.get(j)+1);
            }

        }
        return list;
    }
}
