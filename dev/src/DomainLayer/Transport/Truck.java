package DomainLayer.Transport;

import java.util.concurrent.ConcurrentHashMap;

public class Truck {

    String type; // we assume the type is matching the license meaning we only dealing with C ans C1
    Boolean available;
    String licensePlate;
    double initialWeight;
    double maxWeight; // how much weight the truck can carry
    private final ConcurrentHashMap<String, Boolean> dates;//

    public Truck(String type, String licenseplate, double maxweight, double initialweight){

        this.type = type;
        this.initialWeight = initialweight;
        this.licensePlate = licenseplate;
        this.available = true;
        this.maxWeight = maxweight;
        dates = new ConcurrentHashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public boolean isFree(Date d){return dates.getOrDefault(d.toString(),true);}
    public void isAvailable(Date d) {
        dates.put(d.toString(), true);
    }

    public void isoccupied(Date date){
        dates.put(date.toString(), false);
    }

    public void finished(Date date){
        dates.put(date.toString(), true);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }


//    public boolean setCurrentweight(double currentweight) {
//        if(currentweight>maxweight){
//            throw new NullPointerException();
//        }
//        this.currentweight = currentweight;
//        return true;
//    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }
    public String toString(){
        String res = "type: " + type + " ,License plate: "+ licensePlate + " ,Initial weight: " + initialWeight + " ,Max weight: " + maxWeight +" ,Available: "+ available;
        return res;
    }

    public boolean isSuitable(Driver driver) {
        return this.getType().equalsIgnoreCase(driver.getLicense());
    }
}
