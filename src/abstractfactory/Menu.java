package abstractfactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {

    private List<Dish> dishes;

    public Menu(List<Dish> dishes){
        this.dishes = dishes;
    }

    public Menu(){
        this.dishes = new ArrayList<>();
    }

    public List<Dish> getDishes(){
        return this.dishes;
    }

    public void setDishes(List<Dish> dishes){
        this.dishes = dishes;
    }

    public void addDish(Dish dish){
        this.dishes.add(dish);
    }

    public double calculateTotalPrice(){
        double ret_v = 0;
        for(Dish dish : dishes){
            ret_v += dish.getPrice();
        }
        return ret_v;
    }

}