import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class GrapheLoaderCirculation {

    public static Graphe charger(String fichier) {

        Graphe g = new Graphe();

        // Pour éviter d'ajouter plusieurs fois le même node
        HashSet<String> noeudsDejaAjoutes = new HashSet<>();

        // Pour détecter les sens uniques
        HashSet<String> edgesSet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {

            String ligne;

            while ((ligne = br.readLine()) != null) {

                if (ligne.trim().isEmpty()) continue;

                String[] parts = ligne.split(";");

                if (parts.length < 4) continue;

                String from = parts[0].trim().replace("$", "");
                String to   = parts[1].trim().replace("$", "");
                int distance = (int) Double.parseDouble(parts[2].trim().replace(",", "."));
                String nomRue = parts[3].trim();

                // --------------------------------------------
                // 🔥 Ajouter les nodes UNE SEULE FOIS
                // --------------------------------------------
                if (!noeudsDejaAjoutes.contains(from)) {
                    g.nodes.add(new Graphe.Node(from, from));
                    noeudsDejaAjoutes.add(from);
                }
                if (!noeudsDejaAjoutes.contains(to)) {
                    g.nodes.add(new Graphe.Node(to, to));
                    noeudsDejaAjoutes.add(to);
                }

                // --------------------------------------------
                // 🔥 Ajouter l'arête (TWO_WAY par défaut)
                // --------------------------------------------
                g.edges.add(new Graphe.Edge(from, to, distance, "TWO_WAY", nomRue));

                // --------------------------------------------
                // Stocker la signature pour détecter ONE_WAY
                // --------------------------------------------
                edgesSet.add(from + "->" + to);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // --------------------------------------------
        // 🔥 Détection des sens uniques
        // --------------------------------------------
        for (Graphe.Edge e : g.edges) {

            String inverse = e.to + "->" + e.from;

            if (!edgesSet.contains(inverse)) {
                // Sens unique automatiquement détecté
                e.sens = "ONE_WAY";
            }
        }

        return g;
    }
}
