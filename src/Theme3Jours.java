import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Theme3Jours extends JFrame {

    private String typeCollecte;
    private Map<String, String> jours;

    public Theme3Jours(String type) {

        this.typeCollecte = type;

        setTitle("Jours de collecte – " + type);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        JLabel titre = new JLabel("Jours de collecte : " + nomAffichage(typeCollecte), SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 28));
        titre.setBounds(0, 40, 900, 50);
        panel.add(titre);

        chargerJours();

        int y = 130;
        for (String secteur : jours.keySet()) {

            JLabel l = new JLabel("• " + secteur + " → " + jours.get(secteur), SwingConstants.LEFT);
            l.setFont(new Font("Arial", Font.PLAIN, 22));
            l.setBounds(200, y, 600, 35);
            panel.add(l);

            y += 45;
        }

        // ======== BOUTON RETOUR ========
        JButton retour = new JButton("Retour Liste");
        retour.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        retour.setBounds(340, 480, 220, 45);
        retour.setBackground(new Color(220, 240, 220));
        retour.setBorder(BorderFactory.createLineBorder(new Color(120, 180, 120), 2));
        panel.add(retour);

        retour.addActionListener(e -> {
            dispose();
            new Theme3TypeCollecte();
        });

        // ======== Touche espace : retour ========
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme3TypeCollecte();
                }
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();

        setVisible(true);
    }

    private void chargerJours() {
        jours = new LinkedHashMap<>();

        if (typeCollecte.equals("verre")) {
            jours.put("Gambetta", "Lundi");
            jours.put("Jean-Médecin", "Mardi");
            jours.put("Libération", "Mercredi");
            jours.put("Cimiez", "Jeudi");
            jours.put("Port", "Vendredi");
            jours.put("Vieux Nice", "Samedi");
        }

        else if (typeCollecte.equals("papiers")) {
            jours.put("Gambetta", "Mardi");
            jours.put("Jean-Médecin", "Jeudi");
            jours.put("Libération", "Vendredi");
            jours.put("Cimiez", "Lundi");
            jours.put("Port", "Mercredi");
            jours.put("Vieux Nice", "Samedi");
        }

        else if (typeCollecte.equals("ordures")) {
            jours.put("Gambetta", "Lundi & Jeudi");
            jours.put("Jean-Médecin", "Mardi & Vendredi");
            jours.put("Libération", "Lundi & Vendredi");
            jours.put("Cimiez", "Mercredi & Samedi");
            jours.put("Port", "Mardi & Samedi");
            jours.put("Vieux Nice", "Jeudi & Dimanche");
        }
    }

    // Affichage propre du nom
    private String nomAffichage(String type) {
        switch(type) {
            case "verre": return "Verre";
            case "papiers": return "Papiers & Cartons";
            case "ordures": return "Ordures ménagères";
            default: return type;
        }
    }
}
