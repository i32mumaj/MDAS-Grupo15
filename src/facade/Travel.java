package facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Travel {

    private final Transport outboundTransport;
    private final Transport returnTransport;
    private final List<Accommodation> accommodations;
    private final List<String> activities;

    public Travel(Transport outboundTransport, Transport returnTransport, List<Accommodation> accommodations, List<String> activities) {
        this.outboundTransport = outboundTransport;
        this.returnTransport = returnTransport;
        this.accommodations = new ArrayList<>(accommodations);
        this.activities = new ArrayList<>(activities);
    }

    public Transport getOutboundTransport() {
        return outboundTransport;
    }

    public Transport getReturnTransport() {
        return returnTransport;
    }

    public List<Accommodation> getAccommodations() {
        return Collections.unmodifiableList(accommodations);
    }

    public List<String> getActivities() {
        return Collections.unmodifiableList(activities);
    }
}
