import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ItineraireMultiPoints extends JFrame {

    private JTextField listeIntersectionsField;
    private JTextArea resultatArea;

    private Graphe grapheCirculation; // 🔥 Graphe dynamique mis à jour par la Mairie

    public ItineraireMultiPoints() {

        // 🔥 Toujours récupérer un graphe valide (même si Collectivité n'a rien modifié)
        this.grapheCirculation = Collectivite.getGrapheCirculation();

        setTitle("Tournée optimale entre plusieurs intersections");
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

        JLabel titre = new JLabel("Tournée optimale entre plusieurs intersections", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        fond.add(titre);

        JPanel cadre = new JPanel(null);
        cadre.setBounds(200, 100, 500, 400);
        cadre.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        cadre.setBackground(Color.WHITE);
        fond.add(cadre);

        JLabel labelListe = new JLabel("Intersections à visiter (séparées par des virgules) :");
        labelListe.setBounds(30, 30, 430, 25);
        cadre.add(labelListe);

        listeIntersectionsField = new JTextField();
        listeIntersectionsField.setBounds(30, 60, 440, 35);
        cadre.add(listeIntersectionsField);

        JButton bouton = new JButton("Calculer la tournée");
        bouton.setBounds(120, 115, 260, 40);
        bouton.addActionListener(this::calculerTourneeAction);
        cadre.add(bouton);

        resultatArea = new JTextArea();
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 180, 460, 160);
        cadre.add(scroll);

        JButton retour = new JButton("Retour");
        retour.setBounds(200, 350, 100, 35);
        retour.addActionListener(e -> { dispose(); new Theme1(); });
        cadre.add(retour);

        setVisible(true);
    }

    // ================================================================
    //      ACTION BOUTON → LANCEMENT DU CALCUL
    // ================================================================
    private void calculerTourneeAction(ActionEvent e) {

        String saisie = listeIntersectionsField.getText().trim();

        if (saisie.isEmpty()) {
            resultatArea.setText("Veuillez entrer au moins une intersection.");
            return;
        }

        List<String> ids = new ArrayList<>();
        for (String token : saisie.split(",")) {
            ids.add(token.trim());
        }

        resultatArea.setText(calculerTournee(ids));
    }

    // ================================================================
    //          CALCUL TOURNEE AVEC NOMS DES RUES
    // ================================================================
    private String calculerTournee(List<String> aVisiter) {

        // --- Construire le graphe utilisable pour Dijkstra ---
        Map<String, Map<String, Integer>> adj = new HashMap<>();
        Map<String, Map<String, String>> nomsRue = new HashMap<>();

        // Initialisation des sommets
        for (Graphe.Node n : grapheCirculation.nodes) {
            adj.putIfAbsent(n.id, new HashMap<>());
            nomsRue.putIfAbsent(n.id, new HashMap<>());
        }

        // Ajout des arêtes ouvertes
        for (Graphe.Edge e : grapheCirculation.edges) {

            if (e.fermee) continue;

            adj.get(e.from).put(e.to, e.distance);
            nomsRue.get(e.from).put(e.to, e.nomRue);

            if (!"ONE_WAY".equalsIgnoreCase(e.sens)) {
                adj.get(e.to).put(e.from, e.distance);
                nomsRue.get(e.to).put(e.from, e.nomRue);
            }
        }

        // Vérification que tout existe
        for (String s : aVisiter)
            if (!adj.containsKey(s))
                return "⚠ Intersection inconnue : " + s;

        List<String> nonVisite = new ArrayList<>(aVisiter);
        List<String> ordre = new ArrayList<>();
        List<String> ruesParcourues = new ArrayList<>();

        double distanceTotale = 0;

        String courant = nonVisite.get(0);
        ordre.add(courant);
        nonVisite.remove(courant);

        while (!nonVisite.isEmpty()) {

            double bestDist = Double.POSITIVE_INFINITY;
            String bestCible = null;
            ResultatDijkstra bestPath = null;

            for (String cible : nonVisite) {

                ResultatDijkstra res = dijkstra(courant, cible, adj, nomsRue);

                if (res.distance < bestDist) {
                    bestDist = res.distance;
                    bestCible = cible;
                    bestPath = res;
                }
            }

            if (bestPath == null || bestDist == Double.POSITIVE_INFINITY) {
                return "⚠ Aucun chemin possible vers : " + nonVisite.get(0);
            }

            distanceTotale += bestDist;
            ruesParcourues.addAll(
                    supprimerDoublonsConsecutifs(bestPath.rues)
            );

            ordre.add(bestCible);
            nonVisite.remove(bestCible);
            courant = bestCible;
        }

        return "Tournée (intersections) :\n" +
                String.join(" → ", ordre) +
                "\n\nDistance totale : " + String.format("%.2f", distanceTotale) + " m" +
                "\n\nRues parcourues :\n" +
                String.join(" → ", supprimerDoublonsConsecutifs(ruesParcourues));
    }

    // ================================================================
    //                     DIJKSTRA AVEC NOMS DES RUES
    // ================================================================
    private static class ResultatDijkstra {
        double distance;
        List<String> rues;
        ResultatDijkstra(double distance, List<String> rues) {
            this.distance = distance;
            this.rues = rues;
        }
    }

    private ResultatDijkstra dijkstra(String depart, String arrivee,
                                      Map<String, Map<String, Integer>> adj,
                                      Map<String, Map<String, String>> nomsRue) {

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

            for (var entry : adj.get(cur).entrySet()) {
                String v = entry.getKey();
                int w = entry.getValue();

                double newDist = dist.get(cur) + w;

                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prec.put(v, cur);
                    pq.add(v);
                }
            }
        }

        if (!prec.containsKey(arrivee))
            return new ResultatDijkstra(Double.POSITIVE_INFINITY, new ArrayList<>());

        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            String rue = nomsRue.get(p).get(cur);
            if (rue == null) rue = "(Rue inconnue)";
            rues.add(0, rue);
            cur = p;
        }

        return new ResultatDijkstra(dist.get(arrivee), rues);
    }

    // ================================================================
    //         SUPPRESSION DES DOUBLONS DE RUES SUCCESSIFS
    // ================================================================
    private List<String> supprimerDoublonsConsecutifs(List<String> r) {
        List<String> out = new ArrayList<>();
        String prev = null;

        for (String s : r) {
            if (!s.equals(prev)) out.add(s);
            prev = s;
        }
        return out;
    }
}
