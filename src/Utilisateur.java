import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Utilisateur extends JFrame {

    private int choixUtilisateur = 0;

    public Utilisateur() {
        setTitle("Sélection Utilisateur");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = Utilisateur.class.getResource("/utilisateur.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver utilisateur.png",
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

        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // ----------- 🔹 OPTION 1 : COLLECTIVITÉ -----------
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    choixUtilisateur = 1;
                    dispose();
                    new Collectivite();
                }

                // ----------- 🔹 OPTION 2 : CHOIX DES HYPOTHÈSES -----------
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    choixUtilisateur = 2;
                    dispose();
                    new ChoixHypotheses();  // 👈 ouverture correcte
                }

                // ----------- 🔹 RETOUR (ÉCHAP) -----------
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    new Accueil();
                }
            }
        });

        // Focus clavier sur l'image
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);

        // Sécurisation du focus
        Timer focusTimer = new Timer(100, e -> imageLabel.requestFocusInWindow());
        focusTimer.setRepeats(false);
        focusTimer.start();

        setVisible(true);
    }

    public int getChoixUtilisateur() {
        return choixUtilisateur;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Utilisateur::new);
    }
}
