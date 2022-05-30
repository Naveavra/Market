package DomainLayer.Transport;

import DAL.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderController {
    public DriverDocDAO driverDocs = new DriverDocDAO();
    public OrderDocDAO orderDocs = new OrderDocDAO();
    public TruckDAO trucks = new TruckDAO();
    public SiteDAO sites = new SiteDAO();
    public SuppliesDAO supplies = new SuppliesDAO();
    public DriverDAO drivers = new DriverDAO();
    AtomicInteger id = new AtomicInteger();
    AtomicInteger driverDocID = new AtomicInteger();
    private final static OrderController INSTANCE = new OrderController();
    public static OrderController getInstance(){
        return INSTANCE;
    }
//    public OrderDoc getDocByID(String id){
//        return pool.getOrderDocByID(id);
//    }

    public Date createDate(String date){
        String[] temp = date.split("/");
        return new Date(temp[0], temp[1], temp[2]);

    }
//    public Supply getSupplybyDoc(String docID,String storeID,String supp){
//        for(Supply supply: pool.getOrderDoc(docID).getDestinations().get(pool.getSite(storeID)).keySet()){
//            if(supply.name.equalsIgnoreCase(supp)){
//                return supply;
//            }
//        }
//        throw new NullPointerException();
//    }
//    public static void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantities){
//        Site store = pool.getSite(siteID);
//        ConcurrentHashMap<String,Integer> supplies=new ConcurrentHashMap<>();
//        for(int i = 0;i< quantities.size();i++){
//            supplies.put(suppName.get(i),quantities.get(i));
//        }
//        for(String s: supplies.keySet()){
//            if(supplies.get(s)>0){
//                pool.getOrderDoc(docID).getDestinations().get(store).put(pool.getSupply(s),supplies.get(s));
//            }
//            else {
//                pool.getOrderDoc(docID).getDestinations().get(store).remove(pool.getSupply(s));
//            }
//        }
//        changeDriverDoc(docID,siteID,pool.getOrderDoc(docID).getDestinations().get(store));
//
//    }
    public  boolean setTrucksWeight(String docID,double weight){
        orderDocs.updateDocWeight(docID,String.valueOf(weight),"weight");
        return orderDocs.getOrderDoc(docID).setWeight(weight).equals("Success");
    }

    public String showTrucks(String date,String driverID,String time){
        String res = "Trucks:\n";
        int idx=1;
        String licenseType = drivers.getDriver(driverID).getLicense();
        for(Truck t:trucks.getTrucks(date,time,licenseType)) {
            if(trucks.getAvailability(t.licensePlate,date) && t.isSuitable(drivers.getDriver(driverID))) {
                res += idx + ". " + (t + "\n");
            }
            idx++;
        }
        return res;
    }

    public void setNewTruck(String doc, String newTruckPlate) {
        Truck newTruck = trucks.getTruck(newTruckPlate);
        OrderDocument orderDoc = orderDocs.getOrderDoc(doc);
        trucks.setAvailability(orderDoc.truck, orderDoc.getDate().toString(), orderDoc.time, true);
        trucks.setAvailability(newTruck, orderDoc.getDate().toString(), orderDoc.getTime(), false);
        orderDocs.set(doc,"licensePlate",newTruckPlate);
        orderDoc.setTruck(newTruck, orderDoc.getDate());
    }

    public String showStores(String areacode) {
        int area = Integer.parseInt(areacode);
        ArrayList<String> lst = sites.showSites(area,1);
        if(lst.isEmpty()){
            return "No stores in this area yet.";
        }
        return ShowSitesOption(lst);
    }

    public ConcurrentHashMap<Supply,Integer> createSupplyList(ConcurrentHashMap<String,Integer> supp){
        ConcurrentHashMap<Supply,Integer>supplies = new ConcurrentHashMap<>();
        for(String s:supp.keySet()){
            supplies.put(this.supplies.getSupply(s),supp.get(s));
        }
        return supplies;
    }
    public void replaceStores(String doc, String id2replace, String newStoreID,ConcurrentHashMap<String,Integer>supplies) {
        removeSiteFromDoc(doc,id2replace);
        orderDocs.getOrderDoc(doc).addDest(sites.getSite(newStoreID),createSupplyList(supplies));
        if(Objects.equals(orderDocs.replaceStore(doc,id2replace,newStoreID,supplies),"Success")){
            driverDocs.addDriverDoc(driverDocID.getAndIncrement(),orderDocs.getOrderDoc(doc).getDriver().getId(),doc,newStoreID);
        };
    }

    public String showSupplies(String docID,String storeID,String method) {
        ConcurrentHashMap<Supply,Integer> supplies;
        String res = "";
        int counter = 0;
        if(Objects.equals(method,"by Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
            for(Supply supply: supplies.keySet()){
                res+= counter + ".Name: " +supply.getName()+" ,Quantity: "+supplies.get(supply)+"\n";
                counter += 1;
            }
        }else{
            supplies = this.supplies.showSupplies();
            for(Supply supply: supplies.keySet()){
                res+= counter + ".Name: " +supply.getName()+"\n";
                counter += 1;
            }
        }

        return res;
    }


    public String showSuppliers(String areacode) {
        int area = Integer.parseInt(areacode);
        if (sites.showSites(area, 0).size() == 0){
            return "No suppliers available, sorry G";
        }
        return ShowSitesOption(sites.showSites(area,0));
    }

    private String ShowSitesOption(ArrayList<String> showSites) {
        int counter = 0;
        String res = "";
        for (int i=0;i<showSites.size();i++){
            res += counter + "." + showSites.get(i) + "\n";
            counter++;
        }
        return res;

    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
        int docID = id.getAndIncrement();
        ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>>orderList = new ConcurrentHashMap<>();
        for(String storeName:orders.keySet()){
            orderList.put(sites.getSite(storeName),createSupplyList(orders.get(storeName)));
        }
        OrderDocument doc = new OrderDocument(String.valueOf(docID),sites.getSite(supplier),orderList,createDate(date), time);
        doc.setTruckandDriver(trucks.getTruck(truckPlate),drivers.getDriver(driverID));
        drivers.setAvailability(drivers.getDriver(driverID),doc.getDate().toString(),doc.time,false);
        orderDocs.addDoc(doc);
        createDriverDocs(doc.getId());
        return doc.getId();
    }

    public void removeSiteFromDoc(String docID, String siteID) {
        orderDocs.getOrderDoc(docID).removeDest(sites.getSite(siteID));
        orderDocs.removeDest(docID,siteID,"Destinations");
        orderDocs.removeDest(docID,siteID,"order4Dest");
        removeDriverDoc(docID,siteID);
    }

    public String showDrivers(String d,String time) {
        String res="";
        int idx = 1;
        for(Driver driver: drivers.getDrivers(d,time)){
            if(driver.isFree(createDate(d))) { //i check it by using contains key so if its not contains key hes free
                res += idx + ". " + driver + "\n";
            }
            idx++;
        }
        return res;

    }


    public String getDriver(String driverID) {
        if(drivers.containsDriver(driverID)){
            return driverID;
        }
        return null;
    }


    public void removeDoc(String doc) {
        orderDocs.removeDoc(doc);
    }



    public void changeOrder(String docID, String storeID, ArrayList<String>names,ArrayList<Integer>quantities) {
        ConcurrentHashMap<Supply,Integer> newSupplyOrder = new ConcurrentHashMap<>();
        OrderDocument doc =orderDocs.getOrderDoc(docID);
        Site store = sites.getSite(storeID);
        for(int i = 0;i< names.size();i++){
            if(names.size()==quantities.size()){
                newSupplyOrder.put(supplies.getSupply(names.get(i)),quantities.get(i));
            }
        }
        doc.remove(store.getId());
        orderDocs.removeDest(docID,storeID,"Destinations");
        orderDocs.updateOrder(docID,storeID,names,quantities);
        doc.getDestinations().put(store,newSupplyOrder);
//        changeDriverDoc(docID,storeID,newSupplyOrder);   //TODO Nave: we do not keep driverDoc supplies, we can reach through order4Dest db
    }
    public boolean containsSite(String docID, String storeID) {
        return orderDocs.containsStore(docID,storeID);
    }

    public void transportIsDone(String docID) {
        OrderDocument od = orderDocs.getOrderDoc(docID);
        drivers.setAvailability(od.driver, od.getDate().toString(), od.getTime(), true);
        trucks.setAvailability(od.truck, od.getDate().toString(), od.getTime(), true);
        drivers.RemoveFromIdentityMap(od.driver.getId());
        trucks.RemoveFromIdentityMap(od.truck.getLicensePlate());
        orderDocs.getOrderDoc(docID).finishOrder();
        orderDocs.set(docID,"finished","#t");
    }
    public void createDriverDocs(String docID){
        OrderDocument doc = orderDocs.getOrderDoc(docID);
        for(Site site : doc.getDestinations().keySet()) {
            int id = driverDocID.getAndIncrement();
            driverDocs.addDriverDoc(new DriverDocument(doc.driver,id,doc.getDestinations().get(site),site,doc.getId()));
        }
    }
    public void removeDriverDoc(String orderDocID,String siteID){
        driverDocs.removeDriverDocByODOC(orderDocID,siteID);
    }

//    public void changeDriverDoc(String orderDocID,String siteID,ConcurrentHashMap<Supply,Integer>supplies){
//        driverDocs.updateDriverDoc(orderDocID,siteID,supplies);
//    }

    public String ShowDriverDocs(String docID) {
        ArrayList<DriverDocument> docs = driverDocs.showDriverDocs(docID);
        String res="";
        for (DriverDocument doc: docs) {
            res += doc.toString() + "\n";
        }
        if(res.length() > 0){
            return res;
        }
        return "No driver docs to show for the order document.";

    }


    public String showStoresforDoc(String docID) {
        ArrayList <String> stores = orderDocs.showStores(docID);
        String res = "";
        for(String s : stores){
            res += "ID: "+ s+"\n";
        }
        return res;
    }

    public String viewOrder(String docID) {
        if(orderDocs.getOrderDoc(docID)!=null){
            return orderDocs.getOrderDoc(docID).toString();
        }
        return "";
    }

    public String getTruck(String licensePlate) {
        if(trucks.containsTruck(licensePlate)){
            return licensePlate;
        }
        return "Truck Does not exist";
    }

    public String getDate(String docID) {
        return orderDocs.getOrderDoc(docID).getDate().toString();
    }

    public String getDriverID(String docID) {
        return orderDocs.getOrderDoc(docID).getDriver().getId();
    }
    public String getTime(String docID){
        return orderDocs.getOrderDoc(docID).getTime();
    }
    public double getCurrWeight(String doc) {
        return orderDocs.getOrderDoc(doc).getWeight();
    }

    public boolean getDoc(String docID) {
        return orderDocs.getOrderDoc(docID)!=null;
    }


    public String getSupplyByIdx(int idx, String docID,String storeID,String pred) {
        ConcurrentHashMap<Supply,Integer> supplies;
        if(Objects.equals(pred, "By Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
        }
        else{
            supplies = this.supplies.showSupplies();
        }
        int counter = 0;
        for(Supply supp: supplies.keySet()){
            if(counter==idx){
                return supp.name;
            }
            counter++;
        }
        return null;
    }
}
