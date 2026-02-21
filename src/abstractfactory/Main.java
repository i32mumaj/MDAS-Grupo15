package abstractfactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Variable global para acumular los nombres de los tests fallidos
    private static final List<String> failedTests = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("TEST SUITE - SISTEMA DE PEDIDOS");
        System.out.println("=======================================================================");

        AbstractFactory restaurantFactory = new RestaurantFactory();
        AbstractFactory takeAwayFactory = new TakeAwayFactory();

        // ----------------------------------------------------------------------------------
        // TEST 1: Menú Semanal - Restaurante
        // ----------------------------------------------------------------------------------
        List<Dish> input1 = new ArrayList<>();
        input1.add(new Dish("Sopa", DishType.STARTER));
        input1.add(new Dish("Carne", DishType.MAIN_COURSE));
        input1.add(new Dish("Flan", DishType.DESSERT));

        List<DishExpected> expected1 = new ArrayList<>();
        expected1.add(new DishExpected("Sopa", 10.00, Side.NONE));
        expected1.add(new DishExpected("Carne", 15.00, Side.POTATOES));
        expected1.add(new DishExpected("Flan", 5.00, Side.NONE));

        runTest("1. Restaurante Semanal (Standard)",
                input1, Side.POTATOES,
                restaurantFactory.createWeeklyMenu(input1, Side.POTATOES),
                expected1);


        // ----------------------------------------------------------------------------------
        // TEST 2: Menú Semanal - Take Away
        // ----------------------------------------------------------------------------------
        List<Dish> input2 = new ArrayList<>();
        input2.add(new Dish("Sopa", DishType.STARTER));
        input2.add(new Dish("Carne", DishType.MAIN_COURSE));
        input2.add(new Dish("Flan", DishType.DESSERT));

        List<DishExpected> expected2 = new ArrayList<>();
        expected2.add(new DishExpected("Sopa", 10.20, Side.NONE));
        expected2.add(new DishExpected("Carne", 15.30, Side.SALAD));

        runTest("2. Take Away Semanal (Filtro Postre + Recargo)",
                input2, Side.SALAD,
                takeAwayFactory.createWeeklyMenu(input2, Side.SALAD),
                expected2);


        // ----------------------------------------------------------------------------------
        // TEST 3: Temporada - Restaurante
        // ----------------------------------------------------------------------------------
        Dish input3 = new Dish("Setas", DishType.MAIN_COURSE);
        List<Dish> inputList3 = new ArrayList<>();
        inputList3.add(input3);

        List<DishExpected> expected3 = new ArrayList<>();
        expected3.add(new DishExpected("Setas", 15.00, Side.POTATOES));

        runTest("3. Restaurante Temporada",
                inputList3, Side.POTATOES,
                restaurantFactory.createSeasonalMenu(input3, Side.POTATOES),
                expected3);


        // ----------------------------------------------------------------------------------
        // TEST 4: Temporada - Take Away
        // ----------------------------------------------------------------------------------
        Dish input4 = new Dish("Setas", DishType.MAIN_COURSE);
        List<Dish> inputList4 = new ArrayList<>();
        inputList4.add(input4);

        List<DishExpected> expected4 = new ArrayList<>();
        expected4.add(new DishExpected("Setas", 15.30, Side.SALAD));

        runTest("4. Take Away Temporada",
                inputList4, Side.SALAD,
                takeAwayFactory.createSeasonalMenu(input4, Side.SALAD),
                expected4);


        // ----------------------------------------------------------------------------------
        // TEST 5: Lista Vacía
        // ----------------------------------------------------------------------------------
        runTest("5. Pedido Vacio",
                new ArrayList<>(), Side.POTATOES,
                restaurantFactory.createWeeklyMenu(new ArrayList<>(), Side.POTATOES),
                new ArrayList<>());


        // ----------------------------------------------------------------------------------
        // TEST 6: Take Away solo postres
        // ----------------------------------------------------------------------------------
        List<Dish> input6 = new ArrayList<>();
        input6.add(new Dish("Helado", DishType.DESSERT));
        input6.add(new Dish("Fruta", DishType.DESSERT));

        runTest("6. Take Away Solo Postres (Filtrado total)",
                input6, Side.NONE,
                takeAwayFactory.createWeeklyMenu(input6, Side.NONE),
                new ArrayList<>());


        // ----------------------------------------------------------------------------------
        // TEST 7: Corrección de datos (Setters)
        // ----------------------------------------------------------------------------------
        Dish platoErroneo = new Dish("Pez Espada", DishType.STARTER, 99.00, Side.NONE);
        platoErroneo.setName("Emperador a la plancha");
        platoErroneo.setType(DishType.MAIN_COURSE);

        List<Dish> input7 = new ArrayList<>();
        input7.add(platoErroneo);

        List<DishExpected> expected7 = new ArrayList<>();
        expected7.add(new DishExpected("Emperador a la plancha", 15.00, Side.POTATOES));

        runTest("7. Correccion Datos (Setters/Constructor)",
                input7, Side.POTATOES,
                restaurantFactory.createWeeklyMenu(input7, Side.POTATOES),
                expected7);

        // =================================================================================
        // RESUMEN FINAL
        // =================================================================================
        printFinalSummary();
    }

    // =================================================================================
    // UTILS
    // =================================================================================

    private static void runTest(String testName, List<Dish> inputDishes, Side inputSide, Menu resultMenu, List<DishExpected> expectedList) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");

        // Input data
        System.out.println("INPUT:");
        System.out.println(" - Side: " + inputSide);
        System.out.println(" - Dishes:");
        if (inputDishes.isEmpty()) {
            System.out.println("   (None)");
        } else {
            for (Dish d : inputDishes) {
                String extra = (d.getPrice() > 0) ? " [Manual Price: " + d.getPrice() + "]" : "";
                System.out.println("   * " + d.getName() + " [" + d.getType() + "]" + extra);
            }
        }
        System.out.println(" ");

        // Output data
        List<Dish> actualDishes = resultMenu.getDishes();
        boolean globalPass = true;

        System.out.println("OUTPUT:");
        System.out.printf("%-25s | %-20s | %-20s | %-10s%n",
                "DISH", "PRICE (Exp/Act)", "SIDE (Exp/Act)", "STATUS");
        System.out.println("--------------------------|----------------------|----------------------|----------");

        int maxItems = Math.max(actualDishes.size(), expectedList.size());

        for (int i = 0; i < maxItems; i++) {
            String nameCol;
            String priceCol = "";
            String sideCol = "";
            String status = "OK";

            if (i < expectedList.size() && i < actualDishes.size()) {
                DishExpected exp = expectedList.get(i);
                Dish act = actualDishes.get(i);

                nameCol = act.getName();
                if (nameCol.length() > 25) nameCol = nameCol.substring(0, 22) + "...";

                priceCol = String.format("%.2f / %.2f", exp.expectedPrice, act.getPrice());
                sideCol = exp.expectedSide + " / " + act.getSide();

                boolean priceOk = Math.abs(exp.expectedPrice - act.getPrice()) < 0.01;
                boolean sideOk = exp.expectedSide == act.getSide();
                boolean nameOk = exp.name.equals(act.getName());

                if (!priceOk || !sideOk || !nameOk) {
                    status = "FAIL";
                    globalPass = false;
                }

            } else if (i < expectedList.size()) {
                DishExpected exp = expectedList.get(i);
                nameCol = exp.name + " (MISSING)";
                status = "FAIL";
                globalPass = false;
            } else {
                Dish act = actualDishes.get(i);
                nameCol = act.getName() + " (EXTRA)";
                status = "FAIL";
                globalPass = false;
            }

            System.out.printf("%-25s | %-20s | %-20s | %-10s%n",
                    nameCol, priceCol, sideCol, status);
        }

        double totalEsperado = 0;
        for (DishExpected d : expectedList) totalEsperado += d.expectedPrice;
        double totalReal = resultMenu.calculateTotalPrice();

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("TOTALS: Exp: " + String.format("%.2f", totalEsperado) +
                " | Act: " + String.format("%.2f", totalReal));

        if (Math.abs(totalEsperado - totalReal) > 0.01) {
            System.out.println(">>> TOTAL MISMATCH <<<");
            globalPass = false;
        }

        if (globalPass) {
            System.out.println("RESULT: PASSED");
        } else {
            System.out.println("RESULT: FAILED");
            // Aquí es donde guardamos el fallo en la lista global
            failedTests.add(testName);
        }
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

    static class DishExpected {
        String name;
        double expectedPrice;
        Side expectedSide;

        public DishExpected(String name, double expectedPrice, Side expectedSide) {
            this.name = name;
            this.expectedPrice = expectedPrice;
            this.expectedSide = expectedSide;
        }
    }
}