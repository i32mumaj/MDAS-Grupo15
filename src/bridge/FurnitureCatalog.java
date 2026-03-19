package bridge;

import java.util.*;
import java.util.stream.Collectors;

public abstract class FurnitureCatalog {
    protected List<Provider> providers = new ArrayList<>();

    public void addProvider(Provider provider) {
        providers.add(provider);
    }

    // Recopila todos los productos de los proveedores asignados
    protected List<Product> fetchAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        for (Provider p : providers) {
            allProducts.addAll(p.getInventory());
        }
        return allProducts;
    }

    // 1. Método general [cite: 116]
    public List<Product> searchByMaterial(String material) {
        return fetchAllProducts().stream()
                .filter(p -> p.getMaterial().equalsIgnoreCase(material) && p.getStock() > 0)
                .collect(Collectors.toList());
    }

    // a) Búsqueda ordenada por precio ascendente [cite: 119]
    public List<Product> searchSortedByPriceAsc() {
        return fetchAllProducts().stream()
                .filter(p -> p.getStock() > 0)
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    // b) Búsqueda por stock agrupando duplicados [cite: 120, 121]
    public List<Product> searchSortedByStockDesc() {
        Map<String, Product> grouped = new HashMap<>();
        
        for (Product p : fetchAllProducts()) {
            if (p.getStock() > 0) {
                if (grouped.containsKey(p.getName())) {
                    Product existing = grouped.get(p.getName());
                    existing.setStock(existing.getStock() + p.getStock());
                } else {
                    grouped.put(p.getName(), p);
                }
            }
        }

        return grouped.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                .collect(Collectors.toList());
    }
}