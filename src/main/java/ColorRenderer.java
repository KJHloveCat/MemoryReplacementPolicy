
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;

public class ColorRenderer extends DefaultTableCellRenderer{
    HashMap<Integer, Integer> fault;
    HashMap<Integer, Integer> migrate;
    HashMap<Integer, Integer> hit;

    public ColorRenderer(HashMap<Integer,Integer> fault, HashMap<Integer,Integer> migrate, HashMap<Integer,Integer> hit ) {
        this.fault = fault;
        this.migrate = migrate;
        this.hit = hit;
    }

    public String setText() {
        String str = "";
        int size = fault.size() + migrate.size() + hit.size();
        for(int i=0; i<size; i++){
            if(fault.containsKey(i)){
                str = str.concat("Fault 발생\n");
            }else if(migrate.containsKey(i)){
                str =  str.concat("Migrate 발생\n");
            }else{
               str =  str.concat("Hit 발생\n");
            }
        }
        return str;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {

        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //if구문의 내용은 테이블에 따라 다르니 적절한 코딩하여야 한다.
        //참고로 rowIndex, colIndex는 0부터 시작한다.

        comp.setBackground(Color.BLACK);
        comp.setForeground(Color.WHITE);

        for(int a:fault.keySet()){
            if(row == fault.get(a) && column == a){
                comp.setBackground(Color.RED);
                comp.setForeground(Color.WHITE);
            }
        }

        for(int a:migrate.keySet()){
            if(row == migrate.get(a) && column == a){
                comp.setBackground(Color.orange);
                comp.setForeground(Color.BLACK);
            }
        }

        for(int a:hit.keySet()){
            if(row == hit.get(a) && column == a){
                comp.setBackground(Color.GREEN);
                comp.setForeground(Color.BLACK);
            }
        }

        this.setHorizontalAlignment(JLabel.CENTER);
        return( comp );
    }
}
