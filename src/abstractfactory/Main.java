package abstractfactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("                 AUDITORÍA DEL SISTEMA DE PEDIDOS");
        System.out.println("=======================================================================");

        AbstractFactory restaurantFactory = new RestaurantFactory();
        AbstractFactory takeAwayFactory = new TakeAwayFactory();

        // ----------------------------------------------------------------------------------
        // CASO 1: MENÚ SEMANAL - RESTAURANTE
        // ----------------------------------------------------------------------------------
        List<Dish> input1 = new ArrayList<>();
        input1.add(new Dish("Sopa", DishType.STARTER));
        input1.add(new Dish("Carne", DishType.MAIN_COURSE));
        input1.add(new Dish("Flan", DishType.DESSERT));

        List<DishExpected> expected1 = new ArrayList<>();
        expected1.add(new DishExpected("Sopa", 10.00, Side.NONE));
        expected1.add(new DishExpected("Carne", 15.00, Side.POTATOES));
        expected1.add(new DishExpected("Flan", 5.00, Side.NONE));

        runTest("1. RESTAURANTE SEMANAL (Standard)",
                input1, Side.POTATOES, // <--- Pasamos los inputs
                restaurantFactory.createWeeklyMenu(input1, Side.POTATOES),
                expected1);


        // ----------------------------------------------------------------------------------
        // CASO 2: MENÚ SEMANAL - TAKE AWAY
        // ----------------------------------------------------------------------------------
        List<Dish> input2 = new ArrayList<>();
        input2.add(new Dish("Sopa", DishType.STARTER));
        input2.add(new Dish("Carne", DishType.MAIN_COURSE));
        input2.add(new Dish("Flan", DishType.DESSERT));

        List<DishExpected> expected2 = new ArrayList<>();
        expected2.add(new DishExpected("Sopa", 10.20, Side.NONE));
        expected2.add(new DishExpected("Carne", 15.30, Side.SALAD));

        runTest("2. TAKE AWAY SEMANAL (Filtrado Postre + Recargo)",
                input2, Side.SALAD,
                takeAwayFactory.createWeeklyMenu(input2, Side.SALAD),
                expected2);


        // ----------------------------------------------------------------------------------
        // CASO 3: TEMPORADA - RESTAURANTE
        // ----------------------------------------------------------------------------------
        Dish input3 = new Dish("Setas", DishType.MAIN_COURSE);
        // Wrapper para poder imprimirlo en el test
        List<Dish> inputList3 = new ArrayList<>();
        inputList3.add(input3);

        List<DishExpected> expected3 = new ArrayList<>();
        expected3.add(new DishExpected("Setas", 15.00, Side.POTATOES));

        runTest("3. RESTAURANTE TEMPORADA",
                inputList3, Side.POTATOES,
                restaurantFactory.createSeasonalMenu(input3, Side.POTATOES),
                expected3);


        // ----------------------------------------------------------------------------------
        // CASO 4: TEMPORADA - TAKE AWAY
        // ----------------------------------------------------------------------------------
        Dish input4 = new Dish("Setas", DishType.MAIN_COURSE);
        List<Dish> inputList4 = new ArrayList<>();
        inputList4.add(input4);

        List<DishExpected> expected4 = new ArrayList<>();
        expected4.add(new DishExpected("Setas", 15.30, Side.SALAD));

        runTest("4. TAKE AWAY TEMPORADA",
                inputList4, Side.SALAD,
                takeAwayFactory.createSeasonalMenu(input4, Side.SALAD),
                expected4);


        // ----------------------------------------------------------------------------------
        // CASO 5: LISTA VACÍA
        // ----------------------------------------------------------------------------------
        runTest("5. PEDIDO VACÍO",
                new ArrayList<>(), Side.POTATOES,
                restaurantFactory.createWeeklyMenu(new ArrayList<>(), Side.POTATOES),
                new ArrayList<>());


        // ----------------------------------------------------------------------------------
        // CASO 6: TAKE AWAY SOLO CON POSTRES
        // ----------------------------------------------------------------------------------
        List<Dish> input6 = new ArrayList<>();
        input6.add(new Dish("Helado", DishType.DESSERT));
        input6.add(new Dish("Fruta", DishType.DESSERT));

        runTest("6. TAKE AWAY SOLO POSTRES (Todo filtrado)",
                input6, Side.NONE,
                takeAwayFactory.createWeeklyMenu(input6, Side.NONE),
                new ArrayList<>());
    }

    // =================================================================================
    //                               MOTOR DE PRUEBAS
    // =================================================================================

    private static void runTest(String testName, List<Dish> inputDishes, Side inputSide, Menu resultMenu, List<DishExpected> expectedList) {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("TEST: " + testName);
        System.out.println("-----------------------------------------------------------------------");

        // 1. IMPRIMIR INPUTS (LO QUE NOS PIDES AÑADIR)
        System.out.println("INPUT (Datos de Entrada):");
        System.out.println(" - Guarnición solicitada: " + inputSide);
        System.out.println(" - Platos solicitados (Crudos):");
        if (inputDishes.isEmpty()) {
            System.out.println("   (Ninguno)");
        } else {
            for (Dish d : inputDishes) {
                System.out.println("   * " + d.getName() + " [" + d.getType() + "]");
            }
        }
        System.out.println(" "); // Separador

        // 2. IMPRIMIR RESULTADOS (OUTPUT)
        List<Dish> actualDishes = resultMenu.getDishes();
        boolean globalPass = true;

        System.out.println("OUTPUT (Resultado de la Factoría):");
        System.out.println(String.format("%-15s | %-20s | %-20s | %-10s",
                "PLATO", "PRECIO (Esp/Obt)", "SIDE (Esp/Obt)", "ESTADO"));
        System.out.println("----------------|----------------------|----------------------|----------");

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
                priceCol = String.format("%.2f / %.2f", exp.expectedPrice, act.getPrice());
                sideCol = exp.expectedSide + " / " + act.getSide();

                boolean priceOk = Math.abs(exp.expectedPrice - act.getPrice()) < 0.01;
                boolean sideOk = exp.expectedSide == act.getSide();
                boolean nameOk = exp.name.equals(act.getName());

                if (!priceOk || !sideOk || !nameOk) {
                    status = "FALLO";
                    globalPass = false;
                }

            } else if (i < expectedList.size()) {
                DishExpected exp = expectedList.get(i);
                nameCol = exp.name + " (FALTA)";
                status = "FALLO";
                globalPass = false;
            } else {
                Dish act = actualDishes.get(i);
                nameCol = act.getName() + " (EXTRA)";
                status = "FALLO";
                globalPass = false;
            }

            System.out.println(String.format("%-15s | %-20s | %-20s | %-10s",
                    nameCol, priceCol, sideCol, status));
        }

        double totalEsperado = 0;
        for (DishExpected d : expectedList) totalEsperado += d.expectedPrice;
        double totalReal = resultMenu.calculateTotalPrice();

        System.out.println("-----------------------------------------------------------------------");
        System.out.println("TOTALES: Esperado: " + String.format("%.2f", totalEsperado) +
                " | Obtenido: " + String.format("%.2f", totalReal));

        if (Math.abs(totalEsperado - totalReal) > 0.01) {
            System.out.println(">>> ERROR EN TOTALES <<<");
            globalPass = false;
        }

        System.out.println("RESULTADO TEST: " + (globalPass ? "CORRECTO" : "FALLIDO"));
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