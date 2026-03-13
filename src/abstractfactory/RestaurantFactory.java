package abstractfactory;

import java.util.List;

public class RestaurantFactory extends AbstractFactory {

    @Override
public Menu createWeeklyMenu(List<Dish> rawDishes, Side side) {
    // 1. Validar que vengan exactamente 3 platos
    if (rawDishes.size() != 3) {
        System.out.println("Error: El menú de restaurante debe tener 3 platos.");
        // Opcionalmente lanzar una excepción: throw new IllegalArgumentException(...);
    }
    
    // 2. Comprobar que haya uno de cada tipo (opcional para nota extra)
    boolean hasStarter = rawDishes.stream().anyMatch(d -> d.getType() == DishType.STARTER);
    boolean hasMain = rawDishes.stream().anyMatch(d -> d.getType() == DishType.MAIN_COURSE);
    boolean hasDessert = rawDishes.stream().anyMatch(d -> d.getType() == DishType.DESSERT);
    
    if (!hasStarter || !hasMain || !hasDessert) {
         System.out.println("Advertencia: El menú de restaurante requiere Entrante, Principal y Postre.");
    }

    WeeklyMenu menu = new WeeklyMenu();
    for (Dish dish : rawDishes) {
        configureDish(dish, side, 1.0);
        menu.addDish(dish);
    }
    return menu;
}

    @Override
    public Menu createSeasonalMenu(Dish rawDish, Side side) {
        SeasonalMenu menu = new SeasonalMenu();
        configureDish(rawDish, side, 1.0);
        menu.addDish(rawDish);
        return menu;
    }

}