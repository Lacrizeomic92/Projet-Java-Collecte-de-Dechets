public class PointCollecte {
    private String nom;
    private String sommetId;
    private int contenance;

    public PointCollecte(String nom, String sommetId, int contenance) {
        this.nom = nom;
        this.sommetId = sommetId;
        this.contenance = contenance;
    }

    public String getNom() { return nom; }
    public String getSommetId() { return sommetId; }
    public int getContenance() { return contenance; }
}
