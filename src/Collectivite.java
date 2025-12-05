import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Collectivite extends JFrame {

    // === GRAPHES PARTAGÉS PAR TOUT LE PROGRAMME ===
    private static Graphe graphePlanGlobal;          // Graphe simple (A, B, C…)
    private static Graphe grapheCirculationGlobal;   // Graphe complet (Sxxxx)

    // === GETTERS STATIQUES POUR Y ACCÉDER PARTOUT ===
    public static Graphe getGrapheCirculation() {
        return grapheCirculationGlobal;
    }

    public static Graphe getGraphePlan() {
        return graphePlanGlobal;
    }

    // === CONSTRUCTEUR ===
    public Collectivite() {

        // --- Chargement unique ---
        if (graphePlanGlobal == null) {
            graphePlanGlobal = GrapheLoader.chargerDepuisFichier("nice_graphe_collectivite.txt");
        }

        if (grapheCirculationGlobal == null) {
            grapheCirculationGlobal = GrapheLoaderCirculation.charger("nice_arcs_orientes_complets.txt");
        }

        // --- Interface ---
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

        // --- Gestion des touches ---
        imageLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_1:
                        new AfficherGraphe(graphePlanGlobal);
                        break;

                    case KeyEvent.VK_2:
                        new ModificationsCirculation(grapheCirculationGlobal);
                        break;

                    case KeyEvent.VK_3:
                        new FenetreQuantitesDechets();
                        break;

                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ESCAPE:
                        dispose();
                        new Utilisateur();
                        break;
                }
            }
        });

        add(imageLabel, BorderLayout.CENTER);
        SwingUtilities.invokeLater(imageLabel::requestFocusInWindow);
        setVisible(true);
    }
}
