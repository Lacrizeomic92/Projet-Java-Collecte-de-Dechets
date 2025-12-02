import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Theme1 extends JFrame {

    public Theme1() {

        setTitle("Thème 1 – HO1 (Double sens)");
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

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        // =============================
        //     GESTION DU CLAVIER
        // =============================
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_1:
                        dispose();
                        new ItineraireDeuxPoints();      // HO1 : itinéraire entre 2 intersections
                        break;

                    case KeyEvent.VK_2:
                        dispose();
                        new ItineraireMultiPoints();     // HO1 : tournée multipoints
                        break;

                    case KeyEvent.VK_ESCAPE:
                        dispose();
                        new MenuPrincipal(1);            // Retour au menu avec HO1
                        break;
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
