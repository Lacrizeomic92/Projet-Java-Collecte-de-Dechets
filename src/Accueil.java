import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Accueil extends JFrame {

    public Accueil() {

        setTitle("Accueil");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = Accueil.class.getResource("/camion.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver camion.png",
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

                // Toucher une touche pour passer à la page Utilisateur
                if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ENTER) {

                    dispose();
                    new Utilisateur();      // 👈 affiche la page utilisateur
                }
            }
        });

        // Force le focus
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Accueil::new);
    }
}
