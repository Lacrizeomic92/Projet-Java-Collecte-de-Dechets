import java.util.Random;
public class Configuration {

    // === CAPACITÉS RÉALISTES POUR UNE COMMUNE ===

    // Capacité d'un camion-benne standard (en tonnes)
    public static final int CAPACITE_CAMION = 10;  // Réel: 8-12 tonnes

    // Nombre de camions disponibles simultanément
    public static final int NOMBRE_CAMIONS = 3;    // Réel: 2-4 pour une commune moyenne

    // Capacité maximale par jour (C × N)
    public static final int CAPACITE_MAX_PAR_JOUR = CAPACITE_CAMION * NOMBRE_CAMIONS;

    // Nombre de secteurs (clusters)
    public static final int NOMBRE_SECTEURS = 6;

    // === QUANTITÉS DE DÉCHETS RÉALISTES ===

    // Production moyenne de déchets par habitant (kg/jour)
    public static final double PRODUCTION_PAR_HABITANT = 1.5;  // kg/jour

    // Nombre moyen d'habitants par secteur
    public static final int HABITANTS_PAR_SECTEUR = 5000;

    // Production journalière par secteur (en kg)
    public static final int PRODUCTION_SECTEUR_JOUR =
            (int)(PRODUCTION_PAR_HABITANT * HABITANTS_PAR_SECTEUR);  // ~7.5 tonnes

    // === FRÉQUENCE DE COLLECTE ===

    // Nombre de jours entre deux collectes pour un même secteur
    public static final int FREQUENCE_COLLECTE = 7;  // 1 fois par semaine

    // === AUTRES PARAMÈTRES ===

    // Taux de remplissage maximum recommandé (en %)
    public static final int TAUX_REMPLISSAGE_MAX = 85;

    // Marge de sécurité (en %)
    public static final int MARGE_SECURITE = 10;

    // === MÉTHODES DE CALCUL ===

    public static int calculerQuantiteRealiste(int degreSecteur) {
        // Plus un secteur est dense (plus de voisins), plus il produit de déchets
        Random rand = new Random(degreSecteur * 123);

        // Base: production moyenne ± variation
        int base = PRODUCTION_SECTEUR_JOUR / 1000;  // Convertir kg en tonnes
        int variation = rand.nextInt(3) - 1;  // -1, 0, ou +1

        // Ajustement selon le degré (connectivité)
        int ajustementDegre = degreSecteur * 500 / 1000;  // 0.5 tonne par voisin

        return Math.max(3, base + variation + ajustementDegre);  // Minimum 3 tonnes
    }

    public static void afficherConfiguration() {
        System.out.println("\n=== CONFIGURATION RÉALISTE ===");
        System.out.println("Camion: " + CAPACITE_CAMION + " tonnes");
        System.out.println("Nombre de camions: " + NOMBRE_CAMIONS);
        System.out.println("Capacité max/jour: " + CAPACITE_MAX_PAR_JOUR + " tonnes");
        System.out.println("Secteurs: " + NOMBRE_SECTEURS);
        System.out.println("Production/habitant: " + PRODUCTION_PAR_HABITANT + " kg/j");
        System.out.println("Habitants/secteur: " + HABITANTS_PAR_SECTEUR);
        System.out.println("Production/secteur: ~" + (PRODUCTION_SECTEUR_JOUR/1000) + " tonnes/j");
        System.out.println("Fréquence collecte: 1 fois/" + FREQUENCE_COLLECTE + " jours");
        System.out.println("Taux remplissage max: " + TAUX_REMPLISSAGE_MAX + "%");
    }
}
