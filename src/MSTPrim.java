import java.util.*;

public class MSTPrim {

    public static MSTPrimResult construire(List<PointCollecte> points,
                                           Depot depot) throws Exception {

        // --- 1) Construire la liste d'IDs ---
        List<String> ids = new ArrayList<>();
        ids.add(depot.getSommetId());
        for (PointCollecte pc : points)
            ids.add(pc.getSommetId());

        int n = ids.size();

        // --- 2) Matrice complète des distances ---
        double[][] poids = MatriceDistances.construireMatrice(ids);

        // --- 3) Structures de Prim ---
        boolean[] in = new boolean[n];
        double[] min = new double[n];
        int[] parent = new int[n];

        Arrays.fill(min, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        min[0] = 0; // le dépôt est racine

        // --- 4) Algorithme de Prim ---
        for (int iter = 0; iter < n; iter++) {

            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!in[i] && (u == -1 || min[i] < min[u])) {
                    u = i;
                }
            }

            if (u == -1) break;
            in[u] = true;

            for (int v = 0; v < n; v++) {
                if (!in[v] && poids[u][v] < min[v]) {
                    min[v] = poids[u][v];
                    parent[v] = u;
                }
            }
        }

        return new MSTPrimResult(parent, ids, poids);
    }
}
