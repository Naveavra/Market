package ServiceLayer.transport;

import DomainLayer.FacadeEmployees;
import DomainLayer.Transport.OrderController;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//Miki//
public class OrderTransportService {
    OrderController orderController = OrderController.getInstance();
    FacadeEmployees facadeEmployees = FacadeEmployees.getInstance();
    public OrderTransportService(){

    }

//    public OrderDoc getDocbyDate(String id, String date){
//        return orderCtrl.getDocbyDate(id,date);
//    }


//    public void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantity) {
//        orderCtrl.changeQuantity(docID, siteID, suppName, quantity);
//    }

    public boolean setTrucksWeight(String docID, double weight2add) {
        return facadeEmployees.setTrucksWeight(docID, weight2add);

    }

    public String showTrucks(String date, String driverID, String time) {
        return facadeEmployees.showTrucks(date,driverID,time);
    }

    public void setNewTruck(String docID,String newTruckPlate) {
        facadeEmployees.setNewTruck(docID, newTruckPlate);
    }

    public String showStores(String areaCode) {
        return facadeEmployees.showStores(areaCode);
    }

    public void replaceStores(String docID, String id2replace, String newStoreID, ConcurrentHashMap<String, Integer> supplies) {
        facadeEmployees.replaceStores(docID, id2replace, newStoreID, supplies);
    }



    public String showSupplies() {
        return facadeEmployees.showSupplies("","","");
    }


    public String showSuppliers(String areaCode) {
        return facadeEmployees.showSuppliers(areaCode);

    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
       return facadeEmployees.createDoc(orders, supplier, date,driverID,truckPlate, time);
         }

    public void removeSiteFromDoc(String docID, String siteID) {
        facadeEmployees.removeSiteFromDoc(docID, siteID);

    }

    public String showDrivers(String d,String time) {
        return facadeEmployees.showDrivers(d,time);
    }

    public String getDriver(String driverID) {
        return facadeEmployees.getDriver(driverID);
    }

    public void removeDoc(String doc) {
        facadeEmployees.removeDoc(doc);
    }

    public String showSuppliesByDoc(String docID, String storeID) {
        return facadeEmployees.showSupplies(docID, storeID,"by Doc");
    }


    public boolean changeOrder(String docID, String storeID, ArrayList<String> names,ArrayList<Integer> quantities) {
       return facadeEmployees.changeOrder(docID,storeID,names,quantities);
    }
    public void createDriverDocs(String doc){
        facadeEmployees.createDriverDocs(doc);
    }
    public void transportIsDone(String doc) {
        facadeEmployees.transportIsDone(doc);
    }

    public String showDriverDocs(String docID) {
        return facadeEmployees.ShowDriverDocs(docID);

    }

    public String showStoresForDoc(String docID) {
        return facadeEmployees.showStoresforDoc(docID);
    }


    public String viewOrder(String docID) {
        return facadeEmployees.viewOrder(docID);
    }

    public String getSupplyByIdx(int nextInt) {
        return facadeEmployees.getSupplyByIdx(nextInt,"","","");
          }

    public String getTruck(String licensePlate) {
        return facadeEmployees.getTruck(licensePlate);
    }

    public String getDate(String docID) {
        return facadeEmployees.getDate(docID);
    }

    public String getDriverID(String docID) {
        return facadeEmployees.getDriverID(docID);
    }

    public double getCurrWeight(String doc) {
        return facadeEmployees.getCurrWeight(doc);
    }

    public boolean getDoc(String docID) {
        return facadeEmployees.getDoc(docID);
    }

    public String getSupplyByIdAndDoc(int idx,String docID,String storeID) {
        return facadeEmployees.getSupplyByIdx(idx,docID,storeID,"By Doc");
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
        return facadeEmployees.getTime(docID);
    }
}
