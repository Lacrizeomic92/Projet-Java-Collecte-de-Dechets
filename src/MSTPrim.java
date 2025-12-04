import java.util.*;

public class MSTPrim {

    public static MSTPrimResult construire(List<PointCollecte> points,
                                           Depot depot) throws Exception {

        // =========================================================
        //       ÉTAPE 1 : Construire la liste des IDs
        // =========================================================
        List<String> ids = new ArrayList<>();
        ids.add(depot.getSommetId());       // index 0 = dépôt

        for (PointCollecte pc : points)
            ids.add(pc.getSommetId());      // index 1..n = points

        int n = ids.size();

        // =========================================================
        //   ÉTAPE 2 : Initialisation des structures du Prim
        // =========================================================
        boolean[] in = new boolean[n];               // inclus dans MST ?
        double[] min = new double[n];                // meilleure distance connue
        int[] parent = new int[n];                   // parent dans le MST

        Arrays.fill(min, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);                     // parent inconnu par défaut

        min[0] = 0;      // Le dépôt est la racine du MST
        parent[0] = -1;  // pas de parent

        // =========================================================
        //                ÉTAPE 3 : Algorithme de Prim
        // =========================================================
        for (int iter = 0; iter < n - 1; iter++) {

            // 1) Choisir le sommet non inclus avec plus petite clé
            int u = -1;

            for (int i = 0; i < n; i++) {
                if (!in[i] && (u == -1 || min[i] < min[u])) {
                    u = i;
                }
            }

            // Impossible de trouver un u valide → graphe déconnecté
            if (u == -1) break;

            in[u] = true;

            // 2) Mise à jour des distances des voisins
            for (int v = 0; v < n; v++) {

                if (in[v] || u == v) continue;

                // Calculer le plus court chemin via Dijkstra
                DijkstraNice.CheminResult res =
                        DijkstraNice.dijkstra(ids.get(u), ids.get(v));

                if (res == null) continue;  // si pas de chemin → ignorer

                double d = res.distance;

                // Si c'est meilleur → on met à jour
                if (d < min[v]) {
                    min[v] = d;
                    parent[v] = u;
                }
            }
        }

        // =========================================================
        //     ÉTAPE 4 : Retourner le résultat complet
        // =========================================================
        return new MSTPrimResult(parent, ids);
    }
}
