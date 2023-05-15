import java.util.HashMap;
import java.util.LinkedList;

public class FIFO extends MemoryReplacement_P{

    FIFO(String refStr, int frame) {
        this.refStr = refStr;
        this.refN = refStr.length();
        this.frame = frame;
    }

    public LinkedList<Character>[] Run() {
        LinkedList<Character>[] list = new LinkedList[refN];

        for (int i = 0; i < refN; i++) {
            if (list[i] == null) {
                list[i] = new LinkedList<Character>();
            }

            if (i != 0) {
                list[i].addAll(list[i - 1]);

                if(!list[i].contains(refStr.charAt(i)) && list[i - 1].size() != frame){
                    list[i].add(refStr.charAt(i));
                    fault.put(i, list[i].size()-1);
                } else if(!list[i].contains(refStr.charAt(i)) && list[i - 1].size() == frame){ // migrate
                    list[i].remove();
                    list[i].add(refStr.charAt(i));
                    migrate.put(i,list[i].size()-1);
                }else if(list[i].contains(refStr.charAt(i))){ // hit
                    hit.put(i,list[i].indexOf(refStr.charAt(i)));
                }
            } else {
                list[i].add(refStr.charAt(i));
                fault.put(i,refStr.indexOf(refStr.charAt(i)));
            }
        }
        System.out.println(fault);
        System.out.println(migrate);
        System.out.println(hit);

        return list;
    }

}
