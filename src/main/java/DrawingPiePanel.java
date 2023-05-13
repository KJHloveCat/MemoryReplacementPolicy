import javax.swing.*;
import java.awt.*;

public class DrawingPiePanel extends JPanel {
    int fault;
    int migrate;
    int hit;

    public DrawingPiePanel(){
        super.setPreferredSize(new Dimension(150,150));
        super.setEnabled(true);
        super.setVisible(true);
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if ((fault < 0) || (migrate < 0) || (hit < 0))
            return;
        int total = fault + migrate + hit;
        if (total == 0)
            return;

        int arc1 = (int) 360.0 * fault / total;
        int arc2 = (int) 360.0 * migrate / total;

        g.setColor(Color.RED);
        g.fillArc(15, 15, 200, 200, 0, arc1);
        g.setColor(Color.orange);
        g.fillArc(15, 15, 200, 200, arc1,arc2);
        g.setColor(Color.GREEN);
        g.fillArc(15, 15, 200, 200, arc1 + arc2, 360-(arc1 + arc2));

        Font f = new Font("Arial", Font.PLAIN, 18);
        g.setFont(f);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(fault), 175, 230);
        g.setColor(Color.RED);
        g.drawString("Fault", 200, 230);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(migrate), 175, 250);
        g.setColor(Color.orange);
        g.drawString("Migrate", 200, 250);
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(hit), 175, 270);
        g.setColor(Color.green);
        g.drawString("Hit", 200, 270);
    }

    void setNumbers(int fault, int migrate, int hit) {
        this.fault = fault;
        this.migrate = migrate;
        this.hit = hit;
    }
}
