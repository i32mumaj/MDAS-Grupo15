package bridge;

public abstract class Product {
    private String name;
    private double price;
    private int stock;
    private String material;

    public Product(String name, double price, int stock, String material) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.material = material;
    }

    // Getters y Setters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getMaterial() { return material; }

    @Override
    public String toString() {
        return name + " | Precio: " + price + "€ | Stock: " + stock + " | Material: " + material;
    }
}