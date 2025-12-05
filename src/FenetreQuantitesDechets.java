import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class FenetreQuantitesDechets extends JFrame {

    private static final String FICHIER_POINTS = "points_collecte_nice.txt";

    public FenetreQuantitesDechets() {
        setTitle("Quantités de déchets récoltés");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] colonnes = { "Point de collecte", "Sommet associé", "Quantité (kg)" };
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0);

        int total = 0;

        try {
            List<PointCollecte> points = ChargeurPointsCollecte.chargerPoints(FICHIER_POINTS);

            for (PointCollecte p : points) {
                int qte = p.getContenance(); 
                total += qte;

                modele.addRow(new Object[]{
                        p.getNom(),
                        p.getSommetId(),
                        qte
                });
            }

            modele.addRow(new Object[]{ "TOTAL", "", total });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors du chargement des quantités de déchets :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JTable table = new JTable(modele);
        JScrollPane scrollPane = new JScrollPane(table);

        JLabel info = new JLabel(
                "Données issues du fichier : " + FICHIER_POINTS,
                SwingConstants.CENTER
        );

        add(scrollPane, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);

        setVisible(true);
    }
}
