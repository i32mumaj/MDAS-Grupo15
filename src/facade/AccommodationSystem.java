package facade;

import java.util.ArrayList;
import java.util.List;

public class AccommodationSystem {

    private final List<AccommodationSearchProvider> providers;

    public AccommodationSystem() {
        providers = new ArrayList<>();
        providers.add(new HotelProvider());
        providers.add(new ApartmentProvider());
    }

    public List<Accommodation> search(String city, String date) {
        List<Accommodation> results = new ArrayList<>();
        for (AccommodationSearchProvider provider : providers) {
            results.addAll(provider.search(city, date));
        }
        return results;
    }
}
