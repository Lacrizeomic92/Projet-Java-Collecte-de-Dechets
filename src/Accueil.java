import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Accueil extends JFrame {

    public Accueil() {

        setTitle("Collecte des Déchets – Simulation");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = Accueil.class.getResource("/camion.png");
        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver l'image camion.png\n" +
                            "Vérifie qu'elle est bien dans src/ au même niveau que Accueil.java.",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        // ====== ESPACE → ChoixHypothèses ======
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new ChoixHypotheses();
                }
            }
        });

        setVisible(true);

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
    }
}
