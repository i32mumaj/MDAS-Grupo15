package facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MadridTourism {

    private final Map<String, List<String>> catalog;

    public MadridTourism() {
        catalog = new HashMap<>();

        List<String> concerts = new ArrayList<>();
        concerts.add("Gran Via concert - 2026-05-10");
        concerts.add("Spring concert - 2026-05-20");
        catalog.put("Concert", concerts);

        List<String> museums = new ArrayList<>();
        museums.add("Prado guided tour - 2026-05-11");
        museums.add("Reina Sofia special night - 2026-05-12");
        museums.add("Archaeology museum open day - 2026-06-01");
        catalog.put("Museum", museums);

        List<String> theatre = new ArrayList<>();
        theatre.add("Classic theatre in Plaza Mayor - 2026-05-09");
        theatre.add("Contemporary theatre - 2026-05-12");
        catalog.put("Theatre", theatre);
    }

    public List<String> searchByEventType(String eventType) {
        return new ArrayList<>(catalog.getOrDefault(eventType, new ArrayList<>()));
    }
}
