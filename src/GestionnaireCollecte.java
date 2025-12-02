import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GestionnaireCollecte {
    private static final int NOMBRE_SECTEURS = 6;

    public void demarrer() throws Exception {
        System.out.println("=== PROJET COLLECTE DÉCHETS - THÈME 3 H01 ===");

        System.out.println("\n1. Chargement du graphe...");
        SecteurGraphe original = ChargeurGraphe.charger("nice_secteurs.txt");

        System.out.println("\n2. Création de " + NOMBRE_SECTEURS + " secteurs...");
        HashMap<Integer, ArrayList<NoeudSecteur>> clusters = Clusteriseur.clusteriser(original, NOMBRE_SECTEURS);

        System.out.println("\n3. Construction du graphe des secteurs...");
        SecteurGraphe grapheSecteurs = ConstructeurGraphe.construire(original, clusters);

        System.out.println("\n4. Coloration des secteurs...");
        Colorateur.colorer(grapheSecteurs);

        System.out.println("\n5. Résultats de la planification:");
        afficherResultats(grapheSecteurs);

        System.out.println("\n6. Lancement de la visualisation...");
        lancerVisualisation(grapheSecteurs, original, clusters);
    }

    private void afficherResultats(SecteurGraphe graphe) {
        System.out.println("• " + graphe.noeuds.size() + " secteurs");
        System.out.println("• " + graphe.aretes.size() + " connexions");

        TreeMap<Integer, ArrayList<String>> planning = new TreeMap<>();
        for (NoeudSecteur s : graphe.noeuds) {
            planning.computeIfAbsent(s.couleur, k -> new ArrayList<>())
                    .add(s.nom);
        }

        System.out.println("\n--- CALENDRIER DE COLLECTE ---");
        for (Map.Entry<Integer, ArrayList<String>> entry : planning.entrySet()) {
            System.out.println("Jour " + (entry.getKey() + 1) + ": " +
                    String.join(", ", entry.getValue()));
        }
    }

    private void lancerVisualisation(SecteurGraphe grapheSecteurs,
                                     SecteurGraphe original,
                                     HashMap<Integer, ArrayList<NoeudSecteur>> clusters) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Planification Collecte - 6 Secteurs (H01)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Position circulaire
            positionnerEnCercle(grapheSecteurs, 600, 400, 250);

            PanneauVisualisation panneau = new PanneauVisualisation(grapheSecteurs, original, clusters);
            frame.add(panneau);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void positionnerEnCercle(SecteurGraphe graphe, int centreX, int centreY, int rayon) {
        int n = graphe.noeuds.size();
        for (int i = 0; i < n; i++) {
            NoeudSecteur s = graphe.noeuds.get(i);
            double angle = 2 * Math.PI * i / n;
            s.x = centreX + (int)(rayon * Math.cos(angle));
            s.y = centreY + (int)(rayon * Math.sin(angle));
        }
    }
}
