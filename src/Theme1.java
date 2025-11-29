import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Theme1 extends JFrame {

    public Theme1() {

        setTitle("Thème 1");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // === CHARGEMENT DE L’IMAGE Theme1.png ===
        URL imgUrl = Theme1.class.getResource("/Theme1.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver Theme1.png\n" +
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


        // === Gestion des touches du clavier ===
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // <1> → Itinéraire entre deux points
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new ItineraireDeuxPoints();
                }


                // <ESC> → Retour au menu principal (optionnel)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    new MenuPrincipal();
                }
            }
        });

        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // Hypothèse 1 : Itinéraire entre deux points
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new ItineraireDeuxPoints();
                }

                // Hypothèse 2 : Tournée multipoints
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    dispose();
                    new ItineraireMultiPoints();
                }

                // Retour au Menu Principal (optionnel)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    new MenuPrincipal();
                }
            }
        });

        // Donner le focus au label une fois la fenêtre affichée
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);

        setVisible(true);
    }
}
