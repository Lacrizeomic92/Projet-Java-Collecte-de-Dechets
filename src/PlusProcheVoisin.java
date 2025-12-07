import java.util.*;
import java.io.IOException;

public class PlusProcheVoisin {


    public static class Etape {
        public String depart;
        public String arrivee;
        public double distance;
        public List<String> chemin;

        public Etape(String depart, String arrivee, double distance, List<String> chemin) {
            this.depart = depart;
            this.arrivee = arrivee;
            this.distance = distance;
            this.chemin = chemin;
        }
    }

    public static class ResultatPPV {
        public List<Etape> etapes = new ArrayList<>();
        public double distanceTotale = 0.0;
    }

    public static ResultatPPV calculer(Depot depot, List<PointCollecte> points)
            throws Exception {

        List<PointCollecte> restant = new ArrayList<>(points);
        ResultatPPV resultat = new ResultatPPV();

        String courant = depot.getSommetId();

        while (!restant.isEmpty()) {

            PointCollecte meilleur = null;
            double meilleureDist = Double.MAX_VALUE;
            DijkstraNice.CheminResult meilleurChemin = null;

            for (PointCollecte pc : restant) {
                DijkstraNice.CheminResult res;

                try {
                    res = DijkstraNice.dijkstra(courant, pc.getSommetId());
                } catch (IOException e) {
                    throw new Exception("Erreur fichier nice_arcs.txt", e);
                }

                if (res != null && res.distance < meilleureDist) {
                    meilleureDist = res.distance;
                    meilleur = pc;
                    meilleurChemin = res;
                }
            }

            if (meilleur == null) {
                throw new Exception("Aucun point accessible depuis : " + courant);
            }

            resultat.etapes.add(
                    new Etape(
                            courant,
                            meilleur.getSommetId(),
                            meilleureDist,
                            meilleurChemin.rues
                    )
            );

            resultat.distanceTotale += meilleureDist;

            courant = meilleur.getSommetId();
            restant.remove(meilleur);
        }

        try {
            DijkstraNice.CheminResult retour = DijkstraNice.dijkstra(courant, depot.getSommetId());

            if (retour != null) {
                resultat.etapes.add(
                        new Etape(
                                courant,
                                depot.getSommetId(),
                                retour.distance,
                                retour.rues
                        )
                );

                resultat.distanceTotale += retour.distance;
            }

        } catch (IOException e) {
            throw new Exception("Erreur calcul retour dépôt", e);
        }

        return resultat;
    }
}
