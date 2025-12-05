import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class GrapheLoaderCirculation {

    public static Graphe charger(String fichier) {

        Graphe g = new Graphe();

        HashSet<String> edgesSet = new HashSet<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne;

            while ((ligne = br.readLine()) != null) {

                if (ligne.trim().isEmpty()) continue;

                String[] parts = ligne.split(";");
                if (parts.length < 4) continue;

                String from = parts[0].trim().replace("$", "");
                String to   = parts[1].trim().replace("$", "");
                int distance = (int) Double.parseDouble(parts[2].trim());

                g.nodes.add(new Graphe.Node(from, from));
                g.nodes.add(new Graphe.Node(to, to));

                // ajoute edge en DOUBLE SENS pour l'instant
                g.edges.add(new Graphe.Edge(from, to, distance, "TWO_WAY"));

                // stocker une signature
                edgesSet.add(from + "->" + to);
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // -------- DÉTECTION DES SENS UNIQUES --------
        for (Graphe.Edge e : g.edges) {

            String inverse = e.to + "->" + e.from;

            if (!edgesSet.contains(inverse)) {
                // sens unique
                e.sens = "ONE_WAY";
            }
        }

        return g;
    }
}
