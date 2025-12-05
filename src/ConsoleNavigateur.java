import java.util.Scanner;

public class ConsoleNavigateur {
    private Scanner scanner;
    private String profilUtilisateur;
    private String hypotheseCourante;

    public ConsoleNavigateur() {
        scanner = new Scanner(System.in);
        profilUtilisateur = null;
        hypotheseCourante = null;
    }

    public void demarrer() {
        boolean continuer = true;

        while (continuer) {
            if (profilUtilisateur == null) {
                // Étape 1 : Choix du profil
                choisirProfil();
            } else if (profilUtilisateur.equals("entreprise") && hypotheseCourante == null) {
                // Étape 2 pour l'entreprise : Choix de l'hypothèse
                choisirHypothese();
            } else {
                // Étape 3 : Menu selon le profil
                afficherMenuPrincipal();
            }
        }

        scanner.close();
    }

    private void choisirProfil() {
        System.out.println("\n=== CHOIX DU PROFIL UTILISATEUR ===");
        System.out.println("Quel est votre profil ?");
        System.out.println("1. COLLECTIVITÉ (Mairie, Communauté de communes)");
        System.out.println("   - Soumettre le plan de la commune");
        System.out.println("   - Signaler les modifications de circulation");
        System.out.println("   - Consulter les quantités de déchets récoltés");
        System.out.println();
        System.out.println("2. ENTREPRISE DE COLLECTE");
        System.out.println("   - Calculer les itinéraires de ramassage");
        System.out.println("   - Planifier les jours de collecte");
        System.out.println("   - Programmer les camions");
        System.out.println();
        System.out.println("3. Quitter l'application");
        System.out.print("\nVotre choix : ");

        int choix = lireEntier();

        switch (choix) {
            case 1:
                profilUtilisateur = "collectivite";
                System.out.println("\n✓ Profil : COLLECTIVITÉ sélectionné");
                break;
            case 2:
                profilUtilisateur = "entreprise";
                System.out.println("\n✓ Profil : ENTREPRISE DE COLLECTE sélectionné");
                break;
            case 3:
                System.out.println("\nMerci d'avoir utilisé l'application. Au revoir !");
                System.exit(0);
                break;
            default:
                System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }

    private void choisirHypothese() {
        System.out.println("\n=== CHOIX DE L'HYPOTHÈSE D'ORIENTATION DES RUES ===");
        System.out.println("Pour optimiser la collecte, choisissez comment modéliser le réseau :");
        System.out.println();

        System.out.println("1. HO1 - Toutes les rues sont à double sens");
        System.out.println("   → Graphe non orienté");
        System.out.println("   → Ramassage des deux côtés en un seul passage");
        System.out.println();

        System.out.println("2. HO2 - Certaines rues peuvent être à sens unique");
        System.out.println("   → Graphe orienté");
        System.out.println("   → Ramassage uniquement du côté de la voie");
        System.out.println();

        System.out.println("3. HO3 - Mixte : rues à sens unique et à double sens");
        System.out.println("   → Graphe mixte");
        System.out.println("   → Traitement différent selon le type de rue");
        System.out.println();

        System.out.println("4. Retour au choix du profil");
        System.out.print("\nChoisissez une hypothèse : ");

        int choix = lireEntier();

        switch (choix) {
            case 1:
                hypotheseCourante = "HO1";
                System.out.println("\n Hypothèse HO1 (Double sens) sélectionnée");
                break;
            case 2:
                hypotheseCourante = "HO2";
                System.out.println("\n Hypothèse HO2 (Sens unique) sélectionnée");
                break;
            case 3:
                hypotheseCourante = "HO3";
                System.out.println("\n Hypothèse HO3 (Mixte) sélectionnée");
                break;
            case 4:
                profilUtilisateur = null;
                break;
            default:
                System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }

    private void afficherMenuPrincipal() {
        boolean retourMenu = false;

        while (!retourMenu) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("PROFIL : " + profilUtilisateur.toUpperCase());
            if (profilUtilisateur.equals("entreprise")) {
                System.out.println("HYPOTHÈSE : " + hypotheseCourante);
            }
            System.out.println("=".repeat(60));

            if (profilUtilisateur.equals("collectivite")) {
                // MENU COLLECTIVITÉ - 3 options comme dans ton screenshot
                System.out.println("\n=== MENU COLLECTIVITÉ ===");
                System.out.println("1. Soumettre le plan de la commune");
                System.out.println("2. Signaler les modifications de circulation");
                System.out.println("3. Consulter les quantités de déchets récoltés");
                System.out.println("4. Changer de profil");
                System.out.println("5. Quitter l'application");
                System.out.print("\nVotre choix : ");

                int choix = lireEntier();

                switch (choix) {
                    case 1:
                        soumettrePlanCommune();
                        break;
                    case 2:
                        signalerModificationsCirculation();
                        break;
                    case 3:
                        consulterQuantitesDechets();
                        break;
                    case 4:
                        profilUtilisateur = null;
                        hypotheseCourante = null;
                        retourMenu = true;
                        break;
                    case 5:
                        System.out.println("\nMerci. Au revoir !");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } else {
                // MENU ENTREPRISE
                System.out.println("\n=== MENU ENTREPRISE ===");
                System.out.println("Hypothèse active : " + hypotheseCourante);
                System.out.println();
                System.out.println("THÈMES DE TRAVAIL :");
                System.out.println("1. Thème 1 : Optimiser le ramassage aux pieds des habitations");
                System.out.println("2. Thème 2 : Optimiser les ramassages des points de collecte");
                System.out.println("3. Thème 3 : Planifier les jours de passage");
                System.out.println();
                System.out.println("OUTILS :");
                System.out.println("4. Changer d'hypothèse d'orientation");
                System.out.println("5. Visualiser et tester des graphes");
                System.out.println("6. Changer de profil");
                System.out.println("7. Quitter l'application");
                System.out.print("\nVotre choix : ");

                int choix = lireEntier();

                switch (choix) {
                    case 1:
                        menuTheme1();
                        break;
                    case 2:
                        menuTheme2();
                        break;
                    case 3:
                        menuTheme3();
                        break;
                    case 4:
                        hypotheseCourante = null;
                        retourMenu = true;
                        break;
                    case 5:
                        menuVisualisationTests();
                        break;
                    case 6:
                        profilUtilisateur = null;
                        hypotheseCourante = null;
                        retourMenu = true;
                        break;
                    case 7:
                        System.out.println("\nMerci. Au revoir !");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            }
        }
    }

    // ========== MÉTHODES COLLECTIVITÉ (3 options seulement) ==========

    private void soumettrePlanCommune() {
        System.out.println("\n=== SOUMETTRE LE PLAN DE LA COMMUNE ===");
        System.out.println("Cette fonctionnalité permet de soumettre le plan de la commune");
        System.out.println("ou du territoire à couvrir par la collecte des déchets.");
        System.out.println();

        System.out.print("Nom de la commune : ");
        String commune = scanner.nextLine();

        System.out.println("\nOptions de soumission :");
        System.out.println("1. Téléverser un fichier (CSV, JSON, XML)");
        System.out.println("2. Saisir manuellement les données");
        System.out.println("3. Générer un plan de test");
        System.out.print("Votre choix : ");

        int choix = lireEntier();

        switch (choix) {
            case 1:
                System.out.print("Chemin du fichier : ");
                String chemin = scanner.nextLine();
                System.out.println("\n✓ Fichier " + chemin + " téléversé pour " + commune);
                break;
            case 2:
                System.out.print("Nombre de rues principales : ");
                int rues = lireEntier();
                System.out.print("Nombre de quartiers : ");
                int quartiers = lireEntier();
                System.out.println("\n✓ Plan manuel créé pour " + commune);
                System.out.println("  - " + rues + " rues principales");
                System.out.println("  - " + quartiers + " quartiers");
                break;
            case 3:
                System.out.println("\n✓ Plan de test généré pour " + commune);
                System.out.println("  - 20 intersections");
                System.out.println("  - 35 rues");
                System.out.println("  - 5 quartiers");
                break;
        }

        System.out.println("\nLe plan a été enregistré dans la base de données.");
        System.out.println("L'entreprise de collecte sera notifiée.");
    }

    private void signalerModificationsCirculation() {
        System.out.println("\n=== SIGNALER LES MODIFICATIONS DE CIRCULATION ===");
        System.out.println("Signalez ici les travaux, fermetures, ou changements");
        System.out.println("de circulation qui affectent la collecte des déchets.");
        System.out.println();

        System.out.print("Type de modification : ");
        System.out.println("\n1. Travaux routiers");
        System.out.println("2. Fermeture temporaire");
        System.out.println("3. Sens unique modifié");
        System.out.println("4. Stationnement interdit");
        System.out.println("5. Autre");
        System.out.print("Votre choix : ");

        int type = lireEntier();
        scanner.nextLine(); // consommer la ligne

        System.out.print("Lieu (rue, quartier) : ");
        String lieu = scanner.nextLine();

        System.out.print("Date de début (JJ/MM/AAAA) : ");
        String debut = scanner.nextLine();

        System.out.print("Date de fin (JJ/MM/AAAA) : ");
        String fin = scanner.nextLine();

        System.out.print("Description détaillée : ");
        String description = scanner.nextLine();

        System.out.println("\n✓ Modification signalée :");
        System.out.println("  Lieu : " + lieu);
        System.out.println("  Période : " + debut + " au " + fin);
        System.out.println("  Description : " + description);
        System.out.println("\nCette information sera transmise à l'entreprise de collecte.");
    }

    private void consulterQuantitesDechets() {
        System.out.println("\n=== CONSULTER LES QUANTITÉS DE DÉCHETS RÉCOLTÉS ===");
        System.out.println("Statistiques de collecte - Dernier mois");
        System.out.println("════════════════════════════════════════════════════");

        System.out.println("\n📊 TOTAUX PAR TYPE DE DÉCHET :");
        System.out.printf("  Ordures ménagères : %8.2f tonnes\n", 85.4);
        System.out.printf("  Recyclage (plastique) : %5.2f tonnes\n", 12.3);
        System.out.printf("  Recyclage (verre) : %10.2f tonnes\n", 8.7);
        System.out.printf("  Recyclage (papier) : %9.2f tonnes\n", 9.5);
        System.out.printf("  Encombrants : %16.2f tonnes\n", 5.6);
        System.out.printf("  Déchets verts : %13.2f tonnes\n", 4.3);
        System.out.println("  ───────────────────────────────────");
        System.out.printf("  TOTAL : %22.2f tonnes\n", 125.8);

        System.out.println("\n📈 ÉVOLUTION MENSUELLE :");
        System.out.println("  Mois précédent : 118.2 tonnes");
        System.out.println("  Il y a 3 mois : 112.5 tonnes");
        System.out.println("  Variation : +6.4%");

        System.out.println("\n🗺️  PAR QUARTIER :");
        System.out.println("  Quartier Nord : 32.1 tonnes");
        System.out.println("  Quartier Sud : 28.7 tonnes");
        System.out.println("  Centre-ville : 45.3 tonnes");
        System.out.println("  Zone industrielle : 19.7 tonnes");

        System.out.println("\n🚛 EFFICACITÉ DE COLLECTE :");
        System.out.println("  Nombre de tournées : 52");
        System.out.println("  Km parcourus : 1,345 km");
        System.out.println("  Taux de remplissage moyen : 78%");
        System.out.println("  Émissions CO₂ évitées : 3.1 tonnes");

        System.out.println("\n⚠️  ALERTES :");
        System.out.println("  - Quartier Nord : +15% de déchets ce mois-ci");
        System.out.println("  - Centre-ville : point de collecte saturé");

        System.out.print("\nVoulez-vous exporter ces données ? (O/N) : ");
        String export = scanner.nextLine();
        if (export.equalsIgnoreCase("O")) {
            System.out.println("✓ Données exportées au format CSV.");
        }
    }

    // ========== MÉTHODES ENTREPRISE (inchangées) ==========

    private void menuTheme1() {
        System.out.println("\n=== THÈME 1 - HYPOTHÈSE " + hypotheseCourante + " ===");
        System.out.println("OPTIMISATION DU RAMASSAGE AUX PIEDS DES HABITATIONS");
        System.out.println();
        System.out.println("Problématiques :");
        System.out.println("1. Problématique 1 : Collecte des encombrants");
        System.out.println("2. Problématique 2 : Collecte des poubelles");
        System.out.println("0. Retour au menu entreprise");
        System.out.print("\nChoisissez une problématique : ");

        int choix = lireEntier();

        switch (choix) {
            case 1:
                gererProblematique1();
                break;
            case 2:
                gererProblematique2();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private void gererProblematique1() {
        System.out.println("\n=== COLLECTE DES ENCOMBRANTS ===");
        System.out.println("Hypothèse d'orientation : " + hypotheseCourante);
        System.out.println();
        System.out.println("Options :");
        System.out.println("1. Calculer itinéraire pour un particulier");
        System.out.println("2. Calculer tournée groupée (max 10 adresses)");
        System.out.print("\nVotre choix : ");

        int choix = lireEntier();

        if (choix == 1) {
            System.out.print("Nombre d'adresses à collecter : ");
            int nbAdresses = lireEntier();
            System.out.println("\nCalcul du plus court chemin avec " + hypotheseCourante + "...");
        } else if (choix == 2) {
            System.out.print("Nombre de particuliers dans la tournée : ");
            int nbParticuliers = lireEntier();
            System.out.println("\nCalcul de la tournée optimale avec " + hypotheseCourante + "...");
        }
    }

    private void gererProblematique2() {
        System.out.println("\n=== COLLECTE DES POUBELLES ===");
        System.out.println("Hypothèse : " + hypotheseCourante);
        System.out.println();
        System.out.println("Cas disponibles :");
        System.out.println("1. Cas idéal : Tous les sommets de degrés pairs");
        System.out.println("2. Cas intermédiaire : Deux sommets de degrés impairs");
        System.out.println("3. Cas général : Aucune contrainte sur la parité");
        System.out.print("\nChoisissez un cas : ");

        int choix = lireEntier();

        System.out.println("\nCalcul avec " + hypotheseCourante + "...");
    }

    private void menuTheme2() {
        System.out.println("\n=== THÈME 2 - HYPOTHÈSE " + hypotheseCourante + " ===");
        System.out.println("OPTIMISATION DES POINTS DE COLLECTE");
        System.out.println();
        System.out.println("Approches disponibles :");
        System.out.println("1. Approche par plus proche voisin");
        System.out.println("2. Approche MST (Arbre couvrant minimum)");
        System.out.println("0. Retour au menu entreprise");
        System.out.print("\nChoisissez une approche : ");

        int choix = lireEntier();

        switch (choix) {
            case 1:
                System.out.println("\n=== APPROCHE PLUS PROCHE VOISIN ===");
                System.out.print("Nombre de points de collecte : ");
                int nbPoints = lireEntier();
                System.out.println("Calcul en cours...");
                break;
            case 2:
                System.out.println("\n=== APPROCHE MST ===");
                System.out.print("Nombre de points de collecte : ");
                nbPoints = lireEntier();
                System.out.println("Calcul en cours...");
                break;
        }
    }

    private void menuTheme3() {
        System.out.println("\n=== THÈME 3 - HYPOTHÈSE " + hypotheseCourante + " ===");
        System.out.println("PLANIFICATION DES JOURS DE PASSAGE");
        System.out.println();
        System.out.println("Hypothèses de planification :");
        System.out.println("1. Hypothèse 1 : Secteurs voisins ≠ même jour");
        System.out.println("2. Hypothèse 2 : Avec contraintes de capacité");
        System.out.println("0. Retour au menu entreprise");
        System.out.print("\nChoisissez une hypothèse : ");

        int choix = lireEntier();

        if (choix == 1) {
            System.out.print("Nombre de secteurs : ");
            int nbSecteurs = lireEntier();
            System.out.println("\nApplication de l'algorithme de coloration...");
        } else if (choix == 2) {
            System.out.print("Nombre de secteurs : ");
            int nbSecteurs = lireEntier();
            System.out.print("Charge maximale par camion : ");
            int capacite = lireEntier();
            System.out.println("\nPlanification avec contraintes...");
        }
    }

    private void menuVisualisationTests() {
        System.out.println("\n=== VISUALISATION ET TESTS ===");
        System.out.println("Hypothèse active : " + hypotheseCourante);
        System.out.println();
        System.out.println("1. Créer un graphe de test");
        System.out.println("2. Exporter les résultats");
        System.out.println("0. Retour");
        System.out.print("\nVotre choix : ");

        int choix = lireEntier();

        if (choix == 1) {
            System.out.print("Nombre de sommets : ");
            int sommets = lireEntier();
            System.out.println("\nCréation d'un graphe " + hypotheseCourante + "...");
        }
    }

    // ========== MÉTHODE UTILITAIRE ==========

    private int lireEntier() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide : ");
            }
        }
    }

    public static void main(String[] args) {
        ConsoleNavigateur navigateur = new ConsoleNavigateur();
        navigateur.demarrer();
    }
}
