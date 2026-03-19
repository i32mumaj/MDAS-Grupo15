package bridge;

import java.util.List;
import java.util.stream.Collectors;

public class TableCatalog extends FurnitureCatalog {
    // 3. Método específico de Mesas [cite: 116]
    public List<Table> searchByMinimumDimension(double minDimension) {
        return fetchAllProducts().stream()
                .filter(p -> p instanceof Table)
                .map(p -> (Table) p)
                .filter(t -> t.getDimensions() >= minDimension && t.getStock() > 0)
                .collect(Collectors.toList());
    }
}