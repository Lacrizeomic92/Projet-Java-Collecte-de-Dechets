import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PanneauVisualisationAvecCapacites extends JPanel {
    private final SecteurGraphe grapheSecteurs;
    private final SecteurGraphe original;
    private final HashMap<Integer, ArrayList<NoeudSecteur>> clusters;
    private final HashMap<Integer, ArrayList<NoeudSecteur>> planning;

    private final Color[] couleursJours = {
            new Color(255, 100, 100),    // Rouge
            new Color(100, 100, 255),    // Bleu
            new Color(100, 255, 100),    // Vert
            new Color(255, 255, 100),    // Jaune
            new Color(255, 150, 50),     // Orange
    };

    private static final int CAPACITE_MAX_PAR_JOUR = 30; // 15 × 2

    public PanneauVisualisationAvecCapacites(SecteurGraphe grapheSecteurs,
                                             SecteurGraphe original,
                                             HashMap<Integer, ArrayList<NoeudSecteur>> clusters,
                                             HashMap<Integer, ArrayList<NoeudSecteur>> planning) {
        this.grapheSecteurs = grapheSecteurs;
        this.original = original;
        this.clusters = clusters;
        this.planning = planning;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1300, 850));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dessinerAretes(g2);
        dessinerSecteurs(g2);
        dessinerLegende(g2);

        // 4. Planning par jour
        dessinerPlanning(g2);
    }

    private void dessinerAretes(Graphics2D g2) {
        if (grapheSecteurs == null || grapheSecteurs.aretes.isEmpty()) return;

        g2.setColor(new Color(100, 100, 100, 150));
        g2.setStroke(new BasicStroke(2));
        for (AreteSecteur arete : grapheSecteurs.aretes) {
            if (arete.a != null && arete.b != null) {
                g2.drawLine(arete.a.x, arete.a.y, arete.b.x, arete.b.y);
            }
        }
    }

    private void dessinerSecteurs(Graphics2D g2) {
        if (grapheSecteurs == null || grapheSecteurs.noeuds.isEmpty()) return;

        for (NoeudSecteur secteur : grapheSecteurs.noeuds) {
            int x = secteur.x;
            int y = secteur.y;

            // Cercle coloré selon le jour
            int couleurIndex = secteur.couleur % couleursJours.length;
            g2.setColor(couleursJours[couleurIndex]);
            g2.fillOval(x - 30, y - 30, 60, 60);

            // Bordure
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x - 30, y - 30, 60, 60);

            // Nom du secteur
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(secteur.nom, x - 20, y - 40);

            // Jour
            g2.drawString("J" + (secteur.couleur + 1), x - 15, y + 50);

            // Quantité de déchets
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(secteur.quantiteDechets + "t", x - 10, y + 70);
        }
    }

    private void dessinerLegende(Graphics2D g2) {
        // Fond
        g2.setColor(new Color(255, 255, 255, 240));
        g2.fillRect(20, 20, 320, 200);
        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, 320, 200);

        // Titre
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("PLANIFICATION AVEC CAPACITÉS", 30, 40);

        // Contraintes
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Contraintes respectées:", 30, 60);
        g2.drawString("• Voisins ≠ même jour", 40, 80);
        g2.drawString("• 1 tournée/secteur/jour", 40, 100);
        g2.drawString("• Camion: 15 tonnes max", 40, 120);
        g2.drawString("• 2 camions simultanés", 40, 140);
        g2.drawString("• Capacité max/jour: 30 tonnes", 40, 160);

        // Stats
        if (planning != null) {
            g2.drawString("Jours nécessaires: " + planning.size(), 30, 180);
            g2.drawString("Taux remplissage moyen: " +
                    calculerTauxRemplissage() + "%", 30, 200);
        }
    }

    private void dessinerPlanning(Graphics2D g2) {
        if (planning == null || planning.isEmpty()) return;

        // Position du planning (à droite)
        int xStart = 900;
        int yStart = 50;

        // Fond
        g2.setColor(new Color(240, 240, 255, 240));
        g2.fillRect(xStart - 20, yStart - 30, 380, 400);
        g2.setColor(new Color(100, 100, 150));
        g2.drawRect(xStart - 20, yStart - 30, 380, 400);

        // Titre
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("DÉTAIL PAR JOUR", xStart, yStart);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        int y = yStart + 30;

        // Trier les jours
        ArrayList<Integer> jours = new ArrayList<>(planning.keySet());
        Collections.sort(jours);

        for (int jour : jours) {
            ArrayList<NoeudSecteur> secteurs = planning.get(jour);
            int total = secteurs.stream().mapToInt(s -> s.quantiteDechets).sum();

            // Couleur du jour
            int couleurIndex = jour % couleursJours.length;
            g2.setColor(couleursJours[couleurIndex]);
            g2.fillRect(xStart, y - 10, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRect(xStart, y - 10, 15, 15);

            // Texte
            g2.drawString("Jour " + (jour + 1) + ":", xStart + 25, y);
            g2.drawString(total + "/30 tonnes", xStart + 100, y);

            // Barre de progression
            int barreWidth = 150;
            int remplissageWidth = (total * barreWidth) / CAPACITE_MAX_PAR_JOUR;

            g2.setColor(new Color(200, 200, 200));
            g2.fillRect(xStart + 180, y - 8, barreWidth, 10);
            g2.setColor(couleursJours[couleurIndex]);
            g2.fillRect(xStart + 180, y - 8, remplissageWidth, 10);
            g2.setColor(Color.BLACK);
            g2.drawRect(xStart + 180, y - 8, barreWidth, 10);

            // Pourcentage
            int pourcentage = (total * 100) / CAPACITE_MAX_PAR_JOUR;
            g2.drawString(pourcentage + "%", xStart + 340, y);

            // Secteurs (liste réduite)
            String secteursStr = secteurs.stream()
                    .map(s -> s.nom + "(" + s.quantiteDechets + ")")
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");

            if (secteursStr.length() > 30) {
                secteursStr = secteursStr.substring(0, 30) + "...";
            }

            g2.drawString(secteursStr, xStart, y + 15);

            y += 40;
        }
    }

    private int calculerTauxRemplissage() {
        if (planning == null || planning.isEmpty()) return 0;

        int totalDechets = 0;
        for (ArrayList<NoeudSecteur> secteurs : planning.values()) {
            totalDechets += secteurs.stream().mapToInt(s -> s.quantiteDechets).sum();
        }

        int capaciteTotale = CAPACITE_MAX_PAR_JOUR * planning.size();
        return (totalDechets * 100) / capaciteTotale;
    }
}
