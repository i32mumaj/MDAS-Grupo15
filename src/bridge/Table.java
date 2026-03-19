package bridge;

public class Table extends Product {
    private double dimensions;

    public Table(String name, double price, int stock, String material, double dimensions) {
        super(name, price, stock, material);
        this.dimensions = dimensions;
    }

    public double getDimensions() { return dimensions; }

    @Override
    public String toString() {
        return "[MESA] " + super.toString() + " | Dimensión: " + dimensions + "m";
    }
}