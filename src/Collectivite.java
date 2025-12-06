import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Collectivite extends JFrame {

    // ================================
    // 🔥 GRAPHES GLOBAUX PARTAGÉS
    // ================================
    private static Graphe graphePlanGlobal = null;          // Plan simplifié (A, B, C…)
    private static Graphe grapheCirculationGlobal = null;   // Graphe réel circulation

    private Graphe graphePlan;
    private Graphe grapheCirculation;

    // =========================================================
    // 🔥 ACCÈS GLOBAL AU GRAPHE CIRCULATION (avec auto-chargement)
    // =========================================================
    public static Graphe getGrapheCirculation() {

        // 🔥 Très important : garantir un graphe valide même si
        // la mairie n'a jamais été ouverte.
        if (grapheCirculationGlobal == null) {
            grapheCirculationGlobal =
                    GrapheLoaderCirculation.charger("nice_arcs_orientes_complets.txt");
        }

        return grapheCirculationGlobal;
    }

    // =========================================================
    // 🔥 ACCÈS GLOBAL AU GRAPHE PLAN (optionnel)
    // =========================================================
    public static Graphe getGraphePlan() {

        if (graphePlanGlobal == null) {
            graphePlanGlobal =
                    GrapheLoader.chargerDepuisFichier("nice_graphe_collectivite.txt");
        }

        return graphePlanGlobal;
    }

    // =========================================================
    // 🔥 CONSTRUCTEUR : INTERFACE COLLECTIVITÉ
    // =========================================================
    public Collectivite() {

        // Chargement automatique au cas où
        if (graphePlanGlobal == null) {
            graphePlanGlobal =
                    GrapheLoader.chargerDepuisFichier("nice_graphe_collectivite.txt");
        }
        if (grapheCirculationGlobal == null) {
            grapheCirculationGlobal =
                    GrapheLoaderCirculation.charger("nice_arcs_orientes_complets.txt");
        }

        // Références locales
        this.graphePlan = graphePlanGlobal;
        this.grapheCirculation = grapheCirculationGlobal;

        // -----------------------------------------------------
        // 🔥 INTERFACE GRAPHIQUE
        // -----------------------------------------------------
        setTitle("Menu - Collectivité");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("src/collectivite.png");
        Image scaled = icon.getImage().getScaledInstance(950, 550, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));

        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setFocusable(true);

        // -----------------------------------------------------
        // 🔥 GESTION DES TOUCHES
        // -----------------------------------------------------
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // 1 : Afficher plan simplifié
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    new AfficherGraphe(graphePlan);
                }

                // 2 : Modifications circulation
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    new ModificationsCirculation(grapheCirculation);
                }

                // 3 : Voir quantités (si tu l'ajoutes plus tard)
                if (e.getKeyCode() == KeyEvent.VK_3) {
                    // exemple : new QuantitesDechets()
                }

                // Retour utilisateur
                if (e.getKeyCode() == KeyEvent.VK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                    dispose();
                    new Utilisateur();
                }
            }
        });

        add(imageLabel, BorderLayout.CENTER);
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
