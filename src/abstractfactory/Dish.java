package abstractfactory;

public class Dish {
    private String name;
    private DishType type;
    private double price;
    private Side side;

    public Dish(String name, DishType type, double price, Side side){
        this.name = name;
        this.type = type;
        this.price = price;
        this.side = side;
    }

    public String getName(){
        return this.name;
    }

    public DishType getType(){
        return this.type;
    }

    public double getPrice(){
        return this.price;
    }

    public Side getSide(){
        return this.side;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setType(DishType type){
        this.type = type;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setSide(Side side){
        this.side = side;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.name + "\nTipo: " + this.type + "\nPrecio: " + this.price + "\nSide: " + this.side;
    }
}