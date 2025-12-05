import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AfficherGrapheVisuel extends JFrame {

    private Graphe graphe;

    public AfficherGrapheVisuel(Graphe g) {
        this.graphe = g;

        setTitle("Visualisation du graphe");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new PanelDessin());

        setVisible(true);
    }

    class PanelDessin extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            // Positionnement automatique des noeuds en cercle
            int radius = 200;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            HashMap<String, Point> positions = new HashMap<>();
            int i = 0;
            double angleStep = 2 * Math.PI / graphe.nodes.size();

            for (Graphe.Node n : graphe.nodes) {
                int x = (int) (centerX + radius * Math.cos(i * angleStep));
                int y = (int) (centerY + radius * Math.sin(i * angleStep));
                positions.put(n.id, new Point(x, y));
                i++;
            }

            // Dessiner les arêtes
            for (Graphe.Edge e : graphe.edges) {
                Point p1 = positions.get(e.from);
                Point p2 = positions.get(e.to);

                g2.setColor(Color.BLACK);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Distance affichée au milieu
                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;
                g2.drawString(e.distance + " m", mx, my);

                // Sens unique : flèche
                if (e.sens.equals("ONE_WAY")) {
                    drawArrow(g2, p1.x, p1.y, p2.x, p2.y);
                }
            }

            // Dessiner les noeuds
            for (Graphe.Node n : graphe.nodes) {
                Point p = positions.get(n.id);
                g2.setColor(Color.GREEN);
                g2.fillOval(p.x - 20, p.y - 20, 40, 40);
                g2.setColor(Color.BLACK);
                g2.drawString(n.id, p.x - 5, p.y + 5);
            }
        }

        private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
            int dx = x2 - x1;
            int dy = y2 - y1;
            double angle = Math.atan2(dy, dx);

            int len = 15;
            int wing = 6;

            int x = x2 - (int)(len * Math.cos(angle));
            int y = y2 - (int)(len * Math.sin(angle));

            int xA = x + (int)(wing * Math.cos(angle + Math.PI / 2));
            int yA = y + (int)(wing * Math.sin(angle + Math.PI / 2));

            int xB = x + (int)(wing * Math.cos(angle - Math.PI / 2));
            int yB = y + (int)(wing * Math.sin(angle - Math.PI / 2));

            g2.drawLine(x2, y2, xA, yA);
            g2.drawLine(x2, y2, xB, yB);
        }
    }
}
