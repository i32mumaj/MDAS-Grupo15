package abstractfactory;

import abstractfactory.DishType;
import abstractfactory.Side;

public class Main {
    public static void main(String[] args){
        Dish myDish = new Dish("Macarrones", DishType.MAIN_COURSE, 5, Side.SALAD);

        System.out.println(myDish.toString());
    }
}