package adapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Global variable to accumulate the names of failed tests
    private static final List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("TEST SUITE - FORMATTING SYSTEM (ADAPTER)");
        System.out.println("=======================================================================");

        // ----------------------------------------------------------------------------------
        // SETUP: Create temporary files for testing
        // ----------------------------------------------------------------------------------
        File dir = new File("src/resources/test_tmp");
        if (!dir.exists()) dir.mkdirs();

        File fileX = createTestFile("src/resources/test_tmp/fileX.txt",
                "Line 1 X\nLine 2 X\nLine 3 X\nLine 4 X\nLine 5 X");
        File fileY = createTestFile("src/resources/test_tmp/fileY.txt",
                "Line 1 Y\nLine 2 Y\nLine 3 Y\nLine 4 Y\nLine 5 Y");
        File fileEmpty = createTestFile("src/resources/test_tmp/fileEmpty.txt", "");

        AdvancedFormatter adapter = new Adapter();

        // ----------------------------------------------------------------------------------
        // TEST 1: ConcatFiles - Normal
        // ----------------------------------------------------------------------------------
        File out1 = new File("src/resources/test_tmp/out1.txt");
        try {
            adapter.concatFiles(fileX, fileY, out1.getPath());
            runTest("1. ConcatFiles (File X + File Y)",
                    "File X (5 lines) and File Y (5 lines)",
                    "Line 1 X\nLine 2 X\nLine 3 X\nLine 4 X\nLine 5 X\nLine 1 Y\nLine 2 Y\nLine 3 Y\nLine 4 Y\nLine 5 Y",
                    readFile(out1));
        } catch (Exception e) {
            failTest("1. ConcatFiles (File X + File Y)", e);
        }

        // ----------------------------------------------------------------------------------
        // TEST 2: ConcatFiles - Edge Case (With empty file)
        // ----------------------------------------------------------------------------------
        File out2 = new File("src/resources/test_tmp/out2.txt");
        try {
            adapter.concatFiles(fileX, fileEmpty, out2.getPath());
            runTest("2. ConcatFiles (Normal file + Empty file)",
                    "File X and Empty File",
                    "Line 1 X\nLine 2 X\nLine 3 X\nLine 4 X\nLine 5 X",
                    readFile(out2));
        } catch (Exception e) {
            failTest("2. ConcatFiles (Normal file + Empty file)", e);
        }

        // ----------------------------------------------------------------------------------
        // TEST 3: CombineFiles - Normal (Interleave lines)
        // ----------------------------------------------------------------------------------
        File out3 = new File("src/resources/test_tmp/out3.txt");
        try {
            adapter.combineFiles(fileX, fileY, 2, 4, out3.getPath());
            runTest("3. CombineFiles (Extract and interleave lines 2 to 4)",
                    "Lines 2 to 4 from File X, followed by lines 2 to 4 from File Y",
                    "Line 2 X\nLine 3 X\nLine 4 X\nLine 2 Y\nLine 3 Y\nLine 4 Y",
                    readFile(out3));
        } catch (Exception e) {
            failTest("3. CombineFiles (Extract and interleave lines 2 to 4)", e);
        }

        // ----------------------------------------------------------------------------------
        // TEST 4: CombineFiles - Edge Case (Lines out of bounds)
        // ----------------------------------------------------------------------------------
        File out4 = new File("src/resources/test_tmp/out4.txt");
        try {
            adapter.combineFiles(fileX, fileY, 4, 10, out4.getPath());
            runTest("4. CombineFiles (Out of bounds: lines 4 to 10)",
                    "The file only has 5 lines. Extracts until the end.",
                    "Line 4 X\nLine 5 X\nLine 4 Y\nLine 5 Y",
                    readFile(out4));
        } catch (Exception e) {
            failTest("4. CombineFiles (Out of bounds: lines 4 to 10)", e);
        }

        // ----------------------------------------------------------------------------------
        // TEST 5: SplitFile - Normal (Multiple cuts)
        // ----------------------------------------------------------------------------------
        try {
            int[] positions = {2, 4};
            List<File> splits = adapter.splitFile(fileX, positions);

            String split1Content = !splits.isEmpty() ? readFile(splits.get(0)) : "";
            runTest("5A. SplitFile (Cut 1: Lines 2 to 3)",
                    "Cut from line 2 to the line before line 4",
                    "Line 2 X\nLine 3 X",
                    split1Content);

            String split2Content = splits.size() > 1 ? readFile(splits.get(1)) : "";
            runTest("5B. SplitFile (Cut 2: Line 4 to End)",
                    "Cut from line 4 onwards",
                    "Line 4 X\nLine 5 X",
                    split2Content);
        } catch (Exception e) {
            failTest("5. SplitFile", e);
        }

        // ----------------------------------------------------------------------------------
        // TEST 6: BasicFormatter splitFileInTwo (Required base method)
        // ----------------------------------------------------------------------------------
        try {
            BasicFormatter bf = new BasicFormatter(fileX);
            File[] splitTwo = bf.splitFileInTwo(4);

            runTest("6A. BasicFormatter splitInTwo (Part 1: Before the cut)",
                    "Cut at line 4 (Lines 1 to 3)",
                    "Line 1 X\nLine 2 X\nLine 3 X",
                    readFile(splitTwo[0]));

            runTest("6B. BasicFormatter splitInTwo (Part 2: After the cut)",
                    "Cut at line 4 (Line 4 to the end)",
                    "Line 4 X\nLine 5 X",
                    readFile(splitTwo[1]));
        } catch (Exception e) {
            failTest("6. BasicFormatter splitInTwo", e);
        }

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

        // Normalize strings to avoid OS-specific \r\n issues
        String expectedNorm = expectedContent.trim().replace("\r\n", "\n");
        String actualNorm = actualContent.trim().replace("\r\n", "\n");

        String[] expLines = expectedNorm.split("\n");
        String[] actLines = actualNorm.split("\n");

        // Handle completely empty outputs specifically
        if (expectedNorm.isEmpty()) expLines = new String[0];
        if (actualNorm.isEmpty()) actLines = new String[0];

        boolean globalPass = true;
        int maxItems = Math.max(expLines.length, actLines.length);

        System.out.println("OUTPUT (Line by line comparison):");
        System.out.printf("%-30s | %-30s | %-10s%n", "EXPECTED LINE", "ACTUAL LINE", "STATUS");
        System.out.println("-------------------------------|-------------------------------|----------");

        for (int i = 0; i < maxItems; i++) {
            String expCol = (i < expLines.length) ? expLines[i] : "(MISSING)";
            String actCol = (i < actLines.length) ? actLines[i] : "(EXTRA)";

            String status = "OK";
            if (!expCol.equals(actCol)) {
                status = "FAIL";
                globalPass = false;
            }

            // Truncate long lines for neat display
            if (expCol.length() > 30) expCol = expCol.substring(0, 27) + "...";
            if (actCol.length() > 30) actCol = actCol.substring(0, 27) + "...";

            System.out.printf("%-30s | %-30s | %-10s%n", expCol, actCol, status);
        }

        System.out.println("-----------------------------------------------------------------------");
        if (globalPass) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            failedTests.add(testName);
        }
    }

    private static void failTest(String testName, Exception e) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("RESULT: FATAL ERROR (Exception thrown)");
        e.printStackTrace(System.out);
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

    private static File createTestFile(String path, String content) {
        File file = new File(path);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Setup failed: could not create test file", e);
        }
        return file;
    }

    private static String readFile(File file) {
        if (file == null || !file.exists()) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "ERROR READING FILE";
        }
        return sb.toString();
    }
}