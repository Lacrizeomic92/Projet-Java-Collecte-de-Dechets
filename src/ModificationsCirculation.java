import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModificationsCirculation extends JFrame {

    private Graphe graphe;  // graphe de circulation partagé

    public ModificationsCirculation(Graphe g) {

        this.graphe = g;

        setTitle("Modifications de circulation");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("<html><pre>" +
                "=== MODIFICATIONS DE CIRCULATION ===<br>" +
                "1. Fermer une rue<br>" +
                "2. Réouvrir une rue<br>" +
                "3. Modifier le sens d'une rue<br>" +
                "4. Modifier la distance d'une rue<br>" +
                "5. Afficher l’état actuel des rues<br>" +
                "<br>ESPACE / ESC : Retour au menu Collectivité<br>" +
                "</pre></html>");

        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setHorizontalAlignment(JLabel.CENTER);
        setContentPane(label);

        label.setFocusable(true);

        label.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1 -> fermerRue();
                    case KeyEvent.VK_2 -> ouvrirRue();
                    case KeyEvent.VK_3 -> modifierSens();
                    case KeyEvent.VK_4 -> modifierDistance();
                    case KeyEvent.VK_5 -> afficherEtatRues();

                    // Retour : on recrée une Collectivite, mais avec les graphes statiques conservés
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE -> {
                        dispose();
                        new Collectivite();
                    }
                }
            }
        });

        SwingUtilities.invokeLater(label::requestFocusInWindow);
        setVisible(true);
    }

    // ----------- 1. Fermer une rue -----------
    private void fermerRue() {
        String rue = JOptionPane.showInputDialog(this,
                "Entrer la rue (ex : S273941-S273948)");

        if (rue == null) return;

        rue = rue.trim().toUpperCase();
        String[] parts = rue.split("-");

        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Format invalide.");
            return;
        }

        String s1 = parts[0].trim();
        String s2 = parts[1].trim();

        for (Graphe.Edge e : graphe.edges) {
            boolean matchDirect = e.from.equalsIgnoreCase(s1) && e.to.equalsIgnoreCase(s2);
            boolean matchInverse = e.sens.equals("TWO_WAY") &&
                    e.from.equalsIgnoreCase(s2) && e.to.equalsIgnoreCase(s1);

            if (matchDirect || matchInverse) {
                e.fermee = true;
                JOptionPane.showMessageDialog(
                        this,
                        "Rue " + s1 + " - " + s2 + " fermée."
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Rue introuvable.");
    }

    // ----------- 2. Réouvrir une rue -----------
    private void ouvrirRue() {
        String rue = JOptionPane.showInputDialog(this,
                "Entrer la rue à réouvrir");

        if (rue == null) return;

        rue = rue.trim().toUpperCase();
        String[] parts = rue.split("-");

        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Format invalide.");
            return;
        }

        String s1 = parts[0].trim();
        String s2 = parts[1].trim();

        for (Graphe.Edge e : graphe.edges) {
            boolean matchDirect = e.from.equalsIgnoreCase(s1) && e.to.equalsIgnoreCase(s2);
            boolean matchInverse = e.sens.equals("TWO_WAY") &&
                    e.from.equalsIgnoreCase(s2) && e.to.equalsIgnoreCase(s1);

            if (matchDirect || matchInverse) {
                e.fermee = true;
                JOptionPane.showMessageDialog(
                        this,
                        "Rue " + s1 + " - " + s2 + " fermée."
                );
                return;
            }

        }

        JOptionPane.showMessageDialog(this, "Rue introuvable.");
    }

    // ----------- 3. Modifier le sens -----------
    private void modifierSens() {
        String rue = JOptionPane.showInputDialog(this,
                "Entrer la rue à modifier");

        if (rue == null) return;

        rue = rue.trim().toUpperCase();
        String[] parts = rue.split("-");

        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Format invalide.");
            return;
        }

        String s1 = parts[0].trim();
        String s2 = parts[1].trim();

        for (Graphe.Edge e : graphe.edges) {
            boolean matchDirect = e.from.equalsIgnoreCase(s1) && e.to.equalsIgnoreCase(s2);

            if (matchDirect) {
                if (e.sens.equals("TWO_WAY")) e.sens = "ONE_WAY";
                else e.sens = "TWO_WAY";

                JOptionPane.showMessageDialog(this,
                        "Sens de " + s1 + "-" + s2 + " modifié en " + e.sens);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Rue introuvable.");
    }

    // ----------- 4. Modifier la distance -----------
    private void modifierDistance() {
        String rue = JOptionPane.showInputDialog(this,
                "Entrer la rue à modifier");

        if (rue == null) return;

        rue = rue.trim().toUpperCase();
        String[] parts = rue.split("-");

        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Format invalide.");
            return;
        }

        String s1 = parts[0].trim();
        String s2 = parts[1].trim();

        String newDistStr = JOptionPane.showInputDialog(this, "Nouvelle distance :");
        if (newDistStr == null) return;

        int newDist;
        try {
            newDist = Integer.parseInt(newDistStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Distance invalide.");
            return;
        }

        for (Graphe.Edge e : graphe.edges) {
            boolean matchDirect = e.from.equalsIgnoreCase(s1) && e.to.equalsIgnoreCase(s2);
            boolean matchInverse = e.sens.equals("TWO_WAY") &&
                    e.from.equalsIgnoreCase(s2) && e.to.equalsIgnoreCase(s1);

            if (matchDirect || matchInverse) {
                e.distance = newDist;
                JOptionPane.showMessageDialog(this,
                        "Distance de " + s1 + "-" + s2 + " modifiée à " + newDist + " m");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Rue introuvable.");
    }

    // ----------- 5. Afficher état des rues (avec scroll) -----------
    private void afficherEtatRues() {

        StringBuilder sb = new StringBuilder();
        sb.append("ETAT DES RUES :\n\n");

        for (Graphe.Edge e : graphe.edges) {
            sb.append(e.from)
                    .append(" --> ")
                    .append(e.to)
                    .append(" | ")
                    .append(e.distance).append(" m")
                    .append(" | Sens : ").append(e.sens)
                    .append(" | ").append(" | ").append(!e.fermee ? "OUVERTE" : "FERMEE")
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString(), 30, 60);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "État des rues",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
