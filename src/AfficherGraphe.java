import javax.swing.*;
import java.awt.event.*;

public class AfficherGraphe extends JFrame {

    public AfficherGraphe(Graphe g) {

        setTitle("Plan de la commune - Graphe de Nice");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();

        sb.append("=== NOEUDS ===\n");
        for (Graphe.Node n : g.nodes) {
            sb.append(n.id).append(" : ").append(n.name).append("\n");
        }

        sb.append("\n=== RUES (ARÊTES) ===\n");
        for (Graphe.Edge e : g.edges) {
            sb.append(e.from + " <-> " + e.to +
                    "   Distance: " + e.distance + " m   Sens: " + e.sens + "\n");
        }

        area.setText(sb.toString());
        add(new JScrollPane(area));

        // Rendre la zone de texte focusable
        area.setFocusable(true);

        // AJOUT : Touche espace pour ouvrir le graphe visuel
        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    new AfficherGrapheVisuel(g);   // <-- NOUVELLE FENÊTRE GRAPHIQUE
                }
            }
        });

        SwingUtilities.invokeLater(area::requestFocusInWindow);

        setVisible(true);
    }
}
