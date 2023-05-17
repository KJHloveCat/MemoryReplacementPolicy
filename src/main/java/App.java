import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;


public class App {
    private JPanel panel1;
    private JComboBox policySelection;
    private JTextField RefString;
    private JButton runBtn;
    private JButton random;
    private JScrollPane spanel1;
    private JTable table1;
    private JTextField Fnum;
    private JEditorPane DataLog;
    private JPanel editorAndChart;
    private JPanel test;

    public static boolean isNumber(String str) {
        return str != null && str.matches("\\d+");
    }

    public App() {
        addListener();
    }

    private void addListener() {
        random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int leftLimit = 65;
                int rightLimit = 90;

                Random random = new Random();
                int targetStringLength = random.nextInt(15) + 5;
                String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
                RefString.setText(generatedString);
            }
        });
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ref = RefString.getText().trim();
                String frame = Fnum.getText();

                if (ref.equals("")) {
                    DataLog.setText("Reference String을 입력해주세요.");
                    return;
                } else if (!isNumber(frame)) {
                    DataLog.setText("Frame 값(1이상 자연수)을 입력해주세요.");
                    return;
                }

                int row = Integer.parseInt(Fnum.getText());
                int selection = policySelection.getSelectedIndex();

                if (selection == 0) {
                    FIFO fifo = new FIFO(ref, Integer.parseInt(frame));
                    createTable(ref, row, fifo.Run(), fifo.fault, fifo.migrate, fifo.hit);
                } else if (selection == 1) {
                    OPT opt = new OPT(ref, Integer.parseInt(frame));
                    createTable(ref, row, opt.Run(), opt.fault, opt.migrate, opt.hit);
                } else if (selection == 2) {
                    LRU lru = new LRU(ref, Integer.parseInt(frame));
                    createTable(ref, row, lru.Run(), lru.fault, lru.migrate, lru.hit);
                } else if (selection == 3) {
                    LFU lfu = new LFU(ref, Integer.parseInt(frame));
                    createTable(ref, row, lfu.Run(), lfu.fault, lfu.migrate, lfu.hit);
                } else if (selection == 4) {
                    MFU mfu = new MFU(ref, Integer.parseInt(frame));
                    createTable(ref, row, mfu.Run(), mfu.fault, mfu.migrate, mfu.hit);
                }
            }
        });
    }


    private void createTable(String ref, int row, LinkedList[] list, HashMap fault, HashMap migrate, HashMap hit) {
        table1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table1.getTableHeader().setReorderingAllowed(false);
        table1.getTableHeader().setResizingAllowed(false);

        String a[] = new String[ref.length()];
        Object b[][] = new Object[row][ref.length()];
        for (int i = 0; i < ref.length(); i++) {
            a[i] = String.valueOf(ref.charAt(i));
            for (int j = 0; j < row; j++) {
                b[j][i] = null;
                if (list[i].size() > j) {
                    b[j][i] = list[i].get(j);
                }
            }
        }

        String str = "";

        int size = fault.size() + migrate.size() + hit.size();
        for (int i = 0; i < size; i++) {
            if (fault.containsKey(i)) {
                str = str.concat(b[(int) fault.get(i)][i] + " 데이터 Page Fault 발생\n");
            } else if (migrate.containsKey(i)) {
                str = str.concat(b[(int) migrate.get(i)][i] + " 데이터 Page Migrate 발생\n");
            } else {
                str = str.concat(b[(int) hit.get(i)][i] + " 데이터 Page Hit 발생\n");
            }
        }
        DataLog.setText(str);
        table1.setModel(new DefaultTableModel(
                b, a
        ));

        ColorRenderer dftc = new ColorRenderer(fault, migrate, hit);

        for (int i = 0; i < ref.length(); i++) {
            table1.getColumnModel().getColumn(i).setPreferredWidth(60);
            table1.getColumnModel().getColumn(i).setCellRenderer(dftc);
        }

        DrawingPiePanel dpp = new DrawingPiePanel();
        dpp.setNumbers(fault.size(), migrate.size(), hit.size());
        dpp.repaint();
        test.removeAll();
        test.add(dpp);
        test.updateUI();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("App");
        frame.setTitle("Memory Replacement Policy - 21812062");
        frame.setContentPane(new App().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }
}