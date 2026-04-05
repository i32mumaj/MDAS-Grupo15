package bridge;

public class Sofa extends Product {
    private int seats;

    public Sofa(String name, double price, int stock, String material, int seats) {
        super(name, price, stock, material);
        this.seats = seats;
    }

    public int getSeats() { return seats; }

    @Override
    public String toString() {
        return "[SOFÁ] " + super.toString() + " | Plazas: " + seats;
    }
}