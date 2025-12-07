import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class EulerienCas1 {

    // -------------------------------
    // Charger graphe depuis fichier
    // -------------------------------
    public static Map<String, List<String>> chargerGraphe(String fichier) {

        Map<String, List<String>> adj = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {

                if (ligne.trim().isEmpty()) continue;

                String[] p = ligne.split(";");
                if (p.length < 2) continue;

                String a = p[0].trim();
                String b = p[1].trim();

                adj.putIfAbsent(a, new ArrayList<>());
                adj.putIfAbsent(b, new ArrayList<>());

                // Graphe NON ORIENTÉ → on ajoute dans les 2 sens
                adj.get(a).add(b);
                adj.get(b).add(a);
            }

        } catch (Exception e) {
            System.out.println("Erreur lecture fichier : " + e.getMessage());
        }

        return adj;
    }

    // ------------------------------------------------
    // ALGORITHME DE HIERHOLZER (cycle eulérien)
    // ------------------------------------------------
    public static List<String> trouverCycleEulerien(Map<String, List<String>> adj) {

        // On copie pour pouvoir retirer les arêtes
        Map<String, List<String>> g = new HashMap<>();
        for (String k : adj.keySet()) {
            g.put(k, new ArrayList<>(adj.get(k)));
        }

        Stack<String> chemin = new Stack<>();
        List<String> cycle = new ArrayList<>();

        // On part du premier sommet (peu importe lequel)
        String start = g.keySet().iterator().next();
        chemin.push(start);

        while (!chemin.isEmpty()) {
            String u = chemin.peek();

            if (!g.get(u).isEmpty()) {
                // Prendre un voisin et retirer l’arête dans les deux sens
                String v = g.get(u).remove(0);
                g.get(v).remove(u);
                chemin.push(v);
            } else {
                // Plus d’arêtes → ajouter au cycle final
                cycle.add(chemin.pop());
            }
        }

        Collections.reverse(cycle); // ordre logique
        return cycle;
    }

    // ------------------------------------------------
    // Méthode appelée par la console
    // ------------------------------------------------
    public static void executerCas1() {

        System.out.println("\n=== CAS 1 : Tous les sommets pairs (Cycle eulérien) ===");

        String fichier = "nice_arcs_pairs.txt";
        Map<String, List<String>> graphe = chargerGraphe(fichier);

        System.out.println("Graphe chargé (" + graphe.size() + " sommets).");

        List<String> cycle = trouverCycleEulerien(graphe);

        System.out.println("\n--- CYCLE EULÉRIEN ---");
        for (int i = 0; i < cycle.size(); i++) {
            String s = cycle.get(i);
            if (i < cycle.size() - 1)
                System.out.print(s + " → ");
            else
                System.out.print(s);
        }
        System.out.println("\n");
    }
}
