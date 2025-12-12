import java.util.Scanner;

public class ConsoleNavigateur {

    private final Scanner scanner;

    public ConsoleNavigateur() {
        scanner = new Scanner(System.in);
    }
    public void demarrer() {
        afficherEnTete();

        while (true) {
            System.out.println("\nMENU PRINCIPAL");
            System.out.println("1. Collectivité");
            System.out.println("2. Entreprise de collecte");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 1:
                    menuCollectivite();
                    break;

                case 2:
                    menuEntreprise();
                    break;

                case 0:
                    System.out.println("\nAu revoir !");
                    return;

                default:
                    System.out.println(" Choix invalide.");
            }
        }
    }


    private void afficherEnTete() {
        System.out.println("        NAVIGATEUR DE LA COLLECTE DES DÉCHETS ");
        System.out.println("                 Ville de Nice                ");
        System.out.println("\nBienvenue dans la version console.");
    }
    private void menuCollectivite() {
        System.out.println("\nMENU COLLECTIVITÉ");
        System.out.println("  - Fermeture de rue");
        System.out.println("  - Modification de distances");

        System.out.println("Retour automatique au menu principal.");
    }


    private void menuEntreprise() {

        while (true) {
            System.out.println("\n ENTREPRISE DE COLLECTE ");
            System.out.println("1. Choisir hypothèse (HO1 / HO2 / HO3)");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 0:
                    return;

                case 1:
                    menuChoixHypotheses();
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void menuChoixHypotheses() {

        while (true) {
            System.out.println("\nCHOIX DES HYPOTHÈSES ");
            System.out.println("1. HO1 – Double sens");
            System.out.println("2. HO2 – Sens unique");
            System.out.println("3. HO3 – Mixte");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 0:
                    return;

                case 1:
                    menuHO1();
                    break;

                case 2:
                    menuHO2();
                    break;

                case 3:
                    menuHO3();
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void menuHO1() {

        while (true) {
            System.out.println("\n HO1 – DOUBLE SENS ");
            System.out.println("1. Thème 1 : Collecte complète");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 0:
                    return;

                case 1:
                    menuTheme1();
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void menuTheme1() {

        while (true) {
            System.out.println("\nTHÈME 1 : Collecte complète ");
            System.out.println("Cas 1 : Tous les sommets pairs (circuit eulérien)");
            System.out.println("Cas 2 : Deux sommets impairs (chemin eulérien)");
            System.out.println("Cas 3 : Cas général (graphes chinois)");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");

            int choix = lireEntier();

            switch (choix) {
                case 0:
                    return;

                case 1:
                    lancerCas1();
                    break;

                case 2:
                    System.out.println("Cas 2 pas encore implémenté.");
                    break;

                case 3:
                    System.out.println("Cas 3 pas encore implémenté.");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    private void lancerCas1() {

        System.out.println("\nGRAPH PAIR");

        Graphe g = GrapheLoaderCirculation.charger("nice_arcs_pairs.txt");

        if (g == null || g.edges.isEmpty()) {
            System.out.println("Impossible de charger le graphe !");
            return;
        }

        System.out.println(" Graphe chargé (" + g.nodes.size() + " sommets, "
                + g.edges.size() + " arêtes)");

        // ---- 🔥 NOUVEAU : ouvrir le graphe visuel ----
        System.out.println("Ouverture de la fenêtre graphique...");
        new AfficherGrapheVisuel(g);

        System.out.println(" Fenêtre graphique ouverte !");
    }

    private void menuHO2() {
        System.out.println("\nHO2 – Sens unique");
        System.out.println("À implémenter plus tard.");
    }

    private void menuHO3() {
        System.out.println("\n HO3 – Mixte ");
        System.out.println("À implémenter plus tard.");
    }

    private int lireEntier() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Entrez un nombre valide : ");
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        return n;
    }
}
