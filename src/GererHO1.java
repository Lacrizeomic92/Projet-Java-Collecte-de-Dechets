import java.util.Scanner;

public class GererHO1 {
    private Scanner scanner;

    public GererHO1(Scanner scanner) {
        this.scanner = scanner;
    }

    public void afficherMenu() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== THÈME 1 - HO1 (DOUBLE SENS) ===");
            System.out.println("1. Itinéraire entre 2 intersections");
            System.out.println("2. Tournée multipoints");
            System.out.println("3. Retour au choix des hypothèses");
            System.out.print("Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {
                case 1:
                    System.out.println("\n=== ITINÉRAIRE ENTRE 2 INTERSECTIONS (HO1) ===");
                    demanderItineraireDeuxPoints();
                    break;
                case 2:
                    System.out.println("\n=== TOURNÉE MULTIPOINTS (HO1) ===");
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

        System.out.println("\n[HO1] Calcul de l'itinéraire entre " + depart + " et " + arrivee + "...");

        // Ici, vous intégrerez votre logique métier pour HO1
        // Exemple d'appel à une classe de service :
        // ItineraireService.calculerItineraireHO1(depart, arrivee);

        System.out.println("\nRésultat :");
        System.out.println("1. Rue de France");
        System.out.println("2. Avenue Jean Médecin");
        System.out.println("3. Boulevard Gambetta");
        System.out.println("\nDistance totale : 850 m");

        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    private void demanderTourneeMultiPoints() {
        System.out.print("Liste des intersections à visiter (séparées par des virgules) : ");
        String liste = scanner.nextLine();

        String[] points = liste.split(",");
        System.out.println("\n[HO1] Calcul de la tournée pour " + points.length + " points...");

        // Ici, vous intégrerez votre logique métier pour HO1
        // Exemple d'appel à une classe de service :
        // TourneeService.calculerTourneeHO1(points);

        System.out.println("\nRésultat :");
        System.out.println("Ordre optimal :");
        for (int i = 0; i < points.length; i++) {
            System.out.println((i+1) + ". " + points[i].trim());
        }
        System.out.println("\nDistance totale : 1250 m");

        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}
