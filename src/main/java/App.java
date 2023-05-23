import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


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
    private JButton random_100;

    public static boolean isNumber(String str) {
        return str != null && str.matches("\\d+");
    }

    public static Set<String> randomStr = null;

    public App() {
        addListener();
        Set<String> randomStr;
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

                if (ref.equals("") && randomStr == null) {
                    DataLog.setText("Reference String을 입력해주세요.");
                    return;
                } else if (!isNumber(frame)) {
                    DataLog.setText("Frame 값(1이상 자연수)을 입력해주세요.");
                    return;
                }

                int row = Integer.parseInt(Fnum.getText());
                int selection = policySelection.getSelectedIndex();

                if (randomStr != null){

                    spanel1.setVisible(false);
                        int fault[] = new int[5];
                        int total[] = new int[5];
                        String name[] = {"FIFO", "OPT", "LRU", "LFU", "MFU"};

                        for (String str: randomStr){
                            FIFO fifo = new FIFO(str, Integer.parseInt(frame));
                            fifo.Run();
                            fault[0] += fifo.fault.size() + fifo.migrate.size();
                            total[0] += fifo.fault.size() + fifo.migrate.size() + fifo.hit.size();

                            OPT opt = new OPT(str, Integer.parseInt(frame));
                            opt.Run();
                            fault[1] += opt.fault.size() + opt.migrate.size();
                            total[1] += opt.fault.size() + opt.migrate.size() + opt.hit.size();

                            LRU lru = new LRU(str, Integer.parseInt(frame));
                            lru.Run();
                            fault[2] += lru.fault.size() + lru.migrate.size();
                            total[2] += lru.fault.size() + lru.migrate.size() + lru.hit.size();

                            LFU lfu = new LFU(str, Integer.parseInt(frame));
                            lfu.Run();
                            fault[3] += lfu.fault.size() + lfu.migrate.size();
                            total[3] += lfu.fault.size() + lfu.migrate.size() + lfu.hit.size();

                            MFU mfu = new MFU(str, Integer.parseInt(frame));
                            mfu.Run();
                            fault[4] += mfu.fault.size() + mfu.migrate.size();
                            total[4] += mfu.fault.size() + mfu.migrate.size() + mfu.hit.size();
                        }

                        DataLog.setText("100개의 랜덤 String 으로 페이지 교체");
                        for(int i = 0; i<5; i++){
                            String rate = String.format("%.0f", ((double)fault[i]/total[i])*100);
                            DataLog.setText(DataLog.getText()+"\n"+name[i]+" rate :" + rate +"%");
                        }


                } else {
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

            }
        });

        random_100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int leftLimit = 65;
                int rightLimit = 90;

                Random random = new Random();
                DataLog.setText("");

                if(randomStr != null){
                    DataLog.setText("랜덤 String 100개가 제거되었습니다.");
                    randomStr = null;
                } else {
                    randomStr = new HashSet<String>();

                    for(int i = 0; i< 100; i++){
                        int targetStringLength = random.nextInt(25) + 10;
                        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

                        randomStr.add(generatedString);
                        String log = DataLog.getText();
                        DataLog.setText(log+"\n"+generatedString+" 가 생성되었습니다");
                    }

                    DataLog.setText(DataLog.getText() + "\n"+"제거 하시려면 Random_100 버튼을 한번 더 눌러주세요.");

                }
            }
        });
    }


    private void createTable(String ref, int row, LinkedList[] list, HashMap fault, HashMap migrate, HashMap hit) {
        spanel1.setVisible(true);
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
        Set<String> randomStr;
        JFrame frame = new JFrame("App");
        frame.setTitle("Memory Replacement Policy - 21812062");
        frame.setContentPane(new App().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }
}