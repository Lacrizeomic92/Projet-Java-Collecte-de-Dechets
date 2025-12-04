import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Accueil extends JFrame {

    public Accueil() {

        setTitle("Collecte des Déchets – Simulation");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // CORRIGÉ ICI
        setLocationRelativeTo(null);

        // Chargement de l'image
        URL imgUrl = Accueil.class.getResource("/camion.png");

        // Si pas trouvé dans le classpath, essaie dans le dossier courant
        if (imgUrl == null) {
            imgUrl = Accueil.class.getResource("camion.png");
        }

        // Si toujours pas trouvé, essaie avec un chemin relatif
        if (imgUrl == null) {
            imgUrl = ClassLoader.getSystemResource("camion.png");
        }

        if (imgUrl == null) {
            // Message d'erreur
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver camion.png\n" +
                            "Vérifiez que le fichier est dans:\n" +
                            "1. Le dossier src/ avec Accueil.java\n" +
                            "2. Ou dans un dossier resources/ ajouté au classpath",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);

            // Fallback : fenêtre bleue avec message
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.BLUE);

            JLabel label = new JLabel("APPUYEZ SUR ESPACE POUR CONTINUER", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setForeground(Color.WHITE);
            panel.add(label, BorderLayout.CENTER);

            setContentPane(panel);

            // Ajout du KeyListener au panel
            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        dispose();
                        new Utilisateur();
                    }
                }
            });
            panel.setFocusable(true);

        } else {
            // Image trouvée - affichage normal
            System.out.println("Image chargée depuis : " + imgUrl);

            ImageIcon icon = new ImageIcon(imgUrl);
            Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);

            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setFocusable(true);
            setContentPane(imageLabel);

            // Gestion de la touche ESPACE
            imageLabel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        dispose();
                        new Utilisateur();
                    }
                }
            });
        }

        setVisible(true);

        // Demande du focus après l'affichage
        SwingUtilities.invokeLater(() -> {
            Component content = getContentPane();
            if (content instanceof JPanel) {
                ((JPanel) content).requestFocusInWindow();
            } else if (content instanceof JLabel) {
                ((JLabel) content).requestFocusInWindow();
            }
        });
    }

    // Pour tester
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Accueil());
    }
}
