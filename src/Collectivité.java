import javax.swing.*;
import java.awt.*;

public class Collectivite extends JFrame {

    public Collectivite() {

        // Titre de la fenêtre
        setTitle("Menu - Collectivité");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chargement de l'image
        ImageIcon icon = new ImageIcon("src/collectivite.png");

        // Redimensionner l'image automatiquement si nécessaire
        Image scaledImage = icon.getImage().getScaledInstance(950, 550, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        // Centrer l'image dans la fenêtre
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        add(imageLabel, BorderLayout.CENTER);

        setVisible(true);
    }
}
