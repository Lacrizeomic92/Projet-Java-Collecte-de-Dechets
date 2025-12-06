import java.io.*;
import java.util.*;

public class GrapheLoader {

    public static Graphe chargerDepuisFichier(String path) {
        Graphe graphe = new Graphe();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String ligne;
            boolean lectureNodes = false;
            boolean lectureEdges = false;

            while ((ligne = br.readLine()) != null) {

                ligne = ligne.trim();

                if (ligne.equals("NODES")) {
                    lectureNodes = true;
                    lectureEdges = false;
                    continue;
                }
                if (ligne.equals("EDGES")) {
                    lectureEdges = true;
                    lectureNodes = false;
                    continue;
                }

                if (ligne.isEmpty()) continue;

                if (lectureNodes) {
                    String[] parts = ligne.split(" ", 2);
                    graphe.nodes.add(new Graphe.Node(parts[0], parts[1]));
                }

                if (lectureEdges) {
                    String[] parts = ligne.split(" ");
                    graphe.edges.add(new Graphe.Edge(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            parts[3],        // sens
                            "Rue_" + parts[0] + "_" + parts[1]   // nomRue par défaut
                    ));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return graphe;
    }
}
