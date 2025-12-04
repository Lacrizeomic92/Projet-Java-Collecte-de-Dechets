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
//                 APPROCHE PPV (CORRIGÉE)
// ----------------------------------------------------
    private void executerPPV() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        // Le nouveau PPV renvoie maintenant un objet résultat
        PlusProcheVoisin.ResultatPPV resultat = PlusProcheVoisin.calculer(depot, points);

        afficherResultat("Résultat PPV", formatPPV(resultat, depot));
    }


    private String formatPPV(PlusProcheVoisin.ResultatPPV resultat, Depot depot) {

        StringBuilder sb = new StringBuilder();

        sb.append("=== Approche : Plus Proche Voisin ===\n\n");
        sb.append("Départ : Dépôt (").append(depot.getSommetId()).append(")\n\n");

        int etapeNum = 1;

        for (PlusProcheVoisin.Etape etape : resultat.etapes) {

            sb.append(etapeNum++)
                    .append(") ")
                    .append(etape.depart)
                    .append(" → ")
                    .append(etape.arrivee)
                    .append("\n");

            sb.append("   Distance : ")
                    .append(String.format("%.2f m", etape.distance))
                    .append("\n");

            sb.append("   Rues empruntées :\n");

            if (etape.chemin.isEmpty()) {
                sb.append("      (Aucune rue renseignée)\n");
            } else {
                for (String rue : etape.chemin) {
                    sb.append("      - ").append(rue).append("\n");
                }
            }
            sb.append("\n");
        }

        sb.append("=== Distance totale parcourue : ")
                .append(String.format("%.2f m", resultat.distanceTotale))
                .append(" ===\n");

        return sb.toString();
    }


    // ----------------------------------------------------
    //                 APPROCHE MST
    // ----------------------------------------------------
    private void executerMST() throws Exception {

        Depot depot = ChargeurPointsCollecte.chargerDepot("points_collecte_nice.txt");
        List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints("points_collecte_nice.txt");

        MSTPrimResult mst = MSTPrim.construire(points, depot);

        // 1) DFS sur le MST
        List<Integer> parcours = ParcoursPrefixe.dfs(mst.parent);

        // 2) Shortcutting
        List<Integer> ordre = Shortcutting.appliquer(parcours);

        // 3) Formatage du résultat (avec distances + rues)
        afficherResultat("Résultat MST", formatMST(ordre, points, depot, mst.ids));
    }


    private String formatMST(List<Integer> ordre,
                             List<PointCollecte> points,
                             Depot depot,
                             List<String> ids) {

        StringBuilder sb = new StringBuilder();
        sb.append("=== Approche : MST + DFS + Shortcutting ===\n\n");
        sb.append("Départ : ").append(depot.getSommetId()).append("\n\n");

        double distanceTotale = 0.0;
        String courant = depot.getSommetId();

        // Parcours des points dans l'ordre MST
        for (int idx : ordre) {

            if (idx == 0) continue;

            String idPoint = ids.get(idx);

            PointCollecte pc = points.stream()
                    .filter(p -> p.getSommetId().equals(idPoint))
                    .findFirst()
                    .orElse(null);

            if (pc == null) continue;

            // ---------- CORRECTION : TRY/CATCH SUR LE DIJKSTRA ----------
            DijkstraNice.CheminResult res;
            try {
                res = DijkstraNice.dijkstra(courant, idPoint);
            } catch (Exception e) {
                sb.append("ERREUR : Impossible d'obtenir le chemin entre ")
                        .append(courant).append(" et ").append(idPoint).append("\n\n");
                courant = idPoint;
                continue;
            }

            sb.append(courant).append(" → ").append(idPoint)
                    .append(" (").append(pc.getNom()).append(")\n");
            sb.append("   Distance : ").append(String.format("%.2f m", res.distance)).append("\n");
            sb.append("   Rues :\n");

            for (String rue : res.rues)
                sb.append("      - ").append(rue).append("\n");

            sb.append("\n");

            distanceTotale += res.distance;
            courant = idPoint;
        }

        // ---------- RETOUR AU DÉPÔT ----------
        try {
            DijkstraNice.CheminResult retour = DijkstraNice.dijkstra(courant, depot.getSommetId());

            sb.append(courant).append(" → ").append(depot.getSommetId()).append(" (Retour dépôt)\n");
            sb.append("   Distance : ").append(String.format("%.2f m", retour.distance)).append("\n");
            sb.append("   Rues :\n");

            for (String rue : retour.rues)
                sb.append("      - ").append(rue).append("\n");

            sb.append("\n");

            distanceTotale += retour.distance;

        } catch (Exception e) {
            sb.append("ERREUR : Impossible de retourner au dépôt.\n");
        }

        sb.append("=== Distance totale MST : ")
                .append(String.format("%.2f m", distanceTotale))
                .append(" ===\n");

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
