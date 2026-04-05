package facade;

import java.util.ArrayList;
import java.util.List;

class GroundTransportProvider implements TransportSearchProvider {

    private final List<Transport> catalog;

    public GroundTransportProvider() {
        catalog = new ArrayList<>();
        catalog.add(new Transport("Sevilla", "Madrid", "2026-05-10", TransportType.TRAIN, 59.50));
        catalog.add(new Transport("Madrid", "Sevilla", "2026-05-13", TransportType.TRAIN, 61.50));
        catalog.add(new Transport("Valencia", "Madrid", "2026-05-10", TransportType.BUS, 38.00));
        catalog.add(new Transport("Madrid", "Valencia", "2026-05-13", TransportType.BUS, 38.00));
        catalog.add(new Transport("Barcelona", "Rome", "2026-06-01", TransportType.BUS, 110.00));
    }

    @Override
    public List<Transport> search(String origin, String destination, String date) {
        List<Transport> results = new ArrayList<>();
        for (Transport transport : catalog) {
            if (transport.getOrigin().equalsIgnoreCase(origin)
                    && transport.getDestination().equalsIgnoreCase(destination)
                    && transport.getDate().equals(date)) {
                results.add(transport);
            }
        }
        return results;
    }
}
