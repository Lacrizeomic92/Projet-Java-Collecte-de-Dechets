import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ItineraireMultiPoints extends JFrame {

    private JTextField listeRuesField;
    private JTextArea resultatArea;

    public ItineraireMultiPoints() {

        setTitle("Tournée optimale entre plusieurs rues");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ==== FOND DESSIN ANIMÉ ====
        JPanel panneauFond = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(new Color(245, 255, 240));
                g.fillRect(0, 0, getWidth(), getHeight());

                try {
                    Image camion = new ImageIcon("camion.png").getImage();
                    g.drawImage(camion, 20, 380, 230, 150, this);
                } catch (Exception ignored) {}
            }
        };

        panneauFond.setLayout(null);
        panneauFond.setFocusable(true);
        add(panneauFond);

        // ===== TITRE =====
        JLabel titre = new JLabel("Tournée optimale entre plusieurs rues", SwingConstants.CENTER);
        titre.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 26));
        titre.setBounds(0, 20, 900, 40);
        panneauFond.add(titre);

        // ===== CADRE PRINCIPAL =====
        JPanel cadre = new JPanel();
        cadre.setLayout(null);
        cadre.setBackground(new Color(255, 255, 255, 230));
        cadre.setBounds(200, 100, 500, 400);
        cadre.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));
        panneauFond.add(cadre);

        // ===== CHAMP DE SAISIE =====
        JLabel labelListe = new JLabel("Rues à visiter (séparées par des virgules) :");
        labelListe.setFont(new Font("Arial", Font.PLAIN, 16));
        labelListe.setBounds(30, 30, 400, 25);
        cadre.add(labelListe);

        listeRuesField = new JTextField();
        listeRuesField.setBounds(30, 60, 440, 35);
        listeRuesField.setFont(new Font("Arial", Font.PLAIN, 15));
        cadre.add(listeRuesField);

        // ===== BOUTON CALCUL =====
        JButton bouton = new JButton("Calculer la tournée");
        bouton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
        bouton.setBounds(120, 115, 260, 40);
        bouton.setBackground(new Color(230, 250, 230));
        bouton.setBorder(BorderFactory.createLineBorder(new Color(150, 200, 150), 2, true));
        bouton.setFocusPainted(false);
        cadre.add(bouton);

        // ===== ZONE DE RÉSULTAT =====
        resultatArea = new JTextArea();
        resultatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultatArea.setEditable(false);
        resultatArea.setLineWrap(true);
        resultatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(resultatArea);
        scroll.setBounds(20, 180, 460, 160);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180,180,180)));
        cadre.add(scroll);

        // ===== BOUTON RETOUR =====
        JButton boutonRetour = new JButton("Retour");
        boutonRetour.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        boutonRetour.setBounds(200, 350, 100, 35);
        boutonRetour.setBackground(new Color(255, 230, 230));
        boutonRetour.setBorder(BorderFactory.createLineBorder(new Color(200,130,130), 2, true));
        boutonRetour.setFocusPainted(false);
        cadre.add(boutonRetour);

        boutonRetour.addActionListener(e -> {
            dispose();
            new Theme1();
        });

        // ===== BOUTON CALCUL ACTION =====
        bouton.addActionListener(e -> {
            try {
                String saisie = listeRuesField.getText().trim();
                if (saisie.isEmpty()) {
                    resultatArea.setText("Veuillez entrer au moins une rue.");
                    return;
                }

                java.util.List<String> rues = Arrays.asList(saisie.split(","));
                for (int i=0; i<rues.size(); i++)
                    rues.set(i, rues.get(i).trim());

                String tournee = calculerTournee(rues);
                resultatArea.setText("Tournée optimale :\n" + tournee);

            } catch (Exception ex) {
                resultatArea.setText("Erreur : " + ex.getMessage());
            }
        });

        // ===== RETOUR AVEC ESPACE =====
        panneauFond.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    new Theme1();
                }
            }
        });

        setVisible(true);
    }

    // ======================================================
    //   🔥 ALGORITHME : PLUS PROCHE VOISIN (heuristique)
    // ======================================================
    private String calculerTournee(java.util.List<String> aVisiter) throws IOException {

        // Charger le graphe
        Map<String, java.util.List<String>> adj = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("rues_nice.txt"));
        String ligne;

        while ((ligne = br.readLine()) != null) {
            String[] p = ligne.split(";");
            adj.putIfAbsent(p[0], new ArrayList<>());
            adj.putIfAbsent(p[1], new ArrayList<>());
            adj.get(p[0]).add(p[1]);
            adj.get(p[1]).add(p[0]);
        }
        br.close();

        // On part de la première rue entrée
        String courant = aVisiter.get(0);
        java.util.List<String> nonVisite = new ArrayList<>(aVisiter);
        java.util.List<String> tournee = new ArrayList<>();

        while (!nonVisite.isEmpty()) {
            String prochain = null;
            int meilleureDist = Integer.MAX_VALUE;

            for (String r : nonVisite) {
                int dist = distance(adj, courant, r);
                if (dist < meilleureDist) {
                    meilleureDist = dist;
                    prochain = r;
                }
            }

            tournee.add(prochain);
            courant = prochain;
            nonVisite.remove(prochain);
        }

        return String.join(" → ", tournee);
    }

    // ===== BFS pour calculer la distance =====
    private int distance(Map<String, java.util.List<String>> adj, String depart, String arrivee) {
        Queue<String> q = new LinkedList<>();
        Map<String, Integer> dist = new HashMap<>();

        for (String s : adj.keySet())
            dist.put(s, Integer.MAX_VALUE);

        dist.put(depart, 0);
        q.add(depart);

        while (!q.isEmpty()) {
            String c = q.poll();

            for (String v : adj.getOrDefault(c, new ArrayList<>())) {
                if (dist.get(v) == Integer.MAX_VALUE) {
                    dist.put(v, dist.get(c) + 1);
                    q.add(v);
                }
            }
        }

        return dist.get(arrivee);
    }
}
