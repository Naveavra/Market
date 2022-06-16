package DomainLayer.Transport;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;


public class Pool {

    private final ConcurrentHashMap<String, OrderDocument> transportHistory; //<id, doc>
    private final ConcurrentHashMap<String, Truck> trucks; // the truck license-plate and the truck
    private final ConcurrentHashMap<String, Driver> drivers;
    private final ConcurrentHashMap<String, Store> sites;
    private final ConcurrentHashMap<String, Supply> supplies;
    private final static Pool INSTANCE = new Pool();
    public static Pool getInstance(){
        return INSTANCE;
    }
    private Pool(){
        transportHistory = new ConcurrentHashMap<>();
        trucks = new ConcurrentHashMap<>();
        drivers = new ConcurrentHashMap<>();
        sites = new ConcurrentHashMap<>();
        supplies = new ConcurrentHashMap<>();
    }
    public ConcurrentHashMap<String,Truck> getTrucks(){
        return trucks;
    }
    public ConcurrentHashMap<String,Driver> getDrivers(){
        return drivers;
    }
    public ConcurrentHashMap<String, Store> getSites(){
        return sites;
    }
    public ConcurrentHashMap<String,Supply> getSupplies(){
        return supplies;
    }
    public void addDriver(Driver d){
        drivers.put(d.getId(), d);
    }

    public void addSupply(Supply s){
        supplies.put(s.getName().toLowerCase(Locale.ROOT), s);
    }

    public Driver getDriver(String id){
        return drivers.get(id);
    }

    public Truck getTruck(String id){
        return trucks.get(id);
    }

    public Store getSite(String id){
        return sites.get(id);
    }

    public Supply getSupply(String name){return supplies.get(name);}

    public void removeDriver(String id){
        drivers.remove(id);
    }

    public void addTruck(Truck t){
        trucks.put(t.getLicensePlate(), t);
    }

    public void removeTruck(String id){
        trucks.remove(id);
    }

    public void addSite(Store s){
        sites.put(s.getId(), s);
    }

    public void removeSite(String id){
        sites.remove(id);
    }
    public void removeSupply(String name){supplies.remove(name);}
    public void addDoc(OrderDocument doc){
       if(transportHistory.containsKey(doc.getId())){throw new NullPointerException();}
       else{
           transportHistory.put(doc.getId(), doc);
       }
    }
    public void removeDoc(String doc){
        transportHistory.remove(doc);
    }

    public String showSupplies(){
        String res = "";
        int idx=1;
        for(Supply supply:supplies.values()){
            res+= idx+". "+ supply+"\n";
            idx++;
        }
        return res;
    }
    public Supply getSupplyByIndex(int idx,ArrayList<Supply> supplies){
        int counter = 0;
        for(Supply supply:supplies){
            if(counter==idx-1){
                return supply;
            }
            counter++;
        }
        return null;
    }

    public boolean isEmpty(){
        if(trucks.isEmpty() && drivers.isEmpty()&& sites.isEmpty() && supplies.isEmpty()){
            return true;
        }
        return false;
    }
//    public void addTransports(Date d, ArrayList<OrderDoc> o){ //adding all the transports made that day
//        transporthistory.put(d, o);
//    }
  //  public OrderDoc getOrderDocbyDate(String id,Date date){
    //    return transporthistory.get(date).get(id);
//        for(OrderDoc doc:transporthistory.get(date)){
//            if(doc.id == id){
//                return doc;
//            }
//        }
//        throw new NullPointerException();
   // }
    public OrderDocument getOrderDoc(String id){
        for(OrderDocument doc: transportHistory.values()){
            if(doc.id.equals(id)){
                return doc;
            }
        }
        throw new NullPointerException();
    }
/*    public Truck getTruckAtIDX(int idx){
        int counter = 0;
        for(Truck t:trucks.values()){
            if(counter == idx-1){
                return t;
            }
            counter++;
        }
        return null;
    }*/



    public String showStores(String areacode) {
        String result = "";
        for(Store store : sites.values()){
            if(store.getType() == 1 && store.getAreaCode().toString() == areacode){
                result += "ID: " +store + "\n";
            }
        }
        return result;
    }
    public String showSuppliers(String areaCode) {
        String res = "";
        for(Store supp : sites.values()){
            if(supp.getType() == 0 && supp.getAreaCode().toString().equals(areaCode)){
                res += "ID: " +supp + "\n";
            }
        }
        return res;
    }
}
