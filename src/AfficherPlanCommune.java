import javax.swing.*;
import java.awt.*;

public class AfficherPlanCommune extends JFrame {

    public AfficherPlanCommune() {

        setTitle("Plan de la commune");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Charger l’image (PLACE TON PNG DANS src/)
        ImageIcon icon = new ImageIcon("src/plan_commune.png");

        Image scaled = icon.getImage().getScaledInstance(
                950, 650, Image.SCALE_SMOOTH
        );

        JLabel label = new JLabel(new ImageIcon(scaled));
        label.setHorizontalAlignment(JLabel.CENTER);

        add(label);

        setVisible(true);
    }
}
