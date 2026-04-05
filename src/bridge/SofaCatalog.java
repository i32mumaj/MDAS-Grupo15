package bridge;

import java.util.List;
import java.util.stream.Collectors;

public class SofaCatalog extends FurnitureCatalog {
    // 2. Método específico de Sofás [cite: 116]
    public List<Sofa> searchByMinimumSeats(int minSeats) {
        return fetchAllProducts().stream()
                .filter(p -> p instanceof Sofa)
                .map(p -> (Sofa) p)
                .filter(s -> s.getSeats() >= minSeats && s.getStock() > 0)
                .collect(Collectors.toList());
    }
}