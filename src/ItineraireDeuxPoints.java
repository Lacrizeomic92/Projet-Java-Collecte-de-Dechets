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

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ItineraireDeuxPoints extends JFrame {

    private JTextField departField;
    private JTextField arriveeField;
    private JTextArea resultatArea;

    // Graphe mis à jour par la collectivité
    private Graphe grapheCirculation;

    public ItineraireDeuxPoints() {

        // Toujours récupérer la version mise à jour :
        this.grapheCirculation = Collectivite.getGrapheCirculation();

        setTitle("Itinéraire optimal entre deux intersections");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 255, 240));
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
        retour.addActionListener(e -> {
            dispose();
            new Theme1();
        });
        panel.add(retour);

        setVisible(true);
    }

    // ===================================================================
    //                       DIJKSTRA AVEC NOMS DE RUES
    // ===================================================================
    private CheminResult dijkstra(String depart, String arrivee) {

        // adj : sommet -> (voisin -> distance)
        Map<String, Map<String, Integer>> adj = new HashMap<>();
        // rues : sommet -> (voisin -> nomRue)
        Map<String, Map<String, String>> rues = new HashMap<>();

        // 1) Ajouter tous les nœuds (même isolés)
        for (Graphe.Node n : grapheCirculation.nodes) {
            adj.putIfAbsent(n.id, new HashMap<>());
            rues.putIfAbsent(n.id, new HashMap<>());
        }

        // 2) Ajouter seulement les arêtes OUVERTES
        for (Graphe.Edge e : grapheCirculation.edges) {

            if (e.fermee) continue; // Rue fermée = ignorée

            // Direction principale
            adj.get(e.from).put(e.to, e.distance);
            rues.get(e.from).put(e.to, e.nomRue != null ? e.nomRue : "(Rue inconnue)");

            // Retour si ce n'est pas ONE_WAY
            if (!"ONE_WAY".equalsIgnoreCase(e.sens)) {
                adj.get(e.to).put(e.from, e.distance);
                rues.get(e.to).put(e.from, e.nomRue != null ? e.nomRue : "(Rue inconnue)");
            }
        }

        // Vérifier que les intersections existent
        if (!adj.containsKey(depart) || !adj.containsKey(arrivee)) {
            return null;
        }

        // 3) Dijkstra classique
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prec = new HashMap<>();

        for (String s : adj.keySet()) {
            dist.put(s, Integer.MAX_VALUE);
        }
        dist.put(depart, 0);

        PriorityQueue<String> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(depart);

        while (!pq.isEmpty()) {

            String cur = pq.poll();
            if (cur.equals(arrivee)) break;

            for (var entry : adj.get(cur).entrySet()) {

                String voisin = entry.getKey();
                int poids = entry.getValue();

                int newDist = dist.get(cur) + poids;

                if (newDist < dist.get(voisin)) {
                    dist.put(voisin, newDist);
                    prec.put(voisin, cur);
                    pq.add(voisin);
                }
            }
        }

        if (dist.get(arrivee) == Integer.MAX_VALUE) {
            return null; // aucun chemin possible
        }

        // 4) Reconstruction : on récupère les NOMS des rues, pas les sommets
        List<String> nomsRues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            String rue = rues.get(p).get(cur);
            if (rue == null) rue = "(Rue inconnue)";
            nomsRues.add(0, rue);
            cur = p;
        }

        // Optionnel : supprimer les doublons successifs de rues
        nomsRues = supprimerDoublonsConsecutifs(nomsRues);

        return new CheminResult(nomsRues, dist.get(arrivee));
    }

    private List<String> supprimerDoublonsConsecutifs(List<String> liste) {
        List<String> out = new ArrayList<>();
        String prev = null;
        for (String r : liste) {
            if (!r.equals(prev)) {
                out.add(r);
            }
            prev = r;
        }
        return out;
    }

    // Structure du résultat
    private static class CheminResult {
        List<String> rues;
        int distance;

        CheminResult(List<String> rues, int distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    // ===================================================================
    //                   ACTION DU BOUTON CALCULER
    // ===================================================================
    private void calculer(ActionEvent e) {

        String dep = departField.getText().trim();
        String arr = arriveeField.getText().trim();

        if (dep.isEmpty() || arr.isEmpty()) {
            resultatArea.setText("Veuillez saisir deux identifiants d’intersections.");
            return;
        }

        CheminResult res = dijkstra(dep, arr);

        if (res == null) {
            resultatArea.setText("Aucun chemin trouvé (rue fermée ou sens unique bloquant).");
        } else {
            resultatArea.setText(
                    "Rues traversées :\n" +
                            String.join(" → ", res.rues) +
                            "\n\nDistance totale : " + res.distance + " m"
            );
        }
    }
}

