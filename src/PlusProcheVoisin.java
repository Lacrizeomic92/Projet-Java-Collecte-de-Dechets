import java.util.*;

public class PlusProcheVoisin {

    public static List<PointCollecte> calculer(Depot depot,
                                               List<PointCollecte> points)
            throws Exception {

        List<PointCollecte> restant = new ArrayList<>(points);
        List<PointCollecte> ordre = new ArrayList<>();

        String courant = depot.getSommetId();


        while (!restant.isEmpty()) {

            PointCollecte meilleur = null;
            double meilleureDist = Double.MAX_VALUE;

            for (PointCollecte pc : restant) {

                DijkstraNice.CheminResult res =
                        DijkstraNice.dijkstra(courant, pc.getSommetId());

                if (res != null && res.distance < meilleureDist) {
                    meilleureDist = res.distance;
                    meilleur = pc;
                }
            }

            if (meilleur == null) {
                throw new Exception("Aucun point accessible depuis : " + courant);
            }

            ordre.add(meilleur);
            courant = meilleur.getSommetId();
            restant.remove(meilleur);
        }

        return ordre;
    }
}
