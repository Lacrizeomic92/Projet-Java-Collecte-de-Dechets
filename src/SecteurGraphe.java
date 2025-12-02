import java.util.ArrayList;

public class SecteurGraphe {
    public ArrayList<NoeudSecteur> noeuds = new ArrayList<>();
    public ArrayList<AreteSecteur> aretes = new ArrayList<>();

    public void ajouterNoeud(NoeudSecteur n) { noeuds.add(n); }
    public void ajouterArete(NoeudSecteur a, NoeudSecteur b) { aretes.add(new AreteSecteur(a, b)); }
}

class NoeudSecteur {
    public int id, x, y, couleur = -1;
    public String nom;
    public NoeudSecteur(int id, int x, int y, String nom) {
        this.id = id; this.x = x; this.y = y; this.nom = nom;
    }
}

class AreteSecteur {
    public NoeudSecteur a, b;
    public AreteSecteur(NoeudSecteur a, NoeudSecteur b) {
        this.a = a; this.b = b;
    }
}
