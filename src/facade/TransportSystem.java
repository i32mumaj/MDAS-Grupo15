package facade;

import java.util.ArrayList;
import java.util.List;

public class TransportSystem {

    private final List<TransportSearchProvider> providers;

    public TransportSystem() {
        providers = new ArrayList<>();
        providers.add(new AirTransportProvider());
        providers.add(new GroundTransportProvider());
    }

    public List<Transport> search(String origin, String destination, String date) {
        List<Transport> results = new ArrayList<>();
        for (TransportSearchProvider provider : providers) {
            results.addAll(provider.search(origin, destination, date));
        }
        return results;
    }
}
