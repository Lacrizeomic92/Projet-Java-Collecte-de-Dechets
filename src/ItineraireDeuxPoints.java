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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class ItineraireDeuxPoints extends JFrame {

    private JTextField departField;
    private JTextField arriveeField;
    private JTextArea resultatArea;

    public ItineraireDeuxPoints() {

        setTitle("Itinéraire optimal entre deux intersections");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panneauFond = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(new Color(245, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());

                try {
                    Image camion = new ImageIcon("camion.png").getImage();
                    g.drawImage(camion, 20, 380, 200, 140, this);
                } catch (Exception ignored) {}
            }
        };

        panneauFond.setLayout(null);
        panneauFond.setFocusable(true);
        add(panneauFond);

        JLabel titre = new JLabel("Itinéraire optimal entre deux intersections", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        panneauFond.add(titre);

        JPanel cadre = new JPanel();
        cadre.setLayout(null);
        cadre.setBackground(new Color(255, 255, 255, 235));
        cadre.setBounds(220, 100, 460, 380);
        cadre.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));
        panneauFond.add(cadre);

        JLabel labelDepart = new JLabel("Intersection de départ :");
        labelDepart.setFont(new Font("Arial", Font.PLAIN, 16));
        labelDepart.setBounds(30, 30, 200, 25);
        cadre.add(labelDepart);

        departField = new JTextField();
        departField.setFont(new Font("Arial", Font.PLAIN, 16));
        departField.setBounds(30, 60, 400, 35);
        cadre.add(departField);

        JLabel labelArrivee = new JLabel("Intersection d'arrivée :");
        labelArrivee.setFont(new Font("Arial", Font.PLAIN, 16));
        labelArrivee.setBounds(30, 110, 200, 25);
        cadre.add(labelArrivee);

        arriveeField = new JTextField();
        arriveeField.setFont(new Font("Arial", Font.PLAIN, 16));
        arriveeField.setBounds(30, 140, 400, 35);
        cadre.add(arriveeField);

        JButton boutonCalcul = new JButton("Calculer l'itinéraire");
        boutonCalcul.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        boutonCalcul.setBounds(95, 195, 270, 45);
        boutonCalcul.setBackground(new Color(230, 250, 230));
        boutonCalcul.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 150), 2, true));
        cadre.add(boutonCalcul);

        resultatArea = new JTextArea();
        resultatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 255, 420, 100);
        cadre.add(scroll);

        JButton boutonRetour = new JButton("Retour");
        boutonRetour.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        boutonRetour.setBounds(20, 500 - 60, 120, 35);
        boutonRetour.setBackground(new Color(255, 230, 230));
        boutonRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 130, 130), 2, true));
        panneauFond.add(boutonRetour);

        boutonRetour.addActionListener(e -> {
            dispose();
            new Theme1();
        });

        boutonCalcul.addActionListener(e -> lancerCalcul());

        setVisible(true);
    }

    // ----- Structure du résultat -----
    static class CheminResult {
        List<String> rues;
        double distance;
        CheminResult(List<String> rues, double distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    // ----- Supprimer les doublons consécutifs -----
    private List<String> supprimerDoublonsConsecutifs(List<String> rues) {
        List<String> resultat = new ArrayList<>();
        String precedente = null;

        for (String r : rues) {
            if (!r.equals(precedente)) {
                resultat.add(r);
            }
            precedente = r;
        }

        return resultat;
    }

    // ----- Action bouton -----
    private void lancerCalcul() {
        try {
            String depart = departField.getText().trim();
            String arrivee = arriveeField.getText().trim();

            if (depart.isEmpty() || arrivee.isEmpty()) {
                resultatArea.setText("Veuillez saisir deux intersections (ex : S21863880)");
                return;
            }

            DijkstraNice.CheminResult res = DijkstraNice.dijkstra(depart, arrivee);

            if (res == null) {
                resultatArea.setText("Aucun chemin trouvé entre ces intersections.");
            } else {
                resultatArea.setText(
                        "Rues traversées :\n" +
                                String.join(" → ", res.rues) +
                                "\n\nDistance totale : " + String.format("%.2f", res.distance) + " m"
                );
            }

        } catch (Exception ex) {
            resultatArea.setText("Erreur : " + ex.getMessage());
        }
    }

    // ============================
    //        DIJKSTRA
    // ============================
    CheminResult dijkstra(String depart, String arrivee) throws IOException {

        Map<String, Map<String, Double>> adj = new HashMap<>();
        Map<String, Map<String, String>> rueParArc = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("nice_arcs.txt"));
        String ligne;

        while ((ligne = br.readLine()) != null) {
            String[] p = ligne.split(";");
            if (p.length != 4) continue;

            String id1 = p[0].trim();
            String id2 = p[1].trim();
            double dist = Double.parseDouble(p[2].trim().replace(",", "."));
            String nomRue = p[3].trim();

            adj.putIfAbsent(id1, new HashMap<>());
            adj.putIfAbsent(id2, new HashMap<>());
            rueParArc.putIfAbsent(id1, new HashMap<>());
            rueParArc.putIfAbsent(id2, new HashMap<>());

            adj.get(id1).put(id2, dist);
            adj.get(id2).put(id1, dist);

            rueParArc.get(id1).put(id2, nomRue);
            rueParArc.get(id2).put(id1, nomRue);
        }
        br.close();

        if (!adj.containsKey(depart) || !adj.containsKey(arrivee)) {
            return null;
        }

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prec = new HashMap<>();

        for (String s : adj.keySet()) dist.put(s, Double.POSITIVE_INFINITY);
        dist.put(depart, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(depart);

        while (!pq.isEmpty()) {
            String cur = pq.poll();

            if (cur.equals(arrivee)) break;

            for (var entry : adj.get(cur).entrySet()) {
                String voisin = entry.getKey();
                double poids = entry.getValue();

                double nouvelleDist = dist.get(cur) + poids;

                if (nouvelleDist < dist.get(voisin)) {
                    dist.put(voisin, nouvelleDist);
                    prec.put(voisin, cur);
                    pq.add(voisin);
                }
            }
        }

        if (dist.get(arrivee) == Double.POSITIVE_INFINITY) {
            return null;
        }

        // ----- Reconstruction du chemin -----
        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            String rue = rueParArc.get(p).get(cur);
            rues.add(0, rue);
            cur = p;
        }

        // 🔥 Supprimer les doublons consécutifs
        rues = supprimerDoublonsConsecutifs(rues);

        return new CheminResult(rues, dist.get(arrivee));
    }
}
