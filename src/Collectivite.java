import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Collectivite extends JFrame {

    // Graphes partagés (une seule instance pour tout le programme)
    private static Graphe graphePlanGlobal;          // Graphe simplifié (A, B, C…)
    private static Graphe grapheCirculationGlobal;   // Graphe réel (Sxxxxxx)

    private Graphe graphePlan;
    private Graphe grapheCirculation;

    public Collectivite() {

        if (graphePlanGlobal == null) {
            graphePlanGlobal = GrapheLoader.chargerDepuisFichier("nice_graphe_collectivite.txt");
        }
        if (grapheCirculationGlobal == null) {
            grapheCirculationGlobal = GrapheLoaderCirculation.charger("nice_arcs_orientes_complets.txt");
        }

        // Références locales vers les graphes partagés
        this.graphePlan = graphePlanGlobal;
        this.grapheCirculation = grapheCirculationGlobal;

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

        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                int code = e.getKeyCode();

                // 1 : Soumettre le plan de la commune
                if (code == KeyEvent.VK_1) {
                    new AfficherGraphe(graphePlan);
                }

                // 2 : Signaler les modifications de circulation
                else if (code == KeyEvent.VK_2) {
                    new ModificationsCirculation(grapheCirculation);
                }

                // 3 : Consulter les quantités de déchets récoltés (NOUVEAU)
                else if (code == KeyEvent.VK_3) {
                    new FenetreQuantitesDechets();   // ← nouvelle fenêtre que je t’ai fournie
                }

                // Espace ou Échap : retour à la page Utilisateur
                else if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
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
