import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ItineraireDeuxPoints extends JFrame {

    private JTextField departField;
    private JTextField arriveeField;
    private JTextArea resultatArea;

    public ItineraireDeuxPoints() {

        setTitle("Itinéraire optimal entre deux rues");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== PANEL DE FOND CARTOON =====
        JPanel panneauFond = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Fond pastel
                g.setColor(new Color(245, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());

                // Camion décoratif
                try {
                    Image camion = new ImageIcon("camion.png").getImage();
                    g.drawImage(camion, 20, 380, 200, 140, this);
                } catch (Exception ignored) {}
            }
        };

        panneauFond.setLayout(null);
        panneauFond.setFocusable(true);
        add(panneauFond);

        // ===== TITRE =====
        JLabel titre = new JLabel("Itinéraire optimal entre deux rues", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        panneauFond.add(titre);

        // ===== CADRE BLANC =====
        JPanel cadre = new JPanel();
        cadre.setLayout(null);
        cadre.setBackground(new Color(255, 255, 255, 235));
        cadre.setBounds(220, 100, 460, 380);
        cadre.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));
        panneauFond.add(cadre);

        // ===== CHAMP DÉPART =====
        JLabel labelDepart = new JLabel("Rue de départ :");
        labelDepart.setFont(new Font("Arial", Font.PLAIN, 16));
        labelDepart.setBounds(30, 30, 200, 25);
        cadre.add(labelDepart);

        departField = new JTextField();
        departField.setFont(new Font("Arial", Font.PLAIN, 16));
        departField.setBounds(30, 60, 400, 35);
        cadre.add(departField);

        // ===== CHAMP ARRIVEE =====
        JLabel labelArrivee = new JLabel("Rue d'arrivée :");
        labelArrivee.setFont(new Font("Arial", Font.PLAIN, 16));
        labelArrivee.setBounds(30, 110, 200, 25);
        cadre.add(labelArrivee);

        arriveeField = new JTextField();
        arriveeField.setFont(new Font("Arial", Font.PLAIN, 16));
        arriveeField.setBounds(30, 140, 400, 35);
        cadre.add(arriveeField);

        // ===== BOUTON CALCUL =====
        JButton boutonCalcul = new JButton("Calculer l'itinéraire");
        boutonCalcul.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        boutonCalcul.setBounds(95, 195, 270, 45);
        boutonCalcul.setBackground(new Color(230, 250, 230));
        boutonCalcul.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 150), 2, true));
        boutonCalcul.setFocusPainted(false);
        cadre.add(boutonCalcul);

        // ===== ZONE DE RESULTAT =====
        resultatArea = new JTextArea();
        resultatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 255, 420, 100);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        cadre.add(scroll);

        // ===== BOUTON RETOUR =====
        JButton boutonRetour = new JButton("Retour");
        boutonRetour.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        boutonRetour.setBounds(20, 500 - 60, 120, 35);
        boutonRetour.setBackground(new Color(255, 230, 230));
        boutonRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 130, 130), 2, true));
        boutonRetour.setFocusPainted(false);
        panneauFond.add(boutonRetour);

        boutonRetour.addActionListener(e -> {
            dispose();
            new Theme1();
        });

        // ===== ACTION BOUTON CALCUL =====
        boutonCalcul.addActionListener(e -> lancerCalcul());

        // ===== RETOUR AVEC ESPACE =====
        panneauFond.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme1();
                }
            }
        });

        setVisible(true);
    }

    // ==========================
    //  LANCE LE CALCUL
    // ==========================
    private void lancerCalcul() {
        try {
            String depart = departField.getText().trim();
            String arrivee = arriveeField.getText().trim();

            if (depart.isEmpty() || arrivee.isEmpty()) {
                resultatArea.setText("Merci de saisir une rue de départ et une rue d'arrivée.");
                return;
            }

            CheminResult res = dijkstra(depart, arrivee);

            if (res == null || res.chemin == null) {
                resultatArea.setText("Aucun chemin trouvé entre ces deux rues.");
            } else {
                resultatArea.setText(
                        "Chemin optimal :\n" +
                                String.join(" → ", res.chemin) +
                                "\n\nDistance totale : " + String.format("%.2f", res.distance) + " m"
                );
            }

        } catch (Exception ex) {
            resultatArea.setText("Erreur : " + ex.getMessage());
        }
    }

    // ===== Structure résultat =====
    private static class CheminResult {
        java.util.List<String> chemin;
        double distance;

        CheminResult(java.util.List<String> chemin, double distance) {
            this.chemin = chemin;
            this.distance = distance;
        }
    }

    // ==========================
    //  DIJKSTRA AVEC double
    // ==========================
    private CheminResult dijkstra(String depart, String arrivee) throws IOException {

        // --- Lecture du fichier pondéré ---
        Map<String, Map<String, Double>> adj = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("rues_nice.txt"));
        String ligne;

        while ((ligne = br.readLine()) != null) {
            String[] p = ligne.split(";");
            if (p.length != 3) continue;

            String a = p[0].trim();
            String b = p[1].trim();

            // Remplacer virgule éventuelle par point et parser en double
            String distTexte = p[2].trim().replace(',', '.');
            double d;
            try {
                d = Double.parseDouble(distTexte);
            } catch (NumberFormatException e) {
                continue; // on ignore les lignes mal formées
            }

            adj.putIfAbsent(a, new HashMap<>());
            adj.putIfAbsent(b, new HashMap<>());

            adj.get(a).put(b, d);
            adj.get(b).put(a, d);
        }
        br.close();

        if (!adj.containsKey(depart) || !adj.containsKey(arrivee)) {
            return null;
        }

        // --- Dijkstra ---
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> precedent = new HashMap<>();

        for (String s : adj.keySet()) {
            dist.put(s, Double.POSITIVE_INFINITY);
        }
        dist.put(depart, 0.0);

        PriorityQueue<String> file = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        file.add(depart);

        while (!file.isEmpty()) {
            String courant = file.poll();

            if (courant.equals(arrivee)) break;

            for (Map.Entry<String, Double> entry : adj.get(courant).entrySet()) {
                String voisin = entry.getKey();
                double poids = entry.getValue();

                double nouvelleDist = dist.get(courant) + poids;

                if (nouvelleDist < dist.get(voisin)) {
                    dist.put(voisin, nouvelleDist);
                    precedent.put(voisin, courant);
                    file.add(voisin);
                }
            }
        }

        if (dist.get(arrivee) == Double.POSITIVE_INFINITY) {
            return null;
        }

        // --- Reconstruction du chemin ---
        java.util.List<String> chemin = new ArrayList<>();
        String courant = arrivee;

        while (courant != null) {
            chemin.add(0, courant);
            courant = precedent.get(courant);
        }

        double distanceTotale = dist.get(arrivee);

        return new CheminResult(chemin, distanceTotale);
    }
}
