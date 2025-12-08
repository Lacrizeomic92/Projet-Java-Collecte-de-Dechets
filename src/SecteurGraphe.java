import java.util.ArrayList;

public class SecteurGraphe {
    public ArrayList<NoeudSecteur> noeuds = new ArrayList<>();
    public ArrayList<AreteSecteur> aretes = new ArrayList<>();

    public void ajouterNoeud(NoeudSecteur n) { noeuds.add(n); }
    public void ajouterArete(NoeudSecteur a, NoeudSecteur b) { aretes.add(new AreteSecteur(a, b)); }

    // Nouveau: calculer le degré d'un nœud
    public int getDegre(NoeudSecteur n) {
        int degre = 0;
        for (AreteSecteur a : aretes) {
            if (a.a == n || a.b == n) degre++;
        }
        return degre;
    }
}

class NoeudSecteur {
    public int id, x, y, couleur = -1;
    public String nom;
    public int quantiteDechets;  // qs

    public NoeudSecteur(int id, int x, int y, String nom) {
        this(id, x, y, nom, 3 + (int)(Math.random() * 8)); // 3-10 tonnes
    }

    public NoeudSecteur(int id, int x, int y, String nom, int quantite) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.nom = nom;
        this.quantiteDechets = quantite;
    }

    @Override
    public String toString() {
        return nom + "(" + quantiteDechets + "t)";
    }
}

class AreteSecteur {
    public NoeudSecteur a, b;
    public AreteSecteur(NoeudSecteur a, NoeudSecteur b) {
        this.a = a;
        this.b = b;
    }
}
