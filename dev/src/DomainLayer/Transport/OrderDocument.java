package DomainLayer.Transport;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class OrderDocument {
    Site origin;
    String id;
    private final ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> destinations;
    Date date;
    boolean complete = false;
    Truck truck;
    Driver driver;
    double weight;
    String time;
    private final ConcurrentHashMap<Integer, DriverDocument> driverDocs;

    public OrderDocument(String id, Site origin, ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> destination, Date date, String time){
        this.id = id;
        this.origin = origin;
        this.date = date;
        this.destinations = destination;
        this.driverDocs = new ConcurrentHashMap<>();
        this.time = time;
    }
    public ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> getDestinations(){
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
        return origin.getId();
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
        for(Site store:destinations.keySet()){
            res += store.getId()+"\n";
            for(Supply s:destinations.get(store).keySet()){
                res+= s+ " -- " + destinations.get(store).get(s).toString()+"\n";
            }
        }
        String supp = "Supplies: \n" + this.destinations.toString();
        return id + supp;
    }

    public boolean isWeightLegit(double currweight){
        return currweight <= truck.maxWeight;
    }

    public double getTotalWeight(){ //this will give me the total weight of the supplies list
        // we will use this function after the user will finish listing all the supplies he needs
        double totalweight = 0;
        for(ConcurrentHashMap<Supply,Integer> con:destinations.values()) {
            for (Supply s : con.keySet()) {
                totalweight += s.weight * con.get(s);
            }
        }
        return totalweight;
    }
    public boolean containsStore(String id){
        for(Site s : getDestinations().keySet()){
            if(Objects.equals(s.getId(), id)){
                return true;
            }
        }
        return false;
    }
    public void remove(String id){
        for(Site s : getDestinations().keySet()){
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
        String supp = "\nHe carried supplies from " + origin+" to:\n";
        for(Site store:destinations.keySet()){
            supp+=store.getId() + "\nSupplies:\n";
            for(Supply sup:destinations.get(store).keySet()){
                supp+="Name: "+ sup.name +" ,Quantity: "+destinations.get(store).get(sup)+"\n";
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

    public void removeDest(Site site) {
        destinations.remove(site);
    }

    public void addDest(Site site, ConcurrentHashMap<Supply, Integer> supplies) {
        destinations.put(site,supplies);
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
}
