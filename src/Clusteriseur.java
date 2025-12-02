import java.util.*;

public class Clusteriseur {
    public static HashMap<Integer, ArrayList<NoeudSecteur>> clusteriser(SecteurGraphe g, int k) {
        HashMap<Integer, ArrayList<NoeudSecteur>> clusters = new HashMap<>();
        for (int i = 0; i < k; i++) clusters.put(i, new ArrayList<>());

        Random rand = new Random(123);
        ArrayList<NoeudSecteur> graines = new ArrayList<>();
        for (int i = 0; i < k; i++) graines.add(g.noeuds.get(rand.nextInt(g.noeuds.size())));

        for (NoeudSecteur n : g.noeuds) {
            int meilleur = 0;
            double distMin = Double.MAX_VALUE;
            for (int i = 0; i < k; i++) {
                double d = Math.pow(n.x - graines.get(i).x, 2) + Math.pow(n.y - graines.get(i).y, 2);
                if (d < distMin) { distMin = d; meilleur = i; }
            }
            clusters.get(meilleur).add(n);
        }

        System.out.println("Clusters:");
        for (int i = 0; i < k; i++) System.out.println("  " + i + ": " + clusters.get(i).size());
        return clusters;
    }
}
