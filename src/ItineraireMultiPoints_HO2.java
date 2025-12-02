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

public class ItineraireMultiPoints_HO2 extends JFrame {

    private JTextField listeField;
    private JTextArea resultatArea;

    public ItineraireMultiPoints_HO2() {

        setTitle("Tournée multipoints (HO2 – Sens Unique)");
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

        JLabel titre = new JLabel("Tournée optimale – Hypothèse 2 (sens unique)", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        titre.setBounds(0, 20, 900, 40);
        fond.add(titre);

        JPanel cadre = new JPanel(null);
        cadre.setBackground(new Color(255, 255, 255, 230));
        cadre.setBounds(200, 100, 500, 400);
        fond.add(cadre);

        JLabel label = new JLabel("Intersections à visiter (séparées par des virgules) :");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(30, 30, 450, 25);
        cadre.add(label);

        listeField = new JTextField();
        listeField.setBounds(30, 60, 440, 35);
        cadre.add(listeField);

        JButton bouton = new JButton("Calculer la tournée");
        bouton.setBounds(120, 115, 260, 40);
        bouton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        cadre.add(bouton);

        resultatArea = new JTextArea();
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 180, 460, 180);
        cadre.add(scroll);

        JButton retour = new JButton("Retour");
        retour.setBounds(20, 500, 120, 35);
        fond.add(retour);

        retour.addActionListener(e -> {
            dispose();
            new Theme1_HO2();
        });

        bouton.addActionListener(e -> lancerCalcul());

        setVisible(true);
    }

    // =====================================================
    //                      LANCER CALCUL
    // =====================================================
    private void lancerCalcul() {

        String saisie = listeField.getText().trim();
        if (saisie.isEmpty()) {
            resultatArea.setText("Veuillez entrer au moins une intersection.");
            return;
        }

        List<String> points = new ArrayList<>();
        for (String p : saisie.split(",")) {
            if (!p.trim().isEmpty()) points.add(p.trim());
        }

        try {
            String res = calculerTournee(points);
            resultatArea.setText(res);
        } catch (Exception e) {
            resultatArea.setText("Aucune tournée possible avec ces intersections.");
        }
    }

    // =====================================================
    //                CHARGEMENT + TSP HEURISTIQUE
    // =====================================================
    private String calculerTournee(List<String> aVisiter) throws IOException {

        // ===== Charger graphe orienté =====
        Map<String, Map<String, Double>> adj = new HashMap<>();
        Map<String, Map<String, String>> rueArc = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("nice_arcs_orientes.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {

                String[] p = ligne.split(";");
                if (p.length != 4) continue;

                String a = p[0].trim();
                String b = p[1].trim();
                String distStr = p[2].trim().replace(",", ".");
                String rue = p[3].trim();

                double d;
                try { d = Double.parseDouble(distStr); }
                catch (Exception e) { continue; }

                adj.putIfAbsent(a, new HashMap<>());
                rueArc.putIfAbsent(a, new HashMap<>());

                adj.get(a).put(b, d);
                rueArc.get(a).put(b, rue);
            }
        }

        // Vérification sommets
        for (String s : aVisiter) {
            if (!adj.containsKey(s)) {
                return "Intersection inconnue : " + s;
            }
        }

        // ===== Plus proche voisin =====
        List<String> aVisiterCopy = new ArrayList<>(aVisiter);
        List<String> ordre = new ArrayList<>();

        String courant = aVisiterCopy.remove(0);
        ordre.add(courant);

        while (!aVisiterCopy.isEmpty()) {

            String best = null;
            double bestDist = Double.POSITIVE_INFINITY;

            for (String cible : aVisiterCopy) {
                double d = dijkstraDistance(adj, courant, cible);
                if (d < bestDist) {
                    bestDist = d;
                    best = cible;
                }
            }

            if (best == null || bestDist == Double.POSITIVE_INFINITY) {
                return "Aucune tournée possible avec ces intersections (sens uniques bloquants).";
            }

            ordre.add(best);
            aVisiterCopy.remove(best);
            courant = best;
        }

        // ===== RUES TRAVERSÉES =====
        List<String> ruesFinales = new ArrayList<>();
        double distanceTotale = 0;

        for (int i = 0; i < ordre.size() - 1; i++) {
            SegmentResult seg = dijkstraRues(adj, rueArc, ordre.get(i), ordre.get(i + 1));

            if (seg == null) {
                return "Aucune tournée possible avec ces intersections.";
            }

            ruesFinales.addAll(seg.rues);
            distanceTotale += seg.distance;
        }

        ruesFinales = enleverDoublons(ruesFinales);

        return "Ordre de visite :\n" + String.join(" → ", ordre) +
                "\n\nRues traversées :\n" + String.join(" → ", ruesFinales) +
                "\n\nDistance totale : " + String.format("%.2f", distanceTotale) + " m";
    }

    // =====================================================
    //                   DIJKSTRA DISTANCE
    // =====================================================
    private double dijkstraDistance(Map<String, Map<String, Double>> adj,
                                    String depart, String arrivee) {

        Map<String, Double> dist = new HashMap<>();
        for (String s : adj.keySet()) dist.put(s, Double.POSITIVE_INFINITY);
        dist.put(depart, 0.0);

        PriorityQueue<String> pq =
                new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(depart);

        while (!pq.isEmpty()) {

            String cur = pq.poll();
            if (cur.equals(arrivee)) return dist.get(arrivee);

            for (String v : adj.getOrDefault(cur, new HashMap<>()).keySet()) {
                double nd = dist.get(cur) + adj.get(cur).get(v);

                if (nd < dist.get(v)) {
                    dist.put(v, nd);
                    pq.add(v);
                }
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    // =====================================================
    //          DIJKSTRA POUR RECUPERER LES RUES
    // =====================================================
    private static class SegmentResult {
        List<String> rues;
        double distance;
        SegmentResult(List<String> rues, double distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    private SegmentResult dijkstraRues(Map<String, Map<String, Double>> adj,
                                       Map<String, Map<String, String>> rueArc,
                                       String depart, String arrivee) {

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prec = new HashMap<>();

        for (String s : adj.keySet()) dist.put(s, Double.POSITIVE_INFINITY);
        dist.put(depart, 0.0);

        PriorityQueue<String> pq =
                new PriorityQueue<>(Comparator.comparingDouble(dist::get));
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

        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            rues.add(0, rueArc.get(p).get(cur));
            cur = p;
        }

        return new SegmentResult(rues, dist.get(arrivee));
    }

    // =====================================================
    //           SUPPRESSION DOUBLONS SUCCESSIFS
    // =====================================================
    private List<String> enleverDoublons(List<String> r) {
        List<String> out = new ArrayList<>();
        String last = null;
        for (String s : r) {
            if (!s.equals(last)) {
                out.add(s);
            }
            last = s;
        }
        return out;
    }
}
