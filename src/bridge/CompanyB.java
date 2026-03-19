package bridge;

import java.util.ArrayList;
import java.util.List;

public class CompanyB implements Provider {
    @Override
    public List<Product> getInventory() {
        List<Product> list = new ArrayList<>();
        list.add(new Table("Mesa Oficina Cristal", 150.0, 10, "Cristal", 1.5));
        list.add(new Table("Mesa Comedor", 300.0, 4, "Madera", 2.0));
        return list;
    }
}