package facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelFacade {

    private final TransportSystem transportSystem;
    private final AccommodationSystem accommodationSystem;
    private final MadridTourism madridTourism;
    private final RomeTourism romeTourism;

    public TravelFacade() {
        transportSystem = new TransportSystem();
        accommodationSystem = new AccommodationSystem();
        madridTourism = new MadridTourism();
        romeTourism = new RomeTourism();
    }

    public Travel searchCompleteTrip(String startDate, String endDate, String origin, String destination) {
        Transport outboundTransport = getFirst(transportSystem.search(origin, destination, startDate));
        Transport returnTransport = getFirst(transportSystem.search(destination, origin, endDate));

        List<Accommodation> accommodations = new ArrayList<>();
        for (String date : getDatesBetween(startDate, endDate)) {
            accommodations.addAll(accommodationSystem.search(destination, date));
        }

        List<String> activities = new ArrayList<>();
        if ("Madrid".equalsIgnoreCase(destination)) {
            List<String> eventTypes = Arrays.asList("Concert", "Museum", "Theatre");
            for (String eventType : eventTypes) {
                for (String activity : madridTourism.searchByEventType(eventType)) {
                    if (isActivityWithinRange(activity, startDate, endDate)) {
                        activities.add(activity);
                    }
                }
            }
        } else if ("Rome".equalsIgnoreCase(destination)) {
            for (String date : getDatesBetween(startDate, endDate)) {
                activities.addAll(romeTourism.searchByDate(date));
            }
        }

        return new Travel(outboundTransport, returnTransport, accommodations, activities);
    }

    private Transport getFirst(List<Transport> transports) {
        return transports.isEmpty() ? null : transports.get(0);
    }

    private List<String> getDatesBetween(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<String> dates = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current.toString());
            current = current.plusDays(1);
        }
        return dates;
    }

    private boolean isActivityWithinRange(String activity, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        String activityDate = activity.substring(activity.length() - 10);
        LocalDate date = LocalDate.parse(activityDate);
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
