import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    private int hypothese;  // 1 = HO1, 2 = HO2, 3 = HO3

    public MenuPrincipal(int hypotheseChoisie) {

        this.hypothese = hypotheseChoisie;

        setTitle("Menu Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===============================
        //   CHARGEMENT DE L'IMAGE menu.png
        // ===============================
        URL imgUrl = MenuPrincipal.class.getResource("/menu.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver menu.png\n" +
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

        // ===============================
        //     GESTION DES TOUCHES
        // ===============================
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // ======= THÈME 1 =======
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();

                    if (hypothese == 1)
                        new Theme1();          // HO1 — double sens
                    else if (hypothese == 2)
                        new Theme1_HO2();      // HO2 — sens unique
                    else
                        JOptionPane.showMessageDialog(null,
                                "HO3 n'est pas encore disponible !");
                }

                // ======= THÈME 3 =======
                if (e.getKeyCode() == KeyEvent.VK_3) {
                    dispose();
                    // Le Thème 3 s'ouvre pour toutes les hypothèses
                    new Theme3();              // Thème 3 — Collecte de déchets
                }

                // ======= RETOUR =======
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                    dispose();
                    new ChoixHypotheses();
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
