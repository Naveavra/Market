package DomainLayer.Transport;

import DAL.*;
import DomainLayer.Storage.Product;
import DomainLayer.Suppliers.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class OrderController {
    public DriverDocDAO driverDocs;
    public OrderDocDAO orderDocs;
    public TruckDAO trucks;
    public StoreDAO sites;
    public ProductDAO supplies;
    public DriverDAO drivers;
    AtomicInteger id;
    AtomicInteger driverDocID;
    public SuppliersDAO suppliers;
    public Store defaultStore;
    public OrderController(){
        driverDocs = new DriverDocDAO();
        orderDocs = new OrderDocDAO();
        trucks = new TruckDAO();
        sites = new StoreDAO();
        supplies = new ProductDAO();
        drivers = new DriverDAO();
        id = new AtomicInteger();
        driverDocID = new AtomicInteger();
        suppliers = new SuppliersDAO();
        defaultStore = sites.getSite("616");
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

    public String showStores(String areaCode) {
        int area = Integer.parseInt(areaCode);
        ArrayList<String> lst = sites.showSites(area,1);
        if(lst.isEmpty()){
            return "No stores in this area yet.";
        }
        return ShowSitesOption(lst);
    }

    public ConcurrentHashMap<String,Integer> createSupplyList(ConcurrentHashMap<String,Integer> supp){//need to make sure we are sending productid
        ConcurrentHashMap<String,Integer>supplies = new ConcurrentHashMap<>();
        for(String s:supp.keySet()){
            try {
                supplies.put(String.valueOf(this.supplies.get(Integer.parseInt(s)).getId()), supp.get(s));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
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
        ConcurrentHashMap<String,Integer> supplies;
        List<Product> products;
        String res = "";
        int counter = 0;
        if(Objects.equals(method,"by Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
            for(String supply: supplies.keySet()){
                res+= counter + ".ID: " +supply +" ,Quantity: "+supplies.get(supply)+"\n";
                counter += 1;
            }
        }else{
            try {
                products = this.supplies.getAllProducts();
                for (Product product : products) {
                    res += counter + ".Name: " + product.getName() + ", ID: " + product.getId() + "\n";
                    counter += 1;
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        return res;
    }
    public String showSuppliers(String areacode) {
        int area = Integer.parseInt(areacode);
        List<String> suppliersList = null;
        try {
            suppliersList = suppliers.GetSupplierByArea(area); //ZIV
        } catch (SQLException e) {
            return null;
        }
        if (suppliersList.size() == 0){
            return "No suppliers available, sorry G";
        }
        return ShowSitesOption(suppliersList);

    }


    private String ShowSitesOption(List<String> showSites) {
        int counter = 0;
        String res = "";
        for (int i=0;i<showSites.size();i++){
            res += counter + "." + showSites.get(i) + "\n";
            counter++;
        }
        return res;

    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time)  {
        int docID = id.getAndIncrement();
        ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>>orderList = new ConcurrentHashMap<>();
        for(String storeName:orders.keySet()){
            orderList.put(sites.getSite(storeName),createSupplyList(orders.get(storeName)));
        }
        int supplierid = 0;
        try {
            supplierid = suppliers.getSupplier(Integer.parseInt(supplier)).getSupplierNumber();
        } catch (SQLException e) {
            return null;
        }
        OrderDocument doc = new OrderDocument(String.valueOf(docID),supplierid,orderList,createDate(date), time);
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



    public void changeOrder(String docID, String storeID, ArrayList<String>names,ArrayList<Integer>quantities)  {
        ConcurrentHashMap<String,Integer> newSupplyOrder = new ConcurrentHashMap<>();
        OrderDocument doc =orderDocs.getOrderDoc(docID);
        Store store = sites.getSite(storeID);
        for(int i = 0;i< names.size();i++){
            if(names.size()==quantities.size()){
                try {
                    newSupplyOrder.put(String.valueOf(supplies.get(Integer.parseInt(names.get(i))).getId()),quantities.get(i));
                } catch (SQLException e) {
                    return;
                }
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
        for(Store store : doc.getDestinations().keySet()) {
            int id = driverDocID.getAndIncrement();
            driverDocs.addDriverDoc(new DriverDocument(doc.driver,id,doc.getDestinations().get(store), store,doc.getId()));
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
        ConcurrentHashMap<String,Integer> supplies;
        if(Objects.equals(pred, "By Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
            int counter = 0;
            for(String supp: supplies.keySet()){
                if(counter==idx){
                    return supp;
                }
                counter++;
            }
        }
        else{
            try {
                List<Product> products = this.supplies.getAllProducts();
                for (int i=0; i<products.size(); i++){
                    if(i==idx){
                        return String.valueOf(products.get(i).getId());
                    }
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        return null;
    }
    public HashMap<Integer, Integer> getOrderIdFromOrderDoc(int orderDocId){
        HashMap<Integer, Integer> ans=null;
        try {
            ans = driverDocs.getProductsFromOrderDoc(orderDocId);
        } catch (SQLException exp) {
            System.out.println("didn't work");
        }
        return ans;
    }
    public boolean getAutoTruckandDriver(Date date, String time,Truck truck,Driver driver) {
//        Driver driver = null;
//        Truck truck = null;
        int sevenAfter = 0;
        int sevenBefore = 0;
        while (driver == null && truck == null || (sevenAfter != 7 && sevenBefore != 7)) {
            ArrayList<Driver> drivers = this.drivers.getDrivers(date.toString(), time);
            if (drivers.isEmpty()) {
                if (sevenAfter != 7) {
                    date.AdvanceDate();
                    sevenAfter++;
                } else {
                    date.dayBefore();
                    sevenBefore++;
                }
                continue;
            } else {
                driver = drivers.get(0);
                ArrayList<Truck> trucks = this.trucks.getTrucks(date.toString(), time, driver.getLicense());
                if (trucks.isEmpty()) {
                    if (sevenAfter != 7) {
                        date.AdvanceDate();
                        sevenAfter++;
                    } else {
                        date.dayBefore();
                        sevenBefore++;
                    }
                    continue;
                }
                else{
                    truck = trucks.get(0);
                    break;
                }
            }
        }
        return truck != null && driver != null;

    }
    public boolean createAutoTransport(String supplierNumber, String date, ConcurrentHashMap<String,Integer> supplyList) {
        String time = "MORNING";
        sites.addSite("616",0,1,"Asgard","Thor","050866945");
        Supplier origin = null;
        try {
            origin = suppliers.getSupplier(Integer.parseInt(supplierNumber));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> order = new ConcurrentHashMap<>();
        order.put(defaultStore,supplyList);
        Date date_ = createDate(date);
        Driver driver = null;
        Truck truck = null;
        getAutoTruckandDriver(date_,time,truck,driver);
        if(truck == null || driver == null){
//TODO ADD MESSAGE HERE
            new EmployeeDAO();
            return false;
        }
        OrderDocument d = new OrderDocument(String.valueOf(id.getAndIncrement()),origin.getSupplierNumber(),order,date_,time);
        d.setTruckandDriver(truck,driver);
        orderDocs.addDoc(d);
        createDriverDocs(d.getId());
        d.finishOrder();
        return true;
    }

    public String GetFinish(String docID) {
        return orderDocs.getFinish(docID);
    }
}
