import java.io.*;
import java.util.*;

public class ChargeurTheme2 {

    public static class DonneesTheme2 {
        public Depot depot;
        public List<PointCollecte> points;
        public Map<String, Map<String, Double>> dist; // dist[A][B] = distance
    }

    public static DonneesTheme2 charger(String fichier) throws Exception {

        DonneesTheme2 dt2 = new DonneesTheme2();
        dt2.points = new ArrayList<>();
        dt2.dist = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(fichier));

        // ===== LECTURE DU DEPÔT =====
        String ligne = br.readLine();
        if (ligne == null || !ligne.startsWith("Depot;"))
            throw new Exception("Fichier invalide : pas de dépôt");

        String[] p = ligne.split(";");
        dt2.depot = new Depot(p[1]);

        // ===== LECTURE DES POINTS =====
        while ((ligne = br.readLine()) != null && !ligne.equals("DISTANCES")) {

            String[] t = ligne.split(";");
            if (t.length != 3) continue;

            String nom = t[0];
            String id = t[1];
            int cont = Integer.parseInt(t[2]);

            dt2.points.add(new PointCollecte(nom, id, cont));
        }

        // ===== LECTURE DES DISTANCES =====
        String line;
        while ((line = br.readLine()) != null) {

            String[] t = line.split(";");
            if (t.length != 3) continue;

            String a = t[0];
            String b = t[1];
            double d = Double.parseDouble(t[2]);

            dt2.dist.putIfAbsent(a, new HashMap<>());
            dt2.dist.putIfAbsent(b, new HashMap<>());
            dt2.dist.get(a).put(b, d);
            dt2.dist.get(b).put(a, d);
        }

        br.close();
        return dt2;
    }
}
