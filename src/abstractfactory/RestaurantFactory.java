package abstractfactory;

import java.util.List;

public class RestaurantFactory extends AbstractFactory {

    @Override
    public Menu createWeeklyMenu(List<Dish> rawDishes, Side side) {
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