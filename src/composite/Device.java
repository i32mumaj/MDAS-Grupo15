package composite;

public class Device implements Element{

    private double hourCounter;
    private double kWh;
    private String name;

    public Device(double hourCounter, double kWh, String name){
        this.hourCounter = hourCounter;
        this.kWh = kWh;
        this.name = name;
    }

    public double getHourCounter() {
        return hourCounter;
    }

    public double getkWh() {
        return kWh;
    }

    public void setHourCounter(double hourCounter) {
        this.hourCounter = hourCounter;
    }

    public void setkWh(double kWh) {
        this.kWh = kWh;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double calculateEnergyCosts(){
        return this.hourCounter * this.kWh;
    }

    @Override
    public String getName() {
        return this.name;
    }

}