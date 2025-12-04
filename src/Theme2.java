import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

public class Theme2 extends JFrame {

    private static final int CAPACITE_CAMION = 10;

    public Theme2() {

        setTitle("Thème 2 – Optimiser les ramassages");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------------------------------------
        // CHARGEMENT IMAGE / LABEL (PAS DE FOCUS)
        // ---------------------------------------
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

        label.setFocusable(false); // IMPORTANT !!!
        setContentPane(label);

        // ---------------------------------------
        //        KEY LISTENER SUR LA FENETRE
        // ---------------------------------------
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                try {
                    int code = e.getKeyCode();

                    switch (code) {

                        case KeyEvent.VK_1:
                        case KeyEvent.VK_NUMPAD1:
                            executerPPV();
                            break;

                        case KeyEvent.VK_2:
                        case KeyEvent.VK_NUMPAD2:
                            executerMST();
                            break;

                        case KeyEvent.VK_ESCAPE:
                            dispose();
                            new MenuPrincipal(1);
                            break;
                    }

                } catch (Exception ex) {
                    afficherResultat("Erreur", "Erreur : " + ex.getMessage());
                }
            }
        });

        // ---------------------------------------
        // DONNER LE FOCUS AU FRAME, PAS AU LABEL
        // ---------------------------------------
        setFocusable(true);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        setVisible(true);
    }

    // ----------------------------------------------------
    //      FENÊTRE SCROLLABLE POUR AFFICHAGE DES RÉSULTATS
    // ----------------------------------------------------
    private void afficherResultat(String titre, String contenu) {

        JTextArea area = new JTextArea(contenu);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setLineWrap(false);        // Pas de wrap vertical
        area.setWrapStyleWord(false);   // Pas de wrap horizontal

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(800, 500));

        JFrame frame = new JFrame(titre);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scroll);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ----------------------------------------------------
    //                 APPROCHE PPV
    // ----------------------------------------------------
    private void executerPPV() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        List<PointCollecte> ordre = PlusProcheVoisin.calculer(depot, points);

        afficherResultat("Résultat PPV", formatPPV(ordre, depot));
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

    // ----------------------------------------------------
    //                 APPROCHE MST
    // ----------------------------------------------------
    private void executerMST() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        MSTPrimResult mst = MSTPrim.construire(points, depot);

        List<Integer> parcours = ParcoursPrefixe.dfs(mst.parent);
        List<Integer> ordre = Shortcutting.appliquer(parcours);

        afficherResultat("Résultat MST",
                formatMST(ordre, points, depot, mst.ids));
    }

    private String formatMST(List<Integer> ordre,
                             List<PointCollecte> points,
                             Depot depot,
                             List<String> ids) {

        StringBuilder sb = new StringBuilder("Approche : MST + DFS + Shortcutting\n\n");

        sb.append("Départ : Dépôt (").append(depot.getSommetId()).append(")\n\n");

        for (int idx : ordre) {

            if (idx == 0) continue;

            String idPoint = ids.get(idx);

            PointCollecte pc = points.stream()
                    .filter(p -> p.getSommetId().equals(idPoint))
                    .findFirst()
                    .orElse(null);

            if (pc == null) continue;

            sb.append(" → ").append(pc.getNom())
                    .append(" (").append(pc.getSommetId()).append(")")
                    .append(" | contenance = ").append(pc.getContenance()).append("\n");
        }

        sb.append("\n--- Découpage selon capacité (C=")
                .append(CAPACITE_CAMION)
                .append(") ---\n\n");

        sb.append(decouperTournees(ordre, points, depot, ids));

        return sb.toString();
    }

    private String decouperTournees(List<Integer> ordre,
                                    List<PointCollecte> points,
                                    Depot depot,
                                    List<String> ids) {

        StringBuilder sb = new StringBuilder();

        int capaciteRestante = CAPACITE_CAMION;
        int numTournee = 1;

        sb.append("Tournée ").append(numTournee).append(" :\n");
        sb.append("Départ → Dépôt\n");

        for (int idx : ordre) {

            if (idx == 0) continue;

            String idPoint = ids.get(idx);

            PointCollecte pc = points.stream()
                    .filter(p -> p.getSommetId().equals(idPoint))
                    .findFirst()
                    .orElse(null);

            if (pc == null) continue;

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
