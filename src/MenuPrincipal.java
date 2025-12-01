import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Menu Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // === Chargement de menu.png ===
        URL imgUrl = MenuPrincipal.class.getResource("/menu.png");
        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver l'image menu.png\n" +
                            "Vérifie qu'elle est bien dans src/",
                    "Erreur image",
                    JOptionPane.ERROR_MESSAGE);
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setFocusable(true);
        setContentPane(imageLabel);

        // === Touche 1 → ouvrir Theme1 ===
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new Theme1();
                }
            }
        });

        // === Touche 3 → ouvrir Theme3 ===
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_3) {
                    dispose();
                    new Theme3();
                }
            }
        });

        setVisible(true);

        // donne le focus clavier
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
    }
}
