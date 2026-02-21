package abstractfactory;

import java.util.List;

public abstract class AbstractFactory {

    public abstract Menu createWeeklyMenu(List<Dish> rawDishes, Side side);
    public abstract Menu createSeasonalMenu(Dish rawDish, Side side);

    protected void configureDish(Dish dish, Side side, double priceFactor) {
        double basePrice = 0.0;

        switch (dish.getType()) {
            case STARTER:
                basePrice = 10.0;
                break;
            case MAIN_COURSE:
                basePrice = 15.0;
                break;
            case DESSERT:
                basePrice = 5.0;
                break;
        }

        dish.setPrice(basePrice * priceFactor);

        if (dish.getType() == DishType.MAIN_COURSE) {
            dish.setSide(side);
        } else {
            dish.setSide(Side.NONE);
        }
    }


}