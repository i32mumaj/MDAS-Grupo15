package facade;

import java.util.ArrayList;
import java.util.List;

class ApartmentProvider implements AccommodationSearchProvider {

    private final List<Accommodation> catalog;

    public ApartmentProvider() {
        catalog = new ArrayList<>();
        catalog.add(new Accommodation("Madrid", "2026-05-10", AccommodationType.APARTMENT, 95.00));
        catalog.add(new Accommodation("Madrid", "2026-05-11", AccommodationType.APARTMENT, 95.00));
        catalog.add(new Accommodation("Madrid", "2026-05-12", AccommodationType.APARTMENT, 95.00));
        catalog.add(new Accommodation("Rome", "2026-06-01", AccommodationType.HOSTEL, 85.00));
        catalog.add(new Accommodation("Rome", "2026-06-02", AccommodationType.HOSTEL, 85.00));
        catalog.add(new Accommodation("Rome", "2026-06-03", AccommodationType.HOSTEL, 85.00));
        catalog.add(new Accommodation("Rome", "2026-06-04", AccommodationType.HOSTEL, 85.00));
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
