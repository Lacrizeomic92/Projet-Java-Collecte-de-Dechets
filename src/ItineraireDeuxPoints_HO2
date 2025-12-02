import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class ItineraireDeuxPoints_HO2 extends JFrame {

    private JTextField departField;
    private JTextField arriveeField;
    private JTextArea resultatArea;

    private int hypothese = 2; // 🔥 HO2 = sens unique

    public ItineraireDeuxPoints_HO2() {

        setTitle("Itinéraire (Sens Unique) – Deux Intersections");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel fond = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        fond.setLayout(null);
        add(fond);

        JLabel titre = new JLabel(
                "Itinéraire entre deux intersections (HO2 – Sens Unique)",
                SwingConstants.CENTER
        );
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        titre.setBounds(0, 20, 900, 40);
        fond.add(titre);

        // ===== Cadre =====
        JPanel cadre = new JPanel(null);
        cadre.setBackground(new Color(255, 255, 255, 230));
        cadre.setBounds(220, 100, 460, 400);
        fond.add(cadre);

        // ===== Champ départ =====
        JLabel labelDepart = new JLabel("Intersection de départ :");
        labelDepart.setBounds(30, 40, 300, 25);
        cadre.add(labelDepart);

        departField = new JTextField();
        departField.setBounds(30, 70, 400, 35);
        cadre.add(departField);

        // ===== Champ arrivée =====
        JLabel labelArrivee = new JLabel("Intersection d'arrivée :");
        labelArrivee.setBounds(30, 120, 300, 25);
        cadre.add(labelArrivee);

        arriveeField = new JTextField();
        arriveeField.setBounds(30, 150, 400, 35);
        cadre.add(arriveeField);

        // ===== Bouton calcul =====
        JButton boutonCalcul = new JButton("Calculer l'itinéraire");
        boutonCalcul.setBounds(95, 210, 260, 45);
        cadre.add(boutonCalcul);

        // ===== Zone résultat =====
        resultatArea = new JTextArea();
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(30, 270, 400, 100);
        cadre.add(scroll);

        // ===== Bouton retour =====
        JButton retour = new JButton("Retour");
        retour.setBounds(20, 500, 120, 35);
        fond.add(retour);

        retour.addActionListener(e -> {
            dispose();
            new Theme1_HO2(); // 🔥 Retour propre vers Thème HO2
        });

        boutonCalcul.addActionListener(e -> lancerCalcul());

        setVisible(true);
    }

    // ===========================
    //        Lancer calcul
    // ===========================
    private void lancerCalcul() {

        String depart = departField.getText().trim();
        String arrivee = arriveeField.getText().trim();

        if (depart.isEmpty() || arrivee.isEmpty()) {
            resultatArea.setText("Veuillez saisir deux intersections valides.");
            return;
        }

        try {
            CheminResult r = dijkstraOrienté(depart, arrivee);

            if (r == null) {
                resultatArea.setText("⚠ Aucun chemin possible à cause des sens uniques.");
            } else {
                resultatArea.setText(
                        "Rues traversées :\n" +
                                String.join(" → ", r.rues) +
                                "\n\nDistance totale : " + String.format("%.2f", r.distance) + " m"
                );
            }

        } catch (Exception e) {
            resultatArea.setText("Erreur : " + e.getMessage());
        }
    }

    // ----- Structure résultat -----
    private static class CheminResult {
        List<String> rues;
        double distance;
        CheminResult(List<String> rues, double distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    // ===========================
    //     DIJKSTRA ORIENTÉ
    // ===========================
    private CheminResult dijkstraOrienté(String depart, String arrivee) throws IOException {

        Map<String, Map<String, Double>> adj = new HashMap<>();
        Map<String, Map<String, String>> rueArc = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("nice_arcs.txt"));
        String line;

        while ((line = br.readLine()) != null) {

            String[] p = line.split(";");
            if (p.length != 4) continue;

            String a = p[0].trim();
            String b = p[1].trim();
            double w = Double.parseDouble(p[2].trim().replace(",", "."));
            String rue = p[3].trim();

            adj.putIfAbsent(a, new HashMap<>());
            rueArc.putIfAbsent(a, new HashMap<>());

            // 🔥 HO2 = ORIENTÉ
            adj.get(a).put(b, w);
            rueArc.get(a).put(b, rue);
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

            for (String v : adj.getOrDefault(cur, new HashMap<>()).keySet()) {

                double nd = dist.get(cur) + adj.get(cur).get(v);

                if (nd < dist.get(v)) {
                    dist.put(v, nd);
                    prec.put(v, cur);
                    pq.add(v);
                }
            }
        }

        if (!prec.containsKey(arrivee)) return null;

        // =============== Reconstruction du chemin ===============
        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (!cur.equals(depart)) {
            String p = prec.get(cur);
            rues.add(0, rueArc.get(p).get(cur));
            cur = p;
        }

        rues = enleverDoublons(rues);

        return new CheminResult(rues, dist.get(arrivee));
    }

    private List<String> enleverDoublons(List<String> r) {
        List<String> out = new ArrayList<>();
        String last = null;
        for (String s : r) {
            if (!s.equals(last)) out.add(s);
            last = s;
        }
        return out;
    }
}
