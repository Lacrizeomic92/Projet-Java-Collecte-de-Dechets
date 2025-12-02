import java.util.*;

public class ConstructeurGraphe {
    public static SecteurGraphe construire(SecteurGraphe original,
                                           HashMap<Integer, ArrayList<NoeudSecteur>> clusters) {
        SecteurGraphe g = new SecteurGraphe();
        HashMap<Integer, NoeudSecteur> secteurs = new HashMap<>();

        for (int id : clusters.keySet()) {
            NoeudSecteur s = new NoeudSecteur(id, 0, 0, "S" + id);
            g.ajouterNoeud(s); secteurs.put(id, s);
        }
        HashMap<String, Integer> liens = new HashMap<>();
        for (AreteSecteur a : original.aretes) {
            int c1 = trouverCluster(a.a, clusters), c2 = trouverCluster(a.b, clusters);
            if (c1 != -1 && c2 != -1 && c1 != c2) {
                String cle = Math.min(c1, c2) + "-" + Math.max(c1, c2);
                liens.put(cle, liens.getOrDefault(cle, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> e : liens.entrySet()) {
            String[] parts = e.getKey().split("-");
            int c1 = Integer.parseInt(parts[0]), c2 = Integer.parseInt(parts[1]);
            if (!existeArete(g, secteurs.get(c1), secteurs.get(c2))) {
                g.ajouterArete(secteurs.get(c1), secteurs.get(c2));
            }
        }

        System.out.println("Graphe secteurs: " + g.noeuds.size() + " secteurs, " + g.aretes.size() + " arêtes");
        return g;
    }

    private static int trouverCluster(NoeudSecteur n, HashMap<Integer, ArrayList<NoeudSecteur>> clusters) {
        for (Map.Entry<Integer, ArrayList<NoeudSecteur>> e : clusters.entrySet()) {
            if (e.getValue().contains(n)) return e.getKey();
        }
        return -1;
    }

    private static boolean existeArete(SecteurGraphe g, NoeudSecteur a, NoeudSecteur b) {
        for (AreteSecteur ar : g.aretes) {
            if ((ar.a == a && ar.b == b) || (ar.a == b && ar.b == a)) return true;
        }
        return false;
    }
}
