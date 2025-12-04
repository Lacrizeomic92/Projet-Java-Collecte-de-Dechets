import java.util.*;

public class MatriceDistances {

    /**
     * Construit la matrice complète des distances entre
     * tous les points + le dépôt.
     *
     * ids.get(0) = dépôt
     * ids.get(i) = sommet du point i
     */
    public static double[][] construireMatrice(List<String> ids) throws Exception {

        int n = ids.size();
        double[][] dist = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (i == j) {
                    dist[i][j] = 0;
                    continue;
                }

                DijkstraNice.CheminResult res =
                        DijkstraNice.dijkstra(ids.get(i), ids.get(j));

                if (res == null)
                    dist[i][j] = Double.POSITIVE_INFINITY;
                else
                    dist[i][j] = res.distance;
            }
        }
        return dist;
    }
}
