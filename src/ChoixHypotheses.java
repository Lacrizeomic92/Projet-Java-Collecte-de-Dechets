import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class ChoixHypotheses extends JFrame {

    public ChoixHypotheses() {

        setTitle("Choix des Hypothèses");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = ChoixHypotheses.class.getResource("/choix_hypotheses.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver choix_hypotheses.png",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // HO1 → Menu graphique
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new MenuPrincipal(1);
                }

                // HO2 → Menu graphique
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    dispose();
                    new MenuPrincipal(2);
                }

                // HO3 → Menu graphique
                if (e.getKeyCode() == KeyEvent.VK_3) {
                    dispose();
                    new MenuPrincipal(3);
                }

                // Retour espace
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Accueil();
                }
                if (e.getKeyCode() == KeyEvent.VK_0) {
                    dispose();
                    new Utilisateur();
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);

        setVisible(true);
    }
}
