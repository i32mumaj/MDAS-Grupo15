package facade;

import java.util.List;

interface TransportSearchProvider {
    List<Transport> search(String origin, String destination, String date);
}
