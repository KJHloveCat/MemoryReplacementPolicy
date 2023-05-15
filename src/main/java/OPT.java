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
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() != frame) {
                    list[i].add(refStr.charAt(i));
                    fault.put(i, list[i].size()-1);
                } else if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() == frame) {
                    int index = -1;
                    for (int j = 0; j < frame; j++) {
                        String str = refStr.substring(i);

                        boolean fixed = false;
                        int min_Count_Temp = 0;
                        for (int k = 0; k < frame; k++) {
                            Character fStr = list[i - 1].get(k);
                            int char_Count = str.length() - str.replace(String.valueOf(fStr), "").length();

                            if (char_Count == 0 && !fixed) {
                                index = k;
                                fixed = true;
                            } else if (char_Count != 0 && !fixed) {

                                if (char_Count <= min_Count_Temp) {
                                    index = k;
                                    min_Count_Temp = char_Count;
                                }
                            }
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

            System.out.println("TESt1" + list[i]);
        }
        return list;
    }
}
