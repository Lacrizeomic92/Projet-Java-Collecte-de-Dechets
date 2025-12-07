import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Theme1_HO2 extends JFrame {

    public Theme1_HO2() {

        setTitle("Thème 1 – Hypothèse 2");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        URL imgUrl = Theme1_HO2.class.getResource("/Theme1_HO2.png");

        if (imgUrl == null) {
            JOptionPane.showMessageDialog(this,
                    "Impossible de trouver Theme1_HO2.png\n" +
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

        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();
                    new ItineraireDeuxPoints_HO2();
                }

                if (e.getKeyCode() == KeyEvent.VK_2) {
                    dispose();
                    new ItineraireMultiPoints_HO2();
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    new MenuPrincipal(2);
                }
            }
        });


        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);

        setVisible(true);
    }
}
