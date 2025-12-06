import java.util.*;

public class CheminEulerien {

    // ----------------------------------------------------
    // Trouve le chemin eulérien dans un graphe non orienté
    // (exactement 2 sommets impairs)
    // ----------------------------------------------------
    public static List<String> trouverCheminEulerien(Map<String, List<String>> adj) {

        // 1️⃣ Identifier les sommets impairs
        List<String> impairs = new ArrayList<>();
        for (String s : adj.keySet()) {
            if (adj.get(s).size() % 2 != 0) {
                impairs.add(s);
            }
        }

        if (impairs.size() != 2) {
            throw new RuntimeException("Le graphe doit avoir exactement 2 sommets impairs !");
        }

        String start = impairs.get(0);   // Départ du chemin eulérien

        // Copie du graphe car on va supprimer des arêtes
        Map<String, List<String>> g = new HashMap<>();
        for (String s : adj.keySet()) {
            g.put(s, new ArrayList<>(adj.get(s)));
        }

        List<String> chemin = new ArrayList<>();
        fleury(start, g, chemin);

        return chemin;
    }

    // ----------------------------------------------------
    // Algorithme de Fleury (version chemin eulérien)
    // ----------------------------------------------------
    private static void fleury(String u, Map<String, List<String>> g, List<String> chemin) {

        for (Iterator<String> it = g.get(u).iterator(); it.hasNext(); ) {
            String v = it.next();

            if (!pont(g, u, v)) {
                it.remove();
                g.get(v).remove(u);
                fleury(v, g, chemin);
                break;
            }
        }

        chemin.add(0, u);
    }

    // ----------------------------------------------------
    // Vérifie si une arête (u,v) est un pont
    // ----------------------------------------------------
    private static boolean pont(Map<String, List<String>> g, String u, String v) {

        // Compter composants avant suppression
        int avant = compterComposants(g);

        // Supprimer arête
        g.get(u).remove(v);
        g.get(v).remove(u);

        int apres = compterComposants(g);

        // Restaurer arête
        g.get(u).add(v);
        g.get(v).add(u);

        return apres > avant;
    }

    private static int compterComposants(Map<String, List<String>> g) {
        Set<String> visite = new HashSet<>();
        int count = 0;

        for (String s : g.keySet()) {
            if (!visite.contains(s)) {
                dfs(s, g, visite);
                count++;
            }
        }
        return count;
    }

    private static void dfs(String s, Map<String, List<String>> g, Set<String> vis) {
        vis.add(s);
        for (String v : g.get(s)) {
            if (!vis.contains(v)) {
                dfs(v, g, vis);
            }
        }
    }
}
