package ServiceLayer.transport;

import DomainLayer.Transport.OrderCtrl;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//Miki//
public class OrderService {
    OrderCtrl orderCtrl = OrderCtrl.getInstance();
    public OrderService(){

    }

//    public OrderDoc getDocbyDate(String id, String date){
//        return orderCtrl.getDocbyDate(id,date);
//    }


//    public void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantity) {
//        orderCtrl.changeQuantity(docID, siteID, suppName, quantity);
//    }

    public boolean setTrucksWeight(String docID, double weight2add) {
        return orderCtrl.setTrucksWeight(docID, weight2add);
    }

    public String showTrucks(String date, String driverID, String time) {
        return orderCtrl.showTrucks(date,driverID,time);
    }



    public void setNewTruck(String docID,String newTruckPlate) {
        orderCtrl.setNewTruck(docID, newTruckPlate);
    }

    public String showStores(String areacode) {
        return orderCtrl.showStores(areacode);
    }

    public void replaceStores(String docID, String id2replace, String newStoreID, ConcurrentHashMap<String, Integer> supplies) {
        orderCtrl.replaceStores(docID, id2replace, newStoreID, supplies);
    }



    public String showSupplies() {
        return orderCtrl.showSupplies("","","");
    }


    public String showSuppliers(String areacode) {
        return orderCtrl.showSuppliers(areacode);
    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
        return orderCtrl.createDoc(orders, supplier, date,driverID,truckPlate, time);
    }

    public void removeSiteFromDoc(String docID, String siteID) {
        orderCtrl.removeSiteFromDoc(docID, siteID);
    }

    public String showDrivers(String d,String time) {
        return orderCtrl.showDrivers(d,time);
    }

    public String getDriver(String driverID) {
        return orderCtrl.getDriver(driverID);
    }

    public void removeDoc(String doc) {
        orderCtrl.removeDoc(doc);
    }

    public String showSuppliesByDoc(String docID, String storeID) {
        return orderCtrl.showSupplies(docID, storeID,"by Doc");
    }


    public boolean changeOrder(String docID, String storeID, ArrayList<String> names,ArrayList<Integer> quantities) {
        if (orderCtrl.containsSite(docID, storeID)) {
            orderCtrl.changeOrder(docID,storeID, names,quantities);
            return true;
        } else {
            throw new NullPointerException();
        }
    }
    public void createDriverDocs(String doc){
        orderCtrl.createDriverDocs(doc);
    }
    public void transportIsDone(String doc) {
        orderCtrl.transportIsDone(doc);
    }

    public String showDriverDocs(String docID) {
        return orderCtrl.ShowDriverDocs(docID);
    }

    public String showStoresforDoc(String docID) {
        return orderCtrl.showStoresforDoc(docID);
    }


    public String viewOrder(String docID) {
        return orderCtrl.viewOrder(docID);
    }

    public String getSupplyByIdx(int nextInt) {
        return orderCtrl.getSupplyByIdx(nextInt,"","","");
    }

    public String getTruck(String licensePlate) {
        return orderCtrl.getTruck(licensePlate);
    }

    public String getDate(String docID) {
        return orderCtrl.getDate(docID);
    }

    public String getDriverID(String docID) {
        return orderCtrl.getDriverID(docID);
    }

    public double getCurrWeight(String doc) {
        return orderCtrl.getCurrWeight(doc);
    }

    public boolean getDoc(String docID) {
        return orderCtrl.getDoc(docID);
    }

    public String getSupplyByIdxandDoc(int idx,String docID,String storeID) {
        return orderCtrl.getSupplyByIdx(idx,docID,storeID,"By Doc");
    }

    public boolean validhour(String time) {
        String[] helper = time.split(":");
        int hour = Integer.parseInt(helper[0]);
        int min = Integer.parseInt(helper[1]);
        return (0 <= hour && hour <= 24) && ((0 <= min &&  min <= 60));

    }

    public void build() {
        orderCtrl.build();
    }

    public String getTime(String docID) {
        return orderCtrl.getTime(docID);
    }
}
