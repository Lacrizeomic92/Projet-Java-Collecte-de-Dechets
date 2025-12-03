import java.util.*;

public class Shortcutting {

    public static List<Integer> appliquer(List<Integer> ordre) {
        LinkedHashSet<Integer> set = new LinkedHashSet<>(ordre);
        return new ArrayList<>(set);
    }
}
