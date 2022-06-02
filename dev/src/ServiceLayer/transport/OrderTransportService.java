package ServiceLayer.transport;

import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.Transport.OrderController;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//Miki//
public class OrderTransportService {
    OrderController orderController = OrderController.getInstance();
    FacadeEmployees_Transports facadeEmployeesTransports = FacadeEmployees_Transports.getInstance();
    public OrderTransportService(){

    }

//    public OrderDoc getDocbyDate(String id, String date){
//        return orderCtrl.getDocbyDate(id,date);
//    }


//    public void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantity) {
//        orderCtrl.changeQuantity(docID, siteID, suppName, quantity);
//    }

    public boolean setTrucksWeight(String docID, double weight2add) {
        return facadeEmployeesTransports.setTrucksWeight(docID, weight2add);

    }

    public String showTrucks(String date, String driverID, String time) {
        return facadeEmployeesTransports.showTrucks(date,driverID,time);
    }

    public void setNewTruck(String docID,String newTruckPlate) {
        facadeEmployeesTransports.setNewTruck(docID, newTruckPlate);
    }

    public String showStores(String areaCode) {
        return facadeEmployeesTransports.showStores(areaCode);
    }

    public void replaceStores(String docID, String id2replace, String newStoreID, ConcurrentHashMap<String, Integer> supplies) {
        facadeEmployeesTransports.replaceStores(docID, id2replace, newStoreID, supplies);
    }



    public String showSupplies() {
        return facadeEmployeesTransports.showSupplies("","","");
    }


    public String showSuppliers(String areaCode) {
        return facadeEmployeesTransports.showSuppliers(areaCode);

    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
       return facadeEmployeesTransports.createDoc(orders, supplier, date,driverID,truckPlate, time);
         }

    public void removeSiteFromDoc(String docID, String siteID) {
        facadeEmployeesTransports.removeSiteFromDoc(docID, siteID);

    }

    public String showDrivers(String d,String time) {
        return facadeEmployeesTransports.showDrivers(d,time);
    }

    public String getDriver(String driverID) {
        return facadeEmployeesTransports.getDriver(driverID);
    }

    public void removeDoc(String doc) {
        facadeEmployeesTransports.removeDoc(doc);
    }

    public String showSuppliesByDoc(String docID, String storeID) {
        return facadeEmployeesTransports.showSupplies(docID, storeID,"by Doc");
    }


    public boolean changeOrder(String docID, String storeID, ArrayList<String> names,ArrayList<Integer> quantities) {
       return facadeEmployeesTransports.changeOrder(docID,storeID,names,quantities);
    }
    public void createDriverDocs(String doc){
        facadeEmployeesTransports.createDriverDocs(doc);
    }
    public void transportIsDone(String doc) {
        facadeEmployeesTransports.transportIsDone(doc);
    }

    public String showDriverDocs(String docID) {
        return facadeEmployeesTransports.ShowDriverDocs(docID);

    }

    public String showStoresForDoc(String docID) {
        return facadeEmployeesTransports.showStoresforDoc(docID);
    }


    public String viewOrder(String docID) {
        return facadeEmployeesTransports.viewOrder(docID);
    }

    public String getSupplyByIdx(int nextInt) {
        return facadeEmployeesTransports.getSupplyByIdx(nextInt,"","","");
          }

    public String getTruck(String licensePlate) {
        return facadeEmployeesTransports.getTruck(licensePlate);
    }

    public String getDate(String docID) {
        return facadeEmployeesTransports.getDate(docID);
    }

    public String getDriverID(String docID) {
        return facadeEmployeesTransports.getDriverID(docID);
    }

    public double getCurrWeight(String doc) {
        return facadeEmployeesTransports.getCurrWeight(doc);
    }

    public boolean getDoc(String docID) {
        return facadeEmployeesTransports.getDoc(docID);
    }

    public String getSupplyByIdAndDoc(int idx,String docID,String storeID) {
        return facadeEmployeesTransports.getSupplyByIdx(idx,docID,storeID,"By Doc");
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
        return facadeEmployeesTransports.getTime(docID);
    }
}
