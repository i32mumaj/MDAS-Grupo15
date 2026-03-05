package composite;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Global variable to accumulate the names of failed tests
    private static final List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("TEST SUITE - ENERGY MANAGEMENT SYSTEM (COMPOSITE PATTERN)");
        System.out.println("=======================================================================");

        // ----------------------------------------------------------------------------------
        // TEST 1: Single Device (Leaf) - Normal Calculation
        // ----------------------------------------------------------------------------------
        try {
            Device projector = new Device(4.0, 150.0, "Classroom Projector");
            runTest("1. Single Device Calculation",
                    "Device: 4 hours * 150 kWh",
                    "600.0",
                    String.valueOf(projector.calculateEnergyCosts()));
        } catch (Exception e) { failTest("1. Single Device Calculation", e); }

        // ----------------------------------------------------------------------------------
        // TEST 2: Single Device (Leaf) - Edge Case (Zero Usage)
        // ----------------------------------------------------------------------------------
        try {
            Device brokenLamp = new Device(0.0, 50.0, "Broken Lamp");
            runTest("2. Device with Zero Usage",
                    "Device: 0 hours * 50 kWh",
                    "0.0",
                    String.valueOf(brokenLamp.calculateEnergyCosts()));
        } catch (Exception e) { failTest("2. Device with Zero Usage", e); }

        // ----------------------------------------------------------------------------------
        // TEST 3: Empty Area (Composite) - Edge Case
        // ----------------------------------------------------------------------------------
        try {
            Area emptyRoom = new Area("Room 404");
            runTest("3. Empty Area Calculation",
                    "Area with 0 elements inside",
                    "0.0",
                    String.valueOf(emptyRoom.calculateEnergyCosts()));
        } catch (Exception e) { failTest("3. Empty Area Calculation", e); }

        // ----------------------------------------------------------------------------------
        // TEST 4: Flat Area (Composite with multiple Leaves)
        // ----------------------------------------------------------------------------------
        Area room101 = new Area("Room 101");
        try {
            room101.addElement(new Device(5.0, 300.0, "Student PC 1")); // 1500
            room101.addElement(new Device(5.0, 300.0, "Student PC 2")); // 1500
            runTest("4. Flat Area Calculation",
                    "Room containing 2 PCs (1500 each)",
                    "3000.0",
                    String.valueOf(room101.calculateEnergyCosts()));
        } catch (Exception e) { failTest("4. Flat Area Calculation", e); }

        // ----------------------------------------------------------------------------------
        // TEST 5: Mixed Area (Composite with Leaves and other Composites)
        // ----------------------------------------------------------------------------------
        Area itBuilding = new Area("IT Building");
        try {
            itBuilding.addElement(room101); // Adds 3000
            itBuilding.addElement(new Device(24.0, 200.0, "Vending Machine")); // Adds 4800
            runTest("5. Mixed Area Calculation",
                    "Building containing Room 101 (3000) and Vending Machine (4800)",
                    "7800.0",
                    String.valueOf(itBuilding.calculateEnergyCosts()));
        } catch (Exception e) { failTest("5. Mixed Area Calculation", e); }

        // ----------------------------------------------------------------------------------
        // TEST 6: Deeply Nested Composite (Campus -> Building -> Room -> Device)
        // ----------------------------------------------------------------------------------
        Area mainCampus = new Area("Main Campus");
        try {
            mainCampus.addElement(itBuilding); // Adds 7800
            mainCampus.addElement(new Device(12.0, 100.0, "Street Light")); // Adds 1200
            runTest("6. Deeply Nested Area",
                    "Campus -> IT Building (7800) + Street Light (1200)",
                    "9000.0",
                    String.valueOf(mainCampus.calculateEnergyCosts()));
        } catch (Exception e) { failTest("6. Deeply Nested Area", e); }

        // ----------------------------------------------------------------------------------
        // TEST 7: Dynamic Modification (Add and Remove Elements)
        // ----------------------------------------------------------------------------------
        try {
            Area lab = new Area("Laboratory");
            Device server = new Device(24.0, 500.0, "Main Server"); // 12000
            Device fan = new Device(10.0, 50.0, "Temporary Fan"); // 500

            lab.addElement(server);
            lab.addElement(fan);
            double costWithFan = lab.calculateEnergyCosts(); // 12500

            lab.removeElement(fan);
            double costWithoutFan = lab.calculateEnergyCosts(); // 12000

            runTest("7. Dynamic Add/Remove",
                    "Calculate cost, remove a device, calculate again",
                    "12500.0 -> 12000.0",
                    costWithFan + " -> " + costWithoutFan);
        } catch (Exception e) { failTest("7. Dynamic Add/Remove", e); }

        // ----------------------------------------------------------------------------------
        // TEST 8: Name Retrieval (Polymorphism)
        // ----------------------------------------------------------------------------------
        try {
            Element someArea = new Area("Secret Bunker");
            Element someDevice = new Device(1.0, 1.0, "Secret Server");
            runTest("8. Name Retrieval",
                    "Call getName() on both types acting as Elements",
                    "Secret Bunker | Secret Server",
                    someArea.getName() + " | " + someDevice.getName());
        } catch (Exception e) { failTest("8. Name Retrieval", e); }

        // ----------------------------------------------------------------------------------
        // TEST 9: Removing an element that doesn't exist
        // ----------------------------------------------------------------------------------
        try {
            Area office = new Area("Office");
            Device printer = new Device(2.0, 100.0, "Printer");
            boolean removed = office.removeElement(printer); // Should be false
            runTest("9. Remove Non-Existent Element",
                    "Try to remove a device that wasn't added",
                    "Removed: false | Cost: 0.0",
                    "Removed: " + removed + " | Cost: " + office.calculateEnergyCosts());
        } catch (Exception e) { failTest("9. Remove Non-Existent Element", e); }

        // ----------------------------------------------------------------------------------
        // TEST 10: Modifying a Leaf AFTER it has been added (Reference Check)
        // ----------------------------------------------------------------------------------
        try {
            Area serverRoom = new Area("Server Room");
            Device mainServer = new Device(10.0, 50.0, "Server"); // Initial: 500
            serverRoom.addElement(mainServer);

            // Change state after addition
            mainServer.setHourCounter(20.0); // New cost: 1000

            runTest("10. State Change After Addition",
                    "Change device hours AFTER adding it to Area",
                    "1000.0",
                    String.valueOf(serverRoom.calculateEnergyCosts()));
        } catch (Exception e) { failTest("10. State Change After Addition", e); }

        // ----------------------------------------------------------------------------------
        // TEST 11: Deep nesting of empty Composites (Matryoshka effect)
        // ----------------------------------------------------------------------------------
        try {
            Area outer = new Area("Outer");
            Area middle = new Area("Middle");
            Area inner = new Area("Inner");
            outer.addElement(middle);
            middle.addElement(inner);

            runTest("11. Deep Nesting Empty Areas",
                    "Area -> Area -> Area -> (Nothing)",
                    "0.0",
                    String.valueOf(outer.calculateEnergyCosts()));
        } catch (Exception e) { failTest("11. Deep Nesting Empty Areas", e); }

        // ----------------------------------------------------------------------------------
        // TEST 12: Floating Point Precision
        // ----------------------------------------------------------------------------------
        try {
            Area precisionLab = new Area("Precision Lab");
            precisionLab.addElement(new Device(0.333, 10.0, "Sensor 1")); // 3.33
            precisionLab.addElement(new Device(0.333, 10.0, "Sensor 2")); // 3.33
            precisionLab.addElement(new Device(0.334, 10.0, "Sensor 3")); // 3.34

            runTest("12. Floating Point Precision",
                    "Sum values with decimals ensuring no precision loss",
                    "10.0",
                    String.valueOf(precisionLab.calculateEnergyCosts()));
        } catch (Exception e) { failTest("12. Floating Point Precision", e); }

        // ----------------------------------------------------------------------------------
        // TEST 13: Stress Test (Large Volume of Elements)
        // ----------------------------------------------------------------------------------
        try {
            Area massiveWarehouse = new Area("Warehouse");
            double totalExpected = 0.0;
            for (int i = 0; i < 10000; i++) {
                massiveWarehouse.addElement(new Device(10.0, 5.0, "Bulb " + i)); // 50.0 each
                totalExpected += 50.0;
            }
            runTest("13. Stress Test (10,000 Elements)",
                    "Area containing 10,000 devices",
                    String.valueOf(totalExpected),
                    String.valueOf(massiveWarehouse.calculateEnergyCosts()));
        } catch (Exception e) { failTest("13. Stress Test", e); }

        // ----------------------------------------------------------------------------------
        // TEST 14: Null Element Handling (Expecting Exception)
        // ----------------------------------------------------------------------------------
        try {
            Area riskyArea = new Area("Risky Area");
            riskyArea.addElement(null);

            boolean exceptionCaught = false;
            try {
                riskyArea.calculateEnergyCosts();
            } catch (NullPointerException ex) {
                exceptionCaught = true;
            }
            runTest("14. Null Element Handling",
                    "Add null and calculate (should throw NullPointerException)",
                    "Caught NPE: true",
                    "Caught NPE: " + exceptionCaught);
        } catch (Exception e) { failTest("14. Null Element Handling", e); }

        // ----------------------------------------------------------------------------------
        // TEST 15: Cyclic Dependency (Infinite Loop / StackOverflow Trap)
        // ----------------------------------------------------------------------------------
        try {
            Area paradoxRoom = new Area("Paradox Room");
            paradoxRoom.addElement(paradoxRoom); // Adding the area to ITSELF!

            boolean stackOverflowCaught = false;
            try {
                paradoxRoom.calculateEnergyCosts(); // This will loop infinitely
            } catch (StackOverflowError err) {
                stackOverflowCaught = true;
            }
            runTest("15. Cyclic Dependency (Paradox)",
                    "Add an area to itself. Should trigger StackOverflowError.",
                    "Caught StackOverflow: true",
                    "Caught StackOverflow: " + stackOverflowCaught);
        } catch (Exception e) { failTest("15. Cyclic Dependency", e); }

        // =================================================================================
        // FINAL SUMMARY
        // =================================================================================
        printFinalSummary();
    }

    // =================================================================================
    // UTILS
    // =================================================================================

    private static void runTest(String testName, String inputDesc, String expectedContent, String actualContent) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("INPUT: " + inputDesc);
        System.out.println(" ");

        boolean passed = expectedContent.equals(actualContent);
        String status = passed ? "OK" : "FAIL";

        System.out.printf("%-35s | %-35s | %-10s%n", "EXPECTED", "ACTUAL", "STATUS");
        System.out.println("------------------------------------|------------------------------------|----------");

        String expCol = expectedContent.length() > 33 ? expectedContent.substring(0, 30) + "..." : expectedContent;
        String actCol = actualContent.length() > 33 ? actualContent.substring(0, 30) + "..." : actualContent;

        System.out.printf("%-35s | %-35s | %-10s%n", expCol, actCol, status);

        System.out.println("-----------------------------------------------------------------------");
        if (passed) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            failedTests.add(testName);
        }
    }

    private static void failTest(String testName, Throwable t) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("RESULT: FATAL ERROR (Exception thrown globally)");
        t.printStackTrace(System.out);
        failedTests.add(testName + " (Exception)");
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
}