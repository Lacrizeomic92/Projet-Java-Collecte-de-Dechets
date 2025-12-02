import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

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

                    case KeyEvent.VK_1:
                        // Option 1 : Lancer GestionnaireCollecte
                        dispose();
                        try {
                            new GestionnaireCollecte().demarrer();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Erreur lors du démarrage du gestionnaire:\n" + ex.getMessage(),
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case KeyEvent.VK_2:
                        // Option 2 : Vous pourrez ajouter une autre fonctionnalité ici plus tard
                        JOptionPane.showMessageDialog(null,
                                "Option 2 - À implémenter\n" +
                                        "Ex: Optimisation des tournées",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case KeyEvent.VK_3:
                        // Option 3 : Vous pourrez ajouter une autre fonctionnalité ici plus tard
                        JOptionPane.showMessageDialog(null,
                                "Option 3 - À implémenter\n" +
                                        "Ex: Suivi en temps réel",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;

                    // RETOUR AU MENU PRINCIPAL
                    case KeyEvent.VK_ESCAPE:
                    case KeyEvent.VK_BACK_SPACE:
                        dispose();
                        new ChoixHypotheses();
                        break;

                    // ALTERNATIVE POUR RETOUR
                    case KeyEvent.VK_SPACE:
                        dispose();
                        new ChoixHypotheses();
                        break;
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
