import java.util.*;

public class Shortcutting {

    public static List<Integer> appliquer(List<Integer> ordre) {
        List<Integer> resultat = new ArrayList<>();
        Set<Integer> vus = new HashSet<>();

        for (int x : ordre) {
            if (!vus.contains(x)) {
                resultat.add(x);
                vus.add(x);
            }
        }
        return resultat;
    }
}
