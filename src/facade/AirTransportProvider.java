package facade;

import java.util.ArrayList;
import java.util.List;

class AirTransportProvider implements TransportSearchProvider {

    private final List<Transport> catalog;

    public AirTransportProvider() {
        catalog = new ArrayList<>();
        catalog.add(new Transport("Sevilla", "Madrid", "2026-05-10", TransportType.PLANE, 89.90));
        catalog.add(new Transport("Madrid", "Sevilla", "2026-05-13", TransportType.PLANE, 79.90));
        catalog.add(new Transport("Sevilla", "Rome", "2026-06-01", TransportType.PLANE, 145.00));
        catalog.add(new Transport("Rome", "Sevilla", "2026-06-05", TransportType.PLANE, 152.00));
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
