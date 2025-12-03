import java.util.*;

public class ParcoursPrefixe {

    public static List<Integer> dfs(int[] parent) {
        int n = parent.length;
        List<Integer>[] adj = new List[n];

        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();

        for (int i = 1; i < n; i++)
            adj[parent[i]].add(i);

        List<Integer> ordre = new ArrayList<>();
        dfsRec(0, adj, ordre);
        return ordre;
    }

    private static void dfsRec(int u, List<Integer>[] adj, List<Integer> out) {
        out.add(u);
        for (int v : adj[u]) dfsRec(v, adj, out);
    }
}
