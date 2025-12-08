import javax.swing.*;
import java.util.*;

public class GestionnaireCollecteAvecCapacites {

    private static final int MIN_Q = 5, MAX_Q = 18;

    public void demarrer() {
        try {
            SecteurGraphe original = ChargeurGraphe.charger("nice_secteurs.txt");
            HashMap<Integer, ArrayList<NoeudSecteur>> clusters =
                    Clusteriseur.clusteriser(original, Configuration.NOMBRE_SECTEURS);

            SecteurGraphe grapheSecteurs = ConstructeurGraphe.construire(original, clusters);

            attribuerQuantitesAleatoiresFixes(grapheSecteurs);

            HashMap<Integer, ArrayList<NoeudSecteur>> planning =
                    planifier(grapheSecteurs);

            afficherPlanning(planning);

            lancerVisualisation(grapheSecteurs, original, clusters, planning);

        } catch (Exception e) { e.printStackTrace(); }
    }


    private void attribuerQuantitesAleatoiresFixes(SecteurGraphe graphe) {
        Random r = new Random();
        for (NoeudSecteur s : graphe.noeuds)
            s.quantiteDechets = MIN_Q + r.nextInt(MAX_Q - MIN_Q + 1);
        System.out.println("Quantités aléatoires attribuées (fixes pour cette exécution).");
    }


    private HashMap<Integer, ArrayList<NoeudSecteur>> planifier(SecteurGraphe graphe) {

        HashMap<Integer, ArrayList<NoeudSecteur>> groupes = colorer(graphe);
        HashMap<Integer, ArrayList<NoeudSecteur>> planning = new HashMap<>();

        int jour = 0;
        int CAPA = Configuration.CAPACITE_MAX_PAR_JOUR;

        for (ArrayList<NoeudSecteur> groupe : groupes.values()) {

            int charge = total(groupe);

            if (charge <= CAPA) {
                planning.put(jour++, groupe);
            } else {
                ArrayList<NoeudSecteur> current = new ArrayList<>();
                int c = 0;

                for (NoeudSecteur s : groupe) {
                    if (c + s.quantiteDechets > CAPA) {
                        planning.put(jour++, new ArrayList<>(current));
                        current.clear();
                        c = 0;
                    }
                    current.add(s);
                    c += s.quantiteDechets;
                }

                if (!current.isEmpty())
                    planning.put(jour++, current);
            }
        }

        return planning;
    }


    private HashMap<Integer, ArrayList<NoeudSecteur>> colorer(SecteurGraphe g) {

        ArrayList<NoeudSecteur> n = new ArrayList<>(g.noeuds);
        n.sort((a,b)-> Integer.compare(g.getDegre(b), g.getDegre(a)));

        HashMap<Integer, ArrayList<NoeudSecteur>> groupes = new HashMap<>();

        for (NoeudSecteur s : n) {
            HashSet<Integer> colVoisins = new HashSet<>();

            for (AreteSecteur a : g.aretes) {
                if (a.a == s && a.b.couleur != -1) colVoisins.add(a.b.couleur);
                if (a.b == s && a.a.couleur != -1) colVoisins.add(a.a.couleur);
            }

            int c = 0;
            while (colVoisins.contains(c)) c++;

            s.couleur = c;
            groupes.computeIfAbsent(c, k -> new ArrayList<>()).add(s);
        }

        return groupes;
    }

    private int total(ArrayList<NoeudSecteur> l) {
        return l.stream().mapToInt(s -> s.quantiteDechets).sum();
    }

    private void afficherPlanning(HashMap<Integer, ArrayList<NoeudSecteur>> p) {
        System.out.println("\n=== PLANNING FINAL ===");
        ArrayList<Integer> j = new ArrayList<>(p.keySet());
        Collections.sort(j);
        for (int d : j)
            System.out.println("Jour " + (d+1) + " : " + total(p.get(d)) + " tonnes → " + p.get(d));
    }

    private void lancerVisualisation(SecteurGraphe g, SecteurGraphe o,
                                     HashMap<Integer, ArrayList<NoeudSecteur>> clusters,
                                     HashMap<Integer, ArrayList<NoeudSecteur>> planning) {

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Planification Simplifiée");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            positionnerEnCercle(g, 600, 400, 250);

            f.add(new PanneauVisualisationAvecCapacites(g, o, clusters, planning));
            f.setSize(1300, 850);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    private void positionnerEnCercle(SecteurGraphe g, int x, int y, int r) {
        int n = g.noeuds.size();
        for (int i = 0; i < n; i++) {
            double a = 2 * Math.PI * i / n;
            g.noeuds.get(i).x = x + (int)(r * Math.cos(a));
            g.noeuds.get(i).y = y + (int)(r * Math.sin(a));
        }
    }
}
