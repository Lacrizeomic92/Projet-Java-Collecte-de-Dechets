import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Theme3TypeCollecte extends JFrame {

    public Theme3TypeCollecte() {

        setTitle("Thème 3 – Choix du type de collecte");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ======== PANEL FOND CARTOON ========
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(240, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);
        setContentPane(panel);

        // ======== TITRE ========
        JLabel titre = new JLabel("Choisis le type de collecte :", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 28));
        titre.setBounds(0, 40, 900, 50);
        panel.add(titre);

        // ======== BOUTON VERRE ========
        JButton verre = new JButton("Verre");
        verre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        verre.setBackground(new Color(180, 240, 255));
        verre.setBorder(BorderFactory.createLineBorder(new Color(50, 100, 150), 3));
        verre.setBounds(300, 150, 300, 80);
        panel.add(verre);

        // ======== BOUTON PAPIERS ========
        JButton papiers = new JButton("Papiers & cartons");
        papiers.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        papiers.setBackground(new Color(255, 240, 180));
        papiers.setBorder(BorderFactory.createLineBorder(new Color(160, 120, 40), 3));
        papiers.setBounds(300, 260, 300, 80);
        panel.add(papiers);

        // ======== BOUTON ORDURES ========
        JButton ordures = new JButton("Ordures ménagères");
        ordures.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        ordures.setBackground(new Color(255, 200, 200));
        ordures.setBorder(BorderFactory.createLineBorder(new Color(180, 70, 70), 3));
        ordures.setBounds(300, 370, 300, 80);
        panel.add(ordures);

        // ======== ACTIONS BOUTONS ========
        verre.addActionListener(e -> {
            dispose();
            new Theme3Jours("verre");
        });

        papiers.addActionListener(e -> {
            dispose();
            new Theme3Jours("papiers");
        });

        ordures.addActionListener(e -> {
            dispose();
            new Theme3Jours("ordures");
        });

        // ======== RETOUR (ESPACE) ========
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme3();
                }
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();

        setVisible(true);
    }
}
