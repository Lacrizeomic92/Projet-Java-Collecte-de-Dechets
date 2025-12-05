import java.util.*;

public class Graphe {

    // ---------------------
    // STRUCTURE DU NOEUD
    // ---------------------
    public static class Node {
        public String id;
        public String name;

        public Node(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // ---------------------
    // STRUCTURE DE L'ARÊTE
    // ---------------------
    public static class Edge {
        public String from, to;
        public int distance;
        public String sens;     // "TWO_WAY" ou "ONE_WAY"
        public boolean ouverte; // 🔥 true = rue ouverte, false = fermée

        public Edge(String from, String to, int distance, String sens) {
            this.from = from;
            this.to = to;
            this.distance = distance;
            this.sens = sens;
            this.ouverte = true; // Par défaut, une rue est ouverte
        }
    }

    // ---------------------
    // LISTES DU GRAPHE
    // ---------------------
    public List<Node> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();


    // --------------------------------------------------------
    // 🔥 MÉTHODES UTILES POUR LA PARTIE MODIFICATIONS CIRCULATION
    // --------------------------------------------------------

    // Fermer une rue
    public boolean fermerRue(String from, String to) {
        for (Edge e : edges) {
            if (e.from.equals(from) && e.to.equals(to)) {
                e.ouverte = false;
                return true;
            }
        }
        return false;
    }

    // Réouvrir une rue
    public boolean ouvrirRue(String from, String to) {
        for (Edge e : edges) {
            if (e.from.equals(from) && e.to.equals(to)) {
                e.ouverte = true;
                return true;
            }
        }
        return false;
    }

    // Modifier le sens
    public boolean modifierSens(String from, String to) {
        for (Edge e : edges) {
            if (e.from.equals(from) && e.to.equals(to)) {
                if (e.sens.equals("TWO_WAY")) e.sens = "ONE_WAY";
                else e.sens = "TWO_WAY";
                return true;
            }
        }
        return false;
    }

    // Modifier distance
    public boolean changerDistance(String from, String to, int newDist) {
        for (Edge e : edges) {
            if (e.from.equals(from) && e.to.equals(to)) {
                e.distance = newDist;
                return true;
            }
        }
        return false;
    }
}
