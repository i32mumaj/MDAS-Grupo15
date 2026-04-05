package bridge;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("TEST SUITE - FURNITURE CATALOG (BRIDGE PATTERN)");
        System.out.println("=======================================================================");

        runSortedByPriceTest();
        runMaterialFilterTest();
        runGroupedStockTest();
        runMinimumDimensionTest();
        runMinimumSeatsTest();
        runZeroStockFilterTest();

        printFinalSummary();
    }

    private static void runSortedByPriceTest() {
        TableCatalog catalog = new TableCatalog();
        catalog.addProvider(new CompanyB());
        catalog.addProvider(new CompanyC());

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Mesa Oficina Cristal", 150.0, 10, "Cristal", "TABLE", 1.5, 0));
        expectedProducts.add(new ProductExpected("Mesa Comedor", 295.0, 3, "Madera", "TABLE", 2.0, 0));
        expectedProducts.add(new ProductExpected("Mesa Comedor", 300.0, 4, "Madera", "TABLE", 2.0, 0));

        runTest("1. Tables sorted by ascending price",
                "Providers: CompanyB + CompanyC | Search: searchSortedByPriceAsc()",
                expectedProducts,
                catalog.searchSortedByPriceAsc());
    }

    private static void runMaterialFilterTest() {
        SofaCatalog catalog = new SofaCatalog();
        catalog.addProvider(new CompanyA());
        catalog.addProvider(new CompanyC());

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Sof Chaise Longue", 450.0, 5, "Tela", "SOFA", 0, 3));
        expectedProducts.add(new ProductExpected("Sof Chaise Longue", 460.0, 8, "Tela", "SOFA", 0, 3));

        runTest("2. Sofas filtered by material",
                "Providers: CompanyA + CompanyC | Search: searchByMaterial(\"tela\")",
                expectedProducts,
                catalog.searchByMaterial("tela"));
    }

    private static void runGroupedStockTest() {
        SofaCatalog catalog = new SofaCatalog();
        catalog.addProvider(new CompanyA());
        catalog.addProvider(new CompanyC());

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Sof Chaise Longue", 450.0, 13, "Tela", "SOFA", 0, 3));
        expectedProducts.add(new ProductExpected("Sof Piel Premium", 900.0, 2, "Piel", "SOFA", 0, 2));

        runTest("3. Sofas grouped by descending stock",
                "Providers: CompanyA + CompanyC | Search: searchSortedByStockDesc()",
                expectedProducts,
                catalog.searchSortedByStockDesc());
    }

    private static void runMinimumDimensionTest() {
        TableCatalog catalog = new TableCatalog();
        catalog.addProvider(new CompanyA());
        catalog.addProvider(new CompanyB());
        catalog.addProvider(new CompanyC());

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Mesa Comedor", 300.0, 4, "Madera", "TABLE", 2.0, 0));
        expectedProducts.add(new ProductExpected("Mesa Comedor", 295.0, 3, "Madera", "TABLE", 2.0, 0));

        runTest("4. Tables filtered by minimum dimension",
                "Providers: CompanyA + CompanyB + CompanyC | Search: searchByMinimumDimension(1.8)",
                expectedProducts,
                new ArrayList<Product>(catalog.searchByMinimumDimension(1.8)));
    }

    private static void runMinimumSeatsTest() {
        SofaCatalog catalog = new SofaCatalog();
        catalog.addProvider(new CompanyA());
        catalog.addProvider(new CompanyC());

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Sof Chaise Longue", 450.0, 5, "Tela", "SOFA", 0, 3));
        expectedProducts.add(new ProductExpected("Sof Chaise Longue", 460.0, 8, "Tela", "SOFA", 0, 3));

        runTest("5. Sofas filtered by minimum seats",
                "Providers: CompanyA + CompanyC | Search: searchByMinimumSeats(3)",
                expectedProducts,
                new ArrayList<Product>(catalog.searchByMinimumSeats(3)));
    }

    private static void runZeroStockFilterTest() {
        TableCatalog catalog = new TableCatalog();
        catalog.addProvider(new CompanyB());
        catalog.addProvider(new Provider() {
            @Override
            public List<Product> getInventory() {
                List<Product> products = new ArrayList<>();
                products.add(new Table("Mesa Auxiliar", 80.0, 0, "Metal", 1.0));
                return products;
            }
        });

        List<ProductExpected> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductExpected("Mesa Oficina Cristal", 150.0, 10, "Cristal", "TABLE", 1.5, 0));
        expectedProducts.add(new ProductExpected("Mesa Comedor", 300.0, 4, "Madera", "TABLE", 2.0, 0));

        runTest("6. Zero-stock products are excluded",
                "Providers: CompanyB + TestProvider | Search: searchSortedByPriceAsc()",
                expectedProducts,
                catalog.searchSortedByPriceAsc());
    }

    private static void runTest(String testName, String inputDesc, List<ProductExpected> expectedProducts, List<Product> actualProducts) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("INPUT: " + inputDesc);
        System.out.println(" ");

        boolean passed = true;
        System.out.printf("%-22s | %-12s | %-12s | %-15s | %-10s%n",
                "PRODUCT", "PRICE", "STOCK", "DETAIL", "STATUS");
        System.out.println("-----------------------|--------------|--------------|-----------------|----------");

        int maxItems = Math.max(expectedProducts.size(), actualProducts.size());
        for (int i = 0; i < maxItems; i++) {
            String productName;
            String price = "";
            String stock = "";
            String detail = "";
            String status = "OK";

            if (i < expectedProducts.size() && i < actualProducts.size()) {
                ProductExpected expected = expectedProducts.get(i);
                Product actual = actualProducts.get(i);

                productName = truncate(normalize(actual.getName()), 22);
                price = String.format("%.2f / %.2f", expected.price, actual.getPrice());
                stock = expected.stock + " / " + actual.getStock();
                detail = buildExpectedDetail(expected) + " / " + buildActualDetail(actual);

                boolean nameOk = expected.name.equals(normalize(actual.getName()));
                boolean priceOk = Math.abs(expected.price - actual.getPrice()) < 0.01;
                boolean stockOk = expected.stock == actual.getStock();
                boolean materialOk = expected.material.equalsIgnoreCase(normalize(actual.getMaterial()));
                boolean typeOk = expected.type.equals(resolveType(actual));
                boolean specificOk = matchesSpecificDetail(expected, actual);

                if (!nameOk || !priceOk || !stockOk || !materialOk || !typeOk || !specificOk) {
                    status = "FAIL";
                    passed = false;
                }
            } else if (i < expectedProducts.size()) {
                ProductExpected expected = expectedProducts.get(i);
                productName = truncate(expected.name + " (MISSING)", 22);
                status = "FAIL";
                passed = false;
            } else {
                Product actual = actualProducts.get(i);
                productName = truncate(normalize(actual.getName()) + " (EXTRA)", 22);
                status = "FAIL";
                passed = false;
            }

            System.out.printf("%-22s | %-12s | %-12s | %-15s | %-10s%n",
                    productName, truncate(price, 12), truncate(stock, 12), truncate(detail, 15), status);
        }

        System.out.println("-----------------------------------------------------------------------");
        if (passed) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            failedTests.add(testName);
        }
    }

    private static String buildExpectedDetail(ProductExpected expected) {
        if ("TABLE".equals(expected.type)) {
            return expected.material + " | " + expected.dimension + "m";
        }
        return expected.material + " | " + expected.seats + " seats";
    }

    private static String buildActualDetail(Product product) {
        if (product instanceof Table) {
            Table table = (Table) product;
            return normalize(product.getMaterial()) + " | " + table.getDimensions() + "m";
        }

        Sofa sofa = (Sofa) product;
        return normalize(product.getMaterial()) + " | " + sofa.getSeats() + " seats";
    }

    private static boolean matchesSpecificDetail(ProductExpected expected, Product actual) {
        if (actual instanceof Table && "TABLE".equals(expected.type)) {
            return Math.abs(expected.dimension - ((Table) actual).getDimensions()) < 0.01;
        }
        if (actual instanceof Sofa && "SOFA".equals(expected.type)) {
            return expected.seats == ((Sofa) actual).getSeats();
        }
        return false;
    }

    private static String resolveType(Product product) {
        if (product instanceof Table) {
            return "TABLE";
        }
        if (product instanceof Sofa) {
            return "SOFA";
        }
        return "UNKNOWN";
    }

    private static String normalize(String value) {
        StringBuilder normalized = new StringBuilder();
        for (char current : value.toCharArray()) {
            if (current <= 127) {
                normalized.append(current);
            }
        }
        return normalized.toString().replaceAll("\\s+", " ").trim();
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

    static class ProductExpected {
        String name;
        double price;
        int stock;
        String material;
        String type;
        double dimension;
        int seats;

        ProductExpected(String name, double price, int stock, String material, String type, double dimension, int seats) {
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.material = material;
            this.type = type;
            this.dimension = dimension;
            this.seats = seats;
        }
    }
}
