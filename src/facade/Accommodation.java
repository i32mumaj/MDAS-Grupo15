package facade;

public class Accommodation {

    private final String city;
    private final String date;
    private final AccommodationType type;
    private final double price;

    public Accommodation(String city, String date, AccommodationType type, double price) {
        this.city = city;
        this.date = date;
        this.type = type;
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    public AccommodationType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return city + " | " + date + " | " + type + " | " + String.format("%.2f", price);
    }
}
