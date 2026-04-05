package facade;

import java.util.List;

interface AccommodationSearchProvider {
    List<Accommodation> search(String city, String date);
}
