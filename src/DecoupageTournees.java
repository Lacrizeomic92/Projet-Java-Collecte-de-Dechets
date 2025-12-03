import java.util.*;

public class DecoupageTournees {

    public static List<List<PointCollecte>> decouper(List<PointCollecte> ordre, int capacite) {
        List<List<PointCollecte>> tours = new ArrayList<>();
        int charge = 0;
        List<PointCollecte> current = new ArrayList<>();

        for (PointCollecte pc : ordre) {
            if (charge + pc.getContenance() > capacite) {
                tours.add(current);
                current = new ArrayList<>();
                charge = 0;
            }
            current.add(pc);
            charge += pc.getContenance();
        }

        if (!current.isEmpty())
            tours.add(current);

        return tours;
    }
}
