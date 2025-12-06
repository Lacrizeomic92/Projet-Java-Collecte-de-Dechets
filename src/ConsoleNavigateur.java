import java.util.Scanner;

public class ConsoleNavigateur {
    private Scanner scanner;

    public ConsoleNavigateur() {
        scanner = new Scanner(System.in);
    }

    public void demarrer() {
        afficherEnTete();

        boolean continuer = true;

        while (continuer) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Choix des Hypothèses");
            System.out.println("2. Quitter");
            System.out.print("Votre choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {
                case 1:
                    menuChoixHypotheses();
                    break;
                case 2:
                    continuer = false;
                    System.out.println("\nMerci d'avoir utilisé notre application. Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }

    private void afficherEnTete() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           APPLICATION DE NAVIGATION          ║");
        System.out.println("║              Ville de Nice                   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("\nBienvenue dans la version console de l'application.");
        System.out.println("===================================================");
    }

    private void menuChoixHypotheses() {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== CHOIX DES HYPOTHÈSES ===");
            System.out.println("1. HO1 - Double Sens");
            System.out.println("2. HO2 - Sens Unique");
            System.out.println("3. HO3 - Mixte");
            System.out.println("0. Retour au menu principal");
            System.out.print("\nChoisissez votre hypothèse : ");

            int choixHyp = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choixHyp) {
                case 0:
                    retour = true;
                    break;
                case 1:
                    System.out.println("\n=== HYPOTHÈSE HO1 - DOUBLE SENS ===");
                    GererHO1 gererHO1 = new GererHO1(scanner);
                    gererHO1.afficherMenu();
                    break;
                case 2:
                    System.out.println("\n=== HYPOTHÈSE HO2 - SENS UNIQUE ===");
                    GererHO2 gererHO2 = new GererHO2(scanner);
                    gererHO2.afficherMenu();
                    break;
                case 3:
                    System.out.println("\n=== HYPOTHÈSE HO3 - MIXTE ===");
                    GererHO3 gererHO3 = new GererHO3(scanner);
                    gererHO3.afficherMenu();
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
}
