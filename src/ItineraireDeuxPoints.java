import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ItineraireDeuxPoints extends JFrame {

    private JTextField departField;
    private JTextField arriveeField;
    private JTextArea resultatArea;

    // Graphe mis à jour par la collectivité
    private Graphe grapheCirculation;

    public ItineraireDeuxPoints() {

        // Récupération du graphe global mis à jour
        this.grapheCirculation = Collectivite.getGrapheCirculation();

        setTitle("Itinéraire optimal entre deux intersections");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 255, 240)); // vert très clair
        add(panel);

        JLabel titre = new JLabel("Itinéraire optimal entre deux intersections", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        panel.add(titre);

        JPanel cadre = new JPanel(null);
        cadre.setBackground(Color.WHITE);
        cadre.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        cadre.setBounds(200, 100, 500, 380);
        panel.add(cadre);

        JLabel labelDepart = new JLabel("Intersection de départ :");
        labelDepart.setBounds(30, 30, 200, 30);
        cadre.add(labelDepart);

        departField = new JTextField();
        departField.setBounds(30, 60, 430, 35);
        cadre.add(departField);

        JLabel labelArrivee = new JLabel("Intersection d'arrivée :");
        labelArrivee.setBounds(30, 110, 200, 30);
        cadre.add(labelArrivee);

        arriveeField = new JTextField();
        arriveeField.setBounds(30, 140, 430, 35);
        cadre.add(arriveeField);

        JButton bouton = new JButton("Calculer l'itinéraire");
        bouton.setBounds(120, 200, 260, 45);
        bouton.addActionListener(this::calculer);
        cadre.add(bouton);

        resultatArea = new JTextArea();
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 260, 460, 100);
        cadre.add(scroll);

        JButton retour = new JButton("Retour");
        retour.setBounds(20, 520, 120, 30);
        retour.addActionListener(e -> { dispose(); new Theme1(); });
        panel.add(retour);

        setVisible(true);
    }

    // ==============================
    //       ALGORITHME DIJKSTRA
    // ==============================
    private CheminResult dijkstra(String depart, String arrivee) {

        Map<String, Map<String, Double>> adj = new HashMap<>();
        Map<String, Map<String, String>> nomRue = new HashMap<>();

        // Construire le graphe en mémoire
        for (Graphe.Edge e : grapheCirculation.edges) {

            if (e.fermee) continue; // Rue fermée → ignorée

            adj.putIfAbsent(e.from, new HashMap<>());
            nomRue.putIfAbsent(e.from, new HashMap<>());

            adj.get(e.from).put(e.to, (double) e.distance);
            nomRue.get(e.from).put(e.to, e.sens);

            if (!"ONE_WAY".equalsIgnoreCase(e.sens)) {
                adj.putIfAbsent(e.to, new HashMap<>());
                nomRue.putIfAbsent(e.to, new HashMap<>());
                adj.get(e.to).put(e.from, (double) e.distance);
                nomRue.get(e.to).put(e.from, e.sens);
            }
        }

        if (!adj.containsKey(depart) || !adj.containsKey(arrivee)) return null;

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
                String voisin = entry.getKey();
                double poids = entry.getValue();

                double newDist = dist.get(cur) + poids;

                if (newDist < dist.get(voisin)) {
                    dist.put(voisin, newDist);
                    prec.put(voisin, cur);
                    pq.add(voisin);
                }
            }
        }

        if (dist.get(arrivee) == Double.POSITIVE_INFINITY) return null;

        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            rues.add(0, p + " → " + cur);
            cur = p;
        }

        return new CheminResult(rues, dist.get(arrivee));
    }

    // Structure résultat
    private static class CheminResult {
        List<String> rues;
        double distance;

        CheminResult(List<String> rues, double distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    // ==============================
    //         ACTION BOUTON
    // ==============================
    private void calculer(ActionEvent e) {
        String dep = departField.getText().trim();
        String arr = arriveeField.getText().trim();

        if (dep.isEmpty() || arr.isEmpty()) {
            resultatArea.setText("Veuillez saisir deux identifiants d’intersections.");
            return;
        }

        CheminResult res = dijkstra(dep, arr);

        if (res == null) {
            resultatArea.setText("Aucun chemin trouvé (peut-être à cause d’une rue fermée).");
        } else {
            resultatArea.setText(
                    "Rues traversées :\n" + String.join("\n", res.rues) +
                            "\n\nDistance totale : " + String.format("%.2f", res.distance) + " m"
            );
        }
    }
}
