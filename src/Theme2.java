import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

public class Theme2 extends JFrame {

    private static final int CAPACITE_CAMION = 10;  // Capacité max (CDC)

    public Theme2() {

        setTitle("Thème 2 – Optimiser les ramassages");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label;
        URL imgUrl = Theme2.class.getResource("/Theme2.png");
        ImageIcon icon = (imgUrl != null ? new ImageIcon(imgUrl) : null);

        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(scaled));
        } else {
            label = new JLabel("THÈME 2", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 40));
        }

        label.setFocusable(true);
        setContentPane(label);

        label.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                try {
                    switch (e.getKeyCode()) {

                        case KeyEvent.VK_1:
                            executerPPV();
                            break;

                        case KeyEvent.VK_2:
                            executerMST();
                            break;

                        case KeyEvent.VK_ESCAPE:
                            dispose();
                            new MenuPrincipal(1);
                            break;
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        SwingUtilities.invokeLater(label::requestFocusInWindow);
        setVisible(true);
    }

    // ======================================================
    //           APPROCHE 1 : PLUS PROCHE VOISIN
    // ======================================================
    private void executerPPV() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        List<PointCollecte> ordre = PlusProcheVoisin.calculer(depot, points);

        JOptionPane.showMessageDialog(this, formatPPV(ordre, depot));
    }

    private String formatPPV(List<PointCollecte> ordre, Depot depot) {

        StringBuilder sb = new StringBuilder("Approche : Plus Proche Voisin\n\n");

        sb.append("Départ : Dépôt (").append(depot.getSommetId()).append(")\n\n");

        for (PointCollecte pc : ordre) {
            sb.append(" → ").append(pc.getNom())
                    .append(" (").append(pc.getSommetId()).append(")")
                    .append(" | contenance = ").append(pc.getContenance()).append("\n");
        }

        return sb.toString();
    }

    // ======================================================
    //      APPROCHE 2 : MST + DFS + SHORTCUTTING
    // ======================================================
    private void executerMST() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        int[] parent = MSTPrim.construire(points, depot);

        List<Integer> parcours = ParcoursPrefixe.dfs(parent);
        List<Integer> ordre = Shortcutting.appliquer(parcours);

        String resultat = formatMST(ordre, points, depot);

        JOptionPane.showMessageDialog(this, resultat);
    }

    private String formatMST(List<Integer> ordre,
                             List<PointCollecte> points,
                             Depot depot) {

        StringBuilder sb = new StringBuilder("Approche : MST + DFS + Shortcutting\n\n");

        sb.append("Départ : Dépôt (").append(depot.getSommetId()).append(")\n\n");

        for (int idx : ordre) {
            if (idx == 0) continue;
            PointCollecte pc = points.get(idx - 1);
            sb.append(" → ").append(pc.getNom())
                    .append(" (").append(pc.getSommetId()).append(")")
                    .append(" | contenance = ").append(pc.getContenance()).append("\n");
        }

        sb.append("\n--- Découpage selon la capacité du camion (C = ")
                .append(CAPACITE_CAMION)
                .append(") ---\n\n");

        sb.append(decouperTournees(ordre, points, depot));

        return sb.toString();
    }

    // ======================================================
    //      DÉCOUPAGE DES TOURNÉES (CDC)
    // ======================================================
    private String decouperTournees(List<Integer> ordre,
                                    List<PointCollecte> points,
                                    Depot depot) {

        StringBuilder sb = new StringBuilder();

        int capaciteRestante = CAPACITE_CAMION;
        int numTournee = 1;

        sb.append("Tournée ").append(numTournee).append(" :\n");
        sb.append("Départ → Dépôt\n");

        for (int idx : ordre) {

            PointCollecte pc = points.get(idx - 1);

            if (pc.getContenance() > capaciteRestante) {
                sb.append("Retour → Dépôt\n\n");
                numTournee++;
                sb.append("Tournée ").append(numTournee).append(" :\n");
                capaciteRestante = CAPACITE_CAMION;
            }

            sb.append(" → ").append(pc.getNom())
                    .append(" (").append(pc.getSommetId()).append(")")
                    .append(" | charge = ").append(pc.getContenance()).append("u\n");

            capaciteRestante -= pc.getContenance();
        }

        sb.append("Retour → Dépôt\n");

        return sb.toString();
    }
}
