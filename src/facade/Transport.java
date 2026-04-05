package facade;

public class Transport {

    private final String origin;
    private final String destination;
    private final String date;
    private final TransportType type;
    private final double price;

    public Transport(String origin, String destination, String date, TransportType type, double price) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.type = type;
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public TransportType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return origin + " -> " + destination + " | " + date + " | " + type + " | " + String.format("%.2f", price);
    }
}
