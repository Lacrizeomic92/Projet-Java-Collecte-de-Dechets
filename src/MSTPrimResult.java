import java.util.List;

public class MSTPrimResult {

    public int[] parent;     // parent[i] = parent de i dans le MST
    public List<String> ids; // ids.get(i) = sommet correspondant à l'index i

    public MSTPrimResult(int[] parent, List<String> ids) {
        this.parent = parent;
        this.ids = ids;
    }
}
