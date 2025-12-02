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
import java.util.Arrays;

public class ItineraireMultiPoints extends JFrame {

    private JTextField listeIntersectionsField;
    private JTextArea resultatArea;

    public ItineraireMultiPoints() {

        setTitle("Tournée optimale entre plusieurs intersections");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==== FOND ====
        JPanel panneauFond = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(new Color(245, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());

                try {
                    Image camion = new ImageIcon("camion.png").getImage();
                    g.drawImage(camion, 20, 380, 230, 150, this);
                } catch (Exception ignored) {}
            }
        };

        panneauFond.setLayout(null);
        panneauFond.setFocusable(true);
        add(panneauFond);

        JLabel titre = new JLabel("Tournée optimale entre plusieurs intersections",
                SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        panneauFond.add(titre);

        JPanel cadre = new JPanel(null);
        cadre.setBackground(new Color(255, 255, 255, 230));
        cadre.setBounds(200, 100, 500, 400);
        cadre.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));
        panneauFond.add(cadre);

        JLabel labelListe = new JLabel("Intersections à visiter (séparées par des virgules) :");
        labelListe.setFont(new Font("Arial", Font.PLAIN, 16));
        labelListe.setBounds(30, 30, 400, 25);
        cadre.add(labelListe);

        listeIntersectionsField = new JTextField();
        listeIntersectionsField.setBounds(30, 60, 440, 35);
        listeIntersectionsField.setFont(new Font("Arial", Font.PLAIN, 15));
        cadre.add(listeIntersectionsField);

        JButton bouton = new JButton("Calculer la tournée");
        bouton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        bouton.setBounds(120, 115, 260, 40);
        bouton.setBackground(new Color(230, 250, 230));
        bouton.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 150), 2, true));
        cadre.add(bouton);

        resultatArea = new JTextArea();
        resultatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 180, 460, 160);
        cadre.add(scroll);

        JButton boutonRetour = new JButton("Retour");
        boutonRetour.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        boutonRetour.setBounds(200, 350, 100, 35);
        boutonRetour.setBackground(new Color(255, 230, 230));
        boutonRetour.setBorder(BorderFactory.createLineBorder(new Color(200, 130, 130), 2, true));
        cadre.add(boutonRetour);

        boutonRetour.addActionListener(e -> {
            dispose();
            new Theme1();
        });

        bouton.addActionListener(e -> {
            try {
                String saisie = listeIntersectionsField.getText().trim();
                if (saisie.isEmpty()) {
                    resultatArea.setText("Veuillez entrer au moins une intersection.");
                    return;
                }

                List<String> ids = Arrays.asList(saisie.split(","));
                for (int i = 0; i < ids.size(); i++)
                    ids.set(i, ids.get(i).trim());

                String tournee = calculerTournee(ids);
                resultatArea.setText(tournee);

            } catch (Exception ex) {
                resultatArea.setText("Erreur : " + ex.getMessage());
            }
        });

        panneauFond.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme1();
                }
            }
        });

        setVisible(true);
    }

    // ======================================================================
    //  Résultat Dijkstra (distance + liste de rues)
    // ======================================================================
    private static class ResultatDijkstra {
        double distance;
        List<String> rues;

        ResultatDijkstra(double distance, List<String> rues) {
            this.distance = distance;
            this.rues = rues;
        }
    }

    // ======================================================================
    //                CALCUL TOURNEE AVEC DIJKSTRA
    // ======================================================================
    private String calculerTournee(List<String> aVisiter) throws IOException {

        Map<String, Map<String, Double>> adj = new HashMap<>();
        Map<String, Map<String, String>> rues = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("nice_arcs.txt"));
        String ligne;

        while ((ligne = br.readLine()) != null) {
            String[] p = ligne.split(";");
            if (p.length != 4) continue;

            String id1 = p[0].trim();
            String id2 = p[1].trim();
            double d = Double.parseDouble(p[2].trim().replace(",", "."));
            String rue = p[3].trim();

            adj.putIfAbsent(id1, new HashMap<>());
            adj.putIfAbsent(id2, new HashMap<>());
            rues.putIfAbsent(id1, new HashMap<>());
            rues.putIfAbsent(id2, new HashMap<>());

            adj.get(id1).put(id2, d);
            adj.get(id2).put(id1, d);

            rues.get(id1).put(id2, rue);
            rues.get(id2).put(id1, rue);
        }
        br.close();

        for (String s : aVisiter)
            if (!adj.containsKey(s))
                return "⚠ Intersection inconnue : " + s;

        List<String> nonVisite = new ArrayList<>(aVisiter);
        List<String> ordre = new ArrayList<>();
        List<String> ruesFinales = new ArrayList<>();

        double distanceTotale = 0;

        String courant = nonVisite.get(0);
        ordre.add(courant);
        nonVisite.remove(courant);

        while (!nonVisite.isEmpty()) {

            String prochain = null;
            double best = Double.POSITIVE_INFINITY;
            ResultatDijkstra bestRes = null;

            for (String cible : nonVisite) {
                ResultatDijkstra res = dijkstra(courant, cible, adj, rues);

                if (res.distance < best) {
                    best = res.distance;
                    prochain = cible;
                    bestRes = res;
                }
            }

            distanceTotale += bestRes.distance;

            // 🔥 Nettoyage des doublons consécutifs ici
            ruesFinales.addAll(supprimerDoublonsConsecutifs(bestRes.rues));

            ordre.add(prochain);
            nonVisite.remove(prochain);
            courant = prochain;
        }

        return "Tournée (intersections) :\n" +
                String.join(" → ", ordre) +
                "\n\nDistance totale : " + String.format("%.2f", distanceTotale) + " m" +
                "\n\nRues parcourues :\n" +
                String.join(" → ", supprimerDoublonsConsecutifs(ruesFinales));
    }

    // ======================================================================
    //    SUPPRESSION DES DOUBLONS CONSECUTIFS
    // ======================================================================
    private List<String> supprimerDoublonsConsecutifs(List<String> r) {
        List<String> out = new ArrayList<>();
        String prev = null;

        for (String s : r) {
            if (!s.equals(prev)) {
                out.add(s);
            }
            prev = s;
        }
        return out;
    }

    // ======================================================================
    //                        DIJKSTRA
    // ======================================================================
    private ResultatDijkstra dijkstra(String depart, String arrivee,
                                      Map<String, Map<String, Double>> adj,
                                      Map<String, Map<String, String>> rues) {

        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prec = new HashMap<>();

        for (String s : adj.keySet())
            dist.put(s, Double.POSITIVE_INFINITY);

        dist.put(depart, 0.0);

        PriorityQueue<String> pq =
                new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(depart);

        while (!pq.isEmpty()) {
            String cur = pq.poll();

            if (cur.equals(arrivee)) break;

            for (var entry : adj.get(cur).entrySet()) {
                String v = entry.getKey();
                double w = entry.getValue();

                double nd = dist.get(cur) + w;

                if (nd < dist.get(v)) {
                    dist.put(v, nd);
                    prec.put(v, cur);
                    pq.add(v);
                }
            }
        }

        if (!prec.containsKey(arrivee))
            return new ResultatDijkstra(Double.POSITIVE_INFINITY, new ArrayList<>());

        List<String> out = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            out.add(0, rues.get(p).get(cur));
            cur = p;
        }

        return new ResultatDijkstra(dist.get(arrivee), out);
    }
}
