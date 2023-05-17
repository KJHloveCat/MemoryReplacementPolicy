import java.util.LinkedList;

public class LFU extends MemoryReplacement_P{

    public LFU(String refStr, int frame) {
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

        for (int i = 0; i < refN; i++) {
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() != frame) {
                    list[i].add(refStr.charAt(i));
                    fault.put(i, list[i].size() - 1);
                } else if (!list[i].contains(refStr.charAt(i)) && list[i - 1].size() == frame) { // migrate

                    int maxNum_idx = 0;
                    String leftStr = refStr.substring(0, i);
                    int maxNum = CountChar(leftStr, list[i].get(0));

                    for(int k=1; k<frame; k++){
                        int temp = CountChar(leftStr, list[i].get(k));
                        if(maxNum > temp){
                            temp = maxNum;
                            maxNum_idx = k;
                        }
                    }

                    StringBuffer sb = new StringBuffer(leftStr);
                    String reverseLStr = sb.reverse().toString();

                    int max = reverseLStr.indexOf(list[i].get(maxNum_idx));
                    for(int j=maxNum_idx; j<frame; j++){
                        int temp = reverseLStr.indexOf(list[i].get(j));
                        if(max < temp && maxNum == CountChar(leftStr, list[i].get(j))){
                            max = temp;
                            maxNum_idx = j;
                        }
                    }

                    list[i].remove(maxNum_idx);
                    list[i].add(maxNum_idx, refStr.charAt(i));
                    migrate.put(i, maxNum_idx);
                } else if (list[i].contains(refStr.charAt(i))) { // hit
                    int index = list[i].indexOf(refStr.charAt(i));
                    hit.put(i, index);
                }
            } else { // fault
                list[i].add(refStr.charAt(i));
                fault.put(i, refStr.indexOf(refStr.charAt(i)));
            }


        }
        return list;
    }
}
