import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Theme3Hypothese1 extends JFrame {

    private Map<String, List<String>> voisins;
    private Map<String, Color> couleurs;

    public Theme3Hypothese1() {

        setTitle("Thème 3 – Hypothèse 1 : Carte colorée de Nice");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== Définition des 6 secteurs et de leurs voisins ======
        definirVoisins();

        // ====== Coloration automatique ======
        genererColoration();

        // ====== Panel qui dessine la carte ======
        CartePanel panelCarte = new CartePanel();
        setContentPane(panelCarte);

        // ====== Bouton suivant ======
        JButton suivant = new JButton("Suivant →");
        suivant.setBounds(720, 500, 150, 40);
        suivant.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        suivant.setBackground(new Color(200, 255, 200));
        suivant.setBorder(BorderFactory.createLineBorder(new Color(120, 180, 120), 2));

        panelCarte.setLayout(null);
        panelCarte.add(suivant);

        suivant.addActionListener(e -> {
            dispose();
            new Theme3TypeCollecte();
        });

        // ====== Touche espace → Retour ======
        panelCarte.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme3();
                }
            }
        });

        panelCarte.setFocusable(true);
        panelCarte.requestFocusInWindow();

        setVisible(true);
    }

    // ================================================================
    // 1) Définition des voisins (structure du graphe)
    // ================================================================
    private void definirVoisins() {
        voisins = new HashMap<>();

        voisins.put("Gambetta", Arrays.asList("Jean-Médecin", "Libération"));
        voisins.put("Jean-Médecin", Arrays.asList("Gambetta", "Libération", "Vieux Nice", "Port"));
        voisins.put("Libération", Arrays.asList("Gambetta", "Jean-Médecin", "Cimiez"));
        voisins.put("Cimiez", Arrays.asList("Libération", "Port"));
        voisins.put("Port", Arrays.asList("Jean-Médecin", "Cimiez", "Vieux Nice"));
        voisins.put("Vieux Nice", Arrays.asList("Jean-Médecin", "Port"));
    }

    // ================================================================
    // 2) Coloration automatique du graphe (Jour A / Jour B)
    // ================================================================
    private void genererColoration() {

        couleurs = new HashMap<>();
        Map<String, Integer> couleurCode = new HashMap<>();

        Queue<String> queue = new LinkedList<>();

        for (String secteur : voisins.keySet()) {
            if (!couleurCode.containsKey(secteur)) {

                couleurCode.put(secteur, 0);
                queue.add(secteur);

                while (!queue.isEmpty()) {
                    String current = queue.poll();

                    for (String v : voisins.get(current)) {
                        if (!couleurCode.containsKey(v)) {
                            couleurCode.put(v, 1 - couleurCode.get(current));
                            queue.add(v);
                        }
                    }
                }
            }
        }

        // Conversion en vraies couleurs jolies
        for (String secteur : couleurCode.keySet()) {
            if (couleurCode.get(secteur) == 0)
                couleurs.put(secteur, new Color(140, 200, 255)); // BLEU JOUR A
            else
                couleurs.put(secteur, new Color(170, 255, 170)); // VERT JOUR B
        }
    }

    // ================================================================
    // 3) Panel qui dessine la carte stylisée
    // ================================================================
    class CartePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            setBackground(new Color(245, 255, 245));

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));

            // Positions fixes pour la carte simplifiée
            dessinerSecteur(g2, "Gambetta", 130, 220, 160, 110);
            dessinerSecteur(g2, "Jean-Médecin", 330, 220, 160, 110);
            dessinerSecteur(g2, "Libération", 230, 90, 160, 110);
            dessinerSecteur(g2, "Cimiez", 540, 90, 160, 110);
            dessinerSecteur(g2, "Port", 540, 220, 160, 110);
            dessinerSecteur(g2, "Vieux Nice", 480, 360, 160, 110);
        }

        private void dessinerSecteur(Graphics2D g2, String nom, int x, int y, int w, int h) {
            g2.setColor(couleurs.get(nom));
            g2.fillRoundRect(x, y, w, h, 30, 30);

            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x, y, w, h, 30, 30);

            g2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 17));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (w - fm.stringWidth(nom)) / 2;
            int textY = y + (h + fm.getAscent()) / 2 - 5;
            g2.drawString(nom, textX, textY);
        }
    }
}
