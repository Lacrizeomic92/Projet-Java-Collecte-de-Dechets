import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Collectivite extends JFrame {

    public Collectivite() {

        // Titre de la fenêtre
        setTitle("Menu - Collectivité");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chargement de l'image (mettre ton image dans src/)
        ImageIcon icon = new ImageIcon("src/collectivite.png");

        // Redimensionnement de l'image
        Image scaledImage = icon.getImage().getScaledInstance(950, 550, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Permettre de recevoir les touches
        imageLabel.setFocusable(true);

        // ⌨️ KEYLISTENER POUR LES TOUCHES
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // ----------- OPTION 1 : SOUMETTRE PLAN DE LA COMMUNE -----------
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    Graphe g = GrapheLoader.chargerDepuisFichier("nice_graphe_collectivite.txt");
                    new AfficherGraphe(g);  // Ouvre la fenêtre d’affichage du graphe
                }

                // ----------- OPTION ESCAPE : RETOUR -----------
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    new Utilisateur(); // Retour au menu utilisateur
                }
                if (e.getKeyCode() == KeyEvent.VK_0) {
                    dispose();
                    new Utilisateur(); // Retour au menu utilisateur
                }
            }
        });

        add(imageLabel, BorderLayout.CENTER);

        // On force le focus sur l'image pour capter les touches
        SwingUtilities.invokeLater(() -> imageLabel.requestFocusInWindow());

        setVisible(true);
    }
}
