package ServiceLayer.transport;

import DomainLayer.Transport.OrderController;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//Miki//
public class OrderTransportService {
    OrderController orderController = OrderController.getInstance();
    public OrderTransportService(){

    }

//    public OrderDoc getDocbyDate(String id, String date){
//        return orderCtrl.getDocbyDate(id,date);
//    }


//    public void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantity) {
//        orderCtrl.changeQuantity(docID, siteID, suppName, quantity);
//    }

    public boolean setTrucksWeight(String docID, double weight2add) {
        return orderController.setTrucksWeight(docID, weight2add);
    }

    public String showTrucks(String date, String driverID, String time) {
        return orderController.showTrucks(date,driverID,time);
    }



    public void setNewTruck(String docID,String newTruckPlate) {
        orderController.setNewTruck(docID, newTruckPlate);
    }

    public String showStores(String areacode) {
        return orderController.showStores(areacode);
    }

    public void replaceStores(String docID, String id2replace, String newStoreID, ConcurrentHashMap<String, Integer> supplies) {
        orderController.replaceStores(docID, id2replace, newStoreID, supplies);
    }



    public String showSupplies() {
        return orderController.showSupplies("","","");
    }


    public String showSuppliers(String areacode) {
        return orderController.showSuppliers(areacode);
    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
        return orderController.createDoc(orders, supplier, date,driverID,truckPlate, time);
    }

    public void removeSiteFromDoc(String docID, String siteID) {
        orderController.removeSiteFromDoc(docID, siteID);
    }

    public String showDrivers(String d,String time) {
        return orderController.showDrivers(d,time);
    }

    public String getDriver(String driverID) {
        return orderController.getDriver(driverID);
    }

    public void removeDoc(String doc) {
        orderController.removeDoc(doc);
    }

    public String showSuppliesByDoc(String docID, String storeID) {
        return orderController.showSupplies(docID, storeID,"by Doc");
    }


    public boolean changeOrder(String docID, String storeID, ArrayList<String> names,ArrayList<Integer> quantities) {
        if (orderController.containsSite(docID, storeID)) {
            orderController.changeOrder(docID,storeID, names,quantities);
            return true;
        } else {
            throw new NullPointerException();
        }
    }
    public void createDriverDocs(String doc){
        orderController.createDriverDocs(doc);
    }
    public void transportIsDone(String doc) {
        orderController.transportIsDone(doc);
    }

    public String showDriverDocs(String docID) {
        return orderController.ShowDriverDocs(docID);
    }

    public String showStoresforDoc(String docID) {
        return orderController.showStoresforDoc(docID);
    }


    public String viewOrder(String docID) {
        return orderController.viewOrder(docID);
    }

    public String getSupplyByIdx(int nextInt) {
        return orderController.getSupplyByIdx(nextInt,"","","");
    }

    public String getTruck(String licensePlate) {
        return orderController.getTruck(licensePlate);
    }

    public String getDate(String docID) {
        return orderController.getDate(docID);
    }

    public String getDriverID(String docID) {
        return orderController.getDriverID(docID);
    }

    public double getCurrWeight(String doc) {
        return orderController.getCurrWeight(doc);
    }

    public boolean getDoc(String docID) {
        return orderController.getDoc(docID);
    }

    public String getSupplyByIdxandDoc(int idx,String docID,String storeID) {
        return orderController.getSupplyByIdx(idx,docID,storeID,"By Doc");
    }

    public boolean validHour(String time) {
        String[] helper = time.split(":");
        int hour = Integer.parseInt(helper[0]);
        int min = Integer.parseInt(helper[1]);
        return (0 <= hour && hour <= 24) && ((0 <= min &&  min <= 60));

    }

    public void build() {
        //orderController.build();
    }

    public String getTime(String docID) {
        return orderController.getTime(docID);
    }
}
