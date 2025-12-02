import java.io.*;
import java.util.*;

public class ChargeurGraphe {
    public static SecteurGraphe charger(String fichier) throws Exception {
        SecteurGraphe g = new SecteurGraphe();
        HashMap<String, NoeudSecteur> map = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne;
        Random rand = new Random(42);

        while ((ligne = br.readLine()) != null) {
            ligne = ligne.trim();
            if (ligne.isEmpty() || ligne.startsWith("#")) continue;
            String[] parts = ligne.split(";");
            if (parts.length < 2) continue;

            String idA = parts[0].trim(), idB = parts[1].trim();

            if (!map.containsKey(idA)) {
                NoeudSecteur n = new NoeudSecteur(idA.hashCode(), rand.nextInt(1000), rand.nextInt(600), idA);
                map.put(idA, n); g.ajouterNoeud(n);
            }
            if (!map.containsKey(idB)) {
                NoeudSecteur n = new NoeudSecteur(idB.hashCode(), rand.nextInt(1000), rand.nextInt(600), idB);
                map.put(idB, n); g.ajouterNoeud(n);
            }

            g.ajouterArete(map.get(idA), map.get(idB));
        }
        br.close();
        System.out.println("Chargé: " + g.noeuds.size() + " nœuds, " + g.aretes.size() + " arêtes");
        return g;
    }
}
