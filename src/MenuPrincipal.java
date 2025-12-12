import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    private int hypothese;

    public MenuPrincipal(int hypotheseChoisie) {

        this.hypothese = hypotheseChoisie;

        setTitle("Menu Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


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


        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_1) {
                    dispose();

                    if (hypothese == 1)
                        new Theme1();
                    else if (hypothese == 2)
                        new Theme1_HO2();
                    else
                        JOptionPane.showMessageDialog(null,
                                "HO3 n'est pas encore disponible !");
                }

                if (e.getKeyCode() == KeyEvent.VK_2) {
                    dispose();

                    try {
                        new Theme2();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Erreur lors de l'ouverture du Thème 2 : " + ex.getMessage());
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_3) {
                    dispose();
                    new Theme3();
                }


                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ESCAPE ||
                        e.getKeyCode() == KeyEvent.VK_SPACE) {    // ← AJOUT EXCLUSIF

                    dispose();
                    new ChoixHypotheses();
                }
            }
        });

        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
