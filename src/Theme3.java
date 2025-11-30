import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Theme3 extends JFrame {

    public Theme3() {

        setTitle("Thème 3");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // === CHARGEMENT DE L’IMAGE Theme3.png ===
        URL imgUrl = Theme3.class.getResource("/Theme3.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver Theme3.png\n" +
                            "Assure-toi qu'elle est bien dans src/",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        // === Label contenant l’image ===
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        // === KEYLISTENER POUR LES ACTIONS DU THÈME 3 ===
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // === Retour au Menu Principal ===
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new MenuPrincipal();
                }

                // === Hypothèse 1 ===
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new Theme3Hypothese1();
                }

                // === Hypothèse 2 ===
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    dispose();
                    new Theme3TypeCollecte();  // <-- OUVERTURE DES 3 BOUTONS
                }
            }
        });

        setVisible(true);
        imageLabel.requestFocusInWindow();
    }
}
