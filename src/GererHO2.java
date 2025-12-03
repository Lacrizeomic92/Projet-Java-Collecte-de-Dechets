import java.util.Scanner;

public class GererHO2 {
    private Scanner scanner;

    public GererHO2(Scanner scanner) {
        this.scanner = scanner;
    }

    public void afficherMenu() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== THÈME 1 - HO2 (SENS UNIQUE) ===");
            System.out.println("1. Itinéraire entre 2 intersections");
            System.out.println("2. Tournée multipoints");
            System.out.println("3. Retour au choix des hypothèses");
            System.out.print("Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {
                case 1:
                    System.out.println("\n=== ITINÉRAIRE ENTRE 2 INTERSECTIONS (HO2 - SENS UNIQUE) ===");
                    demanderItineraireDeuxPoints();
                    break;
                case 2:
                    System.out.println("\n=== TOURNÉE MULTIPOINTS (HO2 - SENS UNIQUE) ===");
                    demanderTourneeMultiPoints();
                    break;
                case 3:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void demanderItineraireDeuxPoints() {
        System.out.print("Intersection de départ : ");
        String depart = scanner.nextLine();

        System.out.print("Intersection d'arrivée : ");
        String arrivee = scanner.nextLine();

        System.out.println("\n[HO2 - Sens Unique] Calcul de l'itinéraire entre " + depart + " et " + arrivee + "...");

        // Ici, vous intégrerez votre logique métier pour HO2 (orienté)
        // Exemple d'appel à une classe de service :
        // ItineraireService.calculerItineraireHO2(depart, arrivee);

        System.out.println("\nRésultat (sens unique) :");
        System.out.println("1. Rue de la Liberté (sens unique)");
        System.out.println("2. Avenue Félix Faure (sens unique)");
        System.out.println("3. Boulevard Carabacel (sens unique)");
        System.out.println("\nDistance totale : 920 m");

        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    private void demanderTourneeMultiPoints() {
        System.out.print("Liste des intersections à visiter (séparées par des virgules) : ");
        String liste = scanner.nextLine();

        String[] points = liste.split(",");
        System.out.println("\n[HO2 - Sens Unique] Calcul de la tournée pour " + points.length + " points...");

        // Ici, vous intégrerez votre logique métier pour HO2 (orienté)
        // Exemple d'appel à une classe de service :
        // TourneeService.calculerTourneeHO2(points);

        System.out.println("\nRésultat (sens unique) :");
        System.out.println("Ordre optimal (respect des sens uniques) :");
        for (int i = 0; i < points.length; i++) {
            System.out.println((i+1) + ". " + points[i].trim());
        }
        System.out.println("\nDistance totale : 1450 m");

        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}
