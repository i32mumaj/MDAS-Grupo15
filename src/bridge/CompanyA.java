package bridge;

import java.util.ArrayList;
import java.util.List;

public class CompanyA implements Provider {
    @Override
    public List<Product> getInventory() {
        List<Product> list = new ArrayList<>();
        list.add(new Sofa("Sofá Chaise Longue", 450.0, 5, "Tela", 3));
        list.add(new Sofa("Sofá Piel Premium", 900.0, 2, "Piel", 2));
        return list;
    }
}