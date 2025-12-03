import java.io.*;
import java.util.*;

public class DijkstraNice {

    public static class CheminResult {
        public List<String> rues;
        public double distance;

        public CheminResult(List<String> rues, double distance) {
            this.rues = rues;
            this.distance = distance;
        }
    }

    public static CheminResult dijkstra(String depart, String arrivee) throws IOException {

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

        // Reconstruction
        List<String> rues = new ArrayList<>();
        String cur = arrivee;

        while (prec.containsKey(cur)) {
            String p = prec.get(cur);
            String rue = rueParArc.get(p).get(cur);
            rues.add(0, rue);
            cur = p;
        }

        rues = supprimerDoublonsConsecutifs(rues);

        return new CheminResult(rues, dist.get(arrivee));
    }

    private static List<String> supprimerDoublonsConsecutifs(List<String> rues) {
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
}
