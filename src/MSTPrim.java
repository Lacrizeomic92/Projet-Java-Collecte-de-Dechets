import java.util.*;

public class MSTPrim {

    public static int[] construire(List<PointCollecte> points,
                                   Depot depot) throws Exception {

        // IDs : dépôt + points
        List<String> ids = new ArrayList<>();
        ids.add(depot.getSommetId());
        for (PointCollecte pc : points) ids.add(pc.getSommetId());

        int n = ids.size();
        boolean[] in = new boolean[n];
        double[] min = new double[n];
        int[] parent = new int[n];

        Arrays.fill(min, Double.POSITIVE_INFINITY);
        min[0] = 0;      // dépôt
        parent[0] = -1;  // racine du MST

        for (int iter = 0; iter < n - 1; iter++) {

            // 1) On choisit le sommet non inclus avec la plus petite clé
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!in[i] && (u == -1 || min[i] < min[u])) {
                    u = i;
                }
            }

            in[u] = true;

            // 2) Mise à jour des voisins
            for (int v = 0; v < n; v++) {

                if (in[v] || u == v) continue;

                DijkstraNice.CheminResult res =
                        DijkstraNice.dijkstra(ids.get(u), ids.get(v));

                if (res == null) continue;

                double d = res.distance;

                if (d < min[v]) {
                    min[v] = d;
                    parent[v] = u;
                }
            }
        }

        return parent;
    }
}
