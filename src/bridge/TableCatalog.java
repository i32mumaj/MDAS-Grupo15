package bridge;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableCatalog extends FurnitureCatalog {
    @Override
    public List<Product> searchByMaterial(String material) {
        return fetchAllProducts().stream()
                .filter(p -> p instanceof Table)
                .filter(p -> p.getMaterial().equalsIgnoreCase(material) && p.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchSortedByPriceAsc() {
        return fetchAllProducts().stream()
                .filter(p -> p instanceof Table)
                .filter(p -> p.getStock() > 0)
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> searchSortedByStockDesc() {
        Map<String, Product> grouped = fetchAllProducts().stream()
                .filter(p -> p instanceof Table)
                .filter(p -> p.getStock() > 0)
                .collect(Collectors.toMap(
                        Product::getName,
                        p -> p,
                        (existing, duplicate) -> {
                            existing.setStock(existing.getStock() + duplicate.getStock());
                            return existing;
                        }));

        return grouped.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                .collect(Collectors.toList());
    }

    public List<Table> searchByMinimumDimension(double minDimension) {
        return fetchAllProducts().stream()
                .filter(p -> p instanceof Table)
                .map(p -> (Table) p)
                .filter(t -> t.getDimensions() >= minDimension && t.getStock() > 0)
                .collect(Collectors.toList());
    }
}
