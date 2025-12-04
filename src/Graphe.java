import java.util.*;

public class Graphe {

    public static class Node {
        public String id;
        public String name;
        public Node(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Edge {
        public String from, to;
        public int distance;
        public String sens;

        public Edge(String from, String to, int distance, String sens) {
            this.from = from;
            this.to = to;
            this.distance = distance;
            this.sens = sens;
        }
    }

    public List<Node> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();
}
