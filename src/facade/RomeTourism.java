package facade;

import java.util.ArrayList;
import java.util.List;

public class RomeTourism {

    private final List<String> catalog;

    public RomeTourism() {
        catalog = new ArrayList<>();
        catalog.add("Rome - Colosseum visit - 2026-06-01");
        catalog.add("Rome - Baroque route - 2026-06-02");
        catalog.add("Rome - Vatican Museums - 2026-06-04");
        catalog.add("Rome - Summer festival - 2026-07-01");
    }

    public List<String> searchByDate(String date) {
        List<String> results = new ArrayList<>();
        for (String activity : catalog) {
            if (activity.contains(date)) {
                results.add(activity);
            }
        }
        return results;
    }
}
