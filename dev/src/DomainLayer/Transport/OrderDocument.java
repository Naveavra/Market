package DomainLayer.Transport;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class OrderDocument {
    int supplierID;
    String id;
    private final ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> destinations;
    Date date;
    boolean complete = false;
    Truck truck;
    Driver driver;
    double weight;
    String time;
    private final ConcurrentHashMap<Integer, DriverDocument> driverDocs;

    public OrderDocument(String id, int supplierID, ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> destination, Date date, String time){
        this.id = id;
        this.supplierID = supplierID;
        this.date = date;
        this.destinations = destination;
        this.driverDocs = new ConcurrentHashMap<>();
        this.time = time;
    }
    public ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> getDestinations(){
        return destinations;
    }
    public String setWeight(double weight){ //here the driver calls the manager after visits the supllier
        // to tell him the current weight
        if(truck.getMaxWeight()<weight){
            return "Error: weight exceeds the maximum";
        }
        this.weight = weight;
        return "Success";
    }
    public String getOrigin(){
        return String.valueOf(supplierID);
    }
    public String getTruck(){
        return truck.getLicensePlate();
    }
    public void setTruckandDriver(Truck t,Driver d){
        this.truck=t;
        this.driver=d;
    }

    public String getId() {
        return id;
    }

    public String getDriverDoc(){ //here we create the document for the driver
        String res = "Doc id: " + this.id + "\n";
        for(Store store:destinations.keySet()){
            res += store.getId()+"\n";
            for(String s:destinations.get(store).keySet()){
                res+= s+ " -- " + destinations.get(store).get(s).toString()+"\n";
            }
        }
        String supp = "Supplies: \n" + this.destinations.toString();
        return id + supp;
    }

    public boolean isWeightLegit(double currweight){
        return currweight <= truck.maxWeight;
    }
/*
    public double getTotalWeight(){ //this will give me the total weight of the supplies list
        // we will use this function after the user will finish listing all the supplies he needs
        double totalweight = 0;
        for(ConcurrentHashMap<String,Integer> con:destinations.values()) {
            for (String s : con.keySet()) {
                totalweight += s.weight * con.get(s);
            }
        }
        return totalweight;
    }*/
    public boolean containsStore(String id){
        for(Store s : getDestinations().keySet()){
            if(Objects.equals(s.getId(), id)){
                return true;
            }
        }
        return false;
    }
    public void remove(String id){
        for(Store s : getDestinations().keySet()){
            if(Objects.equals(s.getId(), id)){
                getDestinations().remove(s);
            }
        }
    }
    public String toString(){
        String orderID = "Doc id is: " + id;
        String Date = "\ncreated in " + date;
        String drivertruck = "\nThe driver was " + driver.getName() + " he used the truck with the licenseplate number " +
                truck.getLicensePlate() + "\nThe departure time was: " + this.time + "\nTruck weight: " + weight;
        String supp = "\nHe carried supplies from " + supplierID+" to:\n";
        for(Store store:destinations.keySet()){
            supp+=store.getId() + "\nSupplies:\n";
            for(String sup:destinations.get(store).keySet()){
                supp+="Product id: "+ sup +" ,Quantity: "+destinations.get(store).get(sup)+"\n";
            }
        }

        return orderID + Date + drivertruck  + supp;
    }


    public void setTruck(Truck newTruck, Date d) {
        if(this.truck!=null){
            this.truck.isAvailable(d);
        }
        this.truck = newTruck;
        newTruck.isoccupied(d);
        this.weight = newTruck.initialWeight;
    }

    public Date getDate(){
        return date;
    }

    public void removeDest(Store store) {
        destinations.remove(store);
    }

    public void addDest(Store store, ConcurrentHashMap<String, Integer> supplies) {
        destinations.put(store,supplies);
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void finishOrder() {
        truck.isAvailable(date);
        driver.isAvailable(date);
        this.complete = true;
    }

    public ConcurrentHashMap<Integer, DriverDocument> getDriverDocs() {
        return driverDocs;
    }



    public double getWeight() {
        return this.weight;
    }

    public String getTime() {
        return time;
    }

    public void setFinished(boolean finished) {
        this.complete = finished;
    }

    public String getFinished() {
        if (this.complete){
            return "#t";
        }
        return "#f";
    }
}
