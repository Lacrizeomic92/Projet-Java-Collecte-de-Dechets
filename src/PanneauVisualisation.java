import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PanneauVisualisation extends JPanel {
    private final SecteurGraphe grapheSecteurs;
    private final SecteurGraphe original;
    private final HashMap<Integer, ArrayList<NoeudSecteur>> clusters;

    private final Color[] couleursJours = {
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.ORANGE
    };

    public PanneauVisualisation(SecteurGraphe grapheSecteurs,
                                SecteurGraphe original,
                                HashMap<Integer, ArrayList<NoeudSecteur>> clusters) {
        this.grapheSecteurs = grapheSecteurs;
        this.original = original;
        this.clusters = clusters;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200, 800));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dessinerAretes(g2);
        dessinerSecteurs(g2);
        dessinerLegende(g2);
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
          
            int couleurIndex = secteur.couleur % couleursJours.length;
            g2.setColor(couleursJours[couleurIndex]);
            g2.fillOval(x - 30, y - 30, 60, 60);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x - 30, y - 30, 60, 60);

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String nom = secteur.nom;
            g2.drawString(nom, x - 20, y - 40);
            g2.drawString("J" + (secteur.couleur + 1), x - 15, y + 50);
        }
    }

    private void dessinerLegende(Graphics2D g2) {
        Set<Integer> joursUtilises = new HashSet<>();
        if (grapheSecteurs != null) {
            for (NoeudSecteur s : grapheSecteurs.noeuds) {
                joursUtilises.add(s.couleur);
            }
        }

        g2.setColor(new Color(255, 255, 255, 240));
        g2.fillRect(20, 20, 300, 200);
        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, 300, 200);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("LÉGENDE - PLANIFICATION H01", 30, 40);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("6 secteurs • " + (grapheSecteurs != null ? grapheSecteurs.aretes.size() : 0) + " connexions", 30, 60);
        g2.drawString(joursUtilises.size() + " jours nécessaires", 30, 80);

        g2.drawString("Couleurs des jours:", 30, 100);

        int y = 120;
        int x = 30;

        for (int jour : joursUtilises) {
            int couleurIndex = jour % couleursJours.length;

            g2.setColor(couleursJours[couleurIndex]);
            g2.fillRect(x, y - 10, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y - 10, 15, 15);

            g2.drawString("Jour " + (jour + 1), x + 20, y);

            y += 20;

            if (y > 180) {
                y = 120;
                x += 120;
            }
        }
    }
}
