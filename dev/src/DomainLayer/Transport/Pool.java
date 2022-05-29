package DomainLayer.Transport;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;


public class Pool {

    private final ConcurrentHashMap<String,OrderDoc> transporthistory; //<id, doc>
    private final ConcurrentHashMap<String, Truck> trucks; // the truck licenseplate and the truck
    private final ConcurrentHashMap<String, Driver> drivers;
    private final ConcurrentHashMap<String, Site> sites;
    private final ConcurrentHashMap<String, Supply> supplies;
    private final static Pool INSTANCE = new Pool();
    public static Pool getInstance(){
        return INSTANCE;
    }
    private Pool(){
        transporthistory = new ConcurrentHashMap<>();
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
    public ConcurrentHashMap<String,Site> getSites(){
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

    public Site getSite(String id){
        return sites.get(id);
    }

    public Supply getSupply(String name){return supplies.get(name);}

    public void removeDriver(String id){
        drivers.remove(id);
    }

    public void addTruck(Truck t){
        trucks.put(t.getLicenseplate(), t);
    }

    public void removeTruck(String id){
        trucks.remove(id);
    }

    public void addSite(Site s){
        sites.put(s.getId(), s);
    }

    public void removeSite(String id){
        sites.remove(id);
    }
    public void removeSupply(String name){supplies.remove(name);}
    public void addDoc(OrderDoc doc){
       if(transporthistory.containsKey(doc.getId())){throw new NullPointerException();}
       else{
           transporthistory.put(doc.getId(), doc);
       }
    }
    public void removeDoc(String doc){
        transporthistory.remove(doc);
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

//        for(Supply supply:doc.getDestinations().get(store).get(name)){
//            return supply;
//            }
//            counter++;
//        }
//        return null;
//    }
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
    public OrderDoc getOrderDoc(String id){
        for(OrderDoc doc:transporthistory.values()){
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
        String res = "";
        for(Site store : sites.values()){
            if(store.type == 1 && store.areacode.toString() == areacode){
                res+= "ID: " +store + "\n";
            }
        }
        return res;
    }
    public String showSuppliers(String areacode) {
        String res = "";
        for(Site supp : sites.values()){
            if(supp.type == 0 && supp.areacode.toString().equals(areacode)){
                res+= "ID: " +supp + "\n";
            }
        }
        return res;
    }

    /*public void replaceStores(OrderDoc doc, String id2replace, String newStoreID) {
        Site a = getSite(id2replace);
        Site b = getSite(newStoreID);
        if(a.type!=1 ||b.type!=1){
            throw new NullPointerException();
        }
        doc.setStore(b); //TODO
    }*/
//    public Driver getDriverAtIDX(int idx){
//        int counter = 0;
//        for(Driver d:drivers.values()){
//            if(counter == idx-1){
//                return d;
//            }
//            counter++;
//        }
//        return null;
//    }




}
