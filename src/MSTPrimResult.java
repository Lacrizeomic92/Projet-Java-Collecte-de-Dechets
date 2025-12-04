import java.util.List;

public class MSTPrimResult {

    public int[] parent;           // parent[i]
    public List<String> ids;       // ids.get(i)
    public double[][] poids;       // matrice des distances

    public MSTPrimResult(int[] parent, List<String> ids, double[][] poids) {
        this.parent = parent;
        this.ids = ids;
        this.poids = poids;
    }
}
