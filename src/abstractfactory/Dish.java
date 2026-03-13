package abstractfactory;

public class Dish {
    private String name;
    private DishType type;
    private double price;
    private Side side;

    // Constructor 1: Sin el parámetro "price"
    public Dish(String name, DishType type, Side side){
        this.name = name;
        this.type = type;
        this.side = side;
        this.price = calculateBasePrice(type); // Asigna el precio automáticamente
    }

    // Constructor 2
    public Dish(String name, DishType type){
        this.name = name;
        this.type = type;
        this.side = Side.NONE; // Por defecto sin guarnición
        this.price = calculateBasePrice(type); // Asigna el precio automáticamente
    }

    // NUEVO MÉTODO PRIVADO: Centraliza los precios base de los platos
    private double calculateBasePrice(DishType type) {
        switch (type) {
            case STARTER:
                return 10.0;
            case MAIN_COURSE:
                return 15.0;
            case DESSERT:
                return 5.0;
            default:
                return 0.0;
        }
    }

    // Getters
    public String getName(){ return this.name; }
    public DishType getType(){ return this.type; }
    public double getPrice(){ return this.price; }
    public Side getSide(){ return this.side; }

    // Setters
    public void setName(String name){ this.name = name; }
    
    // Al cambiar el tipo de plato, actualizamos su precio base para que no haya incoherencias
    public void setType(DishType type){
        this.type = type;
        this.price = calculateBasePrice(type); 
    }
    
    public void setPrice(double price){ this.price = price; }
    public void setSide(Side side){ this.side = side; }

    @Override
    public String toString() {
        return "Nombre: " + this.name + "\nTipo: " + this.type + "\nPrecio: " + this.price + "\nSide: " + this.side;
    }
}