package bridge;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Provider companyA = new CompanyA(); // Solo Sofás
        Provider companyB = new CompanyB(); // Solo Mesas
        Provider companyC = new CompanyC(); // Mesas y Sofás

        System.out.println("=== CONFIGURANDO CATÁLOGO DE MESAS ===");
        TableCatalog tableCatalog = new TableCatalog();
        tableCatalog.addProvider(companyA); // No aportará nada, pero el sistema lo soporta
        tableCatalog.addProvider(companyB); 
        tableCatalog.addProvider(companyC);

        System.out.println("\n--- Búsqueda de mesas por precio (menor a mayor) ---");
        printList(tableCatalog.searchSortedByPriceAsc());

        System.out.println("\n--- Búsqueda de mesas > 1.8m ---");
        printList(tableCatalog.searchByMinimumDimension(1.8));


        System.out.println("\n\n=== CONFIGURANDO CATÁLOGO DE SOFÁS ===");
        SofaCatalog sofaCatalog = new SofaCatalog();
        sofaCatalog.addProvider(companyA);
        sofaCatalog.addProvider(companyC);

        System.out.println("\n--- Búsqueda de sofás agrupados por Stock (mayor a menor) ---");
        // Aquí se verá cómo el 'Sofá Chaise Longue' suma el stock de la Empresa A (5) y C (8) = 13.
        printList(sofaCatalog.searchSortedByStockDesc());

        System.out.println("\n--- Búsqueda general por material (Tela) ---");
        printList(sofaCatalog.searchByMaterial("Tela"));
    }

    private static void printList(List<? extends Product> list) {
        if (list.isEmpty()) {
            System.out.println("No se encontraron productos.");
        }
        for (Product p : list) {
            System.out.println(p.toString());
        }
    }
}