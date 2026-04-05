package facade;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("TEST SUITE - TRAVEL SEARCH SYSTEM (FACADE PATTERN)");
        System.out.println("=======================================================================");

        TravelFacade facade = new TravelFacade();

        runTest("1. Successful trip to Madrid",
                "Sevilla -> Madrid | 2026-05-10 to 2026-05-13",
                new ExpectedTrip(
                        "Sevilla -> Madrid | 2026-05-10 | PLANE",
                        "Madrid -> Sevilla | 2026-05-13 | PLANE",
                        6,
                        4,
                        "2026-05-10",
                        "2026-05-12"),
                facade.searchCompleteTrip("2026-05-10", "2026-05-13", "Sevilla", "Madrid"));

        runTest("2. Successful trip to Rome",
                "Sevilla -> Rome | 2026-06-01 to 2026-06-05",
                new ExpectedTrip(
                        "Sevilla -> Rome | 2026-06-01 | PLANE",
                        "Rome -> Sevilla | 2026-06-05 | PLANE",
                        8,
                        3,
                        "2026-06-01",
                        "2026-06-04"),
                facade.searchCompleteTrip("2026-06-01", "2026-06-05", "Sevilla", "Rome"));

        runTest("3. Trip to destination without data",
                "Sevilla -> Berlin | 2026-05-10 to 2026-05-13",
                new ExpectedTrip(
                        "(NONE)",
                        "(NONE)",
                        0,
                        0,
                        "(NONE)",
                        "(NONE)"),
                facade.searchCompleteTrip("2026-05-10", "2026-05-13", "Sevilla", "Berlin"));

        runMadridFilterTest("4. Internal MadridTourism filtering",
                facade.searchCompleteTrip("2026-05-10", "2026-05-12", "Sevilla", "Madrid"));

        printFinalSummary();
    }

    private static void runTest(String testName, String inputDesc, ExpectedTrip expected, Travel actualTrip) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("INPUT: " + inputDesc);
        System.out.println(" ");

        String actualOutbound = summarizeTransport(actualTrip.getOutboundTransport());
        String actualReturn = summarizeTransport(actualTrip.getReturnTransport());
        int actualAccommodations = actualTrip.getAccommodations().size();
        int actualActivities = actualTrip.getActivities().size();
        String firstActivity = actualTrip.getActivities().isEmpty() ? "(NONE)" : extractDate(actualTrip.getActivities().get(0));
        String lastActivity = actualTrip.getActivities().isEmpty() ? "(NONE)"
                : extractDate(actualTrip.getActivities().get(actualTrip.getActivities().size() - 1));

        boolean passed = true;

        System.out.printf("%-24s | %-26s | %-26s | %-10s%n",
                "FIELD", "EXPECTED", "ACTUAL", "STATUS");
        System.out.println("-------------------------|----------------------------|----------------------------|----------");

        passed &= printRow("Outbound", expected.outboundTransport, actualOutbound);
        passed &= printRow("Return", expected.returnTransport, actualReturn);
        passed &= printRow("Accommodations", String.valueOf(expected.totalAccommodations), String.valueOf(actualAccommodations));
        passed &= printRow("Activities", String.valueOf(expected.totalActivities), String.valueOf(actualActivities));
        passed &= printRow("First activity", expected.firstActivityDate, firstActivity);
        passed &= printRow("Last activity", expected.lastActivityDate, lastActivity);

        System.out.println("-----------------------------------------------------------------------");
        if (passed) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            failedTests.add(testName);
        }
    }

    private static void runMadridFilterTest(String testName, Travel actualTrip) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("INPUT: Madrid | Official API searches by event type, not by date");
        System.out.println(" ");

        boolean hasOutOfRangeActivities = false;
        for (String activity : actualTrip.getActivities()) {
            String date = extractDate(activity);
            if (date.compareTo("2026-05-10") < 0 || date.compareTo("2026-05-12") > 0) {
                hasOutOfRangeActivities = true;
                break;
            }
        }

        boolean containsFilteredConcert = containsActivity(actualTrip, "2026-05-20");
        boolean containsFilteredMuseum = containsActivity(actualTrip, "2026-06-01");
        boolean passed = !hasOutOfRangeActivities && !containsFilteredConcert && !containsFilteredMuseum;

        System.out.printf("%-24s | %-26s | %-26s | %-10s%n",
                "CHECK", "EXPECTED", "ACTUAL", "STATUS");
        System.out.println("-------------------------|----------------------------|----------------------------|----------");

        printRow("Out-of-range dates", "false", String.valueOf(hasOutOfRangeActivities));
        printRow("Concert 2026-05-20", "false", String.valueOf(containsFilteredConcert));
        printRow("Museum 2026-06-01", "false", String.valueOf(containsFilteredMuseum));

        System.out.println("-----------------------------------------------------------------------");
        if (passed) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            failedTests.add(testName);
        }
    }

    private static boolean containsActivity(Travel travel, String date) {
        for (String activity : travel.getActivities()) {
            if (activity.contains(date)) {
                return true;
            }
        }
        return false;
    }

    private static boolean printRow(String field, String expected, String actual) {
        boolean ok = expected.equals(actual);
        System.out.printf("%-24s | %-26s | %-26s | %-10s%n",
                truncate(field, 24), truncate(expected, 26), truncate(actual, 26), ok ? "OK" : "FAIL");
        return ok;
    }

    private static String summarizeTransport(Transport transport) {
        if (transport == null) {
            return "(NONE)";
        }
        return transport.getOrigin() + " -> " + transport.getDestination() + " | " + transport.getDate() + " | " + transport.getType();
    }

    private static String extractDate(String activity) {
        return activity.substring(activity.length() - 10);
    }

    private static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    private static void printFinalSummary() {
        System.out.println("\n");
        System.out.println("=======================================================================");
        System.out.println("FINAL SUMMARY");
        System.out.println("=======================================================================");

        if (failedTests.isEmpty()) {
            System.out.println("ALL TESTS PASSED SUCCESSFULLY.");
        } else {
            System.out.println("ERRORS DETECTED. THE FOLLOWING TESTS FAILED:");
            for (String testName : failedTests) {
                System.out.println(" [X] " + testName);
            }
        }
        System.out.println("=======================================================================");
    }

    static class ExpectedTrip {
        String outboundTransport;
        String returnTransport;
        int totalAccommodations;
        int totalActivities;
        String firstActivityDate;
        String lastActivityDate;

        public ExpectedTrip(String outboundTransport, String returnTransport, int totalAccommodations, int totalActivities,
                            String firstActivityDate, String lastActivityDate) {
            this.outboundTransport = outboundTransport;
            this.returnTransport = returnTransport;
            this.totalAccommodations = totalAccommodations;
            this.totalActivities = totalActivities;
            this.firstActivityDate = firstActivityDate;
            this.lastActivityDate = lastActivityDate;
        }
    }
}
