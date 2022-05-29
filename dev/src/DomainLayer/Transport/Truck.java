package DomainLayer.Transport;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Truck {

    String type; // we assume the type is matching the license meaning we only dealing with C ans C1
    Boolean available;
    String licenseplate;
    double initialweight;
    double maxweight; // how much weight the truck can carry
    private final ConcurrentHashMap<String, Boolean> dates;;//

    public Truck(String type, String licenseplate, double maxweight, double initialweight){

        this.type = type;
        this.initialweight = initialweight;
        this.licenseplate = licenseplate;
        this.available = true;
        this.maxweight = maxweight;
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

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }


//    public boolean setCurrentweight(double currentweight) {
//        if(currentweight>maxweight){
//            throw new NullPointerException();
//        }
//        this.currentweight = currentweight;
//        return true;
//    }

    public double getInitialweight() {
        return initialweight;
    }

    public void setInitialweight(double initialweight) {
        this.initialweight = initialweight;
    }

    public double getMaxweight() {
        return maxweight;
    }

    public void setMaxweight(double maxweight) {
        this.maxweight = maxweight;
    }
    public String toString(){
        String res = "type: " + type + " ,License plate: "+ licenseplate+ " ,Initial weight: " + initialweight+ " ,Max weight: " +maxweight +" ,Available: "+ available;
        return res;
    }

    public boolean isSuitable(Driver driver) {
        return this.getType().equalsIgnoreCase(driver.getLicense());
    }
}
