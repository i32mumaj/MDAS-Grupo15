package bridge;

import java.util.ArrayList;
import java.util.List;

public class CompanyC implements Provider {
    @Override
    public List<Product> getInventory() {
        List<Product> list = new ArrayList<>();
        // Coincide el nombre con la Empresa B para probar la suma de stocks
        list.add(new Table("Mesa Comedor", 295.0, 3, "Madera", 2.0)); 
        list.add(new Sofa("Sofá Chaise Longue", 460.0, 8, "Tela", 3));
        return list;
    }
}