import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Collections;

public class GestionnaireCollecteAvecCapacites {

    public void demarrer() {
        try {
            Configuration.afficherConfiguration();

            System.out.println("\n=== PLANIFICATION AVEC CONTRAINTES DE CAPACITÉ ===");
            System.out.println("Hypothèses:");
            System.out.println("1. Secteurs voisins ≠ même jour (coloration)");
            System.out.println("2. 1 tournée max par secteur par jour");
            System.out.println("3. Capacité totale/jour ≤ " + Configuration.CAPACITE_MAX_PAR_JOUR + " tonnes");
            System.out.println("4. Taux remplissage ≤ " + Configuration.TAUX_REMPLISSAGE_MAX + "% (marge sécurité)");

            // Étape 1: Charger et préparer le graphe
            System.out.println("\n1. Chargement du graphe...");
            SecteurGraphe original = ChargeurGraphe.charger("nice_secteurs.txt");

            // Étape 2: Créer les secteurs
            System.out.println("\n2. Création des " + Configuration.NOMBRE_SECTEURS + " secteurs...");
            HashMap<Integer, ArrayList<NoeudSecteur>> clusters =
                    Clusteriseur.clusteriser(original, Configuration.NOMBRE_SECTEURS);

            // Étape 3: Construire le graphe des secteurs
            System.out.println("\n3. Construction du graphe des secteurs...");
            SecteurGraphe grapheSecteurs = ConstructeurGraphe.construire(original, clusters);

            // Étape 4: Attribuer des quantités de déchets RÉALISTES
            System.out.println("\n4. Attribution des quantités de déchets (réalistes):");
            attribuerQuantitesRealistes(grapheSecteurs);

            // Étape 5: Planification avec contraintes
            System.out.println("\n5. Planification optimisée avec contraintes...");
            HashMap<Integer, ArrayList<NoeudSecteur>> planning = planifierOptimise(grapheSecteurs);

            // Étape 6: Analyse des résultats
            System.out.println("\n6. Analyse des résultats:");
            analyserResultats(planning, grapheSecteurs);

            // Étape 7: Visualisation
            System.out.println("\n7. Lancement de la visualisation...");
            lancerVisualisation(grapheSecteurs, original, clusters, planning);

        } catch (Exception e) {
            System.err.println("ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void attribuerQuantitesRealistes(SecteurGraphe graphe) {
        int total = 0;

        System.out.println("Quantités basées sur:");
        System.out.println("  - " + Configuration.HABITANTS_PAR_SECTEUR + " habitants/secteur");
        System.out.println("  - " + Configuration.PRODUCTION_PAR_HABITANT + " kg/habitant/jour");
        System.out.println("  - Ajustement selon densité (degré)");

        for (NoeudSecteur secteur : graphe.noeuds) {
            int degre = graphe.getDegre(secteur);
            int quantite = Configuration.calculerQuantiteRealiste(degre);
            secteur.quantiteDechets = quantite;
            total += quantite;

            System.out.println("  " + secteur.nom +
                    ": " + quantite + " tonnes" +
                    " (degré: " + degre +
                    ", ~" + (quantite * 1000 / Configuration.PRODUCTION_PAR_HABITANT) + " habitants)");
        }

        System.out.println("\n  TOTAL: " + total + " tonnes");
        System.out.println("  Capacité disponible/jour: " + Configuration.CAPACITE_MAX_PAR_JOUR + " tonnes");

        // Calcul du nombre de jours minimum théorique
        int joursMinTheorique = (int) Math.ceil((double) total / Configuration.CAPACITE_MAX_PAR_JOUR);
        System.out.println("  Jours minimum théoriques: " + joursMinTheorique + " jours");
    }

    private HashMap<Integer, ArrayList<NoeudSecteur>> planifierOptimise(SecteurGraphe graphe) {
        System.out.println("\n--- ALGORITHME D'OPTIMISATION ---");

        // 1. Coloration pour séparer les voisins
        System.out.println("Phase 1: Séparation des secteurs voisins...");
        HashMap<Integer, ArrayList<NoeudSecteur>> groupesCouleur = colorerGraphe(graphe);
        System.out.println("  → " + groupesCouleur.size() + " groupes initiaux");

        // 2. Optimisation des capacités
        System.out.println("\nPhase 2: Optimisation des capacités...");
        HashMap<Integer, ArrayList<NoeudSecteur>> planningFinal = optimiserCapacites(groupesCouleur, graphe);

        // 3. Équilibrage des charges
        System.out.println("\nPhase 3: Équilibrage des charges...");
        planningFinal = equilibrerCharges(planningFinal, graphe);

        return planningFinal;
    }

    private HashMap<Integer, ArrayList<NoeudSecteur>> colorerGraphe(SecteurGraphe graphe) {
        // Algorithme de coloration glouton
        ArrayList<NoeudSecteur> noeuds = new ArrayList<>(graphe.noeuds);

        // Trier par degré décroissant
        noeuds.sort((a, b) -> Integer.compare(graphe.getDegre(b), graphe.getDegre(a)));

        HashMap<Integer, ArrayList<NoeudSecteur>> groupes = new HashMap<>();

        for (NoeudSecteur n : noeuds) {
            // Trouver la plus petite couleur disponible
            Set<Integer> couleursVoisines = new HashSet<>();

            for (AreteSecteur a : graphe.aretes) {
                if (a.a == n && a.b.couleur != -1) couleursVoisines.add(a.b.couleur);
                if (a.b == n && a.a.couleur != -1) couleursVoisines.add(a.a.couleur);
            }

            int couleur = 0;
            while (couleursVoisines.contains(couleur)) couleur++;

            n.couleur = couleur;
            groupes.computeIfAbsent(couleur, k -> new ArrayList<>()).add(n);
        }

        return groupes;
    }

    private HashMap<Integer, ArrayList<NoeudSecteur>> optimiserCapacites(
            HashMap<Integer, ArrayList<NoeudSecteur>> groupes,
            SecteurGraphe graphe) {

        HashMap<Integer, ArrayList<NoeudSecteur>> resultat = new HashMap<>();
        int jour = 0;

        for (ArrayList<NoeudSecteur> groupe : groupes.values()) {
            int totalGroupe = calculerTotal(groupe);
            int capaciteAjustee = (Configuration.CAPACITE_MAX_PAR_JOUR * Configuration.TAUX_REMPLISSAGE_MAX) / 100;

            if (totalGroupe <= capaciteAjustee) {
                // Le groupe tient dans un jour (avec marge de sécurité)
                resultat.put(jour, new ArrayList<>(groupe));
                assignerCouleur(groupe, jour);
                jour++;
                System.out.println("  Groupe → Jour " + jour + " (" + totalGroupe + "/" + capaciteAjustee + " tonnes)");
            } else {
                // Nécessite une division
                System.out.println("  Groupe trop lourd (" + totalGroupe + "t), division...");
                ArrayList<ArrayList<NoeudSecteur>> sousGroupes = diviserOptimise(groupe, graphe);

                for (ArrayList<NoeudSecteur> sousGroupe : sousGroupes) {
                    int sousTotal = calculerTotal(sousGroupe);
                    resultat.put(jour, sousGroupe);
                    assignerCouleur(sousGroupe, jour);
                    System.out.println("    → Sous-groupe Jour " + (jour + 1) + ": " + sousTotal + " tonnes");
                    jour++;
                }
            }
        }

        return resultat;
    }

    private ArrayList<ArrayList<NoeudSecteur>> diviserOptimise(
            ArrayList<NoeudSecteur> secteurs,
            SecteurGraphe graphe) {

        ArrayList<ArrayList<NoeudSecteur>> sousGroupes = new ArrayList<>();
        ArrayList<NoeudSecteur> restants = new ArrayList<>(secteurs);

        // Trier par quantité décroissante (First-Fit Decreasing)
        restants.sort((a, b) -> Integer.compare(b.quantiteDechets, a.quantiteDechets));

        // Capacité avec marge de sécurité
        int capaciteAjustee = (Configuration.CAPACITE_MAX_PAR_JOUR * Configuration.TAUX_REMPLISSAGE_MAX) / 100;

        while (!restants.isEmpty()) {
            ArrayList<NoeudSecteur> sousGroupe = new ArrayList<>();
            int charge = 0;

            // Essayer de maximiser le remplissage sans dépasser la capacité
            Iterator<NoeudSecteur> it = restants.iterator();
            while (it.hasNext()) {
                NoeudSecteur s = it.next();
                if (charge + s.quantiteDechets <= capaciteAjustee) {
                    // Vérifier qu'ajouter ce secteur ne crée pas de conflit avec les voisins
                    boolean conflit = false;
                    for (NoeudSecteur existant : sousGroupe) {
                        // Vérifier si 's' est voisin d'un secteur déjà dans le sous-groupe
                        for (AreteSecteur arete : graphe.aretes) {
                            if ((arete.a == s && arete.b == existant) ||
                                    (arete.b == s && arete.a == existant)) {
                                conflit = true;
                                break;
                            }
                        }
                        if (conflit) break;
                    }

                    if (!conflit) {
                        sousGroupe.add(s);
                        charge += s.quantiteDechets;
                        it.remove();
                    }
                }
            }

            if (!sousGroupe.isEmpty()) {
                sousGroupes.add(sousGroupe);
            } else if (!restants.isEmpty()) {
                // Forcer l'ajout du plus petit secteur restant
                NoeudSecteur plusPetit = trouverPlusPetit(restants);
                sousGroupe.add(plusPetit);
                sousGroupes.add(sousGroupe);
                restants.remove(plusPetit);
            }
        }

        return sousGroupes;
    }

    private NoeudSecteur trouverPlusPetit(ArrayList<NoeudSecteur> secteurs) {
        NoeudSecteur plusPetit = secteurs.get(0);
        for (NoeudSecteur s : secteurs) {
            if (s.quantiteDechets < plusPetit.quantiteDechets) {
                plusPetit = s;
            }
        }
        return plusPetit;
    }

    private HashMap<Integer, ArrayList<NoeudSecteur>> equilibrerCharges(
            HashMap<Integer, ArrayList<NoeudSecteur>> planning,
            SecteurGraphe graphe) {

        System.out.println("\n  Équilibrage des charges entre jours...");

        // Calculer la charge moyenne par jour
        int totalDechets = 0;
        for (ArrayList<NoeudSecteur> jour : planning.values()) {
            totalDechets += calculerTotal(jour);
        }

        double chargeMoyenne = (double) totalDechets / planning.size();
        System.out.println("  Charge moyenne/jour: " + String.format("%.1f", chargeMoyenne) + " tonnes");

        // Essayer d'équilibrer en déplaçant des petits secteurs
        boolean amelioration = true;
        int iterations = 0;

        while (amelioration && iterations < 10) {
            amelioration = false;
            iterations++;

            // Trouver les jours les plus et moins chargés
            int jourPlusCharge = -1;
            int jourMoinsCharge = -1;
            int maxCharge = Integer.MIN_VALUE;
            int minCharge = Integer.MAX_VALUE;

            for (Map.Entry<Integer, ArrayList<NoeudSecteur>> entry : planning.entrySet()) {
                int charge = calculerTotal(entry.getValue());
                if (charge > maxCharge) {
                    maxCharge = charge;
                    jourPlusCharge = entry.getKey();
                }
                if (charge < minCharge) {
                    minCharge = charge;
                    jourMoinsCharge = entry.getKey();
                }
            }

            // Essayer de déplacer un secteur du jour le plus chargé vers le moins chargé
            if (jourPlusCharge != -1 && jourMoinsCharge != -1 && jourPlusCharge != jourMoinsCharge) {
                ArrayList<NoeudSecteur> secteurPlusCharge = planning.get(jourPlusCharge);
                ArrayList<NoeudSecteur> secteurMoinsCharge = planning.get(jourMoinsCharge);

                for (NoeudSecteur secteur : new ArrayList<>(secteurPlusCharge)) {
                    // Vérifier si on peut déplacer ce secteur sans créer de conflit
                    if (peutDeplacerSecteur(secteur, secteurMoinsCharge, graphe)) {
                        int chargeSecteur = secteur.quantiteDechets;

                        // Déplacer si ça améliore l'équilibre
                        if (maxCharge - chargeSecteur > minCharge + chargeSecteur) {
                            secteurPlusCharge.remove(secteur);
                            secteurMoinsCharge.add(secteur);
                            secteur.couleur = jourMoinsCharge;

                            System.out.println("    Déplacé " + secteur.nom + " (" + chargeSecteur + "t)");
                            System.out.println("      Jour " + (jourPlusCharge + 1) + ": " + (maxCharge - chargeSecteur) + "t");
                            System.out.println("      Jour " + (jourMoinsCharge + 1) + ": " + (minCharge + chargeSecteur) + "t");

                            amelioration = true;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("  Équilibrage terminé après " + iterations + " itérations");
        return planning;
    }

    private boolean peutDeplacerSecteur(NoeudSecteur secteur,
                                        ArrayList<NoeudSecteur> destination,
                                        SecteurGraphe graphe) {

        // Vérifier qu'aucun voisin n'est déjà dans la destination
        for (NoeudSecteur existant : destination) {
            for (AreteSecteur arete : graphe.aretes) {
                if ((arete.a == secteur && arete.b == existant) ||
                        (arete.b == secteur && arete.a == existant)) {
                    return false; // Conflit de voisinage
                }
            }
        }

        // Vérifier la capacité
        int totalDestination = calculerTotal(destination);
        return totalDestination + secteur.quantiteDechets <=
                (Configuration.CAPACITE_MAX_PAR_JOUR * Configuration.TAUX_REMPLISSAGE_MAX) / 100;
    }

    private void assignerCouleur(ArrayList<NoeudSecteur> secteurs, int couleur) {
        for (NoeudSecteur s : secteurs) {
            s.couleur = couleur;
        }
    }

    private int calculerTotal(ArrayList<NoeudSecteur> secteurs) {
        return secteurs.stream().mapToInt(s -> s.quantiteDechets).sum();
    }

    private void analyserResultats(HashMap<Integer, ArrayList<NoeudSecteur>> planning,
                                   SecteurGraphe graphe) {
        System.out.println("\n=== ANALYSE DES RÉSULTATS ===");
        System.out.println("Nombre de jours nécessaires: " + planning.size());

        int totalDechets = 0;
        int capaciteTotaleDisponible = 0;
        int jourPlusCharge = 0;
        int jourMoinsCharge = 0;
        int maxCharge = Integer.MIN_VALUE;
        int minCharge = Integer.MAX_VALUE;

        // Trier les jours par numéro
        ArrayList<Integer> jours = new ArrayList<>(planning.keySet());
        Collections.sort(jours);

        for (int jour : jours) {
            ArrayList<NoeudSecteur> secteurs = planning.get(jour);
            int totalJour = calculerTotal(secteurs);
            totalDechets += totalJour;
            capaciteTotaleDisponible += Configuration.CAPACITE_MAX_PAR_JOUR;

            // Statistiques par jour
            double tauxRemplissage = (totalJour * 100.0) / Configuration.CAPACITE_MAX_PAR_JOUR;
            double tauxRemplissageAjuste = (totalJour * 100.0) /
                    ((Configuration.CAPACITE_MAX_PAR_JOUR * Configuration.TAUX_REMPLISSAGE_MAX) / 100);

            System.out.println("\nJour " + (jour + 1) + ":");
            System.out.println("  Secteurs: " + secteurs.size());
            System.out.println("  Total déchets: " + totalJour + " tonnes");
            System.out.println("  Taux remplissage: " + String.format("%.1f", tauxRemplissage) + "%");
            System.out.println("  Taux remplissage (ajusté): " + String.format("%.1f", tauxRemplissageAjuste) + "%");
            System.out.println("  Secteurs: " + secteurs);

            // Vérifier contraintes
            if (!verifierPasDeVoisinsMemeJour(secteurs, graphe)) {
                System.err.println("  ⚠ ATTENTION: Des secteurs voisins sont le même jour!");
            }

            if (tauxRemplissageAjuste > 100) {
                System.err.println("  ⚠ ATTENTION: Dépassement de capacité ajustée!");
            }

            // Pour max/min
            if (totalJour > maxCharge) {
                maxCharge = totalJour;
                jourPlusCharge = jour;
            }
            if (totalJour < minCharge) {
                minCharge = totalJour;
                jourMoinsCharge = jour;
            }
        }

        // Synthèse
        System.out.println("\n=== SYNTHÈSE GLOBALE ===");
        System.out.println("Total déchets à collecter: " + totalDechets + " tonnes");
        System.out.println("Capacité totale disponible: " + capaciteTotaleDisponible + " tonnes");

        double tauxUtilisationGlobal = (totalDechets * 100.0) / capaciteTotaleDisponible;
        System.out.println("Taux d'utilisation global: " + String.format("%.1f", tauxUtilisationGlobal) + "%");

        System.out.println("Nombre moyen de secteurs par jour: " +
                String.format("%.1f", (double)graphe.noeuds.size() / planning.size()));

        System.out.println("Jour le plus chargé: Jour " + (jourPlusCharge + 1) +
                " (" + maxCharge + " tonnes)");
        System.out.println("Jour le moins chargé: Jour " + (jourMoinsCharge + 1) +
                " (" + minCharge + " tonnes)");

        double ecartType = calculerEcartType(planning);
        System.out.println("Écart-type des charges: " + String.format("%.2f", ecartType) +
                " tonnes (plus c'est bas, mieux c'est)");
    }

    private double calculerEcartType(HashMap<Integer, ArrayList<NoeudSecteur>> planning) {
        if (planning.isEmpty()) return 0;

        // Calculer la moyenne
        double somme = 0;
        int n = planning.size();

        for (ArrayList<NoeudSecteur> jour : planning.values()) {
            somme += calculerTotal(jour);
        }
        double moyenne = somme / n;

        // Calculer la variance
        double variance = 0;
        for (ArrayList<NoeudSecteur> jour : planning.values()) {
            double ecart = calculerTotal(jour) - moyenne;
            variance += ecart * ecart;
        }
        variance /= n;

        return Math.sqrt(variance);
    }

    private boolean verifierPasDeVoisinsMemeJour(ArrayList<NoeudSecteur> secteurs, SecteurGraphe graphe) {
        Set<NoeudSecteur> setSecteurs = new HashSet<>(secteurs);

        for (NoeudSecteur secteur : secteurs) {
            for (AreteSecteur arete : graphe.aretes) {
                if (arete.a == secteur && setSecteurs.contains(arete.b)) {
                    return false;
                }
                if (arete.b == secteur && setSecteurs.contains(arete.a)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void lancerVisualisation(SecteurGraphe grapheSecteurs,
                                     SecteurGraphe original,
                                     HashMap<Integer, ArrayList<NoeudSecteur>> clusters,
                                     HashMap<Integer, ArrayList<NoeudSecteur>> planning) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Planification avec Capacités - H01 (Version réaliste)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Positionner en cercle
            positionnerEnCercle(grapheSecteurs, 600, 400, 250);

            PanneauVisualisationAvecCapacites panneau =
                    new PanneauVisualisationAvecCapacites(grapheSecteurs, original, clusters, planning);

            frame.add(panneau);
            frame.setSize(1300, 850);
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
