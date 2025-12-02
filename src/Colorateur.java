import java.util.*;

public class Colorateur {
    public static void colorer(SecteurGraphe g) {
        ArrayList<NoeudSecteur> noeuds = new ArrayList<>(g.noeuds);
        noeuds.sort((a, b) -> degre(g, b) - degre(g, a));

        for (NoeudSecteur n : noeuds) {
            HashSet<Integer> couleursVoisines = new HashSet<>();
            for (AreteSecteur a : g.aretes) {
                if (a.a == n && a.b.couleur != -1) couleursVoisines.add(a.b.couleur);
                if (a.b == n && a.a.couleur != -1) couleursVoisines.add(a.a.couleur);
            }

            int c = 0;
            while (couleursVoisines.contains(c)) c++;
            n.couleur = c;
        }

        System.out.println("Coloration terminée (" + couleursUtilisees(g) + " couleurs)");
    }

    private static int degre(SecteurGraphe g, NoeudSecteur n) {
        int d = 0;
        for (AreteSecteur a : g.aretes) if (a.a == n || a.b == n) d++;
        return d;
    }

    private static int couleursUtilisees(SecteurGraphe g) {
        HashSet<Integer> couleurs = new HashSet<>();
        for (NoeudSecteur n : g.noeuds) couleurs.add(n.couleur);
        return couleurs.size();
    }
}
