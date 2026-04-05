package facade;

import java.util.ArrayList;
import java.util.List;

class HotelProvider implements AccommodationSearchProvider {

    private final List<Accommodation> catalog;

    public HotelProvider() {
        catalog = new ArrayList<>();
        catalog.add(new Accommodation("Madrid", "2026-05-10", AccommodationType.HOTEL, 120.00));
        catalog.add(new Accommodation("Madrid", "2026-05-11", AccommodationType.HOTEL, 120.00));
        catalog.add(new Accommodation("Madrid", "2026-05-12", AccommodationType.HOTEL, 120.00));
        catalog.add(new Accommodation("Rome", "2026-06-01", AccommodationType.HOTEL, 150.00));
        catalog.add(new Accommodation("Rome", "2026-06-02", AccommodationType.HOTEL, 150.00));
        catalog.add(new Accommodation("Rome", "2026-06-03", AccommodationType.HOTEL, 150.00));
        catalog.add(new Accommodation("Rome", "2026-06-04", AccommodationType.HOTEL, 150.00));
    }

    @Override
    public List<Accommodation> search(String city, String date) {
        List<Accommodation> results = new ArrayList<>();
        for (Accommodation accommodation : catalog) {
            if (accommodation.getCity().equalsIgnoreCase(city)
                    && accommodation.getDate().equals(date)) {
                results.add(accommodation);
            }
        }
        return results;
    }
}
