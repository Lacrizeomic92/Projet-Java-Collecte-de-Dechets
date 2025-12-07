import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.io.File;

public class Theme3 extends JFrame {

    public Theme3() {

        setTitle("Thème 3 – Collecte de Déchets");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===============================
        //   CHARGEMENT DE L'IMAGE Theme3.jpg
        // ===============================
        URL imgUrl = Theme3.class.getResource("/Theme3.jpg");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver Theme3.jpg\n" +
                            "Assure-toi qu'elle est bien dans src/",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        // ===============================
        //     GESTION DU CLAVIER
        // ===============================
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    // ===============================
                    // OPTION 1 -> AFFICHER PNG
                    // ===============================
                    case KeyEvent.VK_1:
                        dispose();

                        try {
                            JFrame f = new JFrame("Division de la commune – HO1");
                            f.setSize(1200, 800);
                            f.setLocationRelativeTo(null);
                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            // Charger le PNG depuis /src/
                            File pngFile = new File("src/division_commune.png");

                            if (!pngFile.exists()) {
                                JOptionPane.showMessageDialog(null,
                                        "Le fichier division_commune.png est introuvable.\n" +
                                                "Place-le dans le dossier /src/",
                                        "Erreur",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Charger et REDIMENSIONNER l'image
                            ImageIcon rawIcon = new ImageIcon(pngFile.getAbsolutePath());
                            Image rawImg = rawIcon.getImage();

                            // Taille cible pour bien remplir la fenêtre
                            int targetWidth = 1100;
                            int targetHeight = 700;

                            Image scaledImg = rawImg.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                            ImageIcon scaledIconImg = new ImageIcon(scaledImg);

                            // Affichage
                            JLabel lab = new JLabel(scaledIconImg);
                            lab.setHorizontalAlignment(SwingConstants.CENTER);

                            JScrollPane scroll = new JScrollPane(lab);
                            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                            f.add(scroll);
                            f.setVisible(true);

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Erreur lors de l'affichage de l'image:\n" + ex.getMessage(),
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    // OPTION 2
                    case KeyEvent.VK_2:
                        JOptionPane.showMessageDialog(null,
                                "Option 2 - À implémenter",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;

                    // OPTION 3
                    case KeyEvent.VK_3:
                        JOptionPane.showMessageDialog(null,
                                "Option 3 - À implémenter",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;

                    // ===== RETOUR =====
                    case KeyEvent.VK_ESCAPE:
                    case KeyEvent.VK_BACK_SPACE:
                    case KeyEvent.VK_SPACE:
                        dispose();
                        new ChoixHypotheses();
                        break;

                    case KeyEvent.VK_0:
                        dispose();
                        new MenuPrincipal(1);
                        break;
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
