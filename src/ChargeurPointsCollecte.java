import java.io.*;
import java.util.*;

public class ChargeurPointsCollecte {

    public static Depot chargerDepot(String fichier) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String line = br.readLine();

        if (line == null || !line.startsWith("Depot;")) {
            br.close();
            throw new Exception("Format incorrect : pas de depot");
        }

        String[] p = line.split(";");
        String sommetId = p[1];   // ✔ ID OSM en String

        br.close();
        return new Depot(sommetId);
    }

    public static List<PointCollecte> chargerPoints(String fichier) throws Exception {
        List<PointCollecte> points = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fichier));

        // ignorer la ligne du dépôt
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {

            String[] p = line.split(";");
            String nom = p[0];
            String sommetId = p[1];        // ✔ ID OSM en String
            int contenance = Integer.parseInt(p[2]);

            points.add(new PointCollecte(nom, sommetId, contenance));
        }

        br.close();
        return points;
    }
}
